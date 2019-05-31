package com.justin.contrast.metrics;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.http.HttpMethod;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static com.justin.contrast.metric.http.UniqueIdHeaderFilter.HEADER_REQUEST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyMetricLoggerTest {
    private static final String URI = "/accounts";
    private static final long BYTES_WRITTEN = 456666;
    private static final String UNIQUE_ID = "12345667456";

    private JettyMetricLogger testee;
    private MetricFacade mockFacade;
    private Request mockRequest;
    private Response mockResponse;

    @Before
    public void setUp() {
        mockFacade = Mockito.mock(MetricFacade.class);
        testee = new JettyMetricLogger(mockFacade);
        mockRequest = Mockito.mock(Request.class);
        mockResponse = Mockito.mock(Response.class);
    }

    @Test
    public void shouldEmitMetricAsExpected() {
        when(mockRequest.getRequestURI()).thenReturn(URI);
        when(mockRequest.getMethod()).thenReturn("GET");

        final HttpChannel mockChannel = Mockito.mock(HttpChannel.class);
        when(mockResponse.getHttpChannel()).thenReturn(mockChannel);
        when(mockChannel.getBytesWritten()).thenReturn(BYTES_WRITTEN);
        when(mockResponse.getHeader(HEADER_REQUEST_ID)).thenReturn(UNIQUE_ID);

        testee.log(mockRequest, mockResponse);

        ArgumentCaptor<Metric> argument = ArgumentCaptor.forClass(Metric.class);
        verify(mockFacade).emit(argument.capture());

        final Metric found = argument.getValue();
        final Metric expected = buildFromExisting(found);

        assertThat(found).isEqualTo(expected);
    }

    private Metric buildFromExisting(final Metric found) {
        return new Metric(UNIQUE_ID, URI, HttpMethod.GET, BYTES_WRITTEN, found.getTimeTakenMs(), found.getTimestamp());
    }
}
