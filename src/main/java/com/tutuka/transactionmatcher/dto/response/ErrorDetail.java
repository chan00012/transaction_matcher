package com.tutuka.transactionmatcher.dto.response;

import com.tutuka.transactionmatcher.utils.Utilities;
import lombok.*;

@Data
@Builder
public class ErrorDetail {

    @Builder.Default
    private String reference = Utilities.generateErrRefNumber();
    private String message;
}
