package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceImplTest {

    private static final String ACCOUNT_ID = "12345";

    private AccountServiceImpl testee;

    @BeforeEach
    public void setUp() {
        testee = new AccountServiceImpl();
    }

    @Test
    public void shouldThrowExIfNullAccountIdProvided() {
        assertThrows(NullPointerException.class, () -> testee.getAccount(null));
    }

    @Test
    public void shouldReturnAccountWithExpectedId() {
        final Account account = testee.getAccount(ACCOUNT_ID);

        assertThat(account.getId()).isEqualTo(ACCOUNT_ID);
    }

}