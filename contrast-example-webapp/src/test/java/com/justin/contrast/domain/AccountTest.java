package com.justin.contrast.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class AccountTest {
    @Test
    public void shouldHonorEqualsHashCode() {
        EqualsVerifier.forClass(Account.class)
                .verify();
    }
}