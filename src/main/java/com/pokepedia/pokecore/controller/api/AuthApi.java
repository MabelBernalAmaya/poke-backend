package com.pokepedia.pokecore.controller.api;

import com.pokepedia.pokecore.controller.dto.request.LoginRequest;
import com.pokepedia.pokecore.controller.dto.request.RegisterRequest;
import com.pokepedia.pokecore.controller.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pokepedia.pokecore.controller.dto.request.ForgotPasswordRequest;
import com.pokepedia.pokecore.controller.dto.request.ResetPasswordRequest;
@Tag(name = "Auth", description = "Registro y autenticación de usuarios")
@RequestMapping("/v1/auth")
public interface AuthApi {

    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/register")
    ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Iniciar sesión con correo y contraseña")
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request);
    @Operation(summary = "Solicitar recuperacion de contrasena")
    @PostMapping("/forgot-password")
    ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request);

    @Operation(summary = "Restablecer contrasena con token")
    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request);
}