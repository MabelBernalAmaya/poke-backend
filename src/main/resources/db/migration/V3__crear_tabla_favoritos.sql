CREATE TABLE favorito (
                          usuario_id BIGINT NOT NULL REFERENCES app_user(id),
                          pokemon_id BIGINT NOT NULL REFERENCES pokemon(id),
                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                          PRIMARY KEY (usuario_id, pokemon_id)
);