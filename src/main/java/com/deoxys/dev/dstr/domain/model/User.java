package com.deoxys.dev.dstr.domain.model;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.deoxys.dev.dstr.domain.model.UserRole.ADMIN;
import static com.deoxys.dev.dstr.domain.model.UserRole.CUSTOMER;

public class User implements Serializable {

    /**
     * long vs Long
     *   JDBC methods takes and produces long primitive type
     *   MongoDB Java driver takes and produces Long wrapper type
     *
     * Leave it long for now
     * In future: unique Customer String identification (login)
     */
    private long id;

    private String email;
    private String password;
    private String name;
    private String surname;
    private String errType;

    private UserRole role;

    /**
     * boolean vs Boolean
     *   JDBC methods take and produce boolean primitive type
     */
    private boolean enabled;

    private List<Order> orders;

    private Map<Item, Integer> items;

    public static class UserBuilder implements Builder<User> {
        // required fields
        private String email;

        // optional fields
        private long id;
        private String password;
        private String name;
        private String surname;
        private String errType = null;

        private UserRole role = CUSTOMER;

        private boolean enabled = false;

        private List<Order> orders = new ArrayList<>();

        private Map<Item, Integer> items = new HashMap<>();

        public UserBuilder(String email) {
            this.email = email;
        }

        public UserBuilder withId(long id) {
            this.id = id;
            return this;
        }
        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder withName(String name, String surname) {
            this.name = name;
            this.surname = surname;
            return this;
        }
        public UserBuilder withErrType(String errType) {
            this.errType = errType;
            return this;
        }
        public UserBuilder withRole(UserRole role) {
            this.role = role;
            return this;
        }
        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        public UserBuilder enabled() {
            this.enabled = true;
            return this;
        }
        public UserBuilder disabled() {
            this.enabled = false;
            return this;
        }
        public UserBuilder withOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }
        public UserBuilder withItems(Map<Item, Integer> items) {
            this.items = items;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    private User(UserBuilder builder) {
        email = builder.email;
        password = builder.password;
        name = builder.name;
        surname = builder.surname;
        errType = builder.errType;
        role = builder.role;
        enabled = builder.enabled;
        orders = builder.orders;
        items = builder.items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean hasValidPassword(String hashFromDb) {
        return BCrypt.checkpw(password, hashFromDb);
    }

    /**
     * Customer password is not required for view purposes
     */
    public User withoutPassword() {
        password = null;
        return this;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) { this.role = role; }

    public boolean isCustomer() {
        return role.equals(CUSTOMER);
    }

    public boolean isAdmin() {
        return role.equals(ADMIN);
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

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
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

        User user = (User) o;

        return email != null ? email.equals(user.email) : user.email == null;

    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}