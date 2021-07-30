package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.Utilities;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.id")
public class TransactionIdMatcher extends AbstractAdaptiveMatcher {

    @NotNull
    private int charThreshold;

    public TransactionIdMatcher() {
        super(Tag.ID_DISCREPANCY);
    }

    @Override
    public boolean thresholdMatch(Transaction t1, Transaction t2) {
        if (Objects.equals(t1.getTransactionId(), t2.getTransactionId())) {
            return true;
        }

        return Utilities.levenshteinDistance(t1.getTransactionId(), t2.getTransactionId()) <= charThreshold;
    }
}
