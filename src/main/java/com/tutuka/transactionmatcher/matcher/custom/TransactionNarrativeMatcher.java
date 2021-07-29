package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.Utilities;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Objects;

@Log4j2
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "matcher.narrative")
public class TransactionNarrativeMatcher extends AbstractAdaptiveMatcher {

    @NotNull
    private int charThreshold;

    public TransactionNarrativeMatcher() {
        super(Tag.NARRATIVE_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {
        if (Objects.equals(t1.getTransactionNarrative(), t2.getTransactionNarrative())) {
            return true;
        }

        String t1Narr = StringUtils.deleteWhitespace(t1.getTransactionNarrative()).toUpperCase(Locale.ROOT);
        String t2Narr = StringUtils.deleteWhitespace(t2.getTransactionNarrative()).toUpperCase(Locale.ROOT);
        return Utilities.levenshteinDistance(t1Narr, t2Narr) <= charThreshold;
    }
}
