package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */

public class Customer implements Serializable {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String role;
    private boolean enabled;

    List<Order> orders;
    Map<Item, Integer> items;

    public Customer() { }

    public Customer(String email) {
        this.email = email;
    }

    public Customer(String email, String password, String name, String surname) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = "customer";
        this.enabled = true;
    }

    public Customer(String email, String password, String name, String surname,
                    String role, boolean enabled) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCustomer() {
        return role.equals("customer");
    }

    public boolean isAdmin() {
        return role.equals("admin");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public String toString() {
        return  "{ email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}