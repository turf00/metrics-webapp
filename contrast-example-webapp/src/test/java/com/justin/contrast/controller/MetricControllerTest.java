package com.justin.contrast.controller;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStats;
import com.justin.contrast.service.MetricService;
import com.justin.contrast.util.MetricGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith (SpringRunner.class)
@WebMvcTest (MetricController.class)
public class MetricControllerTest {

    private static final String METRIC_ID = "1234567";
    private static final Metric METRIC = MetricGenerator.metric(METRIC_ID, 1559067133968L);
    private static final MetricStats STATS = new MetricStats();
    private static final String EXPECTED_METRIC = "{\"id\":\"1234567\",\"uri\":\"/account\",\"method\":\"GET\"" +
            ",\"bytesWritten\":123,\"timeTakenMs\":2455,\"timestamp\":1559067133968,\"dateTime\":\"2019-05-28T18:12:13.968Z\"}";
    private static final String EXPECTED_METRIC_LIST = String.format("[%s]", EXPECTED_METRIC);
    private static final String EXPECTED_STATS = "[{\"name\":\"requestTime (ms)\",\"count\":0,\"total\":0,\"min\":0,\"max\":0,\"mean\":0}," +
            "{\"name\":\"responseSize (bytes)\",\"count\":0,\"total\":0,\"min\":0,\"max\":0,\"mean\":0}]";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MetricService mockService;

    @Test
    public void shouldReturn200WithAllMetricsWhenRequested() throws Exception {
        when(mockService.getAll())
                .thenReturn(Collections.singletonList(METRIC));

        mockMvc.perform(get("/metrics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(equalTo(EXPECTED_METRIC_LIST)));
    }

    @Test
    public void shouldReturn200WithSingleMetricWhenRequestedById() throws Exception {
        when(mockService.getMetricById(METRIC_ID))
                .thenReturn(METRIC);

        mockMvc.perform(get("/metrics/" + METRIC_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(EXPECTED_METRIC));
    }

    @Test
    public void shouldReturn200WithStatsWhenRequested() throws Exception {
        when(mockService.getMetricStats())
                .thenReturn(STATS);

        mockMvc.perform(get("/metrics/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(EXPECTED_STATS));
    }

    @Test
    public void shouldReturn200WithModelWhenSearchingForId() throws Exception {
        when(mockService.getMetricById(METRIC_ID))
                .thenReturn(METRIC);

        mockMvc.perform(get("/metrics/search?id=" + METRIC_ID)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE));
    }

    @Test
    public void shouldReturn200HtmlForStatsWhenRequestingStats() throws Exception {
        when(mockService.getMetricStats())
                .thenReturn(STATS);

        mockMvc.perform(get("/metrics/stats")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void shouldReturn200HtmlForAllMetricsWhenRequestingAll() throws Exception {
        when(mockService.getAll())
                .thenReturn(Collections.singletonList(METRIC));

        mockMvc.perform(get("/metrics")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

}