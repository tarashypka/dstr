package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.MongoItemDAO;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import com.deoxys.dev.dstr.domain.Customer;
import com.deoxys.dev.dstr.domain.Item;
import com.deoxys.dev.dstr.domain.Order;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by deoxys on 01.06.16.
 */

@WebServlet(name = "OrderStatus", urlPatterns = "/order/status")
public class OrderStatusServlet extends HttpServlet {
    Logger logger = Logger.getLogger(OrderStatusServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String orderId = req.getParameter("id");
        String newStatusName = req.getParameter("status");

        if (orderId == null || orderId.equals("")) {
            throw new ServletException("Wrong order id");
        }

        MongoClient mongo = (MongoClient) req.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        ObjectId _orderId = new ObjectId(orderId);
        Order.OrderStatus oldStatus = orderDAO.findOrderStatus(_orderId);
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(newStatusName);

        Customer customer = (Customer) req.getSession().getAttribute("customer");

        /**
         * Customer is authorized only to:
         *  - renew rejected order
         *  - reject new order
         */
        if (customer != null && customer.isCustomer()) {
            boolean allowed =
                    oldStatus.equals(Order.OrderStatus.REJECTED) &&
                    newStatus.equals(Order.OrderStatus.IN_PROCESS);
            allowed |=
                    oldStatus.equals(Order.OrderStatus.IN_PROCESS) &&
                    newStatus.equals(Order.OrderStatus.REJECTED);
            if ( ! allowed) {
                logger.error("Customer tried to change order status: "
                        + oldStatus.name() + " -> " + newStatus.name());
                throw new ServletException("Not authorized");
            }
        }

        if (orderDAO.updateOrderStatus(_orderId, newStatus)) {
            logger.info("Order's with id=" + orderId + " status was changed: "
                    + oldStatus.name() + " -> " + newStatus.name());

            Order order = orderDAO.findOrder(_orderId);

            // Update order items statuses
            Map<Item, Integer> orderItems = order.getItems();

            boolean succeed = false;
            switch (oldStatus) {
                case REJECTED:
                    switch (newStatus) {
                        case IN_PROCESS:
                            succeed = itemDAO.moveStockedToReserved(orderItems);
                            break;
                        case PROCESSED:
                            succeed = itemDAO.moveStockedToSold(orderItems);
                            break;
                    }
                    break;
                case IN_PROCESS:
                    switch (newStatus) {
                        case REJECTED:
                            succeed = itemDAO.moveReservedtoStocked(orderItems);
                            break;
                        case PROCESSED:
                            succeed = itemDAO.moveReservedToSold(orderItems);
                            break;
                    }
                    break;
                case PROCESSED:
                    switch (newStatus) {
                        case REJECTED:
                            succeed = itemDAO.moveSoldToStocked(orderItems);
                            break;
                        case IN_PROCESS:
                            succeed = itemDAO.moveSoldtoReserved(orderItems);
                            break;
                    }
                    break;
            }
            logger.error((succeed
                    ? "Couldn't change status for one of items "
                    : "Successfully changed status for items ")
                    + "in order with id=" + orderId);
        } else {
            logger.error("Order's with id=" + orderId + " status was not changed");
        }

        if (customer.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        } else {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }
}
