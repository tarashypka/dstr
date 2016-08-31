package com.deoxys.dev.dstr.persistence.dao.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * Used to pass a specific implementation of PreparedStatement#set???() operations
 * to some basic PostgresDAO methods, that are specific to the particular PostgresDAO subclass,
 * thus, avoiding code duplication
 *
 */
@FunctionalInterface
interface PreparedStatementFinisher<T> {

    /**
     *
     * Finishes prepared statement by replacing unknown parameters ? with actual data
     *
     * @param   stmt    prepared statement with particular query that contains ? in it
     */
    void finish(PreparedStatement stmt) throws SQLException;
}
