package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {

    boolean existsByNationalNumber(Integer nationalNumber);

    Optional<PokemonEntity> findByNationalNumber(Integer nationalNumber);

    @Override
    @EntityGraph(attributePaths = {"types", "stats", "region"})
    Optional<PokemonEntity> findById(Long id);

    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Query("SELECT p FROM PokemonEntity p")
    Page<PokemonEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"types", "region"})
    List<PokemonEntity> findByTypes_NameIgnoreCase(String typeName);
}