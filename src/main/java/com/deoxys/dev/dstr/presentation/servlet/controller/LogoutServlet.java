package com.deoxys.dev.dstr.presentation.servlet.controller;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by deoxys on 30.05.16.
 */

@WebServlet(name = "Logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(LogoutServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    logger.info("Logout: JSESSIONID=" + cookie.getValue());
                    break;
                }
            }
        }

        // Invalidate the session
        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");
        logger.info("Logout: invalidate session for customer=" + customer);
        if (session != null) {
            session.invalidate();
            logger.info("Customer=" + customer + " logged out");
        } else {
            logger.info("Customer=" + customer + " has no session for logout");
        }
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}