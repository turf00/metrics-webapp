package com.justin.contrast.metric;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricStatLongTest {

    @Test
    public void shouldSetCorrectValuesOnFirstUpdate() {
        final long metric = 123;

        final MetricStatLong starting = new MetricStatLong("Name");

        final MetricStatLong result = starting.update(metric);

        assertThat(result.getMin()).isEqualTo(metric);
        assertThat(result.getMax()).isEqualTo(metric);
        assertThat(result.getMean()).isEqualTo(metric);
        assertThat(result.getCount()).isEqualTo(1);
    }

    @Test
    public void shouldSetCorrectValuesOnSecondUpdate() {
        final long metric1 = 123;
        final long metric2 = 999;

        final MetricStatLong starting = new MetricStatLong("Name").update(metric1);

        final MetricStatLong result = starting.update(metric2);

        assertThat(result.getMin()).isEqualTo(metric1);
        assertThat(result.getMax()).isEqualTo(metric2);
        assertThat(result.getMean()).isEqualTo((metric1 + metric2) / 2);
        assertThat(result.getCount()).isEqualTo(2);
    }

    @Test
    public void shouldHonorEqualsHashCodeContract() {
        EqualsVerifier.forClass(MetricStatLong.class)
                .verify();
    }
}