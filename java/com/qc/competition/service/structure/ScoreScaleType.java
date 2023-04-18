package com.qc.competition.service.structure;

/**
 * Created by Duncan on 15/02/2015.
 */
public enum ScoreScaleType {
    ABSOLUTE_INTEGER(Integer.MAX_VALUE, 0, 0, Ordering.ASCENDING),
    ABSOLUTE_INTEGER_REVERSED(Integer.MAX_VALUE, 0, 0, Ordering.DESCENDING),
    ABSOLUTE_NUMERIC(Integer.MAX_VALUE, Integer.MIN_VALUE, 2, Ordering.ASCENDING),
    ABSOLUTE_NUMERIC_REVERSED(Integer.MAX_VALUE, Integer.MIN_VALUE, 2, Ordering.DESCENDING),
    NOTATION_10(10, 0, 1, Ordering.ASCENDING),
    NOTATION_10_REVERSED(10, 0, 1, Ordering.DESCENDING),
    NOTATION_100(100, 0, 0, Ordering.ASCENDING),
    NOTATION_100_REVERSED(100, 0, 0, Ordering.DESCENDING);

    public Integer maxValue;
    public Integer minValue;
    public Integer precision;
    public Ordering ordering = Ordering.ASCENDING;

    ScoreScaleType(Integer maxValue, Integer minValue, Integer precision, Ordering ordering) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.precision = precision;
        this.ordering = ordering;
    }

    int compareValue(String value1, String value2) {
        int comparisonValue = 0;
        if (value2 == null) {
            if (value1 == null) {
                comparisonValue = 0;

            } else {
                comparisonValue = -1;

            }
        } else {
            if (value1 == null) {
                comparisonValue = 1;
            } else {
                comparisonValue = getComparisonValueFor(value1).compareTo(getComparisonValueFor(value2));
                if (ordering.equals(Ordering.DESCENDING))
                    comparisonValue = -comparisonValue;

            }
        }
        return comparisonValue;
    }

    private Integer getComparisonValueFor(String value) {
        Double doubleValue;
        if (value == null)
            if (ordering.equals(Ordering.ASCENDING))
                doubleValue = this.maxValue.doubleValue();
            else
                doubleValue = this.minValue.doubleValue();
        else
            doubleValue = Double.parseDouble(value) * 1000;
        Integer integerValue = doubleValue.intValue();
        return integerValue;
    }

    private Double getDoubleValueFor(String value) {
        Double doubleValue;
        if (value == null)
//            if (ordering.equals(Ordering.ASCENDING))
//                doubleValue = Double.MAX_VALUE;
//            else
            doubleValue = 0.0;
        else
            doubleValue = Double.parseDouble(value);
        return doubleValue;
    }

    private Integer getIntegerValueFor(String value) {
        Integer integerValue;
        if (value == null)
//            if (ordering.equals(Ordering.ASCENDING))
//                integerValue = this.minValue;
//            else
            integerValue = 0;
        else
            integerValue = Double.valueOf(value).intValue();
        return integerValue;
    }


    public Number getValueFor(Number value) {
        return getValueFor(value == null ? null : value.toString());
    }

    public Number getValueFor(String value) {
        if (precision == 0)
            return getIntegerValueFor(value);
        return getDoubleValueFor(value);
    }

    public boolean isAscending() {
        return ordering.compareTo(Ordering.ASCENDING) == 0;
    }
}

