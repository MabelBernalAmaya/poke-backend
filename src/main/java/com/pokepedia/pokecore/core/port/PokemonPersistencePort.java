package com.pokepedia.pokecore.core.port;

import com.pokepedia.pokecore.core.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PokemonPersistencePort {

    Optional<Pokemon> findById(Long id);

    Page<Pokemon> findAll(Pageable pageable);

    Optional<Pokemon> findByNationalNumber(Integer nationalNumber);

    boolean existsByNationalNumber(Integer nationalNumber);

    Pokemon save(Pokemon pokemon);

    void deleteById(Long id);
}