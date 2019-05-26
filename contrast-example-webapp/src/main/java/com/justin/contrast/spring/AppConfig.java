package com.justin.contrast.spring;

import com.justin.contrast.service.AccountService;
import com.justin.contrast.service.TransactionService;
import com.justin.contrast.service.impl.AccountServiceImpl;
import com.justin.contrast.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionServiceImpl();
    }
}
