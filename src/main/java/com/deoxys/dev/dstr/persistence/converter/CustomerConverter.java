package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.bson.Document;

/**
 * Created by deoxys on 27.05.16.
 */

public class CustomerConverter implements MongoConverter<Customer> {

    @Override
    public Document toDocument(Customer customer) {
        Document doc = new Document("id", customer.getId());
        doc.put("email", customer.getEmail());
        doc.put("name", customer.getName());
        doc.put("surname", customer.getSurname());
        return doc;
    }

    @Override
    public Customer toObject(Document doc) {
        Customer customer = new Customer();
        customer.setId((Long) doc.get("id"));
        customer.setEmail((String) doc.get("email"));
        customer.setName((String) doc.get("name"));
        customer.setSurname((String) doc.get("surname"));
        return customer;
    }
}
