package com.qc.competition.service.structure;

import org.goochjs.glicko2.Rating;

public class ParticipantScoreRating {
    public double rating;
    public double ratingDeviation;
    public double volatility;
    public int numberOfResults;

    public ParticipantScoreRating() {
        Rating rating = GlickoRatingComparator.getInitialGlobalRatingFor("0");
        this.rating = rating.getRating();
        this.ratingDeviation = rating.getRatingDeviation();
        this.volatility = rating.getVolatility();
        this.numberOfResults = rating.getNumberOfResults();
    }

    public ParticipantScoreRating(double rating, double ratingDeviation, double volatility, int numberOfResults) {
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.numberOfResults = numberOfResults;
    }
}
