package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.BusinessException;
import com.pokepedia.pokecore.persistence.entity.relational.PasswordResetTokenEntity;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.PasswordResetTokenJpaRepository;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceImplTest {

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PasswordResetTokenJpaRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetServiceImpl service;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .email("test@test.com")
                .passwordHash("oldHash")
                .role("TRAINER")
                .build();
    }

    @Test
    void requestReset_whenUserExists_savesToken() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        service.requestReset("test@test.com");

        verify(tokenRepository).save(any());
    }

    @Test
    void requestReset_whenUserNotExists_doesNothing() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        service.requestReset("noexiste@test.com");

        verify(tokenRepository, never()).save(any());
    }

    @Test
    void resetPassword_withValidToken_updatesPassword() {
        PasswordResetTokenEntity token = PasswordResetTokenEntity.builder()
                .id(1L)
                .usuarioId(1L)
                .token("abc123")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        when(tokenRepository.findByToken("abc123")).thenReturn(Optional.of(token));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass123")).thenReturn("newHash");

        service.resetPassword("abc123", "newPass123");

        verify(userRepository).save(any());
        verify(tokenRepository).save(any());
    }

    @Test
    void resetPassword_withInvalidToken_throws() {
        when(tokenRepository.findByToken("invalido")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.resetPassword("invalido", "newPass123"));
    }

    @Test
    void resetPassword_withExpiredToken_throws() {
        PasswordResetTokenEntity expirado = PasswordResetTokenEntity.builder()
                .id(1L)
                .usuarioId(1L)
                .token("expirado")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .used(false)
                .build();

        when(tokenRepository.findByToken("expirado")).thenReturn(Optional.of(expirado));

        assertThrows(BusinessException.class,
                () -> service.resetPassword("expirado", "newPass123"));
    }
}