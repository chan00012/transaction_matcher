package com.tutuka.transactionmatcher.exception;

public class InternalServerException extends RuntimeException {

    public static final String ERR_CODE = "TMIE999";
    public static final String ERR_MSG = "Internal error, please try again later.";

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg() {
        return ERR_MSG;
    }
}
