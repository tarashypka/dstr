package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomerReader
implements HttpRequestReader<User>, HttpSessionReader<User> {

    /**
     * Read Customer from request.
     *
     * It could be Customer trying to log in,
     * or Customer trying to register.
     */
    @Override
    public User read(HttpServletRequest req) {
        return new User.UserBuilder(req.getParameter("email"))
                .withPassword(req.getParameter("psswd"))
                .withName(req.getParameter("name"), req.getParameter("sname"))
                .build();
    }

    /**
     * Read Customer from his session.
     *
     * It could be Customer requesting his account info,
     * or Admin requesting any Customer's info.
     */
    @Override
    public User read(HttpSession ses) {
        return (User) ses.getAttribute("customer");
    }
}
