package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Customer;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by deoxys on 27.05.16.
 */

public class CustomerConverter {

    public static DBObject toDocument(Customer customer) {
        DBObject doc = new BasicDBObject();

        doc.put("email", customer.getEmail());
        doc.put("name", customer.getName());
        doc.put("surname", customer.getSurname());

        return doc;
    }

    public static Customer toCustomer(DBObject doc) {
        Customer customer = new Customer();

        customer.setEmail((String )doc.get("email"));
        customer.setName((String) doc.get("name"));
        customer.setSurname((String) doc.get("surname"));

        return customer;
    }
}