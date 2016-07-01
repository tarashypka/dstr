package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.PostgresCustomerDAO;
import com.deoxys.dev.dstr.domain.Customer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "Customers", urlPatterns = "/customers")
public class CustomersServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomersServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        List<Customer> customers = new ArrayList<>();

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            customers.addAll(customerDAO.selectAllCustomers());
        } catch (SQLException ex) {
            logger.error("PostgreSQL error");
            ex.printStackTrace();
        }
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/jsp/customers.jsp")
                .forward(request, response);
    }
}