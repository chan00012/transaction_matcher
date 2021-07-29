package com.tutuka.transactionmatcher.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    private Utilities() {
    }

    public static String generateReferenceNumber() {
        long millis = Math.abs(Calendar.getInstance().getTimeInMillis() + ThreadLocalRandom.current().nextLong());
        return String.valueOf(millis).substring(0, 10);
    }

    public static String generateErrRefNumber() {
        String ref = UUID.randomUUID().toString();
        return ref.substring(0, 10).replace("-", StringUtils.EMPTY).toUpperCase(Locale.ROOT);
    }

    //code snippet from: https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public static int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
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
