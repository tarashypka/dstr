package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.model.OrderStatus;
import com.deoxys.dev.dstr.persistence.converter.OrderConverter;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

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
    public void add(Order order) {
        ItemDAO itemDAO = new ItemDAO(client);

        // Move ordered items to reserved
        Map<Item, Integer> orderItems = order.getItems();
        for (Item item : orderItems.keySet()) {
            int amount = orderItems.get(item);
            itemDAO.changeStockedStatus(item, -amount);
            itemDAO.changeReservedStatus(item, amount);
        }
        Document doc = converter.toDocument(order);
        collection.insertOne(doc);
        order.setId(doc.get("_id").toString());
    }

    public List<Order> getAllForCustomer(long id) {
        List<Order> orders = new ArrayList<>();
        Bson filter = eq("customer.id", id);
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext())
                orders.add(converter.toObject(cursor.next()));
        }
        return orders;
    }

    public List<Order> getAllInPeriod(Date from, Date till) {
        List<Order> orders = new ArrayList<>();

        Bson filter = and(gte("date", from), lte("date", till));
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext())
                orders.add(converter.toObject(cursor.next()));
        }
        return orders;
    }

    public int countCustomerItems(long id) {
        int nItems = 0;
        BsonDocument filter = new BsonDocument("customer.id", new BsonInt64(id));
        filter.put("status", new BsonInt32(OrderStatus.PROCESSED.getValue()));
        try (MongoCursor<Document> cursor = collection.mapReduce(
                N_ITEMS_MAP_FUNC, N_ITEMS_REDUCE_FUNC).filter(filter).iterator()) {

            if (cursor.hasNext()) {
                String value = cursor.next().get("value").toString();
                nItems = (int) Double.parseDouble(value);
            }
        }
        return nItems;
    }

    public int countCustomerOrders(long id) {
        Bson filter = new BsonDocument("customer.id", new BsonInt64(id));
        return (int) collection.count(filter);
    }

    public OrderStatus getStatus(String id) {
        ObjectId _id = new ObjectId(id);
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson fields = fields(include("status"), excludeId());
        Document doc = collection.find(filter).projection(fields).first();
        int statusVal = (Integer) doc.get("status");
        return OrderStatus.getStatus(statusVal);
    }

    // Without $set operator, order document will be not updated, but replaced
    public void updateStatus(String id, OrderStatus newStatus) {
        ObjectId _id = new ObjectId(id);
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson set  = new BsonDocument("status", new BsonInt32(newStatus.getValue()));
        Bson update = new BasicDBObject("$set", set);
        collection.updateOne(filter, update);
    }
}