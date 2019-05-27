package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.http.HttpMethod;
import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricStatsHandlerTest {

    private MetricStatsHandler testee;

    @BeforeEach
    public void setUp() {
        testee = new MetricStatsHandler();
    }

    @Test
    public void shouldUpdateStatsWhenRequested() {
        final long timeTaken = 299;
        final long bytesWritten = 199;
        final Metric metric = new Metric("id", "uri", HttpMethod.GET, bytesWritten, timeTaken);

        final MetricStats result = testee.update(metric);

        assertThat(result.getResponseSize().getCount()).isEqualTo(1);
        assertThat(result.getResponseSize().getMax()).isEqualTo(bytesWritten);
        assertThat(result.getResponseSize().getMin()).isEqualTo(bytesWritten);
        assertThat(result.getResponseSize().getMean()).isEqualTo(bytesWritten);

        assertThat(result.getRequestTime().getCount()).isEqualTo(1);
        assertThat(result.getRequestTime().getMax()).isEqualTo(timeTaken);
        assertThat(result.getRequestTime().getMin()).isEqualTo(timeTaken);
        assertThat(result.getRequestTime().getMean()).isEqualTo(timeTaken);
    }

    @Test
    public void shouldStartWithZeroStats() {
        final MetricStats metricStats = testee.get();

        assertThat(metricStats.getRequestTime().getCount()).isEqualTo(0);
        assertThat(metricStats.getResponseSize().getCount()).isEqualTo(0);
    }

}