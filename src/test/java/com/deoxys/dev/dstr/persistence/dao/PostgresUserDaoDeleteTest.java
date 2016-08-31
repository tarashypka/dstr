package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.persistence.dao.postgres.UserDAO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public final class PostgresUserDaoDeleteTest extends PostgresTestDataSource {

    private static UserDAO userDAO;
    private User mike = new User.UserBuilder("mike@gmail.com")
            .withPassword("1234").build();

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAO(PostgresTestDataSource.getDataSource());
    }

    @Before
    public void setUp() throws Exception {
        assertTrue(userDAO.add(mike));
    }

    @Test
    public void testDeleteMike_thereAreNoMike_methodReturnsFalse() throws SQLException {
        long id = mike.getId();
        assertTrue(userDAO.delete(id));
        assertFalse(userDAO.delete(id));
    }

    @Test
    public void testDeleteMike_thereIsMike_onlyMikeWasDeleted() throws SQLException {
        long nCustomersOld = userDAO.count();
        assertTrue(userDAO.delete(mike.getId()));
        long nCustomersNew = userDAO.count();
        assertEquals(nCustomersNew, nCustomersOld - 1);
    }
}