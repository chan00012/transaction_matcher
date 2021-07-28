package com.tutuka.transactionmatcher.matcher;

import com.tutuka.transactionmatcher.dto.response.AdaptiveMatchResponse;
import com.tutuka.transactionmatcher.dto.response.DiscrepancyMatchedTransaction;
import com.tutuka.transactionmatcher.dto.response.EvaluatedTransaction;
import com.tutuka.transactionmatcher.entity.TaggedTransaction;
import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdaptiveMatch {

    private static final int MAX_MATCH = 2;

    private final ApplicationContext context;

    public AdaptiveMatchResponse match(Set<TaggedTransaction> refTagTxnSet, Set<TaggedTransaction> comTagTxnSet) {
        log.info("Performing adaptive match. reference transactions size: {}, compared transaction size: {}",
                refTagTxnSet.size(), comTagTxnSet.size());

        List<DiscrepancyMatchedTransaction> discrepancyMatchedTransactions = new ArrayList<>();
        List<TaggedTransaction> qualifiedMatchedTransactions = new ArrayList<>();
        Set<TaggedTransaction> qualifiedRefTaxTxnSet = new HashSet<>();
        Set<TaggedTransaction> qualifiedComTaxTxnSet = new HashSet<>();

        for (TaggedTransaction refTagTxn : refTagTxnSet) {
            List<EvaluatedTransaction> possibleMatchTxns = new ArrayList<>();

            for (TaggedTransaction comTagTxn : comTagTxnSet) {
                Transaction txn1 = refTagTxn.getTransaction();
                Transaction txn2 = comTagTxn.getTransaction();
                TaggedTransaction tagTxn = new TaggedTransaction(txn1);

                tagTxn.addAllTag(negativeMatch(txn1, txn2));

                if (refTagTxn.getTags().contains(Tag.DUPLICATE_REFERENCE)) {
                    tagTxn.addAllTag(refTagTxn.getTags());
                }

                if (comTagTxn.getTags().contains(Tag.DUPLICATE_COMPARE)) {
                    tagTxn.addAllTag(comTagTxn.getTags());
                }

                if (tagTxn.getTags().size() <= MAX_MATCH && !tagTxn.getTags().isEmpty()) {

                    EvaluatedTransaction evalRefTxn = EvaluatedTransaction.builder()
                            .source(refTagTxn.getSource())
                            .count(refTagTxn.getCount())
                            .transaction(txn1)
                            .build();

                    EvaluatedTransaction evalComTxn = EvaluatedTransaction.builder()
                            .source(comTagTxn.getSource())
                            .count(comTagTxn.getCount())
                            .transaction(txn2)
                            .discrepancies(Tag.populateDiscrepancies(tagTxn.getTags()))
                            .build();

                    possibleMatchTxns.add(evalComTxn);
                    DiscrepancyMatchedTransaction dmt = DiscrepancyMatchedTransaction.builder()
                            .referenceTransaction(evalRefTxn)
                            .possibleMatchTransactions(possibleMatchTxns)
                            .build();

                    log.trace("Transaction id: {} has been matched adaptively. number of possible match(es): {}",
                            evalRefTxn.getTransaction().getTransactionId(),
                            possibleMatchTxns.size());
                    discrepancyMatchedTransactions.add(dmt);
                }

                if (tagTxn.getTags().isEmpty()) {
                    qualifiedMatchedTransactions.add(refTagTxn);
                    qualifiedRefTaxTxnSet.add(refTagTxn);
                    qualifiedComTaxTxnSet.add(comTagTxn);
                }
            }
        }

        refTagTxnSet.removeAll(qualifiedRefTaxTxnSet);
        comTagTxnSet.removeAll(qualifiedComTaxTxnSet);
        return AdaptiveMatchResponse.builder()
                .discrepancyMatchedTransactions(discrepancyMatchedTransactions)
                .qualifiedMatchedTransactions(qualifiedMatchedTransactions)
                .build();
    }

    private Set<Tag> negativeMatch(Transaction refTxn, Transaction comTxn) {
        Set<Tag> tags = new HashSet<>();
        List<AbstractAdaptiveMatcher> adaptiveMatchers = new ArrayList<>(context.getBeansOfType(AbstractAdaptiveMatcher.class).values());
        adaptiveMatchers.forEach(am -> am.addTag(refTxn, comTxn, tags));
        return tags;
    }
}
