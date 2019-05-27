package com.justin.contrast.metric.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

// TODO: Tests, once we decide on usage
public class MetricFilter implements Filter {
    private static final String UNKNOWN_URL = "unknown";

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        final long start = System.nanoTime();

        final String url = getUrl(request);

        chain.doFilter(request, response);

        final long end = System.nanoTime();
        System.out.printf("Url: [%s], time taken: [%dms]%n", url, TimeUnit.NANOSECONDS.toMillis(end - start));
    }

    private static String getUrl(final ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getRequestURI();
        }
        return UNKNOWN_URL;
    }
}
