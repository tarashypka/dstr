package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.ItemReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by deoxys on 07.07.16.
 */

public class ItemService extends MongoService<Item> {
    Logger logger = Logger.getLogger(ItemService.class);

    private ItemDAO itemDao;

    public ItemService() {
        super(new ItemReader());
        itemDao = new ItemDAO(mongo);
    }

    public long count() {
        return itemDao.count();
    }

    public void loadItem(HttpServletRequest req) {
        String id = req.getParameter("id");
        if (id == null || id.equals("")) req.setAttribute("error", "Wrong item id");
        else req.setAttribute("item", itemDao.get(id));
    }

    public void loadItems(HttpServletRequest req) {
        req.setAttribute("items", itemDao.getAll());
    }

    public void addItem(HttpServletRequest req) {
        Item item = requestReader.read(req);
        if (req.getAttribute("error") == null) itemDao.add(item);
        req.setAttribute("item", item);
    }

    public void editItem(HttpServletRequest req) {
        Item item = requestReader.read(req);
        itemDao.update(item);
        req.setAttribute("item", item);
    }

    public void deleteItem(HttpServletRequest req) {
        String id = req.getParameter("id");
        itemDao.delete(id);
    }

    public void loadCustomerItems(HttpServletRequest req) {
        Customer customer = (Customer) req.getSession().getAttribute("customer");
        long id = customer.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : customer.getId();
        req.setAttribute("items", itemDao.getAllForCustomer(id));
    }

    /**
     * Order status was changed from PROCESSED.
     * Then this order items sold statuses should ne changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.sold -= orderItems.get(item)
     *
     * @param orderItems items that weren't actually sold
     */
    void takeOrderItemsFromSold(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeSoldStatus(item, - orderItems.get(item));
    }

    /**
     * Order status was changed to PROCESSED.
     * Then this order items sold statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.sold += orderItems.get(item)
     *
     * @param orderItems items that were sold
     */
    void addOrderItemsToSold(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeSoldStatus(item, + orderItems.get(item));
    }

    /**
     * New order was made or old order status was changed from REJECTED.
     * Then this order items stocked statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.stocked -= orderItems.get(item)
     *
     * @param orderItems items that shouldn't be in stock
     */
    void takeOrderItemsFromStock(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeStockedStatus(item, - orderItems.get(item));
    }

    /**
     * Order status was changed to REJECTED.
     * Then this order items stocked statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.stocked += orderItems.get(item)
     *
     * @param orderItems items that should be in stock
     */
    void addOrderItemsToStock(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeStockedStatus(item, + orderItems.get(item));
    }

    /**
     * Order status was changed from IN_PROCESS.
     * Then this order items reserved statuses should ne changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.reserved -= orderItems.get(item)
     *
     * @param orderItems items that shouldn't be in reserve
     */
    void takeOrderItemsFromReserve(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeReservedStatus(item, -orderItems.get(item));
    }

    /**
     * Order status was changed to IN_PROCESS.
     * Then this order items reserved statuses should be changed.
     *
     * Method updates each item in orderItems, such as
     *      item.status.reserved += orderItems.get(item)
     *
     * @param orderItems items that should be in reserve
     */
    void addOrderItemsToReserve(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            itemDao.changeReservedStatus(item, +orderItems.get(item));
    }
}
