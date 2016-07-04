package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */

public class PostgresCustomerDAO {
    private static final String COLL = "customers";
    private Connection postgresConn;

    public PostgresCustomerDAO(DataSource source) throws SQLException {
        this.postgresConn = source.getConnection();
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
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String password = rs.getString("password");
            String role = rs.getString("role");
            boolean enabled = rs.getBoolean("enabled");

            return new Customer(name, surname, email, password, role, enabled);
        }
        return null;
    }

    public List<Customer> selectAllCustomers() throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "SELECT * FROM " + COLL + ";";
        ResultSet rs = stmt.executeQuery(sql);

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String role = rs.getString("role");
            boolean enabled = rs.getBoolean("enabled");
            customers.add(new Customer(name, surname, email, password, role, enabled));
        }
        return customers;
    }

    public boolean insertCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO " + COLL +
                " (name, surname, email, password, role) VALUES (?, ?, ?, ?, ?) ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getSurname());
        stmt.setString(3, customer.getEmail());
        stmt.setString(4, customer.getPassword());
        stmt.setString(5, "customer");

        return stmt.executeUpdate() == 1;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE " + COLL +
                " SET (name, surname, email, password) = (?, ?, ?, ?) ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getSurname());
        stmt.setString(3, customer.getEmail());
        stmt.setString(4, customer.getPassword());

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
        boolean enabled = rs.getBoolean("enabled");

        query = "UPDATE " + COLL + " SET enabled = ? WHERE email = ? ;";
        stmt = postgresConn.prepareStatement(query);
        stmt.setBoolean(1, ! enabled);
        stmt.setString(2, email);

        return stmt.executeUpdate() == 1;
    }

    public boolean deleteCustomer(String email) throws SQLException {
        String query = "DELETE FROM " + COLL + " WHERE email = ? ;";
        PreparedStatement stmt = postgresConn.prepareStatement(query);
        stmt.setString(1, email);

        return stmt.executeUpdate() == 1;
    }
}