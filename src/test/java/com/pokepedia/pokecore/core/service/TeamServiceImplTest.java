package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;
import com.pokepedia.pokecore.core.exception.BusinessException;
import com.pokepedia.pokecore.core.exception.ResourceNotFoundException;
import com.pokepedia.pokecore.persistence.entity.relational.EquipoEntity;
import com.pokepedia.pokecore.persistence.entity.relational.EquipoPokemonEntity;
import com.pokepedia.pokecore.persistence.repository.relational.EquipoJpaRepository;
import com.pokepedia.pokecore.persistence.repository.relational.EquipoPokemonJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private EquipoJpaRepository equipoRepository;

    @Mock
    private EquipoPokemonJpaRepository equipoPokemonRepository;

    @Mock
    private com.pokepedia.pokecore.core.port.PokemonPersistencePort pokemonPort;

    @InjectMocks
    private TeamServiceImpl teamService;

    private EquipoEntity equipo;

    @BeforeEach
    void setUp() {
        equipo = EquipoEntity.builder()
                .id(1L)
                .usuarioId(10L)
                .nombre("Equipo Test")
                .build();
    }

    @Test
    void create_whenValid_savesTeamAndMembers() {
        EquipoRequest request = new EquipoRequest("Equipo Test", List.of(1L, 2L));

        when(equipoRepository.countByUsuarioId(10L)).thenReturn(0L);
        when(equipoRepository.save(any())).thenReturn(equipo);

        EquipoResponse result = teamService.create(10L, request);

        assertThat(result.nombre()).isEqualTo("Equipo Test");
        verify(equipoPokemonRepository, times(2)).save(any());
    }

    @Test
    void create_whenTeamLimitReached_throwsBusinessException() {
        EquipoRequest request = new EquipoRequest("Equipo Test", List.of(1L));

        when(equipoRepository.countByUsuarioId(10L)).thenReturn(10L);

        assertThrows(BusinessException.class, () -> teamService.create(10L, request));
        verify(equipoRepository, never()).save(any());
    }

    @Test
    void create_whenDuplicatePokemon_throwsBusinessException() {
        EquipoRequest request = new EquipoRequest("Equipo Test", List.of(1L, 1L));

        when(equipoRepository.countByUsuarioId(10L)).thenReturn(0L);

        assertThrows(BusinessException.class, () -> teamService.create(10L, request));
    }

    @Test
    void listByUsuario_returnsTeamsWithMembers() {
        when(equipoRepository.findByUsuarioId(10L)).thenReturn(List.of(equipo));
        when(equipoPokemonRepository.findByEquipoId(1L)).thenReturn(List.of(
                EquipoPokemonEntity.builder().equipoId(1L).posicion(1).pokemonId(5L).build()
        ));

        List<EquipoResponse> result = teamService.listByUsuario(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).pokemonIds()).containsExactly(5L);
    }

    @Test
    void update_whenNotOwner_throwsBusinessException() {
        EquipoRequest request = new EquipoRequest("Nuevo", List.of(1L));

        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(BusinessException.class, () -> teamService.update(1L, 999L, request));
    }

    @Test
    void update_whenNotFound_throwsResourceNotFoundException() {
        EquipoRequest request = new EquipoRequest("Nuevo", List.of(1L));

        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.update(99L, 10L, request));
    }

    @Test
    void delete_whenOwner_deletesTeam() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        teamService.delete(1L, 10L);

        verify(equipoRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotOwner_throwsBusinessException() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(BusinessException.class, () -> teamService.delete(1L, 999L));
    }
}