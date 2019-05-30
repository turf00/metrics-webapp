package com.justin.contrast.metric.processing;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RingBufferWithLookupTest {

    private static final int CAPACITY = 10;
    private RingBufferWithLookup<Integer, Integer> testee;

    @Before
    public void setUp() {
        testee = new RingBufferWithLookup<>(CAPACITY);
    }

    @Test
    public void shouldAddMoreElementsThanCapacityAndReturnLastXElementsCorrectly() {
        addXInts(CAPACITY * 2);

        // 20 added but only space for the last 10, therefore we should have the following stored at the end: [11, 12, 13...]
        for (int i = 0; i < CAPACITY; i++) {
            assertThat(testee.getByPos(i)).isEqualTo(CAPACITY + i);
        }
    }

    @Test
    public void shouldAddElementsLessThanCapacityAndReturnCorrectly() {
        addXInts(CAPACITY);

        for (int i = 0; i < CAPACITY; i++) {
            assertThat(testee.getByPos(i)).isEqualTo(i);
        }
    }

    @Test
    public void shouldReturnAllElementsAsExpected() {
        addXInts(CAPACITY);

        final List<Integer> result = testee.getAll();

        for (int i = 0; i < CAPACITY; i++) {
            assertThat(result.contains(i));
        }
    }

    @Test
    public void shouldFetchSpecificKeysAsExpected() {
        addXInts(CAPACITY);

        assertThat(testee.getByKey(0)).isEqualTo(0);
    }

    @Test
    public void shouldReturnNullIfKeyNotFound() {
        addXInts(CAPACITY);

        assertThat(testee.getByKey(331)).isNull();
    }

    @Test
    public void shouldClearBufferWhenRequested() {
        addXInts(CAPACITY);

        testee.clear();

        assertThat(testee.getAll().size()).isEqualTo(0);
    }

    private void addXInts(final int count) {
        for (int i = 0; i < count; i++) {
            testee.put(i, i);
        }
    }

}