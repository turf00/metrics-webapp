package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omg.IOP.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionServiceImplTest {

    private TransactionServiceImpl testee;

    @BeforeEach
    public void setUp() {
        testee = new TransactionServiceImpl();
    }

    @Test
    public void shouldThrowExIfNullTransactionIdProvided() {
        assertThrows(NullPointerException.class, () -> testee.get("Account", null));
    }

    @Test
    public void shouldReturnExpectedTransactionId() {
        final String txId = "1234";

        final Transaction found = testee.get("account", txId);

        assertThat(found.getId()).isEqualTo(txId);
    }
}