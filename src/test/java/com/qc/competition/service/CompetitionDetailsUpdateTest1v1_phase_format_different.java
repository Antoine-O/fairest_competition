package com.qc.competition.service;


import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
@Test(threadPoolSize = 10)
public class CompetitionDetailsUpdateTest1v1_phase_format_different extends CompetitionDetailsUpdateTest {


    @Test
    public void createAndLaunchCompetition_20_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 20, 6, true);
    }


    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    @Test
    public void createAndLaunchCompetition_12_2_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 12, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_33_12_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 33, 12, false);
    }

    @Test
    public void createAndLaunchCompetition_32_12_NoMixing() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixing, 32, 12, false);
    }

    @Test
    public void createAndLaunchCompetition_2_12_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 2, 12, false);
    }


    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getMixingPhaseParameter().playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        competitionComputationParam.getMixingPhaseParameter().numberOfParticipantMatch = competitionComputationParam.getMixingPhaseParameter().playVersusType.numberOfTeam;
        competitionComputationParam.getMixingPhaseParameter().participantQualifiedPerMatch = competitionComputationParam.getMixingPhaseParameter().playVersusType.numberOfTeam / 2;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT4M";
        competitionComputationParam.getQualificationPhaseParameter().playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        competitionComputationParam.getQualificationPhaseParameter().numberOfParticipantMatch = competitionComputationParam.getQualificationPhaseParameter().playVersusType.numberOfTeam;
        competitionComputationParam.getQualificationPhaseParameter().participantQualifiedPerMatch = competitionComputationParam.getQualificationPhaseParameter().playVersusType.numberOfTeam / 2;
        competitionComputationParam.getQualificationPhaseParameter().groupSizeMaximum = 16;
        competitionComputationParam.getFinalPhaseParameter().groupSizeMaximum = 16;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT4M";
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT4M";
        return competitionComputationParam;
    }

}
