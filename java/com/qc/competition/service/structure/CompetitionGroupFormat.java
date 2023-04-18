package com.qc.competition.service.structure;

import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.db.entity.competition.MatchType;
import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTree;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeGroup;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeMatch;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeRound;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.utils.ArraysTools;
import com.qc.competition.utils.Counter;
import com.qc.competition.utils.MathUtils;
import com.qc.competition.utils.Sets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
public enum CompetitionGroupFormat {
    CUSTOM(MatchLinkType.CUSTOM) // custom
    , ELIMINATION(MatchLinkType.SELECTING, TournamentFormat.ANY_ELIMINATION, TournamentFormat.QUADRUPLE_ELIMINATION, TournamentFormat.TRIPLE_ELIMINATION, TournamentFormat.DOUBLE_ELIMINATION, TournamentFormat.SINGLE_ELIMINATION) // elimination if 1 loss
    , NONE(MatchLinkType.NONE) // used for temporary split of participants
    , ROUND_ROBIN(MatchLinkType.STIRRING, TournamentFormat.ROUND_ROBIN) // all vs all
    , LADDER(MatchLinkType.STIRRING, TournamentFormat.LADDER) // all vs all
    , SWISS(MatchLinkType.STIRRING, TournamentFormat.SWISS) // optimized format winners vs winners , losers vs losers
    , SWISS_WITH_ELIMINATION_LEVEL(MatchLinkType.SELECTING) // optimized format winners vs winners , losers vs losers, participant are eliminated if more than X loss
    ;
    public static final int MAX_MATCHES_FOR_ROUND_ROBIN_SINGLE_ROUND = 16;
    protected static String CLASS = CompetitionGroupFormat.class.getSimpleName();
    //    protected static Logger logger = Logger.getLogger(CLASS);
    private static int[] PRIME_NUMBERS_UNDER_100 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};
    public List<TournamentFormat> tournamentFormats;
    public MatchLinkType matchLinkType;

    CompetitionGroupFormat(MatchLinkType matchLinkType, TournamentFormat... tournamentFormats) {
        this.matchLinkType = matchLinkType;
        this.tournamentFormats = Arrays.asList(tournamentFormats);
    }

