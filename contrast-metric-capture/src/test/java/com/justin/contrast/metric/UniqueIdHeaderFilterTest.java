package com.justin.contrast.metric;

import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static com.justin.contrast.metric.http.UniqueIdHeaderFilter.HEADER_REQUEST_ID;

public class UniqueIdHeaderFilterTest {

    private ServletRequest mockRequest;
    private UniqueIdHeaderFilter testee;
    private FilterChain mockChain;

    @Before
    public void setUp() {
        testee = new UniqueIdHeaderFilter();
        mockRequest = Mockito.mock(ServletRequest.class);
        mockChain = Mockito.mock(FilterChain.class);
    }

    @Test
    public void shouldAddUuidHeaderToHttpServletResponse() throws Exception {
        final HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        testee.doFilter(mockRequest, mockResponse, mockChain);

        Mockito.verify(mockResponse).addHeader(Mockito.eq(HEADER_REQUEST_ID), Mockito.anyString());
        Mockito.verify(mockChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldIgnoreNonHttpServletResponse() throws Exception {
        final ServletResponse mockResponse = Mockito.mock(ServletResponse.class);

        testee.doFilter(mockRequest, mockResponse, mockChain);

        Mockito.verifyZeroInteractions(mockResponse, mockRequest);
        Mockito.verify(mockChain).doFilter(mockRequest, mockResponse);
    }
}