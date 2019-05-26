package com.justin.contrast.util;

import java.util.concurrent.ThreadLocalRandom;

public class SimulateUtil {
    private static final long MAX_DELAY_MS = 450;
    private static final long MIN_DELAY_MS = 25;

    private SimulateUtil() { }

    public static void simulateNetworkDelay() {
        simulateNetworkDelay(MIN_DELAY_MS, MAX_DELAY_MS);
    }

    public static void simulateNetworkDelay(final long min,
                                            final long max) {
        final long delayMs = ThreadLocalRandom.current().nextLong(min, max);

        try {
            Thread.sleep(delayMs);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
