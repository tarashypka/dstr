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
import java.io.PrintWriter;

/**
 * Created by deoxys on 01.07.16.
 */

@WebServlet(name = "MainController", urlPatterns = "/controller")
public class MainController extends HttpServlet {

    private static CustomerService customerService = new CustomerService();
    private static OrderService orderService = new OrderService();
    private static ItemService itemService = new ItemService();


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long nItems = itemService.count();
        long nOrders = orderService.count();

        PrintWriter writer = resp.getWriter();
        writer.write("There are "
                + nItems + " items and "
                + nOrders + " orders in database");
    }
}
