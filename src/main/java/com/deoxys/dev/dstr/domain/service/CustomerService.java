package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.UserReaders;
import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.persistence.dao.postgres.CustomerDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

public final class CustomerService extends PostgresService {

    Logger logger = Logger.getLogger(UserService.class);

    private CustomerDAO customerDao;

    public CustomerService() {
        customerDao = new CustomerDAO(dataSource);
    }

    public CustomerService(DataSource dataSource) {
        super(dataSource);
        customerDao = new CustomerDAO(dataSource);
    }

    public void loadCustomer(HttpServletRequest req) {
        User user = UserReaders.readerForAuthorization().read(req.getSession());

        // It could be admin requesting particular customer info
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
        req.setAttribute("customer", customerDao.get(id));
    }

    public void loadCustomers(HttpServletRequest req) {
        req.setAttribute("customers", customerDao.getAll());
    }


}
