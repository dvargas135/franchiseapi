CREATE TABLE IF NOT EXISTS franchises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    CONSTRAINT uk_franchises_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS branches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    CONSTRAINT fk_branches_franchise
        FOREIGN KEY (franchise_id) REFERENCES franchises(id)
        ON DELETE CASCADE,
    CONSTRAINT uk_branches_franchise_name UNIQUE (franchise_id, name)
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    stock INT NOT NULL,
    CONSTRAINT fk_products_branch
        FOREIGN KEY (branch_id) REFERENCES branches(id)
        ON DELETE CASCADE,
    CONSTRAINT uk_products_branch_name UNIQUE (branch_id, name),
    CONSTRAINT chk_products_stock CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT uk_app_users_username UNIQUE (username)
);
