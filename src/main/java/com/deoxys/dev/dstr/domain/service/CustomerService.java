package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.model.User;
import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomerService extends PostgresService<User> {
    Logger logger = Logger.getLogger(CustomerService.class);

    private CustomerDAO customerDao;

    public CustomerService() {
        super(new CustomerReader());
        customerDao = new CustomerDAO(dataSource);
    }

    public void loadCustomer(HttpServletRequest req) {
        User user = sessionReader.read(req.getSession());
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
        req.setAttribute("customer", customerDao.get(id));
    }

    public void loadCustomers(HttpServletRequest req) {
        req.setAttribute("customers", customerDao.getAll());
    }

    public void login(HttpServletRequest req) {
        User user = requestReader.read(req);
        User shouldBe = customerDao.get(user.getEmail());
        if (user.hasValidCredentials(shouldBe))
            req.getSession().setAttribute("user", user.withoutPassword());
        else req.setAttribute("error", user.getErrType());
    }

    public void register(HttpServletRequest req) {
        User user = requestReader.read(req);
        // If customer input was already validated with Js
        boolean validated = Boolean.parseBoolean(req.getParameter("validated"));
        if (validated || user.isValidForInput(req.getParameter("psswd2"))) {
            if (! customerDao.exists(user)) {
                customerDao.add(user);
                // Automatically login after registration
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

    public void swapCustomerStatus(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        customerDao.swapStatus(id);
    }
}
