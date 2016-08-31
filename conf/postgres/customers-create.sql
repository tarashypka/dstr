
/**
 *
 * customers table contains personal Customer data
 * and is used only for presentation purposes
 *
 * FOREIGN KEY is of BIGSERIAL (BIGINT) type instead of varchar,
 * as it could have been if email was a PRIMARY KEY.
 *
 * This avoids
 *   - larger values (email) duplication with FOREIGN KEY
 *   - possible non-constant PRIMARY KEY values (email)
 *
 */

CREATE TABLE customers (
    id              BIGSERIAL           PRIMARY KEY,
    user_id         BIGINT              REFERENCES users(id),
    name            VARCHAR(20),
    surname         VARCHAR(20),
    n_orders        SMALLINT            DEFAULT 0,
    n_items         SMALLINT            DEFAULT 0,
    registered_on   DATE                NOT NULL DEFAULT CURRENT_DATE
);
