package com.justin.contrast.util;

import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class DateGenerator {
    private DateGenerator() { }

    public static ZonedDateTime randomLastXDays(final int days) {
        final long seconds = TimeUnit.DAYS.toSeconds(days);
        final long secondsToSubtract = ThreadLocalRandom.current().nextLong(0, seconds);

        return ZonedDateTime.now().minusSeconds(secondsToSubtract);
    }
}
