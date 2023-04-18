package com.qc.competition.service.structure;

import org.goochjs.glicko2.Rating;

import java.util.Comparator;

public class GlickoParticipantResultComparator implements Comparator<ParticipantResult> {
    @Override
    public int compare(ParticipantResult o1, ParticipantResult o2) {
        Number o1Volatility = o1.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_VOLATILITY).computeNumberValue();
        Number o1RatingDeviation = o1.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING_DEVIATION).computeNumberValue();
        Number o1Rating = o1.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING).computeNumberValue();
        Number o1NumberOfResults = o1.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_NUMBER_OF_RESULTS).computeNumberValue();
        Number o2Volatility = o2.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_VOLATILITY).computeNumberValue();
        Number o2RatingDeviation = o2.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING_DEVIATION).computeNumberValue();
        Number o2Rating = o2.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING).computeNumberValue();
        Number o2NumberOfResults = o2.participantScore.getParticipantScoreValue(ParticipantScorePhase.GLICKO2_NUMBER_OF_RESULTS).computeNumberValue();
        GlickoRatingComparator glickoRatingComparator = new GlickoRatingComparator();
        Rating rating1 = GlickoRatingComparator.getGlobalRatingFor(o1.localId, o1Rating.doubleValue(), o1RatingDeviation.doubleValue(), o1Volatility.doubleValue(), o1NumberOfResults.intValue());
        Rating rating2 = GlickoRatingComparator.getGlobalRatingFor(o2.localId, o2Rating.doubleValue(), o2RatingDeviation.doubleValue(), o2Volatility.doubleValue(), o2NumberOfResults.intValue());
        return glickoRatingComparator.compare(rating1, rating2);
    }
}
