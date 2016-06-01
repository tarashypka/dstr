package com.dstr.model;

import java.io.Serializable;
import java.util.*;

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
    private OrderStatus status;

    public Order() { }

    public Order(String id) {
        this.id = id;
    }

    public enum OrderStatus {
        REJECTED(-1), IN_PROCESS(0), PROCESSED(+1);

        private int value;

        private OrderStatus(int value) {
            this.value = value;
        }

        public static OrderStatus orderStatusbyValue(int value) {
            switch (value) {
                case -1:
                    return OrderStatus.REJECTED;
                case 0:
                    return OrderStatus.IN_PROCESS;
                case +1:
                    return OrderStatus.PROCESSED;
                default:
                    return null;
            }
        }

        public int getValue() {
            return value;
        }
    }

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}