package com.deoxys.dev.dstr.presentation.servlet.controller.customer;

import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 19.06.16.
 */

@WebServlet(name = "CustomerOrdersServlet", urlPatterns = "/customer/orders")
public class CustomerOrdersServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerOrdersServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = (Customer) req.getSession().getAttribute("customer");
        long id = customer.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : customer.getId();

        if (id < 0) {
            throw new ServletException("Wrong customer id");
        }
        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        OrderDAO orderDAO = new OrderDAO(mongo);
        List<Order> orders = orderDAO.getAllForCustomer(customer.getEmail());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/jsp/customer/orders.jsp").forward(req, resp);
    }
}
