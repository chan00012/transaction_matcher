package com.tutuka.transactionmatcher.controller;

import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

public interface ReportApi {

    @ApiOperation(value = "Generate and evaluate two csv files", response = ReportReferenceNumber.class, tags = "Generate Report API")
    Response<ReportReferenceNumber> generateReport(@ApiParam(value = "Reference csv file", required = true) MultipartFile refFile,
                                                   @ApiParam(value = "Compared csv file", required = true)MultipartFile comFile);

    @ApiOperation(value = "Get match report by report reference number", response = MatchReport.class, tags = "Get Match Report API")
    Response<MatchReport> getMatchReport(@ApiParam(value = "The rrn generated from Generate API", required = true) String rrn);

    @ApiOperation(value = "Get unmatched report by report reference number", response = MatchReport.class, tags = "Get Unmatched Report API")
    Response<UnmatchedReport> getUnMatchReport(@ApiParam(value = "The rrn generated from Generate API", required = true) String rrn);
}
