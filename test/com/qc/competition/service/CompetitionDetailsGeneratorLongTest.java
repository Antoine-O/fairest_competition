package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.service.structure.CompetitionInstance;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Created by Duncan on 09/11/2015.
 */
public class CompetitionDetailsGeneratorLongTest extends CompetitionDetailsGeneratorTest {
    private static Logger LOGGER = Logger.getLogger(CompetitionDetailsGeneratorLongTest.class);

    @Test(dataProvider = "computationParameterProvider")
    public void testComputation(int numberOfParticipantCompetition,
                                int hourDuration,
                                String participantTypeString,
                                int numberOfParticipantMatch,
                                String playVersusTypeString,
                                int participantQualifiedPerMatch
    ) {

        Logger logger = LOGGER;
        ParticipantType participantType = ParticipantType.valueOf(participantTypeString);
        PlayVersusType playVersusType = PlayVersusType.valueOf(playVersusTypeString);
        com.qc.competition.ws.simplestructure.Duration competitionDuration = com.qc.competition.ws.simplestructure.Duration.ofHours(hourDuration);

        CompetitionInstance competitionInstance = null;
        Instant start;
        Instant end;
        Duration duration;
        String logPrefix = "" + numberOfParticipantCompetition + "\t" + competitionDuration.toMinutes() + "\t" + participantType + "\t" + numberOfParticipantMatch + "\t" + playVersusType + "\t" + participantQualifiedPerMatch;
        logger.debug(logPrefix);


        start = Instant.now();
//                logger.info(logPrefix + "Start\t" + start);
        List<CompetitionInstance> competitionInstances = getCompetitionInstances(1, numberOfParticipantCompetition, competitionDuration, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1);
        if (competitionInstances.isEmpty())
            Assert.assertFalse(competitionInstances.isEmpty(), logPrefix + "\tNO RESULT");
        else
            competitionInstance = competitionInstances.get(0);
        end = Instant.now();


        duration = Duration.between(start, end);
//                logger.info(logPrefix + "End\t" + end + "\t" + duration);
        logger.info(logPrefix + "\t" + competitionDuration.toString() + "\t" + Duration.ofMinutes(competitionInstance.getExpectedGlobalDuration().avg).toString() + "\t" + duration.toMillis());
//                logger.info(line);


//                DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss");
//                String suffix = "_" + dateFormat.format(new Date());
//                NumberFormat numberFormat = NumberFormat.getNumberInstance();
//                numberFormat.setMinimumIntegerDigits(3);
//

        checkComputationRanking(competitionInstance, logPrefix);


    }


}
