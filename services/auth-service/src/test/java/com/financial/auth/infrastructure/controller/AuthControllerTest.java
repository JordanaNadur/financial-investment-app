package com.financial.auth.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.auth.application.dto.AuthRequest;
import com.financial.auth.application.dto.AuthResponse;
import com.financial.auth.application.dto.RegisterRequest;
import com.financial.auth.application.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
        AuthRequest request = new AuthRequest("user", "password");
        AuthResponse response = new AuthResponse("token123");

        when(authService.authenticate(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest("user", "password", "email@example.com");
        AuthResponse response = new AuthResponse("token123");

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    public void testWelcome() throws Exception {
        mockMvc.perform(get("/auth/welcome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to the Financial Investment App!"));
    }
}
