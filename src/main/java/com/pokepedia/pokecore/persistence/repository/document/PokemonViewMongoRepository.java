package com.pokepedia.pokecore.persistence.repository.document;

import com.pokepedia.pokecore.persistence.entity.document.PokemonViewDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonViewMongoRepository extends MongoRepository<PokemonViewDocument, String> {

}