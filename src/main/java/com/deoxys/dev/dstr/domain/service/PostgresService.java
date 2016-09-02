package com.deoxys.dev.dstr.domain.service;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Service that uses Postgres Connection Pool
 */

abstract class PostgresService {

    /**
     * Any PostgresService subclass can use the same DataSource
     * to initialize different PostgresDAO implementations.
     */
    DataSource dataSource;

    PostgresService() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/postgres");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    PostgresService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
