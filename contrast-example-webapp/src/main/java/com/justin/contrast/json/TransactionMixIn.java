package com.justin.contrast.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

abstract class TransactionMixIn {
    @JsonCreator
    public TransactionMixIn(@JsonProperty ("id") final String id,
                            @JsonProperty ("customerId") final String customerId,
                            @JsonProperty ("effectiveDate") final ZonedDateTime effectiveDate,
                            @JsonProperty ("amount") final String amount,
                            @JsonProperty ("description") final String description) {
    }
}
