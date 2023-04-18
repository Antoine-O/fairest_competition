package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.template.CompetitionComputationParam;
import com.qc.competition.ws.simplestructure.Duration;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest3v3in1v1 extends CompetitionDetailsUpdateTest {

    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        ParticipantType participantType = ParticipantType.TEAM_THREE_PARTICIPANTS;
        int numberOfParticipantMatch = 2;
        competitionComputationParam.sharerPercentageLimit = 10;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int participantQualifiedPerMatch = 1;
        competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30));

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

}
