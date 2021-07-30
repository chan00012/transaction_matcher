package com.tutuka.transactionmatcher.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class Transaction {

    private int rowNum;

    @JsonProperty("ProfileName")
    private String profileName;

    @JsonProperty("TransactionDate")
    private String transactionDate;

    @JsonProperty("TransactionAmount")
    private String transactionAmount;

    @JsonProperty("TransactionNarrative")
    private String transactionNarrative;

    @JsonProperty("TransactionDescription")
    private String transactionDescription;

    @JsonProperty("TransactionID")
    private String transactionId;

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("WalletReference")
    private String walletReference;

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
        return Objects.hash(profileName, transactionDate, transactionAmount, transactionNarrative, transactionDescription, transactionId, transactionType, walletReference);
    }
}
