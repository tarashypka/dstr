package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.persistence.converter.MongoConverter;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 09.07.16.
 */

/**
 * Created by deoxys on 09.07.16.
 *
 * Mongo 3.2 Java Driver proposes at least two approaches
 * to execute basic collection queries:
 *
 *      1. Using newly implemented (v2.10.0) classes:
 *              MongoClient, MongoCollection, Document, ...
 *      2. Using standard classes:
 *              Mongo, DBCollection, DBObject, ...
 *
 * First approach does not support queries with projections yet,
 * so second (deprecated) was used.
 */

public abstract class MongoDAO<T> {

    protected MongoClient client;
    protected DB db;
    protected DBCollection collection;
    protected MongoConverter<T> converter;

    private final String MONGO_DB = "dstr";

    public MongoDAO(MongoClient client, String collName, MongoConverter<T> converter) {
        this.client = client;
        db = client.getDB(MONGO_DB);
        collection = db.getCollection(collName);
        this.converter = converter;
    }

    public abstract boolean add(T obj);

    public long count() {
        return collection.count();
    }

    public T get(String id) {
        ObjectId _id = new ObjectId(id);
        DBObject query = new BasicDBObject("_id", _id);
        DBObject doc = collection.findOne(query);
        return converter.toObject(doc);
    }

    public List<T> getAll() {
        List<T> objects = new ArrayList<>();

        // Exclude sequences
        DBObject exists = new BasicDBObject("$exists", false);
        DBObject query = new BasicDBObject("seq", exists);
        DBCursor cursor = collection.find(query);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            objects.add(converter.toObject(doc));
        }
        cursor.close();
        return objects;
    }

    public long getNextSequence(String fieldName) {
        DBObject query = new BasicDBObject("_id", fieldName);
        DBObject seq = new BasicDBObject("seq", 1);
        DBObject update = new BasicDBObject("$inc", seq);
        DBObject obj = collection.findAndModify(query, update);
        return (long) obj.get("seq");
    }

    public boolean update(T obj) {
        DBObject doc = converter.toDocument(obj);
        return collection.save(doc).wasAcknowledged();
    }

    public boolean delete(String id) {
        ObjectId _id = new ObjectId(id);
        DBObject query = new BasicDBObject("_id", _id);
        return this.collection.remove(query).wasAcknowledged();
    }
}