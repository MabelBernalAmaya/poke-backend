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
import java.util.List;
import com.pokepedia.pokecore.controller.dto.response.PokemonComparisonResponse;
@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<Page<PokemonResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(
                pokemonService.findAll(pageable)
                        .map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id) {
        return ResponseEntity.ok(
                mapper.toResponse(pokemonService.findById(id))
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        Pokemon created = pokemonService.create(mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(created));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        Pokemon updated = pokemonService.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }
    @Override
    public ResponseEntity<List<PokemonResponse>> filterByType(String type) {
        List<Pokemon> pokemons = pokemonService.filterByType(type);
        return ResponseEntity.ok(pokemons.stream().map(mapper::toResponse).toList());
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();


    }
    @Override
    public ResponseEntity<PokemonComparisonResponse> compare(Long id1, Long id2) {
        if (id1.equals(id2)) {
            throw new com.pokepedia.pokecore.core.exception.BusinessException(
                    "Selecciona una especie distinta para comparar", "SAME_POKEMON_COMPARISON");
        }
        Pokemon p1 = pokemonService.findById(id1);
        Pokemon p2 = pokemonService.findById(id2);
        return ResponseEntity.ok(new PokemonComparisonResponse(mapper.toResponse(p1), mapper.toResponse(p2)));
    }
    @Override
    public ResponseEntity<List<PokemonResponse>> filterAdvanced(String type, Integer minStat, Integer maxStat, String sortBy) {
        List<Pokemon> resultado = pokemonService.filterAdvanced(type, minStat, maxStat, sortBy);
        return ResponseEntity.ok(resultado.stream().map(mapper::toResponse).toList());
    }
}