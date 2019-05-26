package com.justin.contrast.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    @Test
    public void shouldHonorEqualsHashCode() {
        EqualsVerifier.forClass(Transaction.class)
                .verify();
    }
}