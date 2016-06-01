package com.dstr.servlet.controller;

import com.dstr.dao.MongoItemDAO;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Customer;
import com.dstr.model.Item;
import com.mongodb.MongoClient;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        if (email != null) {
            customerItems(request, response);
        }

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        Map<Item, Integer> items = new HashMap<>();

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        if (customer != null && customer.getRole().equals("customer")) {
            MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);
            items = orderDAO.findCustomerItems(customer.getEmail());
        } else {
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);
            for (Item i : itemDAO.findAllItems()) items.put(i, null);
        }
        request.getSession().setAttribute("items", items);
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }

    private void customerItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        Map<Item, Integer> items = orderDAO.findCustomerItems(email);

        request.setAttribute("items", items);
        request.getRequestDispatcher("/customer/items.jsp").forward(request, response);
    }
}