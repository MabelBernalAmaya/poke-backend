package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.EquipoPokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoPokemonJpaRepository
        extends JpaRepository<EquipoPokemonEntity, EquipoPokemonEntity.EquipoPokemonId> {
    List<EquipoPokemonEntity> findByEquipoId(Long equipoId);
    void deleteByEquipoId(Long equipoId);
}