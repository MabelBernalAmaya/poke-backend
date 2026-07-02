package com.pokepedia.pokecore.core.model;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value // Lombok: clase inmutable — todos los campos final, solo getters, no setters
@Builder(toBuilder = true) // toBuilder permite crear copias modificadas
public class Pokemon {
    Long id;
    Integer nationalNumber;
    String name;
    String description;
    String imageUrl;
    List<String> types; // Solo los nombres de los tipos
    String region;
    Integer generation;
    Boolean hasMega;
    PokemonStats stats;
}