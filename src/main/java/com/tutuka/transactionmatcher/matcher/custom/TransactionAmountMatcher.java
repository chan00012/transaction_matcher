package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component
public class TransactionAmountMatcher extends AbstractAdaptiveMatcher {

    public TransactionAmountMatcher() {
        super(Tag.AMOUNT_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {
        return Objects.equals(t1.getTransactionAmount(), t2.getTransactionAmount());
    }
}
