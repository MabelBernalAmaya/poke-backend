package com.pokepedia.pokecore.persistence.mapper;

import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import com.pokepedia.pokecore.persistence.entity.relational.RegionEntity;
import com.pokepedia.pokecore.persistence.entity.relational.TypeEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonPersistenceMapperTest {

    private final PokemonPersistenceMapper mapper = Mappers.getMapper(PokemonPersistenceMapper.class);

    @Test
    void toDomain_mapsFieldsCorrectly() {
        RegionEntity region = RegionEntity.builder().id(1L).name("Kanto").build();
        TypeEntity type = TypeEntity.builder().id(1L).name("Electric").build();

        PokemonEntity entity = PokemonEntity.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .region(region)
                .types(List.of(type))
                .build();

        Pokemon result = mapper.toDomain(entity);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Pikachu");
        assertThat(result.getRegion()).isEqualTo("Kanto");
        assertThat(result.getTypes()).containsExactly("Electric");
    }

    @Test
    void toEntity_mapsBasicFields() {
        Pokemon pokemon = Pokemon.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .build();

        PokemonEntity result = mapper.toEntity(pokemon);

        assertThat(result.getNationalNumber()).isEqualTo(25);
        assertThat(result.getName()).isEqualTo("Pikachu");
    }
}