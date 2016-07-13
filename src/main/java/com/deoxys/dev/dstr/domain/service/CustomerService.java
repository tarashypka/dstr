package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by deoxys on 07.07.16.
 */

public class CustomerService extends PostgresService<Customer> {
    Logger logger = Logger.getLogger(CustomerService.class);

    private CustomerDAO dao;

    public CustomerService() {
        super(new CustomerReader());
        dao = new CustomerDAO(dataSource);
    }

    public void loadCustomer(HttpServletRequest req) {
    }

    public void loadCustomers(HttpServletRequest req) {
    }

    public void login(HttpServletRequest req) {
    }

    public void register(HttpServletRequest req) {
    }

    public void logout(HttpServletRequest req) {
    }

    public void swapCustomerStatus(HttpServletRequest req) {
    }
}
