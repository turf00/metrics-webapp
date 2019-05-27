package com.justin.contrast.metric;

public enum HttpMethod {
    DELETE,
    GET,
    HEAD,
    PATCH,
    POST,
    PUT;

    public static HttpMethod fromString(final String input) {
        final String upper = input.toUpperCase().trim();

        return HttpMethod.valueOf(upper);
    }

}
