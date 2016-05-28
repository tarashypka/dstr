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

/**
 * Created by deoxys on 28.05.16.
 */
@WebServlet("/admin/deleteItem")
public class DeleteItemServlet extends HttpServlet {
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

        if (itemDAO.deleteItem(item) > 0) {
            request.setAttribute("success", "Товар видалено");

            // Update Hz cache
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ITEMS").remove(item);
        } else {
            request.setAttribute("error", "Товар не видалено");
        }
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }
}
