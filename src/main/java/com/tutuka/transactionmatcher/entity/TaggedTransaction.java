package com.tutuka.transactionmatcher.entity;

import com.tutuka.transactionmatcher.utils.enums.Tag;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class TaggedTransaction {

    private Integer count;
    private String source;
    private Transaction transaction;
    private Set<Tag> tags;

    public TaggedTransaction(Transaction transaction) {
        this.transaction = transaction;
        this.tags = new HashSet<>();
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void addAllTag(Set<Tag> tags) {
        this.tags.addAll(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaggedTransaction that = (TaggedTransaction) o;
        return Objects.equals(tags, that.tags) && Objects.equals(count, that.count) && Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags, count, transaction);
    }
}
