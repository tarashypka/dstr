package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomerService extends PostgresService<Customer> {
    Logger logger = Logger.getLogger(CustomerService.class);

    private CustomerDAO customerDao;

    public CustomerService() {
        super(new CustomerReader());
        customerDao = new CustomerDAO(dataSource);
    }

    public void loadCustomer(HttpServletRequest req) {
        Customer customer = sessionReader.read(req.getSession());
        long id = customer.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : customer.getId();
        req.setAttribute("customer", customerDao.get(id));
    }

    public void loadCustomers(HttpServletRequest req) {
        req.setAttribute("customers", customerDao.getAll());
    }

    public void login(HttpServletRequest req) {
        Customer customer = requestReader.read(req);
        if (customerDao.hasValidCredentials(customer))
            req.getSession().setAttribute("customer", customer.withoutPassword());
        else req.setAttribute("error", customer.getErrType());
    }

    public void register(HttpServletRequest req) {
        Customer customer = requestReader.read(req);
        // If customer input was already validated with Js
        boolean validated = Boolean.parseBoolean(req.getParameter("validated"));
        if (validated || customerDao.isValidForInput(customer, req.getParameter("psswd2"))) {
            if (! customerDao.exists(customer)) {
                customerDao.add(customer);
                // Automatically login after registration
                req.getSession().setAttribute("customer", customer);
                return;
            } else customer.setErrType("email_dup");
        }
        req.setAttribute("error", customer.getErrType());
        req.setAttribute("_customer", customer);
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
