package com.pokepedia.pokecore.persistence.repository.relational;

import com.pokepedia.pokecore.persistence.entity.relational.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {

    boolean existsByNationalNumber(Integer nationalNumber);

    Optional<PokemonEntity> findByNationalNumber(Integer nationalNumber);

}