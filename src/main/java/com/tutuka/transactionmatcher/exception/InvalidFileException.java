package com.tutuka.transactionmatcher.exception;

import lombok.Getter;

@Getter
public class InvalidFileException extends RuntimeException {

    public static final String ERR_CODE = "TMBE002";
    private static final String ERR_MSG = "Invalid file.";

    public InvalidFileException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return ERR_CODE;
    }

    public String getErrMsg() {
        return ERR_MSG;
    }
}
