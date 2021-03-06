package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.ItemReader;
import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.domain.model.item.Item;
import com.deoxys.dev.dstr.persistence.dao.mongo.ItemDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        itemDao.add(item);
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
        User user = (User) req.getSession().getAttribute("customer");
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
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
    public void takeOrderItemsFromSold(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            takeOrderItemFromSold(item, orderItems.get(item));
    }

    public void takeOrderItemFromSold(Item item, int quantity) {
        itemDao.changeSoldStatus(item, -quantity);
        item.getStatus().changeSold(-quantity);
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
    public void addOrderItemsToSold(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            addOrderItemToSold(item, orderItems.get(item));
    }

    public void addOrderItemToSold(Item item, int quantity) {
        itemDao.changeSoldStatus(item, quantity);
        item.getStatus().changeSold(quantity);
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
    public void takeOrderItemsFromStock(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            takeOrderItemFromStock(item, orderItems.get(item));
    }

    public void takeOrderItemFromStock(Item item, int quantity) {
        itemDao.changeStockedStatus(item, -quantity);
        item.getStatus().changeStocked(-quantity);
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
    public void addOrderItemsToStock(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            addOrderItemToStock(item, orderItems.get(item));
    }

    public void addOrderItemToStock(Item item, int quantity) {
        itemDao.changeStockedStatus(item, quantity);
        item.getStatus().changeStocked(quantity);
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
    public void takeOrderItemsFromReserve(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            takeOrderItemFromReserve(item, orderItems.get(item));
    }

    public void takeOrderItemFromReserve(Item item, int quantity) {
        itemDao.changeReservedStatus(item, -quantity);
        item.getStatus().changeReserved(-quantity);
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
    public void addOrderItemsToReserve(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet())
            addOrderItemToReserve(item, orderItems.get(item));
    }

    public void addOrderItemToReserve(Item item, int quantity) {
        itemDao.changeReservedStatus(item, quantity);
        item.getStatus().changeReserved(quantity);
    }
}
