package com.pokepedia.pokecore.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PokemonStats {
    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;

    public Integer getTotal() {
        return hp + attack + defense + specialAttack + specialDefense + speed;
    }
}