//    public CompetitionGroupFormatTreeGroup getCompetitionGroupFormatTreeGroup(ParticipantType participantType, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipant, int numberOfParticipantOut, int participantQualifiedPerMatch, int competitionGroupQuantity, int lane, int numberOfPlayPerMatchMinimum, int numberOfPlayPerMatchMaximum, boolean allowEvenNumber, Integer numberOfRoundMinimum, Integer numberOfRoundMaximum, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch) {
//        CompetitionGroupFormatTree competitionGroupFormatTree = getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumber, numberOfRoundMinimum, numberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch);
//        return competitionGroupFormatTree.getCompetitionGroupFormatTreeGroup(lane);
//    }

    public CompetitionGroupFormatTree getCompetitionGroupFormatTree(ParticipantType participantType, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipant, int numberOfParticipantOut, int participantQualifiedPerMatch, int competitionGroupQuantity, int numberOfPlayPerMatchMinimum, int numberOfPlayPerMatchMaximum, boolean allowEvenNumber, Integer numberOfRoundMinimum, Integer numberOfRoundMaximum, boolean finalGroupSizeThresholdEnabled, int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup, int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch, boolean fixedParticipantSize, boolean participantSplittable) {
        String logPrefix = "CompetitionGroupFormat:" + this + ",participantType:" + participantType + ",playVersusType:" + playVersusType + ",numberOfParticipantPerMatch:" + numberOfParticipantPerMatch + ",numberOfParticipant:" + numberOfParticipant + ",numberOfParticipantOut:" + numberOfParticipantOut + ",participantQualifiedPerMatch:" + participantQualifiedPerMatch + ",competitionGroupQuantity:" + competitionGroupQuantity + ",numberOfPlayPerMatchMinimum:" + numberOfPlayPerMatchMinimum + ",numberOfPlayPerMatchMaximum:" + numberOfPlayPerMatchMaximum + "numberOfRoundMinimum:" + numberOfRoundMinimum + ",numberOfRoundMaximum:" + numberOfRoundMaximum + ",finalGroupSizeThresholdEnabled" + finalGroupSizeThresholdEnabled + ",finalGroupSizeThreshold:" + finalGroupSizeThreshold + ",numberOfPlayPerMatchMinimumFinalGroup:" + numberOfPlayPerMatchMinimumFinalGroup + ",numberOfPlayPerMatchMaximumFinalGroup:" + numberOfPlayPerMatchMaximumFinalGroup + ",thirdPlaceMatchEnabled:" + thirdPlaceMatch + ",fixedParticipantSize:" + fixedParticipantSize + ",participantSplittable:" + participantSplittable;
//        logger.fine(logPrefix);
        CompetitionGroupFormatTree competitionGroupFormatTree = new CompetitionGroupFormatTree(this, participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumber, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch);
        competitionGroupFormatTree.sort();
        for (int i = 0; i < competitionGroupQuantity; i++) {
            competitionGroupFormatTree.addCompetitionGroupFormatTreeGroup();
        }

        competitionGroupFormatTree.computeInitialGroupParticipant(numberOfParticipant, numberOfParticipantPerMatch, participantQualifiedPerMatch);
        switch (this) {
            case ELIMINATION: {
                CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound = null;
                for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTree.competitionGroupFormatTreeGroups) {

                    while (!competitionGroupFormatTreeGroup.over) {
                        if (competitionGroupFormatTreeGroup.lane == 1 && competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.isEmpty()) {
                            competitionGroupFormatTreeRound = competitionGroupFormatTreeGroup.addCompetitionGroupFormatTreeRound();
//                            competitionGroupFormatTreeRound.participantQuantity = numberOfParticipant;
                            competitionGroupFormatTreeRound.participantQuantity = competitionGroupFormatTree.getCompetitionGroupFormatTreeGroup(1).participantQuantity;
                        } else {
                            CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroupNextLane = competitionGroupFormatTree.getCompetitionGroupFormatTreeGroup(competitionGroupFormatTreeGroup.lane + 1);
                            competitionGroupFormatTreeGroup.computeCompetitionGroupFormatTreeRound(competitionGroupFormatTreeGroupNextLane, this, numberOfParticipantPerMatch, playVersusType, numberOfParticipantOut, participantQualifiedPerMatch, numberOfRoundMinimum, numberOfRoundMaximum, fixedParticipantSize, participantSplittable);
                        }
                    }
                }

                List<CompetitionGroupFormatTreeGroup> competitionGroupFormatTreeGroupsToRemove = new ArrayList<>();
                for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTree.competitionGroupFormatTreeGroups) {
                    if (competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.isEmpty())
                        competitionGroupFormatTreeGroupsToRemove.add(competitionGroupFormatTreeGroup);
                }
                competitionGroupFormatTree.competitionGroupFormatTreeGroups.removeAll(competitionGroupFormatTreeGroupsToRemove);
                if (thirdPlaceMatch && competitionGroupFormatTree.competitionGroupFormatTreeGroups.size() == 1 && numberOfParticipant - participantQualifiedPerMatch * 2 > participantQualifiedPerMatch) {
                    CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup = competitionGroupFormatTree.competitionGroupFormatTreeGroups.first();
                    int numberOfParticipantThirdPlace = numberOfParticipant - participantQualifiedPerMatch * 2;
                    if (numberOfParticipantThirdPlace > numberOfParticipantPerMatch)
                        numberOfParticipantThirdPlace = numberOfParticipantPerMatch;

                    CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroupThirdPlace = competitionGroupFormatTree.addCompetitionGroupFormatTreeGroup();
                    competitionGroupFormatTreeGroupThirdPlace.participantQuantity = numberOfParticipantThirdPlace;
                    competitionGroupFormatTreeGroupThirdPlace.addCompetitionGroupFormatTreeRound();
                    CompetitionGroupFormatTreeRound competitionGroupFormatTreeRoundThirdPlace = competitionGroupFormatTreeGroupThirdPlace.competitionGroupFormatTreeRounds.first();
                    competitionGroupFormatTreeRoundThirdPlace.addCompetitionGroupFormatTreeMatch();
                    competitionGroupFormatTreeRoundThirdPlace.participantQuantity = numberOfParticipantThirdPlace;
                    competitionGroupFormatTreeRoundThirdPlace.round = 1;
                    CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatchThirdPlace = competitionGroupFormatTreeRoundThirdPlace.competitionGroupFormatTreeMatches.first();
                    competitionGroupFormatTreeMatchThirdPlace.participantQuantity = numberOfParticipantThirdPlace;
                    competitionGroupFormatTreeMatchThirdPlace.lane = 1;
//                    competitionGroupFormatTreeMatchThirdPlace.playQuantity = competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.last().competitionGroupFormatTreeMatches.first().playQuantity;
                }

            }
            break;

            case SWISS:
            case ROUND_ROBIN:
            case LADDER: {
                for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTree.competitionGroupFormatTreeGroups) {
                    competitionGroupFormatTreeGroup.computeCompetitionGroupFormatTreeRound(null, this, numberOfParticipantPerMatch, playVersusType, numberOfParticipantOut, participantQualifiedPerMatch, numberOfRoundMinimum, numberOfRoundMaximum, fixedParticipantSize, participantSplittable);
                }
                break;
            }
            case CUSTOM: {
                break;
            }
        }
        competitionGroupFormatTree.computePlayQuantity();
        return competitionGroupFormatTree;

    }

    public int getNumberOfRounds(int participantQuantity, int participantQuantityOut, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipantQualifiedPerMatch, Boolean participantTypeSplittable) {
        int numberOfRound = 0;
        if (numberOfParticipantPerMatch == 1) {
            int participantQuantityRemaining = participantQuantity;
            numberOfRound++;
            while (participantQuantityRemaining > participantQuantityOut) {
                participantQuantityRemaining = participantQuantityRemaining / 2;
                numberOfRound++;
            }
        } else {
            switch (this) {
                case ROUND_ROBIN: {
                    if (numberOfParticipantPerMatch == 2) {
                        int participantQuantityCorrected = participantQuantity;
                        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
                            participantQuantityCorrected += (numberOfParticipantPerMatch - participantQuantity % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;
                        numberOfRound = participantQuantityCorrected - 1;
                    } else {
                        if (participantQuantity <= numberOfParticipantPerMatch) {
                            numberOfRound = 1;
                        } else {
                            numberOfRound = ArraysTools.getParticipantsCombinations(participantQuantity, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable).size();
                        }
                    }
//                numberOfRound = (int) Math.ceil((double) participantQuantity / (numberOfParticipantPerMatch - 1)) * (numberOfParticipantPerMatch - 1);
//                int numberOfBye = getNumberOfByes(participantQuantity, numberOfParticipantPerMatch, participantQualifiedPerMatch);
//                if (numberOfRound == 1 && numberOfBye > 0) {
//                    numberOfRound++;
//                }
                    break;
                }
                case SWISS: {
//                int higherThreshold = (int) Math.ceil((double) participantQuantity * (double) numberOfParticipantQualifiedPerMatch / (double) numberOfParticipantPerMatch) - 1;
//                if (participantQuantityOut > higherThreshold) {
//                    participantQuantityOut = higherThreshold;
//                }
                    if (participantQuantityOut == 0) {
                        participantQuantityOut = 1;
                    }
                    int participantQuantityRounded = playVersusType.roundUp(participantQuantity);

                    int remainingParticipant = participantQuantityRounded;
                    int participantOutSimulation = participantQuantityOut;
                    if (participantOutSimulation == participantQuantity) {
                        participantOutSimulation = participantQuantityRounded / 4;
                    }
                    if (participantOutSimulation < numberOfParticipantQualifiedPerMatch)
                        participantOutSimulation = numberOfParticipantQualifiedPerMatch;
                    if (participantOutSimulation == 0)
                        participantOutSimulation = 1;

                    double multiplicator = (double) numberOfParticipantQualifiedPerMatch / (double) numberOfParticipantPerMatch;
                    if (multiplicator == 1)
                        multiplicator = 0.5;

                    while (remainingParticipant > participantOutSimulation) {
                        numberOfRound++;
                        remainingParticipant = (int) Math.ceil((double) remainingParticipant * multiplicator);
                    }
                    if (numberOfRound < 2 && participantQuantityOut > numberOfParticipantQualifiedPerMatch * numberOfRound) {
                        numberOfRound = 2;
                        if (participantQuantity % numberOfParticipantPerMatch <= numberOfParticipantQualifiedPerMatch)
                            numberOfRound++;
                    }
                    if (numberOfRound % 2 == 0 && participantQuantityOut != participantQuantity)
                        numberOfRound++;
//                int roundRobinNumberOfRounds = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(participantQuantity, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch);
//                if (participantQuantity % numberOfParticipantPerMatch != 0 && numberOfRound < roundRobinNumberOfRounds) {
//                    numberOfRound++;
//                }
//                if (numberOfRound >= roundRobinNumberOfRounds) {
//                    numberOfRound--;
//                }
//
//                numberOfRound = numberOfRound + ((numberOfRound + 1) % 2);
//                int numberOfBye = getNumberOfByes(participantQuantity, numberOfParticipantPerMatch, participantQualifiedPerMatch);
//                if (numberOfRound == 1 && numberOfBye > 0) {
//                    numberOfRound++;
//                }
//                if (numberOfParticipantPerMatch == 2 && participantQuantity % numberOfParticipantPerMatch == 1)
//                    numberOfRound++;
//if(participantQuantity == participantQuantityOut;
//                numberOfRound = Math.ceil(Math.log((double) (participantQuantity) / Math.pow((double) participantQuantityOut, 1.0 / (double) numberOfParticipantPerMatch)) / Math.log((double) numberOfParticipantPerMatch));
                }
                break;

                case ELIMINATION: {
                    if (participantQuantity <= participantQuantityOut) {
                        participantQuantity = participantQuantityOut - 1;
                    }
                    if (participantQuantityOut == 0) {
                        participantQuantityOut = 1;
                    }
                    if (numberOfParticipantQualifiedPerMatch != numberOfParticipantPerMatch)
                        numberOfRound = (int) (Math.log(participantQuantity) / Math.log((double) numberOfParticipantPerMatch / (double) numberOfParticipantQualifiedPerMatch));
                    else
                        numberOfRound = (int) (Math.log(participantQuantity) / Math.log(numberOfParticipantPerMatch));
                    break;
                }
            }
        }
        return numberOfRound;

    }

    public int getNumberOfByes(int participantQuantity, int numberOfParticipantPerMatch,
                               int participantQualifiedPerMatch) {
        int numberOfMatches = participantQuantity / numberOfParticipantPerMatch;
        int numberOfByes = 0;
        if (participantQuantity % numberOfParticipantPerMatch > 0) {
            numberOfMatches++;
            if (participantQuantity - numberOfMatches * participantQualifiedPerMatch < numberOfMatches) {
                numberOfByes = numberOfMatches - (participantQuantity - numberOfMatches * participantQualifiedPerMatch);
            }
        }
        return numberOfByes;
    }


    public int computeNumberOfEliminatedParticipantPerMatch(int numberOfParticipantPerMatch, PlayVersusType
            playVersusType, int numberOfParticipantQualified, int participantPresent) {
        int numberOfEliminatedParticipant = participantPresent - computeNumberOfQualifiedParticipantPerMatch(numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualified, participantPresent);
        return numberOfEliminatedParticipant;
    }

    public int computeNumberOfQualifiedParticipantPerMatch(int numberOfParticipantPerMatch, PlayVersusType
            playVersusType, int numberOfParticipantQualifiedPerMatch, int participantPresent) {
        int numberOfParticipantQualified = 0;
        switch (this) {
            case ELIMINATION:
                if (playVersusType.numberOfTeam == 1) {
                    numberOfParticipantQualified = 1;
                    while (numberOfParticipantQualified < participantPresent) {
                        numberOfParticipantQualified = 2 * numberOfParticipantQualified;
                    }
                    numberOfParticipantQualified = numberOfParticipantQualified / 2;
                } else {
                    if (participantPresent > numberOfParticipantQualifiedPerMatch)
                        numberOfParticipantQualified = numberOfParticipantQualifiedPerMatch;
                    else
                        numberOfParticipantQualified = participantPresent;
                }
                break;
            default:
                numberOfParticipantQualified = participantPresent;
                break;
        }
        return numberOfParticipantQualified;
    }

//    public int expectedCompetitionMatchQuantity(PlayVersusType playVersusType, int numberOfParticipant, int numberOfParticipantOut, int participantQualifiedPerMatch, int competitionGroupQuantity, int round, int roundDetails, int numberOfPlayPerMatchMinimum, int numberOfPlayPerMatchMaximum) {
//        int competitionMatchQuantity = 0;
//        CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup = getCompetitionGroupFormatTreeGroup(playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, round, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum);
//        if (competitionGroupFormatTreeGroup.getCompetitionGroupFormatTreeRound(roundDetails) != null)
//            competitionMatchQuantity = competitionGroupFormatTreeGroup.getCompetitionGroupFormatTreeRound(roundDetails).competitionGroupFormatTreeMatches.size();
//        return competitionMatchQuantity;
//
//    }
//
//
//    public int expectedCompetitionRoundQuantity(PlayVersusType playVersusType, int numberOfParticipant, int numberOfParticipantOut, int participantQualifiedPerMatch, int competitionGroupQuantity, int round, int numberOfPlayPerMatchMinimum, int numberOfPlayPerMatchMaximum) {
//        CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup = getCompetitionGroupFormatTreeGroup(playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, round, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum);
//        return competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.size();
//    }

    public SortedSet<ParticipantPairing> createParticipantPairingForCompetitionGroupRound(CompetitionRound
                                                                                                  competitionRound, int numberOfParticipantMatch, int numberOfParticipantQualifiedPerMatch, PlayVersusType
                                                                                                  playVersusType, Boolean participantTypeSplittable) {
        String logPrefix = "CompetitionGroupFormat:" + this + ",competitionRound:" + competitionRound.toString() + ",playVersusType:" + playVersusType + ",numberOfParticipantMatch:" + numberOfParticipantMatch + ",numberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch;
//        logger.fine(logPrefix);
//        SortedSet<ParticipantPairing> participantPairings = new TreeSet<ParticipantPairing>();
        boolean doPairing = false;
        if (competitionRound.competitionRoundPrevious == null && !competitionRound.isParticipantPairingDefined()) {
            if (competitionRound.competitionGroup.competitionGroupFormat.compareTo(LADDER) == 0) {
                if (competitionRound.competitionGroup.participantSeats.size() >= competitionRound.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch && !competitionRound.isOpen())
                    competitionRound.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
            } else {
                if (competitionRound.getCompetitionGroup().isParticipantPairingDefined()) {
                    doPairing = true;
                    if (this.compareTo(CompetitionGroupFormat.ROUND_ROBIN) != 0) {
                        for (ParticipantPairing participantPairing : competitionRound.getCompetitionGroup().participantPairings) {
                            participantPairing.setCompetitionRound(competitionRound);
                        }
                    }
                }
            }
        }
        if (!competitionRound.isParticipantPairingDefined()) {
            boolean addToGroupPairing = !competitionRound.getCompetitionGroup().isParticipantPairingDefined();

            if (competitionRound.participantPairings.size() < competitionRound.competitionMatches.size()) {
                for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                    ParticipantPairing participantPairing = competitionRound.getCompetitionInstance().createParticipantPairing(competitionMatch.participantQuantity);
                    participantPairing.setCompetitionRound(competitionRound);
//                    competitionRound.addParticipantPairing(participantPairing);
                    participantPairing.setCompetitionMatch(competitionMatch);
                    if (addToGroupPairing)
                        participantPairing.setCompetitionGroup(competitionRound.competitionGroup);
//                    competitionMatch.setParticipantPairing(participantPairing);
//                    participantPairing.competitionMatch.setParticipantPairing(participantPairing);
                }
            }

            switch (this) {
                case ELIMINATION: {
//                    boolean dequeued = false;
//                    boolean fromPreviousRound = false;
                    if (competitionRound.competitionMatches == null || competitionRound.competitionMatches.isEmpty() || (competitionRound.competitionMatches.iterator().next().generation || numberOfParticipantMatch == 1)) {
                        if (playVersusType.numberOfTeam == 1 && (competitionRound.round > 1 || competitionRound.competitionGroup.lane > 1)) {
                            if (competitionRound.localId.compareTo("0005") == 0)
                                System.out.println();
                            if (competitionRound.getCompetitionRoundPrevious() == null || competitionRound.getCompetitionRoundPrevious().isParticipantResultsSet()) {
                                Set<Participant> participants = new HashSet<>();
                                if (competitionRound.getCompetitionRoundPrevious() != null) {
                                    SortedSet<ParticipantResult> participantResults = getParticipantResultsQualified(competitionRound.getCompetitionRoundPrevious());
                                    for (ParticipantResult participantResult : participantResults) {
                                        participants.add(participantResult.participant);
                                    }
                                }
                                int remainingParticipants = competitionRound.competitionMatches.size() - participants.size();

                                if (remainingParticipants > 0) {
                                    ParticipantQueue participantQueue = competitionRound.getCompetitionGroup().participantQueue;
                                    Set<Participant> participantsWithQueue = new HashSet<>(participants);
                                    if (participantQueue != null && !participantQueue.participantQueueElements.isEmpty()) {
                                        for (ParticipantQueueElement participantQueueElement : participantQueue.participantQueueElements) {
                                            participantsWithQueue.addAll(participantQueueElement.participants);
                                            if (participantsWithQueue.size() == competitionRound.competitionMatches.size()) {
                                                doPairing = true;
                                                break;
                                            } else if (participantsWithQueue.size() > competitionRound.competitionMatches.size()) {
                                                break;
                                            }

                                        }

                                    }
                                    if (doPairing) {
                                        while (participantQueue != null && !participantQueue.participantQueueElements.isEmpty() && remainingParticipants > 0) {
                                            participants.addAll(participantQueue.participantQueueElements.iterator().next().participants);
                                            participantQueue.participantQueueElements.remove(0);
                                            remainingParticipants = competitionRound.competitionMatches.size() - participants.size();
                                        }
                                    }
                                } else {
                                    doPairing = true;
                                }
                                if (doPairing) {
                                    for (Participant participant : participants) {
                                        boolean participantAlreadyIn = false;
                                        for (ParticipantPairing participantPairing : competitionRound.participantPairings) {
                                            if (participantPairing.contains(participant)) {
                                                participantAlreadyIn = true;
                                                break;
                                            }
                                        }
                                        if (!participantAlreadyIn) {
                                            for (ParticipantPairing participantPairing : competitionRound.participantPairings) {
                                                if (!participantPairing.isFull() && !participants.isEmpty()) {
                                                    participantPairing.addParticipant(participant);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        } else {
                            List<Participant> participants = new ArrayList<>();
                            int numberOfMatches = competitionRound.competitionMatches.size();
                            if (competitionRound.getCompetitionGroup().lane == 1) {
                                if (competitionRound.round > 1 && competitionRound.getCompetitionRoundPrevious().isParticipantResultsSet()) {
                                    participants.addAll(competitionRound.getCompetitionRoundPrevious().getParticipantsQualified());
                                    if (participants.size() != 0 && participants.size() % numberOfParticipantMatch == 0 && participants.size() / numberOfParticipantMatch == numberOfMatches) {
                                        doPairing = true;
                                    } else if (!competitionRound.competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase.participantTypeSplittable) {
                                        doPairing = true;
                                    }
//                            fromPreviousRound = true;
                                }
                            } else {
//                        int participantSeats = competitionRound.competitionMatches.size() * numberOfParticipantMatch;
                                ParticipantQueue participantQueue = competitionRound.getCompetitionGroup().participantQueue;
//                        int numberOfMatches = competitionRound.competitionMatches.size();
                                SortedMap<Integer, List<Participant>> participantsLane = new TreeMap<>();
                                if (competitionRound.competitionRoundPrevious != null && competitionRound.getCompetitionRoundPrevious().isParticipantResultsSet()) {
                                    participants.addAll(competitionRound.getCompetitionRoundPrevious().getParticipantsQualified());
                                    participantsLane.put(competitionRound.getCompetitionGroup().getRoundOrLane(), competitionRound.getCompetitionRoundPrevious().getParticipantsQualified());
                                }
                                if (participants.size() != 0 && participants.size() % numberOfParticipantMatch == 0 && participants.size() / numberOfParticipantMatch == numberOfMatches) {
                                    doPairing = true;
                                } else if (participantQueue != null && !participantQueue.participantQueueElements.isEmpty()) {
                                    List<ParticipantQueueElement> participantQueueElementToRemove = new ArrayList<>();

                                    for (int i = 0; i < participantQueue.participantQueueElements.size(); i++) {
                                        ParticipantQueueElement participantQueueElement = participantQueue.participantQueueElements.get(i);
//                                    int participantQueueElementFirstSize = participantQueueElement.participants.size();
                                        boolean eligible = true;
                                        for (Participant participant : participants) {
                                            for (Participant participantInQueue : participantQueueElement.participants) {
                                                if (participantInQueue.compareTo(participant) == 0) {
                                                    eligible = false;
                                                }
                                            }
                                        }
                                        if (eligible) {
                                            participants.addAll(participantQueueElement.participants);
                                            if (!participantsLane.containsKey(competitionRound.getCompetitionGroup().getRoundOrLane() - 1)) {
                                                participantsLane.put(competitionRound.getCompetitionGroup().getRoundOrLane() - 1, new ArrayList<>());
                                            }
                                            participantsLane.get(competitionRound.getCompetitionGroup().getRoundOrLane() - 1).addAll(participantQueueElement.participants);
                                            participantQueueElementToRemove.add(participantQueueElement);
                                            if (participants.size() > 0
                                                    && (
                                                    (competitionRound.round == 1 && participants.size() > numberOfMatches * numberOfParticipantQualifiedPerMatch && (participantQueue.participantQueueElements.size() <= i + 1 || participantQueue.participantQueueElements.get(i + 1).participants.size() < participants.size()))
                                                            || (participants.size() % numberOfParticipantMatch == 0 && participants.size() / numberOfParticipantMatch == numberOfMatches))) {
                                                doPairing = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (doPairing) {
                                        participantQueue.participantQueueElements.removeAll(participantQueueElementToRemove);
                                        List<Participant> participantsMixed = new ArrayList<>();
                                        while (!participantsLane.isEmpty()) {
                                            for (Integer key : participantsLane.keySet()) {
                                                if (!participantsLane.get(key).isEmpty()) {
                                                    participantsMixed.add(participantsLane.get(key).get(0));
                                                    participantsLane.get(key).remove(0);
                                                } else {
                                                    participantsLane.remove(key);
                                                    break;
                                                }
                                            }
                                        }
                                        participants.clear();
                                        participants.addAll(participantsMixed);
                                    }
                                }
                            }

                            for (int i = 0; i < participants.size(); i++) {
                                if (participants.get(i) == null) {
                                    participants.remove(i);
                                    i--;
                                }
                            }

                            if (doPairing) {
                                int participantQualified = participants.size();

//                    int participantPairingSeatSizeMininum = (int) Math.floor((double) participantQualified / (double) numberOfMatches);
//                    int participantPairingSeatsMissingQuantity = getClosestAcceptableSize(numberOfParticipantMatch, numberOfParticipantQualifiedPerMatch, participantQualified, true) - participantQualified;

                                int participantPairingSeatsMissingQuantity = 0;
                                if (competitionRound.competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase.participantTypeSplittable) {
                                    int roundFullParticipantNumber = numberOfParticipantMatch;
                                    while (participantQualified > roundFullParticipantNumber) {
                                        if (numberOfParticipantMatch == 1) {
                                            roundFullParticipantNumber *= 2;
                                        } else {
                                            roundFullParticipantNumber *= (numberOfParticipantMatch / numberOfParticipantQualifiedPerMatch);
                                        }
                                    }
                                    participantPairingSeatsMissingQuantity = roundFullParticipantNumber - participantQualified;


                                    if (participantPairingSeatsMissingQuantity > 0) {
                                        SortedSet<ParticipantPairing> participantPairings = Sets.sort(competitionRound.participantPairings);
                                        boolean hasChanged = true;
                                        while (participantPairingSeatsMissingQuantity > 0 && hasChanged) {
                                            hasChanged = false;
                                            for (ParticipantPairing participantPairing : participantPairings) {
                                                if (!(participantPairing.isFull() || participantPairing.getReservedParticipantQuantity() == participantPairing.participantSeats.size())) {
                                                    hasChanged = true;
                                                    participantPairing.addParticipant(competitionRound.competitionInstance.createParticipantTeamVoid());
                                                    participantPairingSeatsMissingQuantity--;
                                                    if (participantPairingSeatsMissingQuantity == 0)
                                                        break;
                                                }
                                            }
//                                participants.add((participants.size() - ((i * numberOfParticipantMatch) % participants.size())), competitionRound.competitionInstance.createParticipantTeamVoid());
                                        }
                                    }
                                }
//                        if (dequeued && fromPreviousRound) {
//                        if (fromPreviousRound) {
//                            SortedSet<ParticipantPairing> participantPairings = (SortedSet<ParticipantPairing>) Sets.sort(competitionRound.participantPairings);
//                            List<Participant> participantListToRemove = new ArrayList<>();
//                            for (ParticipantPairing participantPairing : participantPairings) {
//                                participantListToRemove.clear();
//                                for (Participant participant : participants) {
//                                    if (!(participantPairing.isFull() || participantPairing.getReservedParticipantQuantity() == participantPairing.participantSeats.size())) {
//                                        participantPairing.addParticipant(participant);
//                                        participantListToRemove.add(participant);
//                                    } else {
//                                        break;
//                                    }
//                                }
//                                participants.removeAll(participantListToRemove);
//                            }
//                        } else {
                                SortedSet<ParticipantPairing> participantPairings = Sets.sort(competitionRound.participantPairings);
                                for (ParticipantPairing participantPairing : participantPairings) {
                                    while (!participants.isEmpty()) {
                                        if (!(participantPairing.isFull() || participantPairing.getReservedParticipantQuantity() == participantPairing.participantSeats.size())) {
                                            participantPairing.addParticipant(participants.get(0));
                                            participants.remove(0);
                                        } else {
                                            break;
                                        }
                                    }
                                }
//                        }
                            }
                        }
                    }
                    break;
                }
                case SWISS: {
                    if (competitionRound.competitionRoundPrevious != null && !competitionRound.getCompetitionRoundPrevious().isParticipantResultsSet()) {
                        break;
                    }
                    if (competitionRound.competitionRoundPrevious != null && competitionRound.getCompetitionRoundPrevious().isParticipantResultsSet() && !competitionRound.isParticipantPairingDefined()) {
                        List<ParticipantResult> participantResults = new ArrayList<>();
                        participantResults.addAll(competitionRound.getCompetitionGroup().getCompetitionGroupResult().participantResults);
                        SortedMap<ParticipantResult, List<Participant>> participantResultListSortedMap = new TreeMap<>();
                        Map<Participant, ParticipantResult> participantParticipantResultMap = new HashMap<>();
                        Map<Participant, Counter> participantsNumberOfBye = new TreeMap<>();
                        for (ParticipantResult participantResult : participantResults) {
                            participantResultListSortedMap.put(participantResult, new ArrayList<>());
                            participantsNumberOfBye.put(participantResult.participant, new Counter());
                            participantParticipantResultMap.put(participantResult.participant, participantResult);
                            for (ParticipantResult participantResultOpponent : participantResults) {
                                if (participantResult.participant.compareTo(participantResultOpponent.participant) != 0)
                                    participantResultListSortedMap.get(participantResult).add(participantResultOpponent.participant);
                            }
                        }

                        Map<Participant, List<Participant>> participantPreviousOpponent = new TreeMap<>();
                        for (CompetitionRound competitionRoundPrevious : competitionRound.getCompetitionGroup().competitionRounds) {
                            if (competitionRoundPrevious.isClosed()) {
                                for (CompetitionMatch competitionMatchPrevious : competitionRoundPrevious.competitionMatches) {
                                    for (Participant participant : competitionMatchPrevious.participantPairing.getRealParticipantsAsArray()) {
                                        if (competitionMatchPrevious.isBye()) {
                                            participantsNumberOfBye.get(participant).increment();
                                        }
                                        List<Participant> participantOpponents = participantResultListSortedMap.get(participantParticipantResultMap.get(participant));
                                        List<Participant> participantList = competitionMatchPrevious.participantPairing.getRealParticipantsAsArray();
                                        while (!participantList.isEmpty()) {
                                            Participant participant1 = participantList.get(0);
                                            for (int i = 0; i < participantOpponents.size(); i++) {
                                                if (participantOpponents.get(i).compareTo(participant1) == 0) {
                                                    participantOpponents.remove(i);
                                                    break;
                                                }
                                            }
                                            participantList.remove(0);
                                        }
                                    }
                                }
                            }
                        }

                        Map<Integer, List<Participant>> numberOfByeParticipants = new HashMap<>();
                        for (Participant participant : participantsNumberOfBye.keySet()) {
                            Integer newKey = Integer.valueOf(participantsNumberOfBye.get(participant).value);
                            if (!numberOfByeParticipants.containsKey(newKey)) {
                                numberOfByeParticipants.put(newKey, new ArrayList<>());
                            }
                            numberOfByeParticipants.get(newKey).add(participant);
                        }
                        Map<Integer, List<Participant>> numberOfByeParticipantsSorted = new TreeMap<>(numberOfByeParticipants);


                        List<Participant> participantList = new ArrayList<>();
                        while (!participantResultListSortedMap.isEmpty()) {
                            ParticipantResult participantResult = participantResultListSortedMap.firstKey();
                            participantList.add(participantResult.participant);
                            participantResultListSortedMap.remove(participantResult);
                            if (!participantResultListSortedMap.isEmpty()) {
                                boolean found = false;
                                for (ParticipantResult participantResultKey : participantResultListSortedMap.keySet()) {
                                    if (participantResultListSortedMap.get(participantResultKey).contains(participantResult.participant)) {
                                        participantList.add(participantResultKey.participant);
                                        participantResultListSortedMap.remove(participantResultKey);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    ParticipantResult participantResultKey = participantResultListSortedMap.firstKey();
                                    participantList.add(participantResultKey.participant);
                                    participantResultListSortedMap.remove(participantResultKey);
                                }
                            }
                        }
                        CompetitionCreationParamPhase competitionCreationParamPhase = competitionRound.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//                        if (competitionCreationParamPhase == null)
//                            competitionCreationParamPhase = competitionRound.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
                        Sets.sort(competitionRound.competitionMatches);
                        for (CompetitionMatch competitionMatch :
                                competitionRound.competitionMatches) {
                            if (competitionMatch.acceptParticipant() && competitionMatch.participantQuantity <= competitionCreationParamPhase.participantQualifiedPerMatch) {
                                // Fill bye
                                while (competitionMatch.acceptParticipant() && !participantList.isEmpty()) {
                                    Integer firstKey = ((TreeMap<Integer, List<Participant>>) numberOfByeParticipantsSorted).firstKey();
                                    if (numberOfByeParticipantsSorted.get(firstKey).isEmpty()) {
                                        numberOfByeParticipantsSorted.remove(firstKey);
                                    } else {
                                        Participant participant = numberOfByeParticipantsSorted.get(firstKey).get(0);
                                        competitionMatch.participantPairing.addParticipant(participant);
                                        participantList.remove(participant);
                                        numberOfByeParticipantsSorted.get(firstKey).remove(participant);
                                    }
                                }
                            }
                        }

                        for (CompetitionMatch competitionMatch :
                                competitionRound.competitionMatches) {
                            while (competitionMatch.acceptParticipant() && !participantList.isEmpty()) {
                                competitionMatch.participantPairing.addParticipant(participantList.get(0));
                                participantList.remove(0);
                            }
                        }
                        doPairing = true;
                    }
                    break;
                }
                case ROUND_ROBIN: {
//                    List<Participant> participants = new ArrayList<>();
//                    Map<Integer, List<Participant>> participantQualifiedMapByPosition = new HashMap<>();
//                    participantQualified = new ArrayList<>();
                    if (competitionRound.competitionRoundPrevious != null) {
                        List<List<Participant>> participantListPerPairing = new ArrayList<>();
                        List<List<List<Participant>>> participantListPreviousPerRound = new ArrayList<>();

                        if (numberOfParticipantMatch > 2) {
                            SortedSet<Participant> participants = new TreeSet<>();
//                            numberOfParticipantMatch = 2;

                            for (ParticipantPairing participantPairing : competitionRound.getCompetitionGroup().getCompetitionRoundForRound(1).getParticipantPairings()) {
                                List<Participant> realParticipants = participantPairing.getRealParticipantsAsArray();
//                                if(realParticipants.size()>numberOfParticipantMatch)
//                                    numberOfParticipantMatch=realParticipants.size();
                                participants.addAll(realParticipants);

                            }
                            participants = Sets.sort(participants);
                            participantListPerPairing.add(new ArrayList<>(participants));
                            for (int i = 1; i < competitionRound.round; i++) {
                                participantListPreviousPerRound.add(new ArrayList<>());
                                for (ParticipantPairing participantPairing : competitionRound.getCompetitionGroup().getCompetitionRoundForRound(i).getParticipantPairings()) {
                                    List<Participant> realParticipants = participantPairing.getRealParticipantsAsArray();
                                    participantListPreviousPerRound.get(i - 1).add(realParticipants);
                                }

                            }
                        } else {
                            for (ParticipantPairing participantPairing : competitionRound.getCompetitionGroup().getParticipantPairings()) {
                                participantListPerPairing.add(participantPairing.getParticipantsAsArray());
                            }
                        }
                        SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRoundOrderNew = ArraysTools.roundRobin(participantListPreviousPerRound, participantListPerPairing, numberOfParticipantMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);
                        CompetitionRound competitionRoundCurrent = competitionRound;
                        do {

                            if (!participantsPerMatchPerRoundOrderNew.containsKey(competitionRoundCurrent.round)) {
                                competitionRoundCurrent.getCompetitionGroup().removeCompetitionRound(competitionRoundCurrent);
                                competitionRoundCurrent = null;
                            } else {
                                List<List<Participant>> participantListForRound = participantsPerMatchPerRoundOrderNew.get(competitionRoundCurrent.round);
                                for (CompetitionMatch competitionMatch : competitionRoundCurrent.competitionMatches) {
                                    for (List<Participant> participantList : participantListForRound) {
                                        if (participantList.size() == competitionMatch.participantQuantity) {
                                            ParticipantPairing participantPairing = competitionMatch.participantPairing;
                                            if (participantPairing == null) {
                                                participantPairing = competitionRoundCurrent.competitionInstance.createParticipantPairing(participantList.size());
                                                participantPairing.setCompetitionRound(competitionRoundCurrent);
                                                participantPairing.setCompetitionMatch(competitionMatch);
                                            }
                                            for (Participant participant : participantList) {
                                                participantPairing.addParticipant(participant);
                                            }
                                            participantListForRound.remove(participantList);
                                            break;
                                        }


                                    }
                                }
                                competitionRoundCurrent = competitionRoundCurrent.getCompetitionRoundNext();
                            }
                        } while (competitionRoundCurrent != null);


//                        int round = 1;
//                        SortedSet<Integer> keys = new TreeSet<>(participantsPerMatchPerRoundOrderNew.keySet());
//                        for (Integer key : keys) {
//                            for (CompetitionRound competitionRoundElt : competitionRound.competitionGroup.competitionRounds) {
//                                if (competitionRoundElt.round.compareTo(round) == 0) {
//                                    if (competitionRoundElt.participantPairings == null || competitionRoundElt.participantPairings.size() < competitionRoundElt.competitionMatches.size()) {
//                                        for (CompetitionMatch competitionMatch : competitionRoundElt.competitionMatches) {
//                                            if (competitionMatch.participantPairing == null) {
//                                                ParticipantPairing participantPairing = competitionRoundElt.getCompetitionInstance().createParticipantPairing(numberOfParticipantMatch);
//                                                participantPairing.setCompetitionMatch(competitionMatch);
//                                            }
//                                            if (competitionMatch.participantPairing.competitionRound == null)
//                                                competitionMatch.participantPairing.setCompetitionRound(competitionRoundElt);
//                                        }
//                                    }
//                                    List<List<Participant>> matchParticipantsList = participantsPerMatchPerRoundOrderNew.get(key);
//                                    while (!matchParticipantsList.isEmpty()) {
//                                        List<Participant> participantList = matchParticipantsList.get(0);
//                                        for (CompetitionMatch competitionMatch : competitionRoundElt.competitionMatches) {
//                                            if (competitionMatch.acceptParticipant() && competitionMatch.participantQuantity == participantList.size()) {
//                                                for (Participant participant : participantList) {
//                                                    competitionMatch.participantPairing.addParticipant(participant);
//                                                }
//                                                competitionMatch.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
//                                                matchParticipantsList.remove(0);
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    competitionRoundElt.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
//                                    break;
//                                }
//                            }
//                            round++;
//                        }
                    }
                }
                break;
                case LADDER:

                    break;
                case CUSTOM:

                    break;
                case NONE:

                    break;
            }
        }
        if (doPairing) {
            List<ParticipantPairing> participantPairingToRemove = new ArrayList<>();
            for (ParticipantPairing participantPairing : competitionRound.participantPairings) {
                participantPairing.removeParticipantTeamVoid();
//            if (participantPairing.getRealParticipantQuantity() == 0)
//                participantPairingToRemove.add(participantPairing);
            }
//        participantPairingToRemove.removeAll(participantPairingToRemove);
            if (!competitionRound.getCompetitionGroup().isParticipantPairingDefined() && competitionRound.isParticipantPairingDefined()) {
                for (ParticipantPairing participantPairing : competitionRound.participantPairings) {
                    participantPairing.setCompetitionGroup(competitionRound.competitionGroup);
                    if (competitionRound.competitionGroup.participantPairings == null)
                        competitionRound.competitionGroup.participantPairings = new TreeSet<>();
                    competitionRound.competitionGroup.participantPairings.add(participantPairing);
                }

            }
        }
        return competitionRound.participantPairings;
    }

   /* public void fillCompetitionMatchLinkSameGroup(CompetitionMatch competitionMatch) {
        if (competitionMatch.getCompetitionRound().getCompetitionRoundNext() != null
                && competitionMatch.getCompetitionRound().getCompetitionRoundNext().getCompetitionMatches() != null
                && !competitionMatch.getCompetitionRound().getCompetitionRoundNext().getCompetitionMatches().isEmpty()) {
            CompetitionCreationParamPhase competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//            if (competitionCreationParamPhase == null)
//                competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;


//            int matchQuantity = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.numberOfParticipantMatch / competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.participantQualifiedPerMatch;
//            CompetitionMatch[] competitionMatchesNext = new CompetitionMatch[competitionMatch.getCompetitionRound().getCompetitionRoundNext().getCompetitionMatches().size()];
            SortedSet<CompetitionMatch> competitionMatchesNext = competitionMatch.getCompetitionRound().getCompetitionRoundNext().getCompetitionMatches();
//            CompetitionMatch competitionMatchNext = null;
            int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
            int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
            int numberOfPreviousMatches = numberOfParticipantMatch / participantQualifiedPerMatch;
//            int expectedNumberOfMatchCurrentRound = competitionMatchesNext.length * numberOfPreviousMatches;
//            int numberOfMatchCurrentRound = competitionMatch.getCompetitionRound().getCompetitionMatches().size();
            for (CompetitionMatch competitionMatchNext : competitionMatchesNext) {
                if (competitionMatchNext.previousCompetitionMatchLinks == null) {
                    competitionMatchNext.previousCompetitionMatchLinks = new TreeSet<>();
                }
                if (competitionMatchNext.previousCompetitionMatchLinks.size() < numberOfPreviousMatches) {
                    CompetitionMatchLink competitionMatchLink = competitionMatchNext.addPreviousCompetitionMatch(competitionMatch);
                    fillCompetitionMatchLinkSameGroup(competitionMatchNext);
                    break;
                }
            }
        }
    }*/

    public void fillCompetitionMatchLink(CompetitionMatch competitionMatch) {
        SortedSet<CompetitionMatch> competitionMatchesPrevious = new TreeSet<>();
        SortedSet<Participant> participantWithCompetitionMatchNotFound = new TreeSet<>();

        if (this.compareTo(SWISS_WITH_ELIMINATION_LEVEL) != 0) {

            if (competitionMatch.participantPairing != null) {
                List<Participant> participants = competitionMatch.participantPairing.getRealParticipantsAsArray();

                List<Participant> participantsFound = new ArrayList<>(participants);
                if (competitionMatch.participantPairing != null) {
                    if (competitionMatch.getCompetitionRound().competitionRoundPrevious != null) {
                        CompetitionMatch competitionMatchForParticipant = null;
                        for (Participant participant : participants) {
                            competitionMatchForParticipant = competitionMatch.getCompetitionRound().getCompetitionRoundPrevious().getCompetitionMatchFor(participant);
                            if (competitionMatchForParticipant != null) {

                                ParticipantSeat participantSeatNext = competitionMatch.getParticipantSeatForParticipant(participant);
                                ParticipantResult participantResultNext = competitionMatch.getParticipantResultsForParticipant(participant);

                                ParticipantSeat participantSeatPrevious = competitionMatchForParticipant.getParticipantSeatForParticipant(participant);
                                ParticipantResult participantResultPrevious = competitionMatchForParticipant.getParticipantResultsForParticipant(participant);
                                competitionMatch.addPreviousCompetitionMatch(competitionMatchForParticipant, participantSeatPrevious.index, participantResultPrevious.rank, participantSeatNext.index, participantResultNext != null ? participantResultNext.rank : null);
                                participantsFound.remove(participant);
                            }
                            if (participantsFound.isEmpty())
                                break;
                        }
                    }
                }
                if (this.compareTo(ELIMINATION) == 0 && competitionMatch.competitionGroup.lane > 1) {
//                    participantsFound = new ArrayList<>(participants);
                    CompetitionGroup competitionGroupPreviousLane = competitionMatch.competitionGroup.competitionSeed.getCompetitionGroupForLane(competitionMatch.competitionGroup.lane - 1);
                    SortedSet<CompetitionRound> competitionRoundsSorted = new TreeSet<>(competitionGroupPreviousLane.competitionRounds);
                    List<CompetitionRound> competitionRounds = new ArrayList<>(competitionRoundsSorted);
                    Collections.reverse(competitionRounds);
                    CompetitionMatch competitionMatchForParticipant = null;
                    List<Participant> participantsToCheck = null;
                    for (CompetitionRound competitionRound : competitionRounds) {
                        participantsToCheck = new ArrayList<>(participantsFound);
                        for (Participant participant : participantsToCheck) {
                            if (!participant.isVoid()) {
                                competitionMatchForParticipant = competitionRound.getCompetitionMatchFor(participant);
                                if (competitionMatchForParticipant != null) {
                                    ParticipantSeat participantSeatNext = competitionMatch.getParticipantSeatForParticipant(participant);
                                    ParticipantResult participantResultNext = competitionMatch.getParticipantResultsForParticipant(participant);

                                    ParticipantSeat participantSeatPrevious = competitionMatchForParticipant.getParticipantSeatForParticipant(participant);
                                    ParticipantResult participantResultPrevious = competitionMatchForParticipant.getParticipantResultsForParticipant(participant);
                                    competitionMatch.addPreviousCompetitionMatch(competitionMatchForParticipant, participantSeatPrevious.index, participantResultPrevious.rank, participantSeatNext.index, participantResultNext != null ? participantResultNext.rank : null);
                                    participantsFound.remove(participant);
                                }
                            } else {
                                participantsFound.remove(participant);
                            }
                            if (participantsFound.isEmpty())
                                break;
                        }
                        if (participantsFound.isEmpty())
                            break;
                    }
                    if (competitionMatch.getCompetitionRound().isLatest()) {
                        participantsFound = new ArrayList<>(participants);
                        CompetitionSeed competitionSeedNext = competitionMatch.competitionGroup.competitionGroupResult.competitionSeedNext;
                        if (competitionSeedNext != null && competitionSeedNext.competitionPhase.compareTo(competitionMatch.competitionSeed.competitionPhase) == 0) {
                            SortedSet<CompetitionGroup> competitionGroups = competitionSeedNext.getCompetitionGroups();
                            competitionGroups = Sets.sort(competitionGroups);

                            for (CompetitionGroup competitionGroup : competitionGroups) {

                                SortedSet<CompetitionRound> competitionGroupRounds = competitionGroup.getCompetitionRounds();
                                Sets.sort(competitionGroupRounds);
                                for (CompetitionRound competitionRound : competitionGroupRounds) {
                                    participantsToCheck = new ArrayList<>(participantsFound);
                                    for (Participant participant : participants) {
                                        if (!participant.isVoid()) {
                                            competitionMatchForParticipant = competitionRound.getCompetitionMatchFor(participant);
                                            if (competitionMatchForParticipant != null) {

                                                ParticipantSeat participantSeatPrevious = competitionMatch.getParticipantSeatForParticipant(participant);
                                                ParticipantResult participantResultPrevious = competitionMatch.getParticipantResultsForParticipant(participant);

                                                ParticipantSeat participantSeatNext = competitionMatchForParticipant.getParticipantSeatForParticipant(participant);
                                                ParticipantResult participantResultNext = competitionMatchForParticipant.getParticipantResultsForParticipant(participant);

                                                CompetitionMatchLink competitionMatchLink = competitionMatch.addNextCompetitionMatch(competitionMatchForParticipant, participantSeatPrevious.index, participantResultPrevious.rank, participantSeatNext.index, participantResultNext.rank);
                                                participantsFound.remove(participant);
                                            }
                                        } else {
                                            participantsFound.remove(participant);
                                        }
                                        if (participantsFound.isEmpty())
                                            break;
                                    }
                                    if (participantsFound.isEmpty())
                                        break;
                                }
                            }
                        }
                    }
                } else if (this.compareTo(ELIMINATION) == 0 && (competitionMatch.competitionSeed.stepType.compareTo(StepType.MERGE) == 0 || competitionMatch.competitionSeed.stepType.compareTo(StepType.RESET) == 0)) {


                    for (CompetitionSeed previousCompetitionSeed : competitionMatch.competitionSeed.previousCompetitionSeeds) {
                        for (CompetitionGroup competitionGroup : previousCompetitionSeed.competitionGroups) {

                            SortedSet<CompetitionRound> competitionGroupRounds = competitionGroup.getCompetitionRounds();
                            CompetitionRound competitionRound = (CompetitionRound) Sets.sort(competitionGroupRounds).last();
                            for (Participant participant : competitionMatch.participantPairing.getRealParticipantsAsArray()) {
                                if (!participant.isVoid()) {
                                    CompetitionMatch competitionMatchForParticipant = competitionRound.getCompetitionMatchFor(participant);
                                    if (competitionMatchForParticipant != null) {

                                        ParticipantSeat participantSeatPrevious = competitionMatchForParticipant.getParticipantSeatForParticipant(participant);
                                        ParticipantResult participantResultPrevious = competitionMatchForParticipant.getParticipantResultsForParticipant(participant);

                                        ParticipantSeat participantSeatNext = competitionMatch.getParticipantSeatForParticipant(participant);
                                        ParticipantResult participantResultNext = competitionMatch.getParticipantResultsForParticipant(participant);

                                        CompetitionMatchLink competitionMatchLink = competitionMatchForParticipant.addNextCompetitionMatch(competitionMatch, participantSeatPrevious.index, participantResultPrevious.rank, participantSeatNext.index, participantResultNext.rank);
                                    }
                                }
                            }
                        }
                    }

                } else {
                    CompetitionMatch competitionMatchForParticipant = null;
                    if (competitionMatch.getCompetitionRound().isFirst()) {
                        Set<CompetitionSeed> competitionSeedPrevious = new HashSet<>();
                        for (CompetitionGroupResult competitionGroupResult : competitionMatch.competitionGroup.competitionSeed.competitionGroupResultsPrevious) {
                            if (competitionGroupResult.competitionSeed.competitionPhase.compareTo(competitionMatch.competitionSeed.competitionPhase) == 0) {
                                competitionSeedPrevious.add(competitionGroupResult.competitionSeed);
                            }
                        }
                        for (CompetitionSeed competitionSeed : competitionSeedPrevious) {
                            SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>(competitionSeed.getCompetitionGroups());
//                            Sets.sort(competitionGroups);
                            List<CompetitionGroup> competitionGroupsReversed = Arrays.asList(competitionGroups.toArray(new CompetitionGroup[competitionGroups.size()]));
                            Collections.reverse(competitionGroupsReversed);
                            participantsFound = new ArrayList<>(participants);
                            List<Participant> participantsToCheck = null;
                            for (CompetitionGroup competitionGroup : competitionGroupsReversed) {
                                CompetitionRound competitionRound = competitionGroup.getLastFinishedCompetitionGroupRound();
                                if (competitionRound != null) {
                                    participantsToCheck = new ArrayList<>(participantsFound);
                                    for (Participant participant : participantsToCheck) {
                                        if (!participant.isVoid()) {
                                            competitionMatchForParticipant = competitionRound.getCompetitionMatchFor(participant);
                                            if (competitionMatchForParticipant != null) {

                                                ParticipantSeat participantSeatNext = competitionMatch.getParticipantSeatForParticipant(participant);
                                                ParticipantResult participantResultNext = competitionMatch.getParticipantResultsForParticipant(participant);

                                                ParticipantSeat participantSeatPrevious = competitionMatchForParticipant.getParticipantSeatForParticipant(participant);
                                                ParticipantResult participantResultPrevious = competitionMatchForParticipant.getParticipantResultsForParticipant(participant);

                                                competitionMatch.addPreviousCompetitionMatch(competitionMatchForParticipant, participantSeatPrevious.index, participantResultPrevious.rank, participantSeatNext.index, participantResultNext.rank);
                                                participantsFound.remove(participant);
                                            }
                                        } else {
                                            participantsFound.remove(participant);
                                        }
                                        if (participantsFound.isEmpty())
                                            break;
                                    }

                                    if (participantsFound.isEmpty())
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (this.compareTo(ELIMINATION) != 0) {
//                if (!competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionGroupResultsPrevious().isEmpty()) {
//                    for (CompetitionGroupResult competitionGroupResult : competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionGroupResultsPrevious()) {
//                        if (competitionGroupResult.competitionGroup != null) {
//                            CompetitionMatch lastCompetitionMatchForParticipant = null;
//                            for (Participant participant : participantWithCompetitionMatchNotFound) {
//                                lastCompetitionMatchForParticipant = competitionGroupResult.getCompetitionGroup().getLastCompetitionMatchFor(participant);
//                                if (lastCompetitionMatchForParticipant != null)
//                                    competitionMatchesPrevious.add(lastCompetitionMatchForParticipant);
//                            }
//                        }
//                    }
//                }
            } else if (this.compareTo(ELIMINATION) == 0) {
//                throw new CompetitionInstanceGeneratorException();
//                fillCompetitionMatchLinkSameGroup(competitionMatch);
//                fillCompetitionMatchLinkNextGroup(competitionMatch);
            }
        }


    }

   /* private void fillCompetitionMatchLinkNextGroup(CompetitionMatch competitionMatch) {
        if (competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionGroups().size() > competitionMatch.getCompetitionRound().getCompetitionGroup().lane) {
//            CompetitionGroup[] competitionGroups = new CompetitionGroup[competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionGroups().size()];
            CompetitionGroup competitionGroupLooser = competitionMatch.competitionSeed.getCompetitionGroupForLane(competitionMatch.competitionGroup.lane + 1);
            if (competitionGroupLooser != null) {
                boolean done = false;
//            int matchQuantity = (competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.numberOfParticipantMatch - competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.participantQualifiedPerMatch) / competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.numberOfParticipantMatch;
//            int matchDivider = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.numberOfParticipantMatch / (competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.numberOfParticipantMatch - competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().phaseParameter.participantQualifiedPerMatch);
                CompetitionCreationParamPhase competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//                if (competitionCreationParamPhase == null)
//                    competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
                int numberOfPreviousMatches = competitionCreationParamPhase.numberOfParticipantMatch / (competitionCreationParamPhase.numberOfParticipantMatch - competitionCreationParamPhase.participantQualifiedPerMatch);
                SortedSet<CompetitionRound> competitionGroupRounds = competitionGroupLooser.getCompetitionRounds();
                Sets.sort(competitionGroupRounds);

                for (CompetitionRound competitionRoundLooser : competitionGroupRounds) {
                    for (CompetitionMatch competitionMatchLooser : competitionRoundLooser.getCompetitionMatches()) {
                        if (competitionMatchLooser.previousCompetitionMatchLinks == null) {
                            competitionMatchLooser.previousCompetitionMatchLinks = new TreeSet<>();
                        }
                        if (competitionMatchLooser.previousCompetitionMatchLinks.size() < numberOfPreviousMatches) {
                            competitionMatchLooser.addPreviousCompetitionMatch(competitionMatch);
                            fillCompetitionMatchLinkSameGroup(competitionMatchLooser);
                            done = true;
                        }
                        if (done)
                            break;
                    }
                    if (done)
                        break;
                }
            }
        }
    }*/

    public void fillCompetitionGroupResult(CompetitionGroup competitionGroup) {
        SortedSet<ParticipantResult> participantResults = new TreeSet<>();
        List<Participant> participants = new ArrayList<>(competitionGroup.getRealParticipantsAsArray());
        SortedSet<CompetitionRound> competitionRounds = competitionGroup.getCompetitionRounds();
        Sets.sort(competitionRounds);
        switch (this) {
            case ELIMINATION: {
                ParticipantResult participantResult = null;
                for (CompetitionRound competitionRound : competitionRounds) {
                    int bonus = MathUtils.getPointBonus(competitionRound.round);
                    for (ParticipantResult participantResultRound : competitionRound.participantResults) {
                        participantResult = findParticipantResult(participantResultRound.participant, participantResults);
                        if (participantResult == null) {
                            participantResult = ParticipantResult.createParticipantResultFor(competitionGroup.getIdGenerator(), competitionGroup);
                            participantResult.setParticipant(participantResultRound.participant);
                            participantResults.add(participantResult);
                        }
                        int score = participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_POINTS).computeNumberValue().intValue() + bonus;
                        ParticipantScoreValue participantScoreValueCurrent = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS);
                        if (participantScoreValueCurrent != null) {
//                            score = participantScoreValueCurrent.computeNumberValue().intValue() + score * 10;
                            score = participantScoreValueCurrent.computeNumberValue().intValue() + score;
//                            bye= bye.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_BYE).computeNumberValue().doubleValue();
                        }
                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS, score);

                        ParticipantScoreValue lastActiveRound = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND);
                        if (lastActiveRound == null || lastActiveRound.computeNumberValue() == null || lastActiveRound.computeNumberValue().intValue() < competitionRound.round) {
                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND, competitionRound.round);
                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND_RANK, participantResultRound.rank);
                        }
                        participantResult.participantScore.participantScoresSub.add(participantResultRound.participantScore);
                    }
                }
                if (competitionGroup.competitionRounds != null && !competitionGroup.competitionRounds.isEmpty()) {
                    for (ParticipantResult participantResultGroup : participantResults) {
                        int points = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).computeNumberValue().intValue();
                        int lastActiveRound = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND).computeNumberValue().intValue();
                        points = points + lastActiveRound;
                        participantResultGroup.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS, points);
                    }
                }
//                if (competitionGroup.round > 1) {
//                    for (ParticipantResult participantResultGroup : participantResults) {
//                        participantResultGroup.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS, Integer.valueOf(participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).value) - competitionGroup.competitionInstance.competitionCreationParam.numberOfParticipantMatch);
//                    }
//                }
            }
            break;
            case SWISS:
            case ROUND_ROBIN:
            case LADDER:
            case CUSTOM: {
                ParticipantResult participantResult = null;
                ParticipantResult participantResultRound = null;
                for (Participant participant : participants) {
                    participantResult = ParticipantResult.createParticipantResultFor(competitionGroup.getIdGenerator(), competitionGroup);
                    participantResult.setParticipant(participant);
                    Number score = 0;
                    Number win = 0;
                    Number loss = 0;
                    Number draw = 0;
                    Number bye = 0;
                    for (CompetitionRound competitionRound : competitionRounds) {
                        participantResultRound = competitionRound.getParticipantResultsForParticipant(participant);
                        if (participantResultRound != null) {
                            score = score.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_POINTS).computeNumberValue().doubleValue();
                            if (competitionGroup.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch > 1) {
                                bye = bye.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_BYE).computeNumberValue().doubleValue();
                                win = win.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_WIN).computeNumberValue().doubleValue();
                                loss = loss.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_LOSS).computeNumberValue().doubleValue();
                                draw = draw.doubleValue() + participantResultRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_DRAW).computeNumberValue().doubleValue();
                            }

//                            ParticipantScoreValue lastActiveRound = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND);
//                            if (lastActiveRound == null || lastActiveRound.computeNumberValue() == null || lastActiveRound.computeNumberValue().intValue() < competitionRound.round) {
//                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND, competitionRound.round);
//                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND_RANK, participantResultRound.rank);
//                            }
                            participantResult.participantScore.participantScoresSub.add(participantResultRound.participantScore);
                        }
                    }
//                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.ACTIVE_GROUP, competitionGroup.getLocalId());
                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS, score);
                    if (competitionGroup.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch > 1) {
                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_BYE, bye);
                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_WIN, win);
                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_LOSS, loss);
                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.SCORE_DRAW, draw);
                    }
                    participantResults.add(participantResult);
                }
            }
            break;
            case NONE: {
                ParticipantResult participantResult = null;
                for (Participant participant : participants) {
                    participantResult = ParticipantResult.createParticipantResultFor(competitionGroup.getIdGenerator(), competitionGroup);
                    participantResult.setParticipant(participant);
                    participantResults.add(participantResult);
                }
            }
            break;
        }
        int rank = 1;
        ParticipantScore participantScorePrevious = null;
        Sets.sort(participantResults);
        ParticipantScore.fillRank(participantResults, ParticipantScoreGroup.RANK);
        ParticipantScore.fillPoints(participantResults, ParticipantScoreGroup.SCORE_POINTS);


        if (competitionGroup.competitionGroupResult == null) {
            CompetitionGroupResult competitionGroupResult = competitionGroup.competitionInstance.createCompetitionGroupResult(competitionGroup);
//            try {
            competitionGroupResult.internationalizedLabel = (InternationalizedLabel) competitionGroup.internationalizedLabel.clone();
//
            competitionGroup.setCompetitionGroupResult(competitionGroupResult);

//              } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//                //TODO handle error
//            }
        } else {
            competitionGroup.getCompetitionGroupResult().participantResults.clear();
        }
        competitionGroup.getCompetitionGroupResult().addAllParticipantResults(participantResults);

    }

    private ParticipantResult findParticipantResult(Participant
                                                            participant, SortedSet<ParticipantResult> participantResults) {
        ParticipantResult participantResult = null;
        for (ParticipantResult participantResultCurrent : participantResults)
            if (participantResultCurrent.participant.compareTo(participant) == 0)
                participantResult = participantResultCurrent;
        return participantResult;
    }

