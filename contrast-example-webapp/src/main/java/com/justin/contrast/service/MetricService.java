package com.justin.contrast.service;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStats;

import java.util.List;

public interface MetricService {
    Metric getMetricById(String id);
    MetricStats getMetricStats();
    List<Metric> getAll();
}
