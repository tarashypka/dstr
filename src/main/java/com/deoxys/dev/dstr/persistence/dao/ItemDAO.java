package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.model.OrderStatus;
import com.deoxys.dev.dstr.persistence.converter.ItemConverter;
import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Projections.*;

public class ItemDAO extends MongoDAO<Item> {

    private final static String COLLECTION = "items";

    public ItemDAO(MongoClient client) {
        super(client, COLLECTION, new ItemConverter());
    }

    @Override
    public void add(Item item) {
        Document doc = converter.toDocument(item);
        collection.insertOne(doc);
        item.setId(doc.get("_id").toString());
    }

    public Map<Item, Integer> getAllForCustomer(long id) {
        Map<Item, Integer> items = new HashMap<>();

        OrderDAO orderDAO = new OrderDAO(client);
        BsonDocument filter = new BsonDocument("customer.id", new BsonInt64(id));
        filter.put("status", new BsonInt32(OrderStatus.PROCESSED.getValue()));
        try (MongoCursor<Document> cursor = orderDAO.collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document orderDoc = cursor.next();
                BasicDBList orderItems = (BasicDBList) orderDoc.get("items");
                for (Object itemObj : orderItems) {
                    DBObject itemDoc = (DBObject) itemObj;
                    DBRef itemDbRef = (DBRef) itemDoc.get("id");
                    ObjectId itemId = (ObjectId) itemDbRef.getId();
                    Item item = get(itemId.toString());
                    Integer quantity = (Integer) itemDoc.get("quantity");

                    if (items.containsKey(item)) {
                        int newQuantity = items.get(item) + quantity;
                        items.put(item, newQuantity);
                    } else {
                        items.put(item, quantity);
                    }
                }
            }
        }
        return items;
    }

    private ItemStatus getStatus(String id) {
        ObjectId _id = new ObjectId(id);
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson fields = fields(include("status"), excludeId());
        Document doc = collection.find(filter).projection(fields).first();
        if (doc == null) return null;
        Document statusDoc = (Document) doc.get("status");
        Integer stocked = (Integer) statusDoc.get("stocked");
        Integer reserved = (Integer) statusDoc.get("reserved");
        Integer sold = (Integer) statusDoc.get("sold");
        return new ItemStatus(stocked, reserved, sold);
    }

    public boolean enoughItems(Map<Item, Integer> orderItems) {
        for (Item item : orderItems.keySet()) {
            int quantity = orderItems.get(item);
            ItemStatus status = getStatus(item.getId());
            if (status == null || quantity > status.getStocked()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void expandOrderItems(Order order) {
        Map<Item, Integer> items = order.getItems();
        Map<Item, Integer> expandedItems = new HashMap();
        for (Item item : items.keySet())
            expandedItems.put(get(item.getId()), items.get(item));
        order.setItems(expandedItems);
    }

    /**
     * Amount can be
     *      positive: items should be added to sold
     *      negative: item should be removed from sold
     */
    public void changeSoldStatus(Item item, int amount) {
        int sold = item.sold() + amount;
        ObjectId _id = new ObjectId(item.getId());

        // Without $set operator, item document will be not updated, but replaced
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson set  = new BsonDocument("status.sold", new BsonInt32(sold));
        Bson update = new BasicDBObject("$set", set);
        collection.updateOne(filter, update);
    }

    /**
     * Amount can be
     *      positive: item should be added to stock
     *      negative: item should be removed from stock
     */
    public void changeStockedStatus(Item item, int amount) {
        int stocked = item.stocked() + amount;
        ObjectId _id = new ObjectId(item.getId());

        // Without $set operator, item document will be not updated, but replaced
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson set  = new BsonDocument("status.stocked", new BsonInt32(stocked));
        Bson update = new BasicDBObject("$set", set);
        collection.updateOne(filter, update);
    }

    /**
     * Amount can be
     *      positive: item should be added to reserve
     *      negative: item should be removed from reserve
     */
    public void changeReservedStatus(Item item, int amount) {
        int reserved = item.reserved() + amount;
        ObjectId _id = new ObjectId(item.getId());

        // Without $set operator, item document will be not updated, but replaced
        Bson filter = new BsonDocument("_id", new BsonObjectId(_id));
        Bson set  = new BsonDocument("status.reserved", new BsonInt32(reserved));
        Bson update = new BasicDBObject("$set", set);
        collection.updateOne(filter, update);
    }
}