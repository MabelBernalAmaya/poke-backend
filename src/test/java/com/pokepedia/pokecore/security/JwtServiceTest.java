package com.pokepedia.pokecore.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "7iNDhHqR5qhylJgJC+4EpiyJacA4+Ad7ywfsNhd7SA8=");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    @Test
    void generateToken_andExtractUsername_returnsCorrectUsername() {
        User user = new User("test@test.com", "pass", Collections.emptyList());

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("test@test.com");
    }

    @Test
    void isTokenValid_withMatchingUser_returnsTrue() {
        User user = new User("test@test.com", "pass", Collections.emptyList());
        String token = jwtService.generateToken(user);

        boolean valid = jwtService.isTokenValid(token, user);

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_withDifferentUser_returnsFalse() {
        User user = new User("test@test.com", "pass", Collections.emptyList());
        User otherUser = new User("other@test.com", "pass", Collections.emptyList());
        String token = jwtService.generateToken(user);

        boolean valid = jwtService.isTokenValid(token, otherUser);

        assertThat(valid).isFalse();
    }
}