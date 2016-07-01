package com.deoxys.dev.dstr.presentation.servlet.controller.customer;

import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import com.deoxys.dev.dstr.domain.Item;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.equals("")) {
            throw new ServletException("Wrong customer email");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        Map<Item, Integer> customerItems = orderDAO.findCustomerItems(email);

        request.setAttribute("items", customerItems);
        request.getRequestDispatcher("/WEB-INF/jsp/customer/items.jsp")
                .forward(request, response);
    }
}
