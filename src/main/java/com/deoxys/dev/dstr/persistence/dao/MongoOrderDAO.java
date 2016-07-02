package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.persistence.converter.OrderConverter;
import com.deoxys.dev.dstr.domain.Item;
import com.deoxys.dev.dstr.domain.Order;
import com.mongodb.*;
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
    private MongoClient client;
    private DBCollection collection;

    public final static String MONGO_DB = "dstr";
    public final static String MONGO_COLL = "orders";

    public MongoOrderDAO(MongoClient mongoClient) {
        client = mongoClient;
        DB db = client.getDB(MONGO_DB);
        db.setReadPreference(ReadPreference.secondaryPreferred());
        this.collection = db.getCollection(MONGO_COLL);
    }

    public Order insertOrder(Order order) {
        MongoItemDAO itemDAO = new MongoItemDAO(client);

        // Verify if there are enough items
        // Another order with required items could have been made,
        // before this order was acknowledged by customer
        // This could be reimplemented according to magazine's politics:
        // Sometimes it's better to have items in reserve
        // and boost performance by avoiding additional issues covered here
        if ( ! itemDAO.enoughItems(order.getItems())) {
            return null;
        }

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

        // Move ordered items to reserved
        if ( ! itemDAO.moveStockedToReserved(order.getItems())) {
            return null;
        }

        DBObject orderDoc = OrderConverter.toDocument(order);

        // It's important that nOrders and ordered items documents
        // are always updated before, so whenever new document was created,
        // nOrders and ordered items statuses will be also changed
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

        return Order.OrderStatus.getStatus(statusVal);
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

    public Map<Item, Integer> findCustomerItems(String email) {
        Map<Item, Integer> customerItems = new HashMap<>();

        DBObject query = new BasicDBObject("customer.email", email);
        query.put("status", Order.OrderStatus.PROCESSED.getValue());
        DBCursor cursor = this.collection.find(query);

        MongoItemDAO itemDAO = new MongoItemDAO(client);

        while (cursor.hasNext()) {
            DBObject orderDoc = cursor.next();
            BasicDBList orderItemsDbl = (BasicDBList) orderDoc.get("items");

            for (Object orderItemObj : orderItemsDbl) {
                DBObject orderItemDoc = (DBObject) orderItemObj;
                DBRef orderItemDbRef = (DBRef) orderItemDoc.get("id");
                ObjectId orderItemId = (ObjectId) orderItemDbRef.getId();
                Item orderItem = itemDAO.findItem(orderItemId);
                Integer quantity = (Integer) orderItemDoc.get("quantity");

                if (customerItems.containsKey(orderItem)) {
                    int newQuantity = customerItems.get(orderItem) + quantity;
                    customerItems.put(orderItem, newQuantity);
                } else {
                    customerItems.put(orderItem, quantity);
                }
            }
        }
        cursor.close();

        return customerItems;
    }
}