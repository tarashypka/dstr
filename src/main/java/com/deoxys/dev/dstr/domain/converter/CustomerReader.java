package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by deoxys on 11.07.16.
 */

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
        Customer customer = new Customer();
        customer.setEmail(req.getParameter("email"));
        customer.setPassword(req.getParameter("password"));
        customer.setName(req.getParameter("name"));
        customer.setSurname(req.getParameter("surname"));
        return customer;
    }

    /**
     * Read Customer from his session.
     */
    @Override
    public Customer read(HttpSession ses) {
        return (Customer) ses.getAttribute("customer");
    }
}
