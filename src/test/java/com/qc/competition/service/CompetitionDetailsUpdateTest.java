package com.qc.competition.service;


import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.TournamentFormat;
import com.qc.competition.service.template.*;
import com.qc.competition.utils.MathUtils;
import com.qc.competition.utils.Sets;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 04/01/2015.
 */

abstract public class CompetitionDetailsUpdateTest extends CompetitionDetailsGeneratorTest {

    public static int MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST = 100;
    public static int MAX_HOUR_DURATION = 6;
    private static final Logger LOGGER = Logger.getLogger(CompetitionDetailsUpdateTest.class);

    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload17() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 17, 6, true);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReloadMinimal() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, getCompetitionComputationParamBase().getFirstPhase().numberOfParticipantMatch, 6, true);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload27AllMagic() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 27, 6, false);
    }

    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuickNoSaveAndReload(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        if (!doSaveAndReload)
            createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuickWithSaveAndReload(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        if (doSaveAndReload)
            createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test(dataProvider = "createAndLaunchCompetitionParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationFull(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @DataProvider(name = "computationParameterFormatQuickProvider")
    public Object[][] dataProviderComputationParameterFormatQuickProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatQuickProvider]";
        List<List<Object>> parameterSetList = new ArrayList<>();

        logger.info(logPrefix);
        for (TestGenerationConfiguration testGenerationConfiguration : TestGenerationConfiguration.values()) {
            for (int k = 0; k < 2; k++) {
                SortedSet<Integer> numberOfParticipantCompetitions = new TreeSet<>(MathUtils.getPrimeNumbers(1, MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST));
                for (int i = getCompetitionComputationParamBase().getMixingPhaseParameter().participantQualifiedPerMatch + 1; i < MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST / 2; i = i + 2) {
                    numberOfParticipantCompetitions.add(i);
                }
                for (int i = 1; i < 32; i = i + 1) {
                    numberOfParticipantCompetitions.add(i);
                }
                numberOfParticipantCompetitions = Sets.sort(numberOfParticipantCompetitions);

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

    private List<Object> addParameterSet(List<List<Object>> parameterSetList, TestGenerationConfiguration testGenerationConfiguration, int numberOfParticipantCompetition, int hourDuration, boolean doSaveAndReload) {
        List<Object> parameterSet = new ArrayList<>();
        parameterSet.add(testGenerationConfiguration);
        parameterSet.add(numberOfParticipantCompetition);
        parameterSet.add(hourDuration);
        parameterSet.add(doSaveAndReload);
        parameterSetList.add(parameterSet);
        return parameterSet;

    }

    public void createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, null, null, doSaveAndReload);
    }

    public void createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, Integer numberOfPlayMin, Integer numberOfPlayMax, boolean doSaveAndReload) {
        Logger logger = LOGGER;
        Duration duration = Duration.ofHours(durationInHour);
        String logPrefix = "[participantSize:" + participantSize + "][duration:" + duration + "][testGenerationConfiguration:" + testGenerationConfiguration + "]";
        LOGGER.info(logPrefix);
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.competitionDuration = duration;
//        competitionComputationParam.removeMixingPhase();
//        competitionComputationParam.removeQualificationPhase();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionComputationParam.getFinalPhaseParameter();
        competitionComputationParam.numberOfParticipantCompetition = participantSize;
        competitionComputationParam.competitionName = participantSize + "participants_" + durationInHour + "hours";
        competitionComputationParam.competitionName = competitionComputationParam.competitionName + "_" + testGenerationConfiguration;
        for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
            if (numberOfPlayMax != null) {
                competitionCreationParamPhase.numberOfPlayMaximum = numberOfPlayMax;
            }
            if (numberOfPlayMin != null) {
                competitionCreationParamPhase.numberOfPlayMinimum = numberOfPlayMin;
            }
        }
        try {
            CompetitionCreationParamPhaseFinal finalPhaseTournamentFormatsAccepted;
            switch (testGenerationConfiguration) {
                case AllMagic:
                    break;
                case NoMixing:
                    competitionComputationParam.removeMixingPhase();
                    break;
                case NoQualification:
                    competitionComputationParam.removeQualificationPhase();
                    break;
                case NoMixingNoQualification:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    break;
                case FinalPhaseSingleElimination:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
                    break;
                case FinalPhaseDoubleElimination:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
                    break;
                case FinalPhaseTripleElimination:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.TRIPLE_ELIMINATION);
                    break;
                case FinalPhaseQuadrupleElimination:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.QUADRUPLE_ELIMINATION);
                    break;
                case FinalPhaseSwiss:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                    break;
                case FinalPhaseRoundRobin:
                    competitionComputationParam.removeMixingPhase();
                    competitionComputationParam.removeQualificationPhase();
                    finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
                    finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                    break;
            }
            logger.info(competitionComputationParam.toString());
