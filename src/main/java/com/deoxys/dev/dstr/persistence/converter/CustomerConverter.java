package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.bson.Document;

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
        customer.setId(doc.getLong("id"));
        customer.setEmail(doc.getString("email"));
        customer.setName(doc.getString("name"));
        customer.setSurname(doc.getString("surname"));
        return customer;
    }
}
