package com.deoxys.dev.dstr.presentation.filter;

import com.deoxys.dev.dstr.domain.Customer;
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

    private List<String> pubPages;
    private List<String> authFalsePages;
    private List<String> authTruePages;
    private List<String> customerPrivPages;
    private List<String> adminPrivPages;
    private List<String> pubResourcesDirs;

    public void init(FilterConfig config) throws ServletException {
        String pub = config.getInitParameter("public");
        String authFalse = config.getInitParameter("authFalse");
        String authTrue = config.getInitParameter("authTrue");
        String customerPriv = config.getInitParameter("customerPrivate");
        String adminPriv = config.getInitParameter("adminPrivate");
        String pubResources = config.getInitParameter("publicResources");

        String regex = "\\s*\n\\s*";

        contextPath = config.getServletContext().getContextPath();

        if (pub != null)
            pubPages = toContextPaths(Arrays.asList(pub.split(regex)));
        if (authFalse != null)
            authFalsePages = toContextPaths(Arrays.asList(authFalse.split(regex)));
        if (authTrue != null)
            authTruePages = toContextPaths(Arrays.asList(authTrue.split(regex)));
        if (customerPriv != null)
            customerPrivPages = toContextPaths(Arrays.asList(customerPriv.split(regex)));
        if (adminPriv != null)
            adminPrivPages = toContextPaths(Arrays.asList(adminPriv.split(regex)));
        if (pubResources != null)
            pubResourcesDirs = toContextPaths(Arrays.asList(pubResources.split(regex)));

        logger.info("AuthenticationFilter: initialized successfully");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        logger.info("Requested Resource::" + uri);

        if (uri.equals(contextPath + "/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        List<String> allowedPages = new ArrayList<>();
        List<String> allowedResources = new ArrayList<>();

        allowedPages.addAll(pubPages);
        allowedResources.addAll(pubResourcesDirs);
        if (customer != null) {
            allowedPages.addAll(authTruePages);
            if (customer.isCustomer()) {
                allowedPages.addAll(customerPrivPages);
            } else if (customer.isAdmin()) {
                allowedPages.addAll(adminPrivPages);
            } else {
                logger.error("Undefined customer role " + customer.getRole());
            }
        } else {
            allowedPages.addAll(authFalsePages);
        }

        if (! allowedPages.contains(uri) && ! isAllowedResource(uri, allowedResources)) {
            logger.error("Unauthorized access request");
            resp.sendRedirect(contextPath + "/login");
        } else {
            // Pass the request along the filter chain
            chain.doFilter(request, response);
        }
    }

    private boolean isAllowedResource(String uri, List<String> allowedResources) {
        for (String resourceDir : allowedResources) {
            if (uri.startsWith(resourceDir)) {
                // Should be a file
                if (! uri.replaceAll(resourceDir + "/", "").contains("/")) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> toContextPaths(List<String> pages) {
        if (contextPath.length() > 0) {
            for (int i = 0; i < pages.size(); i++) {
                pages.set(i, contextPath + pages.get(i));
            }
        }
        return pages;
    }

    public void destroy() { }
}
