package com.justin.contrast.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public final class Transaction {
    private final String id;
    private final String customerId;
    private final ZonedDateTime effectiveDate;
    private final String amount;
    private final String description;

    public Transaction(String id,
                       final String customerId,
                       final ZonedDateTime effectiveDate,
                       final String amount,
                       final String description) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.effectiveDate = Objects.requireNonNull(effectiveDate);
        this.amount = Objects.requireNonNull(amount);
        this.description = Objects.requireNonNull(description);
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ZonedDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        final Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getEffectiveDate(), that.getEffectiveDate()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomerId(), getEffectiveDate(), getAmount(), getDescription());
    }
}
