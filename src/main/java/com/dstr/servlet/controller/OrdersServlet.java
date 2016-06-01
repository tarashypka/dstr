package com.dstr.servlet.controller;

import com.dstr.model.Customer;
import com.dstr.model.Item;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "ShowOrders", urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        if (email != null) {
            customerOrders(request, response);
        }

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        List<Order> orders = new ArrayList<>();

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        if (customer != null) {
            if (customer.getRole().equals("admin")) {
                orders = orderDAO.findAllOrders();
            } else if (customer.getRole().equals("customer")) {
                orders = orderDAO.findCustomerOrders(customer.getEmail());
            }
        }
        request.getSession().setAttribute("orders", orders);
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }

    private void customerOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        List<Order> orders = orderDAO.findCustomerOrders(email);

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/customer/orders.jsp").forward(request, response);
    }
}
