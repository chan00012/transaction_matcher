package com.tutuka.transactionmatcher.entity;

import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import lombok.Data;

@Data
public class ReportEntity {

    private MatchReport matchReport;
    private UnmatchedReport unmatchedReport;
    private String referenceFileName;
    private String compareFileName;
    private ReportStatus status;
    private String failMsg;

}
