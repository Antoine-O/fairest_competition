package com.qc.competition.service.structure.format;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.utils.Sets;

import java.text.NumberFormat;
import java.util.*;

public class CompetitionGroupFormatTreeGroup implements Comparable<CompetitionGroupFormatTreeGroup> {
    public int lane;
    public boolean over = false;
    public SortedSet<CompetitionGroupFormatTreeRound> competitionGroupFormatTreeRounds = new TreeSet<>();
    public int participantQuantity;
    public Queue<Integer> participantsQueue = new LinkedList<>();

    @Override
    public int compareTo(CompetitionGroupFormatTreeGroup o) {
        return Integer.compare(lane, o.lane);
    }


    public void sort() {
        Sets.sort(competitionGroupFormatTreeRounds);
        for (CompetitionGroupFormatTreeRound eliminationTreeRound : competitionGroupFormatTreeRounds)
            eliminationTreeRound.sort();
    }


    public CompetitionGroupFormatTreeRound addCompetitionGroupFormatTreeRound() {
        CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = new CompetitionGroupFormatTreeRound();
        competitionGroupFormatTreeRound.round = competitionGroupFormatTreeRounds.size() + 1;
        competitionGroupFormatTreeRounds.add(competitionGroupFormatTreeRound);
        return competitionGroupFormatTreeRound;
    }


