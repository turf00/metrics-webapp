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
import static com.justin.contrast.metrics.JettyChannelListenerMetricLogger.TIME_TAKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyChannelListenerMetricLoggerTest {
    private static final String URI = "/accounts";
    private static final long BYTES_WRITTEN = 456666;
    private static final String UNIQUE_ID = "12345667456";
    private static final Long TOTAL_TIME = 912L;

    private JettyChannelListenerMetricLogger testee;
    private MetricFacade mockFacade;
    private Request mockRequest;
    private Response mockResponse;

    @Before
    public void setUp() {
        mockFacade = Mockito.mock(MetricFacade.class);
        testee = new JettyChannelListenerMetricLogger(mockFacade);

        mockRequest = Mockito.mock(Request.class);
        mockResponse = Mockito.mock(Response.class);
        when(mockRequest.getResponse()).thenReturn(mockResponse);
    }

    @Test
    public void shouldStoreTimeTakenWhenOnAfterDispatchCalled() {
        when(mockRequest.getTimeStamp()).thenReturn(System.currentTimeMillis());

        testee.onAfterDispatch(mockRequest);

        verify(mockRequest).getTimeStamp();
        verify(mockRequest).setAttribute(eq(TIME_TAKEN), anyLong());
    }

    @Test
    public void shouldEmitEventWhenValuesAreAvailable() {
        final HttpChannel mockChannel = Mockito.mock(HttpChannel.class);
        when(mockRequest.getHttpChannel()).thenReturn(mockChannel);
        when(mockChannel.getBytesWritten()).thenReturn(BYTES_WRITTEN);
        when(mockResponse.getHeader(HEADER_REQUEST_ID)).thenReturn(UNIQUE_ID);
        when(mockRequest.getAttribute(TIME_TAKEN)).thenReturn(TOTAL_TIME);
        when(mockRequest.getMethod()).thenReturn("GET");
        when(mockRequest.getRequestURI()).thenReturn(URI);

        testee.onResponseEnd(mockRequest);

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