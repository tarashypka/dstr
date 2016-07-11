package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.persistence.dao.CustomerDAO;
import org.apache.log4j.Logger;

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
}
