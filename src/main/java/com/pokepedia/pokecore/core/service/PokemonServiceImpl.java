package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.core.exception.ResourceNotFoundException;
import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.port.PokemonPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        log.debug("Listando Pokémon");
        return pokemonPort.findAll(pageable);
    }

    @Override
    public Pokemon findById(Long id) {
        log.debug("Buscando Pokémon con id {}", id);

        return pokemonPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    public Pokemon findByNationalNumber(Integer number) {
        return pokemonPort.findByNationalNumber(number)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pokemon", "nationalNumber", number));
    }

    @Override
    @Transactional
    public Pokemon create(Pokemon pokemon) {

        if (pokemonPort.existsByNationalNumber(pokemon.getNationalNumber())) {
            throw new DuplicateResourceException(
                    "Pokemon",
                    "nationalNumber",
                    pokemon.getNationalNumber()
            );
        }

        return pokemonPort.save(pokemon);
    }

    @Override
    @Transactional
    public Pokemon update(Long id, Pokemon pokemon) {

        Pokemon existente = pokemonPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pokemon", "id", id));

        Pokemon actualizado = pokemon.toBuilder()
                .id(existente.getId())
                .build();

        return pokemonPort.save(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        pokemonPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pokemon", "id", id));

        pokemonPort.deleteById(id);
    }
}