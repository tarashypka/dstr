package com.deoxys.dev.dstr.presentation.servlet.controller.customer;

import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import com.deoxys.dev.dstr.domain.model.Item;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by deoxys on 18.06.16.
 */

@WebServlet(name = "CustomerItemsServlet", urlPatterns = "/customer/items")
public class CustomerItemsServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(CustomerItemsServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Customer customer = (Customer) req.getSession().getAttribute("customer");
        String email = customer.isAdmin() ? req.getParameter("email") : customer.getEmail();

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        Map<Item, Integer> customerItems = orderDAO.findCustomerItems(email);

        req.setAttribute("items", customerItems);
        req.getRequestDispatcher("/WEB-INF/jsp/customer/items.jsp").forward(req, resp);
    }
}