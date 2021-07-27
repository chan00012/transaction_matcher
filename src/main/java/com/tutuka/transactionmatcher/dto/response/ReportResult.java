package com.tutuka.transactionmatcher.dto.response;

import lombok.Data;

@Data
public final class ReportResult {

    private UnmatchedReport unmatchedReport;
    private MatchReport matchReport;
}
