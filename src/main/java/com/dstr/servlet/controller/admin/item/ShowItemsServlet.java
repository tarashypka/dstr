package com.dstr.servlet.controller.admin.item;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import com.dstr.model.item.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "ShowItems", urlPatterns = "/admin/items")
public class ShowItemsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        // Too heavy, should be moved into Context Initialization ???
        List<Item> items = itemDAO.findAllItems();

        List<Item> hzItems = Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                .getList("ITEMS");

        System.out.println("Hz instances");
        for (HazelcastInstance hzInstance : Hazelcast.getAllHazelcastInstances()) {
            System.out.println(hzInstance.getName());
        }

        // Update Hz cache
        for (Item item : items)
            if ( ! hzItems.contains(item))
                hzItems.add(item);
        hzItems.retainAll(items);

        request.getRequestDispatcher("/admin/items").forward(request, response);
    }
}