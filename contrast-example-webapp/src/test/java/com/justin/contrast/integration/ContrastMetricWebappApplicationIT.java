package com.justin.contrast.integration;

import com.justin.contrast.ContrastMetricWebappApplication;
import com.justin.contrast.Urls;
import com.justin.contrast.domain.Account;
import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStatLong;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.justin.contrast.metric.MetricStats.STAT_REQUEST_TIME;
import static com.justin.contrast.metric.MetricStats.STAT_RESPONSE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContrastMetricWebappApplication.class)
@AutoConfigureMockMvc
public class ContrastMetricWebappApplicationIT {

    private static final String ACCOUNT_ID = "123456789";

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void shouldRespondCorrectlyWhenMakingAccountRequestThenFetchingMetricsAndStats() {
        final String getAccountUri = Urls.urlGetAccount(ACCOUNT_ID);

        // Get Account
        final ResponseEntity<Account> responseAccount = rest.exchange(getAccountUri,
                HttpMethod.GET,
                null,
                Account.class);

        final Account foundAccount = responseAccount.getBody();
        final String uniqueId = responseAccount.getHeaders().getFirst(UniqueIdHeaderFilter.HEADER_REQUEST_ID);

        assertThat(foundAccount.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(uniqueId).isNotBlank();

        // Get Metric by Unique ID
        final ResponseEntity<Metric> responseMetric = rest.exchange(Urls.urlGetMetric(uniqueId),
                HttpMethod.GET,
                null,
                Metric.class);
        Metric metricFound = responseMetric.getBody();

        assertMetric(uniqueId, getAccountUri, metricFound);

        // Get All Metrics
        final ResponseEntity<List<Metric>> responseMetricList = rest.exchange(Urls.urlGetAllMetrics(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Metric>>() {
        });

        final List<Metric> metricList = responseMetricList.getBody();
        assertThat(metricList.size()).isEqualTo(1);
        metricFound = metricList.get(0);
        assertMetric(uniqueId, getAccountUri, metricFound);

        // Get Metric Stats
        final ResponseEntity<List<MetricStatLong>> responseStats = rest.exchange(Urls.urlGetMetricStats(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MetricStatLong>>() {
                });

        final List<MetricStatLong> metricStats = responseStats.getBody();
        assertThat(metricStats.size()).isEqualTo(2);
        assertStats(metricStats, metricFound);
    }

    @SuppressWarnings ("OptionalGetWithoutIsPresent")
    private void assertStats(final List<MetricStatLong> statsFound,
                             final Metric metric) {
        final MetricStatLong statRequestTime = statsFound.stream()
                .filter(s -> s.getName().equalsIgnoreCase(STAT_REQUEST_TIME))
                .findFirst().get();

        assertThat(statRequestTime.getCount()).isEqualTo(1);
        assertThat(statRequestTime.getMean()).isEqualTo(metric.getTimeTakenMs());
        assertThat(statRequestTime.getMax()).isEqualTo(metric.getTimeTakenMs());
        assertThat(statRequestTime.getMin()).isEqualTo(metric.getTimeTakenMs());
        assertThat(statRequestTime.getTotal()).isEqualTo(metric.getTimeTakenMs());

        final MetricStatLong statResponseSize = statsFound.stream()
                .filter(s -> s.getName().equalsIgnoreCase(STAT_RESPONSE_SIZE))
                .findFirst().get();

        assertThat(statResponseSize.getCount()).isEqualTo(1);
        assertThat(statResponseSize.getTotal()).isEqualTo(metric.getBytesWritten());
        assertThat(statResponseSize.getMin()).isEqualTo(metric.getBytesWritten());
        assertThat(statResponseSize.getMax()).isEqualTo(metric.getBytesWritten());
        assertThat(statResponseSize.getMean()).isEqualTo(metric.getBytesWritten());
    }

    private void assertMetric(final String uniqueId,
                              final String uri,
                              final Metric found) {
        assertThat(found.getId()).isEqualTo(uniqueId);
        assertThat(found.getUri()).isEqualTo(uri);
        assertThat(found.getTimeTakenMs()).isGreaterThan(0);
        assertThat(found.getBytesWritten()).isGreaterThan(0);
        assertThat(found.getMethod()).isEqualTo(com.justin.contrast.metric.http.HttpMethod.GET);
    }

}
