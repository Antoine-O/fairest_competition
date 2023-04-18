package com.qc.competition.utils;

import com.github.dakusui.combinatoradix.Combinator;
import com.qc.competition.service.structure.Participant;
import com.qc.competition.service.structure.ParticipantSingle;
import com.qc.competition.service.structure.ParticipantTeamVoid;

import java.util.*;

public class ArraysTools {
    public static SortedMap<Integer, List<List<Participant>>> roundRobin(List<List<List<Participant>>> participantsPerMatchPerRoundPreviouslyDone, List<List<Participant>> participantsPerMatch, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch, Boolean participantTypeSplittable) {
        SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRound = null;

        if (numberOfParticipantPerMatch <= 2 || (participantsPerMatch.size() == 1 && participantsPerMatch.get(0).size() <= 2)) {
            participantsPerMatchPerRound = new TreeMap<>();
            int numberOfParticipantOriginalSize = 0;
            for (List<Participant> participants : participantsPerMatch) {
                numberOfParticipantOriginalSize += participants.size();
            }
            int maxRounds = numberOfParticipantOriginalSize - 1 + numberOfParticipantOriginalSize % 2;
            List<Participant> participants = participantsPerMatchToRibbon(participantsPerMatch, numberOfParticipantPerMatch);
//            participants = participantsRowListToRibbon(participants, numberOfParticipantPerMatch);
//            int stepPerRound = 1;
//            participantsPerMatchPerRound.put(1, participantsPerMatch);
            for (int i = 0; i < maxRounds; i++) {
                if (i > 0) {
                    Participant participantLast = participants.get(participants.size() - 1);
                    participants.remove(participants.size() - 1);
                    participants.add(1, participantLast);
                }
                List<List<Participant>> participantsRowList = participantsRibbonToRowList(participants, numberOfParticipantPerMatch);
                List<List<Participant>> participantsOutput = new ArrayList<>();
                for (List<Participant> participantRow : participantsRowList) {
                    int index = 0;
                    for (Participant participant : participantRow) {
                        if (participantsOutput.size() < index + 1)
                            participantsOutput.add(new ArrayList<>());
                        participantsOutput.get(index).add(participant);
                        index++;
                    }
                }
                participantsPerMatchPerRound.put(i + 1, participantsOutput);
                List<Participant> participantListToRemove = new ArrayList<>();
                for (List<List<Participant>> participantMatchList : participantsPerMatchPerRound.values()) {
                    for (List<Participant> participantList : participantMatchList) {
                        participantListToRemove.clear();
                        for (Participant participant : participantList) {
                            if (participant instanceof ParticipantTeamVoid)
                                participantListToRemove.add(participant);

                        }
                        participantList.removeAll(participantListToRemove);

                    }

                }
            }

        } else {
            List<Participant> participants = participantsPerMatchToList(participantsPerMatch);
            participantsPerMatchPerRound = getParticipantsCombinations(participantsPerMatchPerRoundPreviouslyDone, participants, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);
        }
        // sort by number of participants
        participantsPerMatchPerRound = sortValuesValuesBySize(participantsPerMatchPerRound);

        return participantsPerMatchPerRound;
    }

    public static SortedMap<Integer, List<List<Participant>>> sortValuesValuesBySize(SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRound) {
        SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRoundSorted = new TreeMap<>();
        for (Integer key : participantsPerMatchPerRound.keySet()) {
            participantsPerMatchPerRoundSorted.put(key, sortValuesBySize(participantsPerMatchPerRound.get(key)));
        }
        return participantsPerMatchPerRoundSorted;
    }

    public static List<List<Participant>> sortValuesBySize(List<List<Participant>> participantsPerMatch) {
        for (int i = 0; i < participantsPerMatch.size(); i++) {
            List<Participant> participantsI = participantsPerMatch.get(i);
            List<Participant> participantsToRemove = new ArrayList<>();
            for (Participant participantI : participantsI) {
                if (participantI.isVoid()) {
                    participantsToRemove.add(participantI);
                }
            }
            participantsI.removeAll(participantsToRemove);
        }
        for (int i = 0; i < participantsPerMatch.size(); i++) {
            List<Participant> participantsI = participantsPerMatch.get(i);
            for (int j = i + 1; j < participantsPerMatch.size(); j++) {
                List<Participant> participantsJ = participantsPerMatch.get(j);
                if (participantsJ.size() > participantsI.size()) {
                    participantsPerMatch.set(i, participantsJ);
                    participantsPerMatch.set(j, participantsI);
                    participantsI = participantsPerMatch.get(i);
                    participantsJ = participantsPerMatch.get(j);
                }
            }
        }
        return participantsPerMatch;
    }

    public static List<Participant> participantsPerMatchToRibbon(List<List<Participant>> participantsPerMatch, int numberOfParticipantPerMatch) {
        List<Participant> participantList = new ArrayList<>();
        List<List<Participant>> participantListPerRow = new ArrayList<>();
        int indexVoid = 1;
        for (int i = 0; i < numberOfParticipantPerMatch; i++) {
            for (List<Participant> participants : participantsPerMatch) {
                if (participantListPerRow.size() < i + 1) {
                    participantListPerRow.add(new ArrayList<>());
                }
                if (participants.size() > i)
                    participantListPerRow.get(i).add(participants.get(i));
                else {
                    ParticipantTeamVoid participantTeamVoid = new ParticipantTeamVoid();
                    participantTeamVoid.localId = "X" + indexVoid;
                    participantTeamVoid.bibNumber = "X" + indexVoid;
                    participantListPerRow.get(i).add(participantTeamVoid);
                    indexVoid++;
                }
            }
        }
        for (int i = 0; i < numberOfParticipantPerMatch; i++) {
            if (i % 2 != 0) {
                List<Participant> participantsReversed = new ArrayList<>(participantListPerRow.get(i));
                Collections.reverse(participantsReversed);
                participantList.addAll(participantsReversed);
            } else {
                participantList.addAll(participantListPerRow.get(i));
            }
        }
        return participantList;
    }

    public static List<List<Participant>> participantsListToPerMatch(List<Participant> participants, int numberOfParticipantPerMatch) {
        List<List<Participant>> participantList = new ArrayList<>();
        List<Participant> participantsPerMatch = new ArrayList<>();
        int index = 0;
        for (Participant participant : participants) {
            if (index % numberOfParticipantPerMatch == 0) {
                if (!participantsPerMatch.isEmpty())
                    participantList.add(participantsPerMatch);
                participantsPerMatch = new ArrayList<>();
            }
            participantsPerMatch.add(participant);
            index++;
        }
        if (!participantsPerMatch.isEmpty())
            participantList.add(participantsPerMatch);
        return participantList;
    }

    public static List<Participant> participantsPerMatchToList(List<List<Participant>> participantsPerMatch) {
        List<Participant> participantList = new ArrayList<>();
        for (List<Participant> participants : participantsPerMatch) {
            participantList.addAll(participants);
        }
        return participantList;
    }

    public static int getStepPerRound(int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        return 1;
//        return numberOfParticipantQualifiedPerMatch > 0 ? numberOfParticipantQualifiedPerMatch :1;
    }

    public static List<List<Participant>> participantsRibbonToRowList(List<Participant> participants, int numberOfParticipantPerMatch) {
        List<List<Participant>> rowList = new ArrayList<>();
        int index = 0;
        Integer row = 0;
        int maxRow = 0;
        Map<Integer, List<Participant>> participantsRibbonRow = new HashMap<>();
        for (Participant participant : participants) {
            row = index / (participants.size() / numberOfParticipantPerMatch);
            maxRow = Math.max(maxRow, row);
            if (!participantsRibbonRow.containsKey(row)) {
                participantsRibbonRow.put(row, new ArrayList<>());
            }
            participantsRibbonRow.get(row).add(participant);
            index++;
        }

        for (int i = 0; i < maxRow + 1; i++) {
            boolean reverse = i % 2 == 1;
            if (reverse) {
                List<Participant> participantList = new ArrayList<>();
                participantList.addAll(participantsRibbonRow.get(i));
                Collections.reverse(participantList);
                rowList.add(participantList);
            } else {
                rowList.add(participantsRibbonRow.get(i));
            }
        }
        return rowList;
    }

    public static List<Participant> participantsRowListToRibbon(List<Participant> participants, int numberOfParticipantPerMatch) {
        List<Participant> participantsRibbon = new ArrayList<>();
        int index = 0;
        Integer row = 0;
        int maxRow = 0;
        SortedMap<Integer, List<Participant>> participantsRibbonRow = new TreeMap<>();
        for (Participant participant : participants) {
            row = index / (participants.size() / numberOfParticipantPerMatch);
            maxRow = Math.max(maxRow, row);
            if (!participantsRibbonRow.containsKey(row)) {
                participantsRibbonRow.put(row, new ArrayList<>());
            }
            participantsRibbonRow.get(row).add(participant);
            index++;
        }
        for (int i = 0; i < maxRow + 1; i++) {
            boolean reverse = i % 2 == 1;
            if (reverse) {
                List<Participant> participantList = new ArrayList<>();
                participantList.addAll(participantsRibbonRow.get(i));
                Collections.reverse(participantList);
                participantsRibbon.addAll(participantList);
            } else {
                participantsRibbon.addAll(participantsRibbonRow.get(i));
            }
        }
        return participantsRibbon;
    }


//    public static int getParticipantsCombinationRounds(int participantSize, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
////        List<Participant> participants = new ArrayList<>();
//        int numberOrRounds = 0;
//        if (numberOfParticipantPerMatch == 2) {
//            numberOrRounds = (participantSize + participantSize % numberOfParticipantPerMatch) - 1;
//        } else {
//            List<List<Participant>> participantsPerMatch = new ArrayList<>();
//            for (int i = 1; i < participantSize + 1; i++) {
//                if (participantsPerMatch.isEmpty() || participantsPerMatch.get(participantsPerMatch.size() - 1).size() == numberOfParticipantPerMatch)
//                    participantsPerMatch.add(new ArrayList<>());
//                Participant participant = new ParticipantSingle();
//                participant.localId = "" + i;
//                participant.id = "" + i;
//                participant.bibNumber = "" + i;
//                participantsPerMatch.get(participantsPerMatch.size() - 1).add(participant);
//            }
//
//
//            numberOrRounds = 1;
//            while (!roundRobin(participantsPerMatch, numberOrRounds, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch).isEmpty()) {
//                numberOrRounds++;
//            }
//            numberOrRounds--;
//
//        }
//        return numberOrRounds;
//    }


//    public static int getParticipantsCombinationRounds(int participantSize, int numberOfParticipantPerMatch) {
//        List<Integer> participants = new ArrayList<>();
//        for (int i = 1; i < participantSize + 1; i++) {
//            participants.add(i);
//        }
//        int participantsFakeSize = (numberOfParticipantPerMatch - participantSize % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;
////        for (int i = 0; i < participantsFakeSize; i++) {
////            participants.add(Integer.MAX_VALUE);
////        }
//        if (numberOfParticipantPerMatch > participantSize)
//            numberOfParticipantPerMatch = participantSize;
//        Permutator<Integer> permutator = new Permutator<>(participants, numberOfParticipantPerMatch);
//        List<List<Integer>> combinations = new ArrayList<>();
//        int numberOrRounds = 0;
//        for (List<Integer> combination : permutator) {
//            boolean valid = true;
////            if (participantsFakeSize > 0) {
////                int countFake = 0;
////                for (Integer combinationValue : combination) {
////                    if (combinationValue.compareTo(Integer.MAX_VALUE) == 0)
////                        countFake++;
////                }
////                valid = countFake == 0;
////            }
//            if (valid && !integerCombinationAlreadyInIntegersCombinations(combinations, combination)) {
//                Collections.sort(combination);
//                if (combinations.isEmpty() || combination.get(0).compareTo(participants.get(0)) == 0 || (numberOfParticipantPerMatch > 2 && participantsFakeSize > 0 && combination.get(0).compareTo(participants.get(1)) == 0)) {
//                    numberOrRounds++;
//                    combinations.add(combination);
//                } else
//                    break;
//            }
//        }
//        return numberOrRounds;
//    }

    private static boolean integerCombinationAlreadyInIntegersCombinations(List<List<Integer>> integersCombinations, List<Integer> integersCombination) {
        boolean alreadyIn = false;
        for (List<Integer> integersCombinationCurrent : integersCombinations) {
            if (compareCombinations(integersCombinationCurrent, integersCombination, true) == 0) {
                alreadyIn = true;
                break;
            }
        }
        return alreadyIn;
    }

