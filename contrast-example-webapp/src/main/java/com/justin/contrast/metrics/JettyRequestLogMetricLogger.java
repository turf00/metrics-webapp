package com.justin.contrast.metrics;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.http.HttpMethod;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;

import java.util.Objects;

public class JettyRequestLogMetricLogger implements RequestLog {

    private volatile RequestLog defaultLogger;
    private final MetricFacade metrics;

    public JettyRequestLogMetricLogger(final RequestLog defaultLogger,
                                       final MetricFacade metricFacade) {
        this.defaultLogger = defaultLogger;
        metrics = Objects.requireNonNull(metricFacade);
    }

    public JettyRequestLogMetricLogger(final MetricFacade metricFacade) {
        this(null, metricFacade);
    }

    public void setDefaultLogger(final RequestLog defaultLogger) {
        this.defaultLogger = defaultLogger;
    }

    @Override
    public void log(final Request request,
                    final Response response) {
        final long end = System.currentTimeMillis();

        // Perform the standard logging if set
        doDefault(request, response);

        final String uri = request.getRequestURI();
        final long bytesWritten = response.getHttpChannel().getBytesWritten();
        final long timeTakenMs = end - request.getTimeStamp();
        final String uniqueId = response.getHeader(UniqueIdHeaderFilter.HEADER_REQUEST_ID);

        // Unique only added to certain paths, therefore ignore other paths if header not set
        if (uniqueId != null) {
            final HttpMethod method = HttpMethod.fromString(request.getMethod());
            final Metric metric = new Metric(uniqueId, uri, method, bytesWritten, timeTakenMs, request.getTimeStamp());
            metrics.emit(metric);
        }
    }

    private void doDefault(final Request request,
                           final Response response) {
        if (defaultLogger != null) {
            defaultLogger.log(request, response);
        }
    }
}
