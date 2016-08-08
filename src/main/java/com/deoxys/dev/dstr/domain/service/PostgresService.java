package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.HttpRequestReader;
import com.deoxys.dev.dstr.domain.converter.HttpSessionReader;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Service that uses Postgres Connection Pool
 */

abstract class PostgresService<T> {

    DataSource dataSource;
    HttpRequestReader<T> requestReader;
    HttpSessionReader<T> sessionReader;

    <t extends HttpRequestReader & HttpSessionReader> PostgresService(t reader) {
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
