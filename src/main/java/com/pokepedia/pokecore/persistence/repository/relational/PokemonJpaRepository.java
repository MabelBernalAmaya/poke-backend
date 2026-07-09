package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Query("""
        SELECT DISTINCT p FROM PokemonEntity p
        LEFT JOIN p.types t
        LEFT JOIN p.stats s
        LEFT JOIN p.region r
        WHERE (:type IS NULL OR LOWER(t.name) = LOWER(:type))
        AND (:region IS NULL OR LOWER(r.name) = LOWER(:region))
        AND (:minStat IS NULL OR (s.hp >= :minStat AND s.attack >= :minStat AND s.defense >= :minStat))
        AND (:maxStat IS NULL OR (s.hp <= :maxStat AND s.attack <= :maxStat AND s.defense <= :maxStat))
    """)
    List<PokemonEntity> findByFilters(
            @Param("type") String type,
            @Param("region") String region,
            @Param("minStat") Integer minStat,
            @Param("maxStat") Integer maxStat
    );
}