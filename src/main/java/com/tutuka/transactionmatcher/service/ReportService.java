package com.tutuka.transactionmatcher.service;

import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import com.tutuka.transactionmatcher.entity.Transaction;

import java.util.List;

public interface ReportService {

    ReportReferenceNumber generate(List<Transaction> t1, List<Transaction> t2);

    MatchReport getMatchReport(String rrn);

    UnmatchedReport getUnmatchReport(String rrn);
}
