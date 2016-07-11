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

@WebServlet(name = "MainController", urlPatterns = "/controller/customer")
public class CustomerController extends HttpServlet {

    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) throw new ServletException("Wrong operation");

        if (action.equals("showItems")) {
            itemService.loadCustomerItems(req, resp);
            req.getRequestDispatcher("/WEB-INF/jsp/customer/items.jsp").forward(req, resp);
        }
        else if (action.equals("showOrders")) {
            orderService.loadCustomerOrders(req, resp);
            req.getRequestDispatcher("/WEB-INF/jsp/customer/orders.jsp").forward(req, resp);
        }
        else if (action.equals("makeOrder")) {
            itemService.loadItems(req, resp);
            req.getRequestDispatcher("/WEB-INF/jsp/orderadd.jsp").forward(req, resp);
        }
        else throw new ServletException("Wrong action");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) throw new ServletException("Wrong operation");

        if (action.equals("addOrderItem")) {
            orderService.addItemToOrder(req, resp);
            req.setAttribute("action", "makeOrder");
        }
        else if (action.equals("makeOrder")) {
            orderService.makeOrder(req, resp);
            req.setAttribute("action", "showOrders");
        }
        else if (action.equals("changeOrderStatus")) {
            orderService.changeOrderStatus(req, resp);
            req.setAttribute("action", "showOrders");
        }
        else throw new ServletException("Wrong action");
        req.getRequestDispatcher("/controller/customer").forward(req, resp);
    }
}
