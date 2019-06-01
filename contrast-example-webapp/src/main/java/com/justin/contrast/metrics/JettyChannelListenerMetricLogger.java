package com.justin.contrast.metrics;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.http.HttpMethod;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;

import java.util.Objects;

public class JettyChannelListenerMetricLogger implements HttpChannel.Listener {

    static final String TIME_TAKEN = "com.justin.timeTakenMs";

    private final MetricFacade metrics;

    public JettyChannelListenerMetricLogger(final MetricFacade metricFacade) {
        this.metrics = Objects.requireNonNull(metricFacade);
    }

    @Override
    public void onAfterDispatch(final Request request) {
        final long timeTakenMs = System.currentTimeMillis() - request.getTimeStamp();
        request.setAttribute(TIME_TAKEN, timeTakenMs);
    }

    @Override
    public void onResponseEnd(Request request) {
        final Long timeTakenMs = (Long) request.getAttribute(TIME_TAKEN);
        final HttpChannel httpChannel = request.getHttpChannel();
        final long bytesWritten = request.getHttpChannel().getBytesWritten();
        final String uniqueId = request.getResponse().getHeader(UniqueIdHeaderFilter.HEADER_REQUEST_ID);
        final String uri = request.getRequestURI();

        // Unique only added to certain paths, therefore ignore other paths if header not set
        if (uniqueId != null && timeTakenMs != null) {
            final HttpMethod method = HttpMethod.fromString(request.getMethod());
            final Metric metric = new Metric(uniqueId, uri, method, bytesWritten, timeTakenMs, request.getTimeStamp());
            metrics.emit(metric);
        }
    }

}
