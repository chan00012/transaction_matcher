package com.tutuka.transactionmatcher.utils.enums;

import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Tag {

    NO_MATCH("No match"),
    DATE_DISCREPANCY("Please check the transaction date difference."),
    AMOUNT_DISCREPANCY("Please check the transaction amount difference."),
    NARRATIVE_DISCREPANCY("Please check the transaction narrative difference."),
    TYPE_DISCREPANCY("Please check the transaction type difference."),
    WALLET_REFERENCE_DISCREPANCY("Please check the wallet reference difference."),
    ID_DISCREPANCY("Please check the transaction id difference."),
    DESCRIPTION_DISCREPANCY("Please check the transaction description difference."),
    DUPLICATE_REFERENCE("Please check reference transaction having duplicates."),
    DUPLICATE_COMPARE("Please check compared transaction having duplicates.");
    private final String spiel;

    Tag(String spiel) {
        this.spiel = spiel;
    }

    public static Map<Tag, String> populateDiscrepancies(Set<Tag> tags) {
        return tags.stream().collect(Collectors.toMap(t -> t, Tag::getSpiel));
    }
}
