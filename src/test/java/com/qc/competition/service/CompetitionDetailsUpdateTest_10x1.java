package com.qc.competition.service;


import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest_10x1 extends CompetitionDetailsUpdateTest {
    @Test
    @Override
    public void createAndLaunchCompetitionSaveAndReload17() {
        super.createAndLaunchCompetitionSaveAndReload17();
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload16() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 16, 6, true);
    }


    @Test
    public void createAndLaunchCompetitionSaveAndReload16DoubleElimination() {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 16, 6, true);
    }


    @Test
    public void createAndLaunchCompetitionSaveAndReload8() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 8, 6, false);
    }

    @Override
    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantMatch = 10;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_10_TEAMS_1_PARTICIPANT;
        int participantQualifiedPerMatch = 5;
        CompetitionComputationParam competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30));
        competitionComputationParam.sharerPercentageLimit = 10;

        return competitionComputationParam;
    }

    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Test
    public void createAndLaunchCompetition_10_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 10, 6, false);
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


    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 30, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_40_2_FinalPhaseSingleElimination() {
//        for (int i = 11; i < 55; i++) {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 40, 2, false);
//        }

    }

    @Test
    public void createAndLaunchCompetition_40_6_allMagic() {
//        for (int i = 11; i < 55; i++) {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 40, 6, true);
//        }

    }

    @Test
    public void createAndLaunchCompetition_40_6_FinalPhaseRoundRobin() {
//        for (int i = 11; i < 55; i++) {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 40, 6, true);
//        }

    }


    @Test
    public void createAndLaunchCompetition_17_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 17, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_17_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 17, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_17_6_FinalPhaseAllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 17, 6, false);
    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationAllMagic(Integer participantSize, Integer durationInHour) {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

}
