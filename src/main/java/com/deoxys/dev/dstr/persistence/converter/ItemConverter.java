package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class ItemConverter implements MongoConverter<Item> {

    @Override
    public Item toObject(Document doc) {

        if (doc == null) return null;

        Item item = new Item();
        item.setId(doc.get("_id").toString());
        doc.remove("_id");

        String category = (String) doc.get("category");
        item.setCategory(category);
        doc.remove("category");

        item.setPrice((Double) doc.get("price"));
        doc.remove("price");

        item.setCurrency((String) doc.get("currency"));
        doc.remove("currency");

        Document statusDoc = (Document) doc.get("status");
        int stocked = (Integer) statusDoc.get("stocked");
        int reserved = (Integer) statusDoc.get("reserved");
        int sold = (Integer) statusDoc.get("sold");

        item.setStatus(new ItemStatus(stocked, reserved, sold));
        doc.remove("status");

        for (String fname : doc.keySet())
            item.addField(fname, doc.getString(fname));

        return item;
    }

    @Override
    public Document toDocument(Item item) {
        Document doc = new Document();

        if (item.getId() != null) doc.put("_id", new ObjectId(item.getId()));

        doc.put("category", item.getCategory());
        doc.put("price", item.getPrice());
        doc.put("currency", item.getCurrency().toString());

        Document statusDoc = new Document();
        statusDoc.put("stocked", item.stocked());
        statusDoc.put("reserved", item.reserved());
        statusDoc.put("sold", item.sold());
        doc.put("status", statusDoc);

        Map<String, String> extFields = item.getExtendedFields();
        for (String name: extFields.keySet())
            doc.put(name, extFields.get(name));

        return doc;
    }
}