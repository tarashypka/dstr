package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.mongodb.MongoClient;
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
 * Created by deoxys on 01.06.16.
 */

@WebServlet(name = "Customer", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));

        if (id < 0) {
            throw new ServletException("Wrong customer id");
        }

        DataSource source = (DataSource)
                req.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        try {
            CustomerDAO customerDAO = new CustomerDAO(source);
            Customer customer = customerDAO.get(id);
            customerDAO.closeConnection();
            if (customer != null) {
                MongoClient mongo = (MongoClient) req.getServletContext()
                        .getAttribute("MONGO_CLIENT");
                OrderDAO orderDAO = new OrderDAO(mongo);
                ItemDAO itemDAO = new ItemDAO(mongo);

                customer.setOrders(orderDAO.getAllForCustomer(id));
                customer.setItems(itemDAO.getAllForCustomer(id));

                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(req, resp);
            } else {
                logger.info("Customer with id=" + id + " not found");
                throw new ServletException("Customer with id=" + id + " not found");
            }
        } catch (SQLException ex) {
            logger.error("DB Connection/Select error: " + ex.getMessage());
            throw new ServletException("DB Connection/Select error: " + ex.getMessage());
        }
    }
}
