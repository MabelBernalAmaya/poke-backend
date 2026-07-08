package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.persistence.entity.relational.FavoritoEntity;
import com.pokepedia.pokecore.persistence.repository.relational.FavoritoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceImplTest {

    @Mock
    private FavoritoJpaRepository favoritoRepository;

    @InjectMocks
    private FavoritoServiceImpl favoritoService;

    private final Long usuarioId = 1L;
    private final Long pokemonId = 25L;

    @Test
    void addFavorito_debeGuardarCuandoNoExisteDuplicado() {
        when(favoritoRepository.existsByUsuarioIdAndPokemonId(usuarioId, pokemonId))
                .thenReturn(false);

        favoritoService.addFavorito(usuarioId, pokemonId);

        verify(favoritoRepository).save(argThat(f ->
                f.getUsuarioId().equals(usuarioId) && f.getPokemonId().equals(pokemonId)
        ));
    }

    @Test
    void addFavorito_debeLanzarExcepcionSiYaExiste() {
        when(favoritoRepository.existsByUsuarioIdAndPokemonId(usuarioId, pokemonId))
                .thenReturn(true);

        assertThatThrownBy(() -> favoritoService.addFavorito(usuarioId, pokemonId))
                .isInstanceOf(DuplicateResourceException.class);

        verify(favoritoRepository, never()).save(any());
    }

    @Test
    void removeFavorito_debeLlamarDelete() {
        favoritoService.removeFavorito(usuarioId, pokemonId);

        verify(favoritoRepository).deleteByUsuarioIdAndPokemonId(usuarioId, pokemonId);
    }

    @Test
    void listFavoritos_debeRetornarListaDeIds() {
        FavoritoEntity fav1 = FavoritoEntity.builder().usuarioId(usuarioId).pokemonId(25L).build();
        FavoritoEntity fav2 = FavoritoEntity.builder().usuarioId(usuarioId).pokemonId(6L).build();

        when(favoritoRepository.findByUsuarioId(usuarioId))
                .thenReturn(List.of(fav1, fav2));

        List<Long> resultado = favoritoService.listFavoritos(usuarioId);

        assertThat(resultado).containsExactly(25L, 6L);
    }

    @Test
    void listFavoritos_debeRetornarListaVaciaSiNoHayFavoritos() {
        when(favoritoRepository.findByUsuarioId(usuarioId)).thenReturn(List.of());

        List<Long> resultado = favoritoService.listFavoritos(usuarioId);

        assertThat(resultado).isEmpty();
    }
}