package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import com.pokepedia.pokecore.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .username("trainer1")
                .email("trainer1@test.com")
                .passwordHash("hashed")
                .role("TRAINER")
                .build();
    }

    @Test
    void register_whenEmailNotExists_createsUserAndReturnsToken() {
        when(userRepository.existsByEmail("trainer1@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("fake-token");

        String token = authService.register("trainer1", "trainer1@test.com", "password123");

        assertThat(token).isEqualTo("fake-token");
        verify(userRepository).save(any());
    }

    @Test
    void register_whenEmailExists_throwsDuplicateResourceException() {
        when(userRepository.existsByEmail("trainer1@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> authService.register("trainer1", "trainer1@test.com", "password123"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        when(userRepository.findByEmail("trainer1@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("fake-token");

        String token = authService.login("trainer1@test.com", "password123");

        assertThat(token).isEqualTo("fake-token");
        verify(authenticationManager).authenticate(any());
    }
}