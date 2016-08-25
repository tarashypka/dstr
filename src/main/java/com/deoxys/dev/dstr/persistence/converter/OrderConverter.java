package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.model.OrderStatus;
import com.deoxys.dev.dstr.domain.model.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class OrderConverter implements MongoConverter<Order> {

    @Override
    @SuppressWarnings("unchecked")
    public Order toObject(Document doc) {

        if (doc == null) return null;

        Order order = new Order();
        order.setId(doc.get("_id").toString());
        order.setOrderNumber(doc.getLong("orderNumber"));
        order.setDate(doc.getDate("date"));

        Document customerDoc = (Document) doc.get("customer");
        order.setCustomer(new CustomerConverter().toObject(customerDoc));

        List<Document> items = (ArrayList) doc.get("items");

        items.forEach(itemDoc -> {
            DBRef dbRef = (DBRef) itemDoc.get("id");
            String id = dbRef.getId().toString();
            String name = itemDoc.getString("name");
            order.addItem(
                    new Item.ItemBuilder(name).withId(id).build(),
                    itemDoc.getInteger("quantity")
            );
        });

        List<Document> receipt = (ArrayList) doc.get("receipt");
        receipt.stream().collect(Collectors.toMap(
                k -> convertCurrency(k.getString("currency")),
                k -> k.getDouble("price")
        )).forEach(order::addPrice);

        order.setStatus(OrderStatus.getStatus(doc.getInteger("status")));

        return order;
    }

    private Currency convertCurrency(String code) {
        return code != null ? Currency.getInstance(code) : null;
    }

    @Override
    public Document toDocument(Order order) {
        Document doc = new Document();

        if (order.getId() != null) {
            doc.put("_id", new ObjectId(order.getId()));
        }
        doc.put("orderNumber", order.getOrderNumber());
        doc.put("date", order.getDate());

        User user = order.getCustomer();
        CustomerConverter customerConverter = new CustomerConverter();
        doc.put("customer", customerConverter.toDocument(user));

        BasicDBList itemsDbl = new BasicDBList();
        order.getItems().forEach((item, amount) -> {
            DBRef dbRef = new DBRef("items", new ObjectId(item.getId()));
            DBObject orderItemsDoc = new BasicDBObject();
            orderItemsDoc.put("id", dbRef);
            orderItemsDoc.put("name", item.getName());
            orderItemsDoc.put("quantity", amount);
            itemsDbl.add(orderItemsDoc);
        });
        doc.put("items", itemsDbl);

        List<DBObject> receiptList = new ArrayList<>();
        order.getReceipt().forEach((currency, price) -> {
            DBObject receiptItemDoc = new BasicDBObject();
            receiptItemDoc.put("currency", currency.toString());
            receiptItemDoc.put("price", price);
            receiptList.add(receiptItemDoc);
        });
        doc.put("receipt", receiptList);

        String status = order.getStatus().name();
        doc.put("status", OrderStatus.valueOf(status).getValue());

        return doc;
    }
}