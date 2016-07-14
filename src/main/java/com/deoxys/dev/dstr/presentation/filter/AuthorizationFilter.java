package com.deoxys.dev.dstr.presentation.filter;

import com.deoxys.dev.dstr.domain.model.Customer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 12.07.16.
 */

public class AuthorizationFilter implements Filter {
    private Logger logger = Logger.getLogger(AuthorizationFilter.class);

    private String contextPath;

    private static final String CONFIG_RESOURCE;
    private static final String MAIN_CONTROLLER;
    private static final String ROOT;
    private static final String HOME;
    private static final String CUSTOMER_CONTROLLER;
    private static final String ADMIN_CONTROLLER;
    private static final String HOME_LINK;
    private static final String LOGIN_LINK;

    static {
        CONFIG_RESOURCE = "auth-rules.xml";
        MAIN_CONTROLLER = "/controller";
        ROOT = "/";
        HOME = "/home";
        CUSTOMER_CONTROLLER = "/controller/customer";
        ADMIN_CONTROLLER = "/controller/admin";
        HOME_LINK = "/WEB-INF/jsp/home.jsp";
        LOGIN_LINK = "/WEB-INF/jsp/login.jsp";
    }

    private static List<String> authActions = new ArrayList<>();
    private static List<String> notAuthActions = new ArrayList<>();
    private static List<String> publicActions = new ArrayList<>();
    private static List<String> customerActions = new ArrayList<>();
    private static List<String> adminActions = new ArrayList<>();

    public void init(FilterConfig config) throws ServletException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            URL resource = this.getClass().getClassLoader().getResource(CONFIG_RESOURCE);
            if (resource == null) throw new ServletException("Resource not found");
            doc = dBuilder.parse(resource.toString());
            doc.getDocumentElement().normalize();
            NodeList actions = doc.getElementsByTagName("action");
            for (int i = 0; i < actions.getLength(); i++) {
                Element e = (Element) actions.item(i);
                String action = e.getAttribute("actionname");
                switch (e.getAttribute("auth")) {
                    case "true":
                        switch (e.getAttribute("allowed")) {
                            case "customer":
                                customerActions.add(action);
                                break;
                            case "admin":
                                adminActions.add(action);
                                break;
                            case "*": authActions.add(action);
                        }
                        break;
                    case "false":
                        notAuthActions.add(action);
                        break;
                    case "*": publicActions.add(action);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
        contextPath = config.getServletContext().getContextPath();
        logger.info("AuthenticationFilter: initialized successfully");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        uri = uri.split(contextPath, 2)[1];
        if (uri.equals(ROOT) || uri.equals(HOME)) {
            req.getRequestDispatcher(HOME_LINK).forward(req, resp);
        }
        HttpSession session = req.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");
        String action = req.getParameter("action");
        if ( ! allowed(customer, uri, action)) {
            logger.error("Unauthorized access request");
            if (customer == null) {
                req.getRequestDispatcher(LOGIN_LINK).forward(req, resp);
            } else throw new ServletException("Unauthorized access");
        } else chain.doFilter(req, resp);
    }

    private boolean allowed(Customer customer, String uri, String action) {
        if (isResource(uri)) return allowedResource(customer, uri);
        return allowedUri(customer, uri) && allowedAction(customer, action);
    }

    private boolean isResource(String uri) {
        return uri.startsWith("/resources");
    }

    /**
     *      /resources          public resources
     *      /resources/admin    admin resources
     *      /resources/customer customer resources
     **/
    private boolean allowedResource(Customer customer, String resource) {
        logger.info("Requested Resource::" + resource);
        resource = resource.split("/resources", 2)[1];
        if (customer.isAdmin()) return resource.startsWith("/admin");
        return ! customer.isCustomer() || resource.startsWith("/customer");
    }

    private boolean allowedUri(Customer customer, String uri) {
        logger.info("Requested URI::" + uri);
        if (uri.equals("/") || uri.equals("home")) return true;
        if (uri.equals(MAIN_CONTROLLER)) return true;
        if (customer == null) return false;
        if (customer.isCustomer()) return uri.equals(CUSTOMER_CONTROLLER);
        return uri.equals(ADMIN_CONTROLLER) || uri.equals(CUSTOMER_CONTROLLER);
    }

    private boolean allowedAction(Customer customer, String action) {
        logger.info("Requested Action::" + action);
        if (action == null || publicActions.contains(action)) return true;
        if (customer == null) return notAuthActions.contains(action);
        if (authActions.contains(action)) return true;
        if (customer.isCustomer()) return customerActions.contains(action);
        if (customer.isAdmin()) return adminActions.contains(action);
        return false;
    }

    public void destroy() { }
}