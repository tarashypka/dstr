package com.dstr.model;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class Order implements Serializable {
    private String id;
    private String orderNumber;
    private Date date;
    private Customer customer;
    private Map<String, Integer> items;
    private Map<Double, Currency> receipt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public Map<Double, Currency> getReceipt() {
        return receipt;
    }

    public void setReceipt(Map<Double, Currency> receipt) {
        this.receipt = receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (date != null ? !date.equals(order.date) : order.date != null) return false;
        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (items != null ? !items.equals(order.items) : order.items != null) return false;
        if (orderNumber != null ? !orderNumber.equals(order.orderNumber) : order.orderNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}