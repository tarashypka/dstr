package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.order.Order;
import com.deoxys.dev.dstr.domain.model.user.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class OrderReader
implements HttpRequestReader<Order>, HttpSessionReader<Order> {

    @Override
    public Order read(HttpServletRequest req) {
        return null;
    }

    /**
     * Read Order from a request.
     *
     * It could be Customer making new Order.
     */
    @Override
    public Order read(HttpSession ses) {
        Order order = (Order) ses.getAttribute("order");
        if (order == null) {
            order = new Order();
            Customer customer = CustomerReaders.readerForAuthorization().read(ses);
            order.setCustomer(customer);
        }
        return order;
    }
}