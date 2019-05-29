package com.justin.contrast.spring;

import com.justin.contrast.metrics.JettyMetricLogger;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JettyCustomiser implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Value("${app.jetty.threads}")
    private int jettyThreads;

    private final JettyMetricLogger metricLogger;

    @Autowired
    public JettyCustomiser(final JettyMetricLogger metricLogger) {
        this.metricLogger = Objects.requireNonNull(metricLogger);
    }

    @Override
    public void customize(final JettyServletWebServerFactory factory) {
        final ThreadPool pool = new QueuedThreadPool(jettyThreads);
        factory.setThreadPool(pool);

        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            final RequestLog defaultLogger = server.getRequestLog();
            metricLogger.setDefaultLogger(defaultLogger);
            server.setRequestLog(metricLogger);
        });
    }
}
