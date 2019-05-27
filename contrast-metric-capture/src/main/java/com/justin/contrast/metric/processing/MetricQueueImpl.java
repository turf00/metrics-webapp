package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricQueue;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MetricQueueImpl implements MetricQueue {
    static final long WAIT_FOR_METRIC_SECS = 2;
    private final BlockingQueue<Metric> queue;
    private final Consumer<Metric> consumer;
    private volatile boolean running;

    MetricQueueImpl(final int capacity,
                    final Consumer<Metric> consumer) {
        queue = new ArrayBlockingQueue<>(capacity,true);
        this.consumer = Objects.requireNonNull(consumer);
    }

    @Override
    public boolean offer(final Metric metric,
                         final long timeoutMs) {
        try {
            return queue.offer(metric, timeoutMs, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public Thread start() {
        final Runnable runnable = () -> {
            while(running) {
                // pull metrics from the queue and process
                final Metric metric;
                try {
                    metric = queue.poll(WAIT_FOR_METRIC_SECS, TimeUnit.SECONDS);
                    consumer.accept(metric);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        return start(runnable);
    }

    Thread start(Runnable runnable) {
        if (!running) {
            running = true;
            final Thread thread = new Thread(runnable, "MetricQueueProcessor");
            thread.start();
            return thread;
        }
        return null;
    }

    @Override
    public void stop() {
        running = false;
        queue.clear();
    }

}
