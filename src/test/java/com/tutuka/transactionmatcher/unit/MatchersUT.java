package com.tutuka.transactionmatcher.unit;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.matcher.custom.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
class MatchersUT {

    @Autowired
    TransactionAmountMatcher transactionAmountMatcher;

    @Autowired
    TransactionDateMatcher transactionDateMatcher;

    @Autowired
    TransactionIdMatcher transactionIdMatcher;

    @Autowired
    TransactionNarrativeMatcher transactionNarrativeMatcher;

    @Autowired
    TransactionDescriptionMatcher transactionDescriptionMatcher;

    @Autowired
    TransactionTypeMatcher transactionTypeMatcher;

    @Autowired
    WalletReferenceMatcher walletReferenceMatcher;

    @Test
    void testAmountMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionAmount("1000");
        t2.setTransactionAmount("900");
        assertTrue(transactionAmountMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testAmountMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionAmount("1000");
        t2.setTransactionAmount("10000");
        assertFalse(transactionAmountMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testAmountMatcher_evalException() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionAmount("1000");
        t2.setTransactionAmount("abc");
        assertFalse(transactionAmountMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDateMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDate("2020-07-29 07:07:59");
        t2.setTransactionDate("2020-07-29 07:12:59");
        assertTrue(transactionDateMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDateMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDate("2020-07-29 07:07:59");
        t2.setTransactionDate("2020-07-29 07:15:59");
        assertFalse(transactionDateMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDateMatcher_evalException() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDate("2020-07-29 07:07:59");
        t2.setTransactionDate("abc");
        assertFalse(transactionDateMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testIdMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionId("1122334455667788");
        t2.setTransactionId("1122333445567788");
        assertTrue(transactionIdMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testIdMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionId("1122334455667788");
        t2.setTransactionId("8877665544371237");
        assertFalse(transactionIdMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testNarrativeMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionNarrative("Mlplole Filli100558    Gaborone      BW");
        t2.setTransactionNarrative("Molepolole Filli100558    Gaborone      BW");
        assertTrue(transactionNarrativeMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testNarrativeMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionNarrative("ENGEN TSOLAMOSESI         GABORONE      BW");
        t2.setTransactionNarrative("Molepolole Filli100558    Gaborone      BW");
        assertFalse(transactionNarrativeMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDescriptionMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDescription("DEDUCT");
        t2.setTransactionDescription("DEDUCT");
        assertTrue(transactionDescriptionMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDescriptionMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDescription("DEDUCT");
        t2.setTransactionDescription("REVERSAL");
        assertFalse(transactionDescriptionMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testDescriptionMatcher_diffValue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionDescription("DEDUCT");
        t2.setTransactionDescription("abc");
        assertFalse(transactionDescriptionMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testTypeMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionType("1");
        t2.setTransactionType("1");
        assertTrue(transactionTypeMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testTypeMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionType("1");
        t2.setTransactionType("0");
        assertFalse(transactionTypeMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testTypeMatcher_diffValue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setTransactionType("1");
        t2.setTransactionType("abc");
        assertFalse(transactionTypeMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testWalletReferenceMatcher_evalTrue() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setWalletReference("P_NzI5NTA3MzFfMTM4NjMzMjM2Mi42NTI2");
        t2.setWalletReference("P_NzI5NTA3MzFfMTM4NjMzMjM2Mi42NTI2");
        assertTrue(walletReferenceMatcher.thresholdMatch(t1, t2));
    }

    @Test
    void testWalletReferenceMatcher_evalFalse() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        t1.setWalletReference("P_NzI5NTA3MzFfMTM4NjMzMjM2Mi42NTI2");
        t2.setWalletReference("P_NzI5NTA3MzFfMTM4NjMzMjM2Mi42NTI2abc");
        assertFalse(walletReferenceMatcher.thresholdMatch(t1, t2));
    }
}
