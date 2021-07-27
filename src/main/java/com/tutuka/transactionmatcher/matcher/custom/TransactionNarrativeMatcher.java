package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TransactionNarrativeMatcher extends AbstractAdaptiveMatcher {

    public TransactionNarrativeMatcher() {
        super(Tag.NARRATIVE_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {
        return Objects.equals(t1.getTransactionNarrative(), t2.getTransactionNarrative());
    }
}
