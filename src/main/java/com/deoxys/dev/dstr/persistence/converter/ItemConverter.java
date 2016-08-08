package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Currency;

public class ItemConverter implements MongoConverter<Item> {

    @Override
    @SuppressWarnings("unchecked")
    public Item toObject(Document doc) {

        if (doc == null) return null;

        Item item = new Item();
        item.setId(doc.get("_id").toString());
        doc.remove("_id");

        item.setName(doc.getString("name"));
        doc.remove("name");

        item.setPrice(doc.getDouble("price"));
        doc.remove("price");

        item.setCurrency(convertCurrency(doc.getString("currency")));
        doc.remove("currency");

        Document statusDoc = (Document) doc.get("status");
        item.setStatus(new ItemStatus(
                statusDoc.getInteger("stocked"),
                statusDoc.getInteger("reserved"),
                statusDoc.getInteger("sold")
        ));
        doc.remove("status");
        item.setTags((ArrayList) doc.get("tags"));
        doc.remove("tags");

        doc.keySet().forEach(k -> item.addField(k, doc.getString(k)));

        return item;
    }

    private Currency convertCurrency(String code) {
        return code != null ? Currency.getInstance(code) : null;
    }

    @Override
    public Document toDocument(Item item) {
        Document doc = new Document();

        if (item.getId() != null) doc.put("_id", new ObjectId(item.getId()));

        doc.put("name", item.getName());
        doc.put("price", item.getPrice());
        doc.put("currency", item.getCurrency().toString());

        Document statusDoc = new Document();
        statusDoc.put("stocked", item.stocked());
        statusDoc.put("reserved", item.reserved());
        statusDoc.put("sold", item.sold());
        doc.put("status", statusDoc);
        doc.put("tags", item.getTags());

        item.getExtendedFields().forEach(doc::put);

        return doc;
    }
}