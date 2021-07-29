package com.tutuka.transactionmatcher.repository.impl;

import com.tutuka.transactionmatcher.dto.response.DiscrepancyMatchedTransaction;
import com.tutuka.transactionmatcher.dto.response.EvaluatedTransaction;
import com.tutuka.transactionmatcher.dto.response.MatchReport;
import com.tutuka.transactionmatcher.dto.response.UnmatchedReport;
import com.tutuka.transactionmatcher.entity.ReportEntity;
import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.utils.enums.Origin;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class ReportRepositoryImpl implements ReportRepository {

    private static final Map<String, ReportEntity> REPORT_STORAGE = new ConcurrentHashMap<>();

    @Override
    public synchronized void saveInitial(String rrn, MultipartFile refFile, MultipartFile comFile) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setStatus(ReportStatus.PENDING);
        reportEntity.setReferenceFileName(refFile.getOriginalFilename());
        reportEntity.setCompareFileName(comFile.getOriginalFilename());

        REPORT_STORAGE.putIfAbsent(rrn, reportEntity);
    }

    @Override
    public void update(String rrn, UnmatchedReport unmatchedReport) {
        ReportEntity re = REPORT_STORAGE.get(rrn);
        List<DiscrepancyMatchedTransaction> dmTxns = unmatchedReport.getDiscrepancyMatchedTransactions();

        dmTxns.forEach(dmTxn -> {
            EvaluatedTransaction refTxn = dmTxn.getReferenceTransaction();
            if (refTxn.getSource().equals(Origin.REFERENCE.name())) {
                refTxn.setSource(re.getReferenceFileName());
            } else if (refTxn.getSource().equals(Origin.COMPARE.name())) {
                refTxn.setSource(re.getCompareFileName());
            }

            List<EvaluatedTransaction> possibleMatchTxns = dmTxn.getPossibleMatchTransactions();
            possibleMatchTxns.forEach(pmTxn -> {
                if (pmTxn.getSource().equals(Origin.REFERENCE.name())) {
                    pmTxn.setSource(re.getReferenceFileName());
                } else if (pmTxn.getSource().equals(Origin.COMPARE.name())) {
                    pmTxn.setSource(re.getCompareFileName());
                }
            });
        });

        List<EvaluatedTransaction> noMatchTxns = unmatchedReport.getNoMatchTransactions();

        noMatchTxns.forEach(nmTxn -> {
            if (nmTxn.getSource().equals(Origin.REFERENCE.name())) {
                nmTxn.setSource(re.getReferenceFileName());
            } else if (nmTxn.getSource().equals(Origin.COMPARE.name())) {
                nmTxn.setSource(re.getCompareFileName());
            }
        });

        re.setUnmatchedReport(unmatchedReport);
        re.setStatus(ReportStatus.SUCCESS);
    }

    @Override
    public void update(String rrn, MatchReport matchReport) {
        ReportEntity re = REPORT_STORAGE.get(rrn);
        matchReport.getFile1().setFileName(re.getReferenceFileName());
        matchReport.getFile2().setFileName(re.getCompareFileName());
        re.setMatchReport(matchReport);
        re.setStatus(ReportStatus.SUCCESS);
    }

    @Override
    public ReportEntity get(String rrn) {
        return REPORT_STORAGE.get(rrn);
    }

}
