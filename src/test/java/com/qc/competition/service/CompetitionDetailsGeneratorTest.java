package com.qc.competition.service;


import com.qc.competition.service.structure.*;
import com.qc.competition.service.structure.tree.CompetitionInstanceTree;
import com.qc.competition.service.template.*;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceComparator;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceGeneratorImpl;
import com.qc.competition.service.xml.XmlUtils;
import com.qc.competition.utils.MathUtils;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.StringUtils;
import com.qc.competition.utils.json.JSONUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Duncan on 04/01/2015.
 */

public class CompetitionDetailsGeneratorTest {

    protected final static String OUTPUT_PATH = System.getenv("outputPath") != null ? System.getenv("outputPath") : System.getProperty("java.io.tmpdir");
    protected static final Integer REGISTRATION_PRICE_IN_CENTS = 500;
    private static final int MAX_NUMBER_OF_PARTICIPANT = 100;
    private static final int MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST = 100;
    private static final int MAX_NUMBER_OF_PARTICIPANT_STEP = 1;
    private static final int MAX_NUMBER_OF_PARALLEL_PLAYS = 20;
    private static final int MAX_NUMBER_OF_PARALLEL_PLAYS_STEP = 1;
    private static final int MAX_HOUR_DURATION = 24;
    private static final int MAX_HOUR_DURATION_STEP = 1;
    public static Duration DEFAULT_COMPETITION_DURATION_FOR_TEST = Duration.ofHours(6);
    public static int DEFAULT_NULMBER_OF_PARTICIPANT_COMPETITION = 64;
    private static final DecimalFormat decimalFormat = new DecimalFormat("###");
    private static final Logger LOGGER = Logger.getLogger(CompetitionDetailsGeneratorTest.class);

