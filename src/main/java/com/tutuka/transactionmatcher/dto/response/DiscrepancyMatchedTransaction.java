package com.tutuka.transactionmatcher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class DiscrepancyMatchedTransaction {

    private EvaluatedTransaction referenceTransaction;
    private List<EvaluatedTransaction> possibleMatchTransactions;

}
