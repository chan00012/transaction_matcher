package com.tutuka.transactionmatcher.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutuka.transactionmatcher.dto.response.*;
import com.tutuka.transactionmatcher.entity.ReportEntity;
import com.tutuka.transactionmatcher.entity.TaggedTransaction;
import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.exception.ReportNotExistException;
import com.tutuka.transactionmatcher.exception.ReportStatusException;
import com.tutuka.transactionmatcher.matcher.AdaptiveMatch;
import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.service.ReportService;
import com.tutuka.transactionmatcher.utils.Utilities;
import com.tutuka.transactionmatcher.utils.enums.Origin;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AdaptiveMatch adaptiveMatch;

    @Override
    public ReportReferenceNumber generate(List<Transaction> t1, List<Transaction> t2) {


        ReportReferenceNumber reportReferenceNumber = new ReportReferenceNumber();
        reportReferenceNumber.setRrn(Utilities.generateReferenceNumber());
        CompletableFuture.runAsync(() -> {
            try {
                evaluate(t1, t2, reportReferenceNumber.getRrn());
            } catch (Exception e) {
                log.error("Exception occurred on async call - errorMessage: {}", e.getMessage());
                ReportEntity re = reportRepository.get(reportReferenceNumber.getRrn());
                re.setStatus(ReportStatus.FAILED);
                re.setFailMsg(e.getMessage());
            }
        });

        return reportReferenceNumber;
    }

    @Override
    public MatchReport getMatchReport(String rrn) {
        ReportEntity re = reportRepository.get(rrn);

        if (Objects.isNull(re)) {
            throw new ReportNotExistException();
        } else if (re.getStatus() != ReportStatus.SUCCESS) {
            throw new ReportStatusException(rrn, re.getStatus());
        } else {
            return re.getMatchReport();
        }
    }

    @Override
    public UnmatchedReport getUnmatchReport(String rrn) {
        ReportEntity re = reportRepository.get(rrn);

        if (Objects.isNull(re)) {
            throw new ReportNotExistException();
        } else if (re.getStatus() != ReportStatus.SUCCESS) {
            throw new ReportStatusException(rrn, re.getStatus());
        } else {
            return re.getUnmatchedReport();
        }
    }

    private void evaluate(List<Transaction> t1, List<Transaction> t2, String rrn) {
        log.info("Start evaluating transactions of report reference number: {}", rrn);
        Set<TaggedTransaction> refTagTxnSet = getInitialAndTagDuplicateTransactions(t1, Origin.REFERENCE);
        Set<TaggedTransaction> comTagTxnSet = getInitialAndTagDuplicateTransactions(t2, Origin.COMPARE);
        Set<TaggedTransaction> matchedTransactionSet = new HashSet<>(refTagTxnSet);

        matchedTransactionSet.retainAll(comTagTxnSet);
        log.info("Initial matched transactions size: {}", matchedTransactionSet.size());

        refTagTxnSet.removeAll(matchedTransactionSet);
        comTagTxnSet.removeAll(matchedTransactionSet);

        AdaptiveMatchResponse adMatch = adaptiveMatch.match(refTagTxnSet, comTagTxnSet);
        matchedTransactionSet.addAll(adMatch.getQualifiedMatchedTransactions());
        List<EvaluatedTransaction> noMatchTxns = getNoMatchedTransactions(refTagTxnSet, comTagTxnSet);

        log.info("Transactions with qualified discrepancies match: {}", adMatch.getDiscrepancyMatchedTransactions().size());
        log.info("Transactions with qualified match: {}", adMatch.getQualifiedMatchedTransactions().size());
        log.info("Transactions with no qualified match: {}", noMatchTxns.size());
        log.info("Updated matched transactions size: {}", matchedTransactionSet.size());

        UnmatchedReport unmatchedReport = UnmatchedReport.builder()
                .discrepancyMatchedTransactions(adMatch.getDiscrepancyMatchedTransactions())
                .noMatchTransactions(noMatchTxns)
                .build();

        MatchReport matchReport = generateMatchReport(t1, t2, matchedTransactionSet);
        reportRepository.update(rrn, unmatchedReport);
        reportRepository.update(rrn, matchReport);

        log.info("Done report evaluation and storage updated, report reference number: {}", rrn);
    }

    private List<EvaluatedTransaction> getNoMatchedTransactions(Set<TaggedTransaction> refTagTxnSet,
                                                                Set<TaggedTransaction> comTagTxnSet) {

        comTagTxnSet.addAll(refTagTxnSet);
        return comTagTxnSet.stream()
                .map(tt -> EvaluatedTransaction.builder()
                        .discrepancies(Tag.populateDiscrepancies(Collections.singleton(Tag.NO_MATCH)))
                        .count(tt.getCount())
                        .source(tt.getSource())
                        .transaction(tt.getTransaction())
                        .build())
                .collect(Collectors.toList());
    }

    private Set<TaggedTransaction> getInitialAndTagDuplicateTransactions(List<Transaction> txn, Origin origin) {
        Set<TaggedTransaction> tts = new HashSet<>();

        for (Transaction t : txn) {
            TaggedTransaction tagTxn = new TaggedTransaction(t);
            tagTxn.setSource(origin.name());
            if (Collections.frequency(txn, t) > 1) {
                tagTxn.setCount(Collections.frequency(txn, t));
                tagTxn.addTag(origin.getTag());

                log.trace("Transaction id: {} has been marked as duplicate. row_Num: {}, source: {}",
                        t.getTransactionId(), t.getRowNum(), origin);
            }
            tts.add(tagTxn);
        }

        return tts;
    }

    private MatchReport generateMatchReport(List<Transaction> t1, List<Transaction> t2,
                                            Set<TaggedTransaction> matchedTransactions) {
        FileResult file1 = FileResult.builder()
                .totalRecords(t1.size())
                .matchingRecords(matchedTransactions.size())
                .unmatchedRecords(t1.size() - matchedTransactions.size())
                .build();

        FileResult file2 = FileResult.builder()
                .totalRecords(t2.size())
                .matchingRecords(matchedTransactions.size())
                .unmatchedRecords(t2.size() - matchedTransactions.size())
                .build();

        return MatchReport.builder()
                .file1(file1)
                .file2(file2)
                .build();
    }
}
