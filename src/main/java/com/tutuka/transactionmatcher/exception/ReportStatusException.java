package com.tutuka.transactionmatcher.exception;

import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import lombok.Getter;

@Getter
public final class ReportStatusException extends RuntimeException {

    private static final String ERR_CODE = "TMBE000";
    private static final String ERR_MSG = "Report generation failed.";
    private static final String PEN_MSG = "report is still pending.";

    private final String rrn;
    private final ReportStatus reportStatus;

    public ReportStatusException(String rrn, ReportStatus reportStatus) {
        this.rrn = rrn;
        this.reportStatus = reportStatus;
    }

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg() {
        return ERR_MSG;
    }

    public String getPenMsg() {
        return PEN_MSG;
    }
}
