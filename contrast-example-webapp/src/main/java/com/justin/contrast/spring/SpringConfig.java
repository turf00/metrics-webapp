package com.justin.contrast.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metric.http.UniqueIdHeaderFilter;
import com.justin.contrast.metric.processing.MetricFacadeImpl;
import com.justin.contrast.metrics.JettyMetricLogger;
import com.justin.contrast.metrics.MetricFacadeSpring;
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

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

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

    // TODO: remove
    /*@Bean
    public FilterRegistrationBean<MetricFilter> loggingFilter(final MetricFacade metricFacade) {
        final FilterRegistrationBean<MetricFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new MetricFilter(metricFacade));
        filter.addUrlPatterns("/accounts/*");
        filter.setName("Metric logging filter");
        filter.setOrder(LOWEST_PRECEDENCE);

        return filter;
    }*/

    @Bean
    public FilterRegistrationBean<UniqueIdHeaderFilter> uniqueIdHeaderFilter() {
        final FilterRegistrationBean<UniqueIdHeaderFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new UniqueIdHeaderFilter());
        filter.addUrlPatterns("/accounts/*");
        filter.setName("UniqueIdHeaderFilter");

        return filter;
    }

    @Bean
    public MetricFacade metricFacade() {
        final MetricFacadeImpl metricFacade = new MetricFacadeImpl(metricBufferSize, metricQueueSize);
        return new MetricFacadeSpring(metricFacade);
    }

    @Bean
    public JettyMetricLogger metricLogger(final MetricFacade metricFacade) {
        return new JettyMetricLogger(metricFacade);
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule(PROPERTIES));

        return objectMapper;
    }
}
