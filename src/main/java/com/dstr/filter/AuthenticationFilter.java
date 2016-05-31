package com.dstr.filter;

import com.dstr.model.customer.Customer;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 30.05.16.
 */

@WebFilter("/authenticationFilter")
public class AuthenticationFilter implements Filter {

    private Logger logger = Logger.getLogger(AuthenticationFilter.class);

    public void init(FilterConfig config) throws ServletException {
        logger.info("AuthenticationFilter: initialized successfully");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        logger.info("Requested Resource::" + uri);

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        List<String> allowedUris = new ArrayList<>();
        allowedUris.add("/");
        allowedUris.add("/home");
        allowedUris.add("/login");
        allowedUris.add("/register");
        allowedUris.add("/logout");
        allowedUris.add("/item");
        allowedUris.add("/items");
        allowedUris.add("/errorHandler");
        if (customer != null) {
            String role = customer.getRole();
            if (role.equals("admin")) {
                allowedUris.add("/admin/customers");
                allowedUris.add("/admin/items");
                allowedUris.add("/admin/orders");
            } else if (role.equals("customer")) {
                allowedUris.add("/customer/items");
                allowedUris.add("/customer/orders");
            }
        }
        String contextPath = req.getContextPath();
        allowedUris = urisToContextUris(allowedUris, contextPath);
        if (! uriStartsWith(uri, allowedUris)) {
            logger.error("Unauthorized access request");
            resp.sendRedirect("/login");
        } else {
            // Pass the request along the filter chain
            chain.doFilter(request, response);
        }
    }

    private static boolean uriStartsWith(String uri, List<String> uris) {
        for (String start : uris) {
            if (uri.startsWith(start)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> urisToContextUris(List<String> uris, String context) {
        if (context.length() > 0) {
            for (int i = 0; i < uris.size(); i++) {
                uris.set(i, context + uris.get(i));
            }
        }
        return uris;
    }

    public void destroy() { }
}
