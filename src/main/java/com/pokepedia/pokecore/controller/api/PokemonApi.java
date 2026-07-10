package com.pokepedia.pokecore.controller.api;

import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonComparisonResponse;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    ResponseEntity<PokemonResponse> create(
            @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Actualizar Pokémon", description = "Solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    ResponseEntity<PokemonResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Eliminar Pokémon", description = "Solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Filtrar Pokémon por tipo")
    @GetMapping("/filter")
    ResponseEntity<List<PokemonResponse>> filterByType(@RequestParam String type);

    @Operation(summary = "Comparar dos Pokémon por sus stats")
    @GetMapping("/compare")
    ResponseEntity<PokemonComparisonResponse> compare(@RequestParam Long id1, @RequestParam Long id2);

    @Operation(summary = "Filtro avanzado: tipo, region, rango de stats y ordenamiento")
    @GetMapping("/filter/advanced")
    ResponseEntity<List<PokemonResponse>> filterAdvanced(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer minStat,
            @RequestParam(required = false) Integer maxStat,
            @RequestParam(required = false) String sortBy
    );
}