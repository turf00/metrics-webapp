package com.justin.contrast.service;

import com.justin.contrast.domain.Transaction;

public interface TransactionService {
    Transaction get(String accountId, String transactionId);
}
