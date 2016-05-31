package com.dstr.servlet.controller;

import com.dstr.dao.PostgresCustomerDAO;
import com.dstr.model.customer.Customer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        request.setCharacterEncoding("UTF-8");

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
                    customer.setRole("customer");
                    HttpSession session = request.getSession();
                    session.setAttribute("customer", customer);
                    response.sendRedirect(request.getContextPath() + "/home");
                    logger.info("Customer " + selectedCustomer + " logged in");
                } else {
                    request.setAttribute("errtype", "password");
                    request.getRequestDispatcher("/login.jsp")
                            .forward(request, response);
                    logger.info("Customer " + selectedCustomer + " didn't log in");
                }
            } else {
                request.setAttribute("errtype", "email");
                logger.info("Customer " + customer + " not found");
                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Login error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: "
                    + ex.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}