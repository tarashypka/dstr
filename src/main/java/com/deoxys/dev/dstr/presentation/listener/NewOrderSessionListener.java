package com.deoxys.dev.dstr.presentation.listener;

/**
 * Created by deoxys on 22.07.16.
 */

import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.service.ItemService;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.*;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.web.SessionState;

import javax.servlet.annotation.WebListener;
import java.util.Map;

@WebListener()
public class NewOrderSessionListener implements EntryRemovedListener<String, SessionState> {

    /**
     * When session with unfinished order was destroyed
     * database should be returned to the state before that order.
     *
     * It could happen if user session expired or he logged out.
     */
    @Override
    public void entryRemoved(EntryEvent<String, SessionState> event) {
        SessionState state = event.getOldValue();
        Map<String, Data> map = state.getAttributes();
        Data orderData = map.get("order");
        if (orderData != null) {
            // Deserialize orderData into order
            Order order = null;
            if (order != null) {
                ItemService itemService = new ItemService();
                itemService.takeOrderItemsFromReserve(order.getItems());
                itemService.addOrderItemsToStock(order.getItems());
            }
        }
    }

    public NewOrderSessionListener() { }
}