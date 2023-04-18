package com.qc.competition.service;

import com.qc.competition.service.template.CompetitionComputationParam;

public class CompetitionDetailsUpdateTest1v1_csgo extends CompetitionDetailsUpdateTest1v1 {


    @Override
    protected boolean isChangedWinner() {
        return false;
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT1H";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT1H";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT1H";
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getMixingPhaseParameter().maximumNumberOfParallelPlay = 0;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT1H";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT1H";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT1H";
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getQualificationPhaseParameter().maximumNumberOfParallelPlay = 0;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT1H";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT1H";
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT1H";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getFinalPhaseParameter().maximumNumberOfParallelPlay = 0;
        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalEnabled = false;
//        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalThreshold = numberOfParticipantMatch * 2;
//        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 7;
//        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 9;

        return competitionComputationParam;

    }


    public void createAndLaunchCompetition_24_18_AllMAgic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 24, 18, false);
    }

}

