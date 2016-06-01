package com.dstr.model;

import java.io.Serializable;
import java.util.Currency;
import java.util.Map;

public class Item implements Serializable {
    private String id;
    private String category;
    private double price;
    private Currency currency;
    private int left;
    private Map<String, String> extendedFields;

    public Item() { }

    public Item(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        this.currency = Currency.getInstance(currencyCode.toUpperCase());
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public Map<String, String> getExtendedFields() {
        return this.extendedFields;
    }

    public void setExtendedFields(Map<String, String> extendedFields) {
        this.extendedFields = extendedFields;
    }

    @Override
    public String toString() {
        return  "{ id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", left=" + left +
                ", extendedFields=" + extendedFields + " }";
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
