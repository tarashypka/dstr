package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.UserReaders;
import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.persistence.dao.postgres.UserDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class UserService extends PostgresService {

    Logger logger = Logger.getLogger(CustomerService.class);

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO(dataSource);
    }

    /**
     * If in the context there is already initialized UserService, then
     * CustomerService could be retrieved out of it,
     * without repeating authentication and getting the new DataSource.
     */
    public CustomerService createCustomerService() {
        return new CustomerService(dataSource);
    }

    public void loadUser(HttpServletRequest req) {
        User user = UserReaders.readerForAuthorization().read(req.getSession());

        // It could be admin requesting particular user info
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
        req.setAttribute("user", userDAO.get(id));
    }

    public void login(HttpServletRequest req) {
        User user = UserReaders.readerForAuthentication().read(req);
        User shouldBe = userDAO.get(user.getEmail());
        if (user.hasValidCredentials(shouldBe))
            req.getSession().setAttribute("user", user.withoutPassword());
        else req.setAttribute("error", user.getErrType());
    }

    public void register(HttpServletRequest req) {
        User user = UserReaders.readerForAuthentication().read(req);
        // If customer input was already validated with Js
        boolean validated = Boolean.parseBoolean(req.getParameter("validated"));
        if (validated || user.isValidForInput(req.getParameter("psswd2"))) {
            if (! userDAO.exists(user)) {
                userDAO.add(user);
                // Automatically authenticate after registration
                req.getSession().setAttribute("user", user);
                return;
            } else user.setErrType("email_dup");
        }
        req.setAttribute("error", user.getErrType());
        req.setAttribute("_user", user);
    }

    public void logout(HttpServletRequest req) {
        HttpSession ses = req.getSession(false);
        if (ses != null) ses.invalidate();
        else req.setAttribute("error", "Session not found");
    }

    public void swapUserStatus(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        userDAO.swapStatus(id);
    }
}
