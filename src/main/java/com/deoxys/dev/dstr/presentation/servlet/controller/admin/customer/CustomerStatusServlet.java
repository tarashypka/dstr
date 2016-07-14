package com.deoxys.dev.dstr.presentation.servlet.controller.admin.customer;

import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
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

        long id = Long.parseLong(req.getParameter("id"));

        if (id < 0) {
            throw new ServletException("Wrong customer id");
        }

        DataSource source = (DataSource) req.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            CustomerDAO customerDAO = new CustomerDAO(source);

            if (customerDAO.swapStatus(id)) {
                logger.info("Customer's with id=" + id + " status was changed");
            } else {
                logger.error("Customer's with id=" + id + " status was not changed");
            }
        } catch (SQLException ex) {
            logger.info("DB Connection/Delete error: " + ex.getMessage());
            throw new ServletException("DB Connection/Delete error: " + ex.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/customers");
    }
}