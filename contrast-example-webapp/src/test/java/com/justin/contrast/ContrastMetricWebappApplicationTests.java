package com.justin.contrast;

import com.justin.contrast.controller.AccountController;
import com.justin.contrast.controller.MetricController;
import com.justin.contrast.controller.TransactionController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContrastMetricWebappApplicationTests {

    // TODO: Further bean tests
    @Autowired
    private AccountController accountController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private MetricController metricController;

    @Test
    public void contextLoads() {
        assertThat(accountController).isNotNull();
        assertThat(transactionController).isNotNull();
        assertThat(metricController).isNotNull();
    }

}
