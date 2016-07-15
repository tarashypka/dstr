package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.persistence.converter.OrderConverter;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.*;

public class OrderDAO extends MongoDAO<Order> {

    private final static String COLLECTION;

    private final static String N_ITEMS_MAP_FUNC, N_ITEMS_REDUCE_FUNC;

    public OrderDAO(MongoClient client) {
        super(client, COLLECTION, new OrderConverter());
    }

    static {
        COLLECTION = "orders";
        N_ITEMS_MAP_FUNC =
            "function() {" +
            "   var nOrderItems = 0;" +
            "   this.items.forEach(function(item) { nOrderItems += item.quantity });" +
            "   emit (\"nItems\", nOrderItems);" +
            "}";
        N_ITEMS_REDUCE_FUNC =
            "function(key, values) {" +
            "   var nCustomerItems = 0;" +
            "   values.forEach(function(nOrderItems) { nCustomerItems += nOrderItems });" +
            "   return nCustomerItems;" +
            "}";
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
            itemDAO.changeReservedStatus(item, +amount);
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

    public int countCustomerItems(String email) {
        int nItems = 0;
        DBObject query = new BasicDBObject("customer.email", email);
        query.put("status", Order.OrderStatus.PROCESSED.getValue());
        MapReduceOutput out = collection.mapReduce(
                N_ITEMS_MAP_FUNC, N_ITEMS_REDUCE_FUNC,
                MapReduceCommand.OutputType.INLINE.name(), query);
        Iterator<DBObject> iterator = out.results().iterator();
        if (iterator.hasNext()) {
            String value = iterator.next().get("value").toString();
            nItems = (int) Double.parseDouble(value);
        }
        out.drop();
        return nItems;
    }

    public int countCustomerOrders(String email) {
        DBObject query = new BasicDBObject("customer.email", email);
        return (int) collection.count(query);
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