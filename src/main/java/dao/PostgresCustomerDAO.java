package dao;

import model.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Statement stmt = postgresConn.createStatement();
        String sql = "SELECT * FROM " + COLL + " WHERE email = '" + email + "';";
        ResultSet rs = stmt.executeQuery(sql);

        return rs.next() ? new Customer(rs.getString("name"), rs.getString("surname"),
                email, rs.getString("password")) : null;
    }

    public List<Customer> selectAllCustomers() throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "SELECT * FROM " + COLL + ";";
        ResultSet rs = stmt.executeQuery(sql);

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String email = rs.getString("email");
            String password = rs.getString("password");
            customers.add(new Customer(name, surname, email, password));
        }
        return customers;
    }

    public boolean insertCustomer(Customer customer) throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "INSERT INTO " + COLL +
                "(name, surname, email, password) VALUES ('" +
                customer.getName() + "', '" +
                customer.getSurname() + "', '" +
                customer.getEmail() + "', '" +
                customer.getPassword() + "');";

        return stmt.executeUpdate(sql) == 1;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "UPDATE " + COLL + " SET (name, surname, email, password) = ('" +
                customer.getName() + "', '" +
                customer.getSurname() + "', '" +
                customer.getEmail() + "', '" +
                customer.getPassword() + "') WHERE email = '" +
                customer.getEmail() + "';";

        return stmt.executeUpdate(sql) == 1;
    }

    public boolean deleteCustomer(String email) throws SQLException {
        Statement stmt = postgresConn.createStatement();
        String sql = "DELETE FROM " + COLL + " WHERE email = '" + email + "';";

        return stmt.executeUpdate(sql) == 1;
    }
}