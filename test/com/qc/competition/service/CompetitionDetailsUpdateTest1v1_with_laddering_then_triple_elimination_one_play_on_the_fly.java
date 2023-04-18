package com.qc.competition.service;

import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

public class CompetitionDetailsUpdateTest1v1_with_laddering_then_triple_elimination_one_play_on_the_fly extends CompetitionDetailsUpdateTest1v1 {


    @Override
    protected boolean registrationOnTheFly() {
        return true;
    }

    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getMixingPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.LADDER);
        competitionComputationParam.removeQualificationPhase();
//        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT4M";
//        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT4M";
//        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT4M";
//        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 1;
//        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 1;
//        competitionComputationParam.getQualificationPhaseParameter().tournamentFormatsAccepted.clear();
//        competitionComputationParam.getQualificationPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
//        competitionComputationParam.getQualificationPhaseParameter().phaseDuration = "PT1H";
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.TRIPLE_ELIMINATION);
        competitionComputationParam.getFinalPhaseParameter().phaseDuration = "PT1H";
        competitionComputationParam.getFinalPhaseParameter().groupSizeMaximum = 8;
        return competitionComputationParam;

    }


    public void createAndLaunchCompetition_18_6() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 18, 6, false);
    }

    public void createAndLaunchCompetition_18_6_NoMixingNoQualification() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixingNoQualification, 18, 6, false);
    }


    public void createAndLaunchCompetition_18_6_NoMixing() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixing, 18, 6, false);
    }

    public void createAndLaunchCompetition_18_6_NoQualification() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoQualification, 18, 6, false);
    }


    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

}
