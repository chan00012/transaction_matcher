package com.tutuka.transactionmatcher.utils.enums;


import lombok.Getter;

@Getter
public enum Origin {

    REFERENCE(Tag.DUPLICATE_REFERENCE),
    COMPARE(Tag.DUPLICATE_COMPARE);

    private final Tag tag;

    Origin(Tag tag) {
        this.tag = tag;
    }
}
