package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by deoxys on 07.07.16.
 */

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
        try {
            req.setAttribute("customer", customerDao.get(id));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void loadCustomers(HttpServletRequest req) {
        try {
            req.setAttribute("customers", customerDao.getAll());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void login(HttpServletRequest req) {
        Customer customer = requestReader.read(req);
        Customer shouldBe = null;
        try {
            shouldBe = customerDao.get(customer.getEmail());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String error = null;
        if (shouldBe == null)
            error = "wrong_email";
        else if ( ! customer.hasValidPassword(shouldBe.getPassword()))
            error = "wrong_password";
        else if ( ! shouldBe.isEnabled())
            error = "account_closed";
        if (error != null) req.setAttribute("error", error);
        else req.getSession().setAttribute("customer", shouldBe.withoutPassword());
    }

    public void register(HttpServletRequest req) {
        Customer customer = requestReader.read(req);
        if (req.getAttribute("error") == null) try {
            if (customerDao.exists(customer)) {
                req.setAttribute("error", "email_dup");
                req.setAttribute("_customer", customer);
            } else {
                customerDao.add(customer);
                customer = customerDao.get(customer.getId());
                req.getSession().setAttribute("customer", customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } else req.setAttribute("_customer", customer);
    }

    public void logout(HttpServletRequest req) {
        HttpSession ses = req.getSession(false);
        if (ses != null) ses.invalidate();
        else req.setAttribute("error", "Session not found");
    }

    public void swapCustomerStatus(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        try {
            customerDao.swapStatus(id);
            Customer updated = customerDao.get(id);
            req.setAttribute("customer", updated);
            req.setAttribute("email", updated.getEmail());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
