package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Transaction;
import com.justin.contrast.util.TransactionGenerator;
import com.justin.contrast.service.TransactionService;

import java.util.Objects;

import static com.justin.contrast.util.SimulateUtil.simulateNetworkDelay;

public final class TransactionServiceImpl implements TransactionService {
    @Override
    public Transaction get(final String accountId,
                           final String transactionId) {
        simulateNetworkDelay();
        return TransactionGenerator.generate(Objects.requireNonNull(transactionId));
    }
}
