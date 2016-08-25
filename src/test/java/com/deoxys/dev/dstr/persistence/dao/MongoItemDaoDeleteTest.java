package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.deoxys.dev.dstr.domain.model.Price;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Currency;

import static org.junit.Assert.*;

public class MongoItemDaoDeleteTest {

    private static ItemDAO itemDao;
    private Item watch = new Item.ItemBuilder("watch")
            .withPrice(new Price(300D, Currency.getInstance("USD")))
            .withStatus(new ItemStatus(10, 10, 10)).build();

    @BeforeClass
    public static void setUpClass() {
        itemDao = new ItemDAO(MongoTestClient.getMongo());
    }

    @Before
    public void setUp() throws Exception {
        itemDao.add(watch);
    }

    @Test
    public void testRemoveWatch_thereIsWatch_onlyWatchWasDeleted() throws Exception {
        long nItemsOld = itemDao.count();
        itemDao.delete(watch.getId());
        assertNull(itemDao.get(watch.getId()));
        long nItemsNew = itemDao.count();
        assertEquals(nItemsNew, nItemsOld - 1);
    }
}