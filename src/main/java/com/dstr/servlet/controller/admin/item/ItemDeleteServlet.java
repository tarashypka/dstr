package com.dstr.servlet.controller.admin.item;

import com.dstr.model.Item;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "ItemDelete", urlPatterns = "/item/delete")
public class ItemDeleteServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemDeleteServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.equals("")) {
            throw new ServletException("Wrong item id");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        if (itemDAO.deleteItem(new ObjectId(id)) > 0) {
            logger.info("Item with id=" + id + " was deleted");

            // Update session
            Map<Item, Integer> items = (HashMap)
                    request.getSession().getAttribute("items");

            items.remove(new Item(id));
            request.getSession().setAttribute("items", items);
        } else {
            logger.error("Item with id=" + id + " was not deleted");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/items.jsp")
                .forward(request, response);
    }
}
