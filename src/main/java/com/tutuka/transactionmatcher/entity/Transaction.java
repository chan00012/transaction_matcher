package com.tutuka.transactionmatcher.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Objects;

@Data
public class Transaction {

    private int rowNum;
    private String profileName;
    private String transactionDate;
    private String transactionAmount;
    private String transactionNarrative;
    private String transactionDescription;
    private String transactionId;
    private String transactionType;
    private String walletReference;
    private String merchant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionType, that.transactionType) &&
                Objects.equals(profileName, that.profileName) &&
                Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(transactionAmount, that.transactionAmount) &&
                Objects.equals(transactionNarrative, that.transactionNarrative) &&
                Objects.equals(transactionDescription, that.transactionDescription) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(walletReference, that.walletReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileName, transactionDate, transactionAmount, transactionNarrative,
                transactionDescription, transactionId, transactionType, walletReference);
    }
}
