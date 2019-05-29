package com.justin.contrast.metrics;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.MetricStats;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;

/**
 * Provides a wrapper for the Spring lifecycle handling for our {@link MetricFacade}.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricFacadeSpring implements MetricFacade, SmartLifecycle {

    private final MetricFacade facade;
    private volatile boolean running = false;

    public MetricFacadeSpring(final MetricFacade facade) {
        this.facade = Objects.requireNonNull(facade);
    }

    @Override
    public void start() {
        facade.start();
        running = true;
    }

    @Override
    public void stop() {
        facade.stop();
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public MetricStats getStats() {
        return facade.getStats();
    }

    @Override
    public Metric getMetric(final String id) {
        return facade.getMetric(id);
    }

    @Override
    public List<Metric> getAllMetrics() {
        return facade.getAllMetrics();
    }

    @Override
    public boolean emit(final Metric metric) {
        return facade.emit(metric);
    }

    @Override
    public boolean emit(final Metric metric,
                        final long waitMs) {
        return facade.emit(metric, waitMs);
    }
}
