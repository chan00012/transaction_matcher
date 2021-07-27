package com.tutuka.transactionmatcher.exception.advisor;

import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.exception.ReportNotExistException;
import com.tutuka.transactionmatcher.exception.ReportStatusException;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ReportStatusException.class})
    protected ResponseEntity<Object> handleConflict(ReportStatusException rse, WebRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("rrn", rse.getRrn());
        data.put("reportStatus", rse.getReportStatus());
        data.put("message", rse.getReportStatus().getMessage());

        HttpStatus status;
        Response<Object> response;
        if (rse.getReportStatus() == ReportStatus.PENDING) {
            status = HttpStatus.ACCEPTED;
            response = Response.ok(data);
        } else {
            status = HttpStatus.CONFLICT;
            response = Response.fail(data, ReportStatusException.ERR_CODE);
        }

        return handleExceptionInternal(rse, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {ReportNotExistException.class})
    protected ResponseEntity<Object> handleConflict(ReportNotExistException rne, WebRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "report does not exist.");
        return handleExceptionInternal(rne, Response.fail(data, ReportNotExistException.ERR_CODE), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