    public static Integer getParticipantsPerMatch(int participantQuantity, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        int missingParticipants = (numberOfParticipantPerMatch - participantQuantity % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;
        int numberOfMatch = (int) Math.ceil((double) (participantQuantity + missingParticipants) / (double) numberOfParticipantPerMatch);
        int participantQuantityTemp = participantQuantity;
        Map<Integer, Integer> participantQuantities = new HashMap<>();
        while (participantQuantityTemp > 0 || missingParticipants > 0) {
            for (int i = 0; i < numberOfMatch; i++) {
                if (missingParticipants > 0) {
                    missingParticipants--;
                } else if (participantQuantityTemp > 0) {
                    if (!participantQuantities.containsKey(i))
                        participantQuantities.put(i, 0);
                    participantQuantities.put(i, participantQuantities.get(i) + 1);
                    participantQuantityTemp--;
                }
            }
        }
        Integer numberOfParticipantPerMatchResult = 0;
        for (Integer numberOfParticipantPerMatchResultEntry : participantQuantities.values()) {
            if (numberOfParticipantPerMatchResult.compareTo(numberOfParticipantPerMatchResultEntry) < 0)
                numberOfParticipantPerMatchResult = numberOfParticipantPerMatchResultEntry;
        }
        return numberOfParticipantPerMatchResult;
    }

    public static SortedMap<Integer, List<List<Participant>>> getParticipantsCombinations(int participantQuantity, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch, Boolean participantTypeSplittable) {


//        int numberOfParticipantPerMatchCorrected = numberOfParticipantPerMatch;
//        if (participantTypeSplittable != null && participantTypeSplittable)
//            numberOfParticipantPerMatchCorrected = getParticipantsPerMatch(participantQuantity, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

        List<Participant> participants = new ArrayList<>();
        for (int i = 0; i < participantQuantity; i++) {
            ParticipantSingle participant = new ParticipantSingle();
            participant.localId = "" + (i + 1);
            participant.id = "" + (i + 1);
            participants.add(participant);
        }
        SortedMap<Integer, List<List<Participant>>> participantsCombinations = getParticipantsCombinations(null, participants, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);
        return participantsCombinations;
    }

    public static SortedMap<Integer, List<List<Participant>>> getParticipantsCombinations(List<List<List<Participant>>> participantsPerMatchPerRoundPreviouslyDone, List<Participant> participants, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch, Boolean participantTypeSplittable) {

        int numberOfParticipantPerMatchCorrected = numberOfParticipantPerMatch;
        if (participantTypeSplittable != null && participantTypeSplittable)
            numberOfParticipantPerMatchCorrected = getParticipantsPerMatch(participants.size(), numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);
        if (numberOfParticipantPerMatchCorrected < numberOfParticipantPerMatch)
            numberOfParticipantPerMatch = numberOfParticipantPerMatchCorrected;

        SortedMap<Integer, List<List<Participant>>> roundsMatchesParticipants = new TreeMap<>();
//        List<Participant> participantVoidToRemove = new ArrayList<>();
        List<Participant> participantsOriginal = new ArrayList<>(participants);
        int numberOfParticipants = participants.size();
        Integer sizeMax = null;
        SortedMap<Integer, Integer> participantMatchParticipantSizeMap = new TreeMap<>();
        List<Integer> maxParticipantVoidPerMatch = new ArrayList<>();
        boolean specialSmallSize = false;
        List<Participant> participantsVoid = new ArrayList<>();
        if (numberOfParticipantPerMatch < numberOfParticipants && ((participantTypeSplittable && numberOfParticipants < (numberOfParticipantQualifiedPerMatch + 1) * 2) || (!participantTypeSplittable && numberOfParticipants < numberOfParticipantPerMatch * 2 - 1))) {
            specialSmallSize = true;
            sizeMax = numberOfParticipantPerMatch;
            participantMatchParticipantSizeMap.put(participantMatchParticipantSizeMap.size(), numberOfParticipantPerMatch);
            participantMatchParticipantSizeMap.put(participantMatchParticipantSizeMap.size(), numberOfParticipantPerMatch);
            for (int i = 0; i < (numberOfParticipantPerMatch * 2) - numberOfParticipants; i++) {
                ParticipantTeamVoid participantTeamVoid = new ParticipantTeamVoid();
                participantTeamVoid.localId = "X" + (i + 1);
                participantTeamVoid.id = "X" + (i + 1);
                participants.add(participantTeamVoid);
                participantsVoid.add(participantTeamVoid);
            }
            maxParticipantVoidPerMatch.add(0);
            maxParticipantVoidPerMatch.add((numberOfParticipantPerMatch * 2) - numberOfParticipants);
        } else {
//        boolean avoidBye = numberOfParticipantPerMatch > 2 && numberOfParticipants > 2 * (numberOfParticipantQualifiedPerMatch + 1);

            int missingParticipantSize = (numberOfParticipantPerMatch - numberOfParticipants % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;

//        for (Participant participant : participants) {
//            if (participant instanceof ParticipantTeamVoid)
//                participantVoidToRemove.add(participant);
//        }
//        participants.removeAll(participantVoidToRemove);

/*        if (avoidBye) {

        }*/
            List<List<Participant>> participantsCombinationsFiltered = new ArrayList<>();
            if (numberOfParticipantPerMatch > 2) {
                int numberOfMatch = (participants.size() + missingParticipantSize) / numberOfParticipantPerMatch;
                for (int i = 0; i < numberOfMatch; i++) {
                    participantMatchParticipantSizeMap.put(i, 0);
                }
                int index = 0;
                int participantRemaining = numberOfParticipants;
                int participantMissingRemaining = missingParticipantSize;
                while (participantMissingRemaining > 0 || participantRemaining > 0) {
                    if (participantTypeSplittable) {
                        if (participantMissingRemaining > 0) {
                            participantMissingRemaining--;
                        } else if (participantRemaining > 0) {
                            participantRemaining--;
                            participantMatchParticipantSizeMap.put(index % numberOfMatch, participantMatchParticipantSizeMap.get(index % numberOfMatch) + 1);
                        }
                        index++;
                    } else {
                        if (participantRemaining > 0) {
                            participantRemaining--;
                            participantMatchParticipantSizeMap.put(index, participantMatchParticipantSizeMap.get(index) + 1);
                        } else {
                            participantMissingRemaining--;
                        }
                        if (participantMatchParticipantSizeMap.get(index).compareTo(numberOfParticipantPerMatch) == 0) {
                            index++;
                        }
                    }
                }


                Set<Integer> sizes = new HashSet<>(participantMatchParticipantSizeMap.values());

                Integer sizeMin = null;
                if (sizes.size() > 1) {
                    for (Integer size : sizes) {
                        if (sizeMax == null || sizeMax.compareTo(size) < 0)
                            sizeMax = size;
                        if (sizeMin == null || sizeMin.compareTo(size) > 0)
                            sizeMin = size;
                    }
                    numberOfParticipantPerMatch = sizeMax;

                } else {
                    sizeMax = sizes.iterator().next();
                    sizeMin = sizeMax;
                    numberOfParticipantPerMatch = sizeMax;
                    missingParticipantSize += (numberOfParticipantPerMatch * numberOfMatch);
                }

                for (Integer matchIndex : participantMatchParticipantSizeMap.keySet()) {
                    maxParticipantVoidPerMatch.add(sizeMax - participantMatchParticipantSizeMap.get(matchIndex));

                }

                participantMatchParticipantSizeMap.clear();
                for (int i = 0; i < numberOfMatch; i++) {
                    participantMatchParticipantSizeMap.put(i, sizeMax);
                }


                missingParticipantSize = 0;
                for (Integer maxParticipantVoidPerMatchValue :
                        maxParticipantVoidPerMatch) {
                    missingParticipantSize += maxParticipantVoidPerMatchValue;
                }

                for (int i = 0; i < missingParticipantSize; i++) {
                    ParticipantTeamVoid participantTeamVoid = new ParticipantTeamVoid();
                    participantTeamVoid.localId = "X" + (i + 1);
                    participantTeamVoid.id = "X" + (i + 1);
                    participants.add(participantTeamVoid);
                    participantsVoid.add(participantTeamVoid);

                }


            } else {
                sizeMax = 2;
            }
        }
        List<List<Participant>> participantsCombinations = new ArrayList<>();
        List<List<Participant>> participantsCombinationsForbidden = new ArrayList<>();

        if (sizeMax <= 4 || participants.size() / sizeMax <= 2) {
            if (participants.size() > sizeMax) {
                Combinator<Participant> combinator = new Combinator<>(participants, sizeMax);
                for (List<Participant> participantsCombination : combinator) {
                    if (maxParticipantVoidPerMatch.contains(numberOfParticipantPerMatch - getRealSize(participantsCombination))) {
                        participantsCombinations.add(participantsCombination);
                    } else {
                        participantsCombinationsForbidden.add(participantsCombination);
                    }
                }
            } else {
                participantsCombinations.add(participants);
            }
        } else {
            List<Participant> participantsPart1 = participants.subList(0, 2 * participants.size() / 3);
            List<Participant> participantsPart2 = participants.subList(participants.size() / 3, participants.size());
            Combinator<Participant> combinator1 = new Combinator<>(participantsPart1, sizeMax / 2);
            Combinator<Participant> combinator2 = new Combinator<>(participantsPart2, sizeMax / 2 + sizeMax % 2);
            int indexStart = 1;
            for (long i = 0; i < combinator1.size(); i++) {
                List<Participant> participantsCombination = combinator1.get(i);
                for (long j = combinator2.size() - indexStart; j >= 0; j--) {
                    List<Participant> participantsCombination2 = combinator2.get(j);
                    Set<Participant> participantsTemp = new HashSet<>();
                    participantsTemp.addAll(participantsCombination);
                    participantsTemp.addAll(participantsCombination2);
                    List<Participant> participantList = new ArrayList<>(participantsTemp);
                    if (maxParticipantVoidPerMatch.contains(numberOfParticipantPerMatch - getRealSize(participantList))) {
                        participantsCombinations.add(participantList);
                        indexStart++;
                        break;
                    }
                }
                if (combinator2.size() - indexStart == 0)
                    break;

            }
        }

//            Map<Integer, List<Participant>> participantMatchMapUnSorted = new HashMap<>();
//            for (Integer round : participantMatchParticipantSizeMap.keySet()) {
//                participantMatchMapUnSorted.put(round, new Participant[participantMatchParticipantSizeMap.get(round)]);
//            }


//            List<List<Participant>> participantsCombinationsTemp = new ArrayList<>(participantsCombinations.subList(i, participantsCombinations.size()));
        SortedMap<Participant, List<Participant>> participantsOpponents = new TreeMap<>();
        for (Participant participant : participants) {
            List<Participant> participantList = new ArrayList<>(participants);
            participantList.remove(participant);
            participantsOpponents.put(participant, participantList);
        }

        Integer currentRound = 1;

        List<Participant> participantsNotPlayed = new ArrayList<>();
        for (Participant participant : participants) {
            if (!(participant instanceof ParticipantTeamVoid)) {
                participantsNotPlayed.add(participant);
            }
        }

        if (participantsPerMatchPerRoundPreviouslyDone != null) {
            for (List<List<Participant>> participantPerMatchPreviouslyDone : participantsPerMatchPerRoundPreviouslyDone) {
                roundsMatchesParticipants.put(currentRound, participantPerMatchPreviouslyDone);
                for (List<Participant> participantMatchPreviouslyDone : participantPerMatchPreviouslyDone) {
                    if (participantMatchPreviouslyDone.size() < numberOfParticipantPerMatch) {
                        List<List<Participant>> participantsPerMatchWithParticipantVoidPreviouslyDoneList = getParticipantsCombinationsWithParticipantVoid(participantMatchPreviouslyDone, participantsVoid, numberOfParticipantPerMatch);
                        for (List<Participant> participantsPerMatchWithParticipantVoidPreviouslyDone : participantsPerMatchWithParticipantVoidPreviouslyDoneList) {
                            participantsOpponents = removeParticipantMeet(participantsOpponents, participantsPerMatchWithParticipantVoidPreviouslyDone);
                        }
                        participantsCombinations = removeUsedCombination(participantsCombinations, participantsPerMatchWithParticipantVoidPreviouslyDoneList, true);
                        int realSize = getRealSize(participantMatchPreviouslyDone);
                        if (realSize <= numberOfParticipantQualifiedPerMatch || (!participantTypeSplittable && realSize < numberOfParticipantPerMatch)) {
                            participantsNotPlayed.removeAll(participantMatchPreviouslyDone);
                        }
                    } else {
                        participantsOpponents = removeParticipantMeet(participantsOpponents, participantMatchPreviouslyDone);
                    }
                }
                participantsCombinations = removeUsedCombination(participantsCombinations, participantPerMatchPreviouslyDone, true);
                currentRound++;
            }
        }

        SortedMap<Participant, List<Participant>> participantsOpponentsPrevious = null;
        int tryIndex = 0;
        boolean forceFinal = false;
        while (!(participantsOpponents.isEmpty() && (participantTypeSplittable || participantsNotPlayed.isEmpty()))) {
            boolean finalPhaseNext = false;
            while ((!participantsOpponents.isEmpty() && getRealSize(new ArrayList<>(participantsOpponents.keySet())) > 0) || (!participantTypeSplittable && !participantsNotPlayed.isEmpty())) {
                SortedMap<Integer, List<Participant>> participantMatchMap = new TreeMap<>();
                boolean sameAsPrevious = false;
                boolean finalPhase = false;
                if (finalPhaseNext) {
                    forceFinal = true;
                    finalPhaseNext = false;
                }
                if (getRealSize(new ArrayList<>(participantsOpponents.keySet())) <= numberOfParticipantQualifiedPerMatch && participantTypeSplittable) {
                    forceFinal = true;
                }

                if (!forceFinal) {
                    if (fillWithParticipantsCombinationsWithAvailableOpponent(participantsCombinations, participantMatchMap, participantMatchParticipantSizeMap, cloneParticipantsOpponents(participantsOpponents), participantsOriginal, sizeMax, new ArrayList<>(maxParticipantVoidPerMatch), false)) {
                    } else if (fillWithParticipantsCombinationsWithAvailableOpponent(participantsCombinations, participantMatchMap, participantMatchParticipantSizeMap, cloneParticipantsOpponents(participantsOpponents), participantsOriginal, sizeMax, new ArrayList<>(maxParticipantVoidPerMatch), true)) {
                    } else {
                        List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
                        if (!specialSmallSize) {
                            for (Collection<List<Participant>> participantsMatch : roundsMatchesParticipants.values()) {
                                participantsCombinationsCloned = removeParticipantsCombinationsPartial(participantsCombinationsCloned, (List<List<Participant>>) participantsMatch, sizeMax, numberOfParticipantQualifiedPerMatch);
                            }
                        } else {
                            for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                                participantsCombinations = removeUsedCombination(participantsCombinations, participantsMatches, true);
                            }
                        }
                        if (fillWithParticipantsCombinations(participantMatchMap, participantMatchParticipantSizeMap.size(), null, participantsCombinations, participantsCombinationsCloned, participants, sizeMax, numberOfParticipantQualifiedPerMatch, cloneParticipantsOpponents(participantsOpponents), new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, false)) {
                        } else if (fillWithParticipantsCombinations(participantMatchMap, participantMatchParticipantSizeMap.size(), null, participantsCombinations, participantsCombinationsCloned, participants, sizeMax, numberOfParticipantQualifiedPerMatch, cloneParticipantsOpponents(participantsOpponents), new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, true)) {
//                        int missingMatch = participantMatchParticipantSizeMap.size() - participantMatchMap.size();
//                        if (missingMatch > 1) {
//                            throw new IllegalArgumentException("!");
//                        }
                        }
                    }
                    for (List<Participant> opponents : participantMatchMap.values()) {
//                        int realSize = getRealSize(opponents);
//                        if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                        participantsOpponents = removeParticipantMeet(participantsOpponents, opponents);
                    }
                    if (!participantsOpponents.isEmpty() && getRealSize(new ArrayList<>(participantsOpponents.keySet())) > 0) {
                        if (participantsOpponentsPrevious != null) {
                            if (participantsOpponentsPrevious.size() == participantsOpponents.size()) {
                                sameAsPrevious = isSame(participantsOpponentsPrevious, participantsOpponents);
                            } else {
                                finalPhaseNext = true;
                            }
                        }
                    }
                }
                if (forceFinal || finalPhase || sameAsPrevious) {
                    forceFinal = false;
                    participantsOpponents = participantsOpponentsPrevious;
                    while (participantsOpponents.size() > getRealSize(new ArrayList<>(participantsOpponents.keySet()))) {
                        for (Participant participant : participantsOpponents.keySet()) {
                            if (participant instanceof ParticipantTeamVoid) {
                                participantsOpponents.remove(participant);
                                List<Participant> participantsToRemove = new ArrayList<>();
                                for (Participant participant2 :
                                        participantsOpponents.keySet()) {
                                    participantsOpponents.get(participant2).remove(participant);
                                    if (participantsOpponents.get(participant2).isEmpty()) {
                                        participantsToRemove.add(participant2);
                                    }
                                }
                                for (Participant participantToRemove : participantsToRemove) {
                                    participantsOpponents.remove(participantToRemove);
                                }


                                break;
                            }
                        }
                    }
                    if (getRealSize(new ArrayList<>(participantsOpponents.keySet())) > 0) {
                        for (Participant participant : participants) {
                            if (!participantsOpponents.containsKey(participant)) {
                                participantsOpponents.put(participant, new ArrayList<>());
                            }
                        }

                        SortedMap<Participant, List<Participant>> participantsOpponentsPreviousCleaned = cleanParticipantsOpponent(participantsOpponentsPrevious);

                        participantMatchMap = new TreeMap<>();
                        boolean goodEnough = true;
                        SortedMap<Participant, List<Participant>> participantsOpponentsCloned = cloneParticipantsOpponents(participantsOpponents);
                        boolean success = fillWithParticipantsCombinationsWithAvailableOpponent(participantsCombinations, participantMatchMap, participantMatchParticipantSizeMap, participantsOpponentsCloned, participantsOriginal, sizeMax, new ArrayList<>(maxParticipantVoidPerMatch), goodEnough);
                        if (success) {
                            for (List<Participant> opponents : participantMatchMap.values()) {
//                                int realSize = getRealSize(opponents);
//                                if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                participantsOpponentsCloned = removeParticipantMeet(participantsOpponentsCloned, opponents);
                            }
                            participantsOpponentsCloned =
                                    cleanParticipantsOpponent(participantsOpponentsCloned);
                        }

                        if (!success || isSame(participantsOpponentsPreviousCleaned, participantsOpponentsCloned)) {
                            participantMatchMap.clear();
                            participantsOpponentsCloned =
                                    cloneParticipantsOpponents(participantsOpponents);
                            List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
                            if (!specialSmallSize) {
                                for (Collection<List<Participant>> participantsMatch : roundsMatchesParticipants.values()) {
                                    participantsCombinationsCloned = removeParticipantsCombinationsPartial(participantsCombinationsCloned, (List<List<Participant>>) participantsMatch, sizeMax, numberOfParticipantQualifiedPerMatch);
                                }
                            } else {
                                for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                                    participantsCombinations = removeUsedCombination(participantsCombinations, participantsMatches, true);
                                }
                            }
                            success = fillWithParticipantsCombinations(participantMatchMap, participantMatchParticipantSizeMap.size(), null, participantsCombinations, participantsCombinationsCloned, participants, sizeMax, numberOfParticipantQualifiedPerMatch, participantsOpponentsCloned, new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, false);
                            if (success) {
                                for (List<Participant> opponents : participantMatchMap.values()) {
//                                    int realSize = getRealSize(opponents);
//                                    if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                    participantsOpponentsCloned = removeParticipantMeet(participantsOpponentsCloned, opponents);
                                }
                                participantsOpponentsCloned =
                                        cleanParticipantsOpponent(participantsOpponentsCloned);
                            }

                        }

                        if (!success || isSame(participantsOpponentsPreviousCleaned, participantsOpponentsCloned)) {
                            participantMatchMap.clear();
                            participantsOpponentsCloned =
                                    cloneParticipantsOpponents(participantsOpponents);
                            List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
                            if (!specialSmallSize) {
                                for (Collection<List<Participant>> participantsMatch : roundsMatchesParticipants.values()) {
                                    participantsCombinationsCloned = removeParticipantsCombinationsPartial(participantsCombinationsCloned, (List<List<Participant>>) participantsMatch, sizeMax, numberOfParticipantQualifiedPerMatch);
                                }
                            } else {
                                for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                                    participantsCombinations = removeUsedCombination(participantsCombinations, participantsMatches, true);
                                }
                            }
                            success = fillWithParticipantsCombinations(participantMatchMap, participantMatchParticipantSizeMap.size(), null, participantsCombinations, participantsCombinationsCloned, participants, sizeMax, numberOfParticipantQualifiedPerMatch, participantsOpponentsCloned, new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, true);
                            if (success) {
                                for (List<Participant> opponents : participantMatchMap.values()) {
//                                    int realSize = getRealSize(opponents);
//                                    if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                    participantsOpponentsCloned = removeParticipantMeet(participantsOpponentsCloned, opponents);
                                }
                                participantsOpponentsCloned =
                                        cleanParticipantsOpponent(participantsOpponentsCloned);
                            }
                        }

                        if (success && !isSame(participantsOpponentsPreviousCleaned, participantsOpponentsCloned)) {
                            for (List<Participant> opponents : participantMatchMap.values()) {
//                                int realSize = getRealSize(opponents);
//                                if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                participantsOpponents = removeParticipantMeet(participantsOpponents, opponents);
                            }
                            if (participantsOpponentsPreviousCleaned.size() != participantsOpponentsCloned.size()) {
                                forceFinal = true;
                            }
                        } else {
                            participantMatchMap.clear();
                        }
                    } else if (getRealSize(new ArrayList<>(participantsOpponents.keySet())) > 0) {
                        Integer maxParticipantVoidMin = null;
                        for (Integer maxParticipantVoid : maxParticipantVoidPerMatch) {
                            if (maxParticipantVoidMin == null || maxParticipantVoidMin.compareTo(maxParticipantVoid) > 0)
                                maxParticipantVoidMin = maxParticipantVoid;
                        }
                        if (maxParticipantVoidMin == null)
                            maxParticipantVoidMin = 0;
                        int indexAddOpponent = 0;
                        while (getRealSize(new ArrayList<>(participantsOpponents.keySet())) < (sizeMax - maxParticipantVoidMin) && indexAddOpponent < participantsOriginal.size()) {
                            if (!participantsOpponents.containsKey(participantsOriginal.get(indexAddOpponent))) {
                                List<Participant> participantList = new ArrayList<>();
                                for (Participant participant : participantsOpponents.keySet()) {
                                    if (!(participant instanceof ParticipantTeamVoid)) {
                                        participantList.add(participant);
                                        break;
                                    }
                                }
                                participantsOpponents.put(participantsOriginal.get(indexAddOpponent), participantList);
                            }
                            indexAddOpponent++;
                        }
                        List<Participant> participantList = new ArrayList<>(participantsOpponents.keySet());
                        participantMatchMap.clear();

                        participantMatchMap.put(participantMatchMap.size(), participantList);
                        participantsOpponents = removeParticipantMeet(participantsOpponents, participantList);


                    } else {
                        participantsOpponents.clear();
                    }
                }
                if (!participantMatchMap.isEmpty()) {
                    boolean okToAdd = false;
                    if (participantMatchMap.size() == participantMatchParticipantSizeMap.size() - 1) {
                        if (!finalPhaseNext) {
                            List<Participant> participantsRemaining = new ArrayList<>(participants);
                            for (List<Participant> participantsInMatch : participantMatchMap.values()) {
//                                int realSize = getRealSize(participantsInMatch);
//                                if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                participantsRemaining.removeAll(participantsInMatch);
                            }
                            participantMatchMap.put(participantMatchMap.size(), participantsRemaining);
                            participantsOpponents = removeParticipantMeet(participantsOpponents, participantsRemaining);
                            okToAdd = true;
                        }
                    } else if (participantsOpponents.isEmpty() && participantMatchMap.size() < participantMatchParticipantSizeMap.size()) {
                        if (!finalPhaseNext) {
                            SortedMap<Participant, List<Participant>> participantsOpponentsCloned = cloneParticipantsOpponents(participantsOpponents);

                            List<Participant> participantsRemaining = new ArrayList<>(participants);
                            for (List<Participant> participantsInMatch : participantMatchMap.values()) {
//                                int realSize = getRealSize(participantsInMatch);
//                                if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                participantsRemaining.removeAll(participantsInMatch);
                            }

                            participantsOpponentsCloned = removeParticipantMeet(participantsOpponentsCloned, participantsRemaining);
                            List<List<Participant>> participantsCombinationsUsed = new ArrayList<>(participantMatchMap.values());
                            List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
                            removeUsedCombination(participantsCombinationsCloned, participantsCombinationsUsed, true);
                            for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                                participantsCombinationsCloned = removeUsedCombination(participantsCombinationsCloned, participantsMatches, true);
                            }
                            for (List<Participant> participantsInMatch : participantMatchMap.values()) {
                                removeCombinationsWithParticipants(participantsCombinationsCloned, participantsInMatch);
                            }


                            SortedMap<Integer, List<Participant>> participantMatchMapRemaining = new TreeMap<>();
                            if (fillWithParticipantsCombinations(participantMatchMapRemaining, participantMatchParticipantSizeMap.size() - participantMatchMap.size(), null, participantsCombinations, participantsCombinationsCloned, participantsRemaining, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantsOpponentsCloned, new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, true)) {
//                                participantMatchMap.clear();
//                                participantMatchMap.putAll(participantMatchMapCloned);
                                for (List<Participant> participantMatchMapRemainingValue : participantMatchMapRemaining.values()) {
                                    participantMatchMap.put(participantMatchMap.size(), participantMatchMapRemainingValue);
                                }
                                okToAdd = true;
                            }
                        }
                    } else {
                        okToAdd = true;
                    }
                    if (okToAdd) {

//                        if (participantsOpponentsPrevious != null && participantsOpponentsPrevious.size() != participantsOpponents.size()) {
//                            finalPhaseNext = true;
//                        }
                        participantsOpponentsPrevious = cloneParticipantsOpponents(participantsOpponents);

                        List<List<Participant>> participantsCombinationsUsed = new ArrayList<>(participantMatchMap.values());
                        roundsMatchesParticipants.put(currentRound, participantsCombinationsUsed);
                        currentRound++;

                        for (List<Participant> participantsValue : participantMatchMap.values()) {
                            int realSize = getRealSize(participantsValue);
                            if ((participantTypeSplittable && realSize > numberOfParticipantQualifiedPerMatch) || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch)) {
                                participantsNotPlayed.removeAll(participantsValue);
                            }
                        }
                        participantsCombinations = removeUsedCombination(participantsCombinations, participantsCombinationsUsed, true);
                    }
                }
            }
            /*Set<Integer> roundsToRemove = new HashSet<>();
            for (Integer round : roundsMatchesParticipants.keySet()) {
                Collection<List<Participant>> participantsMatches = roundsMatchesParticipants.get(round);
                for (List<Participant> participantMatch : participantsMatches) {
                    if (containsCombinationInCombinations(participantsCombinationsForbidden, participantMatch)) {
                        roundsToRemove.add(round);
                        break;
                    }
                }
            }
            if (!roundsToRemove.isEmpty()) {
                List<List<Participant>> participantMatchToRemove = new ArrayList<>();
                for (Integer roundToRemove : roundsToRemove) {
                    participantMatchToRemove.addAll(
                            roundsMatchesParticipants.get(roundToRemove));
                    roundsMatchesParticipants.remove(roundToRemove);
                }
                SortedMap<Integer, List<List<Participant>>> roundsMatchesParticipantsNew = new TreeMap<>();
                Integer newRound = 0;
                for (Integer round : roundsMatchesParticipants.keySet()) {
                    newRound++;
                    roundsMatchesParticipantsNew.put(newRound, roundsMatchesParticipants.get(round));
                }
                currentRound = newRound + 1;

                roundsMatchesParticipants.clear();
                roundsMatchesParticipants.putAll(roundsMatchesParticipantsNew);


                SortedMap<Participant, List<Participant>> participantsOpponentsNew = new TreeMap<>();
                for (Participant participant : participants) {
                    List<Participant> participantList = new ArrayList<>(participants);
                    participantList.remove(participant);
                    participantsOpponentsNew.put(participant, participantList);
                }
                for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                    for (List<Participant> participantMatch : participantsMatches) {
                        int realSize = getRealSize(participantMatch);
                        if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                            participantsOpponentsNew = removeParticipantMeet(participantsOpponentsNew, participantMatch);

                    }
                }
                if (getRealSize(new ArrayList<>(participantsOpponentsNew.keySet())) == 0) {
                    participantsOpponentsNew.clear();
                }
                if (!participantsOpponentsNew.isEmpty()) {
                    List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
                    List<List<Participant>> participantsCombinationsClonedNew = new ArrayList<>();
                    for (List<Participant> participantsCombination : participantsCombinationsCloned) {
//                if (participantsCombination.size() - getRealSize(participantsCombination) <= 1 || participants.size() <= 2 * numberOfParticipantQualifiedPerMatch + 1)
                        if (!participantsCombinationsForbidden.contains(participants) && !participantMatchToRemove.contains(participants))
                            participantsCombinationsClonedNew.add(participantsCombination);
                    }
                    for (Collection<List<Participant>> participantsMatches : roundsMatchesParticipants.values()) {
                        participantsCombinationsClonedNew = removeParticipantsCombinationsPartial(participantsCombinationsClonedNew, (List<List<Participant>>) participantsMatches, sizeMax, numberOfParticipantQualifiedPerMatch);
                    }
                    participantsCombinations.clear();
                    participantsCombinations.addAll(participantsCombinationsClonedNew);
                    participantsOpponents.clear();
                    participantsOpponents.putAll(participantsOpponentsNew);
                    if (participantsOpponentsPrevious != null)
                        participantsOpponentsPrevious.clear();
                    participantsOpponentsPrevious = new TreeMap<>(participantsOpponentsNew);
//                        forceFinal = true;
                }

            }*/
        }

        return roundsMatchesParticipants;
    }

    private static List<List<Participant>> getParticipantsCombinationsWithParticipantVoid(List<Participant> participants, List<Participant> participantsVoid, int numberOfParticipantPerMatch) {
        int participantVoidCombinationSize = numberOfParticipantPerMatch - participants.size();
        List<List<Participant>> participantsCombinationsWithParticipantVoid = new ArrayList<>();
        List<List<Participant>> participantVoidCombinations = new ArrayList<>();
        if (participantVoidCombinationSize > 1) {
            Combinator<Participant> combinator = new Combinator<>(participantsVoid, participantVoidCombinationSize);
            for (List<Participant> participantList : combinator) {
                participantVoidCombinations.add(participantList);
            }
        } else if (participantVoidCombinationSize == 1) {
            for (Participant participant : participants) {
                List<Participant> combination = new ArrayList<>();
                combination.add(participant);
                participantVoidCombinations.add(combination);
            }
        }
        for (List<Participant> participantVoidCombination : participantVoidCombinations) {
            participantVoidCombination.addAll(participants);
            participantsCombinationsWithParticipantVoid.add(participantVoidCombination);
        }
        return participantsCombinationsWithParticipantVoid;
    }

    private static SortedMap<Participant, List<Participant>> cleanParticipantsOpponent(SortedMap<Participant, List<Participant>> participantsOpponents) {
        SortedMap<Participant, List<Participant>> participantsOpponentsCleaned = new TreeMap<>();
        for (Participant participant : participantsOpponents.keySet()) {
            if (!participantsOpponents.get(participant).isEmpty()) {
                participantsOpponentsCleaned.put(participant, participantsOpponents.get(participant));
            }
        }
        return participantsOpponentsCleaned;
    }

    private static boolean isSame(SortedMap<Participant, List<Participant>> participantsOpponentsPrevious, SortedMap<Participant, List<Participant>> participantsOpponents) {
        boolean same = false;
        if (participantsOpponentsPrevious.size() == participantsOpponents.size()) {
            same = true;
            for (Participant participantKey : participantsOpponentsPrevious.keySet()) {
                if (!participantsOpponents.containsKey(participantKey) || participantsOpponents.get(participantKey).size() != participantsOpponentsPrevious.get(participantKey).size()) {
                    same = false;
                    break;
                }
            }
        }
        return same;
    }

    private static boolean getParticipantMatchesFromCombination(int numberOfMatch, List<List<Participant>> participantsCombinations, List<List<Participant>> participantMatchesFromCombination, List<Participant> participantMatchesToAdd) {
        boolean success = true;
        if (participantMatchesToAdd != null) {
            participantMatchesFromCombination.add(participantMatchesToAdd);
        }
        if (participantMatchesFromCombination.size() < numberOfMatch && !participantsCombinations.isEmpty()) {
            List<List<Participant>> participantsCombinationsCloned = cloneParticipantsCombinations(participantsCombinations);
            if (participantMatchesToAdd != null)
                participantsCombinationsCloned = removeCombinationsWithParticipants(participantsCombinationsCloned, participantMatchesToAdd);
            for (List<Participant> participantsCombinationCloned : participantsCombinationsCloned) {
                List<List<Participant>> participantMatchesFromCombinationCloned = cloneParticipantsCombinations(participantMatchesFromCombination);
                success = getParticipantMatchesFromCombination(numberOfMatch, participantsCombinationsCloned, participantMatchesFromCombinationCloned, participantsCombinationCloned);
                if (success) {
                    participantMatchesFromCombination.clear();
                    participantMatchesFromCombination.addAll(participantMatchesFromCombinationCloned);
                    break;
                }
            }
        } else if (participantMatchesFromCombination.size() < numberOfMatch) {
            success = false;
            participantMatchesFromCombination.remove(participantMatchesFromCombination.size() - 1);
        }
        return success;
    }

    private static SortedMap<Participant, List<Participant>> removeParticipantMeet(SortedMap<Participant, List<Participant>> participantsOpponents, List<Participant> opponents) {
//        if (getRealSize(opponents) > numberOfParticipantQualifiedPerMatch) {
        for (Participant opponent : opponents) {
            if (participantsOpponents.containsKey(opponent)) {
                participantsOpponents.get(opponent).removeAll(opponents);
                if (participantsOpponents.get(opponent).isEmpty())
                    participantsOpponents.remove(opponent);
            }
        }
//        } else {
        for (Participant opponent : opponents) {
            if (opponent instanceof ParticipantTeamVoid) {
                if (participantsOpponents.containsKey(opponent)) {
                    participantsOpponents.get(opponent).removeAll(opponents);
                    if (participantsOpponents.get(opponent).isEmpty())
                        participantsOpponents.remove(opponent);
                }
            }
        }
//        }
        return participantsOpponents;

    }

    private static SortedMap<Participant, List<Participant>> removeParticipant(SortedMap<Participant, List<Participant>> participantsOpponents, List<Participant> opponents, boolean cleanVoid) {
        for (Participant opponent : opponents) {
            participantsOpponents.remove(opponent);
        }
        List<Participant> participantToRemoveList = new ArrayList<>();
        for (Participant participant : participantsOpponents.keySet()) {
            List<Participant> participantList = participantsOpponents.get(participant);
            participantList.removeAll(opponents);
            if (cleanVoid && participantList.isEmpty()) {
                participantToRemoveList.add(participant);
            }
        }
        for (Participant participantToRemove : participantToRemoveList) {
            participantsOpponents.remove(participantToRemove);
        }
        return participantsOpponents;

    }


    private static SortedMap<Participant, List<Participant>> removeParticipant(SortedMap<Participant, List<Participant>> participantsOpponents, Participant participant, boolean cleanVoid) {
        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        return removeParticipant(participantsOpponents, participants, cleanVoid);

    }

    private static boolean fillWithParticipantsCombinationsWithAvailableOpponent
            (List<List<Participant>> participantsCombinations, SortedMap<Integer, List<Participant>> participantMatchMap, SortedMap<Integer, Integer> participantMatchParticipantSizeMap,
             SortedMap<Participant, List<Participant>> participantsOpponents, List<Participant> participantsOriginal,
             int numberOfParticipantPerMatch, List<Integer> maxParticipantVoidPerMatch, boolean goodEnough) {

        boolean success = true;
        if (!participantsOpponents.isEmpty() && participantMatchMap.size() < participantMatchParticipantSizeMap.size()) {
            SortedMap<Integer, List<Participant>> participantMatchMapFound = null;
            if (participantsOpponents.size() <= numberOfParticipantPerMatch) {
                success = false;
                participantMatchMapFound = cloneParticipantMatchMap(participantMatchMap);
                List<Participant> participantList = new ArrayList<>(participantsOpponents.keySet());
                if (goodEnough || containsCombinationInCombinations(participantsCombinations, participantList)) {
                    participantMatchMapFound.put(participantMatchMapFound.size(), participantList);
                    success = true;
                }
            } else {
                success = false;
                Integer maxSize = null;
                List<Participant> participants = new ArrayList<>(participantsOpponents.keySet());
                SortedMap<Integer, List<Participant>> sizeMap = new TreeMap<>();
                for (Participant participant : participants) {
                    Integer size = participantsOpponents.get(participant).size();
                    if (!sizeMap.containsKey(-size))
                        sizeMap.put(-size, new ArrayList<>());
                    sizeMap.get(-size).add(participant);
                }
                List<Participant> participantList = new ArrayList<>();
                for (Integer value : sizeMap.keySet()) {
                    participantList.addAll(sizeMap.get(value));
                }
                for (Participant participantFirst : participantList) {
                    participantMatchMapFound = cloneParticipantMatchMap(participantMatchMap);
                    int expectedSize = participantMatchParticipantSizeMap.get(participantMatchMapFound.size());
                    List<Participant> participantCombination = new ArrayList<>();
                    success = extractParticipantsCombinationsWithSize(participantsCombinations, cloneParticipantsOpponents(participantsOpponents), expectedSize, participantFirst, participantCombination, new ArrayList<>(maxParticipantVoidPerMatch), 0, goodEnough);
                    if (success) {
                        Integer participantTeamVoidCounter = 0;
                        for (Participant participant : participantCombination) {
                            if (participant instanceof ParticipantTeamVoid) {
                                participantTeamVoidCounter++;
                            }
                        }
                        if (maxParticipantVoidPerMatch.contains(participantTeamVoidCounter)) {
                            for (int i = 0; i < maxParticipantVoidPerMatch.size(); i++) {
                                if (maxParticipantVoidPerMatch.get(i).compareTo(participantTeamVoidCounter) == 0) {
                                    maxParticipantVoidPerMatch.remove(i);
                                    break;
                                }
                            }

                            participantMatchMapFound.put(participantMatchMapFound.size(), participantCombination);
                            if (participantMatchMapFound.size() < participantMatchParticipantSizeMap.size()) {
                                SortedMap<Participant, List<Participant>> participantsOpponentsSub = cloneParticipantsOpponents(participantsOpponents);
                                participantsOpponentsSub = removeParticipant(participantsOpponentsSub, participantCombination, !goodEnough);
//                            for (Participant opponent : participantCombination) {
//                                participantsOpponentsSub.remove(opponent);
//                            }
                                success = fillWithParticipantsCombinationsWithAvailableOpponent(participantsCombinations, participantMatchMapFound, participantMatchParticipantSizeMap, participantsOpponentsSub, participantsOriginal, numberOfParticipantPerMatch, new ArrayList<>(maxParticipantVoidPerMatch), goodEnough);
                            }
                        } else {
                            success = false;
                        }
                    }
                    if (success) {
                        break;
                    }
                }
            }
            if (success) {
                participantMatchMap.clear();
                participantMatchMap.putAll(participantMatchMapFound);
            }
        } else if (participantMatchMap.size() < participantMatchParticipantSizeMap.size()) {
            success = false;
        }
        if (!success) {
            participantMatchMap.size();
        }
        return success;
    }

    private static SortedMap<Integer, List<Participant>> cloneParticipantMatchMap(SortedMap<Integer, List<Participant>> participantMatchMap) {
        SortedMap<Integer, List<Participant>> participantMatchMapCloned = new TreeMap<>();
        for (Integer key : participantMatchMap.keySet()) {
            participantMatchMapCloned.put(key, new ArrayList<>(participantMatchMap.get(key)));

        }
        return participantMatchMapCloned;
    }

    private static SortedMap<Participant, List<Participant>> cloneParticipantsOpponents(SortedMap<Participant, List<Participant>> participantsOpponents) {
        SortedMap<Participant, List<Participant>> participantsOpponentsSub = new TreeMap<>();
        for (Participant participant : participantsOpponents.keySet()) {
            List<Participant> participantOpponents = participantsOpponents.get(participant);
//            if(participantOpponents == null)
//            for (Participant participant2 : participantsOpponents.keySet()) {
//                if (participant2.localId.compareTo(participant.localId) == 0)
//                    participantOpponents = participantsOpponents.get(participant2);
//            }
            participantsOpponentsSub.put(participant, new ArrayList<>(participantOpponents));
        }
        return participantsOpponentsSub;
    }

    private static boolean extractParticipantsCombinationsWithSize(List<List<Participant>> participantsCombinations, SortedMap<Participant, List<Participant>> participantsOpponents, Integer size, Participant participantContained, List<Participant> participants, List<Integer> maxParticipantVoidPerMatch, Integer depth, boolean goodEnough) {
        participants.add(participantContained);
        boolean success = true;
        if (depth.compareTo(size - 1) != 0) {
            success = false;
            List<Participant> participantsOpponentsPreferred = null;
            if (participantsOpponents.containsKey(participantContained)) {
                participantsOpponentsPreferred = participantsOpponents.get(participantContained);
            }
            int participantVoidInCombination = 0;
            for (Participant participant : participants) {
                if (participant instanceof ParticipantTeamVoid)
                    participantVoidInCombination++;
            }

            Integer participantVoidInCombinationMinimal = null;
            for (Integer maxParticipantVoidPerMatchEntry : maxParticipantVoidPerMatch) {
                if (participantVoidInCombinationMinimal == null || participantVoidInCombinationMinimal.compareTo(maxParticipantVoidPerMatchEntry) > 0)
                    participantVoidInCombinationMinimal = maxParticipantVoidPerMatchEntry;
            }
            boolean priorityToParticipantVoid = false;
            if (participantVoidInCombinationMinimal != null && participantVoidInCombination < participantVoidInCombinationMinimal) {
                priorityToParticipantVoid = true;
            }


            SortedMap<Participant, List<Participant>> participantsOpponentsGoodEnough = null;
            if (goodEnough) {
                participantsOpponentsGoodEnough = cloneParticipantsOpponents(participantsOpponents);
            }

            participantsOpponents = removeParticipantNotCompatibleWith(participantsOpponents, participantContained, goodEnough);
//            Integer participantVoidCurrent = 0;
//            for (Participant participant : participants) {
//                if (participant instanceof ParticipantTeamVoid) {
//                    participantVoidCurrent++;
//                }
//            }
            boolean participantVoidFound = false;
            for (Participant participant : participantsOpponents.keySet()) {
                if (participant instanceof ParticipantTeamVoid) {
                    participantVoidFound = true;
                    break;
                }
            }
            if (goodEnough) {
                participantsOpponentsGoodEnough.remove(participantContained);
                for (Participant participant : participantsOpponentsGoodEnough.keySet()) {
                    participantsOpponentsGoodEnough.get(participant).remove(participantContained);
                }
                for (Participant participant : participantsOpponentsGoodEnough.keySet()) {
                    if (participant instanceof ParticipantTeamVoid) {
                        participantVoidFound = true;
                        break;
                    }
                }
            }


//            Integer participantVoidToRemove = 0;
//            if (participantVoidNext > 0) {
//                while (participantVoidToRemove < participantVoidCurrent + participantVoidNext && participantVoidCurrent + participantVoidNext > 0 && !maxParticipantVoidPerMatch.isEmpty() && !maxParticipantVoidPerMatch.contains(participantVoidToRemove)) {
//                    participantVoidToRemove++;
//                }
//            }
//            if (participantVoidNext > 0 || !maxParticipantVoidPerMatch.isEmpty() || maxParticipantVoidPerMatch.contains(participantVoidToRemove)) {

//            if (!maxParticipantVoidPerMatch.isEmpty() && maxParticipantVoidPerMatch.contains(participantVoidToRemove)) {
//                for (int i = 0; i < maxParticipantVoidPerMatch.size(); i++) {
//                    if (maxParticipantVoidPerMatch.get(i).compareTo(participantVoidToRemove) == 0) {
//                        maxParticipantVoidPerMatch.remove(i);
//                        break;
//                    }
//                }
//            }

            SortedMap<Integer, List<Participant>> participantByRemainingOpponentSize = new TreeMap<>();
            for (Participant participant : participantsOpponents.keySet()) {
                if (!participantByRemainingOpponentSize.containsKey(participantsOpponents.get(participant).size())) {
                    participantByRemainingOpponentSize.put(participantsOpponents.get(participant).size(), new ArrayList<>());
                }
                participantByRemainingOpponentSize.get(participantsOpponents.get(participant).size()).add(participant);
            }


            List<Participant> participantsOrderedByRemainingOpponentSizeDesc = new ArrayList<>();
            for (Integer remainingOpponentSize : participantByRemainingOpponentSize.keySet()) {
                for (Participant participant : participantByRemainingOpponentSize.get(remainingOpponentSize)) {
                    participantsOrderedByRemainingOpponentSizeDesc.add(0, participant);
                }
            }


            List<Participant> participantsOrderedByRemainingOpponentSizeDescGoodEnough = new ArrayList<>();
            if (goodEnough) {
                SortedMap<Integer, List<Participant>> participantByRemainingOpponentSizeGoodEnough = new TreeMap<>();

                for (Participant participant : participantsOpponentsGoodEnough.keySet()) {
                    if (!participantByRemainingOpponentSizeGoodEnough.containsKey(participantsOpponentsGoodEnough.get(participant).size())) {
                        participantByRemainingOpponentSizeGoodEnough.put(participantsOpponentsGoodEnough.get(participant).size(), new ArrayList<>());
                    }
                    participantByRemainingOpponentSizeGoodEnough.get(participantsOpponentsGoodEnough.get(participant).size()).add(participant);
                }

                for (Integer remainingOpponentSize : participantByRemainingOpponentSizeGoodEnough.keySet()) {
                    for (Participant participant : participantByRemainingOpponentSizeGoodEnough.get(remainingOpponentSize)) {
                        participantsOrderedByRemainingOpponentSizeDescGoodEnough.add(0, participant);
                    }
                }
            }
            if (participantsOpponentsPreferred != null && !participantsOpponentsPreferred.isEmpty()) {
                for (Participant participant : participantsOpponentsPreferred) {
                    if (participantsOrderedByRemainingOpponentSizeDesc.contains(participant)) {
                        participantsOrderedByRemainingOpponentSizeDesc.remove(participant);
                        participantsOrderedByRemainingOpponentSizeDesc.add(0, participant);
                    }
                    if (goodEnough) {
                        if (participantsOrderedByRemainingOpponentSizeDescGoodEnough.contains(participant)) {
                            participantsOrderedByRemainingOpponentSizeDescGoodEnough.remove(participant);
                            participantsOrderedByRemainingOpponentSizeDescGoodEnough.add(0, participant);
                        }
                    }
                }
            }
            List<List<Participant>> participantsOrderedByRemainingOpponentSizeDescCombinations = new ArrayList<>();
            List<List<Participant>> participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough = new ArrayList<>();
            if (maxParticipantVoidPerMatch != null && !maxParticipantVoidPerMatch.isEmpty()) {
                List<Participant> participantVoidListToRemove = new ArrayList<>();
                for (Participant participant : participantsOpponents.keySet()) {
                    if (participant instanceof ParticipantTeamVoid) {
                        participantVoidListToRemove.add(participant);
                    }
                }
                List<Participant> participantVoidListToRemoveGoodEnough = new ArrayList<>();
                if (goodEnough) {
                    for (Participant participant : participantsOpponentsGoodEnough.keySet()) {
                        if (participant instanceof ParticipantTeamVoid) {
                            participantVoidListToRemoveGoodEnough.add(participant);
                        }
                    }
                }
                for (Integer maxParticipantVoid : new HashSet<>(maxParticipantVoidPerMatch)) {
                    if (maxParticipantVoid > 0 && !participantVoidListToRemove.isEmpty()) {
                        if (maxParticipantVoid > 1) {
                            Combinator<Participant> combinator = new Combinator<>(participantVoidListToRemove, maxParticipantVoid);
                            for (List<Participant> participantsCombination : combinator) {
                                if (containsCombinations(new ArrayList<>(participantsOpponents.keySet()), participantsCombination)) {
                                    List<Participant> participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDesc);
                                    participantList.removeAll(participantVoidListToRemove);
                                    participantList.addAll(participantsCombination);
                                    participantsOrderedByRemainingOpponentSizeDescCombinations.add(participantList);
                                }
                                if (goodEnough) {
                                    List<Participant> participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDescGoodEnough);
                                    participantList.removeAll(participantVoidListToRemoveGoodEnough);
                                    participantList.addAll(participantsCombination);
                                    participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough.add(participantList);
                                }
                            }
                        } else {
                            for (Participant participant : participantVoidListToRemove) {
                                if (participantsOpponents.containsKey(participant)) {
                                    List<Participant> participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDesc);
                                    participantList.removeAll(participantVoidListToRemove);
                                    participantList.add(participant);
                                    participantsOrderedByRemainingOpponentSizeDescCombinations.add(participantList);
                                }
                                if (goodEnough) {
                                    List<Participant> participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDescGoodEnough);
                                    participantList.removeAll(participantVoidListToRemoveGoodEnough);
                                    participantList.add(participant);
                                    participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough.add(participantList);
                                }
                            }
                        }

                    } else {
                        List<Participant> participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDesc);
                        participantList.removeAll(participantVoidListToRemove);
                        participantsOrderedByRemainingOpponentSizeDescCombinations.add(participantList);
                        if (goodEnough) {
                            participantList = new ArrayList<>(participantsOrderedByRemainingOpponentSizeDescGoodEnough);
                            participantList.removeAll(participantVoidListToRemoveGoodEnough);
                            participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough.add(participantList);
                        }
                    }
                }
            } else {
                participantsOrderedByRemainingOpponentSizeDescCombinations.add(participantsOrderedByRemainingOpponentSizeDesc);
                if (goodEnough) {
                    participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough.add(participantsOrderedByRemainingOpponentSizeDescGoodEnough);
                }
            }
            if (!participantsOrderedByRemainingOpponentSizeDescCombinations.isEmpty()) {
                for (List<Participant> participantsOrderedByRemainingOpponentSizeDescCombination : participantsOrderedByRemainingOpponentSizeDescCombinations) {
                    boolean addOnlyVoid = priorityToParticipantVoid;
//                    if (maxParticipantVoidPerMatch.isEmpty())
//                        for (Participant participant :
//                                participantsOrderedByRemainingOpponentSizeDescCombination) {
//                            if (participant instanceof ParticipantTeamVoid)
//                                addOnlyVoid = true;
//                        }

                    if (!participantsOrderedByRemainingOpponentSizeDescCombination.isEmpty()) {
                        SortedMap<Participant, List<Participant>> participantsOpponentsCloned = cloneParticipantsOpponents(participantsOpponents);
                        participantsOpponentsCloned = removeNotFoundKeys(participantsOpponentsCloned, participantsOrderedByRemainingOpponentSizeDescCombination);
                        for (Participant participant : participantsOrderedByRemainingOpponentSizeDescCombination) {
                            if (!addOnlyVoid || participant instanceof ParticipantTeamVoid) {

                                if (extractParticipantsCombinationsWithSize(participantsCombinations, participantsOpponentsCloned, size, participant, participants, new ArrayList<>(), depth + 1, goodEnough)) {
                                    success = true;
                                    break;
                                } else {
                                    participants.remove(participant);
                                }
                            }
                        }
                    }
                    if (success)
                        break;
                }
            }
            if (!success && goodEnough && !participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough.isEmpty()) {
                for (List<Participant> participantsOrderedByRemainingOpponentSizeDescCombinationGoodEnough : participantsOrderedByRemainingOpponentSizeDescCombinationsGoodEnough) {
                    boolean addOnlyVoid = priorityToParticipantVoid;
//                    if (maxParticipantVoidPerMatch.isEmpty())
//                        for (Participant participant :
//                                participantsOrderedByRemainingOpponentSizeDescCombinationGoodEnough) {
//                            if (participant instanceof ParticipantTeamVoid)
//                                addOnlyVoid = true;
//                        }
                    participantsOpponentsGoodEnough.remove(participantContained);

                    if (!participantsOrderedByRemainingOpponentSizeDescCombinationGoodEnough.isEmpty()) {
                        SortedMap<Participant, List<Participant>> participantsOpponentsCloned = cloneParticipantsOpponents(participantsOpponentsGoodEnough);
                        participantsOpponentsCloned = removeNotFoundKeys(participantsOpponentsCloned, participantsOrderedByRemainingOpponentSizeDescCombinationGoodEnough);
                        for (Participant participant : participantsOrderedByRemainingOpponentSizeDescCombinationGoodEnough) {
                            if (!addOnlyVoid || participant instanceof ParticipantTeamVoid) {
                                if (extractParticipantsCombinationsWithSize(participantsCombinations, participantsOpponentsCloned, size, participant, participants, new ArrayList<>(), depth + 1, goodEnough)) {
                                    success = true;
                                    break;
                                } else {
                                    participants.remove(participant);
                                }
                            }
                        }
                    }
                    if (success)
                        break;
                }
            }

