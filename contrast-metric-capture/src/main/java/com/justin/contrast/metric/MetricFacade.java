package com.justin.contrast.metric;

import java.util.List;

public interface MetricFacade {
    void start();
    void stop();
    MetricStats getStats();
    Metric getMetric(String id);
    List<Metric> getAllMetrics();
    boolean emit(Metric metric);
    boolean emit(Metric metric, long waitMs);
}
