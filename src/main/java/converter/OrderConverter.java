package converter;

import com.mongodb.BasicDBList;
import com.mongodb.DBRef;
import model.Customer;
import model.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */
public class OrderConverter {
    public static Document toDocument(Order order) {
        Document doc = new Document();

        if (order.getId() != null) {
            doc.append("_id", new ObjectId(order.getId()));
        }
        doc.append("orderNumber", order.getOrderNumber());

        Customer customer = order.getCustomer();
        doc.append("customer", CustomerConverter.toDocument(customer));

        return doc;
    }

    public static Order toOrder(Document doc) {
        Order order = new Order();

        ObjectId id = (ObjectId) doc.get("_id");
        order.setId(id.toString());
        order.setOrderNumber((String) doc.get("orderNumber"));

        Document customerDoc = (Document) doc.get("customer");

        order.setCustomer(CustomerConverter.toCustomer(customerDoc));
        List<DBRef> itemsDBRefs = new ArrayList<>();
        BasicDBList itemsDbl = (BasicDBList) doc.get("items");
        itemsDbl.toArray(itemsDBRefs.toArray());

        List<ObjectId> itemsIds = new ArrayList<>();
        for (DBRef dbRef : itemsDBRefs) {
            itemsIds.add((ObjectId) dbRef.getId());
        }
        order.setItemsIds(itemsIds);
        order.setTotalSum(Integer.parseInt((String) doc.get("totalSum")));
        return order;
    }
}