package com.deoxys.dev.dstr.persistence.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by deoxys on 09.07.16.
 */

public abstract class PostgresDAO<T> {

    protected Connection connection;

    public PostgresDAO(DataSource ds) {
        try {
            connection = ds.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public abstract T get(long id) throws SQLException;
    public abstract List<T> getAll() throws SQLException;
    public abstract boolean add(T obj) throws SQLException;
    public abstract boolean update(T obj) throws SQLException;
    public abstract boolean delete(long id) throws SQLException;
    public abstract long count() throws SQLException;

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
