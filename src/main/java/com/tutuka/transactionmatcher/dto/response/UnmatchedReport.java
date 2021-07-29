package com.tutuka.transactionmatcher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UnmatchedReport {

    private List<DiscrepancyMatchedTransaction> discrepancyMatchedTransactions;
    private List<EvaluatedTransaction> noMatchTransactions;
}
