package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.item.Item;
import com.deoxys.dev.dstr.domain.model.item.ItemStatus;
import com.deoxys.dev.dstr.domain.model.item.Price;
import com.deoxys.dev.dstr.persistence.dao.mongo.ItemDAO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MongoItemDaoAddTest {

    private static ItemDAO itemDao;
    private Item watch = new Item.ItemBuilder("watch")
            .withPrice(new Price(300D, Currency.getInstance("USD")))
            .withStatus(new ItemStatus(10, 10, 10)).build();

    @BeforeClass
    public static void setUpClass() {
        itemDao = new ItemDAO(MongoTestClient.getMongo());
    }

    @Test
    public void testInsertWatch_onlyWatchWasInserted() {
        long nItemsOld = itemDao.count();
        itemDao.add(watch);
        long nItemsNew = itemDao.count();
        assertEquals(nItemsNew, nItemsOld + 1);
    }

    @Test
    public void testInsertWatch_properWatchWasInserted() {
        itemDao.add(watch);
        Item shouldBeWatch = itemDao.get(watch.getId());
        assertNotNull(shouldBeWatch);

        /**
         * Check if all fields of inserted watch match properly
         * Item equals() will consider only an id
         */
        assertEquals("Wrong id", watch.getId(), shouldBeWatch.getId());
        assertEquals("Wrong category", watch.getName(), shouldBeWatch.getName());
        assertEquals("Wrong price", watch.getPrice(), shouldBeWatch.getPrice());
        assertEquals("Wrong status", watch.getStatus(), shouldBeWatch.getStatus());
    }

    @After
    public void tearDown() {
        itemDao.delete(watch.getId());
    }
}