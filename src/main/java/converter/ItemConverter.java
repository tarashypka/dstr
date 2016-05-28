package converter;

import com.mongodb.DBRef;
import model.item.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */
public class ItemConverter {
    private static final String COLL = "items";

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

    public static void toBook(Document doc, Book book) {
        book.setSubject(doc.getString("subject"));
        doc.remove("subject");
        book.setAuthor(doc.getString("author"));
        doc.remove("author");
        book.setTitle(doc.getString("title"));
        doc.remove("title");
        book.setLanguage(doc.getString("language"));
        doc.remove("language");
        book.setISBN(doc.getString("ISBN"));
        doc.remove("ISBN");
    }

    public static void toMonitor(Document doc, Monitor mon) {
        mon.setScreenSize(doc.getInteger("screenSize"));
        doc.remove("screenSize");
    }

    public static void toMusic(Document doc, Music music) {
        music.setFormat(doc.getString("format"));
        doc.remove("format");
        music.setArtist(doc.getString("artist"));
        doc.remove("artist");
        music.setAlbum(doc.getString("album"));
        doc.remove("album");
        music.setUPC(doc.getString("UPC"));
        doc.remove("UPC");
    }

    public static void toPhone(Document doc, Phone phone) {
        phone.setProducer(doc.getString("producer"));
        doc.remove("producer");
        phone.setModel(doc.getString("model"));
        doc.remove("model");
    }

    public static void toShoes(Document doc, Shoes shoes) {
        shoes.setBrand(doc.getString("brand"));
        doc.remove("brand");
        shoes.setFabric(doc.getString("fabric"));
        doc.remove("fabric");
        shoes.setColor(doc.getString("color"));
        doc.remove("color");
        shoes.setSize(doc.getInteger("size"));
        doc.remove("size");
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

        /*
        switch (category) {
            case "Book":
                toBook(doc, (Book) item);
                break;
            case "Monitor":
                toMonitor(doc, (Monitor) item);
                break;
            case "Music":
                toMusic(doc, (Music) item);
                break;
            case "Phone":
                toPhone(doc, (Phone) item);
                break;
            case "Shoes":
                toShoes(doc, (Shoes) item);
                break;
            default:
                break;
        } */
        Map<String, String> extFields = new HashMap<>();

        for (String extKey : doc.keySet()) {
            extFields.put(extKey, (String) doc.get(extKey));
        }
        item.setExtendedFields(extFields);

        return item;
    }

    public static List<DBRef> toDBRefs(List<Item> items) {
        List<DBRef> dbRefs = new ArrayList<>();

        for (Item item : items) {
            dbRefs.add(new DBRef(COLL, item.getId()));
        }
        return dbRefs;
    }

    public static List<ObjectId> toItemsIds(List<DBRef> dbRefs) {

        List<ObjectId> itemsIds = new ArrayList<>();

        for (DBRef dbRef : dbRefs) {
            ObjectId id = (ObjectId) dbRef.getId();
            itemsIds.add(id);
        }
        return itemsIds;
    }
}