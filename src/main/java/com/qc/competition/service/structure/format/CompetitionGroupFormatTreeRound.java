package com.qc.competition.service.structure.format;

import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.utils.Sets;

import java.text.NumberFormat;
import java.util.*;

public class CompetitionGroupFormatTreeRound implements Comparable<CompetitionGroupFormatTreeRound>, Cloneable {
    public int round;
    public SortedSet<CompetitionGroupFormatTreeMatch> competitionGroupFormatTreeMatches = new TreeSet<>();
    public int participantQuantity;


    @Override
    public int compareTo(CompetitionGroupFormatTreeRound o) {
        return Integer.compare(round, o.round);
    }


    public void sort() {
        Sets.sort(competitionGroupFormatTreeMatches);
    }


    public CompetitionGroupFormatTreeMatch addCompetitionGroupFormatTreeMatch() {
        CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch = new CompetitionGroupFormatTreeMatch();
        competitionGroupFormatTreeMatch.lane = competitionGroupFormatTreeMatches.size() + 1;
        competitionGroupFormatTreeMatches.add(competitionGroupFormatTreeMatch);
        return competitionGroupFormatTreeMatch;
    }
//    public void computeCompetitionGroupFormatTreeMatches(int numberOfParticipantPerMatch) {

    public void computeCompetitionGroupFormatTreeMatches(int numberOfParticipantPerMatch, int participantQualifiedPerMatch, CompetitionGroupFormat competitionGroupFormat, int lane, boolean fixedParticipantSize, boolean participantTypeSplittable) {
        int missingParticipants = 0;
        if (fixedParticipantSize) {
            int participantQuantityTemp = participantQuantity;
            int roundFullParticipantNumber = 1;
            roundFullParticipantNumber = numberOfParticipantPerMatch;
            while (participantQuantityTemp > roundFullParticipantNumber) {
                roundFullParticipantNumber *= (numberOfParticipantPerMatch / participantQualifiedPerMatch);
            }
            missingParticipants = (roundFullParticipantNumber - participantQuantityTemp);
            int numberOfMatch = (int) Math.ceil((double) (participantQuantityTemp + missingParticipants) / (double) numberOfParticipantPerMatch);
            for (int i = this.competitionGroupFormatTreeMatches.size(); i < numberOfMatch; i++) {
                this.addCompetitionGroupFormatTreeMatch();
            }
            boolean fullParticipant = true;
            int fullMatchQuantity = participantQuantityTemp / numberOfParticipantPerMatch;
            if (fullMatchQuantity == 0)
                fullMatchQuantity = 1;
            int fullMatchQuantityStep = competitionGroupFormatTreeMatches.size() / fullMatchQuantity;
            if (fullMatchQuantityStep == 0)
                fullMatchQuantityStep = 1;
            int index = 0;
            for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches) {
                fullParticipant = index == 0 || (index % fullMatchQuantityStep == 0);
                if (fullParticipant) {
                    for (int i = 0; i < numberOfParticipantPerMatch; i++) {
                        if (participantQuantityTemp > 0) {
                            competitionGroupFormatTreeMatch.participantQuantity++;
                            participantQuantityTemp--;
                        } else {
                            missingParticipants--;
                        }
                    }
                }
                index++;
            }
            while (participantQuantityTemp > 0) {
                List<CompetitionGroupFormatTreeMatch> competitionGroupFormatTreeMatchList = new ArrayList<>(competitionGroupFormatTreeMatches);
                if (round % 2 == 0 && numberOfParticipantPerMatch > 2)
                    Collections.reverse(competitionGroupFormatTreeMatchList);
                for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatchList) {
                    while (((competitionGroupFormatTreeMatch.participantQuantity < participantQualifiedPerMatch || (!participantTypeSplittable && competitionGroupFormatTreeMatch.participantQuantity < numberOfParticipantPerMatch)) && participantQuantityTemp > 0)) {
                        competitionGroupFormatTreeMatch.participantQuantity++;
                        participantQuantityTemp--;
                    }
                    if (participantQuantityTemp == 0) {
                        break;
                    }
                }
            }
            List<CompetitionGroupFormatTreeMatch> competitionGroupFormatTreeMatchList = new ArrayList<>(competitionGroupFormatTreeMatches);
            for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatchList) {
                if (competitionGroupFormatTreeMatch.participantQuantity == 0) {
                    this.removeCompetitionGroupFormatTreeMatch(competitionGroupFormatTreeMatch);
                }
            }

        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
            int roundFullParticipantNumber = 1;
            if (numberOfParticipantPerMatch > 1) {
                if (lane == 1 || round == 1) {
                    roundFullParticipantNumber = numberOfParticipantPerMatch;
                    while (participantQuantity > roundFullParticipantNumber) {
                        roundFullParticipantNumber *= (numberOfParticipantPerMatch / participantQualifiedPerMatch);
                    }
                } else {
                    roundFullParticipantNumber = 0;
                    do {
                        roundFullParticipantNumber += numberOfParticipantPerMatch;
                    } while (participantQuantity > roundFullParticipantNumber);

                }
                missingParticipants = (roundFullParticipantNumber - participantQuantity);
            }
            int numberOfMatch = (int) Math.ceil((double) (participantQuantity + missingParticipants) / (double) numberOfParticipantPerMatch);
            for (int i = this.competitionGroupFormatTreeMatches.size(); i < numberOfMatch; i++) {
                this.addCompetitionGroupFormatTreeMatch();
            }
            int participantQuantityTemp = participantQuantity;
            while (participantQuantityTemp > 0) {
                for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches) {
                    if (missingParticipants > 0) {
                        competitionGroupFormatTreeMatch.participantQuantity++;
                        competitionGroupFormatTreeMatch.missingParticipants++;
                        missingParticipants--;
                    } else {
                        competitionGroupFormatTreeMatch.participantQuantity++;
                        participantQuantityTemp--;
                        if (participantQuantityTemp <= 0)
                            break;
                    }
                }
            }
        } else {
            missingParticipants = (numberOfParticipantPerMatch - participantQuantity % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;
            int numberOfMatch = (int) Math.ceil((double) (participantQuantity + missingParticipants) / (double) numberOfParticipantPerMatch);
            for (int i = this.competitionGroupFormatTreeMatches.size(); i < numberOfMatch; i++) {
                this.addCompetitionGroupFormatTreeMatch();
            }
            int participantQuantityTemp = participantQuantity;
            while (participantQuantityTemp > 0 || missingParticipants > 0) {
                for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches) {
                    if (missingParticipants > 0) {
                        missingParticipants--;
                    } else if (participantQuantityTemp > 0) {
                        competitionGroupFormatTreeMatch.participantQuantity++;
                        participantQuantityTemp--;
                    }
                }
            }
            //reorder based on participantQuantity
            CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatchI = null;
            CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatchJ = null;
            CompetitionGroupFormatTreeMatch[] competitionGroupFormatTreeMatchesArray = new CompetitionGroupFormatTreeMatch[competitionGroupFormatTreeMatches.size()];
            competitionGroupFormatTreeMatches.toArray(competitionGroupFormatTreeMatchesArray);
            for (int i = 0; i < competitionGroupFormatTreeMatchesArray.length; i++) {
                competitionGroupFormatTreeMatchI = competitionGroupFormatTreeMatchesArray[i];
                for (int j = i + 1; j < competitionGroupFormatTreeMatches.size(); j++) {
                    competitionGroupFormatTreeMatchJ = competitionGroupFormatTreeMatchesArray[j];
                    if (competitionGroupFormatTreeMatchI.participantQuantity < competitionGroupFormatTreeMatchJ.participantQuantity) {
                        int participantQuantityI = competitionGroupFormatTreeMatchI.participantQuantity;
                        competitionGroupFormatTreeMatchI.participantQuantity = competitionGroupFormatTreeMatchJ.participantQuantity;
                        competitionGroupFormatTreeMatchJ.participantQuantity = participantQuantityI;
                    }
                }
            }
        }
    }

    private void removeCompetitionGroupFormatTreeMatch(CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch) {
        int lane = competitionGroupFormatTreeMatch.lane;
        competitionGroupFormatTreeMatches.remove(competitionGroupFormatTreeMatch);
        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatchCurrent : competitionGroupFormatTreeMatches) {
            if (competitionGroupFormatTreeMatchCurrent.lane > lane)
                competitionGroupFormatTreeMatchCurrent.lane--;

        }
    }

    public void computePlayQuantity(CompetitionGroupFormat competitionGroupFormat, ParticipantType participantType, int numberOfParticipantPerMatch,
                                    int participantQualifiedPerMatch, PlayVersusType playVersusType, int minPlayQuantity, int maxPlayQuantity,
                                    int laneQuantity, int lane, int roundQuantity, boolean allowEvenNumber, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch) {
        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches)
            competitionGroupFormatTreeMatch.computePlayQuantity(competitionGroupFormat, participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, minPlayQuantity, maxPlayQuantity, laneQuantity, lane, roundQuantity, round, allowEvenNumber, competitionGroupFormatTreeMatches.size(), finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch);

    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);
        stringBuilder.append("Round ").append(numberFormat.format(round)).append(" (").append(numberFormat.format(participantQuantity)).append("P / ").append(numberFormat.format(competitionGroupFormatTreeMatches.size())).append("m / ");
        if (competitionGroupFormatTreeMatches.size() > 0)
            stringBuilder.append(numberFormat.format(competitionGroupFormatTreeMatches.first().playQuantity));
        else
            stringBuilder.append(numberFormat.format(0));
        stringBuilder.append("p )");

