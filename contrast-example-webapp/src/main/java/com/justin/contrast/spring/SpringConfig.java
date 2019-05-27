package com.justin.contrast.spring;

import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.http.MetricFilter;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import com.justin.contrast.metric.processing.MetricFacadeImpl;
import com.justin.contrast.service.AccountService;
import com.justin.contrast.service.MetricService;
import com.justin.contrast.service.TransactionService;
import com.justin.contrast.service.impl.AccountServiceImpl;
import com.justin.contrast.service.impl.MetricServiceImpl;
import com.justin.contrast.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
public class SpringConfig {

    @Value("${app.metric.buffer.size}")
    private int metricBufferSize;
    @Value("${app.metric.queue.size}")
    private int metricQueueSize;

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionServiceImpl();
    }

    @Bean
    public MetricService metricService(final MetricFacade facade) {
        return new MetricServiceImpl(facade);
    }

    @Bean
    public FilterRegistrationBean<MetricFilter> loggingFilter() {
        final FilterRegistrationBean<MetricFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new MetricFilter());
        filter.addUrlPatterns("/accounts/*");
        filter.setName("Metric logging filter");
        filter.setOrder(LOWEST_PRECEDENCE);

        return filter;
    }

    @Bean
    public FilterRegistrationBean<UniqueIdHeaderFilter> uniqueIdHeaderFilter() {
        final FilterRegistrationBean<UniqueIdHeaderFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new UniqueIdHeaderFilter());
        filter.addUrlPatterns("/accounts/*");
        filter.setName("UniqueIdHeaderFilter");

        return filter;
    }

    // TODO: Requires start stop mechanics to follow spring lifecycle
    @Bean
    public MetricFacade metricFacade() {
        // TODO: Take these values from configuration
        final MetricFacadeImpl metricFacade = new MetricFacadeImpl(metricBufferSize, metricQueueSize);
        metricFacade.start();
        return metricFacade;
    }
}
