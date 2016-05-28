package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import converter.OrderConverter;
import model.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

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

    public Order findOrder(Order order) {
        Document doc = this.mongoColl.find(
                eq("_id", new ObjectId(order.getId()))).first();
        return OrderConverter.toOrder(doc);
    }
}