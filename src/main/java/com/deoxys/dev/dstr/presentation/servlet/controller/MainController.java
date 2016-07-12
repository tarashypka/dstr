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
 * Created by deoxys on 01.07.16.
 */

@WebServlet(name = "MainController", urlPatterns = "/controller")
public class MainController extends HttpServlet {

    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static ItemService itemService = new ItemService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) throw new ServletException("Wrong operation");

        if (action.equals("showItem")) {
            itemService.loadItem(req, resp);
            req.getRequestDispatcher("/WEB-INF/jsp/item.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("In Controller");

    }
}
