package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */

public class CustomerDAO extends PostgresDAO<Customer> {

    public CustomerDAO(DataSource ds) {
        super(ds);
    }

    private static final String
            COLLECTION,
            SELECT_CUSTOMER_BY_ID,
            SELECT_CREDENTIALS_BY_EMAIL,
            SELECT_ALL_CUSTOMERS,
            SELECT_ID_BY_EMAIL,
            SELECT_STATUS_BY_ID,
            INSERT_CUSTOMER,
            UPDATE_CUSTOMER_BY_ID,
            UPDATE_STATUS_BY_ID,
            DELETE_CUSTOMER_BY_ID,
            COUNT_ALL_CUSTOMERS,
            COUNT_CUSTOMERS_BY_EMAIL;

    static {
        COLLECTION = "customers";

        SELECT_CUSTOMER_BY_ID =
                "SELECT * FROM " + COLLECTION + " " +
                "WHERE id = ?";

        SELECT_CREDENTIALS_BY_EMAIL =
                "SELECT password, enabled FROM " + COLLECTION + " " +
                 "WHERE email = ?";

        SELECT_ALL_CUSTOMERS =
                "SELECT * FROM " + COLLECTION;

        SELECT_ID_BY_EMAIL =
                "SELECT id FROM " + COLLECTION + " " +
                "WHERE email = ?";

        SELECT_STATUS_BY_ID =
                "SELECT enabled FROM " + COLLECTION + " " +
                "WHERE id = ?";

        INSERT_CUSTOMER =
                "INSERT INTO " + COLLECTION + "(email, password, name, surname) " +
                "VALUES (?, ?, ?, ?)";

        UPDATE_CUSTOMER_BY_ID =
                "UPDATE " + COLLECTION + " " +
                "SET (email, password, name, surname) = (?, ?, ?, ?) " +
                "WHERE id = ?";

        UPDATE_STATUS_BY_ID =
                "UPDATE " + COLLECTION + " " +
                "SET enabled = ? " +
                "WHERE id = ?";

        DELETE_CUSTOMER_BY_ID =
                "DELETE FROM " + COLLECTION + " " +
                "WHERE id = ?";

        COUNT_ALL_CUSTOMERS =
                "SELECT COUNT(*) FROM " + COLLECTION;

        COUNT_CUSTOMERS_BY_EMAIL =
                "SELECT COUNT(*) FROM " + COLLECTION + " " +
                "WHERE email = ?";
    }

    /**
     * Return user without password
     * Password is required only on login for
     */
    @Override
    public Customer get(long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next()
                        ? new Customer(
                                id,
                                rs.getString("email"),
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getString("role"),
                                rs.getBoolean("enabled"))
                        : null;
            }
        }
    }

    /**
     * Required on user login only to verify Customer credentials (password, enabled).
     *
     * It's better to use get(email) with 1 connection to database,
     * than get(getId(email)) with 2 connections to database.
     */
    public Customer get(String email) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CREDENTIALS_BY_EMAIL)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next()
                        ? new Customer(
                                rs.getString("password"),
                                rs.getBoolean("enabled"))
                        : null;
            }
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CUSTOMERS)) {

            while (rs.next())
                customers.add(new Customer(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("role"),
                        rs.getBoolean("enabled")));
        }
        return customers;
    }

    @Override
    public boolean add(Customer customer) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER)) {

            stmt.setString(1, customer.getEmail());
            stmt.setString(2, BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt()));
            stmt.setString(3, customer.getName());
            stmt.setString(4, customer.getSurname());
            if (stmt.executeUpdate() != 1) return false;
            long id = getId(customer);
            if (id < 0) return false;
            customer.setId(id);
            return true;
        }
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER_BY_ID)) {

            stmt.setString(1, customer.getEmail());
            stmt.setString(2, customer.getPassword());
            stmt.setString(3, customer.getName());
            stmt.setString(4, customer.getSurname());
            stmt.setString(5, customer.getEmail());
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER_BY_ID)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean exists(Customer customer) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_CUSTOMERS_BY_EMAIL)) {

            stmt.setString(1, customer.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getLong("count") > 0;
            }
        }
    }

    @Override
    public long count() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_ALL_CUSTOMERS)) {

            return rs.next() ? rs.getLong("count") : -1;
        }
    }

    private long getId(Customer customer) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ID_BY_EMAIL)) {

            stmt.setString(1, customer.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong("id") : -1;
            }
        }
    }

    public boolean swapStatus(long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(SELECT_STATUS_BY_ID)) {

            stmt1.setLong(1, id);
            try (ResultSet rs = stmt1.executeQuery();
                 PreparedStatement stmt2 = conn.prepareStatement(UPDATE_STATUS_BY_ID)) {

                if ( ! rs.next()) return false;
                stmt2.setBoolean(1, ! rs.getBoolean("enabled"));
                stmt2.setLong(2, id);
                return stmt2.executeUpdate() == 1;
            }
        }
    }
}