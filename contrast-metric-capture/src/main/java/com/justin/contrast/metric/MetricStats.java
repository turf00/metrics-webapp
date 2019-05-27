package com.justin.contrast.metric;

public final class MetricStats {
    private final MetricStatLong requestTime;
    private final MetricStatLong responseSize;

    public MetricStats() {
        this(new MetricStatLong("requestTime (ms)"), new MetricStatLong("responseSize (bytes)"));
    }

    MetricStats(final MetricStatLong requestTime,
                final MetricStatLong responseSize) {
        this.requestTime = requestTime;
        this.responseSize = responseSize;
    }

    public MetricStats update(final long responseSize,
                              final long requestTime) {
        final MetricStatLong newSize = this.responseSize.update(responseSize);
        final MetricStatLong newTime = this.requestTime.update(requestTime);

        return new MetricStats(newTime, newSize);
    }

    public MetricStatLong getRequestTime() {
        return requestTime;
    }

    public MetricStatLong getResponseSize() {
        return responseSize;
    }
}
