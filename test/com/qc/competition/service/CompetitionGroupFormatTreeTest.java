package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTree;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

/**
 * Created by Duncan on 20/10/2015.
 */
public class CompetitionGroupFormatTreeTest {
    private static Logger LOGGER = Logger.getLogger(CompetitionGroupFormatTreeTest.class);

    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_ELIMINATION() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int participantQualifiedPerMatch = 2;
        int numberOfParticipantPerMatch = 4;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        boolean participantSplittable = true;
        Logger logger = LOGGER;
        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_1V1_ELIMINATION_SINGLE() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int participantQualifiedPerMatch = 1;
        int numberOfParticipantPerMatch = 2;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;
        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        boolean participantSplittable = true;
        Logger logger = LOGGER;
        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_1V1_ELIMINATION_SINGLE_WITH_THIRD_PLACE_MATCH() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int participantQualifiedPerMatch = 1;
        int numberOfParticipantPerMatch = 2;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;
        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        boolean thirdPlaceMatch = true;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        boolean fixedParticipantSize = false;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch, fixedParticipantSize, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_1V1_DOUBLE_ELIMINATION() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 2;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_1V1_TRIPLE_ELIMINATION() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 3;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1_DECA_ELIMINATION() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 10;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1_FULL_ELIMINATION() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 100;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT_ELIMINATION_SINGLE_FIRST_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = (int) Math.pow(3, 4);
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 3;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT_DOUBLE_ELIMINATION_FIRST_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = (int) Math.pow(3, 4);
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 3;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 2;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT_ELIMINATION_FIRST_AND_SECOND_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 3;
        int participantQualifiedPerMatch = 2;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT_DOUBLE_ELIMINATION_FIRST_AND_SECOND_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 3;
        int participantQualifiedPerMatch = 2;
        int competitionGroupQuantity = 2;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_ELIMINATION_FIRST_AND_SECOND_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 2;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_DOUBLE_ELIMINATION_FIRST_AND_SECOND_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 2;
        int competitionGroupQuantity = 2;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_ELIMINATION_FIRST_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 1;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_DOUBLE_ELIMINATION_FIRST_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 2;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_FULL_ELIMINATION_FIRST_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 512;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = numberOfParticipant;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }


    @Test
    public void testCompetitionGroupFormat_FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT_FULL_ELIMINATION_FIRST_AND_SECOND_QUALIFIED() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 512;
        int numberOfParticipantOut = 2;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 2;
        int competitionGroupQuantity = numberOfParticipant;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());

    }

    @Test
    public void testCompetitionGroupFormat_1V1_SWISS() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.SWISS;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 8;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1_ROUND_ROBIN() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ROUND_ROBIN;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 2;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 8;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1V1V1_SWISS_64() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.SWISS;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 8;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1V1V1_SWISS_59() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.SWISS;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 59;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 8;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }


    @Test
    public void testCompetitionGroupFormat_1V1V1V1_ROUND_ROBIN() {
        CompetitionGroupFormat competitionGroupFormat = CompetitionGroupFormat.ROUND_ROBIN;
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int numberOfParticipant = 64;
        int numberOfParticipantOut = 1;
        int numberOfParticipantPerMatch = 4;
        int participantQualifiedPerMatch = 1;
        int competitionGroupQuantity = 8;
        int numberOfPlayPerMatchMinimum = 1;
        int numberOfPlayPerMatchMaximum = 9;

        int mixingNumberOfRoundMinimum = 1;
        int mixingNumberOfRoundMaximum = 4;
        boolean allowEvenNumberOfPlay = false;
        boolean finalGroupSizeThresholdEnabled = false;
        int finalGroupSizeThreshold = 0;
        int numberOfPlayPerMatchMinimumFinalGroup = 0;
        int numberOfPlayPerMatchMaximumFinalGroup = 0;
        Logger logger = LOGGER;
        boolean participantSplittable = true;

        CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumberOfPlay, mixingNumberOfRoundMinimum, mixingNumberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, false, false, participantSplittable);
        logger.info(competitionGroupFormatTree.toString());
    }
}
