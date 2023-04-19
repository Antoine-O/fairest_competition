package com.qc.competition.service.structure;

/**
 * Created by Duncan on 24/02/2015.
 */
public enum ParticipantType {
    TEAM_ONE_PARTICIPANT(1),
    TEAM_TWO_PARTICIPANTS(2),
    TEAM_THREE_PARTICIPANTS(3),
    TEAM_FOUR_PARTICIPANTS(4),
    TEAM_FIVE_PARTICIPANTS(5),
    TEAM_SIX_PARTICIPANTS(6),
    TEAM_SEVEN_PARTICIPANTS(7),
    TEAM_EIGHT_PARTICIPANTS(8),
    TEAM_NINE_PARTICIPANTS(9),
    TEAM_TEN_PARTICIPANTS(10),
    TEAM_ELEVEN_PARTICIPANTS(11),
    TEAM_TWELVE_PARTICIPANTS(12),
    ;
    public int numberOfParticipants;

    ParticipantType(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Participant createParticipant(CompetitionInstance competitionInstance) {
        Participant participant;
        switch (this) {
            case TEAM_ONE_PARTICIPANT:
                participant = competitionInstance.createParticipantSingle();
//                competitionInstance.addParticipantSingle((ParticipantSingle) participant);
                break;
            default:
                participant = competitionInstance.createParticipantTeam();
                competitionInstance.addParticipantTeam((ParticipantTeam) participant);
                for (int i = 0; i < numberOfParticipants; i++) {
                    ParticipantSingle participantSingle = competitionInstance.createParticipantSingle();
                    competitionInstance.addParticipantSingle(participantSingle);
                    ((ParticipantTeam) participant).addParticipant(participantSingle);
//                    competitionInstance.addParticipantTeamMember(participant.localId, participantSingle.localId);
                }
                break;
        }
        return participant;
    }
}
