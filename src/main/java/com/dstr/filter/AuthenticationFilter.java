package com.dstr.filter;

import com.dstr.model.Customer;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deoxys on 30.05.16.
 */

@WebFilter("/authenticationFilter")
public class AuthenticationFilter implements Filter {
    private Logger logger = Logger.getLogger(AuthenticationFilter.class);

    private String contextPath;

    private List<String> publicPages;
    private List<String> authFalsePages;
    private List<String> authTruePages;
    private List<String> customerPrivatePages;
    private List<String> adminPrivatePages;


    public void init(FilterConfig config) throws ServletException {
        String pub = config.getInitParameter("public");
        String authFalse = config.getInitParameter("authFalse");
        String authTrue = config.getInitParameter("authTrue");
        String customerPriv = config.getInitParameter("customerPrivate");
        String adminPriv = config.getInitParameter("adminPrivate");

        String regex = "\\s*\n\\s*";

        contextPath = config.getServletContext().getContextPath();

        if (pub != null)
            publicPages = toContextPages(Arrays.asList(pub.split(regex)));
        if (authFalse != null)
            authFalsePages = toContextPages(Arrays.asList(authFalse.split(regex)));
        if (authTrue != null)
            authTruePages = toContextPages(Arrays.asList(authTrue.split(regex)));
        if (customerPriv != null)
            customerPrivatePages = toContextPages(Arrays.asList(customerPriv.split(regex)));
        if (adminPriv != null)
            adminPrivatePages = toContextPages(Arrays.asList(adminPriv.split(regex)));

        logger.info("AuthenticationFilter: initialized successfully");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        logger.info("Requested Resource::" + contextPath + uri);

        if (uri.equals(contextPath + "/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        List<String> allowedPages = new ArrayList<>();

        allowedPages.addAll(publicPages);
        if (customer != null) {
            allowedPages.addAll(authTruePages);
            if (customer.isCustomer()) {
                allowedPages.addAll(customerPrivatePages);
            } else if (customer.isAdmin()) {
                allowedPages.addAll(adminPrivatePages);
            } else {
                logger.error("Undefined customer role " + customer.getRole());
            }
        } else {
            allowedPages.addAll(authFalsePages);
        }

        if (! allowedPages.contains(uri)) {
            logger.error("Unauthorized access request");
            resp.sendRedirect(contextPath + "/login");
        } else {
            // Pass the request along the filter chain
            chain.doFilter(request, response);
        }
    }

    private List<String> toContextPages(List<String> pages) {
        if (contextPath.length() > 0) {
            for (int i = 0; i < pages.size(); i++) {
                pages.set(i, contextPath + pages.get(i));
            }
        }
        return pages;
    }

    public void destroy() { }
}
