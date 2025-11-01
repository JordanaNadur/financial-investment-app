package com.financial.transaction.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.transaction.application.dto.TransactionRequest;
import com.financial.transaction.application.dto.TransactionResponse;
import com.financial.transaction.application.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testInvest() throws Exception {
        TransactionRequest request = new TransactionRequest(1L, BigDecimal.valueOf(1000), "INVEST");
        TransactionResponse response = new TransactionResponse(1L, 1L, BigDecimal.valueOf(1000), "COMPLETED");

        when(transactionService.processTransaction(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transactions/invest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.type").value("COMPLETED"));
    }
}
