package com.dstr.servlet.controller.customer.order;

import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.customer.Customer;
import com.dstr.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "ShowOrders", urlPatterns = "/customer/orders")
public class ShowOrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        Customer customer = new Customer();
        customer.setName((String) request.getSession().getAttribute("name"));
        customer.setSurname((String) request.getSession().getAttribute("surname"));
        customer.setEmail((String) request.getSession().getAttribute("email"));

        List<Order> orders = orderDAO.findAllCustomerOrders(customer);

        List<Order> hzOrders = Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                .getList("ORDERS");

        // Update Hz cache
        for (Order order : orders)
            if ( ! hzOrders.contains(order))
                hzOrders.add(order);
        hzOrders.retainAll(orders);

        request.getRequestDispatcher("/customer/orders").forward(request, response);
    }
}
