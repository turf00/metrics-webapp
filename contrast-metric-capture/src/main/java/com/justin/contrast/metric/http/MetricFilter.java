package com.justin.contrast.metric.http;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Objects;

public class MetricFilter implements Filter {
    private final MetricFacade metricFacade;

    public MetricFilter(final MetricFacade metricFacade) {
        this.metricFacade = Objects.requireNonNull(metricFacade);
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        final long start = System.currentTimeMillis();

        final ServletResponseSizeCounter counterResponse;
        ServletResponse targetResponse = response;

        if (response instanceof HttpServletResponse) {
            counterResponse = new ServletResponseSizeCounter(response);
            targetResponse = counterResponse;
        }
        else {
            counterResponse = null;
        }

        chain.doFilter(request, targetResponse);

        if (counterResponse != null && counterResponse.getUniqueId() != null) {
            final long timeTakenMs = System.currentTimeMillis() - start;
            final long bytesWritten = counterResponse.getBytesWritten();

            metricFacade.emit(metric(request, counterResponse.getUniqueId(), bytesWritten, timeTakenMs, start));
        }
    }

    private static Metric metric(final ServletRequest request,
                                 final String uniqueId,
                                 final long bytesWritten,
                                 final long timeTakenMs,
                                 final long timestamp) {

        if (uniqueId != null && request instanceof HttpServletRequest) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final String uri = httpRequest.getRequestURI();
            final HttpMethod method = HttpMethod.fromString(httpRequest.getMethod());

            return new Metric(uniqueId, uri, method, bytesWritten, timeTakenMs, timestamp);
        }
        else {
            return null;
        }
    }

    private static class ServletResponseSizeCounter extends HttpServletResponseWrapper {
        private final CountingServletOutputStream os;

        ServletResponseSizeCounter(final ServletResponse response)
                throws IOException {
            super((HttpServletResponse) response);
            os = new CountingServletOutputStream(response.getOutputStream());
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return os;
        }

        long getBytesWritten() {
            return os.getBytesWritten();
        }

        String getUniqueId() {
            return ((HttpServletResponse) getResponse()).getHeader(UniqueIdHeaderFilter.HEADER_REQUEST_ID);
        }
    }

}
