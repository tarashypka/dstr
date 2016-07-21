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

    private static CustomerService customerService;
    private static OrderService orderService;
    private static ItemService itemService;

    private static final String
            CUSTOMER_JSP,
            ITEMS_JSP,
            ORDER_JSP,
            ORDERS_JSP,
            NEW_ORDER_JSP;

    static {
        customerService = new CustomerService();
        orderService = new OrderService();
        itemService = new ItemService();
        CUSTOMER_JSP = "/WEB-INF/jsp/customer/customer.jsp";
        ITEMS_JSP = "/WEB-INF/jsp/customer/items.jsp";
        ORDER_JSP = "/WEB-INF/jsp/customer/order.jsp";
        ORDERS_JSP = "/WEB-INF/jsp/customer/orders.jsp";
        NEW_ORDER_JSP = "/WEB-INF/jsp/customer/neworder.jsp";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "showCustomer":
                customerService.loadCustomer(req);
                orderService.loadCustomerActivity(req);
                req.getRequestDispatcher(CUSTOMER_JSP).forward(req, resp);
                break;
            case "showItems":
                itemService.loadCustomerItems(req);
                req.getRequestDispatcher(ITEMS_JSP).forward(req, resp);
                break;
            case "showOrder":
                orderService.loadOrder(req);
                req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
                break;
            case "showOrders":
                orderService.loadCustomerOrders(req);
                req.getRequestDispatcher(ORDERS_JSP).forward(req, resp);
                break;
            case "newOrder":
                itemService.loadItems(req);
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
            case "changeOrderStatus":
                orderService.swapOrderStatus(req);
                req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
                break;
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
                itemService.loadItems(req);
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                break;
            case "dropOrderItem":
                orderService.dropItemFromOrder(req);
                itemService.loadItems(req);
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                break;
            case "newOrder":
                orderService.makeOrder(req);
                if (req.getParameter("error") == null)
                    req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
                else req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}