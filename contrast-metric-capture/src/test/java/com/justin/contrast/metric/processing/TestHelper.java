package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.http.HttpMethod;

public final class TestHelper {
    private TestHelper() { }

    public static Metric metric() {
        return new Metric("id", "uri", HttpMethod.GET, 123, 123);
    }
}
