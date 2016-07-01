package com.deoxys.dev.dstr.presentation.servlet.controller.admin.customer;

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

@WebServlet(name = "CustomerStatus", urlPatterns = "/customer/status")
public class CustomerStatusServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerStatusServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);

            if (customerDAO.changeCustomerStatus(email)) {
                logger.info("Customer's with email=" + email + " status was changed");

                // Update session
                List<Customer> customers = (ArrayList)
                        request.getSession().getAttribute("customers");

                Customer customer = customers.get(customers.indexOf(new Customer(email)));
                customers.remove(customer);
                customer.setEnabled( ! customer.isEnabled());
                customers.add(customer);

                request.getSession().setAttribute("customers", customers);
            } else {
                logger.error("Customer's with email=" + email + " status was not changed");
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.info("Delete error: " + ex.getMessage());
            throw new ServletException("DB Connection/Delete error: " + ex.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/jsp/customers.jsp")
                .forward(request, response);
    }
}