//    public int getInitialNumberOfParticipantIn(int numberOfParticipantIn, int numberOfParticipantMatch, int competitionGroupLane) {
//        int initialNumberOfParticipantIn = (int) Math.ceil((double) numberOfParticipantIn / (double) competitionGroupLane);
//        switch (this) {
//            case ELIMINATION:
//                initialNumberOfParticipantIn = numberOfParticipantIn;
//                int numberOfParticipantInQualified = 0;
//                for (int i = 0; i < competitionGroupLane - 1; i++) {
//                    numberOfParticipantInQualified = (int) Math.ceil((double) initialNumberOfParticipantIn / (double) numberOfParticipantMatch);
//                    initialNumberOfParticipantIn = initialNumberOfParticipantIn - numberOfParticipantInQualified;
//                }
//
//                break;
//            default:
//                break;
//        }
//        return initialNumberOfParticipantIn;
//    }

    public void fillCompetitionRoundResult(CompetitionRound competitionRound) {
        SortedSet<ParticipantResult> participantResults = new TreeSet<>();
//        List<Participant> participants = new ArrayList<Participant>();
        Number points = 0;
        Number cumulativePoints = 0;
        Number win = 0;
        Number loss = 0;
        Number bye = 0;
        Number draw = 0;
        ParticipantResult participantResultMatch = null;
        ParticipantResult participantResultRound = null;
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionRound.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//        if (competitionCreationParamPhase == null)
//            competitionCreationParamPhase = competitionRound.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

        for (ParticipantPairing participantPairing : competitionRound.participantPairings) {
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                points = 0;
                bye = 0;
                win = 0;
                loss = 0;
                draw = 0;
                int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
                int matchInRoundSize = competitionRound.getCompetitionMatches().size();
                participantResultRound = ParticipantResult.createParticipantResultFor(competitionRound.getIdGenerator(), competitionRound);
                for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                    competitionMatch.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
                    participantResultMatch = competitionMatch.getParticipantResultsForParticipant(participant);
                    if (participantResultMatch != null) {
                        participantResultRound.participantScore.participantScoresSub.add(participantResultMatch.participantScore);
//                        if (this.compareTo(ELIMINATION) == 0 && competitionRound.competitionGroup.round == 1 && competitionRound.round == competitionRound.competitionGroup.competitionRounds.size() && competitionMatch.round == 1 && competitionRound.competitionMatches.size() > 1) {
//                            score = score.doubleValue() + Math.pow(10, competitionMatch.round) + participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_POINTS).computeNumberValue().doubleValue();
//                        } else {
                        points = points.doubleValue() + participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_POINTS).computeNumberValue().doubleValue();
                        win = win.doubleValue() + participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_WIN).computeNumberValue().doubleValue();
                        loss = loss.doubleValue() + participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_LOSS).computeNumberValue().doubleValue();
                        draw = draw.doubleValue() + participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_DRAW).computeNumberValue().doubleValue();

