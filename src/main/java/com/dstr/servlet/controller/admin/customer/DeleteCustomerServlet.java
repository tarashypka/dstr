package com.dstr.servlet.controller.admin.customer;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */

@WebServlet(name = "DeleteCustomer", urlPatterns = "/customers/delete")
public class DeleteCustomerServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(DeleteCustomerServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);

            if (customerDAO.deleteCustomer(email)) {
                logger.info("Customer with email=" + email + " was deleted");

                // Update session
                List<Customer> customers = (ArrayList)
                        request.getSession().getAttribute("customers");

                customers.remove(new Customer(email));
                request.getSession().setAttribute("customers", customers);
            } else {
                logger.error("Customer with email=" + email + " was not deleted");
            }
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.info("Delete error: " + ex.getMessage());
            throw new ServletException("DB Connection/Delete error: " + ex.getMessage());
        }
        request.getRequestDispatcher("/customers.jsp").forward(request, response);
    }
}