package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */

public class CustomerDAO extends PostgresDAO<Customer> {

    private static String COLLECTION = "customers";

    public CustomerDAO(DataSource ds) {
        super(ds);
    }

    // Customer specific sql statements
    private static final String
            SELECT_CUSTOMER_BY_ID,
            SELECT_CUSTOMER_BY_EMAIL,
            SELECT_ALL_CUSTOMERS,
            SELECT_ID_BY_EMAIL,
            SELECT_STATUS_BY_ID,
            INSERT_CUSTOMER,
            UPDATE_CUSTOMER_BY_ID,
            UPDATE_STATUS_BY_ID,
            DELETE_CUSTOMER_BY_ID,
            COUNT_CUSTOMERS;

    static {
        SELECT_CUSTOMER_BY_ID =
                "SELECT * FROM " + COLLECTION + " " +
                "WHERE id = ?";

        SELECT_CUSTOMER_BY_EMAIL =
                "SELECT * FROM " + COLLECTION + " " +
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

        COUNT_CUSTOMERS =
                "SELECT COUNT(*) FROM " + COLLECTION;
    }

    @Override
    public Customer get(long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_CUSTOMER_BY_ID);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Customer(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("role"),
                    rs.getBoolean("enabled"));
        }
        return null;
    }

    public Customer get(String email) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_CUSTOMER_BY_EMAIL);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Customer(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("role"),
                    rs.getBoolean("enabled"));
        }
        return null;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SELECT_ALL_CUSTOMERS);

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(new Customer(
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
        PreparedStatement stmt = connection.prepareStatement(INSERT_CUSTOMER);
        stmt.setString(1, customer.getEmail());
        stmt.setString(2, customer.getPassword());
        stmt.setString(3, customer.getName());
        stmt.setString(4, customer.getSurname());
        if (stmt.executeUpdate() != 1) return false;
        long id = getId(customer);
        if (id < 0) return false;
        customer.setId(id);
        return true;
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(UPDATE_CUSTOMER_BY_ID);
        stmt.setString(1, customer.getEmail());
        stmt.setString(2, customer.getPassword());
        stmt.setString(3, customer.getName());
        stmt.setString(4, customer.getSurname());
        stmt.setString(5, customer.getEmail());
        return stmt.executeUpdate() == 1;
    }

    @Override
    public boolean delete(long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(DELETE_CUSTOMER_BY_ID);
        stmt.setLong(1, id);
        return stmt.executeUpdate() == 1;
    }

    @Override
    public long count() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(COUNT_CUSTOMERS);
        return rs.next() ? rs.getLong("count") : -1;
    }

    private long getId(Customer customer) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_ID_BY_EMAIL);
        stmt.setString(1, customer.getEmail());
        ResultSet rs = stmt.executeQuery();
        if ( ! rs.next()) return -1;
        return rs.getLong("id");
    }

    public boolean updateStatus(long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_STATUS_BY_ID);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        if ( ! rs.next()) return false;
        stmt = connection.prepareStatement(UPDATE_STATUS_BY_ID);
        stmt.setString(1, COLLECTION);
        stmt.setBoolean(2, ! rs.getBoolean("enabled"));
        stmt.setLong(3, id);
        return stmt.executeUpdate() == 1;
    }
}