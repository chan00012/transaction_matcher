package com.tutuka.transactionmatcher.matcher.custom;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.AbstractAdaptiveMatcher;
import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component
public class TransactionNarrativeMatcher extends AbstractAdaptiveMatcher {

    public TransactionNarrativeMatcher() {
        super(Tag.NARRATIVE_DISCREPANCY);
    }

    @Override
    public boolean evaluate(Transaction t1, Transaction t2) {
        if (Objects.equals(t1.getTransactionNarrative(), t2.getTransactionNarrative())) {
            return true;
        }

        String t1Narr = StringUtils.deleteWhitespace(t1.getTransactionNarrative());
        String t2Narr = StringUtils.deleteWhitespace(t2.getTransactionNarrative());
        return levenshteinDistance(t1Narr, t2Narr) <= 5;
    }

    //code snippet from: https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        for (int i = 0; i < len0; i++) cost[i] = i;

        for (int j = 1; j < len1; j++) {
            newcost[0] = j;

            for (int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        return cost[len0 - 1];
    }
}
