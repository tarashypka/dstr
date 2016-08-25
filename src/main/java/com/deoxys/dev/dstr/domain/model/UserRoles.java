package com.deoxys.dev.dstr.domain.model;

import static com.deoxys.dev.dstr.domain.model.UserRole.ADMIN;
import static com.deoxys.dev.dstr.domain.model.UserRole.CUSTOMER;

public class UserRoles {
    public static UserRole roleOf(String name) {
        switch (name) {
            case "admin":
                return ADMIN;
            case "customer":
                return CUSTOMER;
            default:
                return null;
        }
    }
}
