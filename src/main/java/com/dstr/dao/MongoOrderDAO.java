package com.dstr.dao;

import com.mongodb.*;
import com.dstr.converter.OrderConverter;
import com.dstr.model.Order;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by deoxys on 27.05.16.
 */

/* Mongo 3.2 Java Driver proposes at least two approaches
    to complete basic collection queries:
    1. Using newly implemented (after 2.10.0) classes MongoClient, MongoCollection, Document, ...
    2. Using standard classes Mongo, DBCollection, DBObject, ...
   First approach does not support queries with projections yet,
   so second (deprecated) was used.
 */

public class MongoOrderDAO {

    private DBCollection collection;

    public final static String MONGO_DB = "dstr";
    public final static String MONGO_COLL = "orders";

    public MongoOrderDAO(MongoClient mongoClient) {
        DB db = mongoClient.getDB(MONGO_DB);
        db.setReadPreference(ReadPreference.secondaryPreferred());
        this.collection = db.getCollection(MONGO_COLL);
    }

    public Order insertOrder(Order order) {

        // Change today's orders total amount
        DBObject exists = new BasicDBObject("$exists", true);
        DBObject query = new BasicDBObject("nOrders", exists);
        DBObject nOrdersDoc = this.collection.findOne(query);

        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) nOrdersDoc.get("date"));
        int lastOrderYear = cal.get(Calendar.YEAR);
        int lastOrderYearDay = cal.get(Calendar.DAY_OF_YEAR);

        cal.setTime(new Date());
        int currYear = cal.get(Calendar.YEAR);
        int currYearDay = cal.get(Calendar.DAY_OF_YEAR);
        int currMonth = cal.get(Calendar.MONTH);
        int currMonthDay = cal.get(Calendar.DAY_OF_MONTH);

        int nOrders = 1;
        if (lastOrderYear == currYear && lastOrderYearDay == currYearDay) {
            nOrders = (Integer) nOrdersDoc.get("nOrders") + 1;
        }
        nOrdersDoc.put("date", new Date());
        nOrdersDoc.put("nOrders", nOrders);

        if ( ! this.collection.save(nOrdersDoc).isUpdateOfExisting()) {
            return null;
        }

        order.setOrderNumber(currYear + "-" + currMonth + "-" +
                currMonthDay + "-" + nOrders);

        DBObject orderDoc = OrderConverter.toDocument(order);

        // Insert new order
        // It's important that nOrders document is always updated before,
        // so whenever new document was created, nOrders status will be also changed
        if (this.collection.insert(orderDoc).wasAcknowledged()) {
            order.setId(orderDoc.get("_id").toString());
            return order;
        }
        return null;
    }

    public Order findOrder(ObjectId _orderId) {
        DBObject query = new BasicDBObject("_id", _orderId);
        DBObject orderDoc = this.collection.findOne(query);

        if (orderDoc == null) {
            return null;
        }
        return OrderConverter.toOrder(orderDoc);
    }

    public Order.OrderStatus findOrderStatus(Object _id) {
        DBObject query = new BasicDBObject("_id", _id);
        DBObject projection = new BasicDBObject("status", 1);

        DBObject doc = this.collection.findOne(query, projection);

        int statusVal = (Integer) doc.get("status");

        return Order.OrderStatus.orderStatusbyValue(statusVal);
    }

    public boolean updateOrderStatus(ObjectId _orderId, Order.OrderStatus newStatus) {

        // Get current status
        Order.OrderStatus oldStatus = findOrderStatus(_orderId);

        if (newStatus.equals(oldStatus)) {
            return false;
        }

        // If without $set operator,
        // than order document will be not updated, but replaced
        DBObject query = new BasicDBObject("_id", _orderId);
        DBObject update = new BasicDBObject("status", newStatus.getValue());
        DBObject set = new BasicDBObject("$set", update);

        return this.collection.update(query, set).wasAcknowledged();
    }

    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();

        DBObject exists = new BasicDBObject("$exists", true);
        DBObject query = new BasicDBObject("orderNumber", exists);
        DBCursor cursor = this.collection.find(query);

        while (cursor.hasNext()) {
            DBObject orderDoc = cursor.next();
            if ( ! orderDoc.containsField("nOrders")) {
                orders.add(OrderConverter.toOrder(orderDoc));
            }
        }
        cursor.close();

        return orders;
    }

    public List<Order> findCustomerOrders(String email) {
        List<Order> orders = new ArrayList<>();

        DBObject query = new BasicDBObject("customer.email", email);
        DBCursor cursor = this.collection.find(query);

        while (cursor.hasNext()) {
            DBObject orderDoc = cursor.next();
            orders.add(OrderConverter.toOrder(orderDoc));
        }
        cursor.close();

        return orders;
    }

    public Map<String, Integer> findCustomerItems(String email) {
        Map<String, Integer> customerItems = new HashMap<>();

        DBObject query = new BasicDBObject("customer.email", email);
        DBCursor cursor = this.collection.find(query);

        while (cursor.hasNext()) {
            DBObject orderDoc = cursor.next();
            BasicDBList orderItemsDbl = (BasicDBList) orderDoc.get("items");

            for (Object orderItemObj : orderItemsDbl) {
                DBObject orderItemDoc = (DBObject) orderItemObj;
                DBRef orderItemDbRef = (DBRef) orderItemDoc.get("id");
                String orderItemId = orderItemDbRef.getId().toString();
                Integer quantity = (Integer) orderItemDoc.get("quantity");
                customerItems.put(orderItemId, quantity);
            }
        }
        cursor.close();

        return customerItems;
    }

    // Should be reimplemented
    public int ordersAmount(String email) {
        return findCustomerOrders(email).size();
    }

    // Should be reimplemented
    public int itemsAmount(String email) {
        return findCustomerItems(email).size();
    }
}