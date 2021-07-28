package com.tutuka.transactionmatcher.utils;

public class Constants {

    public static final String SUCCESS = "Success";
    public static final String ERROR = "Error";
    public static final String SUCCESS_CODE = "0";

    public static final String JSON_STATUS = "$.status";
    public static final String JSON_CODE = "$.code";
    public static final String JSON_DATA = "$.data";
    public static final String JSON_RRN = "$.data.rrn";
    public static final String JSON_ERROR = "$.error";
    public static final String JSON_ERR_MSG = "$.error.message";
    public static final String JSON_DATA_MSG = "$.data.message";
    public static final String JSON_ERR_REF = "$.error.reference";
    public static final String JSON_DATA_REF = "$.data.reference";

    public static final String FAILED_REPORT_ERRCODE = "TMBE000";
    public static final String NON_EXISTITING_ERRCODE = "TMBE001";
    public static final String INVALID_FILETYPE_ERRCODE = "TMBE002";
    public static final String UNRECOG_COLUMN_ERRCODE = "TMBE003";
}
