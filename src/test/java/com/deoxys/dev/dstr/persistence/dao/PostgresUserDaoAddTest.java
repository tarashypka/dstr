package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.persistence.dao.postgres.UserDAO;
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

    private static UserDAO userDAO;
    private User mike = new User.UserBuilder("mike@gmail.com")
            .withPassword("1234").build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAO(PostgresTestDataSource.getDataSource());
    }

    @Test
    public void testAdd_thereIsMike_duplicateExceptionThrown() throws SQLException {
        assertTrue(userDAO.add(mike));
        thrown.expect(PSQLException.class);
        thrown.expectMessage(containsString("duplicate key"));
        assertFalse(userDAO.add(mike));
    }

    @Test
    public void testAdd_thereAreNoMike_onlyMikeWasInserted() throws SQLException {
        long nCustomersOld = userDAO.count();
        assertTrue(userDAO.add(mike));
        assertNotNull(userDAO.get(mike.getId()));
        long nCustomersNew = userDAO.count();
        assertEquals(nCustomersNew, nCustomersOld + 1);
    }

    @Test
    public void testAdd_thereAreNoMike_properMikeWasInserted() throws SQLException {
        assertTrue(userDAO.add(mike));
        User shouldBeMike = userDAO.get(mike.getId());
        assertNotNull(shouldBeMike);

        /**
         * Check if all fields of inserted Mike match properly
         * Customer equals() will consider only an email
         */
        assertEquals("Wrong email", shouldBeMike.getEmail(), mike.getEmail());
        assertEquals("Wrong password", shouldBeMike.getPassword(), mike.getPassword());
        assertEquals("Wrong role", shouldBeMike.getRole(), "CUSTOMER");
        assertEquals("Wrong enabled", shouldBeMike.isEnabled(), true);
    }

    @After
    public void tearDown() throws SQLException {
        assertTrue(userDAO.delete(mike.getId()));
    }
}