package com.justin.contrast.controller;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStats;
import com.justin.contrast.service.MetricService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping ("/metrics")
public class MetricController {
    private final MetricService service;

    public MetricController(final MetricService service) {
        this.service = Objects.requireNonNull(service);
    }

    @GetMapping("/stats")
    public MetricStats getAccount() {
        return service.getMetricStats();
    }

    @GetMapping("/{id}")
    public Metric getMetric(@PathVariable String id) {
        return service.getMetricById(id);
    }

    @GetMapping
    public List<Metric> getAll() {
        return service.getAll();
    }
}
