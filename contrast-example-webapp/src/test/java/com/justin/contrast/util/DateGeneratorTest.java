package com.justin.contrast.util;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateGeneratorTest {
    @Test
    public void shouldReturnDateWithinXDays() {
        final int days = 2;

        final ZonedDateTime now = ZonedDateTime.now();

        final ZonedDateTime found = DateGenerator.randomLastXDays(days);

        // provide some extra flexibility in case of slowdown
        final ZonedDateTime start = now.minusDays(days).minusSeconds(30);
        final ZonedDateTime end = now.plusSeconds(30);

        assertThat(found).isBetween(start, end);
    }
}