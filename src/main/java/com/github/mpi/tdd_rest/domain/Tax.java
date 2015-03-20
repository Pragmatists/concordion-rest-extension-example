package com.github.mpi.tdd_rest.domain;

import java.text.NumberFormat;
import java.text.ParseException;

public class Tax {

    private float rate;

    private Tax(float rate) {
        this.rate = rate;
    }

    public static Tax of(String rate) {
        try {
            Number r = NumberFormat.getPercentInstance().parse(rate);
            return new Tax(r.floatValue());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Money calculate(Money price) {
        return price.multiplyBy(rate);
    }

    @Override
    public String toString() {
        return String.format("%.0f%%", rate*100);
    }
}
