package com.dstr.converter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.dstr.model.Customer;
import com.dstr.model.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by deoxys on 27.05.16.
 */

public class OrderConverter {
    private static final String COLL = "orders";

    public static Document toDocument(Order order) {
        Document doc = new Document();

        if (order.getId() != null) {
            doc.append("_id", new ObjectId(order.getId()));
        }
        doc.append("orderNumber", order.getOrderNumber());

        Date date = (Date) order.getDate();
        doc.append("date", date);

        Customer customer = order.getCustomer();
        doc.append("customer", CustomerConverter.toDocument(customer));

        // Items list and payment object should be converted here
        BasicDBList itemsDbl = new BasicDBList();
        Map<String, Integer> items = order.getItems();

        for (String id : items.keySet()) {
            ObjectId _id = new ObjectId(id);
            DBRef dbRef = new DBRef(COLL, _id);
            itemsDbl.add(new BasicDBObject("id", dbRef)
                    .append("quantity", items.get(id)));
        }
        doc.append("items", itemsDbl);

        String status = order.getStatus().name();
        doc.append("status", Order.OrderStatus.valueOf(status));

        return doc;
    }

    public static Order toOrder(Document doc) {
        Order order = new Order();

        order.setId(doc.getObjectId("_id").toString());
        order.setOrderNumber(doc.getString("orderNumber"));
        order.setDate(doc.getDate("date"));

        Document customerDoc = (Document) doc.get("customer");
        order.setCustomer(CustomerConverter.toCustomer(customerDoc));

        Map<String, Integer> items = new HashMap<>();
        List<Document> itemsDocs = (ArrayList) doc.get("items");
        for (Document itemDoc : itemsDocs) {
            DBRef dbRef = (DBRef) itemDoc.get("id");
            String id = dbRef.getId().toString();
            Integer quantity = itemDoc.getInteger("quantity");
            items.put(id, quantity);
        }
        order.setItems(items);

        Map<Double, Currency> receipt = new HashMap<>();
        List<Document> receiptDocs = (ArrayList) doc.get("receipt");
        for (Document receiptDoc : receiptDocs) {
            Double price = receiptDoc.getDouble("price");
            Currency currency = Currency.getInstance(receiptDoc.getString("currency"));
            receipt.put(price, currency);
        }
        order.setReceipt(receipt);

        int status = doc.getInteger("status");
        order.setStatus(Order.OrderStatus.orderStatusbyValue(status));

        return order;
    }
}