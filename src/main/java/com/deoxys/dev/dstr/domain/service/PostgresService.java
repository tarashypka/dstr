package com.deoxys.dev.dstr.domain.service;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Created by deoxys on 08.07.16.
 *
 * Service that uses Postgres Connection Pool
 */

public abstract class PostgresService {

    public DataSource dataSource;

    public PostgresService() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/postgres");
            System.out.println("DS=" + dataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
