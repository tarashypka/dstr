package com.deoxys.dev.dstr.presentation.servlet.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 30.05.16.
 */

@WebServlet(name = "Home", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath.isEmpty() ? "/" : contextPath);
    }
}
