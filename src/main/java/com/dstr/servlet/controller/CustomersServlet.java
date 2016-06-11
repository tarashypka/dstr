package com.dstr.servlet.controller;

import com.dstr.model.Customer;
import com.dstr.dao.PostgresCustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "Customers", urlPatterns = "/customers")
public class CustomersServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataSource source = (DataSource)
                request.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            List<Customer> customers = customerDAO.selectAllCustomers();

            customers.remove(new Customer("dstrdbadmin"));

            request.getSession().setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/jsp/customers.jsp")
                    .forward(request, response);
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Login error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }
}
