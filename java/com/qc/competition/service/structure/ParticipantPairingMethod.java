package com.qc.competition.service.structure;

import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionCreationParamPhaseMixing;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.utils.Sets;

import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
public enum ParticipantPairingMethod {
    BASED_ON_PREVIOUS_RESULT, RANDOM, INITIAL, TOP_VS_MIDDLE;

//    public SortedSet<ParticipantPairing> createPairingForCompetitionGroupRoundFrom(CompetitionRound competitionRound) {
//        SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
//
//        List<ParticipantPairing> participantPairingList = new ArrayList<>();
//        switch (this) {
////            case SHIFT:
////            case TOP_VS_MIDDLE:
////            case RANDOM:
//            case BASED_ON_PREVIOUS_RESULT: {
//                if (competitionRound.competitionRoundPrevious == null) {
//                    competitionRound.addAllParticipantPairings(competitionRound.competitionGroup.participantPairings);
//                } else {
//                    for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
//                        if (competitionMatch.participantPairing.isVoid()) {
//                            ParticipantPairing participantPairing = new ParticipantPairing();
//                            participantPairing.competitionRound = competitionRound;
//                            participantPairing.competitionMatch = competitionMatch;
//                            participantPairingList.add(participantPairing);
//                        } else {
//                            participantPairingList.add(competitionMatch.participantPairing);
//                        }
//                    }
//
//                    List<Participant> participantQualified = new ArrayList<>();
//
//                    if (competitionRound.competitionRoundPrevious != null) {
//                        participantQualified.addAll(competitionRound.competitionRoundPrevious.getParticipantsQualified());
//                    }
//
//                    if (competitionRound.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && competitionRound.competitionGroup.round > 1) {
//                        CompetitionGroup competitionGroupPreviousLane = competitionRound.competitionGroup.competitionSeed.getCompetitionGroupForLane(competitionRound.competitionGroup.round - 1);
//                        participantQualified.addAll(competitionGroupPreviousLane.getCompetitionRoundForRound(competitionRound.roundDetails).getParticipantsEliminated());
//                    }
//
//                    int numberOfTeam = competitionRound.competitionGroup.competitionInstance.competitionCreationParam.playVersusType.numberOfTeam;
//                    int participantPairingSeatSizeMininum = (int) Math.floor((double) participantQualified.size() / (double) numberOfTeam);
//                    int participantPairingSeatsMissingQuantity = numberOfTeam - participantQualified.size() % numberOfTeam;
//                    int participantPairingIndex = 0;
//                    boolean moveToNextPairing = false;
//                    for (Participant participant : participantQualified) {
//                        participantPairingList.get(participantPairingIndex).addParticipant(participant);
//                        if (participantPairingSeatsMissingQuantity > 0 && participantPairingList.get(participantPairingIndex).participantSeats.size() >= participantPairingSeatSizeMininum) {
//                            if (participantPairingSeatsMissingQuantity > 0)
//                                participantPairingSeatsMissingQuantity--;
//                            moveToNextPairing = true;
//                        } else if (participantPairingList.get(participantPairingIndex).participantSeats.size() == competitionRound.competitionGroup.competitionInstance.competitionCreationParam.playVersusType.numberOfTeam) {
//                            moveToNextPairing = true;
//                        }
//                        if (moveToNextPairing) {
//                            participantPairingIndex = (participantPairingIndex + 1) % participantPairingList.size();
//                        }
//                    }
//                }
//                break;
//            }
//        }
//        participantPairings.addAll(participantPairingList);
//        return participantPairings;
//    }

