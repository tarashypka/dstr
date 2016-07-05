package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */

public class PostgresCustomerDao {
    private static final String COLL = "customers";
    private Connection postgresConn;

    public PostgresCustomerDao(DataSource source) {
        try {
            this.postgresConn = source.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        this.postgresConn.close();
    }

    public Customer selectCustomer(String email) throws SQLException {
        String query = "SELECT * FROM " + COLL + " WHERE email = ? ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
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

    public List<Customer> selectAllCustomers() throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "SELECT * FROM " + COLL + ";";
        ResultSet rs = stmt.executeQuery(sql);

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

    public boolean insertCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO " + COLL +
                " (email, password, name, surname) VALUES (?, ?, ?, ?) ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, customer.getEmail());
        stmt.setString(2, customer.getPassword());
        stmt.setString(3, customer.getName());
        stmt.setString(4, customer.getSurname());

        return stmt.executeUpdate() == 1;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE " + COLL +
                " SET (email, password, name, surname) = (?, ?, ?, ?) ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, customer.getEmail());
        stmt.setString(2, customer.getPassword());
        stmt.setString(3, customer.getName());
        stmt.setString(4, customer.getSurname());

        return stmt.executeUpdate() == 1;
    }

    public boolean changeCustomerStatus(String email) throws SQLException {
        String query = "SELECT * FROM " + COLL + " WHERE email = ? ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if ( ! rs.next()) {
            return false;
        }
        query = "UPDATE " + COLL + " SET enabled = ? WHERE email = ? ;";
        stmt = postgresConn.prepareStatement(query);
        stmt.setBoolean(1, ! rs.getBoolean("enabled"));
        stmt.setString(2, email);
        return stmt.executeUpdate() == 1;
    }

    public boolean deleteCustomer(String email) throws SQLException {
        String query = "DELETE FROM " + COLL + " WHERE email = ? ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, email);
        return stmt.executeUpdate() == 1;
    }

    public long count() throws SQLException {
        String query = "SELECT COUNT(*) FROM customers ;";
        Statement stmt = postgresConn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next() ? rs.getLong("count") : -1;
    }
}