package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;

import java.util.List;

public interface TeamService {
    EquipoResponse create(Long usuarioId, EquipoRequest request);
    List<EquipoResponse> listByUsuario(Long usuarioId);
    EquipoResponse update(Long equipoId, Long usuarioId, EquipoRequest request);
    void delete(Long equipoId, Long usuarioId);
    String exportToText(Long equipoId, Long usuarioId);
}