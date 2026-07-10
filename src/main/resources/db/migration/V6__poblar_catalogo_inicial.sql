-- Region unica: Kanto (primera generacion)
INSERT INTO region (name) VALUES ('Kanto') ON CONFLICT (name) DO NOTHING;

-- Tipos usados por los pokemon de primera generacion elegidos
INSERT INTO type (name) VALUES ('Grass') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Poison') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Fire') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Water') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Electric') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Fighting') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Rock') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Ground') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Ghost') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Psychic') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Flying') ON CONFLICT (name) DO NOTHING;
INSERT INTO type (name) VALUES ('Normal') ON CONFLICT (name) DO NOTHING;

-- Pokemon de primera generacion, comunes y variados en tipo (actualiza Bulbasaur/Pikachu existentes)
INSERT INTO pokemon (national_number, name, region_id)
    VALUES (1, 'Bulbasaur', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (4, 'Charmander', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (7, 'Squirtle', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (25, 'Pikachu', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (41, 'Zubat', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (63, 'Abra', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (66, 'Machop', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (74, 'Geodude', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (92, 'Gastly', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

INSERT INTO pokemon (national_number, name, region_id)
    VALUES (133, 'Eevee', (SELECT id FROM region WHERE name = 'Kanto'))
    ON CONFLICT (national_number) DO UPDATE SET region_id = EXCLUDED.region_id;

-- Stats base (aproximados a los reales)
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 45, 49, 49, 65, 65, 45 FROM pokemon WHERE national_number = 1
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 39, 52, 43, 60, 50, 65 FROM pokemon WHERE national_number = 4
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 44, 48, 65, 50, 64, 43 FROM pokemon WHERE national_number = 7
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 35, 55, 40, 50, 50, 90 FROM pokemon WHERE national_number = 25
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 40, 45, 35, 30, 40, 55 FROM pokemon WHERE national_number = 41
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 25, 20, 15, 105, 55, 90 FROM pokemon WHERE national_number = 63
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 70, 80, 50, 35, 35, 35 FROM pokemon WHERE national_number = 66
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 40, 80, 100, 30, 30, 20 FROM pokemon WHERE national_number = 74
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 30, 35, 30, 100, 35, 80 FROM pokemon WHERE national_number = 92
    ON CONFLICT (pokemon_id) DO NOTHING;

INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed)
    SELECT id, 55, 55, 50, 45, 65, 55 FROM pokemon WHERE national_number = 133
    ON CONFLICT (pokemon_id) DO NOTHING;

-- Tipos por pokemon
INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 1 AND t.name = 'Grass'
    ON CONFLICT DO NOTHING;
INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 1 AND t.name = 'Poison'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 4 AND t.name = 'Fire'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 7 AND t.name = 'Water'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 25 AND t.name = 'Electric'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 41 AND t.name = 'Poison'
    ON CONFLICT DO NOTHING;
INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 41 AND t.name = 'Flying'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 63 AND t.name = 'Psychic'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 66 AND t.name = 'Fighting'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 74 AND t.name = 'Rock'
    ON CONFLICT DO NOTHING;
INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 74 AND t.name = 'Ground'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 92 AND t.name = 'Ghost'
    ON CONFLICT DO NOTHING;
INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 92 AND t.name = 'Poison'
    ON CONFLICT DO NOTHING;

INSERT INTO pokemon_type (pokemon_id, type_id)
    SELECT p.id, t.id FROM pokemon p, type t WHERE p.national_number = 133 AND t.name = 'Normal'
    ON CONFLICT DO NOTHING;
