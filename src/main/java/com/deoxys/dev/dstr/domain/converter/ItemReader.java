package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.ItemStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemReader
implements HttpRequestReader<Item>, HttpSessionReader<Item> {

    /**
     * Read Item from a request.
     *
     * It could be Admin adding new / updating existing Item.
     *
     * All input is validated with JavaScript before and is not null or empty.
     */
    @Override
    public Item read(HttpServletRequest req) {
        Item item = new Item();

        item.setId(req.getParameter("id"));
        item.setName(req.getParameter("name"));
        item.setTags(convertTags(req.getParameter("tags")));
        item.setPrice(Double.parseDouble(req.getParameter("price")));
        item.setCurrency(Currency.getInstance(req.getParameter("currency")));
        item.setStatus(new ItemStatus(
                Integer.parseInt(req.getParameter("stocked")),
                Integer.parseInt(req.getParameter("reserved")),
                Integer.parseInt(req.getParameter("sold"))
        ));
        item.setExtendedFields(fetchExtFields(req));
        return item;
    }

    private List<String> convertTags(String tags) {
        // Often tags are in form [tag1, tag2, ...]
        return Arrays.asList(tags.replace("[", "").replace("]", "").split(","));
    }

    private Map<String, String> fetchExtFields(HttpServletRequest req) {
        return req.getParameterMap().keySet().stream()
                .filter(k -> k.matches("field[0-9]+_name"))
                .collect(Collectors.toMap(
                        req::getParameter,
                        k -> req.getParameter(k.replace("name", "val"))
                ));
    }

    @Override
    public Item read(HttpSession ses) {
        return null;
    }
}