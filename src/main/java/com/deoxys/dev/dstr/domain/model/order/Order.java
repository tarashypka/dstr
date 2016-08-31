package com.deoxys.dev.dstr.domain.model.order;

import com.deoxys.dev.dstr.domain.model.item.Item;
import com.deoxys.dev.dstr.domain.model.item.Price;
import com.deoxys.dev.dstr.domain.model.user.User;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Order implements Serializable {
    private String id;

    /**
     * long vs Long
     *   MongoDB Java driver takes and produces Long wrapper type
     *   thus, Long will avoid unnecessary autoboxing
     */
    private Long orderNumber;

    private Date date;
    private User customer;
    private Map<Item, Integer> items = new HashMap<>();
    private Receipt receipt;
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
        else if (amount > currAmount)
            throw new IllegalArgumentException("Not enough items in order");
        else items.put(item, currAmount - amount);
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    /**
     *
     * When new item is added/removed from the order,
     * #addItem or #removeItem methods should be called
     * after #updateReceipt method, since it checks
     * whether that item already was in the order
     *
     * @param item      item with price#currency that receipt be updated with
     * @param amount    amount of items for getting their the total price
     */
    public void updateReceipt(Item item, Integer amount) {
        Price price = item.getPrice();
        Double cash = price.getCash();
        Currency currency = price.getCurrency();

        Double oldCash = receipt.getCashFor(currency);
        Double newCash = amount * cash + (oldCash == null ? 0 : oldCash);

        // If item was already in receipt, the subtract it's old price
        if (items.containsKey(item)) newCash -= cash * items.get(item);

        if (newCash == 0) receipt.remove(currency);
        else if (newCash < 0)
            throw new IllegalArgumentException("Price is negative");
        else receipt.setCashFor(currency, newCash);
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