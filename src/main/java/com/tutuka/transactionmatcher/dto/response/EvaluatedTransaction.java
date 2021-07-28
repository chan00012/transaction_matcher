package com.tutuka.transactionmatcher.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluatedTransaction {

    private Integer count;
    private String source;
    private Transaction transaction;
    private Map<Tag, String> discrepancies;

}
