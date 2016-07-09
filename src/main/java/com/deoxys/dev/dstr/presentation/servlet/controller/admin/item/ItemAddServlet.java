package com.deoxys.dev.dstr.presentation.servlet.controller.admin.item;

import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.domain.model.Item;
import org.apache.log4j.Logger;

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

@WebServlet(name = "AddItem", urlPatterns = "/item/add")
public class ItemAddServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemAddServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String category = req.getParameter("category");
        String priceStr = req.getParameter("price");
        String currency = req.getParameter("currency");
        String stockedStr = req.getParameter("status.stocked");
        String reservedStr = req.getParameter("status.reserved");
        String soldStr = req.getParameter("status.sold");

        Item item = new Item();
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

        if (currency == null || currency.equals(""))
            errors.add("Виберіть валюту");
        else item.setCurrency(currency);

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
            MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
            ItemDAO itemDAO = new ItemDAO(mongo);

            if (itemDAO.add(item)) {
                logger.info("New item=" + item + " was successfully added");
                Hazelcast.getHazelcastInstanceByName("HZ_CACHE").getList("ITEMS").add(item);
            } else {
                logger.error("New item=" + item + " was not added");
            }
            resp.sendRedirect(req.getContextPath() + "/items");
        } else {
            req.setAttribute("error", errors.get(0));
            req.setAttribute("item", item);
            req.getRequestDispatcher("/WEB-INF/jsp/itemadd.jsp").forward(req, resp);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("item", null);
        req.getRequestDispatcher("/WEB-INF/jsp/itemadd.jsp").forward(req, resp);
    }
}