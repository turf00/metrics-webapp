package com.justin.contrast.spring;

import com.justin.contrast.metric.MetricFacade;
import com.justin.contrast.metrics.JettyMetricLogger;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JettyCustomiser implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    private final MetricFacade facade;

    @Autowired
    public JettyCustomiser(final MetricFacade facade) {
        this.facade = Objects.requireNonNull(facade);
    }

    @Override
    public void customize(final JettyServletWebServerFactory factory) {
        // TODO: Take from configuration property
        final ThreadPool pool = new QueuedThreadPool(500);
        factory.setThreadPool(pool);

        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            final RequestLog defaultLogger = server.getRequestLog();
            // TODO: Move the construction here out in some manner it doesn't care about the facade
            server.setRequestLog(new JettyMetricLogger(defaultLogger, facade));
        });
    }
}
