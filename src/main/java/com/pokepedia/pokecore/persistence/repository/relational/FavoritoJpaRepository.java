package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, FavoritoEntity.FavoritoId> {
    List<FavoritoEntity> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndPokemonId(Long usuarioId, Long pokemonId);
    void deleteByUsuarioIdAndPokemonId(Long usuarioId, Long pokemonId);
}