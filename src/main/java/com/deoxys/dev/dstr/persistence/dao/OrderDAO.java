package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.persistence.converter.OrderConverter;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.*;

public class OrderDAO extends MongoDAO<Order> {

    private final static String COLLECTION = "orders";

    public OrderDAO(MongoClient client) {
        super(client, COLLECTION, new OrderConverter());
    }

    @Override
    public boolean add(Order order) {
        ItemDAO itemDAO = new ItemDAO(client);

        /**
         * Verify if there are enough items.
         * Another order with required items could have been made,
         * before this order was acknowledged by customer.
         *
         * This could be reimplemented according to business politics:
         * Sometimes it's better to have items in reserve
         * and boost performance by avoiding additional issues covered here.
         */
        if ( ! itemDAO.enoughItems(order.getItems())) return false;

        // Move ordered items to reserved
        Map<Item, Integer> orderItems = order.getItems();
        for (Item item : orderItems.keySet()) {
            int amount = orderItems.get(item);
            itemDAO.changeStockedStatus(item, - amount);
            itemDAO.changedReservedStatus(item, + amount);
        }

        DBObject doc = converter.toDocument(order);
        if (collection.insert(doc).wasAcknowledged()) {
            order.setId(doc.get("_id").toString());
            return true;
        }
        return false;
    }

    public List<Order> getAllForCustomer(String email) {
        List<Order> orders = new ArrayList<>();
        DBObject query = new BasicDBObject("customer.email", email);
        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            orders.add(converter.toObject(doc));
        }
        cursor.close();
        return orders;
    }

    public Order.OrderStatus getStatus(String id) {
        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBObject projection = new BasicDBObject("status", 1);
        DBObject doc = collection.findOne(query, projection);
        int statusVal = (Integer) doc.get("status");
        return Order.OrderStatus.getStatus(statusVal);
    }

    public boolean updateStatus(String id, Order.OrderStatus newStatus) {
        Order.OrderStatus oldStatus = getStatus(id);
        if (newStatus.equals(oldStatus)) return false;

        // Without $set operator, order document will be not updated, but replaced
        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBObject update = new BasicDBObject("status", newStatus.getValue());
        DBObject set = new BasicDBObject("$set", update);
        return collection.update(query, set).wasAcknowledged();
    }
}