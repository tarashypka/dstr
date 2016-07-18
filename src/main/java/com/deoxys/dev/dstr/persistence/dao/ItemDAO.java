package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.*;
import com.deoxys.dev.dstr.persistence.converter.ItemConverter;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class ItemDAO extends MongoDAO<Item> {

    private final static String COLLECTION = "items";

    public ItemDAO(MongoClient client) {
        super(client, COLLECTION, new ItemConverter());
    }

    @Override
    public boolean add(Item item) {
        DBObject doc = converter.toDocument(item);
        if (collection.insert(doc).wasAcknowledged()) {
            item.setId(doc.get("_id").toString());
            return true;
        }
        return false;
    }

    public Map<Item, Integer> getAllForCustomer(long id) {
        Map<Item, Integer> items = new HashMap<>();
        OrderDAO orderDAO = new OrderDAO(client);
        DBObject query = new BasicDBObject("customer.id", id);
        query.put("status", Order.OrderStatus.PROCESSED.getValue());
        DBCursor cursor = orderDAO.collection.find(query);
        while (cursor.hasNext()) {
            DBObject orderDoc = cursor.next();
            BasicDBList orderItemsDbl = (BasicDBList) orderDoc.get("items");
            for (Object itemObj : orderItemsDbl) {
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
        cursor.close();
        return items;
    }

    public ItemStatus getStatus(String id) {
        ObjectId _id = new ObjectId(id);
        DBObject query = new BasicDBObject("_id", _id);
        DBObject projection = new BasicDBObject("status", 1);
        DBObject doc = collection.findOne(query, projection);
        if (doc == null) return null;
        DBObject statusDoc = (DBObject) doc.get("status");
        int stocked = (Integer) statusDoc.get("stocked");
        int reserved = (Integer) statusDoc.get("reserved");
        int sold = (Integer) statusDoc.get("sold");
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
    public boolean changeSoldStatus(Item item, int amount) {
        DBObject query = new BasicDBObject("_id", new ObjectId(item.getId()));
        int newSoldVal = item.sold() + amount;
        DBObject update = new BasicDBObject("status.sold", newSoldVal);
        DBObject set = new BasicDBObject("$set", update);
        return collection.update(query, set).wasAcknowledged();
    }

    /**
     * Amount can be
     *      positive: item should be added to stock
     *      negative: item should be removed from stock
     */
    public boolean changeStockedStatus(Item item, int amount) {
        DBObject query = new BasicDBObject("_id", new ObjectId(item.getId()));
        int newStockedVal = item.stocked() + amount;
        DBObject update = new BasicDBObject("status.stocked", newStockedVal);
        DBObject set = new BasicDBObject("$set", update);
        return collection.update(query, set).wasAcknowledged();
    }

    /**
     * Amount can be
     *      positive: item should be added to reserve
     *      negative: item should be removed from reserve
     */
    public boolean changeReservedStatus(Item item, int amount) {
        DBObject query = new BasicDBObject("_id", new ObjectId(item.getId()));
        int newReservedVal = item.reserved() + amount;
        DBObject update = new BasicDBObject("status.reserved", newReservedVal);
        DBObject set = new BasicDBObject("$set", update);
        return collection.update(query, set).wasAcknowledged();
    }
}