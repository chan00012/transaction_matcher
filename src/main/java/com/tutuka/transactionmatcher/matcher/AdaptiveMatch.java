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
            DiscrepancyMatchedTransaction dmt = createBaseDMT(refTagTxn, possibleMatchTxns);

            for (TaggedTransaction comTagTxn : comTagTxnSet) {
                Transaction txn1 = refTagTxn.getTransaction();
                Transaction txn2 = comTagTxn.getTransaction();
                Set<Tag> extractedTags = getDiscrepancyTags(txn1, txn2);
                extractedTags.addAll(refTagTxn.getTags());
                extractedTags.addAll(comTagTxn.getTags());

                if (extractedTags.size() <= MAX_MATCH && !extractedTags.isEmpty()) {
                    EvaluatedTransaction evalComTxn = EvaluatedTransaction.builder()
                            .source(comTagTxn.getSource())
                            .count(comTagTxn.getCount())
                            .transaction(txn2)
                            .discrepancies(Tag.populateDiscrepancies(extractedTags))
                            .build();

                    log.trace("Transaction id: {} has been matched adaptively. number of possible match(es): {}",
                            txn1.getTransactionId(),
                            possibleMatchTxns.size());

                    possibleMatchTxns.add(evalComTxn);
                    discrepancyMatchedTransactions.add(dmt);
                    qualifiedRefTaxTxnSet.add(refTagTxn);
                    qualifiedComTaxTxnSet.add(comTagTxn);
                }

                if (extractedTags.isEmpty()) {
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

    private DiscrepancyMatchedTransaction createBaseDMT(TaggedTransaction refTagTxn, List<EvaluatedTransaction> possibleMatchTxns) {
        return DiscrepancyMatchedTransaction.builder()
                .referenceTransaction(EvaluatedTransaction.builder()
                        .source(refTagTxn.getSource())
                        .count(refTagTxn.getCount())
                        .transaction(refTagTxn.getTransaction())
                        .build())
                .possibleMatchTransactions(possibleMatchTxns)
                .build();
    }

    private Set<Tag> getDiscrepancyTags(Transaction refTxn, Transaction comTxn) {
        Set<Tag> tags = new HashSet<>();
        List<AbstractAdaptiveMatcher> adaptiveMatchers = new ArrayList<>(context.getBeansOfType(AbstractAdaptiveMatcher.class).values());
        adaptiveMatchers.forEach(am -> am.addTag(refTxn, comTxn, tags));
        return tags;
    }
}
