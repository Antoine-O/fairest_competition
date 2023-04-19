package com.qc.competition.service;


import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.PlayVersusType;
import com.qc.competition.service.template.CompetitionComputationParam;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest1v1v1v1_notsplittable extends CompetitionDetailsUpdateTest {
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
    public void createAndLaunchCompetition5() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.FinalPhaseRoundRobin, 5, 6, false);
    }

    @Test
    public void createAndLaunchCompetitionSaveAndReload8() {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration.AllMagic, 8, 6, true);
    }


    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantMatch = 4;
        competitionComputationParam.sharerPercentageLimit = 10;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT;
        int participantQualifiedPerMatch = 2;
        competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30));
        for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
            competitionCreationParamPhase.participantTypeSplittable = false;
            competitionCreationParamPhase.numberOfPlayMaximum = 1;
            competitionCreationParamPhase.numberOfPlayMinimum = 1;
        }
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
    public void createAndLaunchCompetition_3_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 1, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_1_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 2, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_2_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 3, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_4_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 4, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_5_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 5, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_7_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 7, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_8_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 8, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_10_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 10, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_10_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 10, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_11_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 11, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_5_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 5, 6, false);
    }
}
