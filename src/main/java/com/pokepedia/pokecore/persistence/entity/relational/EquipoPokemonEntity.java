package com.pokepedia.pokecore.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "equipo_pokemon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@IdClass(EquipoPokemonEntity.EquipoPokemonId.class)
public class EquipoPokemonEntity {

    @Id
    @Column(name = "equipo_id")
    private Long equipoId;

    @Id
    @Column(name = "posicion")
    private Integer posicion;

    @Column(name = "pokemon_id", nullable = false)
    private Long pokemonId;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipoPokemonId implements Serializable {
        private Long equipoId;
        private Integer posicion;
    }
}