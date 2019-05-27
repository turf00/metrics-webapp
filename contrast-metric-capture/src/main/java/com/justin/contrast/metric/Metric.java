package com.justin.contrast.metric;

import java.util.Objects;

public final class Metric {
    private final String id;
    private final String uri;
    private final HttpMethod method;
    private final long bytesWritten;
    private final long timeTakenMs;

    public Metric(final String id,
                  final String uri,
                  final HttpMethod method,
                  final long bytesWritten,
                  final long timeTakenMs) {
        this.id = Objects.requireNonNull(id);
        this.uri = Objects.requireNonNull(uri);
        this.method = Objects.requireNonNull(method);
        this.bytesWritten = bytesWritten;
        this.timeTakenMs = timeTakenMs;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public long getTimeTakenMs() {
        return timeTakenMs;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metric)) {
            return false;
        }
        final Metric metric = (Metric) o;
        return getBytesWritten() == metric.getBytesWritten() &&
                getTimeTakenMs() == metric.getTimeTakenMs() &&
                Objects.equals(getId(), metric.getId()) &&
                Objects.equals(getUri(), metric.getUri()) &&
                getMethod() == metric.getMethod();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUri(), getMethod(), getBytesWritten(), getTimeTakenMs());
    }
}
