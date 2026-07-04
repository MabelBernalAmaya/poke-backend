package com.pokepedia.pokecore.core.service;

public interface AuthService {
    String register(String username, String email, String rawPassword);
    String login(String email, String rawPassword);
}