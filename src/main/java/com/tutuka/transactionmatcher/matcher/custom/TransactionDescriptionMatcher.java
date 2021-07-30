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

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.description")
public class TransactionDescriptionMatcher extends AbstractAdaptiveMatcher {

    @NotEmpty
    private List<String> allowedDescriptor;

    public TransactionDescriptionMatcher() {
        super(Tag.DESCRIPTION_DISCREPANCY);
    }

    @Override
    public boolean thresholdMatch(Transaction t1, Transaction t2) {
        if (allowedDescriptor.stream().anyMatch(ds-> ds.equalsIgnoreCase(t1.getTransactionDescription()))
                && allowedDescriptor.stream().anyMatch(ds-> ds.equalsIgnoreCase(t2.getTransactionDescription()))) {
            return t1.getTransactionDescription().equalsIgnoreCase(t2.getTransactionDescription());
        } else {
            return false;
        }
    }
}
