package com.justin.contrast.metric.http;

import com.justin.contrast.metric.MetricFacade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.justin.contrast.metric.http.UniqueIdHeaderFilter.HEADER_REQUEST_ID;
import static org.mockito.Mockito.when;

public class MetricFilterTest {
    private static final String URI = "/accounts";
    private static final String UNIQUE_ID = "12345667456";

    private MetricFacade mockFacade;
    private MetricFilter testee;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private FilterChain filterChain;

    @Before
    public void setUp() throws IOException {
        mockFacade = Mockito.mock(MetricFacade.class);
        testee = new MetricFilter(mockFacade);

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        filterChain = new WriteSomeDataFilter();
        final ByteArrayServletOutputStream servletOs = new ByteArrayServletOutputStream();

        when(mockResponse.getOutputStream()).thenReturn(servletOs);
    }

    @Test
    public void shouldSkipIfUniqueIdNotSet()
            throws IOException, ServletException {
        when(mockResponse.getHeader(HEADER_REQUEST_ID)).thenReturn(null);

        testee.doFilter(mockRequest, mockResponse, filterChain);

        Mockito.verifyZeroInteractions(mockFacade);
    }

    @Test
    public void shouldEmitMetricAsExpected() throws IOException, ServletException {
        when(mockResponse.getHeader(HEADER_REQUEST_ID)).thenReturn(UNIQUE_ID);
        when(mockRequest.getMethod()).thenReturn("GET");
        when(mockRequest.getRequestURI()).thenReturn(URI);

        testee.doFilter(mockRequest, mockResponse, filterChain);
    }

    @Test
    public void shouldStartMetricFacadeOnInit() {
        final FilterConfig mockConfig = Mockito.mock(FilterConfig.class);

        testee.init(mockConfig);

        Mockito.verify(mockFacade).start();
    }

    @Test
    public void shouldStopMetricFacadeOnInit() {
        testee.destroy();

        Mockito.verify(mockFacade).stop();
    }

    private class WriteSomeDataFilter implements Filter, FilterChain {

        @Override
        public void doFilter(final ServletRequest request,
                             final ServletResponse response,
                             final FilterChain chain) throws IOException {
            final byte[] b = new byte[1025];
            response.getOutputStream().write(b);
        }

        @Override
        public void doFilter(final ServletRequest request,
                             final ServletResponse response) throws IOException {
            doFilter(request, response, null);
        }
    }

    private class ByteArrayServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos = new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(final WriteListener writeListener) {
            // ignore
        }

        @Override
        public void write(final int b) {
            bos.write(b);
        }
    }

}