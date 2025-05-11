CREATE TABLE IF NOT EXISTS tb_role (
    id SERIAL NOT NULL,
    name VARCHAR(255),
    CONSTRAINT tb_role_pkey PRIMARY KEY (id)
);