package com.justin.contrast;

public final class Urls {
    private Urls() { }

    static final String ACCOUNT = "/accounts";
    static final String METRICS = "/metrics";
    static final String STATS = "/stats";

    public static String urlGetAccount(final String accountId) {
        return String.format("%s/%s", ACCOUNT, accountId);
    }

    public static String urlGetAllMetrics() {
        return METRICS;
    }

    public static String urlGetMetricStats() {
        return METRICS + STATS;
    }

    public static String urlGetMetric(final String metricId) {
        return String.format("%s/%s", METRICS, metricId);
    }
}
