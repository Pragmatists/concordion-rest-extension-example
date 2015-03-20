package com.github.mpi.tdd_rest.domain;

import java.math.BigDecimal;
import java.util.Locale;

public class Money {

    private int cents;
    private Currency currency;

    private Money(int cents, Currency currency) {
        this.cents = cents;
        this.currency = currency;
    }

    public static Money eur(String value) {
        
        BigDecimal cents = new BigDecimal(value).multiply(new BigDecimal(100));
        
        return new Money(cents.intValueExact(), Currency.EUR);
    }

    public Money multiplyBy(float multiplier) {
        return new Money(Math.round(cents * multiplier), currency);
        
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.2f %s", (float)cents/100, currency);
    }
}
