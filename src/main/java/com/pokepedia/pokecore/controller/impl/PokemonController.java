package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.api.PokemonApi;
import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import com.pokepedia.pokecore.controller.mapper.PokemonDtoMapper;
import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<Page<PokemonResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(pokemonService.findAll(pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id) {
        return ResponseEntity.ok(mapper.toResponse(pokemonService.findById(id)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        Pokemon created = pokemonService.create(mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(created));
    }
}