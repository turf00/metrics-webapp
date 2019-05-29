package com.justin.contrast.util;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.http.HttpMethod;

public final class MetricGenerator {

    private static final int BYTES_WRITTEN = 123;
    private static final String ACCOUNT = "/account";
    private static final int TIME_TAKEN_MS = 2455;

    private MetricGenerator() { }

    public static Metric metric(final String id) {
        return new Metric(id, ACCOUNT, HttpMethod.GET, BYTES_WRITTEN, TIME_TAKEN_MS);
    }

    public static Metric metric(final String id,
                                final long timestamp) {
        return new Metric(id, ACCOUNT, HttpMethod.GET, BYTES_WRITTEN, TIME_TAKEN_MS, timestamp);
    }
}
