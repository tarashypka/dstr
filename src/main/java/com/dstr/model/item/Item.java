package com.dstr.model.item;

import java.io.Serializable;
import java.util.Currency;
import java.util.Map;

public class Item implements Serializable {
    private String id;
    private String category;
    private double price;
    private Currency currency;
    private Map<String, String> extendedFields;

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

    public Map<String, String> getExtendedFields() {
        return this.extendedFields;
    }

    public void setExtendedFields(Map<String, String> extendedFields) {
        this.extendedFields = extendedFields;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", extendedFields=" + extendedFields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (Double.compare(item.price, price) != 0) return false;
        if (category != null ? !category.equals(item.category) : item.category != null) return false;
        if (currency != null ? !currency.equals(item.currency) : item.currency != null) return false;
        if (extendedFields != null ? !extendedFields.equals(item.extendedFields) : item.extendedFields != null)
            return false;
        if (id != null ? !id.equals(item.id) : item.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (extendedFields != null ? extendedFields.hashCode() : 0);
        return result;
    }
}
