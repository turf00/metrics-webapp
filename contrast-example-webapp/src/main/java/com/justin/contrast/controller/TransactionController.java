package com.justin.contrast.controller;

import com.justin.contrast.domain.Transaction;
import com.justin.contrast.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/accounts/{accountId}/transactions")
public final class TransactionController {
    private final TransactionService service;

    public TransactionController(final TransactionService service) {
        this.service = Objects.requireNonNull(service);
    }

    @GetMapping("/{transactionId}")
    public Transaction getTransaction(@PathVariable final String accountId,
                                      @PathVariable final String transactionId) {
        return service.get(accountId, transactionId);
    }

}
