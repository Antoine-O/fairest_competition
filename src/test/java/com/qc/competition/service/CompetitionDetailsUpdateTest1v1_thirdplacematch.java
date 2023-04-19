package com.qc.competition.service;


import com.qc.competition.service.structure.TournamentFormat;
import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

public class CompetitionDetailsUpdateTest1v1_thirdplacematch extends CompetitionDetailsUpdateTest1v1 {

    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 5;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 7;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
        competitionComputationParam.getFinalPhaseParameter().thirdPlaceMatchEnabled = true;
        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalEnabled = true;
        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalThreshold = 2;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 9;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 9;
        return competitionComputationParam;
    }


    public Boolean getThirdPlaceMatchEnabled() {
        return Boolean.TRUE;
    }

    public void createAndLaunchCompetition_2_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 2, 6, true);
    }

    public void createAndLaunchCompetition_4_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 4, 6, false);
    }

    public void createAndLaunchCompetition_8_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 8, 6, false);
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
