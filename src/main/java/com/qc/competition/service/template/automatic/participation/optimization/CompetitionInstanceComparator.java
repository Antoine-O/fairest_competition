package com.qc.competition.service.template.automatic.participation.optimization;

import com.qc.competition.service.structure.CompetitionInstance;
import com.qc.competition.service.structure.Duration;

import java.util.Comparator;

/**
 * Created by Duncan on 24/09/2015.
 */
public class CompetitionInstanceComparator implements Comparator<CompetitionInstance> {
    Duration competitionDuration;

    public CompetitionInstanceComparator(Duration competitionDuration) {
        this.competitionDuration = competitionDuration;
    }


    @Override
    public int compare(CompetitionInstance competitionInstance1, CompetitionInstance competitionInstance2) {
        int compare = 0;


        Duration expectedGlobalAverageDuration1 = competitionInstance1.expectedDuration;
        Duration expectedGlobalAverageDuration2 = competitionInstance2.expectedDuration;
        Duration expectedGlobalAverageDurationGap1 = expectedGlobalAverageDuration1.minus(competitionDuration);
        Duration expectedGlobalAverageDurationGap2 = expectedGlobalAverageDuration2.minus(competitionDuration);
        Duration durationZero = new Duration(java.time.Duration.ZERO);
        long multiplicator = (competitionDuration.duration.toHours() / 10) + 1;
        if (expectedGlobalAverageDurationGap1.compareTo(durationZero) > 0) {
            expectedGlobalAverageDurationGap1 = expectedGlobalAverageDurationGap1.multipliedBy(multiplicator);
        } else {
            expectedGlobalAverageDurationGap1 = expectedGlobalAverageDurationGap1.abs();
        }
        if (expectedGlobalAverageDurationGap2.compareTo(durationZero) > 0) {
            expectedGlobalAverageDurationGap2 = expectedGlobalAverageDurationGap2.multipliedBy(multiplicator);
        } else {
            expectedGlobalAverageDurationGap2 = expectedGlobalAverageDurationGap2.abs();
        }

//        Duration expectedGlobalMaximumDuration1 = competitionInstance1.getExpectedGlobalMaximumDuration();
//        Duration expectedGlobalMaximumDuration2 = competitionInstance2.getExpectedGlobalMaximumDuration();
//        Duration expectedGlobalMaximumDurationGap1 = expectedGlobalMaximumDuration1.minus(competitionDuration).abs();
//        Duration expectedGlobalMaximumDurationGap2 = expectedGlobalMaximumDuration2.minus(competitionDuration).abs();
//
//
//        Duration expectedGlobalMinimumDuration1 = competitionInstance1.getExpectedGlobalMinimumDuration();
//        Duration expectedGlobalMinimumDuration2 = competitionInstance2.getExpectedGlobalMinimumDuration();
//        Duration expectedGlobalMinimumDurationGap1 = expectedGlobalMinimumDuration1.minus(competitionDuration).abs();
//        Duration expectedGlobalMinimumDurationGap2 = expectedGlobalMinimumDuration2.minus(competitionDuration).abs();

//        Duration expectedGlobalGap1 = expectedGlobalAverageDurationGap1;
//        Duration expectedGlobalGap2 = expectedGlobalAverageDurationGap2;
//
//        Duration expectedGlobalGap1 = expectedGlobalAverageDurationGap1.plus(expectedGlobalMaximumDurationGap1.dividedBy(2));
//        Duration expectedGlobalGap2 = expectedGlobalAverageDurationGap2.plus(expectedGlobalMaximumDurationGap2.dividedBy(2));

//        Duration expectedGlobalGap1 = expectedGlobalAverageDurationGap1.plus(expectedGlobalMaximumDurationGap1.dividedBy(2)).plus(expectedGlobalMinimumDurationGap1.dividedBy(4));
//        Duration expectedGlobalGap2 = expectedGlobalAverageDurationGap2.plus(expectedGlobalMaximumDurationGap2.dividedBy(2)).plus(expectedGlobalMinimumDurationGap2.dividedBy(4));

        compare = expectedGlobalAverageDurationGap1.compareTo(expectedGlobalAverageDurationGap2);
        if (compare == 0) {
            if (compare == 0) {
                compare = -Long.compare(competitionInstance1.getExpectedParticipantMinimumPlay(), competitionInstance2.getExpectedParticipantMinimumPlay());
                if (compare == 0) {
                    compare = Long.compare(competitionInstance1.getExpectedParticipantMaximumPlay(), competitionInstance2.getExpectedParticipantMaximumPlay());
                    if (compare == 0) {
                        compare = -Long.compare(competitionInstance1.getExpectedParticipantAveragePlay(), competitionInstance2.getExpectedParticipantAveragePlay());
                        if (compare == 0) {
                            compare = Long.compare(Long.valueOf(competitionInstance1.localId), Long.valueOf(competitionInstance2.localId));
                        }
                    }
                }
            }
        }
        return compare;
    }


}