    public CompetitionGroupFormatTreeRound getCompetitionGroupFormatTreeRound(int round) {
        for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds)
            if (competitionGroupFormatTreeRound.round == round)
                return competitionGroupFormatTreeRound;
        return null;
    }

    public void computePlayQuantity(CompetitionGroupFormat competitionGroupFormat, ParticipantType participantType, int numberOfParticipantPerMatch, int participantQualifiedPerMatch, PlayVersusType playVersusType, int minPlayQuantity, int maxPlayQuantity, int laneQuantity, boolean allowEvenNumber, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup
            , boolean thirdPlaceMatch) {
        for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds)
            competitionGroupFormatTreeRound.computePlayQuantity(competitionGroupFormat, participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, minPlayQuantity, maxPlayQuantity, laneQuantity, lane, competitionGroupFormatTreeRounds.size(), allowEvenNumber, finalGroupSizeThresholdEnabled,
                    finalGroupSizeThreshold,
                    numberOfPlayPerMatchMinimumFinalGroup,
                    numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch
            );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);
        stringBuilder.append("Group ").append(numberFormat.format(lane)).append(" (").append(numberFormat.format(participantQuantity)).append("P / ").append(numberFormat.format(competitionGroupFormatTreeRounds.size())).append("r)").append("\t|\t");
        for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds)
            stringBuilder.append(competitionGroupFormatTreeRound.toString()).append("\t");
        stringBuilder.append(System.lineSeparator());
        for (Integer participentQueueElement : participantsQueue)
            stringBuilder.append(participentQueueElement).append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public void computeCompetitionGroupFormatTreeRound(CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroupNextLane, CompetitionGroupFormat competitionGroupFormat, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int participantOut, int participantQualifiedPerMatch, Integer numberOfRoundMinimum, Integer numberOfRoundMaximum, boolean fixedParticipantSize, boolean participantSplittable) {
        if (participantOut < participantQualifiedPerMatch)
            participantOut = participantQualifiedPerMatch;
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
            while (!over) {
                int nextRoundParticipantQuantity = 0;
                int nextLaneParticipantQuantity = 0;
                if (!this.competitionGroupFormatTreeRounds.isEmpty()) {
                    this.competitionGroupFormatTreeRounds.last().computeCompetitionGroupFormatTreeMatches(numberOfParticipantPerMatch, participantQualifiedPerMatch, competitionGroupFormat, lane, fixedParticipantSize, participantSplittable);
                    nextRoundParticipantQuantity = this.competitionGroupFormatTreeRounds.last().computeNumberOfQualifiedParticipantPerRound(competitionGroupFormat, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch);
                    nextLaneParticipantQuantity = this.competitionGroupFormatTreeRounds.last().computeNumberOfEliminatedParticipantPerRound(competitionGroupFormat, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch);
                }
//                int participantQuantity = nextRoundParticipantQuantity;
                if (!participantsQueue.isEmpty()) {
                    boolean append = false;
                    boolean forceExit = false;
                    do {
                        if (numberOfParticipantPerMatch == 1) {
                            append = false;
                            int nextValue = participantsQueue.iterator().next().intValue();
                            if (nextRoundParticipantQuantity == 0) {
                                append = true;
                            } else if (nextValue == nextRoundParticipantQuantity) {
                                append = true;
                            } else if (this.competitionGroupFormatTreeRounds.isEmpty() && nextRoundParticipantQuantity < nextValue) {
                                append = true;
                                forceExit = true;
                            } else if (nextRoundParticipantQuantity <= participantQualifiedPerMatch && nextRoundParticipantQuantity + nextValue <= numberOfParticipantPerMatch) {
                                append = true;
                            } else if (isPerfect(nextRoundParticipantQuantity + nextValue, numberOfParticipantPerMatch, participantQualifiedPerMatch)) {
                                append = true;
                            }
                            if (append)
                                nextRoundParticipantQuantity += participantsQueue.poll();
                            append = append && !participantsQueue.isEmpty() && !forceExit;

                        } else {
                            append = false;
                            int nextValue = participantsQueue.iterator().next().intValue();
                            if (nextRoundParticipantQuantity == 0) {
                                append = true;
                            } else if (nextValue == nextRoundParticipantQuantity) {
                                append = true;
                            } else if (this.competitionGroupFormatTreeRounds.isEmpty() && nextRoundParticipantQuantity < nextValue) {
                                append = true;
                                forceExit = true;
                            } else if (nextRoundParticipantQuantity <= participantQualifiedPerMatch && nextRoundParticipantQuantity + nextValue <= numberOfParticipantPerMatch) {
                                append = true;
                            } else if (isPerfect(nextRoundParticipantQuantity + nextValue, numberOfParticipantPerMatch, participantQualifiedPerMatch)) {
                                append = true;
                            }
                            if (append)
                                nextRoundParticipantQuantity += participantsQueue.poll();
                            append = append && !participantsQueue.isEmpty() && !forceExit;
                        }
                    } while (append);
//while (nextRoundParticipantQuantity == 0
//                            || nextRoundParticipantQuantity % numberOfParticipantPerMatch != 0
//                            || (nextRoundParticipantQuantity % numberOfParticipantPerMatch == 0 && (nextRoundParticipantQuantity * participantQualifiedPerMatch / numberOfParticipantPerMatch) % numberOfParticipantPerMatch != 0)
//                            || (nextValueInQueue != 0 && (nextRoundParticipantQuantity + nextValueInQueue) % numberOfParticipantPerMatch != 0)
//                            || (nextValueInQueue != 0 && (nextRoundParticipantQuantity + nextValueInQueue) % numberOfParticipantPerMatch == 0 && ((nextRoundParticipantQuantity + nextValueInQueue) * participantQualifiedPerMatch / numberOfParticipantPerMatch) % numberOfParticipantPerMatch != 0)) {
                }
                if (nextRoundParticipantQuantity > participantQualifiedPerMatch) {
                    CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = this.addCompetitionGroupFormatTreeRound();
                    competitionGroupFormatTreeRound.participantQuantity = nextRoundParticipantQuantity;
                } else if (nextRoundParticipantQuantity > 0 && !participantsQueue.isEmpty()) {
                    if (numberOfParticipantPerMatch > 1) {
                        CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = this.addCompetitionGroupFormatTreeRound();
                        competitionGroupFormatTreeRound.participantQuantity = nextRoundParticipantQuantity;
                    } else {
                        this.over = true;
                    }
                } else {
                    if (participantsQueue.isEmpty()) {
                        this.over = true;
                    } else if (numberOfParticipantPerMatch == 1) {

                    }
                }

                if (competitionGroupFormatTreeGroupNextLane != null) {
                    if (nextLaneParticipantQuantity > 0) {
                        competitionGroupFormatTreeGroupNextLane.participantsQueue.add(nextLaneParticipantQuantity);
                    }
                }
            }
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0) {
            int numberOfRound = (int) Math.ceil(competitionGroupFormat.getNumberOfRounds(participantQuantity, participantOut, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch, participantSplittable));
            if (participantQuantity % playVersusType.numberOfTeam != 0 && participantQuantity > numberOfParticipantPerMatch) {
                numberOfRound++;
            }

            if (numberOfRoundMinimum != null && numberOfRoundMinimum > numberOfRound)
                numberOfRound = numberOfRoundMinimum;

            if (numberOfRoundMaximum != null && numberOfRoundMaximum < numberOfRound)
                numberOfRound = numberOfRoundMaximum;
            for (int i = 0; i < numberOfRound; i++) {
                CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = this.addCompetitionGroupFormatTreeRound();
                competitionGroupFormatTreeRound.participantQuantity = participantQuantity;
                competitionGroupFormatTreeRound.round = i + 1;
                competitionGroupFormatTreeRound.computeCompetitionGroupFormatTreeMatches(numberOfParticipantPerMatch, participantQualifiedPerMatch, competitionGroupFormat, lane, fixedParticipantSize, participantSplittable);

            }
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) {
            int numberOfRound = competitionGroupFormat.getNumberOfRounds(participantQuantity, participantOut, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch, participantSplittable);
            for (int i = 0; i < numberOfRound; i++) {
                CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = this.addCompetitionGroupFormatTreeRound();
                competitionGroupFormatTreeRound.participantQuantity = participantQuantity;
                competitionGroupFormatTreeRound.round = i + 1;
                competitionGroupFormatTreeRound.computeCompetitionGroupFormatTreeMatches(numberOfParticipantPerMatch, participantQualifiedPerMatch, competitionGroupFormat, lane, fixedParticipantSize, participantSplittable);

            }
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = this.addCompetitionGroupFormatTreeRound();
            competitionGroupFormatTreeRound.participantQuantity = participantQuantity;
            competitionGroupFormatTreeRound.round = 1;
        }
    }

    private boolean isPerfect(int participantQuantity, int numberOfParticipantPerMatch, int participantQualifiedPerMatch) {
        int perfectNumber = numberOfParticipantPerMatch;
        boolean isPerfect = participantQuantity == perfectNumber;
        while (!isPerfect && perfectNumber < participantQuantity) {
            if (numberOfParticipantPerMatch == 1) {
                perfectNumber = perfectNumber * 2;

            } else {
                perfectNumber = perfectNumber * (numberOfParticipantPerMatch / participantQualifiedPerMatch);
            }
            isPerfect = participantQuantity == perfectNumber;
        }
        return isPerfect;
    }

    public int getNumberOfBye(int numberOfParticipantPerMatch, PlayVersusType playVersusType) {
        int numberOfBye = 0;
        for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds)
            numberOfBye += competitionGroupFormatTreeRound.getNumberOfBye(numberOfParticipantPerMatch, playVersusType);
        return numberOfBye;
    }

    public void setPlayQuantity(int playQuantity) {

        for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds)
            competitionGroupFormatTreeRound.setPlayQuantity(playQuantity);

    }

    public void doubleRound() {
        if (participantQuantity > 2) {
            List<CompetitionGroupFormatTreeRound> competitionGroupFormatTreeRoundsNew = new ArrayList<>();
            CompetitionGroupFormatTreeRound competitionGroupFormatTreeRoundNew = null;

            int roundIndex = competitionGroupFormatTreeRounds.size();
            for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeRounds) {
                roundIndex++;
                try {
                    competitionGroupFormatTreeRoundNew = competitionGroupFormatTreeRound.clone();
                    competitionGroupFormatTreeRoundNew.round = roundIndex;
                    competitionGroupFormatTreeRoundsNew.add(competitionGroupFormatTreeRoundNew);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            competitionGroupFormatTreeRounds.addAll(competitionGroupFormatTreeRoundsNew);
        }
    }
}
