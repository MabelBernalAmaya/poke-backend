CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password_hash VARCHAR(255),
                          role VARCHAR(20) NOT NULL
);