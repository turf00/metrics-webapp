package com.justin.contrast.controller;

import com.justin.contrast.domain.Transaction;
import com.justin.contrast.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith (SpringRunner.class)
@WebMvcTest (TransactionController.class)
public class TransactionControllerTest {

    private static final String ACCOUNT_ID = "12345";
    private static final String TX_ID = "98765";
    private static final ZonedDateTime DATE_TIME = ZonedDateTime.of(2019, 5, 4, 10, 15, 45, 0, ZoneId.of("Europe/London"));
    private static final String EXPECTED = "{\"id\":\"98765\",\"customerId\":\"customerId\",\"effectiveDate\":\"2019-05-04T10:15:45+01:00\"," +
            "\"amount\":\"919.34\",\"description\":\"description\"}";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService mockService;

    @Test
    public void shouldReturnJsonTransactionForGettingById() throws Exception {
        when(mockService.get(ACCOUNT_ID, TX_ID))
                .thenReturn(transaction());

        final String url = String.format("/accounts/%s/transactions/%s", ACCOUNT_ID, TX_ID);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(EXPECTED)));
    }

    private static Transaction transaction() {
        return new Transaction(TX_ID,
                "customerId",
                DATE_TIME,
                "919.34",
                "description");
    }
}