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

@WebServlet(name = "AdminController", urlPatterns = "/controller/admin")
public class AdminController extends HttpServlet {

    private static CustomerService customerService;
    private static OrderService orderService;
    private static ItemService itemService;

    private static final String
            CUSTOMER_JSP,
            CUSTOMERS_JSP,
            ITEM_JSP,
            NEW_ITEM_JSP,
            ORDER_JSP,
            ORDERS_JSP;

    static {
        customerService = new CustomerService();
        orderService = new OrderService();
        itemService = new ItemService();
        CUSTOMER_JSP = "/WEB-INF/jsp/customer/customer.jsp";
        CUSTOMERS_JSP = "/WEB-INF/jsp/admin/customers.jsp";
        ITEM_JSP = "/WEB-INF/jsp/item.jsp";
        NEW_ITEM_JSP = "/WEB-INF/jsp/admin/newitem.jsp";
        ORDER_JSP = "/WEB-INF/jsp/customer/order.jsp";
        ORDERS_JSP = "/WEB-INF/jsp/admin/orders.jsp";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "showCustomers":
                customerService.loadCustomers(req);
                req.getRequestDispatcher(CUSTOMERS_JSP).forward(req, resp);
                break;
            case "newItem":
                req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            case "editItem":
                itemService.loadItem(req);
                req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            case "deleteItem":
                itemService.deleteItem(req);
                resp.sendRedirect(req.getHeader("referer"));
                break;
            case "showOrders":
                orderService.loadOrders(req);
                req.getRequestDispatcher(ORDERS_JSP).forward(req, resp);
                break;
            case "swapCustomerStatus":
                customerService.swapCustomerStatus(req);
                customerService.loadCustomer(req);
                orderService.loadCustomerActivity(req);
                req.getRequestDispatcher(CUSTOMER_JSP).forward(req, resp);
                break;
            case "changeOrderStatus":
                orderService.changeOrderStatus(req);
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
            case "newItem":
                itemService.addItem(req);
                if (req.getAttribute("error") == null)
                    req.getRequestDispatcher(ITEM_JSP).forward(req, resp);
                else req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            case "editItem":
                itemService.editItem(req);
                if (req.getAttribute("error") == null)
                    req.getRequestDispatcher(ITEM_JSP).forward(req, resp);
                else req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}
