package com.justin.contrast.metric;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static com.justin.contrast.metric.UniqueIdHeaderFilter.HEADER_REQUEST_ID;

public class UniqueIdHeaderFilterTest {

    private ServletRequest mockRequest;
    private UniqueIdHeaderFilter testee;
    private FilterChain mockChain;

    @BeforeEach
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