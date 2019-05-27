package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStats;

/**
 * Class designed specifically for single threaded writes and multiple reads to handle updating stats.
 * Executing concurrent writes is not supported as then stats may be dropped depending on the order of execution.
 */
public class MetricStatsHandler {
    private volatile MetricStats stats;

    MetricStatsHandler() {
        this.stats = new MetricStats();
    }

    public MetricStats get() {
        return stats;
    }

    MetricStats update(final Metric metric) {
        final MetricStats newStats = stats.update(metric.getBytesWritten(), metric.getTimeTakenMs());
        stats = newStats;

        return newStats;
    }
}
