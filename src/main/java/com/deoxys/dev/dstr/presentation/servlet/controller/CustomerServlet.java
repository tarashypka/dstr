package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.PostgresCustomerDAO;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by deoxys on 01.06.16.
 */

@WebServlet(name = "Customer", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        DataSource source = (DataSource)
                req.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            Customer customer = customerDAO.selectCustomer(email);
            customerDAO.closeConnection();
            if (customer != null) {
                MongoClient mongo = (MongoClient) req.getServletContext()
                        .getAttribute("MONGO_CLIENT");
                MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

                customer.setOrders(orderDAO.findCustomerOrders(customer.getEmail()));
                customer.setItems(orderDAO.findCustomerItems(customer.getEmail()));

                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(req, resp);
            } else {
                logger.info("Customer with email " + email + " not found");
                throw new ServletException("Customer with email " + email + " not found");
            }
        } catch (SQLException ex) {
            logger.error("DB Connection/Select error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }
}
