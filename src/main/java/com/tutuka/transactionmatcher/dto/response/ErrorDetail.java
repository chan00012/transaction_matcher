package com.tutuka.transactionmatcher.dto.response;

import com.tutuka.transactionmatcher.utils.Utilities;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetail {

    @Builder.Default
    private String reference = Utilities.generateErrRefNumber();
    private String message;
}
