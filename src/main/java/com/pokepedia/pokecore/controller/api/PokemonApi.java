package com.pokepedia.pokecore.controller.api;

import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pokemon", description = "Gestión del catálogo de Pokémon")
@RequestMapping("/v1/pokemon")
public interface PokemonApi {

    @Operation(
            summary = "Listar todos los Pokémon",
            description = "Retorna lista paginada. Acceso público."
    )
    @GetMapping
    ResponseEntity<Page<PokemonResponse>> findAll(
            @ParameterObject
            @PageableDefault(size = 20, sort = "nationalNumber")
            Pageable pageable);

    @Operation(summary = "Obtener Pokémon por ID")
    @ApiResponse(responseCode = "404", description = "Pokémon no encontrado")
    @GetMapping("/{id}")
    ResponseEntity<PokemonResponse> findById(@PathVariable Long id);

    @Operation(summary = "Crear Pokémon", description = "Solo ADMIN")
    @PostMapping
    ResponseEntity<PokemonResponse> create(
            @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Actualizar Pokémon", description = "Solo ADMIN")
    @PutMapping("/{id}")
    ResponseEntity<PokemonResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Eliminar Pokémon", description = "Solo ADMIN")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}