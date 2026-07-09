package com.pokepedia.pokecore.core.service;

public interface PasswordResetService {
    void requestReset(String email);
    void resetPassword(String token, String newPassword);
}