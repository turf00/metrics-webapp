package com.justin.contrast.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.justin.contrast.domain.Transaction;

import java.util.List;

abstract class AccountMixIn {
    @JsonCreator
    AccountMixIn(@JsonProperty ("id") final String id,
                 @JsonProperty ("type") final String type,
                 @JsonProperty ("userId") final String userId,
                 @JsonProperty ("transactions") final List<Transaction> transactions,
                 @JsonProperty ("balance") final String balance) {}
}