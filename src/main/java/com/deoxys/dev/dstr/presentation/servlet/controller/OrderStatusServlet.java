package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Order;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 01.06.16.
 */

@WebServlet(name = "OrderStatus", urlPatterns = "/order/status")
public class OrderStatusServlet extends HttpServlet {
    Logger logger = Logger.getLogger(OrderStatusServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        String newStatusName = req.getParameter("status");

        if (id == null || id.equals("")) {
            throw new ServletException("Wrong order id");
        }

        MongoClient mongo = (MongoClient) req.getServletContext()
                .getAttribute("MONGO_CLIENT");

        OrderDAO orderDAO = new OrderDAO(mongo);
        ItemDAO itemDAO = new ItemDAO(mongo);

        Order.OrderStatus oldStatus = orderDAO.getStatus(id);
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

        if (orderDAO.updateStatus(id, newStatus)) {
            logger.info("Order's with id=" + id + " status was changed: "
                    + oldStatus.name() + " -> " + newStatus.name());

            // Should be reimplemented according to services
            boolean succeed = true;
            logger.error((succeed
                    ? "Couldn't change status for one of items "
                    : "Successfully changed status for items ")
                    + "in order with id=" + id);
        } else {
            logger.error("Order's with id=" + id + " status was not changed");
        }

        if (customer.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        } else {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }
}
