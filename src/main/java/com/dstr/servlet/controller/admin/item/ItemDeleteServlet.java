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

        String itemId = request.getParameter("id");

        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Wrong item id");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        if (itemDAO.removeItem(new ObjectId(itemId))) {
            logger.info("Товар з id=" + itemId + " успішно видалено");

            // Update session
            Map<Item, Integer> items = (HashMap)
                    request.getSession().getAttribute("items");

            items.remove(new Item(itemId));
            request.getSession().setAttribute("items", items);
        } else {
            logger.error("Товар з id=" + itemId + " не видалено");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/items.jsp")
                .forward(request, response);
    }
}
