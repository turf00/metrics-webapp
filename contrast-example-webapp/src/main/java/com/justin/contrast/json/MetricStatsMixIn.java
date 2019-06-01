package com.justin.contrast.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.justin.contrast.metric.MetricStatLong;

abstract class MetricStatsMixIn {
    @JsonCreator
    MetricStatsMixIn(@JsonProperty("requestTime") final MetricStatLong requestTime,
                     @JsonProperty("responseTime") final MetricStatLong responseSize) {}
}
