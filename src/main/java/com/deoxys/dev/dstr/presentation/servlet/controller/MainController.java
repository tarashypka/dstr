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

    private static CustomerService customerService;
    private static OrderService orderService;
    private static ItemService itemService;

    private static final String
            LOGIN_JSP,
            REGISTER_JSP,
            LOGOUT_JSP,
            ITEM_JSP,
            ITEMS_JSP;

    static {
        customerService = new CustomerService();
        orderService = new OrderService();
        itemService = new ItemService();
        LOGIN_JSP = "/WEB-INF/jsp/login.jsp";
        REGISTER_JSP = "/WEB-INF/jsp/register.jsp";
        LOGOUT_JSP = "/WEB-INF/jsp/logout.jsp";
        ITEM_JSP = "/WEB-INF/jsp/item.jsp";
        ITEMS_JSP = "/WEB-INF/jsp/items.jsp";
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "login":
                req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
                break;
            case "register":
                req.getRequestDispatcher(REGISTER_JSP).forward(req, resp);
                break;
            case "logout":
                req.getRequestDispatcher(LOGOUT_JSP).forward(req, resp);
                break;
            case "showItem":
                itemService.loadItem(req);
                req.getRequestDispatcher(ITEM_JSP).forward(req, resp);
                break;
            case "showItems":
                itemService.loadItems(req);
                req.getRequestDispatcher(ITEMS_JSP).forward(req, resp);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) throw new ServletException("Wrong operation");

        switch (action) {
            case "login":
                customerService.login(req);
                if (req.getAttribute("error") == null)
                    resp.sendRedirect(req.getContextPath());
                else req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
                break;
            case "register":
                customerService.register(req);
                if (req.getAttribute("error") == null)
                    resp.sendRedirect(req.getContextPath());
                else req.getRequestDispatcher(REGISTER_JSP).forward(req, resp);
                break;
            case "logout":
                customerService.logout(req);
                if (req.getAttribute("error") == null)
                    resp.sendRedirect(req.getContextPath());
                else resp.sendRedirect(req.getHeader("referer"));
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}
