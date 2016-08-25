package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.User;
import com.deoxys.dev.dstr.domain.model.UserRoles;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends PostgresDAO<User> {

    public CustomerDAO(DataSource ds) {
        super(ds);
    }

    private static final String
            COLLECTION,
            SELECT_CUSTOMER_BY_ID,
            SELECT_CUSTOMER_BY_EMAIL,
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

        SELECT_CUSTOMER_BY_EMAIL =
                "SELECT password, name, surname, role, enabled FROM " + COLLECTION + " " +
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
     * Password is required only on login for validation purposes
     */
    @Override
    public User get(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new User.UserBuilder(rs.getString("email"))
                        .withName(rs.getString("name"), rs.getString("surname"))
                        .withRole(UserRoles.roleOf(rs.getString("role")))
                        .enabled(rs.getBoolean("enabled"))
                        .build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Required on user login only to verify Customer credentials (password, enabled).
     *
     * It's better to use get(email) with 1 connection to database,
     * than get(getId(email)) with 2 connections to database.
     */
    public User get(String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMER_BY_EMAIL)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new User.UserBuilder(email)
                        .withPassword(rs.getString("password"))
                        .withName(rs.getString("name"), rs.getString("surname"))
                        .withRole(UserRoles.roleOf(rs.getString("role")))
                        .enabled(rs.getBoolean("enabled"))
                        .build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CUSTOMERS)) {

            while (rs.next()) users.add(
                    new User.UserBuilder(rs.getString("email"))
                            .withId(rs.getLong("id"))
                            .withPassword(rs.getString("password"))
                            .withName(rs.getString("name"), rs.getString("surname"))
                            .withRole(UserRoles.roleOf(rs.getString("role")))
                            .enabled(rs.getBoolean("enabled"))
                            .build()
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean add(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            if (stmt.executeUpdate() != 1) return false;
            long id = getId(user);
            if (id < 0) return false;
            user.setId(id);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER_BY_ID)) {

            stmt.setLong(1, user.getId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getEmail());
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER_BY_ID)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean exists(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_CUSTOMERS_BY_EMAIL)) {

            stmt.setString(1, user.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getLong("count") > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public long count() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_ALL_CUSTOMERS)) {

            if (rs.next()) return rs.getLong("count");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private long getId(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ID_BY_EMAIL)) {

            stmt.setString(1, user.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public boolean swapStatus(long id) {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Validate user before login
     */
    public boolean hasValidCredentials(User user) {
        User shouldBe = get(user.getEmail());
        if (shouldBe == null)
            user.setErrType("email_wrong");
        else if (! user.hasValidPassword(shouldBe.getPassword()))
            user.setErrType("psswd_wrong");
        else if ( ! shouldBe.isEnabled())
            user.setErrType("acc_closed");
        else {
            // Login succeeded
            user.setPassword(null);     // password is not required anymore
            user.setName(shouldBe.getName());   // name is required for view
            user.setSurname(shouldBe.getSurname()); // surname is required for view
            user.setRole(shouldBe.getRole());   // role is required for authorization
            user.setEnabled(shouldBe.isEnabled());  // may be of help in future
            return true;
        }
        return false;
    }

    public boolean isValidForInput(User customer, String psswd2) {
        String email = customer.getEmail();
        String psswd = customer.getPassword();
        String name = customer.getName();
        String sname = customer.getSurname();
        if (email == null || email.isEmpty())
            customer.setErrType("email_empty");
        else if (! email.matches("\\S+@\\w+\\.\\w+"))
            customer.setErrType("email_wrong");
        else if (psswd == null || psswd.isEmpty())
            customer.setErrType("psswd_empty");
        else if (psswd.length() < 8)
            customer.setErrType("psswd_weak");
        else if (! psswd.equals(psswd2))
            customer.setErrType("psswd2_wrong");
        else if (name == null || name.isEmpty())
            customer.setErrType("name_empty");
        else if (sname == null || sname.isEmpty())
            customer.setErrType("sname_empty");
        else return true;
        return false;
    }
}