//        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches)
//            stringBuilder.append(competitionGroupFormatTreeMatch.toString());
//        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public int computeNumberOfQualifiedParticipantPerRound(CompetitionGroupFormat competitionGroupFormat,
                                                           int numberOfParticipantPerMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {

        int numberOfQualifiedParticipantPerRound = 0;
        if (playVersusType.numberOfTeam == 1) {
            numberOfQualifiedParticipantPerRound = 1;
            while (numberOfQualifiedParticipantPerRound < competitionGroupFormatTreeMatches.size()) {
                numberOfQualifiedParticipantPerRound = numberOfQualifiedParticipantPerRound * 2;
            }
            numberOfQualifiedParticipantPerRound = numberOfQualifiedParticipantPerRound / 2;
        } else {
            for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches) {
                numberOfQualifiedParticipantPerRound += competitionGroupFormat.computeNumberOfQualifiedParticipantPerMatch(numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch, competitionGroupFormatTreeMatch.participantQuantity);
            }
        }
        return numberOfQualifiedParticipantPerRound;
    }


    @Override
    protected CompetitionGroupFormatTreeRound clone() throws CloneNotSupportedException {
        return (CompetitionGroupFormatTreeRound) super.clone();
    }

    public int computeNumberOfEliminatedParticipantPerRound(CompetitionGroupFormat competitionGroupFormat,
                                                            int numberOfParticipantPerMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        int numberOfEliminatedParticipantPerRound = participantQuantity - computeNumberOfQualifiedParticipantPerRound(competitionGroupFormat, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch);
        return numberOfEliminatedParticipantPerRound;
    }

    public void setCompetitionGroupFormatTreeMatchPlayQuantity(int playQuantity) {
        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches) {
            competitionGroupFormatTreeMatch.playQuantity = playQuantity;
        }
    }

    public int getNumberOfBye(int numberOfParticipantPerMatch, PlayVersusType playVersusType) {
        int numberOfBye = 0;
        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches)
            numberOfBye += competitionGroupFormatTreeMatch.getNumberOfBye(numberOfParticipantPerMatch, playVersusType);
        return numberOfBye;
    }

    public void setPlayQuantity(int playQuantity) {
        for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeMatches)
            competitionGroupFormatTreeMatch.playQuantity = playQuantity;
    }
}