//            competitionComputationParam.checkAndCorrectValues();
            logger.info(competitionComputationParam.toString());
            createAndLaunchCompetitionWithParam(competitionComputationParam, doSaveAndReload);
        } catch (Exception e) {
            logger.error(logPrefix, e);
            Assert.fail(logPrefix, e);
        }


    }


    public void createAndLaunchCompetitionForSizeAndDurationFinalPhaseSingleElimination(Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        Logger logger = LOGGER;
        Duration duration = Duration.ofHours(durationInHour);
        String logPrefix = "[participantSize:" + participantSize + "[duration:" + duration + "]";
        LOGGER.info(logPrefix);
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.competitionDuration = duration;
        competitionComputationParam.removeMixingPhase();
        competitionComputationParam.removeQualificationPhase();
        CompetitionCreationParamPhaseFinal finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
        finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
        finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
        competitionComputationParam.numberOfParticipantCompetition = participantSize;
        competitionComputationParam.competitionName = participantSize + "participants_" + durationInHour + "hours";
        try {
            List<CompetitionComputationParam> competitionComputationParamList = new ArrayList<>();


            CompetitionComputationParam competitionComputationParamFinalPhaseSingleElimination = competitionComputationParam.cloneCompetitionComputationParam();
            competitionComputationParamFinalPhaseSingleElimination.competitionName = competitionComputationParam.competitionName + "[FinalPhaseSingleElimination]";
            competitionComputationParamFinalPhaseSingleElimination.removeQualificationPhase();
            competitionComputationParamFinalPhaseSingleElimination.removeMixingPhase();
            finalPhaseTournamentFormatsAccepted = competitionComputationParam.getFinalPhaseParameter();
            finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.clear();
            finalPhaseTournamentFormatsAccepted.tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
            competitionComputationParamList.add(competitionComputationParamFinalPhaseSingleElimination);

            for (CompetitionComputationParam competitionComputationParamElt : competitionComputationParamList) {
                LOGGER.info("**** " + competitionComputationParamElt.competitionName + " ****");
                competitionComputationParamElt.checkAndCorrectValues();
                createAndLaunchCompetitionWithParam(competitionComputationParamElt, doSaveAndReload);
            }
        } catch (Exception e) {
            logger.error(logPrefix, e);
            Assert.fail(logPrefix, e);
        }


    }

    public void createAndLaunchCompetitionWithPhaseParameters(Integer durationInHour, Integer numberOfParticipantCompetition, CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, boolean doSaveAndReload) {
        Logger logger = LOGGER;
        Duration duration = Duration.ofHours(durationInHour);
        String logPrefix = "[duration:" + duration + "]";
        LOGGER.info(logPrefix);
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.competitionDuration = duration;
        competitionComputationParam.competitionName = durationInHour + "hours";
        competitionComputationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;
        if (competitionCreationParamPhaseMixing != null) {
            competitionComputationParam.setMixingPhase(competitionCreationParamPhaseMixing);
            competitionComputationParam.competitionName = competitionComputationParam.competitionName + "_MIXING";

        }
        if (competitionCreationParamPhaseQualification != null) {
            competitionComputationParam.setQualificationPhase(competitionCreationParamPhaseQualification);
            competitionComputationParam.competitionName = competitionComputationParam.competitionName + "_QUALIFICATION";
        }
        if (competitionCreationParamPhaseFinal != null) {
            competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);
            competitionComputationParam.competitionName = competitionComputationParam.competitionName + "_FINAL";
        }
        try {
            logger.info(competitionComputationParam.toString());
            competitionComputationParam.checkAndCorrectValues();
            logger.info(competitionComputationParam.toString());
            createAndLaunchCompetitionWithParam(competitionComputationParam, doSaveAndReload);
        } catch (Exception e) {
            logger.error(logPrefix, e);
            Assert.fail(logPrefix, e);
        }


    }


    public enum TestGenerationConfiguration {
        AllMagic,
        NoMixing,
        NoQualification,
        NoMixingNoQualification,
        FinalPhaseSingleElimination,
        FinalPhaseDoubleElimination,
        FinalPhaseTripleElimination,
        FinalPhaseQuadrupleElimination,
        FinalPhaseSwiss,
        FinalPhaseRoundRobin
    }


}
