package com.financial.investment.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.application.service.InvestmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestmentController.class)
public class InvestmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestmentService investmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testCreateInvestment() throws Exception {
        InvestmentRequest request = new InvestmentRequest(BigDecimal.valueOf(1000), "FIXED");
        InvestmentResponse response = new InvestmentResponse(1L, BigDecimal.valueOf(1000), "FIXED", "ACTIVE");

        when(investmentService.createInvestment(eq(1L), any(InvestmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/investments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.type").value("FIXED"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testGetInvestments() throws Exception {
        InvestmentResponse response = new InvestmentResponse(1L, BigDecimal.valueOf(1000), "FIXED", "ACTIVE");
        Page<InvestmentResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        when(investmentService.getInvestments(eq(1L), any())).thenReturn(page);

        mockMvc.perform(get("/api/investments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testGetInvestment() throws Exception {
        InvestmentResponse response = new InvestmentResponse(1L, BigDecimal.valueOf(1000), "FIXED", "ACTIVE");

        when(investmentService.getInvestment(eq(1L), eq(1L))).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/investments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void testWithdrawInvestment() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(1000);

        when(investmentService.withdrawInvestment(eq(1L), eq(1L))).thenReturn(amount);

        mockMvc.perform(post("/api/investments/1/withdraw")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));
    }
}
