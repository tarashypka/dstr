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

/**
 * Created by deoxys on 30.05.16.
 */

@WebServlet(name = "Register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

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
            req.setAttribute("error", error);
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } else {
            DataSource source = (DataSource) req.getServletContext()
                    .getAttribute("POSTGRES_CONNECTION_POOL");

            try {
                PostgresCustomerDao customerDAO = new PostgresCustomerDao(source);
                if (customerDAO.insertCustomer(customer)) {
                    logger.info("New customer " + customer + " was created");
                    resp.sendRedirect(req.getContextPath() + "/login");
                } else {
                    logger.info("New customer " + customer + " wasn't created");
                    req.setAttribute("errtype", "duplicate");
                    req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
                }
                customerDAO.closeConnection();
            } catch (SQLException ex) {
                logger.error("DB Connection/Insert error: " + ex.getMessage());
                throw new ServletException("DB Connection/Insert error: " + ex.getMessage());
            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getSession().getAttribute("customer") != null) {
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath.isEmpty() ? "/" : contextPath);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }
}
