package com.pokepedia.pokecore.controller.dto.response;

import java.util.List;

public record PokemonResponse(
        Long id,
        Integer nationalNumber,
        String name,
        List<String> types,
        String region,
        PokemonStatsResponse stats
) {}