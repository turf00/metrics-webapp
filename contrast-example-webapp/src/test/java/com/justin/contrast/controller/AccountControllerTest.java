package com.justin.contrast.controller;

import com.justin.contrast.domain.Account;
import com.justin.contrast.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith (SpringRunner.class)
@WebMvcTest (AccountController.class)
public class AccountControllerTest {

    private static final String ACCOUNT_ID = "1234567";
    private static final String EXPECTED = "{\"id\":\"1234567\",\"type\":\"Checking\",\"userId\":\"userId\",\"transactions\":[],\"balance\":\"312.52\"}";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService mockService;

    @Test
    public void shouldReturn200WithValidAccount() throws Exception {
        when(mockService.getAccount(ACCOUNT_ID))
                .thenReturn(account());

        mockMvc.perform(get("/accounts/" + ACCOUNT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(EXPECTED)));
    }

    private static Account account() {
        return new Account(ACCOUNT_ID,
                "Checking",
                "userId",
                new ArrayList<>(),
                "312.52");
    }
}