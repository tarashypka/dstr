package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by deoxys on 27.05.16.
 */

public class Order implements Serializable {
    private String id;
    private long orderNumber;
    private Date date;
    private Customer customer;
    private Map<Item, Integer> items;
    private Map<Currency, Double> receipt;
    private OrderStatus status;

    public Order() { }

    public Order(String id) {
        this.id = id;
    }

    public enum OrderStatus {
        REJECTED(-1), IN_PROCESS(0), PROCESSED(+1);

        private int value;
        private String name;    // for easy output with JSTL

        OrderStatus(int value) {
            this.value = value;
        }

        public static OrderStatus getStatus(int value) {
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

        public String getName() {
            return value == -1 ? "Rejected" : (value == 0 ? "In process" : "Processed");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
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

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public Item getItem(String id) {
        for (Item item : items.keySet())
            if (id.equals(item.getId())) return item;
        return null;
    }

    public void addItem(Item item, int amount) {
        updateReceipt(item, amount);
        items.put(item, amount);
    }

    public void removeItem(Item item, int amount) {
        updateReceipt(item, 0);
        int currAmount = items.get(item);
        if (amount == currAmount) items.remove(item);
        else items.put(item, currAmount - amount);
    }

    public Map<Currency, Double> getReceipt() {
        return receipt;
    }

    public void setReceipt(Map<Currency, Double> receipt) {
        this.receipt = receipt;
    }

    public void updateReceipt(Item item, int quantity) {
        Currency currency = item.getCurrency();
        Double oldTotal = receipt.get(currency);
        Double price = item.getPrice();
        Double newTotal = quantity * price + (oldTotal == null ? 0 : oldTotal);

        // If item was already in receipt
        if (items.containsKey(item)) newTotal -= price * items.get(item);

        if (newTotal == 0) receipt.remove(currency);
        else receipt.put(currency, newTotal);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  "{ id='" + id + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", date=" + date +
                ", customer=" + customer +
                ", items=" + items +
                ", receipt=" + receipt +
                ", status=" + status + " }";
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