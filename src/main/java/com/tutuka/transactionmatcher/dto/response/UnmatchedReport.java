package com.tutuka.transactionmatcher.dto.response;

import com.tutuka.transactionmatcher.entity.TaggedTransaction;
import lombok.Data;

import java.util.List;

@Data
public final class UnmatchedReport {

    private List<DiscrepancyMatchedTransaction> discrepancyMatchedTransactions;
    private List<EvaluatedTransaction> noMatchTransactions;
}
