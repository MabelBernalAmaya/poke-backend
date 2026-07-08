package com.pokepedia.pokecore.controller.impl;

import com.pokepedia.pokecore.controller.api.TeamApi;
import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;
import com.pokepedia.pokecore.core.service.TeamService;
import com.pokepedia.pokecore.persistence.entity.relational.UserEntity;
import com.pokepedia.pokecore.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;
    private final UserJpaRepository userRepository;

    private Long getUsuarioId(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        return user.getId();
    }

    @Override
    public ResponseEntity<List<EquipoResponse>> listMyTeams(Authentication authentication) {
        return ResponseEntity.ok(teamService.listByUsuario(getUsuarioId(authentication)));
    }

    @Override
    public ResponseEntity<EquipoResponse> create(EquipoRequest request, Authentication authentication) {
        return ResponseEntity.ok(teamService.create(getUsuarioId(authentication), request));
    }

    @Override
    public ResponseEntity<EquipoResponse> update(Long id, EquipoRequest request, Authentication authentication) {
        return ResponseEntity.ok(teamService.update(id, getUsuarioId(authentication), request));
    }

    @Override
    public ResponseEntity<Void> delete(Long id, Authentication authentication) {
        teamService.delete(id, getUsuarioId(authentication));
        return ResponseEntity.noContent().build();
    }
}