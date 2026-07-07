package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.core.exception.ResourceNotFoundException;
import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.port.PokemonPersistencePort;
import com.pokepedia.pokecore.persistence.repository.document.PokemonViewMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;

    @Mock
    private PokemonViewMongoRepository viewRepository;

    @InjectMocks
    private PokemonServiceImpl service;

    private Pokemon pikachu;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .build();
    }

    @Test
    @DisplayName("findById: debe retornar el Pokemon cuando existe")
    void findById_whenExists_returnsPokemon() {
        when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));
        when(viewRepository.save(any())).thenReturn(null);

        Pokemon result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Pikachu");
        verify(pokemonPort).findById(1L);
    }

    @Test
    @DisplayName("findById: debe lanzar ResourceNotFoundException cuando no existe")
    void findById_whenNotFound_throws() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    @DisplayName("create: debe lanzar DuplicateResourceException si el numero ya existe")
    void create_whenDuplicate_throws() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.create(pikachu));
        verify(pokemonPort, never()).save(any());
    }

    @Test
    @DisplayName("create: debe guardar cuando el numero no existe")
    void create_whenNotDuplicate_saves() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(false);
        when(pokemonPort.save(pikachu)).thenReturn(pikachu);

        Pokemon result = service.create(pikachu);

        assertThat(result).isEqualTo(pikachu);
        verify(pokemonPort).save(pikachu);
    }

    @Test
    @DisplayName("delete: debe lanzar ResourceNotFoundException si no existe")
    void delete_whenNotFound_throws() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(pokemonPort, never()).deleteById(any());
    }

    @Test
    @DisplayName("update: debe actualizar manteniendo el id original")
    void update_whenExists_updatesAndKeepsId() {
        Pokemon existente = Pokemon.builder().id(1L).nationalNumber(25).name("Pikachu").build();
        Pokemon cambios = Pokemon.builder().nationalNumber(25).name("Raichu").build();
        Pokemon actualizado = cambios.toBuilder().id(1L).build();

        when(pokemonPort.findById(1L)).thenReturn(Optional.of(existente));
        when(pokemonPort.save(any())).thenReturn(actualizado);

        Pokemon result = service.update(1L, cambios);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Raichu");
    }

    @Test
    @DisplayName("update: debe lanzar ResourceNotFoundException si no existe")
    void update_whenNotFound_throws() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(99L, pikachu));
    }

    @Test
    @DisplayName("delete: debe eliminar cuando existe")
    void delete_whenExists_deletes() {
        when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));


        service.delete(1L);

        verify(pokemonPort).deleteById(1L);
    }
}