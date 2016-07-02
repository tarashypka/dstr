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

/**
 * Created by deoxys on 30.05.16.
 */

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        DataSource source = (DataSource) req.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            Customer selectedCustomer = customerDAO.selectCustomer(email);
            if (selectedCustomer != null) {
                if (selectedCustomer.getPassword().equals(password)) {
                    customer.setRole(selectedCustomer.getRole());
                    req.getSession().setAttribute("customer", selectedCustomer);
                    resp.sendRedirect(req.getContextPath() + "/home");
                    logger.info("Customer " + selectedCustomer + " logged in");
                } else {
                    logger.info("Customer " + selectedCustomer + " didn't log in");

                    req.setAttribute("email", email);
                    req.setAttribute("error", "Пароль введено невірно");
                    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                }
            } else {
                logger.info("Customer " + customer + " not found");
                req.setAttribute("error", "Електронну пошту введено невірно");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            logger.error("DB Connection/Select error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getSession().getAttribute("customer") != null) {
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath.isEmpty() ? "/" : contextPath);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }
}