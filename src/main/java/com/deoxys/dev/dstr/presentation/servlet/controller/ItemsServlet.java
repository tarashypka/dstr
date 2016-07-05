package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.persistence.dao.MongoItemDao;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "Items", urlPatterns = "/items")
public class ItemsServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ItemsServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /**
         *  Hz ITEMS list is created on application context initialization.
         *  All items are in Hz, and are up-to-date,
         *  since Hz cache is affected each time items collection was updated.
         */
        HazelcastInstance hz = Hazelcast.getHazelcastInstanceByName("HZ_CACHE");
        List<Item> items;

        if (hz == null) {
            logger.error("Hazelcast: HZ_CACHE was not found");
            throw new ServletException("Cache was not properly configured");
        }
        if ((items = hz.getList("ITEMS")) == null) {
            logger.error("Hazelcast: ITEMS list was not found in HZ_CACHE");
            throw new ServletException("Cache was not properly configured");
        }

        if (items.size() == 0) {
            // Fetch all items to HZ_CACHE
            MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
            MongoItemDao itemDAO = new MongoItemDao(mongo);

            items.addAll(itemDAO.findAllItems());
        }
        req.setAttribute("items", items);
        req.getRequestDispatcher("/WEB-INF/jsp/items.jsp").forward(req, resp);
    }
}