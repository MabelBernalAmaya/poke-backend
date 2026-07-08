CREATE TABLE equipo (
                        id BIGSERIAL PRIMARY KEY,
                        usuario_id BIGINT NOT NULL REFERENCES app_user(id),
                        nombre VARCHAR(25) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE equipo_pokemon (
                                equipo_id BIGINT NOT NULL REFERENCES equipo(id) ON DELETE CASCADE,
                                pokemon_id BIGINT NOT NULL REFERENCES pokemon(id),
                                posicion INTEGER NOT NULL,
                                PRIMARY KEY (equipo_id, posicion)
);