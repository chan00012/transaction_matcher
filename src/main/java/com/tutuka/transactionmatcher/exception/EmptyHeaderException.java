package com.tutuka.transactionmatcher.exception;

public class EmptyHeaderException extends RuntimeException {

    public static final String ERR_CODE = "TMBE004";
    private static final String ERR_MSG = "Empty header file.";

    public EmptyHeaderException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg() {
        return ERR_MSG;
    }
}
