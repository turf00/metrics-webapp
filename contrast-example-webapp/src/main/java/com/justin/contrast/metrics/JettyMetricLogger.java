package com.justin.contrast.metrics;

import com.justin.contrast.metric.HttpMethod;
import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.UniqueIdHeaderFilter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;

public class JettyMetricLogger implements RequestLog {

    private final RequestLog defaultLogger;
    private final MetricFacade metrics;

    public JettyMetricLogger(final RequestLog defaultLogger,
                             final MetricFacade metricFacade) {
        this.defaultLogger = defaultLogger;
        metrics = metricFacade;
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
        if (uniqueId != null) {

            final Metric metric = new Metric(uniqueId, uri, HttpMethod.GET, bytesWritten, timeTakenMs);
            metrics.emit(metric);
        }

        System.out.printf("URI: [%s], time timeTakenMs: [%dms], bytes: [%d], id: [%s]%n", uri, timeTakenMs, bytesWritten, uniqueId);
    }

    private void doDefault(final Request request,
                           final Response response) {
        if (defaultLogger != null) {
            defaultLogger.log(request, response);
        }
    }
}
