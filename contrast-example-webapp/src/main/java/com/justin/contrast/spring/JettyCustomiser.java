package com.justin.contrast.spring;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JettyCustomiser implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    @Value("${app.jetty.threads}")
    private int jettyThreads;

    @Override
    public void customize(final JettyServletWebServerFactory factory) {
        final ThreadPool pool = new QueuedThreadPool(jettyThreads);
        factory.setThreadPool(pool);
    }
}
