package com.deoxys.dev.dstr.presentation.servlet.controller.admin.item;

import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.MongoItemDao;
import org.apache.log4j.Logger;
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

@WebServlet(name = "ItemDelete", urlPatterns = "/item/delete")
public class ItemDeleteServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemDeleteServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter("id");

        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Wrong item id");
        }

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        MongoItemDao itemDAO = new MongoItemDao(mongo);

        if (itemDAO.removeItem(new ObjectId(itemId))) {
            logger.info("Item with id=" + itemId + " was successfully deleted");
        } else {
            logger.error("Item with id=" + itemId + " was not deleted");
        }
        resp.sendRedirect(req.getContextPath() + "/items");
    }
}
