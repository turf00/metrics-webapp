package com.justin.contrast.metric.processing;

import com.justin.contrast.metric.Metric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.justin.contrast.metric.processing.MetricQueueImpl.WAIT_FOR_METRIC_SECS;
import static org.assertj.core.api.Assertions.assertThat;

public class MetricQueueImplTest {

    private MetricQueueImpl testee;
    private Consumer<Metric> mockConsumer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        mockConsumer = Mockito.mock(Consumer.class);
        testee = new MetricQueueImpl(1, mockConsumer);
    }

    @After
    public void tearDown() {
        testee.stop();
    }

    @Test
    public void shouldTimeoutIfNotAbleToAddToQueueWithinTimeFrame() {
        final Metric metric = TestHelper.metric();

        final boolean resultFirst = testee.offer(metric, 1);
        final boolean resultSecond = testee.offer(metric, 50);

        assertThat(resultFirst).isTrue();
        assertThat(resultSecond).isFalse();
    }

    @Test
    public void shouldConsumeMessagesWhenAddedToQueueAndRunning() throws Exception {
        final Metric metric = TestHelper.metric();
        testee.start();

        final boolean result = testee.offer(metric, 1);

        assertThat(result).isTrue();
        // Give time for consumption, potential test code smell
        TimeUnit.SECONDS.sleep(2);
        Mockito.verify(mockConsumer).accept(metric);
    }

    @Test
    public void shouldHonorStartAndStopForQueue() throws Exception {
        final Thread thread = testee.start();

        TimeUnit.SECONDS.sleep(1);

        testee.stop();

        TimeUnit.SECONDS.sleep(WAIT_FOR_METRIC_SECS + 1);

        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

}