//            }
        } else if (!containsCombinationInCombinations(participantsCombinations, participants)) {
            success = false;
        }
        return success;
    }

    private static SortedMap<Participant, List<Participant>> removeNotFoundKeys(SortedMap<Participant, List<Participant>> participantsOpponents, List<Participant> participants) {
        List<Participant> participantListToRemove = new ArrayList<>();
        for (Participant participant : participantsOpponents.keySet()) {
            if (!participants.contains(participant)) {
                participantListToRemove.add(participant);
            }
        }
        for (Participant participant : participantListToRemove) {
            participantsOpponents.remove(participant);
        }
        return participantsOpponents;
    }

    private static SortedMap<Participant, List<Participant>> removeParticipantNotCompatibleWith(SortedMap<Participant, List<Participant>> participantsOpponents, Participant participantOpponent, boolean goodEnough) {
        Set<Participant> participantsToRemove = new HashSet<>();
        participantsOpponents.remove(participantOpponent);
        for (Participant participant : participantsOpponents.keySet()) {
            if (!(goodEnough && participantsOpponents.isEmpty()) || !participantsOpponents.get(participant).contains(participantOpponent)) {
                participantsToRemove.add(participant);
            }
        }
        for (Participant participantToRemove : participantsToRemove) {
            participantsOpponents.remove(participantToRemove);
        }
        return participantsOpponents;

    }

    private static List<List<Participant>> removeParticipantsCombinationPartial(List<List<Participant>> participantsCombinations, List<Participant> participants, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        List<List<Participant>> participantsMatch = new ArrayList<>();
        participantsMatch.add(participants);
        return removeParticipantsCombinationsPartial(participantsCombinations, participantsMatch, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    private static List<List<Participant>> removeParticipantsCombinationsPartial(List<List<Participant>> participantsCombinations, List<List<Participant>> participantsMatch, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        for (List<Participant> participants : participantsMatch) {
//            for (int i = numberOfParticipantQualifiedPerMatch+1; i < numberOfParticipantPerMatch; i++) {
            if (getRealSize(participants) < numberOfParticipantPerMatch)
                participantsCombinations = removeCombinationsInvolvingParticipants(participantsCombinations, participants, numberOfParticipantPerMatch, numberOfParticipantPerMatch - 1 > 2 ? numberOfParticipantPerMatch - 1 : 2);
//            else
//                participantsCombinations.remove(participants);
//            }
        }
        return participantsCombinations;
    }

    private static List<List<Participant>> cloneParticipantsCombinations(Collection<List<Participant>> participantsCombinations) {
        List<List<Participant>> participantsCombinationCloned = new ArrayList<>();
        participantsCombinationCloned.addAll(participantsCombinations);
        return participantsCombinationCloned;
    }

    private static boolean fillWithParticipantsCombinations(SortedMap<Integer, List<Participant>> participantMatchMap, int numberOfMatches, List<Participant> participantCombinationsSelected, List<List<Participant>> participantsCombinationsBase, List<List<Participant>> participantsCombinations, List<Participant> allParticipants, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch, SortedMap<Participant, List<Participant>> participantsOpponents, List<Integer> maxParticipantVoidPerMatch, boolean participantTypeSplittable, boolean goodEnough) {
        boolean success = true;

        boolean participantVoidMaxOk = true;
        if (participantCombinationsSelected != null) {
            participantMatchMap.put(participantMatchMap.size(), participantCombinationsSelected);

            participantVoidMaxOk = false;
            Integer numberOfParticipantVoid = 0;
            for (Participant participant : participantCombinationsSelected) {
                if (participant instanceof ParticipantTeamVoid)
                    numberOfParticipantVoid++;
            }
            if (maxParticipantVoidPerMatch.contains(numberOfParticipantVoid)) {
                participantVoidMaxOk = true;
            } else {
                for (Integer maxParticipantVoidElt : maxParticipantVoidPerMatch) {
                    if (maxParticipantVoidElt.compareTo(numberOfParticipantVoid) >= 0) {
                        participantVoidMaxOk = true;
                        break;
                    }

                }
            }
            if (!participantVoidMaxOk)
                success = false;
        }

        if (participantVoidMaxOk) {
            if (participantMatchMap.size() < numberOfMatches) {
                success = false;


                if (!participantsCombinations.isEmpty()) {


                    List<List<Participant>> participantsCombinationsSub = cloneParticipantsCombinations(participantsCombinations);
                    if (participantCombinationsSelected != null)
                        participantsCombinationsSub = removeCombinationsWithParticipants(participantsCombinationsSub, participantCombinationsSelected);
                    if (!participantsCombinationsSub.isEmpty()) {
                        for (List<Participant> participantsCombinationSub : participantsCombinationsSub) {
                            SortedMap<Integer, List<Participant>> participantMatchMapCloned = cloneParticipantMatchMap(participantMatchMap);
                            success = fillWithParticipantsCombinations(participantMatchMapCloned, numberOfMatches, participantsCombinationSub, participantsCombinationsBase, participantsCombinationsSub, allParticipants, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantsOpponents, new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, goodEnough);
                            if (success) {
                                participantMatchMap.clear();
                                participantMatchMap.putAll(participantMatchMapCloned);
                                break;
                            } else {
                                participantMatchMap.remove(participantMatchMap.size() - 1);
                            }
                        }
                    }
                    if (participantMatchMap.size() == numberOfMatches - 1) {
                        List<Participant> remainingParticipants = new ArrayList<>(allParticipants);
                        for (List<Participant> participantsMatch : participantMatchMap.values()) {
                            remainingParticipants.removeAll(participantsMatch);
                        }
                        SortedMap<Integer, List<Participant>> participantMatchMapCloned = cloneParticipantMatchMap(participantMatchMap);
                        if (!remainingParticipants.isEmpty() && containsCombinationInCombinations(participantsCombinationsBase, remainingParticipants)) {
                            success = fillWithParticipantsCombinations(participantMatchMapCloned, numberOfMatches, remainingParticipants, participantsCombinationsBase, participantsCombinationsSub, allParticipants, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantsOpponents, new ArrayList<>(maxParticipantVoidPerMatch), participantTypeSplittable, goodEnough);
                            if (success) {
                                participantMatchMap.clear();
                                participantMatchMap.putAll(participantMatchMapCloned);
                            } else if (goodEnough) {
                                for (List<Participant> opponents : participantMatchMap.values()) {
                                    int realSize = getRealSize(opponents);
                                    if (participantTypeSplittable || (!participantTypeSplittable && realSize == numberOfParticipantPerMatch))
                                        participantsOpponents = removeParticipantMeet(participantsOpponents, opponents);
                                }
                                if (participantsOpponents.size() == numberOfParticipantPerMatch) {
                                    List<Participant> participantList = new ArrayList<>(participantsOpponents.keySet());
                                    if (containsCombinationInCombinations(participantsCombinationsBase, participantList)) {
                                        participantMatchMapCloned.put(participantMatchMapCloned.size(), participantList);
                                        success = true;
                                        participantMatchMap.clear();
                                        participantMatchMap.putAll(participantMatchMapCloned);
                                    }
                                }
                            } else {
                                participantMatchMap.remove(participantMatchMap.size() - 1);
                            }
                        }
                    }
                }
            } else {
                success = true;
                for (List<Participant> participantList : participantMatchMap.values()) {
                    Integer numberOfParticipantVoid = 0;
                    for (Participant participant : participantList) {
                        if (participant instanceof ParticipantTeamVoid)
                            numberOfParticipantVoid++;

                    }
                    if (maxParticipantVoidPerMatch.contains(numberOfParticipantVoid)) {
                        for (int i = 0; i < maxParticipantVoidPerMatch.size(); i++) {
                            Integer maxParticipantVoidElt = maxParticipantVoidPerMatch.get(i);
                            if (maxParticipantVoidElt.compareTo(numberOfParticipantVoid) == 0) {
                                maxParticipantVoidPerMatch.remove(i);
                                break;
                            }

                        }
                    } else {
                        success = false;
                        break;
                    }
                }

            }
        }

        return success;
    }

//    private static boolean fillWithParticipantsCombinations(SortedMap<Integer, List<Participant>> participantMatchMap, Integer expectedSize, List<List<Participant>> participantsCombinations, List<Participant> allParticipants, int numberOfParticipantPerMatch, Integer depth) {
//
//
//    }


    private static List<Participant> getParticipantsCombinationsWithSize(Collection<List<Participant>> participantsCombinations, Integer size, Participant participantContained) {
        List<Participant> participants = null;
        boolean found = false;
        for (List<Participant> participantsCombination : participantsCombinations) {
            int realSize = getRealSize(participantsCombination);
            if (size.compareTo(realSize) == 0) {
                for (Participant participantCombination : participantsCombination) {
                    if (participantContained == null || participantCombination.compareTo(participantContained) == 0) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    participants = participantsCombination;
                    break;
                }
            }

        }
        return participants;
    }

    private static int getRealSize(List<Participant> participantsCombination) {
        int realParticipantsSize = 0;
        for (Participant participant : participantsCombination) {
            if (!(participant instanceof ParticipantTeamVoid)) {
                realParticipantsSize++;
            }
        }
        return realParticipantsSize;
    }

    private static boolean isBye(List<Participant> participantsCombination, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        int realParticipantsSize = getRealSize(participantsCombination);
        boolean result = false;
        if (realParticipantsSize <= numberOfParticipantQualifiedPerMatch) {
            result = true;
        }
        return result;
    }

    private static Set<Integer> getDistributionSize(Map<Participant, List<Participant>> opponentsByParticipant) {
        Set<Integer> sizes = new HashSet<>();
        for (List<Participant> participantList :
                opponentsByParticipant.values()) {
            sizes.add(participantList.size());

        }

        return sizes;
    }

    private static List<Participant> computeMatch(Map<Participant, List<Participant>> missingOpponentsByParticipant, Map<Participant, List<Participant>> opponentsByParticipant, int numberOfParticipantPerMatch) {
        return computeMatch(missingOpponentsByParticipant, opponentsByParticipant, numberOfParticipantPerMatch, 0);
    }

    private static List<Participant> computeMatch(Map<Participant, List<Participant>> missingOpponentsByParticipant, Map<Participant, List<Participant>> opponentsByParticipant, int numberOfParticipantPerMatch, int depth) {
        List<Participant> participantList = new ArrayList<>();
        if (numberOfParticipantPerMatch > 0) {
            if (!missingOpponentsByParticipant.isEmpty()) {
                Map.Entry<Participant, List<Participant>> entry = missingOpponentsByParticipant.entrySet().iterator().next();
                participantList.add(entry.getKey());
                Map<Participant, List<Participant>> missingOpponentsByParticipantSub = new TreeMap<>(missingOpponentsByParticipant);
                missingOpponentsByParticipantSub.remove(entry.getKey());
                Map<Participant, List<Participant>> opponentsByParticipantSub = new TreeMap<>(opponentsByParticipant);
                opponentsByParticipantSub.remove(entry.getKey());

                if (depth == 0) {
                    Set<Participant> keysToRemove = new HashSet<>();
                    for (Participant participant : missingOpponentsByParticipantSub.keySet()) {
                        if (!entry.getValue().contains(participant)) {
                            keysToRemove.add(participant);
                        }
                    }
                    for (Participant keyToRemove : keysToRemove) {
                        missingOpponentsByParticipantSub.remove(keyToRemove);
                    }
                }
                participantList.addAll(computeMatch(missingOpponentsByParticipantSub, opponentsByParticipantSub, numberOfParticipantPerMatch - 1, depth + 1));
            } else {
                int currentSize = -1;
                Participant participantFound = null;
                for (Participant participant : opponentsByParticipant.keySet()) {
                    if (currentSize == -1 || opponentsByParticipant.get(participant).size() < currentSize) {
                        currentSize = opponentsByParticipant.get(participant).size();
                        participantFound = participant;
                    }
                }
                if (participantFound != null) {
                    participantList.add(participantFound);
                    Map<Participant, List<Participant>> opponentsByParticipantSub = new TreeMap<>(opponentsByParticipant);
                    opponentsByParticipantSub.remove(participantFound);

                    participantList.addAll(computeMatch(missingOpponentsByParticipant, opponentsByParticipantSub, numberOfParticipantPerMatch - 1, depth + 1));
                }
            }
        }
        return participantList;
    }


    private static boolean combinationAlreadyInCombinations(List<List<Integer>> combinations, List<Integer> combination) {
        boolean alreadyIn = false;
        for (List<Integer> combinationCurrent : combinations) {
            if (compareCombinationsInteger(combinationCurrent, combination, true) == 0) {
                alreadyIn = true;
                break;
            }
        }
        return alreadyIn;
    }

    private static int compareCombinationsInteger(List<Integer> combinationCurrent, List<Integer> combination, boolean strict) {
        return compareCombinations(combinationCurrent, combination, strict);
    }

    private static int compareCombinationsParticipant(List<Participant> combinationCurrent, List<Participant> combination, boolean strict) {
        return compareCombinations(combinationCurrent, combination, strict);
    }

    public static int compareCombinations(List<? extends Comparable> combinationCurrent, List<? extends Comparable> combination, boolean strict) {
        Collections.sort(combinationCurrent);
        Collections.sort(combination);
        int compare = 0;
        if (strict || combinationCurrent.size() == combination.size()) {
            if (combinationCurrent.size() == combination.size()) {
                for (int i = 0; i < combinationCurrent.size(); i++) {
                    compare = combinationCurrent.get(i).compareTo(combination.get(i));
                    if (compare != 0)
                        break;
                }
            } else {
                compare = Integer.compare(combinationCurrent.size(), combination.size());
            }
        } else {
            if (combinationCurrent.size() >= combination.size()) {
                for (Comparable elementProposed : combination) {
                    for (Comparable elementCurrent : combinationCurrent) {
                        compare = elementCurrent.compareTo(elementProposed);
                        if (compare == 0)
                            break;
                    }
                }

            } else {
                compare = Integer.compare(combinationCurrent.size(), combination.size());
            }

        }
        return compare;
    }

    private static boolean participantionCombinationAlreadyInParticipantsCombinations(Collection<List<Participant>> participantsCombinations, List<Participant> participantsCombination, boolean strict) {
        boolean alreadyIn = false;
        for (List<Participant> participantsCombinationCurrent : participantsCombinations) {
            if (compareCombinations(participantsCombinationCurrent, participantsCombination, strict) == 0) {
                alreadyIn = true;
                break;
            }
        }
        return alreadyIn;
    }
//    private static int compareParticipantCombinations(List<Participant> participantsCombinationCurrent, List<Participant> participantsCombination) {
//        Collections.sort(participantsCombinationCurrent);
//        Collections.sort(participantsCombination);
//        int compare = 0;
//        if (participantsCombinationCurrent.size() == participantsCombination.size()) {
//            for (int i = 0; i < participantsCombinationCurrent.size(); i++) {
//                compare = participantsCombinationCurrent.get(i).compareTo(participantsCombination.get(i));
//                if (compare != 0)
//                    break;
//            }
//        } else {
//            compare = Integer.compare(participantsCombinationCurrent.size(), participantsCombination.size());
//        }
//        return compare;
//    }

    private static List<List<Participant>> getParticipantsCombinationsRound(List<List<Participant>> participantsCombinations, List<Participant> participantsCloned, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch, int round) {
        List<Participant> participants = new ArrayList<>();
        List<Participant> currentParticipants = new ArrayList<>();
        List<List<Participant>> participantsPerMatch = new ArrayList<>();
        List<Participant> participantsClonedCopy = new ArrayList<>();
//        List<Participant> currentLonelyParticipantsCombination = new ArrayList<>();
//        List<List<Participant>> lonelyParticipantsCombinations = getLonelyParticipantsCombinations(participantsCloned, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);
        List<List<Participant>> lonelyParticipantsCombinationsUsed = new ArrayList<>();
        int numberOfRoundBasedOnCombinations = 0;
//        if (participantsCloned.size() % numberOfParticipantPerMatch > numberOfParticipantPerMatch / 2) {
//            numberOfRoundBasedOnCombinations = participantsCombinations.size() / ((participantsCloned.size() + (numberOfParticipantPerMatch - participantsCloned.size() % numberOfParticipantPerMatch)) / numberOfParticipantPerMatch);
//        } else {

        if (participantsCloned.size() < numberOfParticipantPerMatch) {
            numberOfRoundBasedOnCombinations = 1;
        } else {
            int a = (participantsCloned.size() - (participantsCloned.size() % numberOfParticipantPerMatch));
            int b = a / numberOfParticipantPerMatch;
            numberOfRoundBasedOnCombinations = participantsCombinations.size() / b;
        }
//        }

        int roundMax = round;
        if (numberOfParticipantPerMatch > 2) {
            roundMax = numberOfRoundBasedOnCombinations;
        }

        Collections.sort(participantsCombinations, (o1, o2) -> {
            return compareCombinationsParticipant(o1, o2, true);
        });
        List<List<Participant>> allMatches = new ArrayList<>();
        if (round <= roundMax) {
            for (int i = 0; i < round && i < roundMax; i++) {

//                List<List<Participant>> participantListToRemove = new ArrayList<>();
//                if (numberOfParticipantPerMatch > 2) {
//                    for (List<Participant> participantList : participantsPerMatch) {
////                    if (participantList.size() == numberOfParticipantPerMatch) {
////                        Permutator<Participant> permutator = null;
////                        permutator = new Permutator<>(participantList, numberOfParticipantPerMatch - 1);
////                        for (List<Participant> combination : permutator) {
////                            if (!participantionCombinationAlreadyInParticipantsCombinations(participantListToRemove, combination, true)) {
////                                participantListToRemove.add(combination);
////                            }
////                        }
////                    }else
//                        if (participantList.size() < numberOfParticipantPerMatch && participantList.size() > numberOfParticipantQualifiedPerMatch) {
//                            if (!participantionCombinationAlreadyInParticipantsCombinations(participantListToRemove, participantList, true)) {
//                                participantListToRemove.add(participantList);
//                            }
//                        }
//                    }
////                participantsCombinations = removeUsedCombination(participantsCombinations, participantListToRemove, false);
//                    participantsCombinations = removeUsedCombination(participantsCombinations, participantsPerMatch, true);
////                lonelyParticipantsCombinations = removeUsedCombination(lonelyParticipantsCombinations , participantListToRemove, false);
//                } else {
                participantsCombinations = removeUsedCombination(participantsCombinations, participantsPerMatch, true);
//                }
                List<Participant> firstCombination = null;
//                currentLonelyParticipantsCombination = new ArrayList<>();
                List<List<Participant>> participantsCombinationsWithLeftParticipants = new ArrayList<>(participantsCombinations);

//                if (lonelyParticipantsCombinations.isEmpty()) {
//                    participantsCombinationsWithLeftParticipants = new ArrayList<>(participantsCombinations);
//                } else {
//                    int index = 0;
//                    do {
//                        participantsCombinationsWithLeftParticipants = new ArrayList<>(participantsCombinations);
//                        currentLonelyParticipantsCombination = lonelyParticipantsCombinations.get(index);
//                        if (!currentLonelyParticipantsCombination.isEmpty()) {
//                            for (Participant lonelyParticipant : currentLonelyParticipantsCombination) {
//                                participantsCombinationsWithLeftParticipants = removeCombinationsWithParticipant(participantsCombinationsWithLeftParticipants, lonelyParticipant);
//                            }
//                        }
//                        index++;
//
//                    }
//                    while (participantsCombinationsWithLeftParticipants.isEmpty() && index < lonelyParticipantsCombinations.size());
//                }
                Collections.sort(participantsCombinationsWithLeftParticipants, (o1, o2) -> {
                    return compareCombinationsParticipant(o1, o2, true);
                });

                participantsPerMatch = new ArrayList<>();
                if (i < roundMax) {

                    int indexTry = 0;
                    if (indexTry < participantsCombinationsWithLeftParticipants.size()) {
                        do {
                            participantsPerMatch = new ArrayList<>();
                            participants = new ArrayList<>();
                            currentParticipants = new ArrayList<>();
                            participantsClonedCopy = new ArrayList<>(participantsCloned);
//                            for (Participant participant : currentLonelyParticipantsCombination) {
//                                participantsClonedCopy.remove(participant);
//                            }
                            firstCombination = participantsCombinationsWithLeftParticipants.get(participantsCombinationsWithLeftParticipants.size() - (indexTry + 1));
                            currentParticipants.addAll(firstCombination);
                            participants.addAll(currentParticipants);
                            participantsPerMatch.add(currentParticipants);
                            for (Participant participant : participants) {
                                participantsClonedCopy.remove(participant);
                            }
//                        participantsClonedCopy.removeAll(participants);
                            boolean doResetIndex = true;
                            for (int j = 0; j < participantsCombinationsWithLeftParticipants.size() && !participantsClonedCopy.isEmpty(); j++) {
                                if (j >= participantsCombinationsWithLeftParticipants.size()) {
                                    if (doResetIndex) {
                                        j = 0;
                                        doResetIndex = false;
                                    } else
                                        break;
                                }

                                boolean alreadyIn = false;
                                currentParticipants = new ArrayList<>();
                                currentParticipants.addAll(participantsCombinationsWithLeftParticipants.get(participantsCombinationsWithLeftParticipants.size() - (j + 1)));
                                for (Participant participant : currentParticipants) {
                                    if (participants.contains(participant)) {
                                        alreadyIn = true;
                                        break;
                                    }
                                }
                                if (!alreadyIn) {
                                    participants.addAll(currentParticipants);
                                    participantsClonedCopy.removeAll(participants);
                                    participantsPerMatch.add(currentParticipants);
                                    j = 0;
                                    doResetIndex = true;
                                }
                            }

                            if (!participantsClonedCopy.isEmpty() && participantsClonedCopy.size() < numberOfParticipantPerMatch && getFirstCombinationWithParticipants(lonelyParticipantsCombinationsUsed, participantsClonedCopy, true) == null) {
                                participants.addAll(participantsClonedCopy);
                                currentParticipants = new ArrayList<>(participantsClonedCopy);
                                participantsClonedCopy.removeAll(participantsClonedCopy);
                                participantsPerMatch.add(currentParticipants);
                                lonelyParticipantsCombinationsUsed.add(currentParticipants);
                                allMatches.addAll(participantsPerMatch);
                            } else if (participantsClonedCopy.isEmpty()) {
                                allMatches.addAll(participantsPerMatch);
                            } else {
                                indexTry++;
                            }
                        }
                        while (!participantsClonedCopy.isEmpty() && indexTry < participantsCombinationsWithLeftParticipants.size());


                        if (!participantsClonedCopy.isEmpty()) {
                            participantsPerMatch.clear();

                            Map<Participant, List<Participant>> opponentsByParticipant = new HashMap<>();
                            for (Participant participant : participantsCloned) {
                                if (!(participant instanceof ParticipantTeamVoid)) {
                                    List<Participant> opponents = ArraysTools.getAllOpponentsFor(participant, allMatches, false);
                                    if (opponents.size() < participantsCloned.size() - 1) {
                                        List<Participant> missingOpponents = new ArrayList<>(participantsCloned);
                                        missingOpponents.removeAll(opponents);
                                        opponentsByParticipant.put(participant, missingOpponents);
                                    }
//                                        LOGGER.debug("[" + participant.bibNumber + "]" + opponents.size() + " " + opponentIds);
//                    Assert.assertEquals(opponents.size(), numberOfParticipant - 1);
                                }
                            }
                            if (opponentsByParticipant.size() <= numberOfParticipantPerMatch) {

                            }


//                        } else if (!currentLonelyParticipantsCombination.isEmpty()) {
//                            participantsPerMatch.add(currentLonelyParticipantsCombination);
                        }
//                        if (!currentLonelyParticipantsCombination.isEmpty()) {
//                            lonelyParticipantsCombinations.remove(currentLonelyParticipantsCombination);
//                        }
                    }
                    if (participantsPerMatch.isEmpty()) {
                        break;
                    }
                } else {
                    participantsPerMatch.clear();
                }
            }
        }
        return participantsPerMatch;
    }
/*
    private static List<List<Participant>> getLonelyParticipantsCombinations(List<Participant> participantsCloned, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        List<List<Participant>> lonelyParticipantsCombinations = new ArrayList<>();
        int numberOfParticipantPerMatchLonely = participantsCloned.size() % numberOfParticipantPerMatch;
        if (numberOfParticipantPerMatchLonely > 0) {
            Permutator<Participant> permutator = null;
//            if (numberOfParticipantPerMatchLonely > numberOfParticipantQualifiedPerMatch)
            permutator = new Permutator<>(participantsCloned, numberOfParticipantPerMatchLonely);
//            else
//                permutator = new Permutator<>(participantsCloned, Math.min(numberOfParticipantPerMatchLonely, numberOfParticipantQualifiedPerMatch));

            for (List<Participant> combination : permutator) {
                if (!participantionCombinationAlreadyInParticipantsCombinations(lonelyParticipantsCombinations, combination, true)) {
                    Collections.sort(combination);
                    lonelyParticipantsCombinations.add(combination);
                }
            }
            int maxLonelyParticipantCombinationsSize = (participantsCloned.size() + (numberOfParticipantPerMatch - participantsCloned.size() % numberOfParticipantPerMatch)) * numberOfParticipantQualifiedPerMatch;
//            if (numberOfParticipantPerMatchLonely <= numberOfParticipantQualifiedPerMatch)
//                maxLonelyParticipantCombinationsSize = numberOfParticipantQualifiedPerMatch * participantsCloned.size();
//            if (numberOfParticipantPerMatchLonely > numberOfParticipantQualifiedPerMatch && !LonelyParticipantsCombinations.isEmpty() && LonelyParticipantsCombinations.size() > (numberOfParticipantQualifiedPerMatch + 1) * participantsCloned.size()) {
            while (lonelyParticipantsCombinations.size() > maxLonelyParticipantCombinationsSize) {
                List<Participant> firstCombinationWithParticipant = getParticipantsCombinationToRemove(lonelyParticipantsCombinations, numberOfParticipantPerMatchLonely);
                if (firstCombinationWithParticipant == null)
                    break;
                lonelyParticipantsCombinations.remove(firstCombinationWithParticipant);
            }
//                for (int i = 0; i < LonelyParticipantsCombinations.size(); i++) {
//                    List<Participant> LonelyParticipantsCombinationCurrent = LonelyParticipantsCombinations.get(i);
//                    for (Participant participant : LonelyParticipantsCombinationCurrent) {
//                        removeCombinationsWithParticipant(LonelyParticipantsCombinations, participant);
//                        LonelyParticipantsCombinations.add(i, LonelyParticipantsCombinationCurrent);
//                    }
//                }
//            }
        }
        Collections.sort(lonelyParticipantsCombinations, (o1, o2) -> {
            return compareCombinationsParticipant(o1, o2, true);
        });
        return lonelyParticipantsCombinations;
    }*/
/*

    private static List<Participant> getParticipantsCombinationToRemove(List<List<Participant>> lonelyParticipantsCombinations, int numberOfParticipantPerMatchLonely) {
        List<Participant> participantsCombinationToRemove = new ArrayList<>();
        Map<Participant, List<List<Participant>>> participantParticipantsCombinationsMap = new HashMap<>();
        if (!lonelyParticipantsCombinations.isEmpty()) {
            for (List<Participant> participantsCombination : lonelyParticipantsCombinations) {
                for (Participant participant : participantsCombination) {
                    if (!participantParticipantsCombinationsMap.containsKey(participant)) {
                        participantParticipantsCombinationsMap.put(participant, new ArrayList<>());
                    }
                    participantParticipantsCombinationsMap.get(participant).add(participantsCombination);

                }
            }
        }

        int bestValue = 0;
        Map<Integer, List<Participant>> levelParticipants = new HashMap<>();
        List<List<Participant>> participantsCombinations = null;
        for (Map.Entry<Participant, List<List<Participant>>> participantParticipantsCombinationsMapEntry : participantParticipantsCombinationsMap.entrySet()) {
            if (bestValue < participantParticipantsCombinationsMapEntry.getValue().size()) {
                participantsCombinations = participantParticipantsCombinationsMapEntry.getValue();
                bestValue = participantParticipantsCombinationsMapEntry.getValue().size();
            }
            Integer key = -participantParticipantsCombinationsMapEntry.getValue().size();
            if (!levelParticipants.containsKey(key)) {
                levelParticipants.put(key, new ArrayList<>());
            }
            levelParticipants.get(key).add(participantParticipantsCombinationsMapEntry.getKey());
        }
        SortedMap<Integer, List<Participant>> sortedMap = new TreeMap<>(levelParticipants);
        List<Participant> participantList = new ArrayList<>();
        int size = sortedMap.size();
        int index = 0;
        Collections.sort(participantsCombinations, (o1, o2) -> {
            return compareCombinationsParticipant(o1, o2, true);
        });
        for (List<Participant> participantListFromSorted : sortedMap.values()) {
            Collections.sort(participantListFromSorted);
            participantList.addAll(participantListFromSorted);
            if (participantList.size() >= numberOfParticipantPerMatchLonely || index + 1 < size) {
//            Collections.reverse(participantList);
                participantsCombinationToRemove = getPreferredCombination(participantsCombinations, participantList, numberOfParticipantPerMatchLonely);
                if (participantsCombinationToRemove != null)
                    break;
//            Collections.reverse(participantList);
//            }
            }
            index++;
        }
        if (participantsCombinationToRemove != null)
            Collections.sort(participantsCombinationToRemove);
        return participantsCombinationToRemove;
    }
*/
/*
    private static List<Participant> getPreferredCombination(List<List<Participant>> participantsCombinations, List<Participant> participantList, int numberOfParticipantPerMatchLonely) {
        List<Participant> preferredCombination = null;
        int index = 0;
        int depth = Math.min(numberOfParticipantPerMatchLonely, participantList.size());
        int maxIndex = MathUtils.factorial(participantList.size(), participantList.size() - depth);
        do {
            preferredCombination = getFirstCombinationWithParticipants(participantsCombinations, getSubList(new ArrayList<>(participantList), depth, index), false);
            index++;
        } while (preferredCombination == null && index < maxIndex);
        return preferredCombination;
    }*/

    private static List<Participant> getFirstCombinationWithParticipants(List<List<Participant>> participantsCombinations, List<Participant> participants, boolean strict) {
        List<Participant> combinationFound = null;
        if (participantionCombinationAlreadyInParticipantsCombinations(participantsCombinations, participants, true)) {
            combinationFound = participants;
        } else if (participantionCombinationAlreadyInParticipantsCombinations(participantsCombinations, participants, false)) {
            for (List<Participant> participantsCombinationCurrent : participantsCombinations) {
                if (compareCombinations(participantsCombinationCurrent, participants, strict) == 0) {
                    combinationFound = participantsCombinationCurrent;
                    break;
                }
            }
        }
        return combinationFound;
    }


    public static List<List<Participant>> getAllCombinationsWithParticipants(List<List<Participant>> participantsCombinations, List<Participant> participants) {
        List<List<Participant>> combinationsFound = new ArrayList<>();
        for (List<Participant> participantsCombinationCurrent : participantsCombinations) {
            if (compareCombinations(participantsCombinationCurrent, participants, false) == 0) {
                combinationsFound.add(participantsCombinationCurrent);
            }
        }
        return combinationsFound;
    }


    private static List<Participant> getSubList(List<Participant> participantList, int numberOfParticipantPerMatchLonely, int index) {
        List<Participant> subList = new ArrayList<>();
        int depth = numberOfParticipantPerMatchLonely;
        int currentIndex = index;
        if (depth > 1) {
            currentIndex = currentIndex / MathUtils.factorial(participantList.size() - 1, participantList.size() - 1 - numberOfParticipantPerMatchLonely);
            depth--;
        }
        subList.add(participantList.get(currentIndex % participantList.size()));
        if (numberOfParticipantPerMatchLonely > 1) {
            List<Participant> subsubList = new ArrayList<>(participantList);
            subsubList.removeAll(subList);
            subList.addAll(getSubList(subsubList, numberOfParticipantPerMatchLonely - 1, index));
        }
        return subList;
    }
//
//    private static List<List<Participant>> createSubLists(List<List<Participant>> subList, List<Participant> participantList, int depth) {
//
//        for (int i = 0; i < participantList.size(); i++) {
//            List<Participant> subParticipants = new ArrayList<>(participantList);
//            subParticipants.remove(participantList.get(i));
//            subList.add(createSubList(participantList.get(i), subParticipants, depth - 1));
//        }
//        return subList;
//    }
//
//    private static List<List<Participant>> createSubList(Participant participant, List<Participant> subParticipants, int depth) {
//        List<List<Participant>> lists = new ArrayList<>();
//        depth--;
//        if (depth > 0) {
//
//        }else{
//            List<Participant> participants = new ArrayList<>();
//            participants.add(participant);
//        }
//        return lists;
//    }

    private static List<List<Participant>> removeCombinationsWithoutParticipant
            (List<List<Participant>> participantsCombinations, Participant startingParticipant) {
        List<List<Participant>> participantsCombinationsToRemove = new ArrayList<>();
        for (List<Participant> participantsCombination : participantsCombinations) {
            boolean withParticipant = false;
            for (Participant participant : participantsCombination) {
                if (participant.compareTo(startingParticipant) == 0) {
                    withParticipant = true;
                    break;
                }
            }
            if (!withParticipant)
                participantsCombinationsToRemove.add(participantsCombination);
        }
        participantsCombinations.removeAll(participantsCombinationsToRemove);
        return participantsCombinations;
    }

    private static List<List<Participant>> removeCombinationsWithParticipant
            (List<List<Participant>> participantsCombinations, Participant participant) {
        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        return removeCombinationsWithParticipants(participantsCombinations, participants);
    }


    private static List<List<Participant>> removeCombinationsWithParticipants
            (List<List<Participant>> participantsCombinations, List<Participant> participants) {
        List<List<Participant>> participantsCombinationsNew = new ArrayList<>();
        boolean withParticipant = false;
        for (List<Participant> participantsCombination : participantsCombinations) {
            withParticipant = false;
            for (Participant participant : participantsCombination) {
                if (participants.contains(participant)) {
                    withParticipant = true;
                    break;
                }
            }
            if (!withParticipant)
                participantsCombinationsNew.add(participantsCombination);
        }
        participantsCombinations.clear();
        participantsCombinations.addAll(participantsCombinationsNew);
        return participantsCombinations;
    }

    private static List<Participant> getFirstCombinationWithParticipant
            (List<List<Participant>> participantsCombinations, Participant startingParticipant) {
        List<Participant> participantsCombinationFound = new ArrayList<>();
        for (List<Participant> participantsCombination : participantsCombinations) {
            for (Participant participant : participantsCombination) {
                if (participant.compareTo(startingParticipant) == 0) {
                    participantsCombinationFound = participantsCombination;
                    break;
                }
            }
        }
        return participantsCombinationFound;
    }

    private static List<List<Participant>> removeUsedCombination
            (List<List<Participant>> participantsCombinations, Collection<List<Participant>> participantsCombinationsUsed, boolean strict) {
        List<List<Participant>> participantsCombinationsToRemove = new ArrayList<>();
        for (List<Participant> participantsCombinationUsed : participantsCombinationsUsed) {
            for (List<Participant> participantsCombination : participantsCombinations) {
                if (compareCombinations(participantsCombination, participantsCombinationUsed, strict) == 0) {
                    participantsCombinationsToRemove.add(participantsCombination);
                    break;
                }
            }
        }
        participantsCombinations.removeAll(participantsCombinationsToRemove);
        return participantsCombinations;
    }


    private static List<List<Participant>> removeUsedPartialCombination
            (List<List<Participant>> participantsCombinations, List<List<Participant>> participantsCombinationsUsed, int numberOfParticipantPerMatch) {
        if (!participantsCombinationsUsed.isEmpty()) {
            List<List<Participant>> participantsCombinationsToRemove = new ArrayList<>();
            if (numberOfParticipantPerMatch > 2) {
                int partialSize = numberOfParticipantPerMatch - 1;
                for (List<Participant> participantsCombinationUsed : participantsCombinationsUsed) {
                    List<Participant> participantsCombinationUsedTemp = new ArrayList<>();
                    for (int i = 0; i < partialSize && i < participantsCombinationUsed.size(); i++) {
                        participantsCombinationUsedTemp.add(participantsCombinationUsed.get(i));
                    }
                    if (participantsCombinationUsedTemp.size() > 1) {
                        for (List<Participant> participantsCombination : participantsCombinations) {
                            if (containsCombinations(participantsCombination, participantsCombinationUsedTemp)) {
                                participantsCombinationsToRemove.add(participantsCombination);
                            }
                        }
                    }
                }
            }
            participantsCombinations.removeAll(participantsCombinationsToRemove);
        }
        return participantsCombinations;
    }


    private static List<List<Participant>> removeCombinationsInvolvingParticipants
            (List<List<Participant>> participantsCombinations, List<Participant> participants, int numberOfParticipantPerMatch, int participantThreshold) {
        if (!participants.isEmpty()) {
            Set<List<Participant>> participantsCombinationsToRemove = new HashSet<>();
            for (List<Participant> participantsCombination : participantsCombinations) {
                int currentThreshold = 0;
                if (participantsCombination.size() == participants.size() || participantThreshold == 1) {
                    for (Participant participantInCombination : participantsCombination) {
                        for (Participant participant : participants) {
                            if (participantInCombination.compareTo(participant) == 0) {
                                currentThreshold++;
                                if (currentThreshold >= participantThreshold) {
                                    participantsCombinationsToRemove.add(participantsCombination);
                                }
                                break;
                            }
                        }
                        if (currentThreshold >= participantThreshold) {
                            break;
                        }
                    }
                }
            }
            participantsCombinations.removeAll(participantsCombinationsToRemove);
        }
        return participantsCombinations;
    }

    private static boolean containsCombinations(List<Participant> participantsCombination, List<Participant> participants) {
        boolean contained = !participants.isEmpty();
        for (Participant participant : participants) {
            contained = false;
            for (Participant participantContainer : participantsCombination) {
                if (participant.compareTo(participantContainer) == 0) {
                    contained = true;
                    break;
                }
            }
            if (!contained)
                break;
        }
        return contained;
    }

    private static boolean containsCombinationInCombinations(List<List<Participant>> participantCombinations, List<Participant> participantCombination) {
        boolean contained = false;
        for (List<Participant> participantCombinationElt : participantCombinations) {
            if (containsCombinations(participantCombinationElt, participantCombination)) {
                contained = true;
                break;
            }
        }
        return contained;
    }


    public static List<Participant> getAllOpponentsFor(Participant participant, List<List<Participant>> allMatches, boolean unique) {
        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        List<List<Participant>> allMatchesConcerned = ArraysTools.getAllCombinationsWithParticipants(allMatches, participants);
        List<Participant> opponents = new ArrayList<>();
        for (List<Participant> match : allMatchesConcerned) {
            for (Participant opponent : match) {
                if (participant.compareTo(opponent) != 0 && (!unique || !opponents.contains(opponent)))
                    opponents.add(opponent);
            }
        }
        opponents.remove(participant);
        return opponents;
    }

}

