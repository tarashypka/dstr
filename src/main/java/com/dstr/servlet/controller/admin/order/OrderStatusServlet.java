package com.dstr.servlet.controller.admin.order;

import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Order;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 01.06.16.
 */
@WebServlet(name = "OrderStatus", urlPatterns = "/order/status")
public class OrderStatusServlet extends HttpServlet {
    Logger logger = Logger.getLogger(OrderStatusServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String statusStr = request.getParameter("status");

        if (id == null || id.equals("")) {
            throw new ServletException("Wrong order id");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        ObjectId _id = new ObjectId(id);
        Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr);

        System.out.println("statusStr==" + statusStr);
        System.out.println("status==" + status.getValue());

        if (orderDAO.changeOrderStatus(_id, status) > 0) {
            logger.info("Order's with id=" + id + " status was changed");

            // Update session
            List<Order> orders = (ArrayList)
                    request.getSession().getAttribute("orders");

            Order order = orders.get(orders.indexOf(new Order(id)));
            orders.remove(order);
            order.setStatus(status);
            orders.add(order);

            request.getSession().setAttribute("orders", orders);
        } else {
            logger.error("Order's with id=" + id + " status was not changed");
        }
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
