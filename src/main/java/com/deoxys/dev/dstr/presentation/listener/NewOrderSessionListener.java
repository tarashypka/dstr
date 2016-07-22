package com.deoxys.dev.dstr.presentation.listener; /**
 * Created by deoxys on 22.07.16.
 */

import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.service.ItemService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener()
public class NewOrderSessionListener implements HttpSessionListener {

    public NewOrderSessionListener() { }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) { }

    /**
     * When session with unfinished order was destroyed or expired
     * database should be returned to the state without this order
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
}