    public void doPairing(CompetitionSeed competitionSeed) throws CompetitionInstanceGeneratorException {
        //      competitionGroupSeed.computeQuantityPairing();

        //    boolean participantIsSet = false;
        boolean ladder = false;
        CompetitionGroup[] competitionGroupsArray = null;
        int competitionGroupsSize = 0;
        if (competitionSeed.competitionGroups != null && !competitionSeed.competitionGroups.isEmpty()) {
            competitionGroupsArray = new CompetitionGroup[competitionSeed.competitionGroups.size()];
            Sets.sort(competitionSeed.getCompetitionGroups()).toArray(competitionGroupsArray);
            competitionGroupsSize = competitionGroupsArray.length;

        }
        if (competitionSeed.competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(competitionSeed.competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            ladder = true;
        }

        if (!competitionSeed.isParticipantPairingDefined() || ladder) {
            if (competitionSeed.getCompetitionGroups() != null) {
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
//                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    competitionGroup.clearParticipantPairings();
                }
            }
            ArrayList<ParticipantResult> participantResultsSelected = new ArrayList<>();
            List<ParticipantResult> allPreviousParticipantResults = competitionSeed.getPreviousParticipantResults();

//            ParticipantResult[] participantResults =            new ParticipantResult[allPreviousParticipantResults.size()];
//            allPreviousParticipantResults.toArray(participantResults);

            participantResultsSelected.addAll(competitionSeed.participantFilteringMethod.filterParticipantResults(allPreviousParticipantResults, competitionSeed.filteringValue, competitionSeed.filteringUnit));
            Collections.sort(participantResultsSelected);
            //      int competitionGroupParticipantPairingQuantity = participantResultArrayList.size() / competitionGroupSeed.competitionInstance.numberOfParticipantMatch;
            CompetitionInstance competitionInstance = competitionSeed.getCompetitionInstance();
            boolean elimination = competitionGroupsSize > 0 && competitionGroupsArray[0].competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0;

            boolean previousSeedIsMixing = false;
            Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.getCompetitionPhase().competitionCreationParamPhase;
//            if (competitionCreationParamPhase == null)
//                competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
            int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
            int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
            if (!competitionSeed.isPreviousCompetitionGroupSeedEmpty()) {
                if (competitionSeed.competitionPhase.previousCompetitionPhases != null)
                    for (CompetitionPhase competitionPhase : competitionSeed.competitionPhase.previousCompetitionPhases) {
                        if (competitionPhase.competitionCreationParamPhase instanceof CompetitionCreationParamPhaseMixing)
                            previousSeedIsMixing = true;
                    }
                if (previousSeedIsMixing) {
                    Map<Participant, List<Participant>> participantOpponentsGroupMap = null;
                    for (CompetitionGroupResult competitionGroupResult : competitionSeed.getCompetitionGroupResultsPrevious()) {
                        participantOpponentsGroupMap = competitionGroupResult.getCompetitionGroup().getParticipantOpponentsMap();
                    }
                    if (participantOpponentsGroupMap != null) {
                        for (Participant participant : participantOpponentsGroupMap.keySet()) {
                            if (!participantOpponentsMap.containsKey(participant))
                                participantOpponentsMap.put(participant, new ArrayList<>());
                            participantOpponentsMap.get(participant).addAll(participantOpponentsGroupMap.get(participant));
                        }
                    }
                }
            }


//            int participantVoidNeeded = (numberOfParticipantMatch - (participantResultsSelected.size() % numberOfParticipantMatch)) % numberOfParticipantMatch;
//            if (elimination) {
//                boolean strict = true;
//                participantVoidNeeded = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(numberOfParticipantMatch, participantQualifiedPerMatch, participantResultsSelected.size(), strict);
//                participantVoidNeeded = participantVoidNeeded - participantResultsSelected.size();
//            }


            switch (this) {
                case RANDOM: {
                    if (competitionSeed.competitionGroups != null && !competitionSeed.competitionGroups.isEmpty() && competitionSeed.competitionGroups.first().competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) {
                        Collections.sort(participantResultsSelected);
                    } else {
                        Collections.shuffle(participantResultsSelected);
                    }

                    for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                        if (competitionGroup.lane == 1 || competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {

                            if (ladder) {
                                while ((!competitionGroup.isFull() && (competitionCreationParamPhase.registrationOnTheFly == null || !competitionCreationParamPhase.registrationOnTheFly)) || (competitionCreationParamPhase.registrationOnTheFly != null && competitionCreationParamPhase.registrationOnTheFly && !participantResultsSelected.isEmpty())) {
                                    Participant participant = getBestMatchFor(competitionGroup.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                    if (participant != null) {
                                        if (!competitionGroup.isForParticipant(participant)) {
                                            ParticipantSeat currentParticipantSeat = competitionGroup.createParticipantSeat(participant);
                                        }
                                        for (ParticipantResult participantResult : participantResultsSelected) {
                                            if (participantResult.participant.compareTo(participant) == 0) {
                                                participantResultsSelected.remove(participantResult);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                CompetitionRound competitionRound = competitionGroup.getCompetitionRoundForRound(1);
                                for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                                    ParticipantPairing currentParticipantPairing = competitionInstance.createParticipantPairing(competitionMatch.participantQuantity);
                                    while (!currentParticipantPairing.isFull() && !participantResultsSelected.isEmpty()) {
                                        Participant participant = getBestMatchFor(currentParticipantPairing.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                        currentParticipantPairing.addParticipant(participant);
                                        for (ParticipantResult participantResultSelected : participantResultsSelected) {
                                            if (participantResultSelected.participant.compareTo(participant) == 0) {
                                                participantResultsSelected.remove(participantResultSelected);
                                                break;
                                            }
                                        }
                                    }
                                    currentParticipantPairing.setCompetitionMatch(competitionMatch);
                                    currentParticipantPairing.setCompetitionRound(competitionRound);
                                    currentParticipantPairing.setCompetitionGroup(competitionGroup);
                                    currentParticipantPairing.setCompetitionSeed(competitionSeed);
                                }
                            }
                        }
                    }
                }
                break;
                case BASED_ON_PREVIOUS_RESULT: {

                    while (!participantResultsSelected.isEmpty()) {
                        for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                            if (competitionGroup.lane == 1 || competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {

                                if (ladder) {
                                    while ((!competitionGroup.isFull() && (competitionCreationParamPhase.registrationOnTheFly == null || !competitionCreationParamPhase.registrationOnTheFly)) || (competitionCreationParamPhase.registrationOnTheFly != null && competitionCreationParamPhase.registrationOnTheFly && !participantResultsSelected.isEmpty())) {
                                        Participant participant = getBestMatchFor(competitionGroup.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                        if (participant != null) {
                                            if (!competitionGroup.isForParticipant(participant)) {
                                                ParticipantSeat currentParticipantSeat = competitionGroup.createParticipantSeat(participant);
                                            }
                                            for (ParticipantResult participantResult : participantResultsSelected) {
                                                if (participantResult.participant.compareTo(participant) == 0) {
                                                    participantResultsSelected.remove(participantResult);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    CompetitionRound competitionRound = competitionGroup.getCompetitionRoundForRound(1);
                                    for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                                        ParticipantPairing currentParticipantPairing = competitionMatch.participantPairing;
                                        if (currentParticipantPairing == null) {
                                            currentParticipantPairing = competitionInstance.createParticipantPairing(competitionMatch.participantQuantity);
                                            currentParticipantPairing.setCompetitionMatch(competitionMatch);
                                            currentParticipantPairing.setCompetitionRound(competitionRound);
                                            currentParticipantPairing.setCompetitionGroup(competitionGroup);
                                            currentParticipantPairing.setCompetitionSeed(competitionSeed);
                                        }
                                        while (!currentParticipantPairing.isFull()) {
                                            Participant participant = getBestMatchFor(currentParticipantPairing.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                            currentParticipantPairing.addParticipant(participant);
                                            for (ParticipantResult participantResultSelected : participantResultsSelected) {
                                                if (participantResultSelected.participant.compareTo(participant) == 0) {
                                                    participantResultsSelected.remove(participantResultSelected);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (participantResultsSelected.isEmpty())
                                break;
                        }
                    }
                }
                break;
                case TOP_VS_MIDDLE: {


                    List<ParticipantPairing> participantPairings = new ArrayList<>();
                    Map<CompetitionGroup, List<ParticipantPairing>> competitionGroupListParticipantPairingsMap = new HashMap<>();
                    for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                        if (competitionGroup.lane == 1 || !elimination) {

                            if (ladder) {
                                while ((!competitionGroup.isFull() && (competitionCreationParamPhase.registrationOnTheFly == null || !competitionCreationParamPhase.registrationOnTheFly)) || (competitionCreationParamPhase.registrationOnTheFly != null && competitionCreationParamPhase.registrationOnTheFly && !participantResultsSelected.isEmpty())) {
//                                    if(competitionSeed.competitionGroups.size() == 1){}else {
                                    Participant participant = getBestMatchFor(competitionGroup.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                    if (participant != null) {
                                        if (!competitionGroup.isForParticipant(participant)) {
                                            ParticipantSeat currentParticipantSeat = competitionGroup.createParticipantSeat(participant);
                                        }
                                        for (ParticipantResult participantResult : participantResultsSelected) {
                                            if (participantResult.participant.compareTo(participant) == 0) {
                                                participantResultsSelected.remove(participantResult);
                                                break;
                                            }
                                        }
                                    }
//                                    }
                                }
                            } else {
                                CompetitionRound competitionRound = competitionGroup.getCompetitionRoundForRound(1);
                                for (CompetitionMatch competitionMatch : competitionRound.getCompetitionMatches()) {
                                    ParticipantPairing currentParticipantPairing = competitionMatch.participantPairing;
                                    if (currentParticipantPairing == null) {
                                        currentParticipantPairing = competitionInstance.createParticipantPairing(competitionMatch.participantQuantity);
                                        currentParticipantPairing.setCompetitionMatch(competitionMatch);
                                        currentParticipantPairing.setCompetitionRound(competitionRound);
                                        currentParticipantPairing.setCompetitionGroup(competitionGroup);
                                        currentParticipantPairing.setCompetitionSeed(competitionSeed);
                                        participantPairings.add(currentParticipantPairing);
                                        if (!competitionGroupListParticipantPairingsMap.containsKey(competitionGroup))
                                            competitionGroupListParticipantPairingsMap.put(competitionGroup, new ArrayList<>());
                                        competitionGroupListParticipantPairingsMap.get(competitionGroup).add(currentParticipantPairing);
                                    }
                                }
                            }
                        }
                    }

                    if (!ladder) {
                        if (elimination || competitionSeed.competitionGroups.size() == 1) {
                            int index = 1;
                            List<ParticipantPairing> participantPairingsTemp = new ArrayList<>(participantPairings);
                            List<ParticipantPairing> participantPairingsReordered = new ArrayList<>();

                            while (!participantPairingsTemp.isEmpty()) {
                                if (index > 1) {
                                    List<List<ParticipantPairing>> participantPairingsSubList = new ArrayList<>();
                                    int initialSize = participantPairingsTemp.size();
                                    int size = initialSize / (numberOfParticipantMatch / participantQualifiedPerMatch);
                                    if (size <= 1)
                                        size = initialSize;
                                    while (!participantPairingsTemp.isEmpty()) {
                                        if (size > participantPairingsTemp.size())
                                            size = participantPairingsTemp.size();
                                        List<ParticipantPairing> participantPairingsTempSub = participantPairingsTemp.subList(0, size);
                                        participantPairingsSubList.add(new ArrayList<>(participantPairingsTempSub));
                                        participantPairingsTemp.removeAll(participantPairingsTempSub);
                                    }

                                    List<List<ParticipantPairing>> participantPairingsSubListReversed = new ArrayList<>();
                                    while (!participantPairingsSubList.isEmpty()) {
                                        List<ParticipantPairing> participantPairingsSub = participantPairingsSubList.get(0);
                                        participantPairingsSubList.remove(participantPairingsSub);
                                        List<ParticipantPairing> participantPairingsSubReversed = new ArrayList<>();
                                        while (!participantPairingsSub.isEmpty()) {
                                            participantPairingsSubReversed.add(participantPairingsSub.get(participantPairingsSub.size() - 1));
                                            participantPairingsSub.remove(participantPairingsSub.size() - 1);
                                        }

                                        participantPairingsSubListReversed.add(participantPairingsSubReversed);
                                    }

                                    for (List<ParticipantPairing> participantPairingsSub : participantPairingsSubListReversed) {
                                        participantPairingsTemp.addAll(participantPairingsSub);
                                    }
                                }
                                participantPairingsReordered.add(participantPairingsTemp.get(0));
                                participantPairingsTemp.remove(0);
                                if (!participantPairingsTemp.isEmpty()) {
                                    Collections.reverse(participantPairingsTemp);
                                    participantPairingsReordered.add(participantPairingsTemp.get(0));
                                    participantPairingsTemp.remove(0);
                                }
                                index++;

                            }
                            int maxTry = participantPairingsReordered.size() * competitionSeed.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch * 2;
                            int tryCount = 0;
                            while (!participantResultsSelected.isEmpty() && tryCount < maxTry) {
                                tryCount++;
                                for (ParticipantPairing participantPairing : participantPairingsReordered) {
                                    if (!participantPairing.isFull()) {
//                                    participantPairing.addParticipant(participantResultsSelected.get(0).participant);
//                                    participantResultsSelected.remove(0);

                                        Participant participant = getBestMatchFor(participantPairing.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                        participantPairing.addParticipant(participant);
                                        for (ParticipantResult participantResultSelected : participantResultsSelected) {
                                            if (participantResultSelected.participant.compareTo(participant) == 0) {
                                                participantResultsSelected.remove(participantResultSelected);
                                                break;
                                            }
                                        }
                                        participantPairings.remove(participantPairing);
                                        participantPairings.add(participantPairing);
                                    }
                                    if (participantResultsSelected.isEmpty())
                                        break;
                                }
                                if (participantResultsSelected.isEmpty())
                                    break;
                            }
                            if ((!participantResultsSelected.isEmpty() && tryCount >= maxTry)) {
                                throw new CompetitionInstanceGeneratorException();
                            }
                        } else {
                            while (!participantResultsSelected.isEmpty()) {
//                            boolean allInitialized = true;
                                for (CompetitionGroup competitionGroup : competitionGroupListParticipantPairingsMap.keySet()) {
                                    for (ParticipantPairing participantPairing : competitionGroupListParticipantPairingsMap.get(competitionGroup)) {
                                        if (!participantPairing.isFull()) {
//                                        allInitialized = false;
//                                        participantPairing.addParticipant(participantResultsSelected.get(0).participant);
//                                        participantResultsSelected.remove(0);
                                            Participant participant = getBestMatchFor(participantPairing.getRealParticipantsAsArray(), participantResultsSelected, participantOpponentsMap, competitionSeed);
                                            participantPairing.addParticipant(participant);
                                            for (ParticipantResult participantResultSelected : participantResultsSelected) {
                                                if (participantResultSelected.participant.compareTo(participant) == 0) {
                                                    participantResultsSelected.remove(participantResultSelected);
                                                    break;
                                                }
                                            }
                                            competitionGroupListParticipantPairingsMap.get(competitionGroup).remove(participantPairing);
                                            competitionGroupListParticipantPairingsMap.get(competitionGroup).add(participantPairing);
                                            break;
                                        }
                                    }
                                }
//                            if (allInitialized)
//                                participantResultsSelected.clear();
                                if (participantResultsSelected.isEmpty())
                                    break;
                            }
                        }
                    }
                }
                break;

            }
            // competitionGroupSeed.addAllParticipantPairings(participantPairings);
            if (!ladder) {
                competitionSeed.removeParticipantTeamVoid();
                competitionSeed.sortParticipantPairings();
                if (competitionSeed.competitionPhase != null && competitionSeed.competitionPhase.competitionSeeds.first().compareTo(competitionSeed) == 0 && competitionSeed.participantPairings != null) {
                    for (ParticipantPairing participantPairing : competitionSeed.participantPairings) {
                        participantPairing.setCompetitionPhase(competitionSeed.competitionPhase);
                    }
                }
            }
        }
        if (!ladder) {
            if (competitionSeed.isParticipantPairingDefined()) {
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups)
                    competitionGroup.sortParticipantPairings();
            }
        } else {
            SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionGroup.sortParticipantSeats();
//                competitionGroup.open();
            }
        }

    }

    private Participant getBestMatchFor(List<Participant> participantsPairing, ArrayList<ParticipantResult> participantResultsPrevious, Map<Participant, List<Participant>> participantPreviousOpponentsMap, CompetitionSeed competitionSeed) {
        Participant participant = null;
        switch (this) {
            case RANDOM:
            case TOP_VS_MIDDLE:
            case BASED_ON_PREVIOUS_RESULT: {

                if (participantsPairing.isEmpty()) {
                    if (!participantResultsPrevious.isEmpty())
                        participant = participantResultsPrevious.get(0).participant;
                } else {
                    int overlapThreshold = participantResultsPrevious.size();
                    for (ParticipantResult participantResult : participantResultsPrevious) {
                        if (participantPreviousOpponentsMap.containsKey(participantResult.participant)) {
                            List<Participant> participantOpponents = participantPreviousOpponentsMap.get(participantResult.participant);
                            int overlap = 0;
                            for (Participant participantPairing : participantsPairing) {
                                for (Participant participantCurrent : participantPairing.getAllRealParticipantsAsArray()) {
                                    if (participantCurrent.compareTo(participantResult.participant) != 0) {
                                        for (Participant participantOpponent : participantOpponents) {
                                            if (participantCurrent.compareTo(participantOpponent) == 0)
                                                overlap++;

                                        }
                                    }
                                }

                            }
                            if (overlapThreshold == participantResultsPrevious.size() && overlap < overlapThreshold) {
                                overlapThreshold = overlap;
                                participant = participantResult.participant;
                            } else if (overlap < overlapThreshold) {
                                overlapThreshold = overlap;
                                participant = participantResult.participant;
                            }
                        } else if (overlapThreshold > 0) {
                            participant = participantResult.participant;
                            overlapThreshold = 0;
                        }
                    }
                }
            }
        }
        return participant;
    }
}
