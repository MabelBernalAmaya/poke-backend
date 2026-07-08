package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonComparisonResponse;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import com.pokepedia.pokecore.controller.mapper.PokemonDtoMapper;
import com.pokepedia.pokecore.core.exception.BusinessException;
import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    @Mock
    private PokemonService pokemonService;

    @Mock
    private PokemonDtoMapper mapper;

    @InjectMocks
    private PokemonController pokemonController;

    @Test
    void findAll_debeRetornarPaginaDeResponses() {
        Pageable pageable = mock(Pageable.class);
        Pokemon pokemon = mock(Pokemon.class);
        PokemonResponse response = mock(PokemonResponse.class);
        Page<Pokemon> page = new PageImpl<>(List.of(pokemon));

        when(pokemonService.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(pokemon)).thenReturn(response);

        ResponseEntity<Page<PokemonResponse>> result = pokemonController.findAll(pageable);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody().getContent()).containsExactly(response);
    }

    @Test
    void findById_debeRetornarPokemon() {
        Long id = 1L;
        Pokemon pokemon = mock(Pokemon.class);
        PokemonResponse response = mock(PokemonResponse.class);

        when(pokemonService.findById(id)).thenReturn(pokemon);
        when(mapper.toResponse(pokemon)).thenReturn(response);

        ResponseEntity<PokemonResponse> result = pokemonController.findById(id);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void create_debeCrearPokemon() {
        PokemonRequest request = mock(PokemonRequest.class);
        Pokemon domain = mock(Pokemon.class);
        Pokemon created = mock(Pokemon.class);
        PokemonResponse response = mock(PokemonResponse.class);

        when(mapper.toDomain(request)).thenReturn(domain);
        when(pokemonService.create(domain)).thenReturn(created);
        when(mapper.toResponse(created)).thenReturn(response);

        ResponseEntity<PokemonResponse> result = pokemonController.create(request);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void update_debeActualizarPokemon() {
        Long id = 1L;
        PokemonRequest request = mock(PokemonRequest.class);
        Pokemon domain = mock(Pokemon.class);
        Pokemon updated = mock(Pokemon.class);
        PokemonResponse response = mock(PokemonResponse.class);

        when(mapper.toDomain(request)).thenReturn(domain);
        when(pokemonService.update(id, domain)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(response);

        ResponseEntity<PokemonResponse> result = pokemonController.update(id, request);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void filterByType_debeRetornarListaFiltrada() {
        Pokemon pokemon = mock(Pokemon.class);
        PokemonResponse response = mock(PokemonResponse.class);

        when(pokemonService.filterByType("fire")).thenReturn(List.of(pokemon));
        when(mapper.toResponse(pokemon)).thenReturn(response);

        ResponseEntity<List<PokemonResponse>> result = pokemonController.filterByType("fire");

        assertThat(result.getBody()).containsExactly(response);
    }

    @Test
    void delete_debeRetornarNoContent() {
        Long id = 1L;

        ResponseEntity<Void> result = pokemonController.delete(id);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(pokemonService).delete(id);
    }

    @Test
    void compare_debeRetornarComparacionDeDosPokemon() {
        Long id1 = 1L;
        Long id2 = 2L;
        Pokemon p1 = mock(Pokemon.class);
        Pokemon p2 = mock(Pokemon.class);
        PokemonResponse r1 = mock(PokemonResponse.class);
        PokemonResponse r2 = mock(PokemonResponse.class);

        when(pokemonService.findById(id1)).thenReturn(p1);
        when(pokemonService.findById(id2)).thenReturn(p2);
        when(mapper.toResponse(p1)).thenReturn(r1);
        when(mapper.toResponse(p2)).thenReturn(r2);

        ResponseEntity<PokemonComparisonResponse> result = pokemonController.compare(id1, id2);

        assertThat(result.getBody()).isNotNull();
        verify(pokemonService).findById(id1);
        verify(pokemonService).findById(id2);
    }

    @Test
    void compare_debeLanzarExcepcionSiMismosIds() {
        Long id = 1L;

        assertThatThrownBy(() -> pokemonController.compare(id, id))
                .isInstanceOf(BusinessException.class);

        verify(pokemonService, never()).findById(any());
    }
}