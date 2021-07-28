package com.tutuka.transactionmatcher.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {

    private Utilities(){}

    public static String generateReferenceNumber() {
        long millis  = Math.abs(Calendar.getInstance().getTimeInMillis() + ThreadLocalRandom.current().nextLong());
        return String.valueOf(millis).substring(0,10);
    }

    public static String generateErrRefNumber() {
        String ref = UUID.randomUUID().toString();
        return ref.substring(0, 10).replace("-", StringUtils.EMPTY).toUpperCase(Locale.ROOT);
    }
}
