package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public final class PostgresUserDaoDeleteTest extends PostgresTestDataSource {

    private static CustomerDAO customerDAO;
    private User mike = new User.UserBuilder("mike@gmail.com")
            .withPassword("1234").withName("Mike", "Mort").build();

    @BeforeClass
    public static void setUpClass() {
        customerDAO = new CustomerDAO(PostgresTestDataSource.getDataSource());
    }

    @Before
    public void setUp() throws Exception {
        assertTrue(customerDAO.add(mike));
    }

    @Test
    public void testDeleteMike_thereAreNoMike_methodReturnsFalse() throws SQLException {
        long id = mike.getId();
        assertTrue(customerDAO.delete(id));
        assertFalse(customerDAO.delete(id));
    }

    @Test
    public void testDeleteMike_thereIsMike_onlyMikeWasDeleted() throws SQLException {
        long nCustomersOld = customerDAO.count();
        assertTrue(customerDAO.delete(mike.getId()));
        long nCustomersNew = customerDAO.count();
        assertEquals(nCustomersNew, nCustomersOld - 1);
    }
}