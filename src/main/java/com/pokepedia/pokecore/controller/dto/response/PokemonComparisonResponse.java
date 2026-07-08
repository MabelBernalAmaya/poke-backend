package com.pokepedia.pokecore.controller.dto.response;

public record PokemonComparisonResponse(
        PokemonResponse pokemon1,
        PokemonResponse pokemon2
) {}