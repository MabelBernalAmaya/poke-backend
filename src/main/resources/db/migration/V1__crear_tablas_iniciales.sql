CREATE TABLE region (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE type (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE pokemon (
                         id BIGSERIAL PRIMARY KEY,
                         national_number INTEGER NOT NULL UNIQUE,
                         name VARCHAR(100) NOT NULL,
                         region_id BIGINT REFERENCES region(id),
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE pokemon_stats (
                               id BIGSERIAL PRIMARY KEY,
                               hp INTEGER,
                               attack INTEGER,
                               defense INTEGER,
                               special_attack INTEGER,
                               special_defense INTEGER,
                               speed INTEGER,
                               pokemon_id BIGINT UNIQUE REFERENCES pokemon(id)
);

CREATE TABLE pokemon_type (
                              pokemon_id BIGINT REFERENCES pokemon(id),
                              type_id BIGINT REFERENCES type(id),
                              PRIMARY KEY (pokemon_id, type_id)
);

CREATE INDEX idx_pokemon_number ON pokemon(national_number);