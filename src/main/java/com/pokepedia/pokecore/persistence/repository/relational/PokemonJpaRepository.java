package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {

    boolean existsByNationalNumber(Integer nationalNumber);

    Optional<PokemonEntity> findByNationalNumber(Integer nationalNumber);

    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Override
    Optional<PokemonEntity> findById(Long id);

    @EntityGraph(attributePaths = {"types", "region"})
    List<PokemonEntity> findByTypes_NameIgnoreCase(String typeName);
}