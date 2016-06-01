package com.dstr.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by deoxys on 01.06.16.
 */

@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {
    private Logger logger = Logger.getLogger(EncodingFilter.class);

    private String encoding = "UTF-8";

    public void init(FilterConfig config) throws ServletException {
        String encodingParam = config.getInitParameter("encoding");

        if (encodingParam != null) {
            encoding = encodingParam;
        }
        logger.info("EncodingFilter: initialized successfully");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        req.setCharacterEncoding(encoding);
        chain.doFilter(req, resp);
    }

    public void destroy() { }
}
