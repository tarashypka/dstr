package com.deoxys.dev.dstr.persistence.converter;

import com.mongodb.DBObject;

/**
 * Created by deoxys on 09.07.16.
 */

public interface MongoConverter<T> {

    public T toObject(DBObject doc);
    public DBObject toDocument(T obj);
}
