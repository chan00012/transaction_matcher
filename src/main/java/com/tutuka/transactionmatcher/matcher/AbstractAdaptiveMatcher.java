package com.tutuka.transactionmatcher.matcher;

import com.tutuka.transactionmatcher.entity.Transaction;
import com.tutuka.transactionmatcher.utils.enums.Tag;

import java.util.Set;

public abstract class AbstractAdaptiveMatcher implements BaseAdaptiveMatcher<Transaction> {

    private final Tag tag;

    protected AbstractAdaptiveMatcher(Tag tag) {
        this.tag = tag;
    }

    public void addTag(Transaction t1, Transaction t2, Set<Tag> tags) {
        if (!evaluate(t1, t2)) {
            tags.add(tag);
        }
    }
}
