package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.core.exception.ResourceNotFoundException;
import com.pokepedia.pokecore.core.model.Pokemon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pokepedia.pokecore.core.port.PokemonPersistencePort;
@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;

    @Override
    public Pokemon findById(Long id) {
        log.debug("Buscando Pokemon con id: {}", id);
        return pokemonPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    @Transactional
    public Pokemon create(Pokemon pokemon) {
        if (pokemonPort.existsByNationalNumber(pokemon.getNationalNumber())) {
            throw new DuplicateResourceException("Pokemon", "nationalNumber", pokemon.getNationalNumber());
        }
        return pokemonPort.save(pokemon);
    }

    // Los otros métodos los implementaremos conforme la guía avance...
    @Override public org.springframework.data.domain.Page<Pokemon> findAll(org.springframework.data.domain.Pageable pageable) { return null; }
    @Override public Pokemon findByNationalNumber(Integer number) { return null; }
    @Override public Pokemon update(Long id, Pokemon pokemon) { return null; }
    @Override public void delete(Long id) {}
}