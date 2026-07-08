package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.core.service.FavoritoService;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoControllerTest {

    @Mock
    private FavoritoService favoritoService;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FavoritoController favoritoController;

    private final Long usuarioId = 1L;
    private final Long pokemonId = 25L;
    private final String email = "trainer1@test.com";

    private UserEntity mockUser() {
        return UserEntity.builder()
                .id(usuarioId)
                .email(email)
                .build();
    }

    @Test
    void listFavoritos_debeRetornarListaDeIds() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        when(favoritoService.listFavoritos(usuarioId)).thenReturn(List.of(25L, 6L));

        ResponseEntity<List<Long>> response = favoritoController.listFavoritos(authentication);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).containsExactly(25L, 6L);
    }

    @Test
    void addFavorito_debeRetornarOk() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));

        ResponseEntity<Void> response = favoritoController.addFavorito(pokemonId, authentication);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(favoritoService).addFavorito(usuarioId, pokemonId);
    }

    @Test
    void removeFavorito_debeRetornarNoContent() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));

        ResponseEntity<Void> response = favoritoController.removeFavorito(pokemonId, authentication);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(favoritoService).removeFavorito(usuarioId, pokemonId);
    }

    @Test
    void getUsuarioId_debeLanzarExcepcionSiUsuarioNoExiste() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoritoController.listFavoritos(authentication))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}