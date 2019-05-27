package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MetricConsumerTest {

    private final RingBufferWithLookup<String, Metric> mockBuffer = Mockito.mock(RingBufferWithLookup.class);
    private final MetricStatsHandler mockStats = Mockito.mock(MetricStatsHandler.class);
    private MetricConsumer testee;

    @BeforeEach
    public void setUp() {
        Mockito.reset(mockBuffer, mockStats);
        testee = new MetricConsumer(mockStats, mockBuffer);
    }

    @Test
    public void shouldIgnoreNullMetric() {
        testee.accept(null);

        Mockito.verifyZeroInteractions(mockStats, mockBuffer);
    }

    @Test
    public void shouldPassMetricToBufferAndStats() {
        final Metric metric = TestHelper.metric();

        testee.accept(metric);

        Mockito.verify(mockStats).update(metric);
        Mockito.verify(mockBuffer).put(metric.getId(), metric);
    }
}