package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Log4j2
@Component
public class TransactionDateMatcher extends AbstractAdaptiveMatcher {

    public TransactionDateMatcher() {
        super(Tag.DATE_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {

        if (Objects.equals(t1.getTransactionDate(), t2.getTransactionDate())) {
            return true;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime t1DateTime = LocalDateTime.parse(t1.getTransactionDate(), formatter);
            LocalDateTime t2DateTime = LocalDateTime.parse(t2.getTransactionDate(), formatter);
            return Math.abs(ChronoUnit.MINUTES.between(t1DateTime, t2DateTime)) <= 5;
        } catch (Exception e) {
            log.warn("Exception occurred while parsing transaction date: {}", e.getMessage());
        }
        return false;
    }
}
