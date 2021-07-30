package com.tutuka.transactionmatcher.matcher.custom;

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
import java.math.BigDecimal;
import java.util.Objects;

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.amount")
public class TransactionAmountMatcher extends AbstractAdaptiveMatcher {

    @NotBlank
    private String amountThreshold;

    public TransactionAmountMatcher() {
        super(Tag.AMOUNT_DISCREPANCY);
    }

    @Override
    public boolean thresholdMatch(Transaction t1, Transaction t2) {
        if (Objects.equals(t1.getTransactionAmount(), t2.getTransactionAmount())) {
            return true;
        }

        try {
            BigDecimal thresAmount = new BigDecimal(amountThreshold);
            BigDecimal t1Amount = new BigDecimal(t1.getTransactionAmount());
            BigDecimal t2Amount = new BigDecimal(t2.getTransactionAmount());
            return t1Amount.subtract(t2Amount).abs().compareTo(thresAmount) <= 0;
        } catch (Exception e) {
            log.warn("Exception occurred while parsing transaction amount: {}", e.getMessage());
        }
        return false;
    }
}
