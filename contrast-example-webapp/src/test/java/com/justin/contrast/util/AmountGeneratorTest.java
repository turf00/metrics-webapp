package com.justin.contrast.util;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountGeneratorTest {
    final Pattern EXPECTED_FORMAT = Pattern.compile("\\d+\\.\\d{2}");

    @Test
    public void shouldReturnNumberInRangeWithTwoDecimalPlacesWhenNonDefaultRange() {
        final int min = Integer.MIN_VALUE;
        final int max = Integer.MAX_VALUE;

        final String found = AmountGenerator.amount(min, max);

        verify(found, min, max);
    }

    @Test
    public void shouldReturnNumberInRangeWithTwoDecimalPlacesWhenDefaultRange() {
        final int min = AmountGenerator.MIN;
        final int max = AmountGenerator.MAX;

        final String found = AmountGenerator.amount();

        verify(found, min, max);
    }

    private void verify(final String found,
                        final int min,
                        final int max) {
        final double foundDouble = Double.parseDouble(found);

        assertThat(foundDouble).isBetween((double) min, (double) max);
        assertThat(found).containsPattern(EXPECTED_FORMAT);
    }
}