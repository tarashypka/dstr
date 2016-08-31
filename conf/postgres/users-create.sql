
/**
 *
 * users table contains User credentials
 * and is only used for registration, authentication purposes
 *
 */

CREATE TABLE users (
    id              BIGSERIAL           PRIMARY KEY,
    email           VARCHAR(30)         NOT NULL UNIQUE,
    password        VARCHAR(100)        NOT NULL,
    role            VARCHAR(10)         DEFAULT 'CUSTOMER',
    enabled         boolean             DEFAULT true
);
