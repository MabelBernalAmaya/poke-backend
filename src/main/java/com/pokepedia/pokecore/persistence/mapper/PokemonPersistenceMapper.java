package com.pokepedia.pokecore.persistence.mapper;

import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import com.pokepedia.pokecore.persistence.entity.relational.TypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = TypeEntity.class)
public interface PokemonPersistenceMapper {

    @Mapping(source = "region.name", target = "region")
    @Mapping(expression = "java(entity.getTypes().stream().map(TypeEntity::getName).toList())", target = "types")
    Pokemon toDomain(PokemonEntity entity);

    @Mapping(target = "region", ignore = true)
    @Mapping(target = "types", ignore = true)
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PokemonEntity toEntity(Pokemon pokemon);
}