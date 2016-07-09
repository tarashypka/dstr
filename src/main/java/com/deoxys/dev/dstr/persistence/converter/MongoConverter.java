package com.deoxys.dev.dstr.persistence.converter;

import com.mongodb.DBObject;

/**
 * Created by deoxys on 09.07.16.
 */

public abstract class MongoConverter<T> {

    public abstract T toObject(DBObject doc);
    public abstract DBObject toDocument(T obj);
}
