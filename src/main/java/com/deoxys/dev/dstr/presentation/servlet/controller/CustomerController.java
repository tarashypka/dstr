package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.domain.service.CustomerService;
import com.deoxys.dev.dstr.domain.service.ItemService;
import com.deoxys.dev.dstr.domain.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 11.07.16.
 */

@WebServlet(name = "CustomerController", urlPatterns = "/controller/customer")
public class CustomerController extends HttpServlet {

    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static ItemService itemService = new ItemService();

    private static String
            CUSTOMER_JSP,
            CUSTOMER_ITEMS_JSP,
            CUSTOMER_ORDER_JSP,
            CUSTOMER_ORDERS_JSP,
            NEW_ORDER_JSP;

    static {
        CUSTOMER_JSP = "/WEB-INF/jsp/customer.jsp";
        CUSTOMER_ITEMS_JSP = "/WEB-INF/jsp/customer/items.jsp";
        CUSTOMER_ORDER_JSP = "/WEB-INF/jsp/customer/order.jsp";
        CUSTOMER_ORDERS_JSP = "/WEB-INF/jsp/customer/orders.jsp";
        NEW_ORDER_JSP = "/WEB-INF/jsp/neworder.jsp";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "showCustomer":
                customerService.loadCustomer(req);
                req.getRequestDispatcher(CUSTOMER_JSP).forward(req, resp);
                break;
            case "showItems":
                itemService.loadCustomerItems(req);
                req.getRequestDispatcher(CUSTOMER_ITEMS_JSP).forward(req, resp);
                break;
            case "showOrder":
                orderService.loadCustomerOrder(req);
                req.getRequestDispatcher(CUSTOMER_ORDER_JSP).forward(req, resp);
                break;
            case "showOrders":
                orderService.loadCustomerOrders(req);
                req.getRequestDispatcher(CUSTOMER_ORDERS_JSP).forward(req, resp);
                break;
            case "newOrder":
                itemService.loadItems(req);
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
            default:
                throw new ServletException("Wrong action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "addOrderItem":
                orderService.addItemToOrder(req);
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                break;
            case "newOrder":
                orderService.makeOrder(req);
                if (req.getParameter("error") == null) {
                    orderService.loadCustomerOrders(req);
                    req.getRequestDispatcher(CUSTOMER_ORDERS_JSP).forward(req, resp);
                } else req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                break;
            case "changeOrderStatus":
                orderService.swapOrderStatus(req);
                orderService.loadCustomerOrders(req);
                req.getRequestDispatcher(CUSTOMER_ORDERS_JSP).forward(req, resp);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}