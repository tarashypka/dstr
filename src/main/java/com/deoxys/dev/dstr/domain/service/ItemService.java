package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.persistence.dao.MongoItemDAO;

import java.util.Map;

/**
 * Created by deoxys on 07.07.16.
 */

public class ItemService extends MongoService {

    private MongoItemDAO itemDao;

    public ItemService() {
        super();
        itemDao = new MongoItemDAO(super.mongo);
    }

    public long count() {
        return itemDao.count();
    }

    /**
     * Order status was changed from PROCESSED.
     * Then this order items sold statuses should ne changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.sold -= orderItems.get(item)
     *
     * @param orderItems items that weren't actually sold
     * @return true if all orderItems sold statuses where updated
     */
    public boolean takeOrderItemsFromSold(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.takeFromSold(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Order status was changed to PROCESSED.
     * Then this order items sold statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.sold += orderItems.get(item)
     *
     * @param orderItems items that were sold
     * @return true if all orderItems sold statuses were updated
     */
    public boolean addOrderItemsToSold(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.addToSold(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * New order was made or old order status was changed from REJECTED.
     * Then this order items stocked statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.stocked -= orderItems.get(item)
     *
     * @param orderItems items that shouldn't be in stock
     * @return true if all orderItems stocked statuses where updated
     */
    public boolean takeOrderItemsFromStock(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.takeFromStock(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Order status was changed to REJECTED.
     * Then this order items stocked statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.stocked += orderItems.get(item)
     *
     * @param orderItems items that should be in stock
     * @return true if all orderItems stocked statuses were updated
     */
    public boolean addOrderItemsToStock(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.addToStock(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Order status was changed from IN_PROCESS.
     * Then this order items reserved statuses should ne changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.reserved -= orderItems.get(item)
     *
     * @param orderItems items that shouldn't be in reserve
     * @return true if all orderItems reserved statuses were updated
     */
    public boolean takeOrderItemsFromReserve(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.takeFromReserve(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Order status was changed to IN_PROCESS.
     * Then this order items reserved statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.reserved += orderItems.get(item)
     *
     * @param orderItems items that should be in reserve
     * @return true if all orderItems reserved statuses were updated
     */
    public boolean addOrderItemsToReserve(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            if (itemDao.addToReserve(item, orderItems.get(item))) {
                return false;
            }
        }
        return true;
    }
}
