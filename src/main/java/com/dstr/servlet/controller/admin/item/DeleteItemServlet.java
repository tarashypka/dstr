package com.dstr.servlet.controller.admin.item;

import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import com.dstr.model.item.Item;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "DeleteItem", urlPatterns = "/admin/items/delete")
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

        ObjectId _id = new ObjectId(id);
        item = itemDAO.findItem(_id);

        if (itemDAO.deleteItem(_id) > 0) {
            request.setAttribute("success", "Товар видалено");

            // Update Hz cache
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ITEMS").remove(item);
        } else {
            request.setAttribute("error", "Товар не видалено");
        }
        request.getRequestDispatcher("/admin/items").forward(request, response);
    }
}
