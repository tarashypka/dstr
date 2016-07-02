package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.Customer;
import com.deoxys.dev.dstr.domain.Item;
import com.deoxys.dev.dstr.domain.Order;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by deoxys on 27.05.16.
 */

public class OrderConverter {
    private static final String COLL = "orders";

    public static DBObject toDocument(Order order) {
        DBObject doc = new BasicDBObject();

        if (order.getId() != null) {
            doc.put("_id", new ObjectId(order.getId()));
        }
        doc.put("orderNumber", order.getOrderNumber());

        doc.put("date", order.getDate());

        Customer customer = order.getCustomer();
        doc.put("customer", CustomerConverter.toDocument(customer));

        BasicDBList itemsDbl = new BasicDBList();
        Map<Item, Integer> orderItems = order.getItems();

        for (Item item : orderItems.keySet()) {
            ObjectId _itemId = new ObjectId(item.getId());
            DBRef dbRef = new DBRef(COLL, _itemId);
            DBObject orderItemsDoc = new BasicDBObject();
            orderItemsDoc.put("id", dbRef);
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

    public static Order toOrder(DBObject doc) {
        Order order = new Order();

        order.setId(doc.get("_id").toString());
        order.setOrderNumber((String) doc.get("orderNumber"));
        order.setDate((Date) doc.get("date"));

        DBObject customerDoc = (DBObject) doc.get("customer");
        order.setCustomer(CustomerConverter.toCustomer(customerDoc));

        Map<Item, Integer> items = new HashMap<>();
        BasicDBList itemsDbl = (BasicDBList) doc.get("items");
        for (Object itemObj : itemsDbl) {
            DBObject itemDoc = (DBObject) itemObj;
            DBRef dbRef = (DBRef) itemDoc.get("id");
            String id = dbRef.getId().toString();
            items.put(new Item(id), (Integer) itemDoc.get("quantity"));
        }
        order.setItems(items);

        Map<Currency, Double> receipt = new HashMap<>();
        BasicDBList receiptDocs = (BasicDBList) doc.get("receipt");
        for (Object receiptObj : receiptDocs) {
            DBObject receiptDoc = (DBObject) receiptObj;
            Double price = (Double) receiptDoc.get("price");
            Currency currency = Currency.getInstance((String) receiptDoc.get("currency"));
            receipt.put(currency, price);
        }
        order.setReceipt(receipt);

        int status = (Integer) doc.get("status");
        order.setStatus(Order.OrderStatus.orderStatusByValue(status));

        return order;
    }
}