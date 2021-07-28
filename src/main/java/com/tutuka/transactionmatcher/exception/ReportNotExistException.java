package com.tutuka.transactionmatcher.exception;

import lombok.Getter;

@Getter
public final class ReportNotExistException extends RuntimeException {

    public static final String ERR_CODE = "TMBE001";
    private static final String ERR_MSG = "Report does not exist.";

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg() {
        return ERR_MSG;
    }
}
