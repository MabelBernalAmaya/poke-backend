package com.pokepedia.pokecore.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favoritos", description = "Gestión de Pokémon favoritos del usuario")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/favorites")
public interface FavoritoApi {

    @Operation(summary = "Listar mis favoritos")
    @GetMapping
    ResponseEntity<List<Long>> listFavoritos(Authentication authentication);

    @Operation(summary = "Agregar a favoritos")
    @PostMapping("/{pokemonId}")
    ResponseEntity<Void> addFavorito(@PathVariable Long pokemonId, Authentication authentication);

    @Operation(summary = "Quitar de favoritos")
    @DeleteMapping("/{pokemonId}")
    ResponseEntity<Void> removeFavorito(@PathVariable Long pokemonId, Authentication authentication);
}