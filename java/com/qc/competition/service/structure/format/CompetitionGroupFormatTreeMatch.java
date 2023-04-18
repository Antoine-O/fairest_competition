package com.qc.competition.service.structure.format;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.structure.CompetitionGroupFormat;

public class CompetitionGroupFormatTreeMatch implements Comparable<CompetitionGroupFormatTreeMatch> {
    public int lane;
    public int participantQuantity = 0;
    public int playQuantity = 0;
    public int missingParticipants = 0;

    @Override
    public int compareTo(CompetitionGroupFormatTreeMatch o) {
        return Integer.compare(lane, o.lane);
    }


    public void computePlayQuantity(CompetitionGroupFormat competitionGroupFormat, ParticipantType participantType, int numberOfParticipantPerMatch, int participantQualifiedPerMatch, PlayVersusType playVersusType, int minPlayQuantity, int maxPlayQuantity, int laneQuantity, int lane, int roundQuantity, int round, boolean allowEvenNumber, int matchQuantity, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch) {
        playQuantity = competitionGroupFormat.computePlayQuantity(participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, minPlayQuantity, maxPlayQuantity, laneQuantity, lane, roundQuantity, round, allowEvenNumber, matchQuantity, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch);
    }


    @Override
    public String toString() {
        return "Match " + lane + " (" + participantQuantity + ")" + " playDetails : " + playQuantity + System.lineSeparator();
    }

    public int getNumberOfBye(int numberOfParticipantPerMatch, PlayVersusType playVersusType) {
        return numberOfParticipantPerMatch > 1 && participantQuantity == 1 ? 1 : 0;
    }
}