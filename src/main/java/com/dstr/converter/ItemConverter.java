package com.dstr.converter;

import com.dstr.model.Item;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class ItemConverter {

    public static Document toDocument(Item item) {
        Document doc = new Document();

        if (item.getId() != null) {
            doc.append("_id", new ObjectId(item.getId()));
        }
        doc.append("category", item.getCategory());
        doc.append("price", item.getPrice());
        doc.append("currency", item.getCurrency().toString());

        Map<String, String> extFields = item.getExtendedFields();
        if (extFields != null) {
            for (String fieldKey: extFields.keySet()) {
                doc.append(fieldKey, extFields.get(fieldKey));
            }
        }
        return doc;
    }

    public static Item toItem(Document doc) {
        Item item = new Item();

        item.setId(doc.get("_id").toString());
        doc.remove("_id");

        String category = doc.getString("category");
        item.setCategory(category);
        doc.remove("category");

        item.setPrice(doc.getDouble("price"));
        doc.remove("price");

        item.setCurrency(doc.getString("currency"));
        doc.remove("currency");

        item.setLeft(doc.getInteger("left"));
        doc.remove("left");

        Map<String, String> extFields = new HashMap<>();

        for (String extKey : doc.keySet()) {
            extFields.put(extKey, (String) doc.get(extKey));
        }
        item.setExtendedFields(extFields);

        return item;
    }
}