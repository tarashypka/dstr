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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            Customer selectedCustomer = customerDAO.selectCustomer(email);
            if (selectedCustomer != null) {
                if (selectedCustomer.getPassword().equals(password)) {
                    customer.setRole(selectedCustomer.getRole());
                    request.getSession().setAttribute("customer", selectedCustomer);
                    response.sendRedirect(request.getContextPath() + "/home");
                    logger.info("Customer " + selectedCustomer + " logged in");
                } else {
                    logger.info("Customer " + selectedCustomer + " didn't log in");

                    request.setAttribute("email", email);
                    request.setAttribute("error", "Пароль введено невірно");
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                            .forward(request, response);
                }
            } else {
                logger.info("Customer " + customer + " not found");

                request.setAttribute("error", "Електронну пошту введено невірно");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                        .forward(request, response);
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Login error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("customer") != null) {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath.isEmpty() ? "/" : contextPath);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                .forward(request, response);
    }
}