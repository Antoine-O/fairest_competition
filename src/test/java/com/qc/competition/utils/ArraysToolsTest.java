package com.qc.competition.utils;


import com.qc.competition.service.Utils;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.service.structure.Participant;
import com.qc.competition.service.structure.ParticipantTeamVoid;
import com.qc.competition.service.structure.PlayVersusType;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.*;

public class ArraysToolsTest {
    static Logger LOGGER = Logger.getLogger(ArraysToolsTest.class);


    @Test(groups = {"unit"})
    public void testRoundRobin12() {
        int numberOfParticipant = 12;
        int numberOfParticipantPerMatch = 2;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }


    @Test(groups = {"unit"})
    public void testRoundRobin4() {
        int numberOfParticipant = 4;
        int numberOfParticipantPerMatch = 2;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin5() {
        int numberOfParticipant = 5;
        int numberOfParticipantPerMatch = 2;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }


    @Test(groups = {"unit"})
    public void testRoundRobin6With3ParticipantsPerMatch() {
        int numberOfParticipant = 6;
        int numberOfParticipantPerMatch = 3;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin4With3ParticipantsPerMatch() {
        int numberOfParticipant = 4;
        int numberOfParticipantPerMatch = 3;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin7With4ParticipantsPerMatch() {
        int numberOfParticipant = 7;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin5With4ParticipantsPerMatch() {
        int numberOfParticipant = 5;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin6With4ParticipantsPerMatch() {
        int numberOfParticipant = 6;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin9With4ParticipantsPerMatch() {
        int numberOfParticipant = 9;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin10With4ParticipantsPerMatch() {
        int numberOfParticipant = 10;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin12With4ParticipantsPerMatch() {
        int numberOfParticipant = 12;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin14With4ParticipantsPerMatch() {
        int numberOfParticipant = 14;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin16With4ParticipantsPerMatch() {
        int numberOfParticipant = 16;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }


    @Test(groups = {"unit"})
    public void testRoundRobin13With4ParticipantsPerMatch() {
        int numberOfParticipant = 13;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }


    @Test(groups = {"unit"})
    public void testRoundRobin20With4ParticipantsPerMatch() {
        int numberOfParticipant = 20;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @Test(groups = {"unit"})
    public void testRoundRobin50With4ParticipantsPerMatch() {
        int numberOfParticipant = 50;
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 1;
        testRoundRobin(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);

    }

    @DataProvider(name = "testRoundRobinDataProvider", parallel = false)
    public Object[][] testRoundRobinDataProvider() {
        List<List<Object>> parameterSetList = new ArrayList<>();
        {
            {
                int numberOfParticipantPerMatch = 2;
                for (int numberOfParticipant = 2; numberOfParticipant <= 50; numberOfParticipant++) {
                    if (numberOfParticipant >= numberOfParticipantPerMatch + 1) {
//                        if (numberOfParticipantPerMatch != 3) {
                        Set<Integer> numberOfParticipantQualifiedPerMatchList = new HashSet<>();
                        numberOfParticipantQualifiedPerMatchList.add(1);
                        numberOfParticipantQualifiedPerMatchList.add(numberOfParticipantPerMatch / 2);
                        for (Integer numberOfParticipantQualifiedPerMatch : numberOfParticipantQualifiedPerMatchList) {

                            List parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(Boolean.TRUE);
                            parameterSetList.add(parameters);
                            parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(Boolean.FALSE);
                            parameterSetList.add(parameters);

                        }
//                        }
                    }
                }
            }

            for (int numberOfParticipant = 2; numberOfParticipant <= 50; numberOfParticipant++) {
                for (int numberOfParticipantPerMatch = 3; numberOfParticipantPerMatch <= 4; numberOfParticipantPerMatch++) {
                    if (numberOfParticipant >= numberOfParticipantPerMatch + 1) {
//                        if (numberOfParticipantPerMatch != 3) {
                        Set<Integer> numberOfParticipantQualifiedPerMatchList = new HashSet<>();
//                            numberOfParticipantQualifiedPerMatchList.add(1);
                        numberOfParticipantQualifiedPerMatchList.add(numberOfParticipantPerMatch / 2);
                        for (Integer numberOfParticipantQualifiedPerMatch : numberOfParticipantQualifiedPerMatchList) {
                            List parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(Boolean.TRUE);
                            parameterSetList.add(parameters);
                            parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(Boolean.FALSE);
                            parameterSetList.add(parameters);

                        }
//                        }
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        return computationParameters;
    }

    @DataProvider(name = "testRoundRobinDataProviderFor1v1v1v1", parallel = false)
    public Object[][] testRoundRobinDataProviderFor1v1v1v1() {
        List<List<Object>> parameterSetList = new ArrayList<>();
        {
            int numberOfParticipantPerMatch = 4;
//                if (numberOfParticipantPerMatch != 3) {
            Set<Integer> numberOfParticipantQualifiedPerMatchList = new HashSet<>();
            numberOfParticipantQualifiedPerMatchList.add(1);
            if (numberOfParticipantPerMatch > 2)
                numberOfParticipantQualifiedPerMatchList.add(numberOfParticipantPerMatch / 2);
            for (Integer numberOfParticipantQualifiedPerMatch : numberOfParticipantQualifiedPerMatchList) {
                for (int numberOfParticipant = numberOfParticipantQualifiedPerMatch + 1; numberOfParticipant <= 50; numberOfParticipant++) {
                    List parameters = new ArrayList<>();
                    parameters.add(numberOfParticipant);
                    parameters.add(numberOfParticipantPerMatch);
                    parameters.add(numberOfParticipantQualifiedPerMatch);
                    parameters.add(Boolean.TRUE);
                    parameterSetList.add(parameters);
                    parameters = new ArrayList<>();
                    parameters.add(numberOfParticipant);
                    parameters.add(numberOfParticipantPerMatch);
                    parameters.add(numberOfParticipantQualifiedPerMatch);
                    parameters.add(Boolean.FALSE);
                    parameterSetList.add(parameters);
                }

//                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        return computationParameters;
    }


    @Test
    public void test6thRound() {
        int numberOfParticipant = 12, numberOfParticipantPerMatch = 2, numberOfParticipantQualifiedPerMatch = 1, round = 6;
        Boolean participantTypeSplittable = false;
        List<List<Participant>> participantPerMatchOriginal = getFakeParticipants(numberOfParticipant, numberOfParticipantPerMatch);
        ArraysTools.roundRobin(null, participantPerMatchOriginal, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);

    }

    @Test(groups = {"unit"}, dataProvider = "testRoundRobinDataProvider")
    public void test10TimesSameResultRound(int numberOfParticipant,
                                           int numberOfParticipantPerMatch,
                                           int numberOfParticipantQualifiedPerMatch, boolean participantSplittable) {
        PlayVersusType playVersusType = PlayVersusType.getValueFor(numberOfParticipantPerMatch, 1);
        int previousResult = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(numberOfParticipant, numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        int newResult = 0;
        for (int i = 0; i < 10; i++) {
            newResult = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(numberOfParticipant, numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);

            LOGGER.info("[" + i + "]Number Of round \t:\t" + newResult);
            if (i > 0)
                Assert.assertEquals(newResult, previousResult);
            previousResult = newResult;
        }
    }

    @Test(groups = {"unit"}, dataProvider = "testRoundRobinDataProvider")
    public void test10TimesSameResult(int numberOfParticipant,
                                      int numberOfParticipantPerMatch,
                                      int numberOfParticipantQualifiedPerMatch, boolean participantSplittable) {
        List<List<Participant>> participantPerMatchOriginal = getFakeParticipants(numberOfParticipant, numberOfParticipantPerMatch);
        Boolean participantTypeSplittable = false;
        SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRoundPrevious = null;
        for (int i = 0; i < 10; i++) {

            List<List<Participant>> participantListPerMatchPrevious = null;
            List<List<Participant>> participantListPerMatch = new ArrayList<>(participantPerMatchOriginal);
            SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRound = ArraysTools.roundRobin(null, participantListPerMatch, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);
            if (participantListPerMatchPrevious != null) {
                for (Integer round : participantsPerMatchPerRound.keySet()) {
                    Assert.assertTrue(compareParticipantPerMatch(participantsPerMatchPerRoundPrevious.get(round), participantsPerMatchPerRound.get(round)) == 0, "round " + round + "fails");
                }
            }
            participantsPerMatchPerRoundPrevious = participantsPerMatchPerRound;
        }
    }

    private int compareParticipantPerMatch(List<List<Participant>> participantListPerMatchPrevious, List<List<Participant>> participantListPerMatch) {
        int result = Integer.compare(participantListPerMatchPrevious.size(), participantListPerMatch.size());
        if (result == 0)
            for (int i = 0; i < participantListPerMatchPrevious.size(); i++) {
                result = ArraysTools.compareCombinations(participantListPerMatchPrevious.get(i), participantListPerMatch.get(i), true);
                if (result != 0)
                    break;
            }
        return result;
    }

    @Test(groups = {"unit"}, dataProvider = "testRoundRobinDataProviderFor1v1v1v1")
    public void testRoundRobinFor1v1v1v1(int numberOfParticipant,
                                         int numberOfParticipantPerMatch,
                                         int numberOfParticipantQualifiedPerMatch) {
        testRoundRobin(numberOfParticipant,
                numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch);
    }


    @Test(groups = {"unit"}, dataProvider = "testRoundRobinDataProvider")
    public void testRoundRobin(int numberOfParticipant,
                               int numberOfParticipantPerMatch,
                               int numberOfParticipantQualifiedPerMatch) {
        String output = "";
        Set<String> matches = new HashSet<>();
        List<List<Participant>> participantPerMatchOriginal = getFakeParticipants(numberOfParticipant, numberOfParticipantPerMatch);
        output = "";
        PlayVersusType playVersusType = PlayVersusType.getValueFor(numberOfParticipantPerMatch, 1);
//        int numberOfRound = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(numberOfParticipant, numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch);
//        LOGGER.info("Number Of round \t:\t" + numberOfRound);
//        int numberOfParticipantOriginalSize = 0;
//        for (List<Participant> participants : participantPerMatchOriginal) {
//            output += "|";
//            for (Participant participant : participants) {
//                output += "-" + participant.bibNumber;
//                numberOfParticipantOriginalSize++;
//            }
//        }
//        LOGGER.info("participantListOriginal      \t:\t" + output);
//        List<Participant> participantListOriginalRibbon = ArraysTools.participantsPerMatchToRibbon(participantPerMatchOriginal, numberOfParticipantPerMatch);
//        output = "";
//        for (Participant participant : participantListOriginalRibbon) {
//            output += "|" + participant.bibNumber;
//        }
//        LOGGER.info("participantListOriginalRibbon\t:\t" + output);

//        int emptyParticipantsQuantity = 0;
//        for (List<Participant> participants : participantPerMatchOriginal) {
//            for (Participant participant : participants) {
//                if (participant instanceof ParticipantTeamVoid)
//                    emptyParticipantsQuantity++;
//            }
//        }
//        if (numberOfParticipantPerMatch == 2) {
//            Assert.assertEquals(numberOfParticipantOriginalSize - 1, numberOfRound);
//        } else {
//            Assert.assertTrue(numberOfRound <= (numberOfParticipantOriginalSize * (1 + numberOfParticipantOriginalSize % numberOfParticipantPerMatch)) * numberOfParticipantOriginalSize, numberOfRound + "<=" + (numberOfParticipantOriginalSize * (1 + numberOfParticipantOriginalSize % numberOfParticipantPerMatch)) * numberOfParticipantOriginalSize);
//            Assert.assertTrue(numberOfRound >= Math.floor((double) numberOfParticipantOriginalSize / (double) numberOfParticipantPerMatch), numberOfRound + ">=" + Math.floor((double) numberOfParticipantOriginalSize / (double) numberOfParticipantPerMatch));
//        }
//        List<List<Participant>> allMatches = new ArrayList<>();
        Boolean participantTypeSplittable = false;
        SortedMap<Integer, List<List<Participant>>> participantsPerMatchPerRound = ArraysTools.roundRobin(null, participantPerMatchOriginal, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantTypeSplittable);
        List<List<Participant>> allMatches = new ArrayList<>();
        for (Integer round : participantsPerMatchPerRound.keySet()) {

            List<List<Participant>> participantListPerMatch = participantsPerMatchPerRound.get(round);
            output = "";
            for (List<Participant> participants : participantListPerMatch) {
                output += "|";
                for (Participant participant : participants) {
                    output += "-" + participant.bibNumber;
                }
            }
            LOGGER.info("[" + round + "]" + "participantListOutput     \t:\t" + output);

            for (List<Participant> participantList : participantListPerMatch) {
                List<String> matchParticipants = new ArrayList<>();
                for (Participant participant : participantList) {
                    matchParticipants.add(participant.localId);
                }
                Collections.sort(matchParticipants);
                String match = "";
                for (String matchParticipant : matchParticipants) {
                    match += "-" + matchParticipant;
                }
                LOGGER.debug("[" + round + "]" + match);
                Assert.assertTrue(matchParticipants.size() <= numberOfParticipantPerMatch, matchParticipants.size() + "<=" + numberOfParticipantPerMatch);
//                int previousSize = getMatchNotByeSize(matches, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);
                matches.add(match);
//                int currentSize = getMatchNotByeSize(matches, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);
//                int currentByeSize = getMatchByeSize(matches, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch);
//                if (currentByeSize == 0)
//                    Assert.assertNotEquals(previousSize, currentSize, "[" + round + "]" + "previousSize:" + previousSize + " " + "currentSize :" + currentSize);
            }
            allMatches.addAll(participantListPerMatch);
        }
        for (List<Participant> participants : participantPerMatchOriginal) {
            for (Participant participant : participants) {
                if (!(participant instanceof ParticipantTeamVoid)) {
                    List<Participant> opponents = ArraysTools.getAllOpponentsFor(participant, allMatches, true);
                    List<String> opponentIds = new ArrayList<>();
                    List<String> opponentIdReals = new ArrayList<>();
                    for (Participant participant1 :
                            opponents) {
                        opponentIds.add(participant1.localId);
                        if (!(participant1 instanceof ParticipantTeamVoid)) {
                            opponentIdReals.add(participant1.localId);
                        }
                    }
                    Collections.sort(opponentIds);
                    LOGGER.debug("[" + participant.bibNumber + "]" + opponents.size() + " " + opponentIds);
                    Assert.assertEquals(new HashSet<>(opponentIdReals).size(), numberOfParticipant - 1);
                }
            }
        }
        for (List<Participant> participants : participantPerMatchOriginal) {
            for (Participant participant : participants) {
                if (!(participant instanceof ParticipantTeamVoid)) {
                    List<Participant> opponents = ArraysTools.getAllOpponentsFor(participant, allMatches, false);
                    List<String> opponentIds = new ArrayList<>();
                    List<String> opponentIdReals = new ArrayList<>();
                    for (Participant participant1 :
                            opponents) {
                        opponentIds.add(participant1.localId);
                        if (!(participant1 instanceof ParticipantTeamVoid)) {
                            opponentIdReals.add(participant1.localId);
                        }
                    }
                    Collections.sort(opponentIds);
                    LOGGER.debug("[" + participant.bibNumber + "]" + opponents.size() + " " + opponentIds);
                    Assert.assertEquals(new HashSet<>(opponentIdReals).size(), numberOfParticipant - 1);
                    if (numberOfParticipantPerMatch == 2)
                        Assert.assertEquals(opponentIdReals.size(), numberOfParticipant - 1);
                    else
                        Assert.assertTrue(opponents.size() >= numberOfParticipant - 1);
                }
            }
        }
    }


    private int getMatchNotByeSize(Set<String> matches, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        int matchNotByeSize = 0;
        for (String match : matches) {
            StringTokenizer st = new StringTokenizer(match, "-");
            if (st.countTokens() == numberOfParticipantPerMatch || st.countTokens() > numberOfParticipantQualifiedPerMatch)
                matchNotByeSize++;
        }
        return matchNotByeSize;
    }


    private int getMatchByeSize(Set<String> matches, int numberOfParticipantPerMatch, int numberOfParticipantQualifiedPerMatch) {
        int matchByeSize = 0;
        for (String match : matches) {
            StringTokenizer st = new StringTokenizer(match, "-");
            if (st.countTokens() <= numberOfParticipantQualifiedPerMatch)
                matchByeSize++;
        }
        return matchByeSize;
    }

    private List<List<Participant>> getFakeParticipants(int numberOfParticipant, int numberOfParticipantPerMatch) {
        List<List<Participant>> participantsPerMatch = new ArrayList<>();
        List<Participant> participants = new ArrayList<>();
//        int numberOfFakeParticipant = (numberOfParticipantPerMatch - numberOfParticipant % numberOfParticipantPerMatch) % numberOfParticipantPerMatch;
//        int numberOfFakeParticipantRemaining = numberOfFakeParticipant;
        for (int i = 0; i < numberOfParticipant; i++) {
            com.qc.competition.service.structure.ParticipantSingle participant = new com.qc.competition.service.structure.ParticipantSingle();
            participant.id = "" + (i + 1);
            participant.localId = "" + (i + 1);
            participant.bibNumber = "" + (i + 1);
            participants.add(participant);
//            if (numberOfFakeParticipantRemaining > 0 && i > 0 && (i + 1) % (numberOfParticipant / numberOfFakeParticipant) == 0) {
//                com.qc.competition.service.structure.ParticipantTeamVoid participantTeamVoid = new com.qc.competition.service.structure.ParticipantTeamVoid();
//                participantTeamVoid.id = "X" + (numberOfFakeParticipant - numberOfFakeParticipantRemaining);
//                participantTeamVoid.localId = "X" + (numberOfFakeParticipant - numberOfFakeParticipantRemaining);
//                participantTeamVoid.bibNumber = "X" + i;
//                participants.add(participantTeamVoid);
//                numberOfFakeParticipantRemaining--;
//            }
        }
//
//        for (int i = 0; i < numberOfFakeParticipantRemaining; i++) {
//            com.qc.competition.service.structure.ParticipantTeamVoid participantTeamVoid = new com.qc.competition.service.structure.ParticipantTeamVoid();
//            participantTeamVoid.id = "X" + i;
//            participantTeamVoid.localId = "X" + i;
//            participantTeamVoid.bibNumber = "X" + i;
//            participants.add(participantTeamVoid);
//        }
        participantsPerMatch = ArraysTools.participantsListToPerMatch(participants, numberOfParticipantPerMatch);
        return participantsPerMatch;
    }

//
//    public void testRoundRobin3() {
//
//        com.qc.competition.service.structure.ParticipantSingle participant1 = new com.qc.competition.service.structure.ParticipantSingle();
//        participant1.localId = "1";
//        participant1.bibNumber = "1";
//        com.qc.competition.service.structure.ParticipantSingle participant2 = new com.qc.competition.service.structure.ParticipantSingle();
//        participant2.localId = "2";
//        participant2.bibNumber = "2";
//        com.qc.competition.service.structure.ParticipantSingle participant3 = new com.qc.competition.service.structure.ParticipantSingle();
//        participant3.localId = "3";
//        participant3.bibNumber = "3";
//        com.qc.competition.service.structure.ParticipantSingle participant4 = new com.qc.competition.service.structure.ParticipantSingle();
//        participant4.localId = "4";
//        participant4.bibNumber = "4";
//        List<Participant> participantList = new ArrayList<>();
//        participantList.add(participant1);
//        participantList.add(participant2);
//        participantList.add(participant3);
//        participantList.add(participant4);
//        ArraysTools.roundRobin(participantList, 1, 1);
//        Assert.assertEquals(participantList.get(1).bibNumber, "2");
//
//
//        participantList = new ArrayList<>();
//        participantList.add(participant1);
//        participantList.add(participant2);
//        participantList.add(participant3);
//        participantList.add(participant4);
//        ArraysTools.roundRobin(participantList, 2, 1);
//        Assert.assertEquals(participantList.get(1).bibNumber, "4");
//
//        participantList = new ArrayList<>();
//        participantList.add(participant1);
//        participantList.add(participant2);
//        participantList.add(participant3);
//        participantList.add(participant4);
//        ArraysTools.roundRobin(participantList, 3, 1);
//        Assert.assertEquals(participantList.get(1).bibNumber, "3");
//
//        participantList = new ArrayList<>();
//        participantList.add(participant1);
//        participantList.add(participant2);
//        participantList.add(participant3);
//        participantList.add(participant4);
//        ArraysTools.roundRobin(participantList, 4, 1);
//        Assert.assertEquals(participantList.get(1).bibNumber, "2");
//
//    }


    @Test(groups = {"unit"})
    public void testRoundForRoundRobin() {
        int numberOfParticipantPerMatch = 2;
        int numberOfParticipantQualifiedPerMatch = 1;
        int numberOfParticipant = 4;

        testRoundForRoundRobin(numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch,
                numberOfParticipant
        );
    }


    @Test(groups = {"unit"})
    public void testRoundForRoundRobin1v1v1v1_4players() {
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        int numberOfParticipant = 4;
        testRoundForRoundRobin(numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch,
                numberOfParticipant
        );
    }


    @Test(groups = {"unit"})
    public void testRoundForRoundRobin1v1v1v1_5players() {
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        int numberOfParticipant = 5;
        testRoundForRoundRobin(numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch,
                numberOfParticipant
        );
    }


    @Test(groups = {"unit"})
    public void testRoundForRoundRobin1v1v1v1_6players() {
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        int numberOfParticipant = 6;
        testRoundForRoundRobin(numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch,
                numberOfParticipant
        );
    }


    @Test(groups = {"unit"})
    public void testRoundForRoundRobin1v1v1v1_10players() {
        int numberOfParticipantPerMatch = 4;
        int numberOfParticipantQualifiedPerMatch = 2;
        int numberOfParticipant = 10;
        testRoundForRoundRobin(numberOfParticipantPerMatch,
                numberOfParticipantQualifiedPerMatch,
                numberOfParticipant
        );
    }

    public void testRoundForRoundRobin(int numberOfParticipantPerMatch,
                                       int numberOfParticipantQualifiedPerMatch,
                                       int numberOfParticipant
    ) {

        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = participantQuantityCorrected / 4;
        if (participantQuantityOut == 0)
            participantQuantityOut = 1;
        boolean participantSplittable = true;

        SortedMap<Integer, List<List<Participant>>> roundMatchParticipants = ArraysTools.getParticipantsCombinations(numberOfParticipant, numberOfParticipantPerMatch, numberOfParticipantQualifiedPerMatch, participantSplittable);
        for (Integer round : roundMatchParticipants.keySet()) {
            LOGGER.info("---- ROUND " + round + " ----");
            List<List<Participant>> matchParticipants = roundMatchParticipants.get(round);
            for (List<Participant> participants : matchParticipants) {
                String output = "";
                for (Participant participant : participants) {
                    output += participant.localId + "-";
                }
                LOGGER.info(output);

            }

        }

        int numberOfRound = roundMatchParticipants.size();
        if (numberOfParticipantPerMatch == 2) {
            Assert.assertEquals(participantQuantityCorrected - 1, numberOfRound);
        } else {
            Assert.assertTrue(numberOfRound >= numberOfParticipant / numberOfParticipantPerMatch, numberOfRound + ">" + numberOfParticipant / numberOfParticipantPerMatch);
            Assert.assertTrue(numberOfRound <= numberOfParticipant + numberOfParticipantPerMatch, numberOfRound + "<=" + (numberOfParticipant + numberOfParticipantPerMatch));
        }
    }

}
