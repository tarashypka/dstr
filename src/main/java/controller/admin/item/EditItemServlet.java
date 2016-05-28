package controller.admin.item;

import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import dao.MongoItemDAO;
import model.item.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */
@WebServlet("/admin/editItem")
public class EditItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id товару");
        }
        String category = request.getParameter("category");
        double price = Double.parseDouble(request.getParameter("price"));
        String currency = request.getParameter("currency");

        String errtype = null;
        if (category == null || category.equals(""))
            errtype = "category";
        else if (price <= 0.0)
            errtype = "price";
        else if (currency == null || currency.equals(""))
            errtype = "currency";

        Item item = new Item();
        item.setId(id);
        item.setCategory(category);
        item.setPrice(price);
        item.setCurrency(currency);
        item.setExtendedFields(new HashMap<String, String>());
        if (errtype != null) {
            request.setAttribute("item", item);
            request.setAttribute("errtype", errtype);
        } else {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);

            if (itemDAO.updateItem(item) > 0) {
                request.setAttribute("success", "Товар редаговано");

                // Should be replaced, too heavy
                List<Item> items = itemDAO.findAllItems();

                // Update Hz cache
                List<Item> hzItems =
                        Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ITEMS");

                hzItems.add(item);
                hzItems.retainAll(items);
            } else {
                request.setAttribute("error", "Товар не редаговано");
            }
        }
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id товару");
        }
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);
        Item item = new Item();
        item.setId(id);
        item = itemDAO.findItem(item);
        request.setAttribute("item", item);
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }
}
