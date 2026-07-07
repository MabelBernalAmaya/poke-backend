package com.pokepedia.pokecore.persistence.adapter;

import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.port.PokemonPersistencePort;
import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import com.pokepedia.pokecore.persistence.mapper.PokemonPersistenceMapper;
import com.pokepedia.pokecore.persistence.repository.relational.PokemonJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PokemonPersistenceAdapter implements PokemonPersistencePort {

    private final PokemonJpaRepository repository;
    private final PokemonPersistenceMapper mapper;

    @Override
    public Optional<Pokemon> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findByNationalNumber(Integer nationalNumber) {
        return repository.findByNationalNumber(nationalNumber)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNationalNumber(Integer nationalNumber) {
        return repository.existsByNationalNumber(nationalNumber);
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        PokemonEntity entity = mapper.toEntity(pokemon);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Pokemon> filterByType(String type) {
        return repository.findByTypes_NameIgnoreCase(type).stream()
                .map(mapper::toDomain)
                .toList();
    }
}