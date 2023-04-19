package com.qc.competition.service.structure;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Duncan on 05/01/2015.
 */
public enum Unit {
    ABSOLUTE, PERCENTAGE;

    public int toQuantity(int value, int size) {
        int returnedValue = 0;
        switch (this) {
            case ABSOLUTE:
                returnedValue = Math.min(value, size);
                break;
            case PERCENTAGE:
                returnedValue = BigDecimal.valueOf(size).multiply(BigDecimal.valueOf(value)).divide(BigDecimal.valueOf(100), RoundingMode.UP).intValue();
                break;
        }
        return returnedValue;
    }
}

