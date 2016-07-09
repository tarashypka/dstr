package com.deoxys.dev.dstr.presentation.servlet.controller.admin.item;

import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
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

        String id = req.getParameter("id");

        if (id == null || id.equals("")) {
            throw new ServletException("Wrong item id");
        }

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        ItemDAO itemDAO = new ItemDAO(mongo);

        if (itemDAO.delete(id)) {
            logger.info("Item with id=" + id + " was successfully deleted");
        } else {
            logger.error("Item with id=" + id + " was not deleted");
        }
        resp.sendRedirect(req.getContextPath() + "/items");
    }
}
