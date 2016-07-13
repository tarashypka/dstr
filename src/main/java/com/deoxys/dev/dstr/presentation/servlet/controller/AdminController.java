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
 * Created by deoxys on 12.07.16.
 */

@WebServlet(name = "AdminController", urlPatterns = "/controller/admin")
public class AdminController extends HttpServlet {

    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static ItemService itemService = new ItemService();

    private static String
            MAIN_CONTROLLER,
            ALL_ORDERS_JSP,
            CUSTOMER_JSP,
            ALL_CUSTOMERS_JSP,
            NEW_ITEM_JSP,
            EDIT_ITEM_JSP;

    static {
        MAIN_CONTROLLER = "/controller";
        ALL_ORDERS_JSP = "/WEB-INF/jsp/orders.jsp";
        CUSTOMER_JSP = "/WEB-INF/jsp/customer.jsp";
        ALL_CUSTOMERS_JSP = "/WEB-INF/jsp/customers.jsp";
        NEW_ITEM_JSP = "/WEB-INF/jsp/newitem.jsp";
        EDIT_ITEM_JSP = "/WEB-INF/jsp/edititem.jsp";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "showOrders":
                orderService.loadOrders(req);
                req.getRequestDispatcher(ALL_ORDERS_JSP).forward(req, resp);
                break;
            case "showCustomer":
                customerService.loadCustomer(req);
                req.getRequestDispatcher(CUSTOMER_JSP).forward(req, resp);
                break;
            case "showCustomers":
                customerService.loadCustomers(req);
                req.getRequestDispatcher(ALL_CUSTOMERS_JSP).forward(req, resp);
                break;
            case "newItem":
                req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            case "editItem":
                itemService.loadItem(req);
                req.getRequestDispatcher(EDIT_ITEM_JSP).forward(req, resp);
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
                if (req.getParameter("error") == null) {
                    req.setAttribute("action", "showItems");
                    req.getRequestDispatcher(MAIN_CONTROLLER).forward(req, resp);
                } else req.getRequestDispatcher(NEW_ITEM_JSP).forward(req, resp);
                break;
            case "editItem":
                itemService.editItem(req);
                if (req.getParameter("error") == null) {
                    req.setAttribute("action", "showItems");
                    req.getRequestDispatcher(MAIN_CONTROLLER).forward(req, resp);
                } else req.getRequestDispatcher(EDIT_ITEM_JSP).forward(req, resp);
                break;
            case "changeCustomerStatus":
                customerService.swapCustomerStatus(req);
                customerService.loadCustomers(req);
                req.getRequestDispatcher(ALL_CUSTOMERS_JSP).forward(req, resp);
            case "changeOrderStatus":
                orderService.changeOrderStatus(req);
                orderService.loadOrders(req);
                req.getRequestDispatcher(ALL_ORDERS_JSP).forward(req, resp);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}
