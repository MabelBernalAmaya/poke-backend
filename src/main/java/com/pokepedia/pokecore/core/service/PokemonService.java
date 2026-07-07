package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PokemonService {
    Page<Pokemon> findAll(Pageable pageable);
    Pokemon findById(Long id);
    Pokemon findByNationalNumber(Integer number);
    Pokemon create(Pokemon pokemon);
    Pokemon update(Long id, Pokemon pokemon);
    void delete(Long id);
    List<Pokemon> filterByType(String type);
}