//                        }
                        if (competitionMatch.matchType.compareTo(MatchType.BYE) == 0)
                            bye = participantResultMatch.participantScore.getParticipantScoreValue(ParticipantScoreMatch.SCORE_BYE).computeNumberValue().doubleValue();
                        if (competitionRound.competitionRoundPrevious != null) {
                            ParticipantResult participantResultsForParticipantForPreviousRound = competitionRound.getCompetitionRoundPrevious().getParticipantResultsForParticipant(participant);
                            if (participantResultsForParticipantForPreviousRound != null) {
                                ParticipantScoreValue participantScoreValuePreviousBye = participantResultsForParticipantForPreviousRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_BYE);
                                if (participantScoreValuePreviousBye != null)
                                    bye = bye.intValue() + participantScoreValuePreviousBye.computeNumberValue().intValue();
                            }
                        }
                        break;
                    }
                }
                cumulativePoints = points;
                if (competitionRound.competitionRoundPrevious != null) {
                    ParticipantResult participantResultPreviousRound = competitionRound.competitionRoundPrevious.getParticipantResultsForParticipant(participant);
                    if (participantResultPreviousRound != null)
                        cumulativePoints = cumulativePoints.doubleValue() + participantResultPreviousRound.participantScore.getParticipantScoreValue(ParticipantScoreRound.SCORE_CUMULATIVE_POINTS).computeNumberValue().doubleValue();
                }
                participantResultRound.setParticipant(participant);
