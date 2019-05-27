package com.justin.contrast.metric;

import com.justin.contrast.metric.http.HttpMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpMethodTest {
    @Test
    public void shouldHandleCreatingFromStringWithDifferenceCaseAndWhitespace() {
        String input = " get ";
        assertCorrect(HttpMethod.GET, input);

        input = " head ";
        assertCorrect(HttpMethod.HEAD, input);

        input = " patch ";
        assertCorrect(HttpMethod.PATCH, input);

        input = " PuT ";
        assertCorrect(HttpMethod.PUT, input);

        input = " \tpost ";
        assertCorrect(HttpMethod.POST, input);

        input = "delete";
        assertCorrect(HttpMethod.DELETE, input);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.fromString("not a http method"));
    }

    private void assertCorrect(final HttpMethod expected, final String input) {
        assertThat(HttpMethod.fromString(input)).isEqualTo(expected);
    }
}