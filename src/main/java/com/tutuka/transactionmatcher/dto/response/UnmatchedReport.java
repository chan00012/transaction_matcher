package com.tutuka.transactionmatcher.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UnmatchedReport {

    private List<DiscrepancyMatchedTransaction> discrepancyMatchedTransactions;
    private List<EvaluatedTransaction> noMatchTransactions;
}
