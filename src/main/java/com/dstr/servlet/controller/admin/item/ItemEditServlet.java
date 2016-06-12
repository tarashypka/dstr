package com.dstr.servlet.controller.admin.item;

import com.dstr.model.ItemStatus;
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

        String itemId = request.getParameter("id");
        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Невірний id товару");
        }

        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String currency = request.getParameter("currency");
        String stockedStr = request.getParameter("status.stocked");
        String reservedStr = request.getParameter("status.reserved");
        String soldStr = request.getParameter("status.sold");

        Item item = new Item();
        item.setId(itemId);
        item.setCategory(category);

        List<String> errors = new ArrayList<>();

        if (category == null || category.equals(""))
            errors.add("Введіть категорію");

        double price = -1.0;

        if (priceStr == null || priceStr.equals("")) {
            errors.add("Введіть ціну");
        } else if ((price = Double.parseDouble(priceStr)) <= 0.0) {
            errors.add("Ціну введено невірно");
        } else item.setPrice(price);

        if (currency == null || currency.equals("")) {
            errors.add("Виберіть валюту");
        } else item.setCurrency(currency);

        int stocked = -1, reserved = -1, sold = -1;

        if (stockedStr == null || stockedStr.equals("")) {
            stocked = 0;
        } else if ((stocked = Integer.parseInt(stockedStr)) < 0) {
            errors.add("Поле 'залишилось' введено невірно");
        }

        if (reservedStr == null || reservedStr.equals("")) {
            reserved = 0;
        } else if ((reserved = Integer.parseInt(reservedStr)) < 0) {
            errors.add("Поле 'зарезервовано' введено невірно");
        }

        if (soldStr == null || soldStr.equals("")) {
            sold = 0;
        } else if ((sold = Integer.parseInt(soldStr)) < 0) {
            errors.add("Поле 'продано' введено невірно");
        }
        item.setStatus(new ItemStatus(stocked, reserved, sold));

        if (errors.isEmpty()) {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);

            if (itemDAO.updateItem(item)) {
                logger.info("Товар " + item + " успішно редаговано");
            } else {
                logger.error("Товар " + item + " не редаговано");
            }
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            request.setAttribute("error", errors.get(0));
            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/jsp/itemedit.jsp")
                    .forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String itemId = request.getParameter("id");
        if (itemId == null || itemId.equals("")) {
            throw new ServletException("Невірний id товару");
        }
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);
        Item item = itemDAO.findItem(new ObjectId(itemId));
        request.setAttribute("item", item);
        request.getRequestDispatcher("/WEB-INF/jsp/itemedit.jsp")
                .forward(request, response);
    }
}
