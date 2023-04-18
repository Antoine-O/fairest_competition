package com.qc.competition.service.structure;

import org.goochjs.glicko2.Rating;
import org.goochjs.glicko2.RatingCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

public class GlickoRatingComparator implements Comparator<Rating> {
    static final RatingCalculator RATING_CALCULATOR_GLOBAL = new RatingCalculator(0.06, 0.5);

    public static BigDecimal getComparisonFormulaResult(Rating rating) {
        BigDecimal volatility = BigDecimal.valueOf(rating.getVolatility());
        BigDecimal ratingDeviation = BigDecimal.valueOf(rating.getRatingDeviation());
        BigDecimal ratingValue = BigDecimal.valueOf(rating.getRating());
        BigDecimal numberOfResults = BigDecimal.valueOf(rating.getNumberOfResults());
        if (numberOfResults.compareTo(BigDecimal.ZERO) == 0)
            numberOfResults = BigDecimal.ONE;
        return ratingValue.add(ratingDeviation.negate().divide(numberOfResults.multiply(volatility), RoundingMode.UP));
    }

    public static Rating getInitialGlobalRatingFor(String uid) {
        return getInitialRatingFor(RATING_CALCULATOR_GLOBAL, uid);
    }

    public static Rating getInitialRatingFor(RatingCalculator ratingCalculator, String uid) {
        Rating rating = new Rating(uid, ratingCalculator);
        return rating;

    }

    public static Rating getRatingFor(RatingCalculator ratingCalculator, String uid, double ratingValue, double ratingDeviation, double volatility, int numberOfResults) {
        Rating rating = new Rating(uid, ratingCalculator, ratingValue, ratingDeviation, volatility);
        rating.incrementNumberOfResults(numberOfResults);
        return rating;

    }

    public static Rating getGlobalRatingFor(String uid, double ratingValue, double ratingDeviation, double volatility, int numberOfResults) {
        return getRatingFor(RATING_CALCULATOR_GLOBAL, uid, ratingValue, ratingDeviation, volatility, numberOfResults);
    }

    @Override
    public int compare(Rating o1, Rating o2) {
        return -getComparisonFormulaResult(o1).compareTo(getComparisonFormulaResult(o2));
    }

}
