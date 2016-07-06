package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.mongodb.*;
import com.deoxys.dev.dstr.persistence.converter.ItemConverter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class MongoItemDao {
    private DBCollection collection;

    private final static String MONGO_DB = "dstr";
    private final static String MONGO_COLL = "items";

    public MongoItemDao(MongoClient mongoClient) {
        DB db = mongoClient.getDB(MONGO_DB);
        db.setReadPreference(ReadPreference.secondaryPreferred());
        this.collection = db.getCollection(MONGO_COLL);
    }

    public Item insertItem(Item item) {
        DBObject itemDoc = ItemConverter.toDocument(item);

        if (this.collection.insert(itemDoc).wasAcknowledged()) {
            item.setId(itemDoc.get("_id").toString());
            return item;
        }
        return null;
    }

    public boolean updateItem(Item item) {
        DBObject itemDoc = ItemConverter.toDocument(item);
        return this.collection.save(itemDoc).wasAcknowledged();
    }

    public boolean removeItem(ObjectId _itemId) {
        DBObject query = new BasicDBObject("_id", _itemId);
        return this.collection.remove(query).wasAcknowledged();
    }

    public boolean updateItemStatus(ObjectId _itemId, ItemStatus status) {
        DBObject query = new BasicDBObject("_id", _itemId);

        DBObject statusDoc = new BasicDBObject();
        statusDoc.put("stocked", status.getStocked());
        statusDoc.put("reserved", status.getReserved());
        statusDoc.put("sold", status.getSold());

        // If without $set operator,
        // than order document will be not updated, but replaced
        DBObject update = new BasicDBObject("status", statusDoc);
        DBObject set = new BasicDBObject("$set", update);
        return this.collection.update(query, set).wasAcknowledged();
    }

    public Item findItem(ObjectId _itemId) {
        DBObject query = new BasicDBObject("_id", _itemId);
        DBObject itemDoc = this.collection.findOne(query);
        return ItemConverter.toItem(itemDoc);
    }

    public ItemStatus findItemStatus(ObjectId _itemId) {
        DBObject query = new BasicDBObject("_id", _itemId);
        DBObject projection = new BasicDBObject("status", 1);

        DBObject doc = this.collection.findOne(query, projection);

        if (doc == null) {
            return null;
        }

        DBObject statusDoc = (DBObject) doc.get("status");

        int stocked = (Integer) statusDoc.get("stocked");
        int reserved = (Integer) statusDoc.get("reserved");
        int sold = (Integer) statusDoc.get("sold");

        return new ItemStatus(stocked, reserved, sold);
    }

    public List<Item> findAllItems() {
        List<Item> items = new ArrayList<>();

        DBCursor cursor = this.collection.find();

        while (cursor.hasNext()) {
            DBObject itemDoc = cursor.next();
            items.add(ItemConverter.toItem(itemDoc));
        }
        cursor.close();

        return items;
    }

    public boolean moveStockedToReserved(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeStocked(-quantity);
            newStatus.changeReserved(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean moveStockedToSold(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item.getId());

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeStocked(-quantity);
            newStatus.changeSold(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean moveReservedtoStocked(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeReserved(-quantity);
            newStatus.changeStocked(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean moveReservedToSold(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeReserved(-quantity);
            newStatus.changeSold(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean moveSoldToStocked(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeSold(-quantity);
            newStatus.changeStocked(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean moveSoldtoReserved(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus newStatus = findItemStatus(_itemId);

            if (newStatus == null) {
                return false;
            }
            newStatus.changeSold(-quantity);
            newStatus.changeReserved(+quantity);

            if ( ! updateItemStatus(_itemId, newStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean enoughItems(Map<Item, Integer> orderItems) {

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            int quantity = orderItems.get(item);

            ItemStatus status = findItemStatus(_itemId);

            if (status == null || quantity > status.getStocked()) {
                return false;
            }
        }
        return true;
    }

    public long count() {
        return this.collection.count();
    }
}