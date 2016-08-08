package com.deoxys.dev.dstr.presentation.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ErrorHandler", urlPatterns = "/error")
public class ErrorHandlerServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ErrorHandlerServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processError(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Analyze servlet error/exception
        String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");

        req.setAttribute("servletName", servletName == null ? "Unknown" : servletName);
        req.setAttribute("requestUri", requestUri == null ? "Unknown" : requestUri);

        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        req.setAttribute("statusCode", statusCode);
        if (statusCode == 500) {
            Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
            String exName = throwable.getClass().getName();
            String exMsg = throwable.getMessage();
            exMsg = (exMsg == null) ? "Unknown" : exMsg;
            req.setAttribute("exception", exName);
            req.setAttribute("exceptionMsg", exMsg);
            logger.error(exName + ": " + exMsg);
        } else {
            logger.error(statusCode);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
    }
}
