package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Account;
import com.justin.contrast.util.AmountGenerator;
import com.justin.contrast.domain.Transaction;
import com.justin.contrast.util.TransactionGenerator;
import com.justin.contrast.service.AccountService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.justin.contrast.util.SimulateUtil.simulateNetworkDelay;

public final class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_TYPE = "Checking";
    private static final int MAX_TRANSACTION_COUNT = 20;


    @Override
    public Account getAccount(final String accountId) {
        Objects.requireNonNull(accountId);
        simulateNetworkDelay();
        return build(accountId);
    }

    private static Account build(final String accountId) {
        return new Account(accountId,
                ACCOUNT_TYPE,
                UUID.randomUUID().toString(),
                transactions(),
                AmountGenerator.amount());
    }

    private static List<Transaction> transactions() {
        return TransactionGenerator.generate(MAX_TRANSACTION_COUNT);
    }
}
