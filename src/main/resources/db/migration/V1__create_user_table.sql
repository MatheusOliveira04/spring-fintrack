CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tb_user (
    enabled BOOLEAN,
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    verification_code VARCHAR(255),
    username VARCHAR(255),
    CONSTRAINT tb_user_pkey PRIMARY KEY (id),
    CONSTRAINT uk_username UNIQUE (username)
);
