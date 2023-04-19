package com.qc.competition.service.format;



import com.qc.competition.service.Utils;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTree;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompetitionGroupFormatTest {
    static Logger LOGGER = Logger.getLogger(CompetitionGroupFormatTest.class);

    @Test(groups = {"unit"}, dataProvider = "mainDataProvider")
    public void testSwissRound25Percent(int numberOfParticipant, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipantQualifiedPerMatch, boolean participantSplittable) {
        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = participantQuantityCorrected / 4;
        if (participantQuantityOut == 0)
            participantQuantityOut = 1;
        int numberOfRound = CompetitionGroupFormat.SWISS.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        LOGGER.info(CompetitionGroupFormat.SWISS + "\tnumberOfParticipant:" + numberOfParticipant + "\tnumberOfParticipantPerMatch:" + numberOfParticipantPerMatch + "\tplayVersusType:" + playVersusType + "\tparticipantQuantityOut:" + participantQuantityOut + "\tnumberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch + "\t" + "numberOfRound:" + numberOfRound);
        Assert.assertTrue(numberOfRound > 0);
        Assert.assertTrue(numberOfRound <= participantQuantityCorrected - 1);
    }


    @Test(groups = {"unit"}, dataProvider = "mainDataProvider")
    public void testSwissRoundTopForFinals4Matches(int numberOfParticipant, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipantQualifiedPerMatch, boolean participantSplittable) {
        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = numberOfParticipantPerMatch * 4;
//        if (participantQuantityOut < numberOfParticipant)
//            participantQuantityOut = 1;
        int numberOfRound = CompetitionGroupFormat.SWISS.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        LOGGER.info(CompetitionGroupFormat.SWISS + "\tnumberOfParticipant:" + numberOfParticipant + "\tnumberOfParticipantPerMatch:" + numberOfParticipantPerMatch + "\tplayVersusType:" + playVersusType + "\tparticipantQuantityOut:" + participantQuantityOut + "\tnumberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch + "\tnumberOfRound:" + numberOfRound);
        Assert.assertTrue(numberOfRound > 0);
        Assert.assertTrue(numberOfRound < participantQuantityCorrected - 1);
    }

    public void testSwissRoundTopForFinals4Matches500() {
        int numberOfParticipant = 500;
        int numberOfParticipantPerMatch = 2;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipantQualifiedPerMatch = 1;
        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = numberOfParticipantPerMatch * 4;
        boolean participantSplittable = true;
        int numberOfRound = CompetitionGroupFormat.SWISS.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        LOGGER.info(CompetitionGroupFormat.SWISS + "\tnumberOfParticipant:" + numberOfParticipant + "\tnumberOfParticipantPerMatch:" + numberOfParticipantPerMatch + "\tplayVersusType:" + playVersusType + "\tparticipantQuantityOut:" + participantQuantityOut + "\tnumberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch + "\tnumberOfRound:" + numberOfRound);
        Assert.assertTrue(numberOfRound > 0);
        Assert.assertTrue(numberOfRound < participantQuantityCorrected - 1);
    }

    @Test
    public void testSwissRoundTopForFinals4Matches5() {
        int numberOfParticipant = 5;
        int numberOfParticipantPerMatch = 4;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = true;
        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = numberOfParticipantPerMatch * 4;
        int numberOfRound = CompetitionGroupFormat.SWISS.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        LOGGER.info(CompetitionGroupFormat.SWISS + "\tnumberOfParticipant:" + numberOfParticipant + "\tnumberOfParticipantPerMatch:" + numberOfParticipantPerMatch + "\tplayVersusType:" + playVersusType + "\tparticipantQuantityOut:" + participantQuantityOut + "\tnumberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch + "\tnumberOfRound:" + numberOfRound);
        Assert.assertTrue(numberOfRound > 0);
        Assert.assertTrue(numberOfRound < participantQuantityCorrected - 1);
    }

    @Test(groups = {"unit"}, dataProvider = "mainDataProvider")
    public void testRoundRobinRound(int numberOfParticipant, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int numberOfParticipantQualifiedPerMatch, boolean participantSplittable) {
        int participantQuantityCorrected = numberOfParticipant;
        if (participantQuantityCorrected % numberOfParticipantPerMatch != 0)
            participantQuantityCorrected += numberOfParticipantPerMatch - participantQuantityCorrected % numberOfParticipantPerMatch;
        int participantQuantityOut = participantQuantityCorrected / 4;
        if (participantQuantityOut == 0)
            participantQuantityOut = 1;

        int numberOfRound = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
        LOGGER.info(CompetitionGroupFormat.ROUND_ROBIN + "\tnumberOfParticipant:" + numberOfParticipant + "\tnumberOfParticipantPerMatch" + numberOfParticipantPerMatch + "\tplayVersusType:" + playVersusType + "\tnumberOfParticipantQualifiedPerMatch:" + numberOfParticipantQualifiedPerMatch + "\t" + "numberOfRound:" + numberOfRound);
        if (numberOfParticipantPerMatch == 2) {
            Assert.assertEquals(participantQuantityCorrected - 1, numberOfRound);
        } else {
            Assert.assertTrue(numberOfRound <= (numberOfParticipant * (1 + numberOfParticipant % numberOfParticipantPerMatch)) * numberOfParticipant, numberOfRound + "<=" + (numberOfParticipant * (1 + numberOfParticipant % numberOfParticipantPerMatch)) * numberOfParticipant);
            Assert.assertTrue(numberOfRound >= numberOfParticipant / numberOfParticipantPerMatch, numberOfRound + ">=" + numberOfParticipant / numberOfParticipantPerMatch);
        }
    }

    @Test
    public void testRoundRobinRound5for4PlayerPerMatch() {
        int numberOfParticipant = 5;
        int numberOfParticipantPerMatch = 4;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = true;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }

    @Test
    public void testRoundRobinRound5for4PlayerPerMatchNotSplittable() {
        int numberOfParticipant = 5;
        int numberOfParticipantPerMatch = 4;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = false;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }


    @Test
    public void testRoundRobinRound7for4PlayerPerMatch() {
        int numberOfParticipant = 7;
        int numberOfParticipantPerMatch = 4;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = true;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }

    @Test
    public void testRoundRobinRound10for4PlayerPerMatch() {
        int numberOfParticipant = 10;
        int numberOfParticipantPerMatch = 4;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = true;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }


    @Test
    public void testRoundRobinRound30for10PlayerPerMatch() {
        int numberOfParticipant = 30;
        int numberOfParticipantPerMatch = 10;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_10_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 5;
        boolean participantSplittable = true;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }

    @Test
    public void testRoundRobinRound3for2PlayerPerMatch() {
        int numberOfParticipant = 3;
        int numberOfParticipantPerMatch = 2;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipantQualifiedPerMatch = 1;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantOut = numberOfParticipant;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 1;
        boolean allowEvenNumber = false;
        Integer numberOfRoundMinimum = 1;
        Integer numberOfRoundMaximum = 100;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        boolean participantSplittable = true;
        CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, numberOfParticipantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumber, numberOfRoundMinimum, numberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        Assert.assertEquals(competitionGroupFormatTree.competitionGroupFormatTreeGroups.size(), 1, competitionGroupFormatTree.competitionGroupFormatTreeGroups.size() + "groups !");
        Assert.assertEquals(competitionGroupFormatTree.competitionGroupFormatTreeGroups.iterator().next().competitionGroupFormatTreeRounds.size(), 1, competitionGroupFormatTree.competitionGroupFormatTreeGroups.size() + "rounds !");
        Assert.assertEquals(competitionGroupFormatTree.competitionGroupFormatTreeGroups.iterator().next().competitionGroupFormatTreeRounds.iterator().next().competitionGroupFormatTreeMatches.size(), numberOfParticipant, competitionGroupFormatTree.competitionGroupFormatTreeGroups.size() + "matchs !");
    }

    @DataProvider(name = "mainDataProvider", parallel = false)
    public Object[][] testRoundRobinDataProvider() {
        List<List<Object>> parameterSetList = new ArrayList<>();
        int participantSize = 1;
        {
            for (int numberOfParticipantPerMatch = 2; numberOfParticipantPerMatch <= 4; numberOfParticipantPerMatch++) {
//                if (numberOfParticipantPerMatch % 2 == 0) {
                Set<Integer> numberOfParticipantQualifiedPerMatchList = new HashSet<>();
                numberOfParticipantQualifiedPerMatchList.add(1);
                numberOfParticipantQualifiedPerMatchList.add(numberOfParticipantPerMatch / 2);
                for (Integer numberOfParticipantQualifiedPerMatch : numberOfParticipantQualifiedPerMatchList) {
                    for (int numberOfParticipant = numberOfParticipantPerMatch + 1; numberOfParticipant <= 50; numberOfParticipant++) {
                        if (numberOfParticipantPerMatch != 3) {
                            List parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(PlayVersusType.getValueFor(numberOfParticipantPerMatch, participantSize));
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(true);
                            parameterSetList.add(parameters);
                            parameters = new ArrayList<>();
                            parameters.add(numberOfParticipant);
                            parameters.add(numberOfParticipantPerMatch);
                            parameters.add(PlayVersusType.getValueFor(numberOfParticipantPerMatch, participantSize));
                            parameters.add(numberOfParticipantQualifiedPerMatch);
                            parameters.add(false);
                            parameterSetList.add(parameters);
                        }
                    }
                }
//                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        return computationParameters;
    }


    @Test
    public void testRoundRobinRoun64for8PlayerPerMatch() {
        int numberOfParticipant = 64;
        int numberOfParticipantPerMatch = 8;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_8_TEAMS_1_PARTICIPANT;
        int numberOfParticipantQualifiedPerMatch = 2;
        boolean participantSplittable = true;
        testRoundRobinRound(numberOfParticipant, numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualifiedPerMatch, participantSplittable);
    }

}
