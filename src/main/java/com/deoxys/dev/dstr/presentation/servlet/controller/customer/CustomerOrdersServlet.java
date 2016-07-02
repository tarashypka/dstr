package com.deoxys.dev.dstr.presentation.servlet.controller.customer;

import com.deoxys.dev.dstr.domain.Customer;
import com.deoxys.dev.dstr.domain.Order;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
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
        String email = customer.isAdmin()
                ? req.getParameter("email")
                : customer.getEmail();

        if (email == null || email.equals("")) {
            logger.error("Wrong customer's email");
            throw new ServletException("Wrong customer's email");
        }

        MongoClient mongo = (MongoClient) req.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        List<Order> orders = orderDAO.findCustomerOrders(email);

        req.getSession().setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/jsp/customer/orders.jsp")
                .forward(req, resp);
    }
}
