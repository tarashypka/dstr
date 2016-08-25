package com.deoxys.dev.dstr.domain.model;

public enum UserRole {
    ADMIN, CUSTOMER;

    @Override
    public String toString() {
        return this.name();
    }
}
