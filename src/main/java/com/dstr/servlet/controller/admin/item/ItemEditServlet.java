package com.dstr.servlet.controller.admin.item;

import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import com.dstr.model.Item;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "ItemEdit", urlPatterns = "/item/edit")
public class ItemEditServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemAddServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id товару");
        }

        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String currency = request.getParameter("currency");
        String leftStr = request.getParameter("left");

        Item item = new Item();
        item.setId(id);
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

            if (itemDAO.updateItem(item) > 0) {
                logger.info("Item " + item + " was successfully edited");
            } else {
                logger.error("Item " + item + " was not edited");
            }
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            request.setAttribute("errtype", errtypes.get(0));
            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/jsp/itemadd.jsp")
                    .forward(request, response);
        }
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
        Item item = itemDAO.findItem(new ObjectId(id));
        request.setAttribute("item", item);
        request.getRequestDispatcher("/WEB-INF/jsp/itemedit.jsp")
                .forward(request, response);
    }
}
