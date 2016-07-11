package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Order;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by deoxys on 11.07.16.
 */

public class OrderReader implements HttpSessionReader<Order> {

    /**
     * Read Order from a request.
     * It could be Customer making new Order.
     */
    @Override
    public Order read(HttpSession ses) {
        Order order = new Order();
        CustomerReader customerReader = new CustomerReader();
        order.setCustomer(customerReader.read(ses));
        order.setItems((HashMap) ses.getAttribute("orderItems"));
        order.setReceipt((HashMap) ses.getAttribute("receipt"));
        return order;
    }
}