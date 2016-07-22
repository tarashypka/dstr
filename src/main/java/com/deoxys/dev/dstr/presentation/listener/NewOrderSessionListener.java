package com.deoxys.dev.dstr.presentation.listener;

/**
 * Created by deoxys on 22.07.16.
 */

import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.service.ItemService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener()
public class NewOrderSessionListener implements HttpSessionListener {

    public NewOrderSessionListener() { }

    /**
     * When session with unfinished order was destroyed
     * database should be returned to the state before that order.
     *
     * It could happen if user session expired or he logged out.
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Order order = (Order) se.getSession().getAttribute("order");
        if (order != null) {
            ItemService itemService = new ItemService();
            itemService.takeOrderItemsFromReserve(order.getItems());
            itemService.addOrderItemsToStock(order.getItems());
        }
    }

    public void sessionCreated(HttpSessionEvent httpSessionEvent) { }
}