package com.qc.competition.service;

import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

public class CompetitionDetailsUpdateTest1v1_three_plays extends CompetitionDetailsUpdateTest1v1 {
    @Override
    public void createAndLaunchCompetition_10_6_DoubleElimination() {
        super.createAndLaunchCompetition_10_6_DoubleElimination();
    }


    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 3;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 3;
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

}
