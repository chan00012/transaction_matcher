package com.tutuka.transactionmatcher.controller;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.csv.CsvMappingException;
import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.exception.EmptyHeaderException;
import com.tutuka.transactionmatcher.exception.InvalidFileException;
import com.tutuka.transactionmatcher.exception.UnrecognizedColumnException;
import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.service.ReportService;
import com.tutuka.transactionmatcher.utils.CsvUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
public class ReportController implements ReportApi {

    private final ReportService reportsService;
    private final ReportRepository reportRepository;

    @PostMapping(value = "/generate")
    public Response<ReportReferenceNumber> generateReport(@RequestPart("referenceFile") MultipartFile refFile,
                                                          @RequestPart("compareFile") MultipartFile comFile)  {
        log.info("Generate report API invoked - request: {}, {}",
                refFile.getOriginalFilename(), comFile.getOriginalFilename());

        try {
            List<Transaction> t1 = CsvUtil.read(refFile.getInputStream());
            List<Transaction> t2 = CsvUtil.read(comFile.getInputStream());
            ReportReferenceNumber reportReferenceNumber = reportsService.generate(t1, t2);
            reportRepository.saveInitial(reportReferenceNumber.getRrn(), refFile, comFile);

            return Response.ok(reportReferenceNumber);
        } catch (IOException e) {
            if (e instanceof UnrecognizedPropertyException) {
                throw new UnrecognizedColumnException(e);
            } else if (e instanceof CsvMappingException) {
                throw new EmptyHeaderException(e);
            } else {
                throw new InvalidFileException(e);
            }
        }
    }


    @GetMapping("/match")
    public Response<MatchReport> getMatchReport(@RequestParam String rrn) {
        log.info("Get match report API invoked - rrn: {}", rrn);
        MatchReport matchReport = reportsService.getMatchReport(rrn);
        return Response.ok(matchReport);
    }

    @GetMapping("/unmatch")
    public Response<UnmatchedReport> getUnMatchReport(@RequestParam String rrn) {
        log.info("Get unmatch report API invoked - rrn: {}", rrn);
        UnmatchedReport unmatchedReport = reportsService.getUnmatchReport(rrn);
        return Response.ok(unmatchedReport);
    }
}

