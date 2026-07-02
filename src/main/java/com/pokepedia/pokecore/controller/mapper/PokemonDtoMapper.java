package com.pokepedia.pokecore.controller.mapper;

import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import com.pokepedia.pokecore.core.model.Pokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PokemonDtoMapper {

    PokemonResponse toResponse(Pokemon pokemon);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "generation", ignore = true)
    @Mapping(target = "hasMega", ignore = true)
    @Mapping(target = "stats", ignore = true)
    Pokemon toDomain(PokemonRequest request);

    List<PokemonResponse> toResponseList(List<Pokemon> pokemons);
}
