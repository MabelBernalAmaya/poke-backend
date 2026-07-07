package com.pokepedia.pokecore.controller.mapper;

import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import com.pokepedia.pokecore.core.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonDtoMapperTest {

    private final PokemonDtoMapper mapper = Mappers.getMapper(PokemonDtoMapper.class);

    @Test
    void toResponse_mapsFieldsCorrectly() {
        Pokemon pokemon = Pokemon.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .types(List.of("Electric"))
                .region("Kanto")
                .build();

        PokemonResponse result = mapper.toResponse(pokemon);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Pikachu");
        assertThat(result.types()).containsExactly("Electric");
    }

    @Test
    void toDomain_mapsRequestFields() {
        PokemonRequest request = new PokemonRequest(25, "Pikachu", List.of("Electric"), 1L);

        Pokemon result = mapper.toDomain(request);

        assertThat(result.getNationalNumber()).isEqualTo(25);
        assertThat(result.getName()).isEqualTo("Pikachu");
    }
}