package com.deoxys.dev.dstr.persistence.converter;

import com.deoxys.dev.dstr.domain.model.User;
import org.bson.Document;

public class CustomerConverter implements MongoConverter<User> {

    @Override
    public Document toDocument(User user) {
        return new Document("email", user.getEmail());
    }

    @Override
    public User toObject(Document doc) {
        return new User.UserBuilder(doc.getString("email")).build();
    }
}
