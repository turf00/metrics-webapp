package com.justin.contrast.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract class MetricStatLongMixIn {
    @JsonCreator
    MetricStatLongMixIn(@JsonProperty("name") final String name,
                        @JsonProperty("count") final long count,
                        @JsonProperty("total") final long total,
                        @JsonProperty("min") final long min,
                        @JsonProperty("max") final long max) {}
}
