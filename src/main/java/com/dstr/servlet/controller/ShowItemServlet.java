package com.dstr.servlet.controller;

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
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "ShowItem", urlPatterns = "/item")
public class ShowItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id товару");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);
        Item item = itemDAO.findItem(new ObjectId(id));

        request.setAttribute("item", item);
        request.getRequestDispatcher("/item").forward(request, response);
    }
}
