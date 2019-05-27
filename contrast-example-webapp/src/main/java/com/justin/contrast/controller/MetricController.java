package com.justin.contrast.controller;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStatLong;
import com.justin.contrast.metric.MetricStats;
import com.justin.contrast.service.MetricService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping ("/metrics")
public class MetricController {
    private static final List<Metric> EMPTY = new ArrayList<>();
    private final MetricService service;

    public MetricController(final MetricService service) {
        this.service = Objects.requireNonNull(service);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MetricStatLong> getStats() {
        final MetricStats metricStats = service.getMetricStats();
        return Arrays.asList(metricStats.getRequestTime(), metricStats.getResponseSize());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Metric getMetric(@PathVariable String id) {
        return service.getMetricById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Metric> getAll() {
        return service.getAll();
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAllHtml(final Map<String, Object> model) {
        final List<Metric> allMetrics = getAll();
        return handleMetrics(model, allMetrics);
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMetricHtml(@RequestParam final String id,
                                      final Map<String, Object> model) {
        final Metric metric = getMetric(id);
        List<Metric> metrics = (metric == null) ? EMPTY : Collections.singletonList(metric);

        return handleMetrics(model, metrics);
    }

    @GetMapping(value = "/stats", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getStatsHtml(final Map<String, Object> model) {
        final List<MetricStatLong> stats = getStats();

        model.put("stats", stats);
        return new ModelAndView("stats", model);
    }

    private ModelAndView handleMetrics(final Map<String, Object> model,
                                       final List<Metric> metrics) {
        model.put("metrics", metrics);
        return new ModelAndView("listMetrics", model);
    }
}
