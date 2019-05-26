package com.justin.contrast.controller;

import com.justin.contrast.domain.Account;
import com.justin.contrast.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping ("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = Objects.requireNonNull(accountService);
    }

    @GetMapping(value = "/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        return accountService.getAccount(accountId);
    }
}
