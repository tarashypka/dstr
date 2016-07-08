package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Currency;

import static org.junit.Assert.*;

public class MongoItemDaoRemoveItemTest {

    private static MongoItemDAO itemDao;
    private Item watch = new Item("watch", 300,
            Currency.getInstance("USD"), new ItemStatus(10, 10, 10));

    @BeforeClass
    public static void setUpClass() {
        itemDao = new MongoItemDAO(MongoTestClient.getMongo());
    }

    @Before
    public void setUp() throws Exception {
        assertNotNull(itemDao.insertItem(watch));
    }

    @Test
    public void testRemoveWatch_thereIsWatch_methodReturnsTrue() throws Exception {
        assertTrue(itemDao.removeItem(new ObjectId(watch.getId())));
    }

    @Test
    public void testRemoveWatch_thereIsWatch_onlyWatchWasDeleted() throws Exception {
        long nItemsOld = itemDao.count();
        assertTrue(itemDao.removeItem(new ObjectId(watch.getId())));
        assertNull(itemDao.findItem(new ObjectId(watch.getId())));
        long nItemsNew = itemDao.count();
        assertEquals(nItemsNew, nItemsOld - 1);
    }
}