package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import com.pokepedia.pokecore.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(String username, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Usuario", "email", email);
        }

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(rawPassword))
                        .role("TRAINER")
                        .build()
        );

        User userDetails = new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        return jwtService.generateToken(userDetails);
    }

    @Override
    public String login(String email, String rawPassword) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado tras autenticación"));

        User userDetails = new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        return jwtService.generateToken(userDetails);
    }
}