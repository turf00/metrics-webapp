package com.justin.contrast.service.impl;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.MetricStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static com.justin.contrast.util.MetricGenerator.metric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MetricServiceImplTest {

    private MetricFacade mockFacade;
    private MetricServiceImpl testee;

    @BeforeEach
    public void setUp() {
        mockFacade = Mockito.mock(MetricFacade.class);
        testee = new MetricServiceImpl(mockFacade);
    }

    @Test
    public void shouldPassThroughValuesAsExpected() {
        final String id = "1234";
        final Metric metric = metric(id);

        when(mockFacade.getMetric(id)).thenReturn(metric);
        assertThat(testee.getMetricById(id)).isEqualTo(metric);

        final MetricStats expectedStats = new MetricStats();

        when(mockFacade.getStats()).thenReturn(expectedStats);
        assertThat(testee.getMetricStats()).isEqualTo(expectedStats);

        final List<Metric> expectedMetrics = Collections.singletonList(metric);

        when(mockFacade.getAllMetrics()).thenReturn(expectedMetrics);
        assertThat(testee.getAll()).isEqualTo(expectedMetrics);
    }

}