package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.MongoItemDAO;
import com.deoxys.dev.dstr.domain.Item;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "Item", urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String itemId = req.getParameter("id");
        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Wrong item id");
        }

        ObjectId _itemId = new ObjectId(itemId);

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        Item item = itemDAO.findItem(_itemId);

        req.setAttribute("item", item);
        req.getRequestDispatcher("/WEB-INF/jsp/item.jsp").forward(req, resp);
    }
}
