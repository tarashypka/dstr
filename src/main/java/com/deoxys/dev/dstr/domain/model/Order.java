package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.*;

public class Order implements Serializable {
    private String id;

    /**
     * long vs Long
     *   MongoDB Java driver takes and produces Long wrapper type
     *
     * Thus, in order to avoid redundant autoboxing, Long will be better
     */
    private Long orderNumber;

    private Date date;
    private User customer;
    private Map<Item, Integer> items = new HashMap<>();
    private Map<Currency, Double> receipt = new HashMap<>();
    private OrderStatus status;

    public Order() { }

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
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

    public void addItem(Item item, Integer amount) {
        items.put(item, amount);
    }

    public void removeItem(Item item, Integer amount) {
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

    public void addPrice(Currency c, Double d) {
        receipt.put(c, d);
    }

    public void updateReceipt(Item item, Integer amount) {
        Price price = item.getPrice();
        Double cash = price.getCash();
        Currency currency = price.getCurrency();

        Double oldTotal = receipt.get(currency);
        Double newTotal = amount * cash + (oldTotal == null ? 0 : oldTotal);

        // If item was already in receipt
        if (items.containsKey(item)) newTotal -= cash * items.get(item);

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