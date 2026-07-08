package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;
import com.pokepedia.pokecore.core.service.TeamService;
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
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TeamController teamController;

    private final Long usuarioId = 1L;
    private final String email = "trainer1@test.com";

    private UserEntity mockUser() {
        return UserEntity.builder()
                .id(usuarioId)
                .email(email)
                .build();
    }

    @Test
    void listMyTeams_debeRetornarListaDeEquipos() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        List<EquipoResponse> equipos = List.of(mock(EquipoResponse.class));
        when(teamService.listByUsuario(usuarioId)).thenReturn(equipos);

        ResponseEntity<List<EquipoResponse>> response = teamController.listMyTeams(authentication);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(equipos);
    }

    @Test
    void create_debeCrearEquipo() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        EquipoRequest request = mock(EquipoRequest.class);
        EquipoResponse response = mock(EquipoResponse.class);
        when(teamService.create(usuarioId, request)).thenReturn(response);

        ResponseEntity<EquipoResponse> result = teamController.create(request, authentication);

        assertThat(result.getBody()).isEqualTo(response);
        verify(teamService).create(usuarioId, request);
    }

    @Test
    void update_debeActualizarEquipo() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        Long equipoId = 10L;
        EquipoRequest request = mock(EquipoRequest.class);
        EquipoResponse response = mock(EquipoResponse.class);
        when(teamService.update(equipoId, usuarioId, request)).thenReturn(response);

        ResponseEntity<EquipoResponse> result = teamController.update(equipoId, request, authentication);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void delete_debeRetornarNoContent() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        Long equipoId = 10L;

        ResponseEntity<Void> result = teamController.delete(equipoId, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(teamService).delete(equipoId, usuarioId);
    }

    @Test
    void export_debeRetornarTextoDelEquipo() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser()));
        Long equipoId = 10L;
        String textoEsperado = "Equipo exportado...";
        when(teamService.exportToText(equipoId, usuarioId)).thenReturn(textoEsperado);

        ResponseEntity<String> result = teamController.export(equipoId, authentication);

        assertThat(result.getBody()).isEqualTo(textoEsperado);
    }

    @Test
    void getUsuarioId_debeLanzarExcepcionSiUsuarioNoExiste() {
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamController.listMyTeams(authentication))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}