package com.qc.competition.service;


import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest1v1v1v1v1v1v1v1 extends CompetitionDetailsUpdateTest {
    @Test
    @Override
    public void createAndLaunchCompetitionSaveAndReload17() {
        super.createAndLaunchCompetitionSaveAndReload17();
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload16() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 16, 6, true);
    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationFinalPhaseRoundRobin(Integer participantSize, Integer durationInHour) {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, durationInHour, false);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload4() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.AllMagic, 4, 6, true);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload3() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.AllMagic, 3, 6, true);
    }


    @Test
    public void createAndLaunchCompetitionSaveAndReload5() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.NoQualification, 5, 6, true);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload8() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.AllMagic, 8, 6, true);
    }

    @Test
    public void createAndLaunchCompetition64() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 70, 1, 1, 1, false);
    }


    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantMatch = 8;
        competitionComputationParam.sharerPercentageLimit = 10;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_10_TEAMS_1_PARTICIPANT;
        int participantQualifiedPerMatch = 1;
        competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(10), Duration.ofMinutes(10), Duration.ofMinutes(10));

        return competitionComputationParam;
    }


    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Override
    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test
    public void createAndLaunchCompetition_4_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 4, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_8_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 8, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_9_4_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 9, 4, false);
    }


    @Test
    public void createAndLaunchCompetition_10_1_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 10, 1, false);
    }

    @Test
    public void createAndLaunchCompetition_2_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 2, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_3_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 3, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_4_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 4, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_5_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 5, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_9_7_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 9, 7, false);
    }


    @Test
    public void createAndLaunchCompetition_4_6_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 4, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_5_6_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 5, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 16, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 32, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 32, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 30, 6, true);
    }

    @Test(dataProvider = "numberOfParticipantsFullDataProvider", timeOut = 10000L)
    public void createAndLaunchCompetition_FinalPhaseDoubleElimination(int participantNumber) {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantNumber, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_8_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_40_2_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 40, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 30, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_64_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 64, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_64_6_FinalPhaseTripleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, 64, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_64_6_FinalPhaseQuadrupleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, 64, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_64_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 64, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_60_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 60, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_60_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 60, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_29_6_FinalPhases() {
        int participantSize = 29;
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, participantSize, 6, false);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, 6, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, 6, false);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, participantSize, 6, false);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_32_6_FinalPhases() {
        int participantSize = 32;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, participantSize, 6, false);
    }


    @Override
    @Test(dataProvider = "createAndLaunchCompetitionParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationFull(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationAllMagic(Integer participantSize, Integer durationInHour) {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }
}
