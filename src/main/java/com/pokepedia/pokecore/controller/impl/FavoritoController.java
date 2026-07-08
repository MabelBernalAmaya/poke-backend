package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.api.FavoritoApi;
import com.pokepedia.pokecore.core.service.FavoritoService;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoritoController implements FavoritoApi {

    private final FavoritoService favoritoService;
    private final UserJpaRepository userRepository;

    private Long getUsuarioId(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        return user.getId();
    }

    @Override
    public ResponseEntity<List<Long>> listFavoritos(Authentication authentication) {
        return ResponseEntity.ok(favoritoService.listFavoritos(getUsuarioId(authentication)));
    }

    @Override
    public ResponseEntity<Void> addFavorito(Long pokemonId, Authentication authentication) {
        favoritoService.addFavorito(getUsuarioId(authentication), pokemonId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeFavorito(Long pokemonId, Authentication authentication) {
        favoritoService.removeFavorito(getUsuarioId(authentication), pokemonId);
        return ResponseEntity.noContent().build();
    }
}