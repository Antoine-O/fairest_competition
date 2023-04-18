package com.qc.competition.service.structure.format;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.utils.Sets;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 19/10/2015.
 */
public class CompetitionGroupFormatTree {
    private final int numberOfPlayPerMatchMinimum;
    private final int numberOfPlayPerMatchMaximum;
    public CompetitionGroupFormat competitionGroupFormat;
    public ParticipantType participantType;
    public int participantQualifiedPerMatch;
    public int numberOfParticipantPerMatch;
    public PlayVersusType playVersusType;
    public SortedSet<CompetitionGroupFormatTreeGroup> competitionGroupFormatTreeGroups = new TreeSet<>();
    public boolean allowEvenNumber;
    public boolean finalGroupSizeThresholdEnabled;
    public int finalGroupSizeThreshold;
    public int numberOfPlayPerMatchMinimumFinalGroup;
    public int numberOfPlayPerMatchMaximumFinalGroup;
    public boolean thirdPlaceMatch;

    public CompetitionGroupFormatTree(CompetitionGroupFormat competitionGroupFormat, ParticipantType participantType, int numberOfParticipantPerMatch, int participantQualifiedPerMatch, PlayVersusType playVersusType, int numberOfPlayPerMatchMinimum, int numberOfPlayPerMatchMaximum, boolean allowEvenNumber, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch) {
        this.competitionGroupFormat = competitionGroupFormat;
        this.participantType = participantType;
        this.numberOfParticipantPerMatch = numberOfParticipantPerMatch;
        this.participantQualifiedPerMatch = participantQualifiedPerMatch;
        this.playVersusType = playVersusType;
        this.numberOfPlayPerMatchMinimum = numberOfPlayPerMatchMinimum;
        this.numberOfPlayPerMatchMaximum = numberOfPlayPerMatchMaximum;
        this.allowEvenNumber = allowEvenNumber;
        this.finalGroupSizeThresholdEnabled = finalGroupSizeThresholdEnabled;
        this.finalGroupSizeThreshold = finalGroupSizeThreshold;
        this.numberOfPlayPerMatchMinimumFinalGroup = numberOfPlayPerMatchMinimumFinalGroup;
        this.numberOfPlayPerMatchMaximumFinalGroup = numberOfPlayPerMatchMaximumFinalGroup;
        this.thirdPlaceMatch = thirdPlaceMatch;

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CompetitionGroupFormatTree").append(System.lineSeparator());
        stringBuilder.append("competitionGroupFormat                = ").append(competitionGroupFormat).append(System.lineSeparator());
        stringBuilder.append("participantType                       = ").append(participantType).append(System.lineSeparator());
        stringBuilder.append("numberOfParticipantPerMatch           = ").append(numberOfParticipantPerMatch).append(System.lineSeparator());
        stringBuilder.append("participantQualifiedPerMatch          = ").append(participantQualifiedPerMatch).append(System.lineSeparator());
        stringBuilder.append("playVersusType                        = ").append(playVersusType).append(System.lineSeparator());
        stringBuilder.append("numberOfPlayPerMatchMinimum           = ").append(numberOfPlayPerMatchMinimum).append(System.lineSeparator());
        stringBuilder.append("allowEvenNumber                       = ").append(allowEvenNumber).append(System.lineSeparator());
        stringBuilder.append("finalGroupSizeThresholdEnabled        = ").append(finalGroupSizeThresholdEnabled).append(System.lineSeparator());
        stringBuilder.append("finalGroupSizeThreshold               = ").append(finalGroupSizeThreshold).append(System.lineSeparator());
        stringBuilder.append("numberOfPlayPerMatchMinimumFinalGroup = ").append(numberOfPlayPerMatchMinimumFinalGroup).append(System.lineSeparator());
        stringBuilder.append("numberOfPlayPerMatchMaximumFinalGroup = ").append(numberOfPlayPerMatchMaximumFinalGroup).append(System.lineSeparator());
        stringBuilder.append("thirdPlaceMatchEnabled                       = ").append(thirdPlaceMatch).append(System.lineSeparator());
        for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTreeGroups)
            stringBuilder.append(competitionGroupFormatTreeGroup.toString());
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public void sort() {
        Sets.sort(competitionGroupFormatTreeGroups);
        for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTreeGroups)
            competitionGroupFormatTreeGroup.sort();
    }

    public CompetitionGroupFormatTreeGroup addCompetitionGroupFormatTreeGroup() {
        CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup = new CompetitionGroupFormatTreeGroup();
        competitionGroupFormatTreeGroup.lane = competitionGroupFormatTreeGroups.size() + 1;
        competitionGroupFormatTreeGroups.add(competitionGroupFormatTreeGroup);
        return competitionGroupFormatTreeGroup;
    }


    public CompetitionGroupFormatTreeGroup getCompetitionGroupFormatTreeGroup(int lane) {
        for (CompetitionGroupFormatTreeGroup eliminationTreeGroup : competitionGroupFormatTreeGroups)
            if (eliminationTreeGroup.lane == lane)
                return eliminationTreeGroup;
        return null;
    }

    public void computeInitialGroupParticipant(int numberOfParticipant, int numberOfParticipantInMatch, int numberOfParticipantQualifiedPerMatch) {
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
//            int participantPairingSeatsNeededQuantity = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(numberOfParticipantInMatch, numberOfParticipantQualifiedPerMatch, numberOfParticipant,false);
            getCompetitionGroupFormatTreeGroup(1).participantQuantity = numberOfParticipant;
        } else {
            int numberOfParticipantMissing = (participantQualifiedPerMatch - (numberOfParticipant % participantQualifiedPerMatch)) % participantQualifiedPerMatch;
            while (numberOfParticipant > 0) {
                Sets.sort(competitionGroupFormatTreeGroups);
                for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTreeGroups) {
                    if (numberOfParticipant > 0) {
                        competitionGroupFormatTreeGroup.participantQuantity++;

                        if (numberOfParticipantMissing > 0) {
                            numberOfParticipantMissing--;
                            competitionGroupFormatTreeGroup.participantQuantity--;
                        } else {
                            numberOfParticipant--;
                        }
                    } else {
                        break;
                    }
                }

//                for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTreeGroups) {
//                    for (int i = 1; i < numberOfParticipantPerMatch; i++) {
//
//                    }
//                    if(competitionGroupFormatTreeGroup.participantQuantity % numberOfParticipantPerMatch <= participantQualifiedPerMatch && ){
//
//                    }
//                }
            }
        }
    }

    public void computePlayQuantity() {

        for (CompetitionGroupFormatTreeGroup eliminationTreeGroup : competitionGroupFormatTreeGroups)
            eliminationTreeGroup.computePlayQuantity(competitionGroupFormat, participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, competitionGroupFormatTreeGroups.size(), allowEvenNumber, finalGroupSizeThresholdEnabled,
                    finalGroupSizeThreshold,
                    numberOfPlayPerMatchMinimumFinalGroup,
                    numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch
            );
        if (thirdPlaceMatch && competitionGroupFormatTreeGroups.size() == 2 && competitionGroupFormatTreeGroups.last().competitionGroupFormatTreeRounds.size() == 1) {
            CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = competitionGroupFormatTreeGroups.last().competitionGroupFormatTreeRounds.last();
            competitionGroupFormatTreeRound.competitionGroupFormatTreeMatches.first().playQuantity = competitionGroupFormatTreeGroups.first().competitionGroupFormatTreeRounds.last().competitionGroupFormatTreeMatches.first().playQuantity;

        }
    }

    public int getCompetitionRoundQuantityMaximum() {
        int competitionRoundQuantity = 0;
        for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTreeGroups)
            competitionRoundQuantity = Math.max(competitionRoundQuantity, competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.size());
        return competitionRoundQuantity;
    }

    public int getNumberOfBye() {
        int numberOfBye = 0;
        for (CompetitionGroupFormatTreeGroup eliminationTreeGroup : competitionGroupFormatTreeGroups)
            numberOfBye += eliminationTreeGroup.getNumberOfBye(numberOfParticipantPerMatch, playVersusType);
        return numberOfBye;
    }

    public void setPlayQuantity(int playQuantity) {
        for (CompetitionGroupFormatTreeGroup eliminationTreeGroup : competitionGroupFormatTreeGroups)
            eliminationTreeGroup.setPlayQuantity(playQuantity);
    }

    public void doubleRound() {
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0)
            for (CompetitionGroupFormatTreeGroup eliminationTreeGroup : competitionGroupFormatTreeGroups)
                eliminationTreeGroup.doubleRound();
    }
}
