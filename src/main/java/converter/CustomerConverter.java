package converter;

import model.customer.Customer;
import org.bson.Document;

/**
 * Created by deoxys on 27.05.16.
 */
public class CustomerConverter {
    public static Document toDocument(Customer customer) {
        Document doc = new Document();

        doc.append("name", customer.getName());
        doc.append("surname", customer.getSurname());
        doc.append("email", customer.getEmail());

        return doc;
    }

    public static Customer toCustomer(Document doc) {
        Customer customer = new Customer();

        customer.setName(doc.getString("name"));
        customer.setSurname(doc.getString("surname"));
        customer.setEmail(doc.getString("email"));

        return customer;
    }
}
