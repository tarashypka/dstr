package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.*;

public class Item implements Serializable {
    private String id;
    private String name;
    private double price;
    private Currency currency;
    private ItemStatus status;
    private List<String> tags = new ArrayList<>();
    private Map<String, String> extendedFields = new LinkedHashMap<>();

    public Item() { }

    public Item(String id) {
        this.id = id;
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(String name, double price, Currency currency, ItemStatus status) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setCurrency(String currencyCode) {
        currency = Currency.getInstance(currencyCode.toUpperCase());
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public boolean enoughInStock(int required) {
        return required <= status.getStocked();
    }

    public int stocked() {
        return status.getStocked();
    }

    public int reserved() {
        return status.getReserved();
    }

    public int sold() {
        return status.getSold();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setTags(String tagsInp) {
        // Often tags are in form [tag1, tag2, ...]
        tagsInp = tagsInp.replace("[", "");
        tagsInp = tagsInp.replace("]", "");
        Arrays.asList(tagsInp.split(",")).forEach(this::addTag);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public Map<String, String> getExtendedFields() {
        return extendedFields;
    }

    public void setExtendedFields(Map<String, String> extendedFields) {
        this.extendedFields = extendedFields;
    }

    public void addField(String k, String v) {
        if (k != null && ! k.equals("") && v != null && ! v.equals(""))
            extendedFields.put(k, v);
    }

    @Override
    public String toString() {
        return  "{ id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", status=" + status +
                ", extendedFields=" + extendedFields +
                ", tags=" + tags + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != null ? !id.equals(item.id) : item.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
