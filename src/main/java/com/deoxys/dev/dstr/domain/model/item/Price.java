package com.deoxys.dev.dstr.domain.model.item;

import java.util.Currency;

public final class Price {

    /**
     * double vs Double
     *   MongoDB Java driver takes and produces Double wrapper type,
     *   thus, Double will avoid unnecessary autoboxing
     *
     * Further, in case of heavy computations (taxes, discounts)
     * Double type could be changed into preferable for it BigDecimal
     * with all caveats taken into account.
     */
    private Double cash;
    private Currency currency;

    @Override
    public String toString() {
        return  "{ cash=" + cash +
                ", currency=" + currency + '}';
    }

    public Price(Double cash, Currency currency) {
        this.cash = cash;
        this.currency = currency;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        return currency.equals(price.currency);

    }

    @Override
    public int hashCode() {
        return currency.hashCode();
    }
}
