package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.MetricQueue;
import com.justin.contrast.metric.MetricStats;

import java.util.List;
import java.util.Objects;

public final class MetricFacadeImpl implements MetricFacade {

    static final long WAIT_TIME_MS = 10;

    private final RingBufferWithLookup<String, Metric> buffer;
    private final MetricQueue queue;
    private final MetricStatsHandler stats;

    public MetricFacadeImpl(final int bufferCapacity,
                            final int pendingQueueCapacity) {
        buffer = new RingBufferWithLookup<>(bufferCapacity);
        stats = new MetricStatsHandler();
        final MetricConsumer processor = new MetricConsumer(stats, buffer);
        queue = new MetricQueueImpl(pendingQueueCapacity, processor);
    }

    MetricFacadeImpl(final RingBufferWithLookup<String, Metric> buffer,
                     final MetricQueue queue,
                     final MetricStatsHandler stats) {
        this.buffer = Objects.requireNonNull(buffer);
        this.queue = Objects.requireNonNull(queue);
        this.stats = Objects.requireNonNull(stats);
    }

    @Override
    public void start() {
        queue.start();
    }

    @Override
    public void stop() {
        queue.stop();
    }

    @Override
    public MetricStats getStats() {
        return stats.get();
    }

    @Override
    public Metric getMetric(final String id) {
        return buffer.getByKey(id);
    }

    @Override
    public List<Metric> getAllMetrics() {
        return buffer.getAll();
    }

    @Override
    public boolean emit(final Metric metric) {
        return emit(metric, WAIT_TIME_MS);
    }

    @Override
    public boolean emit(final Metric metric,
                        final long waitMs) {
        return queue.offer(metric, waitMs);
    }

}
