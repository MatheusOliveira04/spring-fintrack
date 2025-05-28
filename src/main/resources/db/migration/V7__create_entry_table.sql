CREATE TABLE tb_entry (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    description VARCHAR(50) NOT NULL,
    value NUMERIC NOT NULL,
    date DATE NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    category_id UUID,
    user_id UUID,
    CONSTRAINT fk_entry_category FOREIGN KEY (category_id) REFERENCES tb_category(id),
    CONSTRAINT fk_entry_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);