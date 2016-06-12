package com.dstr.servlet.controller.admin.order;

import com.dstr.dao.MongoItemDAO;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Customer;
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
import java.util.*;

/**
 * Created by deoxys on 01.06.16.
 */

@WebServlet(name = "OrderStatus", urlPatterns = "/order/status")
public class OrderStatusServlet extends HttpServlet {
    Logger logger = Logger.getLogger(OrderStatusServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String newOrderStatusStr = request.getParameter("status");

        if (id == null || id.equals("")) {
            throw new ServletException("Wrong order id");
        }

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");

        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        ObjectId _orderId = new ObjectId(id);
        Order.OrderStatus oldOrderStatus = orderDAO.findOrderStatus(_orderId);
        Order.OrderStatus newOrderStatus = Order.OrderStatus.valueOf(newOrderStatusStr);

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        // Customer is only authorized to renew rejected order
        if (customer != null && customer.isCustomer()) {
            boolean allowed =
                    oldOrderStatus.equals(Order.OrderStatus.REJECTED) &&
                    newOrderStatus.equals(Order.OrderStatus.IN_PROCESS);
            if ( ! allowed) {
                logger.error("Customer tried to change order status: "
                        + oldOrderStatus.name() + " -> " + newOrderStatus.name());
                throw new ServletException("Not authorized");
            }
        }

        if (orderDAO.updateOrderStatus(_orderId, newOrderStatus)) {
            logger.info("Order's with id=" + id + " status was changed: "
                    + oldOrderStatus.name() + " -> " + newOrderStatus.name());

            List<Order> orders = (ArrayList)
                    request.getSession().getAttribute("orders");

            Order order = orders.get(orders.indexOf(new Order(id)));

            // Update order items statuses
            Map<String, Integer> itemsInReceipt = order.getItems();

            for (String itemId : itemsInReceipt.keySet()) {
                int quantity = itemsInReceipt.get(itemId);
                ObjectId _itemId = new ObjectId(itemId);

                boolean succeed = false;
                switch (oldOrderStatus) {
                    case REJECTED:
                        switch (newOrderStatus) {
                            case IN_PROCESS:
                                succeed = itemDAO.moveStockedToReserved(_itemId, quantity);
                                break;
                            case PROCESSED:
                                succeed = itemDAO.moveStockedToSold(_itemId, quantity);
                                break;
                        }
                        break;
                    case IN_PROCESS:
                        switch (newOrderStatus) {
                            case REJECTED:
                                succeed = itemDAO.moveReservedtoStocked(_itemId, quantity);
                                break;
                            case PROCESSED:
                                succeed = itemDAO.moveReservedToSold(_itemId, quantity);
                                break;
                        }
                        break;
                    case PROCESSED:
                        switch (newOrderStatus) {
                            case REJECTED:
                                succeed = itemDAO.moveSoldToStocked(_itemId, quantity);
                                break;
                            case IN_PROCESS:
                                succeed = itemDAO.moveSoldtoReserved(_itemId, quantity);
                                break;
                        }
                        break;
                }
                if (succeed) {
                    logger.info("Successfully changed order status"
                            + " for item with id=" + itemId
                            + ": " + oldOrderStatus.name()
                            + " -> " + newOrderStatus.name());
                } else {
                    logger.error("Couldn't change order status"
                            + " for item with id=" + itemId
                            + ": " + oldOrderStatus.name()
                            + " -> " + newOrderStatus.name());
                }
            }

            // Update session
            orders.remove(order);
            order.setStatus(newOrderStatus);
            orders.add(order);

            request.getSession().setAttribute("orders", orders);
        } else {
            logger.error("Order's with id=" + id + " status was not changed");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp")
                .forward(request, response);
    }
}
