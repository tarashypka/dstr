package com.dstr.dao;

import com.dstr.model.Item;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import com.dstr.converter.OrderConverter;
import com.dstr.model.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by deoxys on 27.05.16.
 */

public class MongoOrderDAO {
    private MongoClient mongoClient;
    private MongoCollection<Document> mongoColl;
    public final static String MONGO_DB = "dstr";
    public final static String MONGO_COLL = "orders";

    public MongoOrderDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mongoColl = mongoClient.getDatabase(MONGO_DB).getCollection(MONGO_COLL);
    }

    public Order createOrder(Order order) {
        Document doc = OrderConverter.toDocument(order);
        this.mongoColl.insertOne(doc);
        order.setId(doc.get("_id").toString());
        return order;
    }

    public int updateOrder(Order order) {
        Document doc = OrderConverter.toDocument(order);
        UpdateResult updRes = this.mongoColl.updateOne(
                eq("_id", new ObjectId(order.getId())), new Document("$set", doc));
        return (int) updRes.getModifiedCount();
    }

    public Order findOrder(ObjectId _id) {
        Document doc = this.mongoColl.
                withReadPreference(ReadPreference.secondaryPreferred()).
                find(eq("_id", _id)).first();
        return OrderConverter.toOrder(doc);
    }

    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        MongoCursor<Document> cursor = this.mongoColl.
                withReadPreference(ReadPreference.secondaryPreferred()).
                find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                orders.add(OrderConverter.toOrder(doc));
            }
        } finally {
            cursor.close();
        }
        return orders;
    }

    public List<Order> findCustomerOrders(String email) {
        List<Order> orders = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("customer.email", email);

        MongoCursor<Document> cursor = this.mongoColl.
                withReadPreference(ReadPreference.secondaryPreferred()).
                find(query).iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                orders.add(OrderConverter.toOrder(doc));
            }
        } finally {
            cursor.close();
        }
        return orders;
    }

    public Map<Item, Integer> findCustomerItems(String email) {
        Map<Item, Integer> items = new HashMap<>();

        BasicDBObject query = new BasicDBObject("customer.email", email);

        MongoCursor<Document> cursor = this.mongoColl.
                withReadPreference(ReadPreference.secondaryPreferred()).
                find(query).iterator();
        try {
            MongoItemDAO itemDAO = new MongoItemDAO(mongoClient);

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                List itemsDbl = (ArrayList) doc.get("items");

                for (Object el : itemsDbl) {
                    Document itemRef = (Document) el;
                    DBRef dbRef = (DBRef) itemRef.get("id");
                    ObjectId _id = (ObjectId) dbRef.getId();
                    Item item = itemDAO.findItem(_id);
                    items.put(item, itemRef.getInteger("quantity"));
                }
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    // Should be reimplemented
    public int ordersAmount(String email) {
        return findCustomerOrders(email).size();
    }

    // Should be reimplemented
    public int itemsAmount(String email) {
        return findCustomerItems(email).size();
    }

    // It seems MongoCollection find method has no projection method
    // to decrease network load:
    // api.mongodb.com/java/current/com/mongodb/client/MongoCollection.html
    public int itemSold(ObjectId _id) {
        int amount = 0;

        Document query = new Document("items.id", new DBRef("items", _id));
        MongoCursor<Document> cursor = this.mongoColl.
                withReadPreference(ReadPreference.secondaryPreferred()).
                find(query).iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                List<Document> itemsDocs = (ArrayList) doc.get("items");
                for (Document itemDoc : itemsDocs) {
                    DBRef itemRef = (DBRef) itemDoc.get("id");
                    ObjectId itemId = (ObjectId) itemRef.getId();
                    if (itemId.equals(_id)) {
                        amount += itemDoc.getInteger("quantity");
                    }
                }
            }
        } finally {
            cursor.close();
        }
        return amount;
    }

    public int changeOrderStatus(ObjectId _id, Order.OrderStatus status) {

        UpdateResult updRes = this.mongoColl.updateOne(
                eq("_id", _id),
                new Document("$set", new Document("status", status.getValue()))
        );
        return (int) updRes.getModifiedCount();
    }
}