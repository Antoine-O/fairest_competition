package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.service.structure.*;
import org.testng.annotations.Test;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 19/02/2015.
 */
public class PlayVersusTypeTest {
    private static Logger LOGGER_main = Logger.getLogger(CompetitionDetailsGeneratorTest.class.getName() + ".main");

    private SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> createTestValue(CompetitionInstance competitionInstance, int numberOfTeam, int teamSize) {
        return createTestValue(competitionInstance, numberOfTeam, teamSize, 0);
    }

    private SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> createTestValue(CompetitionInstance competitionInstance, int numberOfTeam, int teamSize, int teamSizeStep) {
        SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMap = new TreeMap<>();
        SortedSet<ParticipantTeam> participantTeams = new TreeSet<>();
        ParticipantTeam participantTeam = null;
//        ParticipantTeamMember participantTeamMember = null;
        for (int i = 0; i < numberOfTeam; i++) {
            participantTeam = competitionInstance.createParticipantTeam();
            for (int j = 0; j < teamSize; j++) {
                ParticipantSingle participantSingle = competitionInstance.createParticipantSingle();
                participantTeam.addParticipant(participantSingle);
//                competitionInstance.addParticipantTeamMember(participantTeam.localId, participantSingle.localId);
            }
            teamSize += teamSizeStep;
            participantTeams.add(participantTeam);
            participantTeamMembersMap.put(participantTeam, participantTeam.participantTeamMembers);
        }
        return participantTeamMembersMap;
    }

    @Test
    public void createTestValue() {
        CompetitionInstance competitionInstance = CompetitionInstance.createInstance();
        int roundQuantity;
        for (int teamSizeStep = 0; teamSizeStep < 2; teamSizeStep++) {
            for (PlayVersusType playVersusType : PlayVersusType.values()) {
                SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMap = createTestValue(competitionInstance, playVersusType.numberOfTeam, playVersusType.teamSize, teamSizeStep);
                LOGGER_main.log(Level.INFO, playVersusType.name());
                LOGGER_main.log(Level.INFO, "teamSizeStep:" + teamSizeStep);
                roundQuantity = playVersusType.getRoundQuantity(competitionInstance, participantTeamMembersMap);
                for (int i = 0; i < roundQuantity; i++) {
                    LOGGER_main.log(Level.INFO, "roundDetails:" + (i + 1) + " / " + roundQuantity);
                    ParticipantPairing participantPairing = playVersusType.getParticipantPairing(competitionInstance, participantTeamMembersMap, i + 1);
                    LOGGER_main.log(Level.INFO, participantPairing.toDescription());
                }

            }
        }
    }
}
