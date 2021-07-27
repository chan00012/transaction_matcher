package com.tutuka.transactionmatcher.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public final class Response<T> {

    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";
    private static final String WARNING = "Warning";
    private static final String SUCCESS_CODE = "0";

    private String status;
    private String code;
    private String message;
    private T data;
    private T error;


    public static <T> Response<T> ok(T t) {
        Response<T> response = new Response<>();
        response.setCode(SUCCESS_CODE);
        response.setStatus(SUCCESS);
        response.setData(t);
        return response;
    }

    public static <T> Response<T> fail(T t, String errCode) {
        Response<T> response = new Response<>();
        response.setCode(errCode);
        response.setStatus(ERROR);
        response.setError(t);
        return response;
    }
}
