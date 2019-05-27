package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class that handles processing {@link Metric} objects retrieved from a queue.
 * It does the following:
 * <li>Updates the stats for overall metrics</li>
 * <li>Adds the metric to our buffer of metrics</li>
 */
class MetricConsumer implements Consumer<Metric> {

    private final MetricStatsHandler statsHandler;
    private final RingBufferWithLookup<String, Metric> buffer;

    MetricConsumer(final MetricStatsHandler statsHandler,
                   final RingBufferWithLookup<String, Metric> buffer) {
        this.statsHandler = Objects.requireNonNull(statsHandler);
        this.buffer = Objects.requireNonNull(buffer);
    }

    @Override
    public void accept(final Metric metric) {
        if (metric != null) {
            buffer.put(metric.getId(), metric);
            statsHandler.update(metric);
        }
    }
}