//                if (this.compareTo(ELIMINATION) == 0 && competitionRound.competitionGroup.round > 1)
//                    score = score.doubleValue() - (double) competitionRound.competitionGroup.round;
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_CUMULATIVE_POINTS, cumulativePoints);
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_POINTS, points);
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_BYE, bye);
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_WIN, win);
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_LOSS, loss);
                participantResultRound.participantScore.setParticipantScoreValue(ParticipantScoreRound.SCORE_DRAW, draw);
                participantResults.add(participantResultRound);
            }
        }

        Sets.sort(participantResults);


        ParticipantScore.fillRank(participantResults, ParticipantScoreRound.RANK);


        competitionRound.participantResults.clear();
        competitionRound.participantResults.addAll(participantResults);

    }

    public SortedSet<ParticipantResult> getParticipantResultsEliminated(CompetitionRound competitionRound) {
        SortedSet<ParticipantResult> participantResultsEliminated = new TreeSet<>();
        if (competitionRound.isParticipantResultsSet()) {
            for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                participantResultsEliminated.addAll(competitionMatch.participantResults);
            }
            participantResultsEliminated.removeAll(getParticipantResultsQualified(competitionRound));
            Sets.sort(participantResultsEliminated);
        }
        return participantResultsEliminated;
    }

    public SortedSet<ParticipantResult> getParticipantResultsQualified(CompetitionRound competitionRound) {
        SortedSet<ParticipantResult> participantResultsQualified = new TreeSet<>();
        if (competitionRound.isParticipantResultsSet()) {
            CompetitionCreationParamPhase competitionCreationParamPhase = competitionRound.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
            if (competitionCreationParamPhase.numberOfParticipantMatch == 1) {
                int participantQualifiedSize = 1;
                while (participantQualifiedSize < competitionRound.competitionMatches.size()) {
                    participantQualifiedSize = participantQualifiedSize * 2;
                }
                participantQualifiedSize = participantQualifiedSize / 2;


                ParticipantResult[] participantResultsArray = new ParticipantResult[participantQualifiedSize];
                participantResultsArray = competitionRound.getParticipantResults().toArray(participantResultsArray);
                participantResultsQualified.addAll(Arrays.asList(participantResultsArray).subList(0, participantQualifiedSize));
            } else {
                for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                    ParticipantResult[] participantResults = new ParticipantResult[competitionMatch.participantResults.size()];
                    competitionMatch.participantResults.toArray(participantResults);
//                for (int i = 0; i < getNumberOfParticipantQualifiedPerMatch(competitionMatch, competitionCreationParam); i++) {
//                    participantResultsQualified.add(participantResults[i]);
//                }
                    participantResultsQualified.addAll(Arrays.asList(participantResults).subList(0, getNumberOfParticipantQualifiedPerMatch(competitionMatch)));
                }
            }
            Sets.sort(participantResultsQualified);
        }
        return participantResultsQualified;
    }

    public int getNumberOfParticipantQualifiedPerMatch(CompetitionMatch competitionMatch) {
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//        if (competitionCreationParamPhase == null)
//            competitionCreationParamPhase = competitionMatch.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
        int participantPresent = competitionMatch.participantPairing.getRealParticipantQuantity();
        int numberOfParticipantQualified = computeNumberOfQualifiedParticipantPerMatch(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, competitionCreationParamPhase.participantQualifiedPerMatch, participantPresent);
        return numberOfParticipantQualified;
    }

    public int getNumberOfParticipantEliminatedPerMatch(CompetitionMatch competitionMatch) {
        int participantPresent = competitionMatch.getParticipantPairings().iterator().next().participantSeats.size();
        int numberOfParticipantQualified = getNumberOfParticipantQualifiedPerMatch(competitionMatch);
        int numberOfParticipantEliminated = participantPresent - numberOfParticipantQualified;
        return numberOfParticipantEliminated;
    }

    public int computePlayQuantity(ParticipantType participantType, int numberOfParticipantPerMatch,
                                   int participantQualifiedPerMatch, PlayVersusType
                                           playVersusType, int minPlayQuantity, int maxPlayQuantity, int laneQuantity, int lane, int roundQuantity,
                                   int round, boolean allowEvenNumber, int matchQuantity, boolean finalGroupSizeThresholdEnabled,
                                   int finalGroupSizeThreshold, int numberOfPlayPerMatchMinimumFinalGroup,
                                   int numberOfPlayPerMatchMaximumFinalGroup, boolean thirdPlaceMatch) {
        // in case of TEAM of X players Vs TEAM of X players  on YvY game with X > Y, all players must playDetails.
        int playMultiplicator = 1;
        if (participantType.numberOfParticipants > playVersusType.teamSize)
            playMultiplicator = (int) Math.ceil((double) participantType.numberOfParticipants / (double) playVersusType.teamSize);

        int playQuantity = 0;

        int minPlayQuantityUsed = minPlayQuantity;
        int maxPlayQuantityUsed = maxPlayQuantity;
        if (this.compareTo(ELIMINATION) == 0) {
            if (finalGroupSizeThresholdEnabled && matchQuantity * numberOfParticipantPerMatch <= finalGroupSizeThreshold) {
                if (lane == 1 && (laneQuantity == 1 || thirdPlaceMatch)) {
                    minPlayQuantityUsed = numberOfPlayPerMatchMinimumFinalGroup;
                    maxPlayQuantityUsed = numberOfPlayPerMatchMaximumFinalGroup;
                } else {
                    if (round == roundQuantity && (laneQuantity + (thirdPlaceMatch ? 0 : 1) - lane) * numberOfParticipantPerMatch <= finalGroupSizeThreshold) {
                        minPlayQuantityUsed = numberOfPlayPerMatchMinimumFinalGroup;
                        maxPlayQuantityUsed = numberOfPlayPerMatchMaximumFinalGroup;
                    }

                }
            }
            double percentage;
            if (roundQuantity > 0) {
                if (roundQuantity > 1) {
                    percentage = (((double) round - 1) / ((double) roundQuantity - 1)) / Math.pow(lane, 1.0 / (double) (laneQuantity - lane + 1));
                } else {
                    percentage = 1;
                }
            } else {
                percentage = 0;
            }
            playQuantity = minPlayQuantityUsed + (int) Math.ceil(percentage * (double) (maxPlayQuantityUsed - minPlayQuantityUsed));
        } else {
            if (finalGroupSizeThresholdEnabled && matchQuantity * numberOfParticipantPerMatch <= finalGroupSizeThreshold) {
                minPlayQuantityUsed = numberOfPlayPerMatchMinimumFinalGroup;
                maxPlayQuantityUsed = numberOfPlayPerMatchMaximumFinalGroup;
            } else {
                minPlayQuantityUsed = minPlayQuantity;
                maxPlayQuantityUsed = maxPlayQuantity;
            }
            playQuantity = minPlayQuantityUsed;
        }
        if (!allowEvenNumber && playQuantity % 2 == 0)
            playQuantity = playQuantity + 1;
        if (playQuantity + (playQuantity + 1 % 2) > maxPlayQuantity)
            playQuantity = playQuantity - ((playQuantity + 1) % 2);
        else
            playQuantity = playQuantity + ((playQuantity + 1) % 2);
        if (playQuantity > maxPlayQuantityUsed)
            playQuantity = maxPlayQuantityUsed;
        if (playQuantity < minPlayQuantityUsed)
            playQuantity = minPlayQuantityUsed;
        playQuantity = playQuantity * playMultiplicator;

        if (playQuantity > maxPlayQuantity)
            playQuantity = maxPlayQuantity;
        if (playQuantity < minPlayQuantity)
            playQuantity = minPlayQuantity;

        return playQuantity;

    }

   /* public boolean isBye(CompetitionMatch competitionMatch, int matchInRoundSize, int participantQualifiedPerMatch) {
        if (competitionMatch.participantPairing != null) {
            int participantQuantity = competitionMatch.participantPairing.getRealParticipantQuantity();
            boolean onlyMatchInRound = matchInRoundSize == 1;

            switch (this) {
                case ELIMINATION:
                    return participantQuantity <= participantQualifiedPerMatch && onlyMatchInRound;
                case ROUND_ROBIN:
                case SWISS:
                    return participantQuantity <= participantQualifiedPerMatch;
            }
        }
        return false;
    }*/

    public int getNumberOfGroups(ParticipantType participantType, int numberOfParticipantPerMatch, PlayVersusType
            playVersusType, int numberOfParticipant, int minNumberOfParticipantGroup, int maxNumberOfParticipantGroup,
                                 int participantQuantityOut, int participantQualifiedPerMatch, boolean allowEvenNumber, Integer
                                         numberOfRoundMinimum, Integer numberOfRoundMaximum, boolean thirdPlaceMatch, boolean fixedParticipantSize,
                                 boolean participantSplittable
    ) {
        SortedSet<Integer> groupsQuantities = new TreeSet<>();
        BigDecimal groupSize1 = BigDecimal.valueOf(getClosestAcceptableSize(numberOfParticipantPerMatch, participantQualifiedPerMatch, Math.min(numberOfParticipant, minNumberOfParticipantGroup), true));
        BigDecimal groupSize2 = BigDecimal.valueOf(getClosestAcceptableSize(numberOfParticipantPerMatch, participantQualifiedPerMatch, Math.min(numberOfParticipant, maxNumberOfParticipantGroup), true));
        groupsQuantities.add(BigDecimal.valueOf(numberOfParticipant).divide(groupSize1, RoundingMode.UP).setScale(0, RoundingMode.UP).intValue());
        groupsQuantities.add(BigDecimal.valueOf(numberOfParticipant).divide(groupSize2, RoundingMode.UP).setScale(0, RoundingMode.UP).intValue());
        Sets.sort(groupsQuantities);
        int minGroupQuantity = groupsQuantities.first();
        int maxGroupQuantity = groupsQuantities.last();
        int numberOfGroups = 0;
        if (participantQuantityOut == 1) {
            numberOfGroups = 1;
        } else {
            if (minGroupQuantity != maxGroupQuantity) {
                SortedSet<Integer> possibleGroupSizesBasedOnParticipantQuantityOut = new TreeSet<>();
                possibleGroupSizesBasedOnParticipantQuantityOut.add(1);
                for (int prime : PRIME_NUMBERS_UNDER_100) {
                    int participantQuantityOutTmp = participantQuantityOut;
                    while (participantQuantityOutTmp % prime == 0 && participantQuantityOutTmp > 1) {
                        SortedSet<Integer> possibleGroupSizesBasedOnParticipantQuantityOutTmp = new TreeSet<>();
                        possibleGroupSizesBasedOnParticipantQuantityOutTmp.add(prime);
                        for (Integer possibleGroupSize : possibleGroupSizesBasedOnParticipantQuantityOut) {
                            possibleGroupSizesBasedOnParticipantQuantityOutTmp.add(possibleGroupSize * prime);
                        }
                        possibleGroupSizesBasedOnParticipantQuantityOut.addAll(possibleGroupSizesBasedOnParticipantQuantityOutTmp);
                        participantQuantityOutTmp = participantQuantityOutTmp / prime;
                    }
                }
                Sets.sort(possibleGroupSizesBasedOnParticipantQuantityOut);
                int minNumberOfGroupTmp = BigDecimal.valueOf(numberOfParticipant).divide(BigDecimal.valueOf(maxNumberOfParticipantGroup), RoundingMode.UP).toBigInteger().intValueExact();
                int maxNumberOfGroupTmp = BigDecimal.valueOf(numberOfParticipant).divide(BigDecimal.valueOf(minNumberOfParticipantGroup), RoundingMode.UP).toBigInteger().intValueExact();
                int minNumberOfGroup = Math.min(maxNumberOfGroupTmp, minNumberOfGroupTmp);
                int maxNumberOfGroup = Math.max(maxNumberOfGroupTmp, minNumberOfGroupTmp);
                possibleGroupSizesBasedOnParticipantQuantityOut = possibleGroupSizesBasedOnParticipantQuantityOut.subSet(minNumberOfGroup, maxNumberOfGroup + 1);
                if (!possibleGroupSizesBasedOnParticipantQuantityOut.isEmpty())
                    minNumberOfGroup = possibleGroupSizesBasedOnParticipantQuantityOut.first();
//            maxNumberOfGroup = possibleGroupSizesBasedOnParticipantQuantityOut.last();
//
//            SortedSet<Integer> possibleGroupSizesBasedOnNumberOfTeam = new TreeSet<>();
//            for (int divider = 1; divider < numberOfParticipant / participantQualifiedPerMatch; divider++) {
//                if ((numberOfParticipant / participantQualifiedPerMatch) % divider == 0) {
//                    possibleGroupSizesBasedOnNumberOfTeam.add(divider * participantQualifiedPerMatch);
//                }
//            }
//
//            Sets.sort(possibleGroupSizesBasedOnNumberOfTeam);
//            possibleGroupSizesBasedOnNumberOfTeam = possibleGroupSizesBasedOnNumberOfTeam.subSet(minNumberOfGroup,maxNumberOfGroup + 1);


//            SortedSet<Integer> possibleGroupSizesBasedCompetitionGroupFormat = new TreeSet<>();
                Map<Integer, SortedSet<Integer>> possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp = new HashMap<>();
                SortedMap<Integer, SortedSet<Integer>> possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye = new TreeMap<>();

                for (int possibleGroupSizeBasedCompetitionGroupFormat = 1; possibleGroupSizeBasedCompetitionGroupFormat < numberOfParticipant / 2 + 1; possibleGroupSizeBasedCompetitionGroupFormat++) {
                    CompetitionGroupFormatTree competitionGroupFormatTree = new CompetitionGroupFormatTree(this, participantType, numberOfParticipantPerMatch, participantQualifiedPerMatch, playVersusType, 1, 1, allowEvenNumber, false, 0, 0, 0, thirdPlaceMatch);
                    for (int i = 0; i < possibleGroupSizeBasedCompetitionGroupFormat; i++) {
                        CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup = competitionGroupFormatTree.addCompetitionGroupFormatTreeGroup();
                    }
                    competitionGroupFormatTree.computeInitialGroupParticipant(numberOfParticipant, numberOfParticipantPerMatch, participantQualifiedPerMatch);
                    int participantQuantityOutPerGroup = BigDecimal.valueOf(participantQuantityOut).divide(BigDecimal.valueOf(competitionGroupFormatTree.competitionGroupFormatTreeGroups.size()), RoundingMode.UP).toBigInteger().intValueExact();
                    for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTree.competitionGroupFormatTreeGroups) {
                        competitionGroupFormatTreeGroup.computeCompetitionGroupFormatTreeRound(null, this, numberOfParticipantPerMatch, playVersusType, participantQuantityOutPerGroup, participantQualifiedPerMatch, numberOfRoundMinimum, numberOfRoundMaximum, fixedParticipantSize, participantSplittable);
                        //              competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.first().computeCompetitionGroupFormatTreeMatches(participantQualifiedPerMatch);
                    }
                    int numberOfBye = competitionGroupFormatTree.getNumberOfBye();
                    if (!possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.containsKey(numberOfBye))
                        possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.put(numberOfBye, new TreeSet<>());
                    possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.get(numberOfBye).add(possibleGroupSizeBasedCompetitionGroupFormat);
                }
                //possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.putAll(possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp);

                for (Integer numberOfBye : possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.keySet()) {
                    SortedSet<Integer> possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye = possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.get(numberOfBye);
                    possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye = possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye.subSet(minNumberOfGroup, maxNumberOfGroup + 1);
                    if (!possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye.isEmpty())
                        possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.put(numberOfBye, possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye);
                }


                SortedSet<Integer> bestChoices = new TreeSet<>();
                int numberOfBye = 0;
                possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.clear();
                possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.putAll(possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye);
                while (bestChoices.isEmpty() && !possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.isEmpty()) {
                    bestChoices.addAll(possibleGroupSizesBasedOnParticipantQuantityOut);
                    if (possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.containsKey(numberOfBye) && !possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.get(numberOfBye).isEmpty()) {
                        bestChoices.retainAll(possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.get(numberOfBye));
                        possibleGroupSizesBasedCompetitionGroupFormatByNumberOfByeTemp.remove(numberOfBye);
                    }
                    if (bestChoices.isEmpty())
                        numberOfBye++;
                }
                if (!bestChoices.isEmpty() && numberOfBye == 0) {
                    Sets.sort(bestChoices);
                    numberOfGroups = bestChoices.last();
                } else if (possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.containsKey(0) && !possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.get(0).isEmpty()) {
                    Integer previousGap = null;
                    Integer selectedValue = null;
                    Integer currentGap = null;
                    Integer currentGapTmp = null;
                    for (Integer possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye : possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.get(0)) {
                        for (Integer possibleGroupSizeBasedOnParticipantQuantityOut : possibleGroupSizesBasedOnParticipantQuantityOut) {
                            currentGapTmp = Math.abs(possibleGroupSizeBasedOnParticipantQuantityOut - possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye);
                            if (currentGap == null || currentGapTmp.compareTo(currentGap) < 0)
                                currentGap = currentGapTmp;
                        }

                        if (previousGap == null || currentGap.compareTo(previousGap) <= 0) {
                            selectedValue = possibleGroupSizeBasedCompetitionGroupFormatByNumberOfBye;
                            previousGap = currentGap;
                        }
                    }
                    if (selectedValue != null)
                        numberOfGroups = selectedValue;
                } else if (!possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.isEmpty()) {
                    numberOfGroups = possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.get(possibleGroupSizesBasedCompetitionGroupFormatByNumberOfBye.firstKey()).last();
                } else if (!possibleGroupSizesBasedOnParticipantQuantityOut.isEmpty()) {
                    numberOfGroups = possibleGroupSizesBasedOnParticipantQuantityOut.last();
                } else {
                    numberOfGroups = 1;
                }
//            bestChoices.retainAll(possibleGroupSizesBasedOnNumberOfTeam);
                if (numberOfGroups > 1 && MathUtils.isPrime(numberOfParticipant)) {
                    numberOfGroups =
                            BigDecimal.valueOf(numberOfGroups).divide(BigDecimal.valueOf(4.0 * Math.log10(numberOfParticipant)), RoundingMode.UP).toBigInteger().intValueExact();
//                            (int) Math.ceil((double) numberOfGroups / (4.0 * Math.log10(numberOfParticipant)));
                }
            }
        }

        if (numberOfGroups < minGroupQuantity)
            numberOfGroups = minGroupQuantity;
        if (numberOfGroups > maxGroupQuantity)
            numberOfGroups = maxGroupQuantity;

        return numberOfGroups;
    }

