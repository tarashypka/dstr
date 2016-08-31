package com.deoxys.dev.dstr.persistence.dao.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Used to pass a specific implementation of ResultSet#get???() operations
 * to some basic PostgresDAO methods, that are specific to the particular PostgresDAO subclass,
 * thus, avoiding code duplication
 *
 */
@FunctionalInterface
interface ResultSetRetriever<T> {

    /**
     *
     * Retrieve a specific type from the ResultSet object
     *
     * @param   rs      set containing all selected data
     * @return          type retrieved from rs data
     */
    T retrieve(ResultSet rs) throws SQLException;
}
