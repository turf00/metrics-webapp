package com.justin.contrast.domain;

import java.util.List;
import java.util.Objects;

public final class Account {
    private final String id;
    private final String type;
    private final String userId;
    private final List<Transaction> transactions;
    private final String balance;

    public Account(final String id,
                   final String type,
                   final String userId,
                   final List<Transaction> transactions,
                   final String balance) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.userId = Objects.requireNonNull(userId);
        this.transactions = Objects.requireNonNull(transactions);
        this.balance = Objects.requireNonNull(balance);
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        final Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(type, account.type) &&
                Objects.equals(userId, account.userId) &&
                Objects.equals(transactions, account.transactions) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, userId, transactions, balance);
    }
}
