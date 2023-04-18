package com.qc.competition.service;

import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

public class CompetitionDetailsUpdateTest1v1_one_play extends CompetitionDetailsUpdateTest1v1 {
    @Override
    public void createAndLaunchCompetition_10_6_DoubleElimination() {
        super.createAndLaunchCompetition_10_6_DoubleElimination();
    }

    @Test
    public void createAndLaunchCompetition_16_6_DoubleElimination() {
        super.createAndLaunchCompetition_16_6_FinalPhaseDoubleElimination();
    }

    @Test
    public void createAndLaunchCompetition_4_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 4, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_8_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, false);
    }


    @Override
    public void createAndLaunchCompetition_5_6_DoubleElimination() {
        super.createAndLaunchCompetition_5_6_DoubleElimination();
    }


    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT4M";
        return competitionComputationParam;

    }


    public void createAndLaunchCompetition_2_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 2, 6, true);
    }

    public void createAndLaunchCompetition_4_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 4, 6, false);
    }


    public void createAndLaunchCompetition_18_2_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixing, 18, 6, false);
    }


    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    public void createAndLaunchCompetition_9_1_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 9, 1, true);
    }

    public void createAndLaunchCompetition_9_1_TripleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, 9, 1, true);
    }

    @Test
    public void createAndLaunchCompetition_56_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 56, 6, false);
    }


}
