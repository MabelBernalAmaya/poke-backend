package com.pokepedia.pokecore.core.service;

import com.pokepedia.pokecore.core.exception.DuplicateResourceException;
import com.pokepedia.pokecore.persistence.entity.relational.FavoritoEntity;
import com.pokepedia.pokecore.persistence.repository.relational.FavoritoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoJpaRepository favoritoRepository;

    @Override
    public void addFavorito(Long usuarioId, Long pokemonId) {
        if (favoritoRepository.existsByUsuarioIdAndPokemonId(usuarioId, pokemonId)) {
            throw new DuplicateResourceException("Favorito", "pokemonId", pokemonId);
        }
        favoritoRepository.save(FavoritoEntity.builder()
                .usuarioId(usuarioId)
                .pokemonId(pokemonId)
                .build());
    }

    @Override
    public void removeFavorito(Long usuarioId, Long pokemonId) {
        favoritoRepository.deleteByUsuarioIdAndPokemonId(usuarioId, pokemonId);
    }

    @Override
    public List<Long> listFavoritos(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId).stream()
                .map(FavoritoEntity::getPokemonId)
                .toList();
    }
}