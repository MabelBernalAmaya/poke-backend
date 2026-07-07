package com.pokepedia.pokecore.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokepedia.pokecore.controller.dto.request.LoginRequest;
import com.pokepedia.pokecore.controller.dto.request.RegisterRequest;
import com.pokepedia.pokecore.core.service.AuthService;
import com.pokepedia.pokecore.security.JwtAuthFilter;
import com.pokepedia.pokecore.security.JwtService;
import com.pokepedia.pokecore.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void register_withValidBody_returns200() throws Exception {
        RegisterRequest request = new RegisterRequest("trainer1", "trainer1@test.com", "password123");
        when(authService.register("trainer1", "trainer1@test.com", "password123"))
                .thenReturn("fake-token");

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }

    @Test
    void register_withInvalidBody_returns400() throws Exception {
        RegisterRequest invalid = new RegisterRequest("", "not-an-email", "123");

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidBody_returns200() throws Exception {
        LoginRequest request = new LoginRequest("trainer1@test.com", "password123");
        when(authService.login("trainer1@test.com", "password123"))
                .thenReturn("fake-token");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }
}