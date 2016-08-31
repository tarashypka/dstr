package com.deoxys.dev.dstr.domain.model.order;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * Receipt type represents real receipt, where prices are grouped by their currencies.
 *
 * F.e If there are 3 items in order with prices 2 USD, 3 USD and 4 GBP,
 *     than receipt should contain entries 5 USD and 4 GBP.
 *
 */
public final class Receipt {

    private Map<Currency, Double> totalPrice = new HashMap<>();

    public Double getCashFor(Currency currency) {
        return totalPrice.get(currency);
    }

    public void setCashFor(Currency currency, Double cash) {
        this.totalPrice.put(currency, cash);
    }

    public Double remove(Currency c) {
        return totalPrice.remove(c);
    }

    public Set<Currency> getCurrencies() {
        return totalPrice.keySet();
    }
}
