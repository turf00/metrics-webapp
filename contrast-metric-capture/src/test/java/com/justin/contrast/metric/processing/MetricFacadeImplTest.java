package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricQueue;
import com.justin.contrast.metric.MetricStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.justin.contrast.metric.processing.MetricFacadeImpl.WAIT_TIME_MS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricFacadeImplTest {
    private RingBufferWithLookup<String, Metric> mockBuffer;
    private MetricQueue mockQueue;
    private MetricStatsHandler mockStats;
    private MetricFacadeImpl testee;
    private final Metric metric = TestHelper.metric();

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        mockBuffer = mock(RingBufferWithLookup.class);
        mockQueue = Mockito.mock(MetricQueue.class);
        mockStats = Mockito.mock(MetricStatsHandler.class);
        testee = new MetricFacadeImpl(mockBuffer, mockQueue, mockStats);
    }

    @Test
    public void shouldStartQueueWhenStarting() {
        testee.start();

        verify(mockQueue).start();
    }

    @Test
    public void shouldStopQueueWhenStopping() {
        testee.stop();

        verify(mockQueue).stop();
    }

    @Test
    public void shouldPassThroughStatsWhenRequested() {
        final MetricStats expected = new MetricStats();

        when(mockStats.get()).thenReturn(expected);

        assertThat(testee.getStats()).isEqualTo(expected);
    }

    @Test
    public void shouldGetSingleMetricByKeyWhenRequested() {
        when(mockBuffer.getByKey(metric.getId())).thenReturn(metric);

        assertThat(testee.getMetric(metric.getId())).isEqualTo(metric);
    }

    @Test
    public void shouldReturnAllMetricsWhenRequested() {
        final List<Metric> expected = Arrays.asList(metric);

        when(mockBuffer.getAll()).thenReturn(expected);

        assertThat(testee.getAllMetrics()).isEqualTo(expected);
    }

    @Test
    public void shouldEmitMetricToQueueWhenRequestedWithDefaultTimeout() {
        when(mockQueue.offer(metric, WAIT_TIME_MS)).thenReturn(true);

        final boolean found = testee.emit(metric);


        assertThat(found).isTrue();
        Mockito.verify(mockQueue).offer(metric, WAIT_TIME_MS);
    }

    @Test
    public void shouldEmitMetricToQueueWithSpecificTimeout() {
        final long timeOutMs = 5467;

        when(mockQueue.offer(metric, timeOutMs)).thenReturn(true);

        final boolean found = testee.emit(metric, timeOutMs);

        assertThat(found).isTrue();
        Mockito.verify(mockQueue).offer(metric, timeOutMs);
    }

    @Test
    public void shouldReturnFalseWhenMetricCouldNotBeEmittedInTime() {
        when(mockQueue.offer(metric, WAIT_TIME_MS)).thenReturn(false);

        final boolean found = testee.emit(metric, WAIT_TIME_MS);

        assertThat(found).isFalse();
        Mockito.verify(mockQueue).offer(metric, WAIT_TIME_MS);
    }

}