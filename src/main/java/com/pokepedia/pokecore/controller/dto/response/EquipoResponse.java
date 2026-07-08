package com.pokepedia.pokecore.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EquipoResponse(
        Long id,
        String nombre,
        List<Long> pokemonIds,
        LocalDateTime createdAt
) {}