package com.deoxys.dev.dstr.domain.model.item;

import java.io.Serializable;
import java.util.List;

public final class Tag implements Serializable {
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
