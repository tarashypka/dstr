package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.bson.LazyBSONList;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by deoxys on 27.05.16.
 */

public class OrderConverter implements MongoConverter<Order> {

    @Override
    public Order toObject(Document doc) {
        Order order = new Order();
        order.setId(doc.get("_id").toString());
        order.setOrderNumber((Long) doc.get("orderNumber"));
        order.setDate((Date) doc.get("date"));

        Document customerDoc = (Document) doc.get("customer");
        CustomerConverter customerConverter = new CustomerConverter();
        order.setCustomer(customerConverter.toObject(customerDoc));

        Map<Item, Integer> items = new HashMap<>();
        List<Document> itemsL = (ArrayList) doc.get("items");
        for (Document itemDoc : itemsL) {
            DBRef dbRef = (DBRef) itemDoc.get("id");
            String id = dbRef.getId().toString();
            String name = itemDoc.getString("name");
            items.put(new Item(id, name), itemDoc.getInteger("quantity"));
        }
        order.setItems(items);

        Map<Currency, Double> receipt = new HashMap<>();
        List<Document> receiptL = (ArrayList) doc.get("receipt");
        for (Document priceDoc : receiptL) {
            Double price = (Double) priceDoc.get("price");
            Currency currency = Currency.getInstance((String) priceDoc.get("currency"));
            receipt.put(currency, price);
        }
        order.setReceipt(receipt);

        int status = (Integer) doc.get("status");
        order.setStatus(Order.OrderStatus.getStatus(status));

        return order;
    }

    @Override
    public Document toDocument(Order order) {
        Document doc = new Document();

        if (order.getId() != null) {
            doc.put("_id", new ObjectId(order.getId()));
        }
        doc.put("orderNumber", order.getOrderNumber());
        doc.put("date", order.getDate());

        Customer customer = order.getCustomer();
        CustomerConverter customerConverter = new CustomerConverter();
        doc.put("customer", customerConverter.toDocument(customer));

        BasicDBList itemsDbl = new BasicDBList();
        Map<Item, Integer> orderItems = order.getItems();
        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            DBRef dbRef = new DBRef("items", _itemId);
            DBObject orderItemsDoc = new BasicDBObject();
            orderItemsDoc.put("id", dbRef);
            orderItemsDoc.put("name", item.getName());
            orderItemsDoc.put("quantity", orderItems.get(item));
            itemsDbl.add(orderItemsDoc);
        }
        doc.put("items", itemsDbl);

        Map<Currency, Double> receipt = order.getReceipt();
        List<DBObject> receiptList = new ArrayList<>();
        for (Currency currency : receipt.keySet()) {
            DBObject receiptItemDoc = new BasicDBObject();
            receiptItemDoc.put("currency", currency.toString());
            receiptItemDoc.put("price", receipt.get(currency));
            receiptList.add(receiptItemDoc);
        }
        doc.put("receipt", receiptList);

        String status = order.getStatus().name();
        doc.put("status", Order.OrderStatus.valueOf(status).getValue());

        return doc;
    }
}