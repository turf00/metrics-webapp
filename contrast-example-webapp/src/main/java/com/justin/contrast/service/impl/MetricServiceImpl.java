package com.justin.contrast.service.impl;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.MetricStats;
import com.justin.contrast.service.MetricService;

import java.util.List;
import java.util.Objects;

public class MetricServiceImpl implements MetricService {
    private final MetricFacade metricFacade;

    public MetricServiceImpl(final MetricFacade metricFacade) {
        this.metricFacade = Objects.requireNonNull(metricFacade);
    }

    @Override
    public Metric getMetricById(final String id) {
        return metricFacade.getMetric(id);
    }

    @Override
    public MetricStats getMetricStats() {
        return metricFacade.getStats();
    }

    @Override
    public List<Metric> getAll() {
        return metricFacade.getAllMetrics();
    }
}
