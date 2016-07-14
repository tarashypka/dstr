package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by deoxys on 11.07.16.
 */

public class ItemReader
implements HttpRequestReader<Item>, HttpSessionReader<Item> {

    /**
     * Read Item from a request.
     *
     * It could be Admin adding new / updating existing Item.
     */
    @Override
    public Item read(HttpServletRequest req) {
        Item item = new Item();
        String error = null;
        String action = req.getParameter("action");
        if (action.equals("editItem")) {
            String id = req.getParameter("id");
            if (id == null || id.equals(""))
                error = "Wrong item id";
            else item.setId(req.getParameter("id"));
        }

        String category = req.getParameter("category");
        String price = req.getParameter("price");
        String currency = req.getParameter("currency");
        String stocked = req.getParameter("stocked");
        String reserved = req.getParameter("reserved");
        String sold = req.getParameter("sold");

        if (category == null || category.equals("")) {
            if (error == null) error = "Select category";
        } else item.setCategory(category);
        if (price == null || price.equals("")) {
            if (error == null) error = "Enter price";
        } else item.setPrice(Double.parseDouble(price));
        if (currency == null || currency.equals("")) {
            if (error == null) error = "Select currency";
        } else item.setCurrency(currency);

        ItemStatus status = new ItemStatus();

        if (stocked == null || stocked.equals("")) {
            if (error == null) error = "How many in stock?";
        } else status.setStocked(Integer.parseInt(stocked));
        if (reserved == null || reserved.equals(""))
            status.setReserved(0);
        else status.setReserved(Integer.parseInt(reserved));
        if (sold == null || sold.equals(""))
            status.setSold(0);
        else status.setSold(Integer.parseInt(sold));

        item.setStatus(status);
        return item;
    }

    @Override
    public Item read(HttpSession ses) {
        return null;
    }
}