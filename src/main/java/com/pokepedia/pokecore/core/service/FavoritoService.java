package com.pokepedia.pokecore.core.service;

import java.util.List;

public interface FavoritoService {
    void addFavorito(Long usuarioId, Long pokemonId);
    void removeFavorito(Long usuarioId, Long pokemonId);
    List<Long> listFavoritos(Long usuarioId);
}