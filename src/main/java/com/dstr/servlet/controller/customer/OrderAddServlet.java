package com.dstr.servlet.controller.customer;

import com.dstr.dao.MongoItemDAO;
import com.dstr.dao.MongoOrderDAO;
import com.dstr.model.Customer;
import com.dstr.model.Item;
import com.dstr.model.ItemStatus;
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
 * Created by deoxys on 29.05.16.
 */

@WebServlet(name = "OrderAdd", urlPatterns = "/order/add")
public class OrderAddServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(OrderAddServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        Map<Item, Integer> orderItems = (HashMap)
                request.getSession().getAttribute("orderItems");
        if (orderItems == null) {
            orderItems = new HashMap<>();
        }

        Map<Currency, Double> receipt = (HashMap)
                request.getSession().getAttribute("receipt");
        if (receipt == null) {
            receipt = new HashMap<>();
        }

        if (action.equals("item")) {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                throw new ServletException("Невірний id товару");
            }

            String quantityStr = request.getParameter("quantity");
            if (quantityStr == null || quantityStr.equals("")) {
                request.setAttribute("error", "Скільки одиниць товару?");
                request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                        .forward(request, response);
            }

            int quantity = Integer.parseInt(quantityStr);

            // Check if there are enough items
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);

            Item item = itemDAO.findItem(new ObjectId(id));
            ItemStatus itemStatus = item.getStatus();

            if (itemStatus == null) {
                request.setAttribute("error", "Товар не знайдено");
                request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                        .forward(request, response);
            } else {
                int stocked = itemStatus.getStocked();

                if (quantity > stocked) {
                    request.setAttribute("error", "Недостатньо товарів");
                    request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                            .forward(request, response);
                }

                Currency currency = item.getCurrency();
                double price = item.getPrice();
                double currPrice = price * quantity;

                if (receipt.containsKey(currency)) {
                    double totalPrice = receipt.get(currency);

                    // Customer changed item's quantity
                    if (orderItems.containsKey(item)) {
                        totalPrice -= item.getPrice() * orderItems.get(item);
                    }
                    receipt.put(currency, totalPrice + currPrice);
                } else receipt.put(currency, currPrice);
                request.getSession().setAttribute("receipt", receipt);

                orderItems.put(item, quantity);
                request.getSession().setAttribute("orderItems", orderItems);
                request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                        .forward(request, response);
            }
        } else if (action.equals("order")) {

            Order order = new Order();

            order.setOrderNumber("NULL");
            order.setDate(new Date());

            Customer customer = (Customer) request.getSession().getAttribute("customer");
            order.setCustomer(customer);

            Map<String, Integer> items = new HashMap<>();
            for (Item i : orderItems.keySet()) items.put(i.getId(), orderItems.get(i));

            order.setItems(items);
            order.setReceipt(receipt);
            order.setStatus(Order.OrderStatus.IN_PROCESS);

            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

            if (orderDAO.insertOrder(order) != null) {
                logger.info("New order " + order + " was successfully added");

                // Delete order data from session
                request.getSession().removeAttribute("receipt");
                request.getSession().removeAttribute("orderItems");

                response.sendRedirect(request.getContextPath() + "/orders");
            } else {
                logger.info("New order " + order + " was not added");
                request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                        .forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Update items list before making new order
        Map<Item, Integer> items = new HashMap<>();

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);
        for (Item i : itemDAO.findAllItems()) items.put(i, null);

        request.getSession().setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp")
                .forward(request, response);
    }
}
