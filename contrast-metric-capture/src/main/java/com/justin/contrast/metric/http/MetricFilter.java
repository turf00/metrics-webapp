package com.justin.contrast.metric.http;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

// TODO: Tests, once we decide on usage
// TODO: Add ability to capture size of outputstream, etc.
public class MetricFilter implements Filter {
    private static final String UNKNOWN_URL = "unknown";

    private final MetricFacade metricFacade;

    public MetricFilter(final MetricFacade metricFacade) {
        this.metricFacade = Objects.requireNonNull(metricFacade);
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        final long start = System.currentTimeMillis();

        chain.doFilter(request, response);

        final long end = System.currentTimeMillis();
        final String url = getUrl(request);

        final long timeTakenMs = end - start;

        //metricFacade.emit()
    }

    private static Metric metric() {
        return null;
    }

    private static String getUrl(final ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getRequestURI();
        }
        return UNKNOWN_URL;
    }
}
