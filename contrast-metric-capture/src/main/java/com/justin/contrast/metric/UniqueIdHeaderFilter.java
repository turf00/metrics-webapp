package com.justin.contrast.metric;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UniqueIdHeaderFilter implements Filter {

    public static final String HEADER_REQUEST_ID = "X-Contrast-Request-Id";

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).addHeader(HEADER_REQUEST_ID, UUID.randomUUID().toString());
        }

        chain.doFilter(request, response);
    }
}
