package com.deoxys.dev.dstr.domain.model.user;

import static com.deoxys.dev.dstr.domain.model.user.UserRole.ADMIN;
import static com.deoxys.dev.dstr.domain.model.user.UserRole.CUSTOMER;

public final class UserRoles {
    public static UserRole roleOf(String name) {
        switch (name) {
            case "ADMIN":
                return ADMIN;
            case "CUSTOMER":
                return CUSTOMER;
            default:
                return null;
        }
    }
}
