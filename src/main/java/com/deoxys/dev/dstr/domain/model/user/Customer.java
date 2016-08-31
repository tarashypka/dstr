package com.deoxys.dev.dstr.domain.model.user;

import com.deoxys.dev.dstr.domain.model.item.Item;
import com.deoxys.dev.dstr.domain.model.order.Order;

import java.util.*;

public final class Customer extends User {

    /**
     * For now, it's mandatory that Customer
     * specifies his names during registration.
     *
     * TODO: make names an optional feature in customer personal account page
     */
    private String name;
    private String surname;

    /**
     * All Orders that Customer made.
     *
     * Orders persist in MongoDB.
     */
    private List<Order> orders;

    /**
     * Amounts of Items that Customer already purchased,
     * thus, from orders with status PROCESSED.
     *
     * Items persist in MongoDB.
     */
    private Map<Item, Integer> items;

    /**
     * nOrders - total number of Orders that Customer made
     * nItems  - total number of Items  that Customer purchased
     *
     * nOrders persist in Postgres and is incremented on each new Order.
     * nItems  persist in Postgres and is
     *   incremented when Order status was changed to   PROCESSED
     *   decremented when Order status was changed from PROCESSED
     *
     * These decisions (to persist data in Postgres) are chosen
     * because of the fact that nOrders, nItems are highly used in presentation layer
     * and evaluating them each time with MongoDB queries may lead to performance overhead.
     */
    private int nOrders;
    private int nItems;

    private Date registeredOn;

    public final static class CustomerBuilder extends UserBuilder<CustomerBuilder> {

        // required fields

        // optional fields with their default values
        private String name;
        private String surname;
        private UserRole role = UserRole.CUSTOMER;
        private List<Order> orders = new ArrayList<>();
        private Map<Item, Integer> items = new HashMap<>();
        private int nOrders = 0;    // new Customer made no Orders yet
        private int nItems = 0;     // new Customer purchased no Items yet
                                    // is the Customer really new ???

        private Date registeredOn;

        public CustomerBuilder(String email) {
            super(email);
        }

        public CustomerBuilder withName(String name, String surname) {
            this.name = name;
            this.surname = surname;
            return this;
        }
        public CustomerBuilder withOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }
        public CustomerBuilder withItems(Map<Item, Integer> items) {
            this.items = items;
            return this;
        }
        public CustomerBuilder withNOrders(int nOrders) {
            this.nOrders = nOrders;
            return this;
        }
        public CustomerBuilder withNItems(int nItems) {
            this.nItems = nItems;
            return this;
        }
        public CustomerBuilder registeredOn(Date registeredOn) {
            this.registeredOn = registeredOn;
            return this;
        }

        @Override
        public Customer build() {
            return new Customer(this);
        }
    }

    private Customer(CustomerBuilder builder) {
        super(builder);
        name = builder.name;
        surname = builder.surname;
        orders = builder.orders;
        items = builder.items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public int getnOrders() {
        return nOrders;
    }

    public void setnOrders(int nOrders) {
        this.nOrders = nOrders;
    }

    public int getnItems() {
        return nItems;
    }

    public void setnItems(int nItems) {
        this.nItems = nItems;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    @Override
    public String toString() {
        return  "{ email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled + " }";
    }
}
