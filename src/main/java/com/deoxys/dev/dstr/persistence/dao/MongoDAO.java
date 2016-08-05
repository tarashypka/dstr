package com.deoxys.dev.dstr.persistence.dao;

import com.deoxys.dev.dstr.persistence.converter.MongoConverter;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

public abstract class MongoDAO<T> {

    MongoClient client;
    MongoDatabase database;
    MongoCollection<Document> collection;
    protected MongoConverter<T> converter;

    private final String MONGO_DB = "dstr";

    MongoDAO(MongoClient client, String collName, MongoConverter<T> converter) {
        this.client = client;
        database = client.getDatabase(MONGO_DB);
        collection = database.getCollection(collName);
        this.converter = converter;
    }

    public abstract void add(T obj);

    public long count() {
        return collection.count();
    }

    public T get(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        return converter.toObject(collection.find(filter).first());
    }

    public List<T> getAll() {
        List<T> objects = new ArrayList<>();

        // Exclude sequences
        Bson filter = exists("seq", false);
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext())
                objects.add(converter.toObject(cursor.next()));
        }

        return objects;
    }

    public long getNextSequence(String fieldName) {
        Bson filter = new BsonDocument("_id", new BsonString(fieldName));
        Bson seq = new BsonDocument("seq", new BsonInt64(1));
        Bson update = new BasicDBObject("$inc", seq);
        Document doc = collection.findOneAndUpdate(filter, update);
        return (long) doc.get("seq");
    }

    public void update(T obj) {
        Document doc = converter.toDocument(obj);
        Bson filter = eq("_id", doc.getObjectId("_id"));
        collection.replaceOne(filter, doc);
    }

    public void delete(String id) {
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }
}