package com.deoxys.dev.dstr.presentation.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by deoxys on 30.05.16.
 */

@WebServlet(name = "ErrorHandler", urlPatterns = "/error")
public class ErrorHandlerServlet extends HttpServlet {
    final static Logger logger = Logger.getLogger(ErrorHandlerServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processError(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Analyze servlet error/exception
        String servletName = (String) request
                .getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");

        request.setAttribute("servletName",
                servletName == null ? "Unknown" : servletName);
        request.setAttribute("requestUri",
                requestUri == null ? "Unknown" : requestUri);

        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        request.setAttribute("statusCode", statusCode);
        if (statusCode == 500) {
            Throwable throwable = (Throwable) request
                    .getAttribute("javax.servlet.error.exception");
            String exName = throwable.getClass().getName();
            String exMsg = throwable.getMessage();
            exMsg = (exMsg == null) ? "Unknown" : exMsg;
            request.setAttribute("exception", exName);
            request.setAttribute("exceptionMsg", exMsg);
            logger.error(exName + ": " + exMsg);
        } else {
            logger.error(statusCode);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")
                .forward(request, response);
    }
}
