package com.deoxys.dev.dstr.persistence.dao.postgres;

import com.deoxys.dev.dstr.domain.model.user.Customer;
import com.deoxys.dev.dstr.domain.model.user.UserRoles;

import javax.sql.DataSource;
import java.util.List;

public class CustomerDAO extends PostgresDAO<Customer> {

    public CustomerDAO(DataSource ds) {
        super(ds);
    }

    private static final String
            CUSTOMERS_COLLECTION,
            USERS_COLLECTION,
            SELECT_CUSTOMER_BY_ID,
            SELECT_ALL_CUSTOMERS,
            INSERT_CUSTOMER,
            UPDATE_CUSTOMER_NAME_BY_ID,        // different update clauses
            UPDATE_CUSTOMER_NORDERS_BY_ID,     // unnecessary updates
            UPDATE_CUSTOMER_NITEMS_BY_ID,
            DELETE_CUSTOMER_BY_ID,
            COUNT_ALL_CUSTOMERS;

    private static final ResultSetRetriever<Customer> customerRetriever;

    static {
        CUSTOMERS_COLLECTION = "customers";

        USERS_COLLECTION = "users";

        // Select all customer data
        SELECT_CUSTOMER_BY_ID =
                "SELECT  cs.name, cs.surname, cs.n_orders, cs.n_items, cs.registered_on" +
                        "us.email, us.password, us.role, us.enabled " +
                "FROM " + CUSTOMERS_COLLECTION + " cs, " + USERS_COLLECTION + " us " +
                "WHERE us.id = ? AND us.id = cs.user_id";

        SELECT_ALL_CUSTOMERS =
                "SELECT * FROM " + CUSTOMERS_COLLECTION;

        INSERT_CUSTOMER =
                "INSERT INTO " + CUSTOMERS_COLLECTION + "(user_id, name, surname) " +
                "VALUES (?, ?, ?)";

        // It could be customer trying to change his personal data
        UPDATE_CUSTOMER_NAME_BY_ID =
                "UPDATE " + CUSTOMERS_COLLECTION + " " +
                "SET name = ?, surname = ? " +
                "WHERE id = ?";

        // Is updated on each new order
        UPDATE_CUSTOMER_NORDERS_BY_ID =
                "UPDATE " + CUSTOMERS_COLLECTION + " " +
                "SET n_orders = ? " +
                "WHERE id = ?";

        // Is updated when any orders status changes from/to processed
        UPDATE_CUSTOMER_NITEMS_BY_ID =
                "UPDATE " + CUSTOMERS_COLLECTION + " " +
                "SET n_items = ? " +
                "WHERE id = ?";

        DELETE_CUSTOMER_BY_ID =
                "DELETE FROM " + CUSTOMERS_COLLECTION + " " +
                "WHERE id = ?";

        COUNT_ALL_CUSTOMERS =
                "SELECT COUNT(*) FROM " + CUSTOMERS_COLLECTION;

        customerRetriever = rs ->
                new Customer.CustomerBuilder(rs.getString("email"))
                        .withPassword(rs.getString("password"))
                        .withName(rs.getString("name"), rs.getString("surname"))
                        .withNOrders(rs.getInt("n_orders"))
                        .withNItems(rs.getInt("n_items"))
                        .withRole(UserRoles.roleOf(rs.getString("role")))
                        .enabled(rs.getBoolean("enabled"))
                        .registeredOn(rs.getDate("registered_on"))
                        .build();
    }

    public Customer get(long id) {
        return get(SELECT_CUSTOMER_BY_ID, stmt -> stmt.setLong(1, id), customerRetriever);
    }

    public List<Customer> getAll() {
        return getAll(SELECT_ALL_CUSTOMERS, stmt -> { }, customerRetriever);
    }

    public boolean add(Customer customer) {
        UserDAO userDAO = new UserDAO(this.dataSource);
        return userDAO.add(customer) && add(INSERT_CUSTOMER,
                stmt -> {
                    stmt.setLong(1, customer.getId());
                    stmt.setString(2, customer.getName());
                    stmt.setString(3, customer.getSurname());
                }
        );
    }

    public boolean updateName(Customer customer) {
        return update(UPDATE_CUSTOMER_NAME_BY_ID,
                stmt -> {
                    stmt.setString(1, customer.getName());
                    stmt.setString(2, customer.getSurname());
                    stmt.setLong(3, customer.getId());
                }
        );
    }

    public boolean updatenOrders(Customer customer) {
        return update(UPDATE_CUSTOMER_NORDERS_BY_ID,
                stmt -> {
                    stmt.setInt(1, customer.getnOrders());
                    stmt.setLong(2, customer.getId());
                }
        );
    }

    public boolean updatenItems(Customer customer) {
        return update(UPDATE_CUSTOMER_NITEMS_BY_ID,
                stmt -> {
                    stmt.setInt(1, customer.getnItems());
                    stmt.setLong(2, customer.getId());
                }
        );
    }

    public boolean delete(long id) {
        return delete(DELETE_CUSTOMER_BY_ID, stmt -> stmt.setLong(1, id));
    }

    public long count() {
        return count(COUNT_ALL_CUSTOMERS);
    }
}