    public static CompetitionComputationParam getDefaultCompetitionComputationParamStatic(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration, MergePolicy mergePolicy, ResetPolicy resetPolicy, Boolean thirdPlaceMatchEnabled) {
        return new CompetitionDetailsGeneratorTest().getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, minimumPlayDuration, averagePlayDuration, maximumPlayDuration, mergePolicy, resetPolicy, thirdPlaceMatchEnabled);
    }

    //    boolean changedWinner = false;
    //
    public CompetitionComputationParam getDefaultCompetitionComputationParam(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration) {
        return this.getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, minimumPlayDuration, averagePlayDuration, maximumPlayDuration, getMergePolicy(), getResetPolicy(), getThirdPlaceMatchEnabled());
    }

    public Boolean getThirdPlaceMatchEnabled() {
        return Boolean.FALSE;
    }

    protected MergePolicy getMergePolicy() {
        return MergePolicy.STANDARD;
    }

    protected ResetPolicy getResetPolicy() {
        return ResetPolicy.NONE;
    }

    public CompetitionComputationParam getDefaultCompetitionComputationParam(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration, MergePolicy mergePolicy, ResetPolicy resetPolicy, Boolean thirdPlaceMatchEnabled) {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        competitionComputationParam.competitionDuration = competitionDuration;
        competitionComputationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;
        competitionComputationParam.sharerPercentageLimit = 10;
        competitionComputationParam.participantType = participantType;


        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = new CompetitionCreationParamPhaseFinal();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());
        competitionCreationParamPhaseFinal.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseFinal.participantType = participantType;
        competitionCreationParamPhaseFinal.playVersusType = playVersusType;
        competitionCreationParamPhaseFinal.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseFinal.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseFinal.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseFinal.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseFinal.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseFinal.numberOfPlayMinimum = 5;
        competitionCreationParamPhaseFinal.numberOfPlayMaximum = 7;
        competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantMatch * 8;
        competitionCreationParamPhaseFinal.groupSizeMinimum = participantQualifiedPerMatch + 1;
        competitionCreationParamPhaseFinal.groupSizeFinalEnabled = true;
        competitionCreationParamPhaseFinal.groupSizeFinalThreshold = numberOfParticipantMatch * 2;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum = 7;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum = 9;
        competitionCreationParamPhaseFinal.mergePolicy = mergePolicy;
        competitionCreationParamPhaseFinal.resetPolicy = resetPolicy;
        competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled = getThirdPlaceMatchEnabled();
        competitionCreationParamPhaseFinal.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseFinal.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseFinal.checkAndCorrectValues();

        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);

        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = new CompetitionCreationParamPhaseQualification();
        competitionCreationParamPhaseQualification.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseQualification.tournamentFormatsAccepted.addAll(TournamentFormat.allButElimination());
        competitionCreationParamPhaseQualification.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseQualification.participantType = participantType;
        competitionCreationParamPhaseQualification.playVersusType = playVersusType;
        competitionCreationParamPhaseQualification.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseQualification.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseQualification.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseQualification.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseQualification.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseQualification.numberOfPlayMinimum = 3;
        competitionCreationParamPhaseQualification.numberOfPlayMaximum = 5;
        competitionCreationParamPhaseQualification.groupSizeMaximum = numberOfParticipantMatch * 4;
        competitionCreationParamPhaseQualification.groupSizeMinimum = numberOfParticipantMatch * 2;
        competitionCreationParamPhaseQualification.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseQualification.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseQualification.checkAndCorrectValues();
        competitionComputationParam.setQualificationPhase(competitionCreationParamPhaseQualification);

        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = new CompetitionCreationParamPhaseMixing();
        competitionCreationParamPhaseMixing.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseMixing.tournamentFormatsAccepted.addAll(TournamentFormat.allButElimination());
        competitionCreationParamPhaseMixing.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseMixing.participantType = participantType;
        competitionCreationParamPhaseMixing.playVersusType = playVersusType;
        competitionCreationParamPhaseMixing.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseMixing.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseMixing.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseMixing.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseMixing.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseMixing.numberOfPlayMinimum = 3;
        competitionCreationParamPhaseMixing.numberOfPlayMaximum = 3;
        competitionCreationParamPhaseMixing.numberOfRoundMinimum = 1;
        competitionCreationParamPhaseMixing.numberOfRoundMaximum = 3;
        competitionCreationParamPhaseMixing.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseMixing.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseMixing.checkAndCorrectValues();
        competitionComputationParam.setMixingPhase(competitionCreationParamPhaseMixing);

        competitionComputationParam.getFirstPhase().registrationOnTheFly = registrationOnTheFly();
        competitionComputationParam.checkAndCorrectValues();
        return competitionComputationParam;

    }

    protected Integer getMaximumNumberOfParallelPlay() {
        return 0;
    }

    protected boolean isChangedWinner() {
        return false;
    }

    protected boolean registrationOnTheFly() {
        return false;
    }

    protected CompetitionInstance getCompetitionInstance(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        return getCompetitionInstances(1, numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch).get(0);
    }

    protected List<CompetitionInstance> getCompetitionInstances(CompetitionComputationParam competitionComputationParam, int sizeResult) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        List<CompetitionInstance> competitionInstances = null;
        try {
            competitionInstances = competitionInstanceGeneratorImpl.computeCompetitionFormatFor(competitionComputationParam, sizeResult);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }
        return competitionInstances;
    }


    @Test
    public void testComputationSameResult() {
        List<CompetitionInstance> competitionInstanceResult = new ArrayList<>();
        List<CompetitionInstance> competitionInstances = null;
        CompetitionInstance competitionInstance = null;
        for (int i = 0; i < 10; i++) {
            competitionInstances = getCompetitionInstances(1, DEFAULT_NULMBER_OF_PARTICIPANT_COMPETITION, DEFAULT_COMPETITION_DURATION_FOR_TEST, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
            competitionInstance = competitionInstances.get(0);
            competitionInstance.localId = "1";
            competitionInstanceResult.add(competitionInstances.get(0));
        }
        CompetitionInstanceComparator competitionInstanceComparator = new CompetitionInstanceComparator(DEFAULT_COMPETITION_DURATION_FOR_TEST);
        for (CompetitionInstance competitionInstance1st : competitionInstanceResult) {

            for (CompetitionInstance competitionInstance1st2 : competitionInstanceResult) {
                if (competitionInstanceComparator.compare(competitionInstance1st, competitionInstance1st2) != 0)
                    Assert.fail(competitionInstance1st.getDescriptionTable().toString() + System.lineSeparator() + competitionInstance1st2.getDescriptionTable().toString());
            }

        }


    }

    @Test(dataProvider = "computationParameterFormatProvider")
    private void testComputationRanking(int numberOfParticipantCompetition, int hourDuration) {
        String logPrefix = "[numberOfParticipant:" + numberOfParticipantCompetition + "]\t[duration:" + Duration.ofHours(hourDuration).toString() + "]";

        List<CompetitionInstance> competitionInstances = getCompetitionInstances(1, numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
        if (competitionInstances.isEmpty())
            Assert.assertFalse(competitionInstances.isEmpty(), logPrefix + "\tNo Format Found");
        checkComputationRanking(competitionInstances.get(0), logPrefix);

    }

    protected void writeCartouches(List<CompetitionInstance> competitionInstances) {
        for (CompetitionInstance competitionInstance : competitionInstances) {
            writeCartouche(competitionInstance);
        }
    }

    protected String writeCartouche(CompetitionInstance competitionInstance) {
        Logger logger = LOGGER;
        String logPrefix = "writeCartouche";
        competitionInstance.reset(true);
        competitionInstance.fillWithFakeRegistrations();
        String suffix = null;
        try {
            competitionInstance.initialize();
            competitionInstance.open();
            competitionInstance.launchSimulation(isChangedWinner());
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss");
            suffix = "_" + competitionInstance.localId + "_" + dateFormat.format(new Date());
            String cssContent = Utils.getCssContent();
            String filename = OUTPUT_PATH + "competition_cartouche" + suffix + ".html";
            logger.info(logPrefix + "\t" + filename);
            try {
                competitionInstance.writeCartouche(OUTPUT_PATH + "competition_cartouche" + suffix + ".html", "", cssContent);
            } catch (FileNotFoundException e) {
                logger.error(logPrefix + "\t" + filename, e);
            }
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }

        return suffix;
    }


    @Test(dataProvider = "computationParameterFormatProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantEliminationSimple(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, 1);
    }


    @Test(dataProvider = "computationParameterFormatProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantEliminationDouble(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true)
    public void testComputationRankingParticipantEliminationDouble6hours(int numberOfParticipantCompetition) {
        checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationDouble3() {
        checkComputationRankingElimination(3, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationDouble5() {
        checkComputationRankingElimination(5, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationDouble64() {
        checkComputationRankingElimination(64, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationDouble60() {
        checkComputationRankingElimination(60, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationTriple64() {
        checkComputationRankingElimination(64, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 3, 1);
    }


    @Test
    public void testComputationRankingParticipantEliminationTriple16() {
        checkComputationRankingElimination(16, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 3, 1);
    }

    @Test
    public void testComputationRankingParticipantEliminationTriple60() {
        checkComputationRankingElimination(60, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 3, 1);
    }


    @Test(dataProvider = "computationParameterFormatProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantEliminationTriple(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 3, 1);
    }


    @Test(dataProvider = "computationParameterFormatCorrectNumberOfParticipantProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantEliminationTripleCorrectNumberOfParticipant(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 3, 1);
    }


    @Test(dataProvider = "computationParameterFormatProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantSwiss(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingSwiss(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
    }


    @Test(dataProvider = "computationParameterFormatProvider", threadPoolSize = 10)
    public void testComputationRankingParticipantRoundRobin(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingRoundRobin(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
    }

    @Test
    public void testComputationRankingParticipantRoundRobin3() {
        int numberOfParticipantCompetition = 3;
        int hourDuration = 12;
        boolean withFinal = true;
        checkComputationRankingRoundRobin(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
    }


    @Test
    public void testComputationRankingParticipantRoundRobin10() {
        int numberOfParticipantCompetition = 10;
        int hourDuration = 12;
        boolean withFinal = true;
        checkComputationRankingRoundRobin(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
    }

    protected List<CompetitionInstance> getCompetitionInstances(int sizeResult, int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        List<CompetitionInstance> competitionInstances = getCompetitionInstances(competitionComputationParam, sizeResult);
        return competitionInstances;
    }

    @DataProvider(name = "computationParameterFormatProvider", parallel = true)
    public Object[][] dataProviderComputationParameterFormatProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatProvider]";

        logger.info(logPrefix);
        SortedSet<Integer> numberOfParticipantCompetitions = new TreeSet<>(MathUtils.getPrimeNumbers(1, MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST));
        for (int i = 1; i < 20; i = i + 1) {
            numberOfParticipantCompetitions.add(i);
        }
        for (int i = getCompetitionComputationParamBase().getMixingPhaseParameter().participantQualifiedPerMatch + 1; i < MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST; i = i + 2) {
            numberOfParticipantCompetitions.add(i);
        }
        numberOfParticipantCompetitions = Sets.sort(numberOfParticipantCompetitions);

        List<List<Object>> parameterSetList = new ArrayList<>();
        for (Integer numberOfParticipantCompetition : numberOfParticipantCompetitions) {
            for (int j = 1; j < MAX_HOUR_DURATION; j = j + MAX_HOUR_DURATION_STEP) {
                int hourDuration = j;
//                for (int k = 0; k < 2; k++) {
                List<Object> parameterSet = new ArrayList<>();
                parameterSet.add(numberOfParticipantCompetition);
                parameterSet.add(hourDuration);
//                    parameterSet.add(k % 2 == 0);
                parameterSetList.add(parameterSet);
                logger.info(logPrefix + "\t" + parameterSet);
//                }

            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "computationParameterFormatProviderStupa", parallel = true)
    public Object[][] dataProviderComputationParameterFormatProviderStupa() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatProviderStupa]";

        logger.info(logPrefix);
        List<Integer> numberOfParticipantCompetitions = new ArrayList<>();
        int i = 16;
        numberOfParticipantCompetitions.add(i++);
        numberOfParticipantCompetitions.add(i++);
        numberOfParticipantCompetitions.add(i++);
        numberOfParticipantCompetitions.add(i++);
        numberOfParticipantCompetitions.add(i++);
        Collections.sort(numberOfParticipantCompetitions);

        List<List<Object>> parameterSetList = new ArrayList<>();
        for (Integer numberOfParticipantCompetition : numberOfParticipantCompetitions) {
            int hourDuration = 6;
//                for (int k = 0; k < 2; k++) {
            List<Object> parameterSet = new ArrayList<>();
            parameterSet.add(numberOfParticipantCompetition);
            parameterSet.add(hourDuration);
//                    parameterSet.add(k % 2 == 0);
            parameterSetList.add(parameterSet);
            logger.info(logPrefix + "\t" + parameterSet);
//                }

        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "computationParameterFormatProviderNumberOfParticipants")
    public Object[][] dataProviderComputationParameterFormatProviderNumberOfParticipantsProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatProviderNumberOfParticipants]";

        logger.info(logPrefix);
        List<Integer> numberOfParticipantCompetitions = MathUtils.getPrimeNumbers(1, MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST);
        for (int i = 2; i < MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST; i = i + 2) {
            numberOfParticipantCompetitions.add(i);
        }
        Collections.sort(numberOfParticipantCompetitions);

        List<List<Object>> parameterSetList = new ArrayList<>();
        for (Integer numberOfParticipantCompetition : numberOfParticipantCompetitions) {
            List<Object> parameterSet = new ArrayList<>();
            parameterSet.add(numberOfParticipantCompetition);
            parameterSetList.add(parameterSet);
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "computationParameterFormatCorrectNumberOfParticipantProvider", parallel = true)
    public Object[][] dataProviderComputationParameterFormatCorrectNumberOfParticipantProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            int numberOfParticipantCompetition = (int) Math.pow(getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch, i);
            for (int j = 1; j < MAX_HOUR_DURATION; j = j + MAX_HOUR_DURATION_STEP) {
                int hourDuration = j;
                for (int k = 0; k < 2; k++) {
                    List<Object> parameterSet = new ArrayList<>();
                    parameterSet.add(numberOfParticipantCompetition);
                    parameterSet.add(hourDuration);
                    parameterSet.add(k % 2 == 0);
                    parameterSetList.add(parameterSet);
                    logger.info(logPrefix + "\t" + parameterSet);
                }

            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "computationParameterFormatQuickProvider")
    public Object[][] dataProviderComputationParameterFormatQuickProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatQuickProvider]";
        List<List<Object>> parameterSetList = new ArrayList<>();

        logger.info(logPrefix);
        for (CompetitionDetailsUpdateTest.TestGenerationConfiguration testGenerationConfiguration : CompetitionDetailsUpdateTest.TestGenerationConfiguration.values()) {
            for (int k = 0; k < 2; k++) {
                List<Integer> numberOfParticipantCompetitions = MathUtils.getPrimeNumbers(1, MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST);
                for (int i = 2; i < MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST; i = i + 2) {
                    numberOfParticipantCompetitions.add(i);
                }
                Collections.sort(numberOfParticipantCompetitions);

                for (Integer numberOfParticipantCompetition : numberOfParticipantCompetitions) {
                    for (int j = 1; j < MAX_HOUR_DURATION; j = j + 5) {
                        int hourDuration = j;
                        List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, k % 2 == 1);
                        logger.info(logPrefix + "\t" + parameterSet);
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "numberOfParticipantsQuickDataProvider")
    public Object[][] dataProviderNumberOfParticipantsQuickDataProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[dataProviderNumberOfParticipantsQuickDataProvider]";
        int[] numberOfParticipants = {getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch, 5, 6, 7, 10, 12, 17, 20, 28, 31, 41};
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int numberOfParticipant : numberOfParticipants) {
            List<Object> parameterSet = new ArrayList<>();
            parameterSet.add(numberOfParticipant);
            parameterSetList.add(parameterSet);
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "numberOfParticipantsFullDataProvider")
    public Object[][] dataProviderNumberOfParticipantsFullDataProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[dataProviderNumberOfParticipantsFullDataProvider]";
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int numberOfParticipant = getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch; numberOfParticipant < MAX_NUMBER_OF_PARTICIPANT; numberOfParticipant++) {
            List<Object> parameterSet = new ArrayList<>();
            parameterSet.add(numberOfParticipant);
            parameterSetList.add(parameterSet);
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    private List<Object> addParameterSet(List<List<Object>> parameterSetList, CompetitionDetailsUpdateTest.TestGenerationConfiguration testGenerationConfiguration, int numberOfParticipantCompetition, int hourDuration, boolean doSaveAndReload) {
        List<Object> parameterSet = new ArrayList<>();
        parameterSet.add(testGenerationConfiguration);
        parameterSet.add(numberOfParticipantCompetition);
        parameterSet.add(hourDuration);
        parameterSet.add(doSaveAndReload);
        parameterSetList.add(parameterSet);
        return parameterSet;

    }

    @DataProvider(name = "createAndLaunchCompetitionParameterFormatProvider", parallel = true)
    public Object[][] createAndLaunchCompetitionParameterFormatProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[createAndLaunchCompetitionParameterFormatProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (CompetitionDetailsUpdateTest.TestGenerationConfiguration testGenerationConfiguration : CompetitionDetailsUpdateTest.TestGenerationConfiguration.values()) {
            for (int k = 0; k < 2; k++) {
                for (int i = 2; i < MAX_NUMBER_OF_PARTICIPANT; i = i + MAX_NUMBER_OF_PARTICIPANT_STEP) {
                    int numberOfParticipantCompetition = i;
                    for (int j = 1; j < MAX_HOUR_DURATION; j = j + MAX_HOUR_DURATION_STEP) {
                        int hourDuration = j;
                        List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, k % 2 == 1);
                        logger.info(logPrefix + "\t" + parameterSet);

                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

//
//    @Test
//    public void testComputationRanking8ParticipantElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(8, Duration.ofMinutes(30), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }
//
//    @Test
//    public void testComputationRanking128ParticipantElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(128, DEFAULT_COMPETITION_DURATION_FOR_TEST, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }
//
//
//    @Test
//    public void testComputationRanking1024ParticipantElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(1024, DEFAULT_COMPETITION_DURATION_FOR_TEST, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }
//
//
//    @Test
//    public void testComputationRanking16ParticipantElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(16, Duration.ofMinutes(30), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }
//
//
//    @Test
//    public void testComputationRanking64ParticipantDoubleElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(64, Duration.ofMinutes(300), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }
//
//
//    @Test
//    public void testComputationRankingParticipantDoubleElimination() {
//        SortedSet<ParticipantResult> registererParticipantRankingFinals = checkComputationRankingElimination(32, Duration.ofMinutes(360), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 2, 1);
//
//
//    }


    @DataProvider(name = "computationParameterGameProvider")
    public Object[][] dataProviderComputationParameterGameProvider() {
        Logger logger = LOGGER;


        logger.info("[computationParameterGameProvider]");
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 2; i < MAX_NUMBER_OF_PARTICIPANT; i = i + MAX_NUMBER_OF_PARTICIPANT_STEP) {
            int numberOfParticipantCompetition = i;
            for (int j = 0; j < MAX_NUMBER_OF_PARALLEL_PLAYS; j = j + MAX_NUMBER_OF_PARALLEL_PLAYS_STEP) {
                int parallelGame = j;
                for (int k = 1; k < MAX_HOUR_DURATION; k = k + MAX_NUMBER_OF_PARTICIPANT_STEP) {
                    int durationInHour = k;
                    List<Object> parameterSet = new ArrayList<>();
                    parameterSet.add(numberOfParticipantCompetition);
                    parameterSet.add(parallelGame);
                    parameterSet.add(durationInHour);
                    parameterSetList.add(parameterSet);
                    logger.info("[computationParameterGameProvider]\t" + parameterSet);

                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info("[computationParameterGameProvider]\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }


    @DataProvider(name = "computationParameterProvider")
    public Object[][] dataProviderComputationParameterProvider() {
        Logger logger = LOGGER;


        logger.info("[computationParameterProvider]");
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 2; i < MAX_NUMBER_OF_PARTICIPANT; i = i + MAX_NUMBER_OF_PARTICIPANT_STEP) {
            int numberOfParticipantCompetition = i;
            for (int j = 1; j < MAX_HOUR_DURATION; j = j + MAX_HOUR_DURATION_STEP) {
                int hourDuration = j;
//                for (ParticipantType participantType :ParticipantType.values()) {
                ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
//                  for (int k =2; k < MAX_NUMBER_OF_PARTICIPANT_MATCH; k = k + MAX_NUMBER_OF_PARTICIPANT_MATCH_STEP) {
//                int numberOfParticipantMatch = k;
                int numberOfParticipantMatch = 2;
//                for (PlayVersusType playVersusType:PlayVersusType.values()) {
                PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
                for (int l = 1; l <= numberOfParticipantMatch - 1; l = l + 1) {
                    int participantQualifiedPerMatch = l;
                    if (numberOfParticipantMatch % participantQualifiedPerMatch == 0) {
                        List<Object> parameterSet = new ArrayList<>();
                        parameterSet.add(numberOfParticipantCompetition);
                        parameterSet.add(hourDuration);
                        parameterSet.add(participantType.name());
                        parameterSet.add(numberOfParticipantMatch);
                        parameterSet.add(playVersusType.name());
                        parameterSet.add(participantQualifiedPerMatch);
                        parameterSetList.add(parameterSet);
                        logger.info("[computationParamProvider]\t" + parameterSet);
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info("[computationParametersProvider]\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }


    protected SortedSet<ParticipantResult> checkComputationRankingElimination(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int eliminationLevel, int participantQualifiedPerMatch) {
        Logger logger = LOGGER;
        String logPrefix = "" + numberOfParticipantCompetition + "\t" + competitionDuration.toMinutes() + "\t" + participantType + "\t" + numberOfParticipantMatch + "\t" + playVersusType + "\t" + participantQualifiedPerMatch;
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = getCompetitionInstanceElimination(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, eliminationLevel, participantQualifiedPerMatch);
        } catch (Throwable throwable) {
            Assert.fail(logPrefix, throwable);
        }


        return checkComputationRanking(competitionInstance, logPrefix);
    }

    protected SortedSet<ParticipantResult> checkComputationRanking(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int eliminationLevel, int participantQualifiedPerMatch) {
        Logger logger = LOGGER;
        String logPrefix = "" + numberOfParticipantCompetition + "\t" + competitionDuration.toMinutes() + "\t" + participantType + "\t" + numberOfParticipantMatch + "\t" + playVersusType + "\t" + participantQualifiedPerMatch;

        CompetitionInstance competitionInstance = getCompetitionInstance(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch);

        return checkComputationRanking(competitionInstance, logPrefix);
    }


    protected SortedSet<ParticipantResult> checkComputationRankingSwiss(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        Logger logger = LOGGER;
        String logPrefix = "" + numberOfParticipantCompetition + "\t" + competitionDuration.toMinutes() + "\t" + participantType + "\t" + numberOfParticipantMatch + "\t" + playVersusType + "\t" + participantQualifiedPerMatch;
        CompetitionInstance competitionInstance = getCompetitionInstanceSwiss(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch);

        return checkComputationRanking(competitionInstance, logPrefix);
    }

    @DataProvider(name = "computationParameterFormatManualProvider")
    public Object[][] dataProviderComputationParameterFormatManualProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatManualProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();


        int hourDuration = 6;
        boolean doSaveAndReload = false;
        for (CompetitionDetailsUpdateTest.TestGenerationConfiguration testGenerationConfiguration : CompetitionDetailsUpdateTest.TestGenerationConfiguration.values()) {

            {
                int numberOfParticipantCompetition = getCompetitionComputationParamBase().getFirstPhase().participantQualifiedPerMatch + 1;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch + 1;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch + getCompetitionComputationParamBase().getFirstPhase().participantQualifiedPerMatch + 1 + 1;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                for (int numberOfParticipantCompetition = getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch; numberOfParticipantCompetition < 20; numberOfParticipantCompetition++) {
                    List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                    logger.info(logPrefix + "\t" + parameterSet);
                    parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                    logger.info(logPrefix + "\t" + parameterSet);
                }
            }
            {
                int numberOfParticipantCompetition = 17;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = 27;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = 32;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = 56;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = 63;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }
            {
                int numberOfParticipantCompetition = 99;
                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
                parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, !doSaveAndReload);
                logger.info(logPrefix + "\t" + parameterSet);
            }

//            {
//                int numberOfParticipantCompetition = 500;
//                List<Object> parameterSet = addParameterSet(parameterSetList, testGenerationConfiguration, numberOfParticipantCompetition, hourDuration, doSaveAndReload);
//                logger.info(logPrefix + "\t" + parameterSet);
//            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    protected CompetitionInstance getCompetitionInstanceElimination(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int eliminationLevel, int participantQualifiedPerMatch) {

        Logger logger = LOGGER;
        CompetitionInstanceGenerator competitionInstanceGenerator = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.removeQualificationPhase();
        competitionComputationParam.removeMixingPhase();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.eliminationWithNumberOfBrackets(eliminationLevel));
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGenerator.createCompetitionInstanceEliminationFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }

        return competitionInstance;
    }

    protected CompetitionInstance getCompetitionInstanceSwiss(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {

        Logger logger = LOGGER;
        CompetitionInstanceGenerator competitionInstanceGenerator = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.removeQualificationPhase();
        competitionComputationParam.removeMixingPhase();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
        competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGenerator.createCompetitionInstanceSwissFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }

        return competitionInstance;
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(null, null, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, Duration.ofMinutes(5), Duration.ofMinutes(5), Duration.ofMinutes(5));
        return competitionComputationParam;
    }


    @Test(dataProvider = "computationParameterFormatProvider")

    public void testComputationRankingParticipantRoundRobin1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingRoundRobin(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 1);


    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void testComputationRankingParticipantRoundRobin1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingRoundRobin(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 2);

    }

    protected void checkComputationRankingRoundRobin(PlayVersusType playVersusType, int numberOfParticipantCompetition, int hourDuration, int participantQualifiedPerMatch) {
        if (numberOfParticipantCompetition > participantQualifiedPerMatch) {
            checkComputationRankingRoundRobin(numberOfParticipantCompetition, Duration.ofMinutes(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, playVersusType.numberOfTeam, playVersusType, 2);
        }


    }


    @Test(dataProvider = "computationParameterFormatProvider")
    public void testComputationRankingParticipantElimination1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 1);


    }

    @Test(dataProvider = "computationParameterFormatProvider")

    public void testComputationRankingParticipantElimination1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingElimination(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 2);

    }

    protected void checkComputationRankingElimination(PlayVersusType playVersusType, int numberOfParticipantCompetition, int hourDuration, int participantQualifiedPerMatch) {
        if (numberOfParticipantCompetition > participantQualifiedPerMatch) {
            checkComputationRankingElimination(numberOfParticipantCompetition, Duration.ofMinutes(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, playVersusType.numberOfTeam, playVersusType, 2, 2);
        }


    }

    @Test(dataProvider = "computationParameterFormatProvider")

    public void testComputationRankingParticipantSwiss1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingSwiss(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 1);


    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void testComputationRankingParticipantSwiss1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRankingSwiss(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 2);

    }

    protected void checkComputationRankingSwiss(PlayVersusType playVersusType, int numberOfParticipantCompetition, int hourDuration, int participantQualifiedPerMatch) {
        if (numberOfParticipantCompetition > participantQualifiedPerMatch) {
            checkComputationRankingSwiss(numberOfParticipantCompetition, Duration.ofMinutes(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, playVersusType.numberOfTeam, playVersusType, 2);
        }


    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void testComputationRankingParticipant1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRanking(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 1);


    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void testComputationRankingParticipant1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRanking(PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, numberOfParticipantCompetition, hourDuration, 2);

    }

    public void checkComputationRanking(PlayVersusType playVersusType, int numberOfParticipantCompetition, int hourDuration, int participantQualifiedPerMatch) {
        if (numberOfParticipantCompetition > participantQualifiedPerMatch) {
            checkComputationRanking(numberOfParticipantCompetition, Duration.ofMinutes(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, playVersusType.numberOfTeam, playVersusType, 2, 2);
        }


    }


    protected SortedSet<ParticipantResult> checkComputationRankingRoundRobin(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        Logger logger = LOGGER;
        CompetitionInstance competitionInstance = getCompetitionInstanceRoundRobin(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch);
        String logPrefix = "" + numberOfParticipantCompetition + "\t" + competitionDuration.toMinutes() + "\t" + participantType + "\t" + numberOfParticipantMatch + "\t" + playVersusType + "\t" + participantQualifiedPerMatch;
//        IdGenerator.reset(Participant.CLASS);
//        IdGenerator.reset(ParticipantTeam.CLASS);
//        IdGenerator.reset(ParticipantSingle.CLASS);
//        IdGenerator.reset(ParticipantTeamMember.CLASS);

//        DescriptionTable descriptionTable = competitionInstance.getDescriptionTable();
//        logger.info("DescriptionTable" + System.lineSeparator() + descriptionTable.toString());
//        competitionInstance.reset();

        return checkComputationRanking(competitionInstance, logPrefix);
    }

    CompetitionInstance getCompetitionInstanceRoundRobin(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantPerMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {

        Logger logger = LOGGER;
        CompetitionInstanceGenerator competitionInstanceGenerator = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantPerMatch, playVersusType, participantQualifiedPerMatch, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.setQualificationPhase(null);
        competitionComputationParam.setMixingPhase(null);
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
        competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGenerator.createCompetitionInstanceRoundRobinFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }

//        IdGenerator.reset(Participant.CLASS);
//        IdGenerator.reset(ParticipantTeam.CLASS);
//        IdGenerator.reset(ParticipantSingle.CLASS);
//        IdGenerator.reset(ParticipantTeamMember.CLASS);

//        DescriptionTable descriptionTable = competitionInstance.getDescriptionTable();
//        logger.info("Computation Parameter" + System.lineSeparator() + competitionComputationParam.toSimpleDescription());
//        logger.info("DescriptionTable" + System.lineSeparator() + descriptionTable.toString());
        competitionInstance.reset(true);

        return competitionInstance;
    }


    @Test(dataProvider = "computationParameterFormatProvider")
    public void checkComputationRanking(int numberOfParticipantCompetition, int hourDuration) {
        checkComputationRanking(numberOfParticipantCompetition, Duration.ofHours(hourDuration));
    }

    protected SortedSet<ParticipantResult> checkComputationRanking(int numberOfParticipant, Duration duration) {
        Logger logger = LOGGER;
        CompetitionInstance competitionInstance = getCompetitionInstances(1, numberOfParticipant, duration, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1).get(0);
        DescriptionTable descriptionTable = new DescriptionTable();
        String logPrefix = "[numberOfParticipant:" + numberOfParticipant + "]\t[duration:" + duration.toString() + "]";
        competitionInstance.reset(true);
        return checkComputationRanking(competitionInstance, logPrefix);
    }

    protected SortedSet<ParticipantResult> checkComputationRanking(CompetitionInstance competitionInstance, String logPrefix) {
        Logger logger = LOGGER;
        try {
            competitionInstance.reset(true);
            competitionInstance.fillWithFakeRegistrations();
            competitionInstance.initialize();
            competitionInstance.open();
            competitionInstance.launchSimulation(isChangedWinner());

//            String secondParticipantId = (String) Sets.sort(competitionInstance.participants).toArray()[1];
//            if (!competitionInstance.isClosed()) {
//                saveXML(competitionInstance);
//                writeCartouche(competitionInstance);
//                Assert.assertTrue(competitionInstance.isClosed(), logPrefix + "\tNOT CLOSED");
//            }

            isComputationRankingValid(competitionInstance, logPrefix);

        } catch (Throwable throwable) {
            logger.error(logPrefix, throwable);
            try {
                saveJSON(competitionInstance, "ERROR");
                writeCartouche(competitionInstance);
            } catch (IOException e) {
                logger.error(logPrefix, e);
            } finally {
                Assert.fail(logPrefix, throwable);
            }
        }


        return competitionInstance.participantResults;
    }


    protected boolean isComputationRankingValid(CompetitionInstance competitionInstance, String logPrefix) {
        boolean result = false;
        if (!competitionInstance.isClosed()) {
            Assert.assertTrue(competitionInstance.isClosed(), logPrefix + "\tNOT CLOSED");
        }
        Participant firstParticipant = null;
        for (ParticipantSeat participantSeat : competitionInstance.participantSeats) {
            if (firstParticipant == null || firstParticipant.compareTo(participantSeat.participant) > 0) {
                firstParticipant = participantSeat.participant;

            }
        }
        Integer finalRank = competitionInstance.getParticipantResultForParticipant(firstParticipant).rank;
        if (!isChangedWinner()) {
            if (finalRank.compareTo(1) == 0 || (finalRank.compareTo(2) == 0 && competitionInstance.getCompetitionComputationParam().getFirstPhase().numberOfParticipantMatch == 2 && competitionInstance.participantSeats.size() % 2 == 1)) {
                result = true;
            } else {
                String rankingPreview = "";
                List<ParticipantResult> participantResults = competitionInstance.getRegistererParticipantRankingFinal();
                for (int i = 0; i < finalRank; i++) {
                    if (!rankingPreview.isEmpty()) {
                        rankingPreview += "\n";
                    }
                    rankingPreview += participantResults.get(i).rank + " - " + participantResults.get(i).participantScore.toString() + " - " + participantResults.get(i).participant.toSimpleDescription();
                }
                Assert.assertEquals(finalRank.intValue(), 1, logPrefix + "\tParticipant " + firstParticipant + " not first (" + finalRank + ")" + "\n" + rankingPreview + "\n");
            }
        } else if (competitionInstance.participantResults.isEmpty()) {
            result = false;
            Assert.assertFalse(competitionInstance.participantResults.isEmpty(), logPrefix + "\tNo Result found");
        }
        return result;
    }


    protected boolean isParticipantVersusValid(CompetitionInstance competitionInstance, String logPrefix) {
        boolean result = true;
        for (CompetitionSeed competitionSeed : competitionInstance.competitionSeeds) {
            result = result && isParticipantVersusValid(competitionSeed, logPrefix);
        }
        return result;
    }


    protected boolean isParticipantVersusValid(CompetitionSeed competitionSeed, String logPrefix) {
        boolean result = true;
        for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
            result = result && isParticipantVersusValid(competitionGroup, logPrefix);
        }
        return result;
    }

    protected boolean isParticipantVersusValid(CompetitionGroup competitionGroup, String logPrefix) {
        boolean result = true;
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionGroup.getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionGroup.getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

        int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
        int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
        HashMap<Participant, Set<Participant>> participantAndOpponentsHashMap = new HashMap<>();
        for (CompetitionRound competitionRound : competitionGroup.competitionRounds) {
            for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
                if (competitionMatch.participantPairing != null) {
                    List<Participant> participants = competitionMatch.participantPairing.getRealParticipantsAsArray();
                    for (Participant participant : participants) {
                        if (!participantAndOpponentsHashMap.containsKey(participant))
                            participantAndOpponentsHashMap.put(participant, new HashSet<>());
                        int size = participantAndOpponentsHashMap.get(participant).size();
                        participantAndOpponentsHashMap.get(participant).addAll(participants);
                        int nextSize = participantAndOpponentsHashMap.get(participant).size();
                        if (participants.size() > 1 && nextSize - size < participants.size() / (numberOfParticipantMatch / participantQualifiedPerMatch)) {
                            result = false;
                        }
                    }

                }
            }
        }
        return result;
    }


    @Test
    public void testCompetitionInstanceTree() throws IOException {
        Logger logger = LOGGER;
        CompetitionInstance competitionInstance = getCompetitionInstance(DEFAULT_NULMBER_OF_PARTICIPANT_COMPETITION, DEFAULT_COMPETITION_DURATION_FOR_TEST, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);

        CompetitionInstanceTree competitionInstanceTree = competitionInstance.getCompetitionInstanceTree();
        XmlUtils<CompetitionInstanceTree> xmlUtils = new XmlUtils<>(CompetitionInstanceTree.class);
        String xml = xmlUtils.toString(competitionInstanceTree);
        logger.info(xml);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss");
        String suffix = "_" + dateFormat.format(new Date());
        xmlUtils.toFile(competitionInstanceTree, "test/com/qc/competition/service/competition_tree" + suffix + ".xml");
    }

    @Test(dataProvider = "computationParameterGameProvider")
    public void testMariokart8(int numberOfParticipantCompetition, int maximumNumberOfParallelPlay, int durationInHour) {
        String logPrefix = "[kMariokart8]\t" + numberOfParticipantCompetition + "\t" + maximumNumberOfParallelPlay;
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParamMarioKart8();
        competitionComputationParam.competitionDuration = Duration.ofHours(durationInHour);
        for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
            competitionCreationParamPhase.maximumNumberOfParallelPlay = maximumNumberOfParallelPlay;
        }

        competitionComputationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;


        List<CompetitionInstance> competitionInstances = getCompetitionInstances(competitionComputationParam, 1);
        checkComputationRanking(competitionInstances.get(0), logPrefix);
    }

    protected CompetitionComputationParam getDefaultCompetitionComputationParamMarioKart8() {
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(null, null, ParticipantType.TEAM_ONE_PARTICIPANT, 4, PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT, 2, Duration.ofMinutes(5), Duration.ofMinutes(5), Duration.ofMinutes(5));
        competitionComputationParam.competitionName = "Mariokart 8";
//        
//        competitionComputationParam.minimumPlayDuration = Duration.ofMinutes(5);
//        competitionComputationParam.averagePlayDuration = Duration.ofMinutes(5);
//        competitionComputationParam.maximumPlayDuration = Duration.ofMinutes(5);
//        competitionComputationParam.maximumNumberOfParallelPlay = 0;
//        competitionComputationParam.finalPhase = true;
//        competitionComputationParam.maximumEliminationLevel = 2;
//
//        competitionComputationParam.maxFinalGroupSize = 8;
//        competitionComputationParam.maxNumberOfPlayGlobal = 9;
//        competitionComputationParam.minNumberOfPlayGlobal = 1;
//        competitionComputationParam.numberOfParticipantMatch = 4;
//        competitionComputationParam.numberOfPlayMultiplicator = 1;
//        competitionComputationParam.participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
//        competitionComputationParam.playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
//        competitionComputationParam.sharerPercentageLimit = 10;
//        competitionComputationParam.intermissionDuration = Duration.ofMinutes(5);
//        competitionComputationParam.participantQualifiedPerMatch = 2;

        return competitionComputationParam;
    }

    @Test(dataProvider = "computationParameterGameProvider")
    public void testHearthStone(int numberOfParticipantCompetition, int maximumNumberOfParallelPlay, int durationInHour) {
        String logPrefix = "[HearthStone]\t" + numberOfParticipantCompetition + "\t" + maximumNumberOfParallelPlay;

        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParamHearthStone();
        competitionComputationParam.competitionName = "HearthStone";
        competitionComputationParam.competitionDuration = Duration.ofHours(durationInHour);
        for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
            competitionCreationParamPhase.maximumNumberOfParallelPlay = maximumNumberOfParallelPlay;
        }
        competitionComputationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;


        List<CompetitionInstance> competitionInstances = getCompetitionInstances(competitionComputationParam, 1);
        checkComputationRanking(competitionInstances.get(0), logPrefix);
    }


    protected CompetitionComputationParam getDefaultCompetitionComputationParamHearthStone() {
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(null, null, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.competitionName = "HearthStone";

        return competitionComputationParam;
    }

//    @Deprecated
//    protected CompetitionInstance checkSaveAndReloadXML(CompetitionInstance competitionInstance, String filePrefix, boolean delete) throws IOException {
//        boolean checkSame = false;
//        return checkSaveAndReloadXML(competitionInstance, filePrefix, delete, checkSame);
//    }
//
//    @Deprecated
//    protected CompetitionInstance checkSaveAndReloadXML(CompetitionInstance competitionInstance, String filePrefix, boolean delete, boolean checkSame) throws IOException {
//        Logger logger = LOGGER;
//        CompetitionInstance competitionInstanceNew = null;
//        String filename = null;
//        try {
//            competitionInstance.sortContent();
//            filename = saveXML(competitionInstance, filePrefix);
//            // Thread.sleep(500);
//            competitionInstanceNew = loadXML(filename);
//            Path path = Paths.get(filename);
//            if (checkSame) {
//                // Thread.sleep(500);
//                String content1 = FileUtils.getFileContent(filename);
//                // Thread.sleep(500);
//                if (delete)
//                    Files.delete(path);
//                // Thread.sleep(500);
//                filename = saveXML(competitionInstanceNew, filePrefix);
//                // Thread.sleep(500);
//                String content2 = FileUtils.getFileContent(filename);
//                // Thread.sleep(500);
//                path = Paths.get(filename);
//                if (delete)
//                    Files.delete(path);
//                if (content1.compareTo(content2) != 0)
//                    Assert.assertEquals(content1, content2);
//            } else if (delete)
//                Files.delete(path);
//
//        } catch (IOException e) {
//            logger.error("file:" + filename, e);
//            Assert.fail(filename, e);
//            throw e;
//
////        } catch (InterruptedException e) {
////            e.printStackinfo();
//        }
//        return competitionInstanceNew;
//
//    }

//    @Deprecated
//    protected CompetitionInstance saveAndReloadXML(CompetitionInstance competitionInstance, String filePrefix, boolean delete) {
//        Logger logger = LOGGER;
//        CompetitionInstance competitionInstanceNew = null;
//        String filename = null;
//        try {
//            filename = saveXML(competitionInstance, filePrefix);
//            competitionInstanceNew = loadXML(filename);
//            if (delete) {
//                Path path = Paths.get(filename);
//                Files.delete(path);
//            }
//        } catch (IOException e) {
//            logger.error("file:" + filename, e);
//            Assert.fail(filename, e);
//        }
//        return competitionInstanceNew;
//
//    }
//
//    @Deprecated
//    protected CompetitionInstance loadXML(String filename) throws IOException {
//        Logger logger = LOGGER;
//        CompetitionInstance competitionInstanceNew = null;
//        XmlUtils<CompetitionInstance> xmlUtils = new XmlUtils<>(CompetitionInstance.class);
//
//        try {
//            competitionInstanceNew = xmlUtils.fromFile(filename);
//        } catch (IOException e) {
//            logger.error("file:" + filename, e);
//            Assert.fail(filename, e);
//            throw e;
//        }
//        return competitionInstanceNew;
//    }
//
//    @Deprecated
//    protected String saveXML(CompetitionInstance competitionInstance, String filePrefix) throws IOException {
//        Logger logger = LOGGER;
//        XmlUtils<CompetitionInstance> xmlUtils = new XmlUtils<>(CompetitionInstance.class);
//        String filename = null;
//        String logPrefix = "saveXML\t";
//        try {
//            boolean withTimestamp = false;
//            filename = OUTPUT_PATH + filePrefix + competitionInstance.getFilePrefix(withTimestamp, true) + ".xml";
//            logger.info(logPrefix + filename);
//            logger.info(logPrefix + xmlUtils.toFile(competitionInstance, filename).getAbsolutePath());
//        } catch (IOException e) {
//            logger.error(logPrefix + "file:" + filename, e);
//            Assert.fail(filename, e);
//            throw e;
//        }
//        return filename;
//    }


    protected CompetitionInstance saveAndReloadJSON(CompetitionInstance competitionInstance, String filePrefix, boolean delete) throws IOException {
        Logger logger = LOGGER;
        CompetitionInstance competitionInstanceNew = null;
        String filename = null;
        try {
            filename = saveJSON(competitionInstance, filePrefix);
            competitionInstanceNew = loadJSON(filename);
            if (delete) {
                Path path = Paths.get(filename);
                Files.delete(path);
            }
        } catch (IOException e) {
            logger.error("file:" + filename, e);
            Assert.fail(filename, e);
            throw e;
        }
        return competitionInstanceNew;

    }

    protected CompetitionInstance loadJSON(String filename) throws IOException {
        Logger logger = LOGGER;
        CompetitionInstance competitionInstanceNew = null;
        try {
            competitionInstanceNew = JSONUtils.fileToJSONObject(filename, CompetitionInstance.class);
        } catch (IOException e) {
            logger.error("file:" + filename, e);
            Assert.fail(filename, e);
            throw e;
        }
        return competitionInstanceNew;
    }


    protected String saveJSON(CompetitionInstance competitionInstance, String filePrefix) throws IOException {
        Logger logger = LOGGER;
        String filename = null;
        String logPrefix = "saveJSON\t";
        try {
            boolean withTimestamp = false;
            filename = OUTPUT_PATH + filePrefix + competitionInstance.getFilePrefix(withTimestamp, true) + ".json";
            logger.info(logPrefix + filename);
            logger.info(logPrefix + JSONUtils.jsonObjectToFile(competitionInstance, filename).getAbsolutePath());
        } catch (IOException e) {
            logger.error(logPrefix + "file:" + filename, e);
            Assert.fail(filename, e);
            throw e;
        }
        return filename;
    }

    protected CompetitionInstance doSaveAndReload(String logPrefix, CompetitionInstance competitionInstance, String filePrefix) throws IOException {
        Logger logger = LOGGER;
        logger.info(logPrefix + "\t" + "[SAVE_RELOAD]");
        CompetitionInstance competitionInstanceJSON = saveAndReloadJSON(competitionInstance, filePrefix, false);
        competitionInstance = competitionInstanceJSON;
//        CompetitionInstance competitionInstanceXML = checkSaveAndReloadXML(competitionInstance, filePrefix, false);
//        competitionInstance = competitionInstanceXML;
        return competitionInstance;
    }

    protected void writeCartouche(CompetitionInstance competitionInstance, String logPrefix, String filePrefix, boolean withTimestamp) {
        Logger logger = LOGGER;
        String filename = null;
        try {

            String cssContent = Utils.getCssContent();
            filename = OUTPUT_PATH + filePrefix + this.getClass().getSimpleName() + "_" + competitionInstance.getFilePrefix(withTimestamp, true) + ".html";
            logger.info(logPrefix + "\t" + filename);
            competitionInstance.writeCartouche(filename, null, cssContent);
        } catch (FileNotFoundException e) {
            logger.error(logPrefix + "\t" + "file:" + filename, e);
            Assert.fail(filename, e);
        }

    }

    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createCompetitionForSizeAndDurationQuick(Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        createCompetitionForSizeAndDuration(participantSize, durationInHour, doSaveAndReload);
    }

    @Test
    public void createCompetitionForSizeAndDuration500() {
        createCompetitionForSizeAndDuration(500, 12, false);
    }


    protected CompetitionInstance createCompetitionWithParam(CompetitionComputationParam competitionComputationParam) {
        Logger logger = LOGGER;
        String logPrefix = competitionComputationParam.toString();
        logger.info(logPrefix + "\t" + "[START]");
        CompetitionInstance competitionInstance = null;
        try {

            CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();


            logger.info(logPrefix + "\t" + "[COMPUTE_FORMAT]");
            competitionInstance = competitionInstanceGeneratorImpl.computeCompetitionFormatFor(competitionComputationParam);
            logger.info(logPrefix + "\t" + "[END]");
        } catch (Throwable t) {
            writeCartouche(competitionInstance, logPrefix, "ERROR", true);
            Assert.fail(logPrefix, t);
        }
        return competitionInstance;
    }


    protected void createAndLaunchCompetitionWithParam(CompetitionComputationParam competitionComputationParam, boolean doSaveAndReload) {

        Logger logger = LOGGER;
        String logPrefix = competitionComputationParam.toString();
        logger.info(logPrefix + "\t" + "[START]");
        CompetitionInstance competitionInstance = null;
        try {

            CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
            competitionInstanceGeneratorImpl.setTest(testOnEveryGeneration());


            logger.info(logPrefix + "\t" + "[COMPUTE_FORMAT]");
//            competitionComputationParam.checkAndCorrectValues();
            competitionInstance = competitionInstanceGeneratorImpl.computeCompetitionFormatFor(competitionComputationParam);
            competitionInstance.internationalizedLabel.defaultLabel = competitionComputationParam.numberOfParticipantCompetition + " Participants / " + competitionComputationParam.participantType + " / numberOfParticipantMatch =" + competitionComputationParam.getFirstPhase().numberOfParticipantMatch + " / " + competitionComputationParam.getFirstPhase().playVersusType.name();
//        CompetitionInstance competitionInstance = competitionInstances.get(0);
            String recapTable = competitionInstance.getDescriptionTable().toString();


//        logger.info("Competition Format Found :\n" + competitionInstance.toDescription());
            int index = 0;
            if (doSaveAndReload) {
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index++ + "_FIRST_");
            }
            logger.info(logPrefix + "\t" + "[RESET]");
            competitionInstance.reset(true);
            if (doSaveAndReload) {
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index++ + "_RESET_");
            }
            int divisor = 1;
            if (competitionComputationParam.getFirstPhase().registrationOnTheFly)
                divisor = 2;
            logger.info(logPrefix + "\t" + "[REGISTRATIONS]");
            for (int i = 0; i < competitionComputationParam.numberOfParticipantCompetition / divisor; i++) {
                Participant participant = competitionComputationParam.participantType.createParticipant(competitionInstance);
                participant.internationalizedLabel.defaultLabel = "Participant " + StringUtils.numberWithTrailingZeros(i + 1, 3);
                int threshold = competitionComputationParam.numberOfParticipantCompetition / competitionInstance.getCompetitionComputationParam().getFirstPhase().numberOfParticipantMatch;
                Integer seedRanking = i >= threshold ? i + 1 : null;
                competitionInstance.subscribe(participant, seedRanking);
                logger.info(logPrefix + "\t" + "[REGISTRATIONS]\t" + (i + 1) + "/" + competitionComputationParam.numberOfParticipantCompetition + "\t" + participant);
                if (doSaveAndReload) {
                    competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index++ + "_REGISTRATIONS_");
                }
            }
            if (doSaveAndReload) {
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index++ + "_REGISTRATIONS_DONE_");
            }
            logger.info(logPrefix + "\t" + "[INITIALIZE]");
            competitionInstance.initialize();
            writeCartouche(competitionInstance, logPrefix, index++ + "_READY_", true);
            if (doSaveAndReload) {
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index++ + "_STARTING_");
            }
            logger.info(logPrefix + "\t" + "[STARTED]");
            competitionInstance.open();
            if (doSaveAndReload) {
                writeCartouche(competitionInstance, logPrefix, index + "_STARTED_", true);
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index + "_STARTED_");
                index++;
            }

            // logger.info( "competitionInstance.toSimpleDescriptionXmlStringOutput:\n" + competitionInstance.toSimpleDescriptionXmlStringOutput());
            //  logger.info( "competitionInstance.toSimpleDescriptionXmlStringOutput:\n" + competitionInstance.toSimpleDescriptionXmlStringOutput());

//        if (doSaveAndReload) competitionInstance = saveAndReloadXML(competitionInstance);
//        if (doSaveAndReload) competitionInstance = saveAndReloadXML(competitionInstance);
            if (doSaveAndReload) {
                boolean changeWinner = isChangedWinner();
                int matchPerWave = 1;
                boolean generation = false;
                while (competitionInstance.launchSimulationCurrentWave(changeWinner, generation, matchPerWave)) {
                    logger.info(logPrefix + "\t" + "[WAVE_" + competitionInstance.getCurrentWave() + "]");
                    competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index + "_STARTED_");
                    writeCartouche(competitionInstance, logPrefix, index + "_STARTED_", true);
                    index++;
                }
            } else {
                boolean changeWinner = isChangedWinner();
                boolean generation = false;
                competitionInstance.launchSimulationFull(generation, changeWinner);
            }

            //   competitionInstance.fillCompetitionParticipantResult();
            if (doSaveAndReload) {
                writeCartouche(competitionInstance, logPrefix, index + "_OVER_", true);
                competitionInstance = doSaveAndReload(logPrefix, competitionInstance, index + "_OVER_");

                index++;
            }

            logger.info(logPrefix + "\t" + competitionInstance.toString());
            StringBuilder competitionPlaySummary = new StringBuilder();
            SortedSet<CompetitionPlay> competitionPlays = competitionInstance.getCompetitionPlays();
            for (CompetitionPlay competitionPlay : competitionPlays) {
                competitionPlaySummary.append(competitionPlay.toString()).append(System.lineSeparator());
            }
            logger.info(logPrefix + "\t" + "Competition Group Plays Summary List:");
            logger.info(logPrefix + "\t" + competitionPlaySummary.toString());
            logger.info(logPrefix + "\t" + "competitionInstance.participantCompetitionResults:");
            StringBuilder participantCompetitionResultsSummary = new StringBuilder();
            for (ParticipantResult participantResult : competitionInstance.participantResults) {
                participantCompetitionResultsSummary.append(JSONUtils.jsonObjectToString(participantResult, true)).append(System.lineSeparator());
            }
            logger.info(logPrefix + "\t" + participantCompetitionResultsSummary.toString());
            logger.info(logPrefix + "\t" + Duration.ofMinutes(competitionInstance.getExpectedGlobalDuration().avg).toString());
            logger.info(logPrefix + "\t" + "recapTable" + "\n" + recapTable);
            writeCartouche(competitionInstance, logPrefix, index + "_ENDED_", true);
            isComputationRankingValid(competitionInstance, logPrefix);
            isParticipantVersusValid(competitionInstance, logPrefix);
            checkDistribution(competitionInstance, logPrefix);
            logger.info(logPrefix + "\t" + "[END]");
        } catch (Throwable t) {
            if (competitionInstance != null)
                writeCartouche(competitionInstance, logPrefix, "ERROR", true);
            Assert.fail(logPrefix, t);
        }
    }


    private boolean testOnEveryGeneration() {
        return false;
    }

    private void checkDistribution(CompetitionInstance competitionInstance, String logPrefix) {
        if (competitionInstance.potValueInCents != null) {
            if (competitionInstance.getDistributedPotInCents().compareTo(competitionInstance.potValueInCents) != 0) {
                Assert.assertEquals(competitionInstance.getDistributedPotInCents(), competitionInstance.potValueInCents, "Distribution Fails");
            }
            Integer previousPercent = null;
            Integer currentPercent = null;
            for (ParticipantResult participantResult : competitionInstance.participantResults) {
                currentPercent = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT).computeNumberValue().intValue();
                if (previousPercent != null && currentPercent.compareTo(previousPercent) > 0) {
                    Assert.assertTrue(currentPercent.compareTo(previousPercent) > 0, "Distribution unbalanced for currentPercent:" + currentPercent + " with previousPercent:" + previousPercent);
                }
                previousPercent = currentPercent;
            }
            Integer previousPrize = null;
            Integer currentPrize = null;
            for (ParticipantResult participantResult : competitionInstance.participantResults) {
                currentPrize = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE).computeNumberValue().intValue();
                if (previousPrize != null && currentPrize.compareTo(previousPrize) > 0) {
                    Assert.assertTrue(currentPrize.compareTo(previousPrize) > 0, "Distribution unbalanced for currentPrize:" + currentPrize + " with previousPrize:" + previousPrize);
                }

                previousPrize = currentPrize;
            }
        }
    }


    @Test(dataProvider = "computationParameterFormatProvider")
    public void createCompetitionForSizeAndDuration(Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        Logger logger = LOGGER;
        Duration duration = Duration.ofHours(durationInHour);
        String logPrefix = "[participantSize:" + participantSize + "[duration:" + duration + "]";
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.competitionDuration = duration;
        competitionComputationParam.numberOfParticipantCompetition = participantSize;
        try {
            createCompetitionWithParam(competitionComputationParam);
        } catch (Exception e) {
            logger.error(logPrefix, e);
            Assert.fail(logPrefix, e);
        }


    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 5000)
    public void testComputationRankingParticipantEliminationSimpleForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));

        competitionComputationParam.removeMixingPhase();
        competitionComputationParam.removeQualificationPhase();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
        competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);

        competitionComputationParam.checkAndCorrectValues();
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(isElimination(competitionInstance, 1));
    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 5000)
    public void testComputationRankingParticipantEliminationDoubleForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
            competitionComputationParam.removeQualificationPhase();
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
            competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
//            if (competitionComputationParam.minimumEliminationLevel == 2) {
            Assert.assertTrue(isElimination(competitionInstance, 2));
//            } else {
//                Assert.assertTrue(numberOfParticipantCompetition < competitionComputationParam.participantQualifiedPerMatch * 2);
//            }
        }
    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 5000)
    public void testComputationRankingParticipantEliminationTripleForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
            competitionComputationParam.removeQualificationPhase();
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.TRIPLE_ELIMINATION);
            competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
//            if (competitionComputationParam.minimumEliminationLevel == 2) {
            Assert.assertTrue(isElimination(competitionInstance, 3));
//            } else {
//                Assert.assertTrue(numberOfParticipantCompetition < competitionComputationParam.participantQualifiedPerMatch * 2);
//            }
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 5000)
    public void testComputationRankingParticipantEliminationQuadrupleForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
            competitionComputationParam.removeQualificationPhase();
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.QUADRUPLE_ELIMINATION);
            competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
