package com.tutuka.transactionmatcher.matcher;

import com.tutuka.transactionmatcher.utils.enums.Tag;

import java.util.Set;

public interface BaseAdaptiveMatcher<T> {

    boolean thresholdMatch(T t1, T t2);

    void addTag(T t1, T t2, Set<Tag> tags);

}
