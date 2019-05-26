package com.justin.contrast.util;

import java.util.concurrent.ThreadLocalRandom;

public final class AmountGenerator {

    static final int MIN = -2000;
    static final int MAX = 2000;

    private AmountGenerator() { }

    public static String amount() {
        return amount(MIN, MAX);
    }

    public static String amount(final int start,
                                final int end) {
        return String.format("%.2f", ThreadLocalRandom.current().nextDouble(start, end));
    }
}
