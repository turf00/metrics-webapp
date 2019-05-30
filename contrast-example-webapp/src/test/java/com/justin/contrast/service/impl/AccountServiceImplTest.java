package com.justin.contrast.service.impl;

import com.justin.contrast.domain.Account;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountServiceImplTest {

    private static final String ACCOUNT_ID = "12345";

    private AccountServiceImpl testee;

    @Before
    public void setUp() {
        testee = new AccountServiceImpl();
    }

    @Test
    public void shouldThrowExIfNullAccountIdProvided() {
        assertThatThrownBy(() -> testee.getAccount(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldReturnAccountWithExpectedId() {
        final Account account = testee.getAccount(ACCOUNT_ID);

        assertThat(account.getId()).isEqualTo(ACCOUNT_ID);
    }

}