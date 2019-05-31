package com.justin.contrast.metrics;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.MetricStats;
import com.justin.contrast.util.MetricGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricFacadeSpringTest {

    private static final String ID = "12345";
    private static final Metric METRIC = MetricGenerator.metric(ID);
    private static final MetricStats STATS = new MetricStats();

    private MetricFacade mockFacade;
    private MetricFacadeSpring testee;

    @Before
    public void setUp() {
        mockFacade = Mockito.mock(MetricFacade.class);
        testee = new MetricFacadeSpring(mockFacade);
    }

    @Test
    public void shouldPassThroughCallsAsExpected() {
        testee.emit(METRIC);
        verify(mockFacade).emit(METRIC);

        final long delayMs = 123;
        testee.emit(METRIC, delayMs);
        verify(mockFacade).emit(METRIC, delayMs);

        final List<Metric> expectedList = Collections.singletonList(METRIC);
        when(mockFacade.getAllMetrics()).thenReturn(expectedList);
        assertThat(testee.getAllMetrics()).isEqualTo(expectedList);

        when(mockFacade.getMetric(ID)).thenReturn(METRIC);
        assertThat(testee.getMetric(ID)).isEqualTo(METRIC);

        when(mockFacade.getStats()).thenReturn(STATS);
        assertThat(testee.getStats()).isEqualTo(STATS);
    }

    @Test
    public void shouldHonourSpringLifecycle() {
        assertThat(testee.isRunning()).isFalse();

        testee.start();

        verify(mockFacade).start();
        assertThat(testee.isRunning()).isTrue();

        testee.stop();
        assertThat(testee.isRunning()).isFalse();
        verify(mockFacade).stop();
    }

}
