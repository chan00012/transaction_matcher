package com.tutuka.transactionmatcher.utils.enums;

import lombok.Getter;

@Getter
public enum ReportStatus {
    SUCCESS("success"),
    FAIL("report generation failed."),
    PENDING("report is still pending.");

    final String message;


    ReportStatus(String message) {
        this.message = message;
    }
}
