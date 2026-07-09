CREATE TABLE password_reset_token (
                                      id BIGSERIAL PRIMARY KEY,
                                      usuario_id BIGINT NOT NULL REFERENCES app_user(id),
                                      token VARCHAR(255) NOT NULL UNIQUE,
                                      expires_at TIMESTAMP NOT NULL,
                                      used BOOLEAN NOT NULL DEFAULT false
);
