package com.pokepedia.pokecore.controller.api;

import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Teams", description = "Gestión de equipos Pokémon (Team Builder)")
@RequestMapping("/v1/teams")
public interface TeamApi {

    @Operation(summary = "Listar mis equipos")
    @GetMapping
    ResponseEntity<List<EquipoResponse>> listMyTeams(Authentication authentication);

    @Operation(summary = "Crear un nuevo equipo")
    @PostMapping
    ResponseEntity<EquipoResponse> create(@Valid @RequestBody EquipoRequest request, Authentication authentication);

    @Operation(summary = "Actualizar un equipo existente")
    @PutMapping("/{id}")
    ResponseEntity<EquipoResponse> update(@PathVariable Long id, @Valid @RequestBody EquipoRequest request, Authentication authentication);

    @Operation(summary = "Eliminar un equipo")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication);

    @Operation(summary = "Exportar equipo a formato texto")
    @GetMapping("/{id}/export")
    ResponseEntity<String> export(@PathVariable Long id, Authentication authentication);
}