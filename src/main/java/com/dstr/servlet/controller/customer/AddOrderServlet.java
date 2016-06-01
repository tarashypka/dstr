package com.dstr.servlet.controller.customer;

import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Customer;
import com.dstr.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "AddOrder", urlPatterns = "/orders/add")
public class AddOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Order order = new Order();

        // Add orderNumber

        order.setDate(new Date());

        // Session specific
        Customer customer = new Customer();
        customer.setName((String) request.getSession().getAttribute("name"));
        customer.setSurname((String) request.getSession().getAttribute("surname"));
        customer.setEmail((String) request.getSession().getAttribute("email"));
        order.setCustomer(customer);

        order.setItems((HashMap) request.getAttribute("items"));

        // Add receipt

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);
        order = orderDAO.createOrder(order);

        request.setAttribute("success", "Нове замовлення додано");

        // Update Hz cache
        Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ORDERS").add(order);

        request.getRequestDispatcher("/customer/orders").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("items", null);
        request.getRequestDispatcher("/customer/makeOrder").forward(request, response);
    }
}
