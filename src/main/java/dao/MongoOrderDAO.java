package dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import converter.OrderConverter;
import model.customer.Customer;
import model.order.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by deoxys on 27.05.16.
 */
public class MongoOrderDAO {
    private MongoCollection<Document> mongoColl;
    public final static String MONGO_DB = "dstr";
    public final static String MONGO_COLL = "orders";

    public MongoOrderDAO(MongoClient mongoClient) {
        this.mongoColl = mongoClient.getDatabase(MONGO_DB).getCollection(MONGO_COLL);
    }

    public Order createOrder(Order order) {
        Document doc = OrderConverter.toDocument(order);
        this.mongoColl.insertOne(doc);
        order.setId(doc.get("_id").toString());
        return order;
    }

    public int updateOrder(Order order) {
        Document doc = OrderConverter.toDocument(order);
        UpdateResult updRes = this.mongoColl.updateOne(
                eq("_id", new ObjectId(order.getId())), new Document("$set", doc));
        return (int) updRes.getModifiedCount();
    }

    public Order findOrder(ObjectId _id) {
        Document doc = this.mongoColl.find(
                eq("_id", _id)).first();
        return OrderConverter.toOrder(doc);
    }

    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        MongoCursor<Document> cursor = this.mongoColl.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                orders.add(OrderConverter.toOrder(doc));
            }
        } finally {
            cursor.close();
        }
        return orders;
    }

    public List<Order> findAllCustomerOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();

        BasicDBObject query = new BasicDBObject(
                "customer.name", customer.getName()).append(
                "customer.surname", customer.getSurname()).append(
                "customer.email", customer.getEmail());

        MongoCursor<Document> cursor = this.mongoColl.find(query).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                orders.add(OrderConverter.toOrder(doc));
            }
        } finally {
            cursor.close();
        }
        return orders;
    }

    public Map<String, Integer> findCustomerItems(Customer customer) {
        Map<String, Integer> items = new HashMap<>();

        BasicDBObject query = new BasicDBObject(
                "customer.name", customer.getName()).append(
                "customer.surname", customer.getSurname()).append(
                "customer.email", customer.getEmail());

        MongoCursor<Document> cursor = this.mongoColl.find(query).iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                BasicDBList itemsDbl = (BasicDBList) doc.get("items");

                for (Object el : itemsDbl) {
                    BasicDBObject obj = (BasicDBObject) el;
                    DBRef dbRef = (DBRef) obj.get("id");
                    items.put(dbRef.getId().toString(), obj.getInt("quantity"));
                }
            }
        } finally {
            cursor.close();
        }
        return items;
    }
}