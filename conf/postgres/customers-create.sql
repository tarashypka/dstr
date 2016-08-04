CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(20),
    surname VARCHAR(20),
    role VARCHAR(10) DEFAULT 'customer',
    enabled boolean DEFAULT true
);
