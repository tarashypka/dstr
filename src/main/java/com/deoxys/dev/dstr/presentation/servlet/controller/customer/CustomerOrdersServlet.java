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

@WebServlet(name = "CustomerOrdersServlet")
public class CustomerOrdersServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerOrdersServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        String email = customer.isAdmin()
                ? customer.getEmail()
                : request.getParameter("email");

        if (email == null || email.equals("")) {
            logger.error("Wrong customer's email");
            throw new ServletException("Wrong customer's email");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        List<Order> orders = orderDAO.findCustomerOrders(email);

        request.getSession().setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp")
                .forward(request, response);
    }
}
