package com.qc.competition.service.structure;

import com.qc.competition.utils.MathUtils;

import java.util.Set;

/**
 * Created by Duncan on 01/04/2017.
 */
public class PointsCalculation {
    @Deprecated
    public static int getPoints(boolean bye, Integer rank, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        int points = 0;
        if (!bye) {
            points = getPoints(rank, participantsSize);
        } else {
            if (round <= maxRound / 2 + 1)
                points = participantsMaxSize;
        }
        return points;
    }

    @Deprecated
    public static int getPoints(Integer rank, Integer participantsSize) {
        double rankDouble = rank.doubleValue();
        double participantsSizeDouble = participantsSize.doubleValue();
        int points = (int) Math.round(Math.ceil(((participantsSizeDouble - rankDouble) * participantsSizeDouble + 1 + Math.ceil(1 - rankDouble / participantsSizeDouble)) / 2));

//        ARRONDI.SUP(((B$1-$A2)*B$1+1+ARRONDI.SUP(1-$A2/B$1;0))/2;0)

//        points = points + (participantsSize - 1) / rank;
//        if (points <= 0)
//            points = 1;
        return points;
    }
    public static boolean isDraw(Integer rank, Set<ParticipantResult> participantResults) {
        Integer previousRank = null;
        boolean draw = false;
        for (ParticipantResult participantResult : participantResults) {
            if (previousRank != null && previousRank.compareTo(participantResult.rank) == 0)
                draw = true;
            previousRank = participantResult.rank;
        }
        return draw;
    }

    public static int getPlayPoints(Integer rank, Set<ParticipantResult> participantResults) {
//        int points = 0;
//        if (participantResults.size() == 1) {
//            points = 0;
//        } else if (participantResults.size() == 2) {
//            boolean draw = isDraw(rank, participantResults);
//            if (draw) {
//                points = 2;
//            } else {
//                if (rank == 1) {
//                    points = 4;
//                } else if (rank == 2) {
//                    points = 1;
//                }
//            }
//        } else {
//            points = MathUtils.getRankingPoints(rank, participantResults.size());
//        }
        return getCommonPoints(rank, participantResults);
    }

    public static int getCommonPoints(Integer rank, Set<ParticipantResult> participantResults) {
        int points = 0;
        if (participantResults.size() == 1) {
            points = 0;
        } else if (participantResults.size() == 2) {
            boolean draw = isDraw(rank, participantResults);
            if (draw) {
                points = 2;
            } else {
                if (rank == 1) {
                    points = 4;
                } else if (rank == 2) {
                    points = 1;
                }
            }
        } else {
            points = MathUtils.getRankingPoints(rank, participantResults.size());
        }
        return points;
    }

//    public static int getCompetitionPoints(Integer rank, Integer participantsSize) {
//        int points = 0;
//        if (rank != 0) {
//            for (int i = 0; i < participantsSize - rank + 1; i++) {
//                points += participantsSize - rank + 1;
//            }
//
//        }
//        return points;
//    }
//

    public static int getMatchPoints(Integer rank, Set<ParticipantResult> participantResults, Integer minMatchQuantity, Integer matchPlayedQuantity, Integer numberOfParticipantQualified) {
 /*       int points = 0;
        if (rank != 0) {
            points = getPlayPoints(rank, participantResults);
            boolean draw = isDraw(rank, participantResults);
            if (draw) {

            } else {
//                if (numberOfParticipantQualified >= rank && matchPlayedQuantity > 1) {
//                    points BigDecimal bonus = BigDecimal.valueOf(points).multiply(BigDecimal.valueOf(minMatchQuantity)).divide(BigDecimal.valueOf(matchPlayedQuantity), 1, RoundingMode.HALF_UP);
//                    points += bonus.setScale(0, RoundingMode.HALF_UP).intValue();
//                }
            }
        }
        return points*/
        return getCommonPoints(rank, participantResults);
    }


    public static int getGroupPoints(Integer rank, Set<ParticipantResult> participantResults) {

        return getCommonPoints(rank, participantResults);
    }

    public static int getSeedPoints(Integer rank, Set<ParticipantResult> participantResults) {
        int points = getCommonPoints(rank, participantResults);
        return points;
    }

}
