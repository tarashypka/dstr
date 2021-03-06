package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.domain.model.user.UserRole;

/**
 * Fabric of static methods for retrieving request, session readers
 * with desired behavior.
 *
 * Object could be retrieved from a reader with
 *   HttpRequestReader#read()
 *   HttpSessionReader#read()
 */
public final class UserReaders {

    /**
     * For authentication purposes only users credentials are required.
     */
    public static HttpRequestReader<User> readerForAuthentication() {
        return req -> new User.UserBuilder(req.getParameter("email"))
                .withPassword(req.getParameter("psswd"))
                .withRole(UserRole.CUSTOMER)        // default role for registration is CUSTOMER,
                .build();                           // ADMIN should be created in another way
    }

    /**
     * For authorization purposes, user's role (ADMIN, CUSTOMER)
     * and status (whether user is enabled) may be required.
     */
    public static HttpSessionReader<User> readerForAuthorization() {
        return ses -> (User) ses.getAttribute("user");
    }
}
