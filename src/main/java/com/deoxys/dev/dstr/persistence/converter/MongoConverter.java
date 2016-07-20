package com.deoxys.dev.dstr.persistence.converter;

import org.bson.Document;

/**
 * Created by deoxys on 09.07.16.
 */

public interface MongoConverter<T> {
    T toObject(Document doc);
    Document toDocument(T obj);
}
