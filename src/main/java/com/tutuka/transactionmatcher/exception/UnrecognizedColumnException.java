package com.tutuka.transactionmatcher.exception;

public class UnrecognizedColumnException extends RuntimeException {

    public static final String ERR_CODE = "TMBE003";
    public static final String ERR_MSG = "Invalid column. [%s]";

    public UnrecognizedColumnException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg(String column) {
        return String.format(ERR_MSG, column);
    }
}
