package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TransactionServiceImplTest {

    private TransactionServiceImpl testee;

    @Before
    public void setUp() {
        testee = new TransactionServiceImpl();
    }

    @Test
    public void shouldThrowExIfNullTransactionIdProvided() {
        assertThatThrownBy(() -> testee.get("Account", null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldReturnExpectedTransactionId() {
        final String txId = "1234";

        final Transaction found = testee.get("account", txId);

        assertThat(found.getId()).isEqualTo(txId);
    }
}