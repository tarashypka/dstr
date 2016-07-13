package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Currency;
import java.util.HashMap;

/**
 * Created by deoxys on 11.07.16.
 */

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
            order.setItems(new HashMap<Item, Integer>());
            order.setReceipt(new HashMap<Currency, Double>());
        }
        return order;
    }
}