package com.justin.contrast.metric;

public interface MetricQueue {
    boolean offer(final Metric metric,
                  final long timeoutMs);
    Thread start();
    void stop();
}
