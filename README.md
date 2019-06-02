# Metrics WebApp

[![Build Status](https://travis-ci.com/turf00/metrics-webapp.svg?branch=master)](https://travis-ci.org/turf00/metrics-webapp)
[![codecov](https://codecov.io/gh/turf00/metrics-webapp/branch/master/graph/badge.svg)](https://codecov.io/gh/turf00/metrics-webapp)

## What

An example Spring Boot application showing the capturing of metrics related to HTTP requests.

### Modules

| Module                  | Purpose                                                                                                                                                                                                                                                                                                        |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| contrast-metric-capture | Provides the functionality to capture the metrics and process them, as well as fetch metrics, metric stats, etc.  This module is generic and has very few dependencies.                                                                                                                                        |
| contrast-example-webapp | This is the Spring Boot runnable web application that provides RESTful endpoints to query.  In essence this is an example app showing how to use the metrics capture.  It also provides a small number of very basic looking HTML pages for viewing and fetching metrics.  Metrics are also available via the RESTful API. |
| contrast-perftest       | This is a very basic performance test for the application, which I used during development.  It uses Gatling (Scala) and can be executed via: `mvn gatling:test`                                                                                                                                               |

## Why

This section contains some information on design considerations.

### General

+ Maven was used over Gradle as I have more experience with Maven.  Although for any new projects I would prefer to use Gradle.
+ I was forced to use Junit 4 rather than Junit 5 as code coverage did not appear to work correctly with Jacoco and Junit 5.
+ I used Spring Boot and Spring Web MVC for implementing the APIs that we are going to capture metrics on.
+ Mustache was used as a simple templating language for the very basic UI pages to fetch metrics.
+ I created some RESTful endpoints rather than pages as I am more familiar with that.  Most Web UI experience I have is with React and JS.
+ I used Jetty as the container for the Spring Boot app as I prefer it to Tomcat.

### Unique Header

The `UniqueIdHeaderFilter` in `contrast-metric-capture` adds a unique header to each response which is a UUID.  This can then be used to match metrics to the response.

### Metric Capture

I created three methods of capturing the metrics:

+ `MetricFilter` which is a standard servlet Filter.  It is to be executed first and last in the filter chain.  It is the most generic of the options and could be wired into any scenario where the Servlet API is available.  It is the method I went with in the end as its most generic.  It is defined in `contrast-metric-capture` package: `com.justin.contrast.metric.http`.  A wrapper for the `ServletOutputStream` records how many bytes have been written for our metric capture.

The following are specific to Jetty only:

+ `JettyChannelListenerMetricLogger` which uses the Jetty `HttpChannel.Listener` interface to capture the metrics.  This could be enabled by the following code in `JettyCustomiser`:
```java
private final JettyChannelListenerMetricLogger metricLogger;

    @Autowired
    public JettyCustomiser(final JettyChannelListenerMetricLogger metricLogger) {
        this.metricLogger = Objects.requireNonNull(metricLogger);
    }

    @Override
    public void customize(final JettyServletWebServerFactory factory) {
        final ThreadPool pool = new QueuedThreadPool(jettyThreads);
        factory.setThreadPool(pool);

        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            for (final Connector c : server.getConnectors()) {
                c.addBean(metricLogger);
            }
        });
    }
```

+ `JettyRequestLogMetricLogger` which uses the standard Jetty request logging to emit the metrics and could be enabled as follows via `JettyCustomiser`:
```java
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

```

### Metric Emission & Processing

+ In order to remove the need for shared state when possible and to avoid delaying the response back to the client for metric capture overhead, the emitted metrics are immediately added to a blocking queue.  The emit will wait a maximum of 10ms and then give up.  In this manner a failure to process metrics should not have a large negative impact on the response time for API as a whole.
+ From the start I wanted to be able to retain a limited number of metrics in memory rather than using all available memory for metrics and I also wanted to be able to lookup the metrics fast without having to scan through a full data structure.
+ In order to achieve this I made a very simple ring buffer and combined it with a HashMap, the later being used for lookup.  The ring buffer in essence keeps track of which metrics are valid currently by age and when an item is removed from the ring buffer it will be removed from the Map.
+ I am fully aware that currently this is not thread safe for multiple threads but it is designed to be used by a single metric consumer thread.  The problem would be if metrics were being added very quickly an item might be removed from the ring buffer before it has actually been added to the Map and therefore remain orphaned in the Map if added after.
  + This could be addressed by making these items synchronized or also by processing the remove in a further background, for instance if the remove shows nothing in the Map this would be an indication of the above scenario.
+ In order to avoid shared state the actual metric stats are immutable which will incur an overhead of object allocation.  I would probably redesign this if creating it again.

## Test APIs

Rather than pages I've added RESTful endpoints to the application as I am more familiar with these and haven't done a lot of web UI work in Java.

The list of endpoints available can be found in the Swagger docs here: http://localhost:8080/swagger-ui.html#

The general APIs represent the Accounts and Transactions for a bank.

## Web UI for getting metrics

The following pages are available for fetching metrics:

+ List all currently stored metrics: <http://localhost:8080/metrics>
+ The above page also provides a search option to find metrics by a unique id.  This can also be used by replacing uniqueId in the following URL: <http://localhost:8080/metrics/search?id=uniqueId>
+ View the metric stats: <http://localhost:8080/metrics/stats>

## Using the app

### Building the app

You can build the app with Maven (3.6.0+) and Java 8 installed.  The following should be executed in the root of the project:

`mvn clean package`

### Running the app

You can start the app from the command line as follows in the root of the project:

`java -jar contrast-example-webapp/target/contrast-example-webapp-0.1-SNAPSHOT.jar`

### Application configuration

There are a number of configuration settings which can be adjusted from the command line: by setting properties or changing the values in `application.properties`:

| Property               | Purpose                                                                                                                                                                                                | Default |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| app.metric.queue.size  | Size of the queue used to hold metrics for processing.  This should be larger than the expected maximum number of concurrent requests to the web applications.  I.e. the property `app.jetty.threads`. | 4098    |
| app.metric.buffer.size | How many metrics to retain in memory at one time.                                                                                                                                                      | 2048    |
| app.jetty.threads      | Maximum number of request handling threads to allow, i.e. the max number of concurrent requests that can be processed at once.                                                                         | 500     |

Here is an example of setting one of these values and starting the app:

`java -Dapp.jetty.threads=998 -jar contrast-example-webapp/target/contrast-example-webapp-0.1-SNAPSHOT.jar`

## Future Work

+ Dockerise the application and the build.
+ The consumer that parses the incoming metrics to produce the stats, etc should handle parsing multiple metrics at a single time.  This would involve using smart batching, i.e. batching by count and time.
+ Replace the usage of immutable metric stats with a single class that captures the stats, therefore eliminating the need to allocate the objects each time we have new metrics or a batch of new metrics arrive.
+ In order to remove contention on the blocking queue and move the flow from Multi Producer Single consumer, to Single Producer, Single Consumer, each thread could have its own queue that it adds metrics to.  These are then processed onto a centralised queue, or perhaps consumed as they become available by a single consumer.  This would ease any contention on the queue.
+ I have done zero logging currently and would add this for specific scenarios, i.e. when metrics cannot be added to the queue, etc.
+ Make the perf test also fetch some metrics and enforce that they are valid.
