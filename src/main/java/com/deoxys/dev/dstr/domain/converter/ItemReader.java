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

        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String currency = req.getParameter("currency");
        String stocked = req.getParameter("stocked");
        String reserved = req.getParameter("reserved");
        String sold = req.getParameter("sold");
        String tags = req.getParameter("tags");

        item.setId(req.getParameter("id"));
        if (name == null || name.isEmpty()) error = "empty_name";
        else item.setName(name);
        if (tags == null || tags.isEmpty() || tags.equals("[]")) {
            if (error == null) error = "empty_tags";
        } else item.setTags(tags);
        if (price == null || price.isEmpty()) {
            if (error == null) error = "empty_price";
        } else item.setPrice(Double.parseDouble(price));
        if (currency == null || currency.isEmpty()) {
            if (error == null) error = "empty_currency";
        } else item.setCurrency(currency);

        ItemStatus status = new ItemStatus();

        if (stocked == null || stocked.isEmpty()) {
            if (error == null) error = "empty_stocked";
        } else status.setStocked(Integer.parseInt(stocked));
        req.setAttribute("error", error);

        if (reserved == null || reserved.isEmpty()) status.setReserved(0);
        else status.setReserved(Integer.parseInt(reserved));
        if (sold == null || sold.isEmpty()) status.setSold(0);
        else status.setSold(Integer.parseInt(sold));

        item.setStatus(status);

        // Read extended fields
        req.getParameterMap().forEach((key, vals) -> {
            if (key.matches("field[0-9]+-name")) {
                String n = key.substring(5, key.indexOf("-name"));
                String fval = req.getParameter("field" + n + "-val");
                if (fval == null || fval.isEmpty()) {

                }
                item.addField(vals[0], fval);
            }
        });

        return item;
    }

    @Override
    public Item read(HttpSession ses) {
        return null;
    }
}