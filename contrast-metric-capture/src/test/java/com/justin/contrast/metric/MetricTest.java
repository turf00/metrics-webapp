package com.justin.contrast.metric;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class MetricTest {
    @Test
    public void shouldHonourEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(Metric.class)
                .verify();
    }

}