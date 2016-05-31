package com.dstr.servlet.controller.admin.item;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoItemDAO;
import com.dstr.model.item.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "AddItem", urlPatterns = "/admin/items/add")
public class AddItemServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String category = request.getParameter("category");
        double price = Double.parseDouble(request.getParameter("price"));
        String currency = request.getParameter("currency");

        String errtype = null;
        Item item = new Item();
        item.setCategory(category);
        item.setPrice(price);
        item.setCurrency(currency);
        if (category == null || category.equals(""))
            errtype = "category";
        else if (price <= 0.0)
            errtype = "price";
        else if (currency == null || currency.equals(""))
            errtype = "currency";
        if (errtype != null) {
            request.setAttribute("errtype", errtype);
            request.setAttribute("additem", item);
        }
        else {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);
            item = itemDAO.createItem(item);
            request.setAttribute("success", "Новий товар додано");

            // Update Hz cache
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ITEMS").add(item);
        }
        request.getRequestDispatcher("/admin/items").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        request.setAttribute("item", null);

        IMap<String, Object> sessions = Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getMap("my-sessions");

        System.out.println("iter");
        for (String str: sessions.keySet()) {
            System.out.println("key=" + str);
            System.out.println("val=" + sessions.get(str));
        }


        request.getRequestDispatcher("/admin/items").forward(request, response);
    }
}
