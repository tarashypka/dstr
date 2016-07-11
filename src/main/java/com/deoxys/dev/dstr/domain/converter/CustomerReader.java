package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by deoxys on 11.07.16.
 */

public class CustomerReader
implements HttpRequestReader<Customer>, HttpSessionReader<Customer> {

    @Override
    public Customer read(HttpServletRequest req) {
        return null;
    }

    /**
     * Read Customer from his session.
     */
    @Override
    public Customer read(HttpSession ses) {
        return (Customer) ses.getAttribute("customer");
    }
}
