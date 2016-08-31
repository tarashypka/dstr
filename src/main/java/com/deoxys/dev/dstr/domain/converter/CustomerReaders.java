package com.deoxys.dev.dstr.domain.converter;

import com.deoxys.dev.dstr.domain.model.user.Customer;

/**
 * Fabric of static methods for retrieving request, session readers
 * with desired behavior.
 *
 * Object could be retrieved from a reader with
 *   HttpRequestReader#read()
 *   HttpSessionReader#read()
 */
public class CustomerReaders {

    /**
     * Single static SessionReader to avoid code duplication in several methods like
     *      readerForAuthorization()
     *      readerForNewOrder()
     * that have exactly the same implementation (they are added to maintain readability).
     */
    private final static HttpSessionReader<Customer> rawSessionReader =
            ses -> (Customer) ses.getAttribute("customer");

    /**
     * For update account info purposes.
     * Customer can change his password, name, surname.
     */
    public static HttpRequestReader<Customer> readerForUpdate() {
        return req -> new Customer.CustomerBuilder(req.getParameter("email"))
                .withPassword(req.getParameter("password"))
                .withName(req.getParameter("name"), req.getParameter("surname"))
                .build();
    }

    /**
     * What should be stored in session is not fully clear yet.
     */
    public static HttpSessionReader<Customer> readerForAuthorization() {
        return rawSessionReader;
    }

    public static HttpSessionReader<Customer> readerForNewOrder() {
        return rawSessionReader;
    }
}
