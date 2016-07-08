package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.persistence.dao.PostgresCustomerDAO;

/**
 * Created by deoxys on 07.07.16.
 */

public class CustomerService extends PostgresService {

    private PostgresCustomerDAO customerDao;

    public CustomerService() {
        super();
        customerDao = new PostgresCustomerDAO(super.dataSource);
    }
}
