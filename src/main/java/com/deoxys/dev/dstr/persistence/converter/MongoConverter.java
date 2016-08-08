package com.deoxys.dev.dstr.persistence.converter;

import org.bson.Document;

public interface MongoConverter<T> {
    T toObject(Document doc);
    Document toDocument(T obj);
}
