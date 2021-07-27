package com.tutuka.transactionmatcher.repository;

import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import com.tutuka.transactionmatcher.entity.ReportEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ReportRepository {

    void saveInitial(String rrn, MultipartFile refFile, MultipartFile comFile);

    void update(String rrn, UnmatchedReport unmatchedReport);

    void update(String rrn, MatchReport matchReport);

    ReportEntity get(String rrn);
}
