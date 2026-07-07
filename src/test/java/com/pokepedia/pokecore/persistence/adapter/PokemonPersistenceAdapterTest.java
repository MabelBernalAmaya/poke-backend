package com.pokepedia.pokecore.persistence.adapter;

import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import com.pokepedia.pokecore.persistence.mapper.PokemonPersistenceMapper;
import com.pokepedia.pokecore.persistence.repository.relational.PokemonJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PokemonPersistenceAdapterTest {

    @Mock
    private PokemonJpaRepository repository;

    @Mock
    private PokemonPersistenceMapper mapper;

    @InjectMocks
    private PokemonPersistenceAdapter adapter;

    private PokemonEntity entity;
    private Pokemon domain;

    @BeforeEach
    void setUp() {
        entity = PokemonEntity.builder().id(1L).nationalNumber(25).name("Pikachu").build();
        domain = Pokemon.builder().id(1L).nationalNumber(25).name("Pikachu").build();
    }

    @Test
    void findById_whenExists_returnsMappedPokemon() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Pokemon> result = adapter.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pikachu");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pokemon> result = adapter.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_mapsAndDelegatesToRepository() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Pokemon result = adapter.save(domain);

        assertThat(result.getName()).isEqualTo("Pikachu");
        verify(repository).save(entity);
    }

    @Test
    void existsByNationalNumber_delegatesToRepository() {
        when(repository.existsByNationalNumber(25)).thenReturn(true);

        boolean result = adapter.existsByNationalNumber(25);

        assertThat(result).isTrue();
    }

    @Test
    void deleteById_delegatesToRepository() {
        adapter.deleteById(1L);

        verify(repository).deleteById(1L);
    }
}