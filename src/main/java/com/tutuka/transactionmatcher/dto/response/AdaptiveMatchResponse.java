package com.tutuka.transactionmatcher.dto.response;

import com.tutuka.transactionmatcher.entity.TaggedTransaction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdaptiveMatchResponse {

    private List<DiscrepancyMatchedTransaction> discrepancyMatchedTransactions;
    private List<TaggedTransaction> qualifiedMatchedTransactions;
}