//    public int getClosestAcceptableSize(int numberOfParticipantInMatch, int numberOfParticipantQualifiedPerMatch, int numberOfParticipant) {
//        return getClosestAcceptableSize(numberOfParticipantInMatch, numberOfParticipantQualifiedPerMatch, numberOfParticipant, true);
//    }

    public int getClosestAcceptableSize(int numberOfParticipantInMatch, int numberOfParticipantQualifiedPerMatch,
                                        int numberOfParticipant, boolean strict) {
        int closestAcceptableSize = numberOfParticipantInMatch;
        switch (this) {
            case ELIMINATION:
                if (numberOfParticipantInMatch > 1) {
                    int totalParticipants = 0;
                    for (int i = 0; i < 128; i++) {
                        totalParticipants = (int) Math.pow(numberOfParticipantInMatch / numberOfParticipantQualifiedPerMatch, i + 1);
                        if (totalParticipants >= numberOfParticipant) {
                            if (strict)
                                closestAcceptableSize = (int) Math.pow(numberOfParticipantInMatch / numberOfParticipantQualifiedPerMatch, i);
                            else
                                closestAcceptableSize = (int) Math.pow(numberOfParticipantInMatch / numberOfParticipantQualifiedPerMatch, i + 1);
//                        if (closestAcceptableSize < numberOfParticipantInMatch)
//                            closestAcceptableSize = numberOfParticipantInMatch;
//                        if (closestAcceptableSize > numberOfParticipant) {
//                            if (!strict)
//                                closestAcceptableSize = (int) Math.pow(numberOfParticipantInMatch / numberOfParticipantQualifiedPerMatch, i);
//                            else
//                                closestAcceptableSize = numberOfParticipant;
//                        }
                            break;
                        }

                    }
                } else {
                    closestAcceptableSize = numberOfParticipant;
                }

                break;
            default:
                closestAcceptableSize = (int) Math.ceil((double) numberOfParticipant / (double) numberOfParticipantInMatch) * numberOfParticipantInMatch;
                break;
        }
        return closestAcceptableSize;
    }

    public int getNextAcceptableSize(int numberOfParticipantInMatch, int numberOfParticipantQualifiedPerMatch,
                                     int numberOfParticipant) {
        return getClosestAcceptableSize(numberOfParticipantInMatch, numberOfParticipantQualifiedPerMatch, numberOfParticipant, false);
    }

    public MatchLinkType getMatchLinkType() {
        return matchLinkType;
    }
}

