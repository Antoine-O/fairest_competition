package com.qc.competition.service.template;

import com.qc.competition.db.entity.game.TournamentFormat;

/**
 * Created by Duncan on 26/05/2015.
 */


public class CompetitionCreationParamPhaseQualification extends CompetitionCreationParamPhase {
    //    public static int MAX_MATCH_IN_GROUP = 8;
//    public static int MIN_MATCH_IN_GROUP = 4;
    public Integer numberOfParticipant;
    public Integer groupSizeMinimum;
    public Integer groupSizeMaximum;

    @Override
    public int compareTo(CompetitionCreationParamPhase competitionCreationParamPhase) {
        int compare = super.compareTo(competitionCreationParamPhase);
        if (compare == 0) {
            groupSizeMinimum.compareTo(((CompetitionCreationParamPhaseQualification) competitionCreationParamPhase).groupSizeMinimum);
        }
        if (compare == 0 && groupSizeMaximum != null && ((CompetitionCreationParamPhaseQualification) competitionCreationParamPhase).groupSizeMaximum != null) {
            groupSizeMaximum.compareTo(((CompetitionCreationParamPhaseQualification) competitionCreationParamPhase).groupSizeMaximum);
        }
        return compare;
    }

    @Override
    public void checkAndCorrectValues() {
        super.checkAndCorrectValues();
        if (phaseIndex == null)
            phaseIndex = 2;
        if (numberOfParticipant != null)
            if (groupSizeMaximum == null || groupSizeMaximum > numberOfParticipant) {
                groupSizeMaximum = numberOfParticipant;
            }
        if (numberOfParticipantMatch == 1) {
            if (groupSizeMinimum == null || groupSizeMinimum <= 2 * 2) {
                groupSizeMinimum = 2 * 2;
            }
        } else {
            if (groupSizeMinimum == null || groupSizeMinimum < 2 * numberOfParticipantMatch) {
                groupSizeMinimum = 2 * numberOfParticipantMatch;
            }
        }
        if (numberOfParticipantMatch == 1) {
            if (groupSizeMaximum == null || (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && groupSizeMaximum > 2 * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN)) {
                groupSizeMaximum = 2 * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN;
            }
        } else {
            if (groupSizeMaximum == null || (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && groupSizeMaximum > numberOfParticipantMatch * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN)) {
                groupSizeMaximum = numberOfParticipantMatch * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN;
            }
        }

        if (groupSizeMinimum > groupSizeMaximum) {
            groupSizeMinimum = groupSizeMaximum;
        }

        if (groupSizeMaximum < groupSizeMinimum) {
            int temp = groupSizeMinimum;
            groupSizeMinimum = groupSizeMaximum;
            groupSizeMaximum = groupSizeMinimum;
        }

        if (tournamentFormatsAccepted == null || tournamentFormatsAccepted.isEmpty()) {
            tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
            tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
//            tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION_WITH_THIRD_PLACE_MATCH);
        }
        if (numberOfParticipantMatch > 1) {
            if (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && !TournamentFormat.checkRoundRobinCompatibility(groupSizeMaximum, numberOfParticipantMatch)) {
                tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                if (tournamentFormatsAccepted.isEmpty())
                    tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            }
        } else if (numberOfParticipant != null && numberOfParticipant > TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN) {
            tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
            if (tournamentFormatsAccepted.isEmpty())
                tournamentFormatsAccepted.add(TournamentFormat.SWISS);
        }
        if (registrationOnTheFly == null) {
            registrationOnTheFly = false;
        }
        if (registrationOnTheFly) {
            tournamentFormatsAccepted.clear();
            tournamentFormatsAccepted.add(TournamentFormat.LADDER);
            groupSizeMinimum = participantQualifiedPerMatch + 1;
            groupSizeMaximum = numberOfParticipant;
        }

    }

    @Override
    public CompetitionCreationParamPhase cloneCompetitionCreationParamPhase() {
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = new CompetitionCreationParamPhaseQualification();
        competitionCreationParamPhaseQualification.groupSizeMaximum = groupSizeMaximum;
        competitionCreationParamPhaseQualification.groupSizeMinimum = groupSizeMinimum;
        competitionCreationParamPhaseQualification.numberOfParticipant = numberOfParticipant;
        competitionCreationParamPhaseQualification.copyFrom(this);
        return competitionCreationParamPhaseQualification;
    }

}
