package com.dstr.servlet.controller;

import com.dstr.dao.MongoOrderDAO;
import com.dstr.dao.PostgresCustomerDAO;
import com.dstr.model.Customer;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        DataSource source = (DataSource)
                request.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            Customer selectedCustomer = customerDAO.selectCustomer(email);
            customerDAO.closeConnection();
            if (selectedCustomer != null) {
                request.setAttribute("customer", selectedCustomer);

                MongoClient mongo = (MongoClient) request.getServletContext()
                        .getAttribute("MONGO_CLIENT");
                MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

                request.setAttribute("nOrders", orderDAO.ordersAmount(email));
                request.setAttribute("nItems", orderDAO.itemsAmount(email));
                request.getRequestDispatcher("/customer.jsp").forward(request, response);

                customerDAO.closeConnection();
            } else {
                logger.info("Customer with email " + email + " not found");
                throw new ServletException("Customer with email " + email + " not found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Login error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }
}
