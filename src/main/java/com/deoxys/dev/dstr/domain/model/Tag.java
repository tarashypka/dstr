package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deoxys on 28.07.16.
 */

public class Tag implements Serializable {
    private String name;
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
