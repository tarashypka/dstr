package com.dstr.servlet.controller;

import com.dstr.model.Customer;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "ShowOrders", urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        if (customer.isCustomer()) {
            customerOrders(request, response, customer.getEmail());
        } else if (customer.isAdmin()) {
            if (request.getParameter("email") != null) {
                customerOrders(request, response, request.getParameter("email"));
            } else {
                allOrders(request, response);
            }
        }
    }

    /* Supposed there could be a lot of orders at all,
        then it's preferred to save them in Hz instance
        and not to download all of them after
        each time admin requests /orders
     */
    private void allOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        List<Order> orders = orderDAO.findAllOrders();
        request.getSession().setAttribute("orders", orders);
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }

    /* Supposed customer's orders list is not that big
        and admin can change each order status,
        then it's preferred to save them in customer's session
        and update the list every time customer requests /orders
     */
    private void customerOrders(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException {

        if (email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        List<Order> orders = orderDAO.findCustomerOrders(email);

        request.getSession().setAttribute("orders", orders);
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
