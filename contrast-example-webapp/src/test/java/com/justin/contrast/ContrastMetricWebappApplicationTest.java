package com.justin.contrast;

import com.justin.contrast.controller.AccountController;
import com.justin.contrast.controller.MetricController;
import com.justin.contrast.controller.TransactionController;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContrastMetricWebappApplicationTest {

    // TODO: Further bean tests
    @Autowired
    private AccountController accountController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private MetricController metricController;
    @Autowired
    private FilterRegistrationBean<UniqueIdHeaderFilter> filterRegistrationBean;

    @Test
    public void contextLoads() {
        assertThat(accountController).isNotNull();
        assertThat(transactionController).isNotNull();
        assertThat(metricController).isNotNull();

        assertThat(filterRegistrationBean).isNotNull();
    }

}
