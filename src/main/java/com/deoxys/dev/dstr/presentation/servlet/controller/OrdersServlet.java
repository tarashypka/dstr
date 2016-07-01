package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.domain.Order;
import com.mongodb.MongoClient;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);
        List<Order> orders = orderDAO.findAllOrders();

        request.getSession().setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp")
                .forward(request, response);
    }
}
