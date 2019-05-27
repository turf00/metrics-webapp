package com.justin.contrast.metric;

import java.util.Objects;

public final class MetricStatLong {
    private final String name;
    private final long count;
    private final long total;
    private final Long min;
    private final long max;
    private final long mean;

    public MetricStatLong(final String name) {
        this.name = Objects.requireNonNull(name);
        count = 0;
        total = 0;
        min = null;
        max = 0;
        mean = 0;
    }

    MetricStatLong(final String name,
                   final long count,
                   final long total,
                   final long min,
                   final long max) {
        this.name = name;
        this.count = count;
        this.total = total;
        this.min = min;
        this.max = max;
        this.mean = total / count;
    }

    public MetricStatLong update(final long metric) {

        return new MetricStatLong(name,
                count + 1,
                total + metric,
                newMin(metric),
                Math.max(metric, max));
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    public long getTotal() {
        return total;
    }

    public long getMin() {
        return min == null ? 0 : min;
    }

    public long getMax() {
        return max;
    }

    public long getMean() {
        return mean;
    }

    private Long newMin(final long newPossible) {
        return min == null ? newPossible : Math.min(min, newPossible);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetricStatLong)) {
            return false;
        }
        final MetricStatLong that = (MetricStatLong) o;
        return getCount() == that.getCount() &&
                getTotal() == that.getTotal() &&
                getMax() == that.getMax() &&
                getMean() == that.getMean() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getMin(), that.getMin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCount(), getTotal(), getMin(), getMax(), getMean());
    }
}
