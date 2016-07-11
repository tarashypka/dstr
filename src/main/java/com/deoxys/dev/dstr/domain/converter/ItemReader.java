package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by deoxys on 11.07.16.
 */

public class ItemReader implements HttpRequestReader<Item> {

    /**
     * Read Item from a request.
     * It could be Admin adding a new Item,
     * or customer adding Item to an Order.
     */
    @Override
    public Item read(HttpServletRequest req) {
        Item item = new Item();
        item.setId(req.getParameter("itemId"));
        item.setCategory(req.getParameter("itemCategory"));
        item.setCurrency(req.getParameter("itemCurrency"));
        String price = req.getParameter("itemPrice");
        if (price != null) item.setPrice(Integer.parseInt(price));
        ItemStatus status = new ItemStatus();
        String reserved = req.getParameter("itemReserved");
        if (reserved != null) status.setSold(Integer.parseInt(reserved));
        String sold = req.getParameter("itemSold");
        if (sold != null) status.setSold(Integer.parseInt(sold));
        String stocked = req.getParameter("itemStocked");
        if (stocked != null) status.setStocked(Integer.parseInt(stocked));
        item.setStatus(status);
        return item;
    }
}