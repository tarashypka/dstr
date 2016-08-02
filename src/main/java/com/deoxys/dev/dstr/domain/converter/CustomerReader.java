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
        String email = req.getParameter("email");
        String psswd = req.getParameter("psswd");
        String psswd2 = req.getParameter("psswd2");
        String name = req.getParameter("name");
        String sname = req.getParameter("sname");
        String validated = req.getParameter("validated");

        if (! Boolean.parseBoolean(validated)) {
            System.out.println("VALIDATION WITH CR");
            // Input fields were not validated with JS
            if (email == null || email.isEmpty())
                req.setAttribute("error", "email_empty");
            else if (! email.matches("\\S+@\\w+\\.\\w+"))
                req.setAttribute("error", "email_wrong");
            else if (psswd == null || psswd.isEmpty())
                req.setAttribute("error", "psswd_empty");
            else if (psswd.length() < 8)
                req.setAttribute("error", "psswd_weak");
            else if (! psswd.equals(psswd2))
                req.setAttribute("error", "psswd2_wrong");
            else if (name == null || name.isEmpty())
                req.setAttribute("error", "name_empty");
            else if (sname == null || sname.isEmpty())
                req.setAttribute("error", "sname_empty");
        }
        return new Customer(email, psswd, name, sname);
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
