package com.deoxys.dev.dstr.persistence.dao.postgres;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PostgresDAO<T> {

    DataSource dataSource;

    PostgresDAO(DataSource ds) {
        dataSource = ds;
    }

    protected T get(String query,
                    PreparedStatementFinisher<T> finisher,
                    ResultSetRetriever<T> retriever) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            finisher.finish(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return retriever.retrieve(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected List<T> getAll(String query,
                             PreparedStatementFinisher<T> finisher,
                             ResultSetRetriever<T> retriever) {

        List<T> objs = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            finisher.finish(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) objs.add(retriever.retrieve(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return objs;
    }

    protected boolean add(String query, PreparedStatementFinisher<T> finisher) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            finisher.finish(stmt);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Polymorphic add(), update(), delete() implementations are the same on this level!
     */
    protected boolean update(String query, PreparedStatementFinisher<T> finisher) {
        return add(query, finisher);
    }

    protected boolean delete(String query, PreparedStatementFinisher<T> finisher) {
        return add(query, finisher);
    }

    /**
     *
     * Using type parameter here allows to have single implementation for
     * different unique types of each persisted entity in the database.
     *
     * F.e User may have unique email (String) or unique id (long),
     *     but implementation remains the same
     *
     */
    protected boolean exists(String query, PreparedStatementFinisher<T> finisher) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            finisher.finish(stmt);
            if (stmt.executeQuery().next()) return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public long count(String query) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) return rs.getLong("count");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}