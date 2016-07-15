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
        String action = req.getParameter("action");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        if (action.equals("login")) {
            customer.setEmail(email);
            customer.setPassword(password);
        } else if (action.equals("register")) {
            String error = null;
            if (email == null || ! email.matches("\\S+@\\w+\\.\\w+")) {
                error = "Wrong email";
            } else customer.setEmail(email);
            if (password == null || password.length() < 8) {
                if (error == null) error = "Password is weak";
            } else customer.setPassword(password);
            if (name == null || name.equals("")) {
                if (error == null) error = "Pick a name";
            } else customer.setName(name);
            if (surname == null || surname.equals("")) {
                if (error == null) error = "Pick a surname";
            } else customer.setSurname(surname);
            req.setAttribute("error", error);
        }
        return customer;
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
