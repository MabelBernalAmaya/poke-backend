package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.controller.dto.request.EquipoRequest;
import com.pokepedia.pokecore.controller.dto.response.EquipoResponse;
import com.pokepedia.pokecore.core.exception.BusinessException;
import com.pokepedia.pokecore.core.exception.ResourceNotFoundException;
import com.pokepedia.pokecore.persistence.entity.relational.EquipoEntity;
import com.pokepedia.pokecore.persistence.entity.relational.EquipoPokemonEntity;
import com.pokepedia.pokecore.persistence.repository.relational.EquipoJpaRepository;
import com.pokepedia.pokecore.persistence.repository.relational.EquipoPokemonJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final int MAX_MIEMBROS = 6;
    private static final int MAX_EQUIPOS = 10;

    private final EquipoJpaRepository equipoRepository;
    private final EquipoPokemonJpaRepository equipoPokemonRepository;
    private final com.pokepedia.pokecore.core.port.PokemonPersistencePort pokemonPort;
    @Override
    @Transactional
    public EquipoResponse create(Long usuarioId, EquipoRequest request) {
        validarLimiteEquipos(usuarioId);
        validarPokemonesSinDuplicados(request.pokemonIds());

        EquipoEntity equipo = equipoRepository.save(
                EquipoEntity.builder()
                        .usuarioId(usuarioId)
                        .nombre(request.nombre())
                        .build()
        );

        guardarMiembros(equipo.getId(), request.pokemonIds());

        return toResponse(equipo, request.pokemonIds());
    }

    @Override
    public List<EquipoResponse> listByUsuario(Long usuarioId) {
        return equipoRepository.findByUsuarioId(usuarioId).stream()
                .map(equipo -> toResponse(equipo, obtenerPokemonIds(equipo.getId())))
                .toList();
    }

    @Override
    @Transactional
    public EquipoResponse update(Long equipoId, Long usuarioId, EquipoRequest request) {
        EquipoEntity equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", equipoId));

        validarPropietario(equipo, usuarioId);
        validarPokemonesSinDuplicados(request.pokemonIds());

        equipoPokemonRepository.deleteByEquipoId(equipoId);
        guardarMiembros(equipoId, request.pokemonIds());

        return toResponse(equipo, request.pokemonIds());
    }

    @Override
    @Transactional
    public void delete(Long equipoId, Long usuarioId) {
        EquipoEntity equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", equipoId));

        validarPropietario(equipo, usuarioId);
        equipoRepository.deleteById(equipoId);
    }

    private void validarLimiteEquipos(Long usuarioId) {
        if (equipoRepository.countByUsuarioId(usuarioId) >= MAX_EQUIPOS) {
            throw new BusinessException(
                    "El usuario ya tiene el maximo de " + MAX_EQUIPOS + " equipos", "TEAM_LIMIT_REACHED");
        }
    }

    private void validarPokemonesSinDuplicados(List<Long> pokemonIds) {
        if (pokemonIds.size() > MAX_MIEMBROS) {
            throw new BusinessException("Un equipo admite maximo " + MAX_MIEMBROS + " Pokemon", "TEAM_TOO_BIG");
        }
        Set<Long> unicos = new HashSet<>(pokemonIds);
        if (unicos.size() != pokemonIds.size()) {
            throw new BusinessException("No se permiten Pokemon duplicados en el equipo", "DUPLICATE_TEAM_MEMBER");
        }
    }

    private void validarPropietario(EquipoEntity equipo, Long usuarioId) {
        if (!equipo.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso sobre este equipo", "FORBIDDEN_TEAM_ACCESS");
        }
    }

    private void guardarMiembros(Long equipoId, List<Long> pokemonIds) {
        for (int i = 0; i < pokemonIds.size(); i++) {
            equipoPokemonRepository.save(EquipoPokemonEntity.builder()
                    .equipoId(equipoId)
                    .posicion(i + 1)
                    .pokemonId(pokemonIds.get(i))
                    .build());
        }
    }

    private List<Long> obtenerPokemonIds(Long equipoId) {
        return equipoPokemonRepository.findByEquipoId(equipoId).stream()
                .map(EquipoPokemonEntity::getPokemonId)
                .toList();
    }

    private EquipoResponse toResponse(EquipoEntity equipo, List<Long> pokemonIds) {
        return new EquipoResponse(equipo.getId(), equipo.getNombre(), pokemonIds, equipo.getCreatedAt());
    }
    @Override
    public String exportToText(Long equipoId, Long usuarioId) {
        EquipoEntity equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", equipoId));

        validarPropietario(equipo, usuarioId);

        List<Long> pokemonIds = obtenerPokemonIds(equipoId);
        StringBuilder texto = new StringBuilder();
        texto.append("Equipo: ").append(equipo.getNombre()).append("\n\n");

        for (Long pokemonId : pokemonIds) {
            pokemonPort.findById(pokemonId).ifPresent(pokemon -> {
                texto.append(pokemon.getName()).append("\n");
                texto.append("Tipo: ").append(String.join("/", pokemon.getTypes())).append("\n");
                if (pokemon.getStats() != null) {
                    texto.append("HP: ").append(pokemon.getStats().getHp())
                            .append(" | ATK: ").append(pokemon.getStats().getAttack())
                            .append(" | DEF: ").append(pokemon.getStats().getDefense())
                            .append("\n");
                }
                texto.append("\n");
            });
        }

        return texto.toString();
    }
}