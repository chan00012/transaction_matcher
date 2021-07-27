package com.tutuka.transactionmatcher.exception;

import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import lombok.Getter;

@Getter
public class ReportStatusException extends RuntimeException {

    public static final String ERR_CODE = "TMBE000";
    private final String rrn;
    private final ReportStatus reportStatus;

    public ReportStatusException(String rrn, ReportStatus reportStatus) {
        this.rrn = rrn;
        this.reportStatus = reportStatus;
    }
}
