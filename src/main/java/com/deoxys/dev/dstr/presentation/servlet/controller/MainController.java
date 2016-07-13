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

    private static final String HOME_LINK;
    private static final String LOGIN_JSP;
    private static final String REGISTER_JSP;
    private static final String LOGOUT_JSP;
    private static final String ITEM_JSP;
    private static final String ALL_ITEMS_JSP;

    static {
        HOME_LINK = "/home";
        LOGIN_JSP = "/WEB-INF/jsp/login.jsp";
        REGISTER_JSP = "/WEB-INF/jsp/register.jsp";
        LOGOUT_JSP = "/WEB-INF/jsp/logout.jsp";
        ITEM_JSP = "/WEB-INF/jsp/item.jsp";
        ALL_ITEMS_JSP = "/WEB-INF/jsp/items.jsp";
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
                req.getRequestDispatcher(ALL_ITEMS_JSP).forward(req, resp);
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
                if (req.getParameter("error") == null) {
                    resp.sendRedirect(req.getContextPath() + HOME_LINK);
                } else req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
                break;
            case "register":
                customerService.register(req);
                if (req.getParameter("error") == null) {
                    resp.sendRedirect(req.getContextPath() + HOME_LINK);
                } else req.getRequestDispatcher(REGISTER_JSP).forward(req, resp);
                break;
            case "logout":
                customerService.logout(req);
                resp.sendRedirect(req.getContextPath() + HOME_LINK);
                break;
            default:
                throw new ServletException("Wrong action");
        }
    }
}
