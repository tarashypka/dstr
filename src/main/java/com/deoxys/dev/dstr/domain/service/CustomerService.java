package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by deoxys on 07.07.16.
 */

public class CustomerService extends PostgresService {
    Logger logger = Logger.getLogger(CustomerService.class);

    private CustomerDAO dao;

    public CustomerService() {
        super();
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
