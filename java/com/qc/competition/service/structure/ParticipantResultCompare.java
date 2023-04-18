package com.qc.competition.service.structure;

import java.util.Comparator;
import java.util.List;

public class ParticipantResultCompare implements Comparator<ParticipantResult> {
    private final List<String> scores;

    public ParticipantResultCompare(List<String> scores) {
        this.scores = scores;
    }

    @Override
    public int compare(ParticipantResult o1, ParticipantResult o2) {
        int result = 0;
        if (o1.rank != null) {
            if (o2.rank != null) {
                result = o1.rank.compareTo(o2.rank);
            } else {
                result = 1;
            }
        } else {
            if (o2.rank != null) {
                result = -1;
            } else {
            }
        }
        if (result == 0) {
            result = o1.participantScore.compareScoreTo(o2.participantScore, scores);
        }
//        if (result == 0) {
//            return o1.participant.compareTo(o2.participant);
//        }
        return result;
    }
}
