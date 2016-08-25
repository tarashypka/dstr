package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.User;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public final class PostgresUserDaoAddTest {

    private static CustomerDAO customerDAO;
    private User mike = new User.UserBuilder("mike@gmail.com")
            .withPassword("1234").withName("Mike", "Mort").build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        customerDAO = new CustomerDAO(PostgresTestDataSource.getDataSource());
    }

    @Test
    public void testAdd_thereIsMike_duplicateExceptionThrown() throws SQLException {
        assertTrue(customerDAO.add(mike));
        thrown.expect(PSQLException.class);
        thrown.expectMessage(containsString("duplicate key"));
        assertFalse(customerDAO.add(mike));
    }

    @Test
    public void testAdd_thereAreNoMike_onlyMikeWasInserted() throws SQLException {
        long nCustomersOld = customerDAO.count();
        assertTrue(customerDAO.add(mike));
        assertNotNull(customerDAO.get(mike.getId()));
        long nCustomersNew = customerDAO.count();
        assertEquals(nCustomersNew, nCustomersOld + 1);
    }

    @Test
    public void testAdd_thereAreNoMike_properMikeWasInserted() throws SQLException {
        assertTrue(customerDAO.add(mike));
        User shouldBeMike = customerDAO.get(mike.getId());
        assertNotNull(shouldBeMike);

        /**
         * Check if all fields of inserted Mike match properly
         * Customer equals() will consider only an email
         */
        assertEquals("Wrong email", shouldBeMike.getEmail(), mike.getEmail());
        assertEquals("Wrong password", shouldBeMike.getPassword(), mike.getPassword());
        assertEquals("Wrong name", shouldBeMike.getName(), mike.getName());
        assertEquals("Wrong surname", shouldBeMike.getSurname(), mike.getSurname());
        assertEquals("Wrong role", shouldBeMike.getRole(), "customer");
        assertEquals("Wrong enabled", shouldBeMike.isEnabled(), true);
    }

    @After
    public void tearDown() throws SQLException {
        assertTrue(customerDAO.delete(mike.getId()));
    }
}