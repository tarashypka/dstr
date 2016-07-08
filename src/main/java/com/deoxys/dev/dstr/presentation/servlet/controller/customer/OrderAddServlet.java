package com.deoxys.dev.dstr.presentation.servlet.controller.customer;

import com.deoxys.dev.dstr.persistence.dao.MongoItemDAO;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;
import com.deoxys.dev.dstr.domain.model.Order;
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

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        Map<Item, Integer> orderItems = (HashMap) req.getSession().getAttribute("orderItems");
        if (orderItems == null) {
            orderItems = new HashMap<>();
        }

        Map<Currency, Double> receipt = (HashMap) req.getSession().getAttribute("receipt");
        if (receipt == null) {
            receipt = new HashMap<>();
        }

        if (action.equals("item")) {
            String id = req.getParameter("id");
            if (id == null || id.equals("")) {
                throw new ServletException("Невірний id товару");
            }

            String quantityStr = req.getParameter("quantity");
            if (quantityStr == null || quantityStr.equals("")) {
                req.setAttribute("error", "Скільки одиниць товару?");
                req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
            }

            int quantity = Integer.parseInt(quantityStr);

            // Check if there are enough items
            MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
            MongoItemDAO itemDAO = new MongoItemDAO(mongo);

            Item item = itemDAO.findItem(new ObjectId(id));
            ItemStatus itemStatus = item.getStatus();

            if (itemStatus == null) {
                req.setAttribute("error", "Товар не знайдено");
                req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
            } else {
                int stocked = itemStatus.getStocked();

                if (quantity > stocked) {
                    req.setAttribute("error", "Недостатньо товарів");
                    req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
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
                req.getSession().setAttribute("receipt", receipt);

                orderItems.put(item, quantity);
                req.getSession().setAttribute("orderItems", orderItems);
                req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
            }
        } else if (action.equals("order")) {

            Order order = new Order();

            order.setOrderNumber("NULL");
            order.setDate(new Date());

            Customer customer = (Customer) req.getSession().getAttribute("customer");
            order.setCustomer(customer);

            Map<Item, Integer> items = new HashMap<>();
            for (Item i : orderItems.keySet()) items.put(i, orderItems.get(i));

            order.setItems(items);
            order.setReceipt(receipt);
            order.setStatus(Order.OrderStatus.IN_PROCESS);

            MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
            MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

            if (orderDAO.insertOrder(order) != null) {
                logger.info("New order " + order + " was successfully added");

                // Delete order data from session
                req.getSession().removeAttribute("receipt");
                req.getSession().removeAttribute("orderItems");

                resp.sendRedirect(req.getContextPath() + "/orders");
            } else {
                logger.info("New order " + order + " was not added");
                req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Update items list before making new order
        Map<Item, Integer> items = new HashMap<>();

        MongoClient mongo = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);
        for (Item i : itemDAO.findAllItems()) items.put(i, null);

        req.getSession().setAttribute("items", items);
        req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
    }
}