//            if (competitionComputationParam.minimumEliminationLevel == 2) {
            Assert.assertTrue(isElimination(competitionInstance, 4));
//            } else {
//                Assert.assertTrue(numberOfParticipantCompetition < competitionComputationParam.participantQualifiedPerMatch * 2);
//            }
        }
    }

    //
//    @Test
//    public void testComputationRankingParticipantEliminationDoubleForced129() {
//        int numberOfParticipantCompetition = 129;
//        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
//        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
//        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
//            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
//            competitionComputationParam.removeMixingPhase();
//            competitionComputationParam.removeQualificationPhase();
//            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
//            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
//            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
//            competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
//            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
//            competitionComputationParam.checkAndCorrectValues();
//            CompetitionInstance competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
////            if (competitionComputationParam.minimumEliminationLevel == 2) {
//            Assert.assertTrue(isElimination(competitionInstance, 2));
////            } else {
////                Assert.assertTrue(numberOfParticipantCompetition < competitionComputationParam.participantQualifiedPerMatch * 2);
////            }
//        }
//    }
//
//
//    @Test
//    public void testComputationRankingParticipantEliminationDoubleForced7() {
//        int numberOfParticipantCompetition = 7;
//        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
//        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
//        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
//            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1);
//            competitionComputationParam.qualificationPhase = false;
//            competitionComputationParam.mixingPhase = false;
//            competitionComputationParam.finalPhaseTournamentFormatsAccepted.clear();
//            competitionComputationParam.finalPhaseTournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
//            competitionComputationParam.checkAndCorrectValues();
//            CompetitionInstance competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
//            Assert.assertTrue(isElimination(competitionInstance, 2));
//        }
//    }
//
    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantRoundRobinForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.removeQualificationPhase();
        competitionComputationParam.removeMixingPhase();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
        competitionComputationParam.checkAndCorrectValues();
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(isRoundRobin(competitionInstance));
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 5000)
    public void testComputationRankingParticipantSwissForced(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        competitionComputationParam.removeQualificationPhase();
        competitionComputationParam.removeMixingPhase();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
        competitionComputationParam.checkAndCorrectValues();
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(isSwiss(competitionInstance));
    }

    private boolean isElimination(CompetitionInstance competitionInstance, int eliminationLevelExpected) {
        boolean result = true;
        int eliminationLevel = 0;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            int eliminationLevelCurrent = 0;
            if (competitionSeed.getCompetitionGroups() != null) {
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                competitionGroups = Sets.sort(competitionGroups);
                for (CompetitionGroup competitionGroup : competitionGroups) {
                    if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) != 0) {
                        if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
                            result = false;
                            break;
                        } else {
                            eliminationLevelCurrent++;
                        }
                    }
                }
            }
            if (eliminationLevelCurrent > eliminationLevel)
                eliminationLevel = eliminationLevelCurrent;
        }
        if (eliminationLevel != eliminationLevelExpected) {
            result = false;
        }
        return result;
    }

    private boolean isRoundRobin(CompetitionInstance competitionInstance) {
        boolean result = true;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null) {
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                competitionGroups = Sets.sort(competitionGroups);
                for (CompetitionGroup competitionGroup : competitionGroups) {
                    if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) != 0) {
                        if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) != 0) {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isSwiss(CompetitionInstance competitionInstance) {
        boolean result = true;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null) {
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) != 0) {
                        if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) != 0) {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantMixingQualificationFinal(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            int numberOfParticipantMatch = 2;
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, numberOfParticipantMatch, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = competitionComputationParam.getMixingPhaseParameter();
            competitionCreationParamPhaseMixing.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseMixing.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            competitionCreationParamPhaseMixing.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
            competitionComputationParam.setMixingPhase(competitionCreationParamPhaseMixing);

            CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionComputationParam.getQualificationPhaseParameter();
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
            competitionComputationParam.setQualificationPhase(competitionCreationParamPhaseQualification);


            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);

            competitionComputationParam.checkAndCorrectValues();
//            if (numberOfParticipantCompetition > numberOfParticipantMatch)
//                Assert.assertNotNull(competitionComputationParam.getMixingPhaseParameter(), "Mixing Phase should be requested");
//            else
//                Assert.assertNull(competitionComputationParam.getMixingPhaseParameter(), "Mixing Phase should NOT be requested");
//            if (competitionComputationParam.minGroupSize < numberOfParticipantCompetition) {
//                Assert.assertTrue(competitionComputationParam.qualificationPhase, "Qualification Phase should be requested");
//            } else {
//                Assert.assertFalse(competitionComputationParam.qualificationPhase, "Qualification Phase should NOT be requested");
//            }
//            Assert.assertTrue(competitionComputationParam.finalPhase, "Final Phase should be requested");
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
//            int eliminationLevel = 2;
            checkGeneration(logContext, competitionInstance, competitionComputationParam);

        }
    }

    private void checkGeneration(String logContext, CompetitionInstance competitionInstance, CompetitionComputationParam competitionComputationParam) {

        LOGGER.info(logContext + "\tmixingPhaseRequested:" + competitionComputationParam.getMixingPhaseParameter() + "\thasMixingPhase(competitionInstance):" + hasMixingPhase(competitionInstance));
        LOGGER.info(logContext + "\tqualificationPhaseRequested:" + competitionComputationParam.getQualificationPhaseParameter() + "\thasQualificationPhase(competitionInstance):" + hasQualificationPhase(competitionInstance));
        LOGGER.info(logContext + "\tfinalPhaseRequested:" + competitionComputationParam.getFinalPhaseParameter() + "\tgetFinalEliminationLevel(competitionInstance):" + getFinalEliminationLevel(competitionInstance));
        Assert.assertEquals(competitionComputationParam.getMixingPhaseParameter() == null, !hasMixingPhase(competitionInstance), "mixing phase not expected");
        Assert.assertEquals(competitionComputationParam.getMixingPhaseParameter() != null, hasMixingPhase(competitionInstance), "mixing phase expected");
        Assert.assertEquals(competitionComputationParam.getQualificationPhaseParameter() == null, !hasQualificationPhase(competitionInstance), "qualification phase not expected");
        Assert.assertEquals(competitionComputationParam.getQualificationPhaseParameter() != null, hasQualificationPhase(competitionInstance), "qualification phase expected");
        Assert.assertEquals(competitionComputationParam.getFinalPhaseParameter() == null, !hasFinalGroup(competitionInstance, null), "final phase not expected");
        Assert.assertEquals(competitionComputationParam.getFinalPhaseParameter() != null, hasFinalGroup(competitionInstance, null), "final phase expected");
        if (competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.size() == 1) {
            TournamentFormat tournamentFormat = competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.iterator().next();
            int eliminationLevelExpected = 0;
            switch (tournamentFormat) {
                case SWISS:
                    Assert.assertEquals(competitionComputationParam.getFinalPhaseParameter() == null, !hasFinalGroup(competitionInstance, CompetitionGroupFormat.SWISS), "final phase " + CompetitionGroupFormat.SWISS + " expected");
                    break;
                case ROUND_ROBIN:
                    Assert.assertEquals(competitionComputationParam.getFinalPhaseParameter() == null, !hasFinalGroup(competitionInstance, CompetitionGroupFormat.ROUND_ROBIN), "final phase " + CompetitionGroupFormat.ROUND_ROBIN + " expected");
                    break;
                case SINGLE_ELIMINATION:
                case ANY_ELIMINATION:
                    eliminationLevelExpected = 1;
                case DOUBLE_ELIMINATION:
                    eliminationLevelExpected = 2;
                case TRIPLE_ELIMINATION:
                    eliminationLevelExpected = 3;
                case QUADRUPLE_ELIMINATION:
                    eliminationLevelExpected = 4;
                    Assert.assertTrue(hasFinalGroup(competitionInstance, CompetitionGroupFormat.ELIMINATION), "final phase " + CompetitionGroupFormat.ELIMINATION + " expected");
                    Assert.assertEquals(getFinalEliminationLevel(competitionInstance), eliminationLevelExpected, "final phase " + CompetitionGroupFormat.ELIMINATION + " level  " + eliminationLevelExpected + " expected");
                    break;

            }
            Assert.assertEquals(competitionComputationParam.getFinalPhaseParameter() != null, hasFinalGroup(competitionInstance, CompetitionGroupFormat.ELIMINATION), "final phase expected");

        }
    }


    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantQualificationFinal(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            int numberOfParticipantMatch = 2;
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, numberOfParticipantMatch, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();

            CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionComputationParam.getQualificationPhaseParameter();
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
            competitionComputationParam.setQualificationPhase(competitionCreationParamPhaseQualification);


            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);

            competitionComputationParam.checkAndCorrectValues();
