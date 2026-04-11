package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.dto.request.LoginRequest;
import br.com.gva.imobfacil.dto.response.AuthResponse;
import br.com.gva.imobfacil.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock UserDetailsService userDetailsService;
    @Mock JwtService jwtService;
    @InjectMocks AuthService authService;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setSenha("admin123");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtService.gerarToken("admin")).thenReturn("jwt-token-gerado");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token-gerado");
        assertThat(response.getUsername()).isEqualTo("admin");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldThrowWhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setSenha("senha-errada");

        doThrow(new BadCredentialsException("Bad credentials"))
            .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
            .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).gerarToken(any());
    }
}
