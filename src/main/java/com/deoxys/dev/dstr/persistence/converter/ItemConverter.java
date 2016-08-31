package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.item.Item;
import com.deoxys.dev.dstr.domain.model.item.ItemStatus;
import com.deoxys.dev.dstr.domain.model.item.Price;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Currency;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemConverter implements MongoConverter<Item> {

    @Override
    @SuppressWarnings("unchecked")
    public Item toObject(Document doc) {

        if (doc == null) return null;

        Price price = new Price(
                doc.getDouble("price"),
                convertCurrency(doc.getString("currency"))
        );

        Document statusDoc = (Document) doc.get("status");
        ItemStatus status = new ItemStatus(
                statusDoc.getInteger("stocked"),
                statusDoc.getInteger("reserved"),
                statusDoc.getInteger("sold")
        );

        Item.ItemBuilder builder = new Item.ItemBuilder(doc.getString("name"))
                .withId(doc.getObjectId("_id").toString())
                .withPrice(price)
                .withStatus(status)
                .withTags((ArrayList) doc.get("tags"));

        doc.remove("_id");
        doc.remove("name");
        doc.remove("price");
        doc.remove("currency");
        doc.remove("status");
        doc.remove("tags");

        return builder.withExtFields(doc.keySet().stream().collect(
                Collectors.toMap(Function.identity(), doc::getString))
        ).build();
    }

    private Currency convertCurrency(String code) {
        return code != null ? Currency.getInstance(code) : null;
    }

    @Override
    public Document toDocument(Item item) {
        Document doc = new Document();

        if (item.getId() != null) doc.put("_id", new ObjectId(item.getId()));

        Price price = item.getPrice();

        doc.put("name", item.getName());
        doc.put("price", price.getCash());
        doc.put("currency", price.getCurrency().toString());

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