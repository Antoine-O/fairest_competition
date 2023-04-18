package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.template.CompetitionComputationParam;
import com.qc.competition.ws.simplestructure.Duration;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest_100x1 extends CompetitionDetailsUpdateTest {

    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantMatch = 100;
        PlayVersusType playVersusType = PlayVersusType.FREE_FOR_ALL_100_TEAMS_1_PARTICIPANT;
        int participantQualifiedPerMatch = 50;
        CompetitionComputationParam competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30));
        competitionComputationParam.sharerPercentageLimit = 10;
        competitionComputationParam.phases.last().participantTypeSplittable = true;
        return competitionComputationParam;
    }


    @Test
    public void createAndLaunchCompetition_10_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 10, 6, false);
    }

}
