package com.pokepedia.pokecore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_withoutAuthHeader_letsRequestPassWithoutAuthenticating() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void doFilter_withValidToken_authenticatesUser() throws Exception {
        UserDetails userDetails = new User("trainer1@test.com", "hash", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.extractUsername("valid-token")).thenReturn("trainer1@test.com");
        when(userDetailsService.loadUserByUsername("trainer1@test.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-token", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() != null;
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_withInvalidToken_doesNotAuthenticate() throws Exception {
        UserDetails userDetails = new User("trainer1@test.com", "hash", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtService.extractUsername("invalid-token")).thenReturn("trainer1@test.com");
        when(userDetailsService.loadUserByUsername("trainer1@test.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid-token", userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() == null;
        verify(filterChain).doFilter(request, response);
    }
}