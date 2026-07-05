package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.api.AuthApi;
import com.pokepedia.pokecore.controller.dto.request.LoginRequest;
import com.pokepedia.pokecore.controller.dto.request.RegisterRequest;
import com.pokepedia.pokecore.controller.dto.response.TokenResponse;
import com.pokepedia.pokecore.core.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> register(RegisterRequest request) {
        String token = authService.register(request.username(), request.email(), request.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}