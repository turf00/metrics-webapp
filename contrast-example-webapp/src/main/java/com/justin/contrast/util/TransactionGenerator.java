package com.justin.contrast.util;

import com.justin.contrast.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TransactionGenerator {
    private static final int LAST_7_DAYS = 7;

    private TransactionGenerator() { }

    public static Transaction generate() {
        return generate(UUID.randomUUID().toString());
    }

    public static Transaction generate(final String id) {
        final String amount = AmountGenerator.amount();

        return new Transaction(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                DateGenerator.randomLastXDays(LAST_7_DAYS),
                amount,
                String.format("Description of the transaction id: %s", id));
    }

    public static List<Transaction> generate(final int maxCount) {
        final List<Transaction> result = new ArrayList<>();

        final int txCount = ThreadLocalRandom.current().nextInt(1, maxCount + 1);

        for (int i = 0; i < txCount; i++) {
            result.add(generate());
        }

        return result;
    }
}
