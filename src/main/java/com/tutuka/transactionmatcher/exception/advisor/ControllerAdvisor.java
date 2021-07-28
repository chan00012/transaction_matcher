package com.tutuka.transactionmatcher.exception.advisor;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.tutuka.transactionmatcher.dto.response.ErrorDetail;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.exception.InvalidFileException;
import com.tutuka.transactionmatcher.exception.ReportNotExistException;
import com.tutuka.transactionmatcher.exception.ReportStatusException;
import com.tutuka.transactionmatcher.exception.UnrecognizedColumnException;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ReportStatusException.class})
    protected ResponseEntity<Object> handleReportStatusConflict(ReportStatusException rse, WebRequest request) {

        if (rse.getReportStatus() == ReportStatus.PENDING) {
            ErrorDetail errorDetail = ErrorDetail.builder().message(rse.getPenMsg()).build();
            log.warn("Accepted but still processing - reference:{}, rrn: {}", errorDetail.getReference(), rse.getRrn());
            return handleExceptionInternal(rse, Response.ok(errorDetail), new HttpHeaders(), HttpStatus.ACCEPTED, request);
        } else {
            ErrorDetail errorDetail = ErrorDetail.builder().message(rse.getErrMsg()).build();
            log.error("Error encountered - reference:{}, code: {}, rrn: {}", errorDetail.getReference(), rse.getCode(), rse.getRrn());
            return handleExceptionInternal(rse, Response.fail(errorDetail, rse.getCode()), new HttpHeaders(), HttpStatus.CONFLICT, request);
        }

    }

    @ExceptionHandler(value = {ReportNotExistException.class})
    protected ResponseEntity<Object> handleReportExistenceConflict(ReportNotExistException rne, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder().message(rne.getErrMsg()).build();
        log.error("Error encountered - reference: {}, code: {}", errorDetail.getReference(), rne.getCode());
        return handleExceptionInternal(rne, Response.fail(errorDetail, rne.getCode()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {InvalidFileException.class, UnrecognizedColumnException.class})
    protected ResponseEntity<Object> handleFileConflict(RuntimeException re, WebRequest request) {
        ErrorDetail errorDetail;
        String code;

        if (re instanceof UnrecognizedColumnException) {
            UnrecognizedColumnException uce = (UnrecognizedColumnException) re;
            UnrecognizedPropertyException upe = (UnrecognizedPropertyException) uce.getCause();
            code = uce.getCode();
            errorDetail = ErrorDetail.builder().message(uce.getErrMsg(upe.getPropertyName())).build();
        } else {
            InvalidFileException ife = (InvalidFileException) re;
            code = ife.getCode();
            errorDetail = ErrorDetail.builder().message(ife.getErrMsg()).build();
        }

        log.error("Error encountered - reference: {}, code: {}, errMsg: {}", errorDetail.getReference(), code, re.getMessage());
        return handleExceptionInternal(re, Response.fail(errorDetail, code), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
