package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public final class PostgresCustomerDaoDeleteCustomerTest extends PostgresTestDataSource {

    private static PostgresCustomerDao customerDAO;
    private Customer mike = new Customer("mike@gmail.com", "1234", "Mike", "Mort");

    @BeforeClass
    public static void setUpClass() {
        customerDAO = new PostgresCustomerDao(PostgresTestDataSource.getDataSource());
    }

    @Before
    public void setUp() throws Exception {
        assertTrue(customerDAO.insertCustomer(mike));
    }

    @Test
    public void testDeleteMike_thereAreNoMike_methodReturnsFalse() throws SQLException {
        assertTrue(customerDAO.deleteCustomer(mike.getEmail()));
        assertFalse(customerDAO.deleteCustomer(mike.getEmail()));
    }

    @Test
    public void testDeleteMike_thereIsMike_onlyMikeWasDeleted() throws SQLException {
        long nCustomersOld = customerDAO.count();
        assertTrue(customerDAO.deleteCustomer(mike.getEmail()));
        assertNull(customerDAO.selectCustomer(mike.getEmail()));
        long nCustomersNew = customerDAO.count();
        assertEquals(nCustomersNew, nCustomersOld - 1);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        customerDAO.closeConnection();
    }
}