package com.qc.competition.service.template;

import com.qc.competition.db.entity.game.TournamentFormat;

/**
 * Created by Duncan on 26/05/2015.
 */


public class CompetitionCreationParamPhaseMixing extends CompetitionCreationParamPhase {

    public Integer numberOfParticipant;
    public Integer numberOfRoundMinimum;
    public Integer numberOfRoundMaximum;

    @Override
    public int compareTo(CompetitionCreationParamPhase competitionCreationParamPhase) {
        int compare = super.compareTo(competitionCreationParamPhase);
        if (compare == 0) {
            numberOfRoundMinimum.compareTo(((CompetitionCreationParamPhaseMixing) competitionCreationParamPhase).numberOfRoundMinimum);
        }
        if (compare == 0) {
            numberOfRoundMaximum.compareTo(((CompetitionCreationParamPhaseMixing) competitionCreationParamPhase).numberOfRoundMaximum);
        }
        return compare;
    }

    @Override
    public void checkAndCorrectValues() {
        super.checkAndCorrectValues();
        if (phaseIndex == null)
            phaseIndex = 1;

        if (tournamentFormatsAccepted == null || tournamentFormatsAccepted.isEmpty()) {
            tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
        }
        int numberOfRoundComputed = 1;
        if (numberOfParticipant != null) {
            if (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && !TournamentFormat.checkRoundRobinCompatibility(numberOfParticipant, numberOfParticipantMatch)) {
                tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                if (tournamentFormatsAccepted.isEmpty()) {
                    tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                }
            }

            int topSizeDefined = numberOfParticipant;
            int topSizeThreshold = (int) Math.ceil((double) numberOfParticipant / 4.0);
            if (numberOfParticipantMatch == 1) {
                if (topSizeThreshold < 2) {
                    topSizeThreshold = 2;
                }
            } else if (topSizeThreshold < numberOfParticipantMatch) {

                topSizeThreshold = participantQualifiedPerMatch;
            }
            while (topSizeDefined > topSizeThreshold && numberOfRoundComputed < numberOfParticipant) {
                numberOfRoundComputed++;
                if (numberOfParticipantMatch == 1) {
                    topSizeDefined = (int) Math.ceil((double) topSizeDefined / (double) numberOfParticipantMatch);
                } else {
                    topSizeDefined = (int) Math.ceil((double) topSizeDefined * (double) participantQualifiedPerMatch / (double) numberOfParticipantMatch);
                }
            }
            if (numberOfParticipantMatch > 1 && numberOfParticipant % numberOfParticipantMatch != 0)
                numberOfRoundComputed++;
            if (numberOfRoundMinimum == null)
                numberOfRoundMinimum = numberOfRoundComputed;
            else if (numberOfRoundMinimum == 1 && numberOfParticipantMatch > 1 && numberOfParticipant % numberOfParticipantMatch != 0)
                numberOfRoundMinimum++;

            if (numberOfRoundMaximum == null || numberOfRoundMaximum > numberOfRoundComputed)
                numberOfRoundMaximum = numberOfRoundComputed;
            if (numberOfRoundMaximum < numberOfRoundMinimum)
                numberOfRoundMaximum = numberOfRoundMinimum;

            boolean fixNumberOfRoundMinAndMax = false;
            if (TournamentFormat.checkRoundRobinCompatibility(numberOfParticipant, numberOfParticipantMatch)) {
                if (numberOfParticipantMatch > 4) {
                    fixNumberOfRoundMinAndMax = true;
                }
            } else {
                fixNumberOfRoundMinAndMax = true;
            }
            if (fixNumberOfRoundMinAndMax) {
                numberOfRoundMinimum = numberOfRoundMaximum;
                tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                if (tournamentFormatsAccepted.isEmpty()) {
                    tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                }

            }
        }
    }

    @Override
    public CompetitionCreationParamPhase cloneCompetitionCreationParamPhase() {
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = new CompetitionCreationParamPhaseMixing();
        competitionCreationParamPhaseMixing.copyFrom(this);
        competitionCreationParamPhaseMixing.numberOfRoundMaximum = numberOfRoundMaximum;
        competitionCreationParamPhaseMixing.numberOfRoundMinimum = numberOfRoundMinimum;
        competitionCreationParamPhaseMixing.numberOfParticipant = numberOfParticipant;
        return competitionCreationParamPhaseMixing;
    }

}
