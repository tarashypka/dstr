package com.dstr.servlet.controller.admin.item;

import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import com.dstr.model.Item;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "AddItem", urlPatterns = "/item/add")
public class ItemAddServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemAddServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String currency = request.getParameter("currency");
        String leftStr = request.getParameter("left");

        Item item = new Item();
        item.setCategory(category);

        double price;
        int left;

        List<String> errtypes = new ArrayList<>();

        if (category == null || category.equals(""))
            errtypes.add("category");

        if (priceStr == null || priceStr.equals("")) {
            errtypes.add("priceNull");
            item.setPrice(-1.0);
        } else if ((price = Double.parseDouble(priceStr)) <= 0.0) {
            errtypes.add("priceNegative");
            item.setPrice(-1.0);
        } else item.setPrice(price);

        if (currency == null || currency.equals(""))
            errtypes.add("currency");
        else item.setCurrency(currency);

        if (leftStr == null || leftStr.equals("")) {
            errtypes.add("leftNull");
            item.setLeft(-1);
        } else if ((left = Integer.parseInt(leftStr)) < 0) {
            errtypes.add("leftNegative");
            item.setLeft(-1);
        } else item.setLeft(left);

        if (errtypes.isEmpty()) {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);
            item = itemDAO.createItem(item);

            logger.info("New item " + item + " successfully added");
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            request.setAttribute("errtype", errtypes.get(0));
            request.setAttribute("item", item);
            request.getRequestDispatcher("/itemadd.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("item", null);
        request.getRequestDispatcher("/itemadd.jsp").forward(request, response);
    }
}