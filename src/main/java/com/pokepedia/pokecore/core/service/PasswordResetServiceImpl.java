package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.BusinessException;
import com.pokepedia.pokecore.persistence.entity.relational.PasswordResetTokenEntity;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.PasswordResetTokenJpaRepository;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserJpaRepository userRepository;
    private final PasswordResetTokenJpaRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void requestReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();

            tokenRepository.save(PasswordResetTokenEntity.builder()
                    .usuarioId(user.getId())
                    .token(token)
                    .expiresAt(LocalDateTime.now().plusMinutes(15))
                    .used(false)
                    .build());

            log.info("=== SIMULACION DE ENVIO DE CORREO ===");
            log.info("Para: {}", user.getEmail());
            log.info("Asunto: Recuperacion de contrasena - Pokedex DOSW");
            log.info("Enlace de recuperacion: http://localhost:8080/reset-password?token={}", token);
            log.info("Valido por 15 minutos");
            log.info("======================================");
        });
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Token invalido", "INVALID_RESET_TOKEN"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expirado o ya utilizado", "EXPIRED_RESET_TOKEN");
        }

        UserEntity user = userRepository.findById(resetToken.getUsuarioId())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado", "USER_NOT_FOUND"));

        UserEntity actualizado = user.toBuilder()
                .passwordHash(passwordEncoder.encode(newPassword))
                .build();
        userRepository.save(actualizado);

        PasswordResetTokenEntity usado = resetToken.toBuilder().used(true).build();
        tokenRepository.save(usado);

        log.info("Contrasena restablecida exitosamente para usuario id: {}", user.getId());
    }
}