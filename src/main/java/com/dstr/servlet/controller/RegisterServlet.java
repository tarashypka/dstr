package com.dstr.servlet.controller;

import com.dstr.dao.PostgresCustomerDAO;
import com.dstr.model.Customer;
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

@WebServlet(name = "Register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String error = null;
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);
        if (name == null || name.equals(""))
            error = "Введіть ім'я";
        else if (surname == null || surname.equals(""))
            error = "Введіть прізвище";
        else if (email == null || ! email.matches("\\S+@\\w+\\.\\w+"))
            error = "Електронну пошту введено невірно";
        else if (password == null || password.length() < 8)
            error = "Занадто короткий пароль";
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            DataSource source = (DataSource) request.getServletContext()
                    .getAttribute("POSTGRES_CONNECTION_POOL");

            try {
                PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
                if (customerDAO.insertCustomer(customer)) {
                    logger.info("New customer " + customer + " was created");
                    response.sendRedirect(request.getContextPath() + "/login");
                } else {
                    logger.info("New customer " + customer + " wasn't created");
                    request.setAttribute("errtype", "duplicate");
                    request.getRequestDispatcher("/register.jsp")
                            .forward(request, response);
                }
                customerDAO.closeConnection();
            } catch (SQLException ex) {
                logger.error("Register: " + ex.getMessage());
                ex.printStackTrace();
                throw new ServletException("DB Connection/Insert error: "
                        + ex.getMessage());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("customer") != null) {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath.isEmpty() ? "/" : contextPath);
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
