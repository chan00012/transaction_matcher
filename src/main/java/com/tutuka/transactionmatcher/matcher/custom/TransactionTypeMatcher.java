package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.type")
public class TransactionTypeMatcher extends AbstractAdaptiveMatcher {

    @NotEmpty
    private List<String> allowedType;

    public TransactionTypeMatcher() {
        super(Tag.TYPE_DISCREPANCY);
    }

    @Override
    public boolean thresholdMatch(Transaction t1, Transaction t2) {
        if (allowedType.contains(t1.getTransactionType()) && allowedType.contains(t2.getTransactionType())) {
            return Objects.equals(t1.getTransactionType(),t2.getTransactionType());
        } else {
            return false;
        }
    }
}
