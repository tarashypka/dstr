package converter;

import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by deoxys on 17.05.16.
 */
public class UserConverter {
    public static Document toDocument(User user) {
        Document document = new Document();

        if (user.getId() != null) {
            document.append("_id", new ObjectId(user.getId()));
        }
        document.append("name", user.getName());
        document.append("email", user.getEmail());
        document.append("password", user.getPassword());

        return document;
    }

    public static User toUser(Document doc) {
        User user = new User();

        ObjectId id = (ObjectId) doc.get("_id");
        user.setId(id.toString());
        user.setName((String) doc.get("name"));
        user.setEmail((String) doc.get("email"));
        user.setPassword((String) doc.get("password"));

        return user;

    }
}