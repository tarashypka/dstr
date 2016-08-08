package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomerReader
implements HttpRequestReader<Customer>, HttpSessionReader<Customer> {

    /**
     * Read Customer from request.
     *
     * It could be Customer trying to log in,
     * or Customer trying to register.
     */
    @Override
    public Customer read(HttpServletRequest req) {
        return new Customer(
                req.getParameter("email"),
                req.getParameter("psswd"),
                req.getParameter("name"),
                req.getParameter("sname")
        );
    }

    /**
     * Read Customer from his session.
     *
     * It could be Customer requesting his account info,
     * or Admin requesting any Customer's info.
     */
    @Override
    public Customer read(HttpSession ses) {
        return (Customer) ses.getAttribute("customer");
    }
}
