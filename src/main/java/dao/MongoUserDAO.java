package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import converter.UserConverter;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by deoxys on 17.05.16.
 */
public class MongoUserDAO {
    private MongoCollection<Document> mongoColl;
    public final static String MONGO_DB = "dstr";

    public MongoUserDAO(MongoClient mongoClient) {
        this.mongoColl = mongoClient.getDatabase(MONGO_DB).getCollection("users");
    }

    public User createUser(User user) {
        Document doc = UserConverter.toDocument(user);
        this.mongoColl.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        user.setId(id.toString());
        return user;
    }

    public int updateUser(User user) {
        Document doc = UserConverter.toDocument(user);
        System.out.println("user id = " + user.getId());
        UpdateResult updRes = this.mongoColl.updateOne(
                eq("_id", new ObjectId(user.getId())), new Document("$set", doc));
        return (int) updRes.getModifiedCount();
    }

    public User findUser(User user) {
        Document doc = this.mongoColl.find(
                eq("_id", new ObjectId(user.getId()))).first();
        return UserConverter.toUser(doc);
    }
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<User>();
        MongoCursor<Document> cursor = this.mongoColl.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                User user = UserConverter.toUser(doc);
                users.add(user);
            }
        } finally {
            cursor.close();
        }
        return users;
    }

    public int deleteUser(User user) {
        DeleteResult delRes = this.mongoColl.deleteOne(
                eq("_id", new ObjectId(user.getId())));
        return (int) delRes.getDeletedCount();
    }
}