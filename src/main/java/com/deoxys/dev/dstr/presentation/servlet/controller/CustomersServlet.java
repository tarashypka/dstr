package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.PostgresCustomerDao;
import com.deoxys.dev.dstr.domain.model.Customer;
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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DataSource source = (DataSource)
                req.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        List<Customer> customers = new ArrayList<>();

        try {
            PostgresCustomerDao customerDAO = new PostgresCustomerDao(source);
            customers.addAll(customerDAO.selectAllCustomers());
        } catch (SQLException ex) {
            logger.error("DB Connection/Select error:" + ex.getMessage());
            throw new ServletException("DB Connection/Select error:" + ex.getMessage());
        }
        req.setAttribute("customers", customers);
        req.getRequestDispatcher("/WEB-INF/jsp/customers.jsp").forward(req, resp);
    }
}