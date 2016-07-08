package com.deoxys.dev.dstr.presentation.servlet.controller.admin.customer;

import com.deoxys.dev.dstr.persistence.dao.PostgresCustomerDAO;
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
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "CustomerStatus", urlPatterns = "/customer/status")
public class CustomerStatusServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerStatusServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        DataSource source = (DataSource) req.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);

            if (customerDAO.changeCustomerStatus(email)) {
                logger.info("Customer's with email=" + email + " status was changed");
            } else {
                logger.error("Customer's with email=" + email + " status was not changed");
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            logger.info("DB Connection/Delete error: " + ex.getMessage());
            throw new ServletException("DB Connection/Delete error: " + ex.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/customers");
    }
}