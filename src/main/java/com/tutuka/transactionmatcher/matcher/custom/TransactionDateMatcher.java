package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.constraint.DateTimeFormat;
import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.date")
public class TransactionDateMatcher extends AbstractAdaptiveMatcher {

    @NotNull
    private long duration;

    @NotBlank
    @DateTimeFormat
    private String datePattern;

    @NotNull
    private ChronoUnit timeUnit;

    public TransactionDateMatcher() {
        super(Tag.DATE_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {

        if (Objects.equals(t1.getTransactionDate(), t2.getTransactionDate())) {
            return true;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDateTime t1DateTime = LocalDateTime.parse(t1.getTransactionDate(), formatter);
            LocalDateTime t2DateTime = LocalDateTime.parse(t2.getTransactionDate(), formatter);
            return Math.abs(timeUnit.between(t1DateTime, t2DateTime)) <= duration;
        } catch (Exception e) {
            log.warn("Exception occurred while parsing transaction date: {}", e.getMessage());
        }
        return false;
    }
}
