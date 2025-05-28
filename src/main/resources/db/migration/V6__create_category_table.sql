CREATE TABLE tb_category (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    description VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    user_id UUID,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES tb_user (id)
);