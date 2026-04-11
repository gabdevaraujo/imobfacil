package br.com.gva.imobfacil.controller;

import br.com.gva.imobfacil.dto.request.LoginRequest;
import br.com.gva.imobfacil.dto.response.AuthResponse;
import br.com.gva.imobfacil.exception.GlobalExceptionHandler;
import br.com.gva.imobfacil.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock AuthService authService;
    @InjectMocks AuthController authController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void shouldReturn200WithTokenWhenLoginSucceeds() throws Exception {
        when(authService.login(any())).thenReturn(new AuthResponse("jwt-token", "admin"));

        String body = objectMapper.writeValueAsString(
            buildLoginRequest("admin", "admin123"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void shouldReturn400WhenLoginBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        when(authService.login(any()))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        String body = objectMapper.writeValueAsString(
            buildLoginRequest("admin", "senha-errada"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private LoginRequest buildLoginRequest(String username, String senha) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setSenha(senha);
        return request;
    }
}