//            if (numberOfParticipantCompetition > numberOfParticipantMatch)
//                Assert.assertNotNull(competitionComputationParam.getMixingPhaseParameter(), "Mixing Phase should be requested");
//            else
//                Assert.assertNull(competitionComputationParam.getMixingPhaseParameter(), "Mixing Phase should NOT be requested");
//            if (competitionComputationParam.minGroupSize < numberOfParticipantCompetition) {
//                Assert.assertTrue(competitionComputationParam.qualificationPhase, "Qualification Phase should be requested");
//            } else {
//                Assert.assertFalse(competitionComputationParam.qualificationPhase, "Qualification Phase should NOT be requested");
//            }
//            Assert.assertTrue(competitionComputationParam.finalPhase, "Final Phase should be requested");
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
//            int eliminationLevel = 2;
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true /*,timeOut = 50000*/)
    public void testComputationRankingParticipantFinal(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
            competitionComputationParam.removeQualificationPhase();
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true)
    public void testComputationRankingParticipantMixingFinal(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
//            competitionComputationParam.removeQualificationPhase();
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantQualification(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeMixingPhase();
//            competitionComputationParam.removeQualificationPhase();
            competitionComputationParam.removeFinalPhase();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true)
    public void testComputationRankingParticipantMixingQualification(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
//            competitionComputationParam.removeMixingPhase();
//            competitionComputationParam.removeQualificationPhase();
            competitionComputationParam.removeFinalPhase();
            competitionComputationParam.checkAndCorrectValues();
            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    @Test(dataProvider = "computationParameterFormatProviderNumberOfParticipants", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantMixing(int numberOfParticipantCompetition) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        String logContext = "[numberOfParticipantCompetition:" + numberOfParticipantCompetition + "]";
        LOGGER.info(logContext);
        if (playVersusType.numberOfTeam < numberOfParticipantCompetition) {
            CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, Duration.ofHours(6), ParticipantType.TEAM_ONE_PARTICIPANT, 2, playVersusType, 1, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
            competitionComputationParam.removeFinalPhase();
            competitionComputationParam.removeQualificationPhase();
            competitionComputationParam.checkAndCorrectValues();

            CompetitionInstance competitionInstance = null;
            try {
                competitionInstance = competitionInstanceGeneratorImpl.createCompetitionInstanceFor(competitionComputationParam);
            } catch (CompetitionInstanceGeneratorException e) {
                e.printStackTrace();
            }
            checkGeneration(logContext, competitionInstance, competitionComputationParam);
        }
    }

    public boolean hasMixingPhase(CompetitionInstance competitionInstance) {
        return hasMixingPhase(competitionInstance, true);
    }

    public boolean hasMixingPhase(CompetitionInstance competitionInstance, boolean mandatoryNextPhase) {
        boolean mixingPhase = false;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null && !competitionSeed.getCompetitionGroups().isEmpty()) {
                mixingPhase = true;
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    if ((competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0 || competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0)
                            && (!mandatoryNextPhase
                            || (competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext() != null && (competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().participantFilteringMethod.compareTo(ParticipantFilteringMethod.ALL) == 0
                            || ((competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().participantFilteringMethod.compareTo(ParticipantFilteringMethod.HIGH_PASS) == 0 || competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().participantFilteringMethod.compareTo(ParticipantFilteringMethod.LOW_PASS) == 0)
                            && ((competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringUnit.compareTo(Unit.PERCENTAGE) == 0 && competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringValue == 100)
                            || (competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringUnit.compareTo(Unit.ABSOLUTE) == 0 && competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringValue >= competitionInstance.getCompetitionComputationParam().numberOfParticipantCompetition)
                    )))))) {
                    } else {
                        mixingPhase = false;
                        break;
                    }
                }
            }
            if (mixingPhase)
                break;
        }
        return mixingPhase;
    }

    public boolean hasQualificationPhase(CompetitionInstance competitionInstance) {
        boolean withFinalStepAfterMandatory = true;
        boolean withMixingStepBeforeMandatory = false;
        return hasQualificationOrGroupPhase(competitionInstance, withMixingStepBeforeMandatory);
    }

    public boolean hasQualificationOrGroupPhase(CompetitionInstance competitionInstance, boolean withMixingStepBeforeMandatory) {
        boolean qualificationPhase = false;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null && !competitionSeed.getCompetitionGroups().isEmpty()) {
                qualificationPhase = true;
                SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    if (
                            (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0 || competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) &&
                                    (competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext() != null && ((competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().participantFilteringMethod.compareTo(ParticipantFilteringMethod.HIGH_PASS) == 0 || competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().participantFilteringMethod.compareTo(ParticipantFilteringMethod.LOW_PASS) == 0)
                                            && ((competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringUnit.compareTo(Unit.PERCENTAGE) == 0 && competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringValue != 100)
                                            || (competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringUnit.compareTo(Unit.ABSOLUTE) == 0 && competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().filteringValue != competitionInstance.getCompetitionComputationParam().numberOfParticipantCompetition)
                                    )))
                                    && (!withMixingStepBeforeMandatory || (competitionGroup.getCompetitionSeed().getCompetitionGroupResultsPrevious() != null && !competitionGroup.getCompetitionSeed().getCompetitionGroupResultsPrevious().isEmpty()
                                    && competitionGroup.getCompetitionSeed().getCompetitionGroupResultsPrevious().iterator().next().getCompetitionGroup() != null
                                    && competitionGroup.getCompetitionSeed().getCompetitionGroupResultsPrevious().iterator().next().getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) != 0 && competitionGroup.getCompetitionSeed().participantFilteringMethod.compareTo(ParticipantFilteringMethod.ALL) == 0))
                    ) {
                    } else {
                        qualificationPhase = false;
                        break;
                    }
                }
            }
            if (qualificationPhase)
                break;
        }
        return qualificationPhase;
    }

    public boolean hasFinalElimination(CompetitionInstance competitionInstance, int eliminationLevelExpected) {
        boolean result = false;
        int eliminationLevel = getFinalEliminationLevel(competitionInstance);
        result = eliminationLevel == eliminationLevelExpected;
        return result;
    }

    public int getFinalEliminationLevel(CompetitionInstance competitionInstance) {
        int eliminationDepth = 0;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null) {
                if (competitionSeed.getCompetitionGroups().size() == 1) {
                    SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                    competitionGroups = Sets.sort(competitionGroups);

                    if (competitionGroups.first().getCompetitionGroupResult().getCompetitionSeedNext() == null) {
                        eliminationDepth = getEliminationDepth(competitionSeed);
                        break;

                    }
                }
            }
        }
        return eliminationDepth;
    }

    public boolean hasFinalGroup(CompetitionInstance competitionInstance, CompetitionGroupFormat competitionGroupFormat) {
        boolean result = false;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null) {
                if (competitionSeed.getCompetitionGroups().size() == 1) {
                    SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                    competitionGroups = Sets.sort(competitionGroups);

                    if (competitionGroups.first().getCompetitionGroupResult().getCompetitionSeedNext() == null) {
//                        int eliminationDepth = getEliminationDepth(competitionSeed);
                        if (competitionGroupFormat == null || competitionGroups.first().competitionGroupFormat.compareTo(competitionGroupFormat) == 0) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public int getEliminationDepth(CompetitionSeed competitionSeed) {
        int eliminationDepth = 0;
        SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
        competitionGroups = Sets.sort(competitionGroups);

        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                eliminationDepth = 1;

                Set<CompetitionSeed> competitionSeedPreviouses = new HashSet<>();
                for (CompetitionGroupResult competitionGroupResult : competitionSeed.getCompetitionGroupResultsPrevious()) {
                    if (competitionGroupResult.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                        competitionSeedPreviouses.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
                    }
                }

                List<Integer> subDepths = new ArrayList<>();

                for (CompetitionSeed competitionSeedPrevious : competitionSeedPreviouses) {
                    subDepths.add(getEliminationDepth(competitionSeedPrevious));
                }
                Integer subDepthMax = 0;
                for (Integer subDepth : subDepths) {
                    subDepthMax = Math.max(subDepth, subDepthMax);
                }
                eliminationDepth += subDepthMax;
            }
        }
        return eliminationDepth;
    }


    public int getRemainingNumberOfParticipantBeforeFinal(CompetitionInstance competitionInstance) {

        int result = 0;
        for (CompetitionSeed competitionSeed :
                competitionInstance.competitionSeeds) {
            if (competitionSeed.getCompetitionGroups() != null) {

                if (competitionSeed.getCompetitionGroups().size() == 1) {
                    SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
                    competitionGroups = Sets.sort(competitionGroups);

                    if (competitionGroups.first().getCompetitionGroupResult().getCompetitionSeedNext() == null) {
                        CompetitionSeed competitionSeedEliminationFirst = getCompetitionSeedEliminationFirst(competitionSeed);
                        result = competitionSeedEliminationFirst.filteringValue;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public CompetitionSeed getCompetitionSeedEliminationFirst(CompetitionSeed competitionSeed) {
        CompetitionSeed competitionSeedResult = null;
        Set<CompetitionSeed> competitionSeedPreviouses = new HashSet<>();
        if (competitionSeed.getCompetitionGroups() != null && !competitionSeed.getCompetitionGroups().isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
            competitionGroups = Sets.sort(competitionGroups);

            if (competitionGroups.first().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                competitionSeedResult = competitionSeed;
            }
            CompetitionSeed competitionSeedSubResult = null;
            for (CompetitionGroupResult competitionGroupResult : competitionSeed.getCompetitionGroupResultsPrevious()) {
                if (competitionGroupResult.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                    competitionSeedSubResult = getCompetitionSeedEliminationFirst(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
                    if (competitionSeedSubResult != null)
                        competitionSeedResult = competitionSeedSubResult;
                }
            }
        }
        return competitionSeedResult;
    }


    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantEliminationSimpleForcedQuick(int numberOfParticipantCompetition) {

        testComputationRankingParticipantEliminationSimpleForced(numberOfParticipantCompetition);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantEliminationDoubleForcedQuick(int numberOfParticipantCompetition) {

        testComputationRankingParticipantEliminationDoubleForced(numberOfParticipantCompetition);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantRoundRobinForcedQuick(int numberOfParticipantCompetition) {

        testComputationRankingParticipantRoundRobinForced(numberOfParticipantCompetition);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantSwissForcedQuick(int numberOfParticipantCompetition) {

        testComputationRankingParticipantSwissForced(numberOfParticipantCompetition);
    }


    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantEliminationSimpleQuick(int numberOfParticipantCompetition) {

        int hourDuration = 6;
        boolean withFinal = true;
        testComputationRankingParticipantEliminationSimple(numberOfParticipantCompetition, hourDuration);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantEliminationDoubleQuick(int numberOfParticipantCompetition) {

        int hourDuration = 6;
        boolean withFinal = true;
        testComputationRankingParticipantEliminationDouble(numberOfParticipantCompetition, hourDuration);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantRoundRobinQuick(int numberOfParticipantCompetition) {

        int hourDuration = 6;
        boolean withFinal = true;
        testComputationRankingParticipantRoundRobin(numberOfParticipantCompetition, hourDuration);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantSwissQuick(int numberOfParticipantCompetition) {

        int hourDuration = 6;
        testComputationRankingParticipantSwiss(numberOfParticipantCompetition, hourDuration);
    }

    @Test
    public void testComputationRankingParticipantEliminationDoubleForcedQuickSimple() {
        int numberOfParticipantCompetition = 5;
        testComputationRankingParticipantEliminationDoubleForced(numberOfParticipantCompetition);
    }


    @Test
    public void testComputationRankingParticipantEliminationDoubleQuickSimple() {
        int numberOfParticipantCompetition = 7;
        testComputationRankingParticipantEliminationDoubleQuick(numberOfParticipantCompetition);
    }


    @Test
    public void testComputationRankingParticipantSwissQuickSimple() {
        int numberOfParticipantCompetition = 31;
        int hourDuration = 6;
        testComputationRankingParticipantSwiss(numberOfParticipantCompetition, hourDuration);
    }


    @Test
    public void testComputationRankingParticipantMixingQualificationFinalQuickSimple() {
        int numberOfParticipantCompetition = 4;
        testComputationRankingParticipantMixingQualificationFinal(numberOfParticipantCompetition);
    }

    @Test
    public void testComputationRankingParticipantQualificationFinalQuickSimple() {
        int numberOfParticipantCompetition = 41;
        testComputationRankingParticipantQualificationFinal(numberOfParticipantCompetition);
    }

    @Test(dataProvider = "numberOfParticipantsQuickDataProvider", threadPoolSize = 1, singleThreaded = true, timeOut = 50000)
    public void testComputationRankingParticipantEliminationTripleForcedQuick(int numberOfParticipantCompetition) {

        testComputationRankingParticipantEliminationTripleForced(numberOfParticipantCompetition);
    }


    @Test
    public void testComputationRankingParticipantSwiss26() {
        int numberOfParticipantCompetition = 26;
        int hourDuration = 3;
        checkComputationRankingSwiss(numberOfParticipantCompetition, Duration.ofHours(hourDuration), ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
    }

    @BeforeSuite
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("logging.properties").getFile());
        String path = file.getPath();
        System.setProperty("java.util.logging.config.file", path);
    }
}


