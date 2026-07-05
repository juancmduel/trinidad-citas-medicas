package com.trinidad.citas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinidad.citas.dto.LoginRequest;
import com.trinidad.citas.dto.LoginResponse;
import com.trinidad.citas.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "api", "web"})
@DisplayName("AuthRestController — POST /api/auth/login")
class AuthRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AuthService authService;

    @Test
    @DisplayName("Login exitoso → 200 con token JWT")
    void loginExitoso() throws Exception {
        LoginResponse respuesta = LoginResponse.builder()
            .token("jwt-token-abc")
            .tipo("Bearer")
            .username("admin")
            .email("admin@trinidad.com")
            .roles(Set.of("ADMINISTRADOR"))
            .expiracionMs(3600000L)
            .build();

        given(authService.login(any())).willReturn(respuesta);

        LoginRequest req = new LoginRequest("admin", "admin123");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token-abc"))
            .andExpect(jsonPath("$.tipo").value("Bearer"))
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.roles[0]").value("ADMINISTRADOR"));
    }

    @Test
    @DisplayName("Credenciales inválidas → 401")
    void loginFallido() throws Exception {
        given(authService.login(any()))
            .willThrow(new BadCredentialsException("Credenciales inválidas"));

        LoginRequest req = new LoginRequest("admin", "wrong");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Body inválido (username vacío) → 400")
    void loginBodyInvalido() throws Exception {
        given(authService.login(any()))
            .willReturn(LoginResponse.builder().build()); // no debería llegar aquí

        LoginRequest req = new LoginRequest("", "admin123");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }
}
