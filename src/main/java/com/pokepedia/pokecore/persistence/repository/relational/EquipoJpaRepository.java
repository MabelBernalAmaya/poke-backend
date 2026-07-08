package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.EquipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoJpaRepository extends JpaRepository<EquipoEntity, Long> {
    List<EquipoEntity> findByUsuarioId(Long usuarioId);
    long countByUsuarioId(Long usuarioId);
}