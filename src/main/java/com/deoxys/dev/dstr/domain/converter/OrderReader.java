package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Order;

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
            CustomerReader customerReader = new CustomerReader();
            order.setCustomer(customerReader.read(ses));
        }
        return order;
    }
}