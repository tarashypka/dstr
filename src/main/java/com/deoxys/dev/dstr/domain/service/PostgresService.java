package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.HttpRequestReader;
import com.deoxys.dev.dstr.domain.converter.HttpSessionReader;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Created by deoxys on 08.07.16.
 *
 * Service that uses Postgres Connection Pool
 */

public abstract class PostgresService<T> {

    protected DataSource dataSource;
    protected HttpRequestReader<T> requestReader;
    protected HttpSessionReader<T> sessionReader;

    public <T extends HttpRequestReader & HttpSessionReader> PostgresService(T reader) {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/postgres");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        requestReader = reader;
        sessionReader = reader;
    }
}
