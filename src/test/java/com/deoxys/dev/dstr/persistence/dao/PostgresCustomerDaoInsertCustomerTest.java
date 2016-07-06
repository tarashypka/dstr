package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public final class PostgresCustomerDaoInsertCustomerTest {

    private static PostgresCustomerDao customerDAO;
    private Customer mike = new Customer("mike@gmail.com", "1234", "Mike", "Mort");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        customerDAO = new PostgresCustomerDao(PostgresTestDataSource.getDataSource());
    }

    @Test
    public void testInsertMike_thereIsMike_duplicateExceptionThrown() throws SQLException {
        assertTrue(customerDAO.insertCustomer(mike));

        thrown.expect(PSQLException.class);
        thrown.expectMessage(containsString("duplicate key"));

        assertFalse(customerDAO.insertCustomer(mike));
    }

    @Test
    public void testInsertMike_thereAreNoMike_onlyMikeWasInserted() throws SQLException {
        long nCustomersOld = customerDAO.count();
        assertTrue(customerDAO.insertCustomer(mike));
        assertNotNull(customerDAO.selectCustomer(mike.getEmail()));
        long nCustomersNew = customerDAO.count();
        assertEquals(nCustomersNew, nCustomersOld + 1);
    }

    @Test
    public void testInsertMike_thereAreNoMike_properMikeWasInserted() throws SQLException {
        customerDAO.insertCustomer(mike);
        Customer shouldBeMike = customerDAO.selectCustomer(mike.getEmail());
        assertNotNull(shouldBeMike);

        /**
         * Check if all fields of inserted Mike match properly
         * Customer equals() will consider only an email
         */
        assertEquals("Wrong email", mike.getEmail(), shouldBeMike.getEmail());
        assertEquals("Wrong password", mike.getPassword(), shouldBeMike.getPassword());
        assertEquals("Wrong name", mike.getName(), shouldBeMike.getName());
        assertEquals("Wrong surname", mike.getSurname(), shouldBeMike.getSurname());
        assertEquals("Wrong role", mike.getRole(), shouldBeMike.getRole());
        assertEquals("Wrong enabled", mike.isEnabled(), shouldBeMike.isEnabled());
    }

    @After
    public void tearDown() throws SQLException {
        assertTrue(customerDAO.deleteCustomer(mike.getEmail()));
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        customerDAO.closeConnection();
    }
}