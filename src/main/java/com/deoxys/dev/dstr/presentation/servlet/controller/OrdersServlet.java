package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import org.apache.log4j.Logger;

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
    final static Logger logger = Logger.getLogger(OrdersServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        OrderDAO orderDAO = new OrderDAO(mongo);

        List<Order> orders = orderDAO.getAll();

        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(req, resp);
    }
}
