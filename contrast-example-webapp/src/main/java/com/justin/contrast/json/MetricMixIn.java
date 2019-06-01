package com.justin.contrast.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.justin.contrast.metric.http.HttpMethod;

@JsonIgnoreProperties (value={ "dateTime" }, allowGetters=true)
abstract class MetricMixIn {
    @JsonCreator
    MetricMixIn(@JsonProperty ("id") final String id,
                @JsonProperty("uri") final String uri,
                @JsonProperty("method") final HttpMethod method,
                @JsonProperty("bytesWritten") final long bytesWritten,
                @JsonProperty("timeTakenMs") final long timeTakenMs,
                @JsonProperty("timestamp") final long timestamp) {}
}
