package com.justin.contrast.metric;

import com.justin.contrast.metric.http.HttpMethod;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class Metric {
    private final String id;
    private final String uri;
    private final HttpMethod method;
    private final long bytesWritten;
    private final long timeTakenMs;
    private final long timestamp;

    public Metric(final String id,
                  final String uri,
                  final HttpMethod method,
                  final long bytesWritten,
                  final long timeTakenMs) {
        this(id, uri, method, bytesWritten, timeTakenMs, System.currentTimeMillis());
    }

    public Metric(final String id,
                  final String uri,
                  final HttpMethod method,
                  final long bytesWritten,
                  final long timeTakenMs,
                  final long timestamp) {
        this.id = Objects.requireNonNull(id);
        this.uri = Objects.requireNonNull(uri);
        this.method = Objects.requireNonNull(method);
        this.bytesWritten = bytesWritten;
        this.timeTakenMs = timeTakenMs;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public ZonedDateTime getDateTime() {
        final Instant instant = Instant.ofEpochMilli(timestamp);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
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
                getMethod() == metric.getMethod() &&
                Objects.equals(getTimestamp(), metric.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUri(), getMethod(), getBytesWritten(), getTimeTakenMs(), getTimestamp());
    }
}
