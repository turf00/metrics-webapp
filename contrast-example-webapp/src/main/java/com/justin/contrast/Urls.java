package com.justin.contrast;

public final class Urls {
    private Urls() { }

    public static final String ACCOUNT = "/accounts";

    public static String urlGetAccount(final String accountId) {
        return String.format("%s/%s", ACCOUNT, accountId);
    }
}
