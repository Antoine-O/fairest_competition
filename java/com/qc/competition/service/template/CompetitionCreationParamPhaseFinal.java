package com.qc.competition.service.template;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qc.competition.db.entity.game.MergePolicy;
import com.qc.competition.db.entity.game.ResetPolicy;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.utils.Sets;
import com.qc.competition.ws.utils.JsonBooleanDeserialiser;
import com.qc.competition.ws.utils.JsonBooleanSerializer;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 26/05/2015.
 */


public class CompetitionCreationParamPhaseFinal extends CompetitionCreationParamPhase {

    public Integer groupSizeMinimum;
    public Integer groupSizeMaximum;
    @XmlAttribute
    public Integer groupSizeFinalThreshold;
    @XmlAttribute
    public Boolean groupSizeFinalEnabled;
    @XmlAttribute
    public Integer numberOfPlayFinalMaximum;
    @XmlAttribute
    public Integer numberOfPlayFinalMinimum;
    @XmlAttribute
    public Integer numberOfParallelPlayFinalMaximum;
    @XmlAttribute
    public String intermissionDurationFinal;
    @XmlAttribute
    public ResetPolicy resetPolicy;
    @XmlAttribute
    public MergePolicy mergePolicy;
    @XmlAttribute
    @JsonDeserialize(using = JsonBooleanDeserialiser.class)
    @JsonSerialize(using = JsonBooleanSerializer.class)
    public Boolean thirdPlaceMatchEnabled;

    @Override
    public int compareTo(CompetitionCreationParamPhase competitionCreationParamPhase) {
        int compare = super.compareTo(competitionCreationParamPhase);
        if (compare == 0) {
            compare = groupSizeMinimum.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeMinimum);
        }
        if (compare == 0) {
            compare = groupSizeMaximum.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeMaximum);
        }

        if (compare == 0) {
            if (groupSizeFinalEnabled != null && ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalEnabled != null) {
                compare = groupSizeFinalEnabled.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalEnabled);
            } else if (groupSizeFinalEnabled != null) {
                compare = 1;
            } else if (((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalEnabled != null) {
                compare = -1;
            }
        }
        if (compare == 0) {
            if (groupSizeFinalEnabled != null && groupSizeFinalEnabled && ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalEnabled != null && ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalEnabled) {
                compare = groupSizeFinalThreshold.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeFinalThreshold);
                if (compare == 0) {
                    compare = numberOfPlayFinalMinimum.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).numberOfPlayFinalMinimum);
                }
                if (compare == 0) {
                    compare = numberOfPlayFinalMaximum.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).numberOfPlayFinalMaximum);
                }
                if (compare == 0) {
                    if (numberOfParallelPlayFinalMaximum != null && ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).numberOfParallelPlayFinalMaximum != null) {
                        compare = numberOfParallelPlayFinalMaximum.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).numberOfParallelPlayFinalMaximum);
                    } else if (numberOfParallelPlayFinalMaximum != null) {
                        compare = 1;
                    } else if (((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).numberOfParallelPlayFinalMaximum != null) {
                        compare = -1;
                    }
                }
                if (compare == 0) {
                    if (thirdPlaceMatchEnabled != null && ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).thirdPlaceMatchEnabled != null) {
                        compare = thirdPlaceMatchEnabled.compareTo(((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).thirdPlaceMatchEnabled);
                    } else if (thirdPlaceMatchEnabled != null) {
                        if (thirdPlaceMatchEnabled)
                            compare = 1;
                        else
                            compare = -1;
                    } else if (((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).thirdPlaceMatchEnabled != null) {
                        if (((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).thirdPlaceMatchEnabled)
                            compare = -1;
                        else
                            compare = 1;
                    }
                }
            }
        }
        return compare;
    }

    @Override
    public void checkAndCorrectValues() {
        super.checkAndCorrectValues();

        if (thirdPlaceMatchEnabled == null)
            thirdPlaceMatchEnabled = false;
        if (phaseIndex == null)
            phaseIndex = 3;
        if (numberOfParticipantMatch > 1) {
            if (groupSizeMinimum == null || groupSizeMinimum <= participantQualifiedPerMatch) {
                groupSizeMinimum = participantQualifiedPerMatch + 1;
            }
        } else {
            groupSizeMinimum = 2;
        }
        if (numberOfParticipantMatch > 1) {
            if (groupSizeMaximum == null || (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && groupSizeMaximum > numberOfParticipantMatch * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN)) {
                groupSizeMaximum = numberOfParticipantMatch * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN;
            }
        } else {
            if (groupSizeMaximum == null || groupSizeMaximum > 2 * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN)
                groupSizeMaximum = 2 * TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN;
        }
        if (groupSizeMinimum > groupSizeMaximum) {
            groupSizeMinimum = groupSizeMaximum;
        }

        if (tournamentFormatsAccepted == null || tournamentFormatsAccepted.isEmpty()) {
            tournamentFormatsAccepted = new TreeSet<>();
            tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
            tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());
        }
        if (numberOfParticipantMatch > 1) {
            if (tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN) && !TournamentFormat.checkRoundRobinCompatibility(groupSizeMaximum, numberOfParticipantMatch)) {
                tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                if (tournamentFormatsAccepted.isEmpty())
                    tournamentFormatsAccepted.add(TournamentFormat.SWISS);
            }
        } else if (groupSizeMinimum > TournamentFormat.MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN) {
            tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
            if (tournamentFormatsAccepted.isEmpty())
                tournamentFormatsAccepted.add(TournamentFormat.SWISS);
        }


        //        if (groupSizeMaximum / numberOfParticipantMatch > MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN) {
//        }
//        if (numberOfParticipantMatch <= 4 || groupSizeMaximum / numberOfParticipantMatch <= 2) {
//
//        } else {
//            tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
//            if (tournamentFormatsAccepted.isEmpty())
//                tournamentFormatsAccepted.add(TournamentFormat.SWISS);
//        }
        if (groupSizeFinalEnabled != null && groupSizeFinalEnabled) {
            if (groupSizeFinalThreshold == null) {
                groupSizeFinalThreshold = playVersusType.numberOfTeam;
            }

            if (numberOfPlayFinalMinimum == null) {
                numberOfPlayFinalMinimum = numberOfPlayMinimum;
            }
            if (numberOfPlayFinalMaximum == null) {
                numberOfPlayFinalMaximum = numberOfPlayMaximum;
            }


            if (numberOfPlayFinalMinimum % 2 == 0 && (allowEvenNumberOfPlay == null || !allowEvenNumberOfPlay)) {
                if (numberOfPlayFinalMinimum - 1 > numberOfPlayMinimum)
                    numberOfPlayFinalMinimum--;
                else
                    numberOfPlayFinalMinimum++;
            }

            if (numberOfPlayFinalMaximum % 2 == 0 && (allowEvenNumberOfPlay == null || !allowEvenNumberOfPlay)) {
                if (numberOfPlayFinalMaximum + 1 < numberOfPlayMaximum)
                    numberOfPlayFinalMaximum++;
                else
                    numberOfPlayFinalMaximum--;
            }
            if (numberOfParallelPlayFinalMaximum == null)
                numberOfParallelPlayFinalMaximum = 0;

        }
        if (resetPolicy == null)
            resetPolicy = ResetPolicy.NONE;
        if (mergePolicy == null)
            mergePolicy = MergePolicy.STANDARD;
        if (thirdPlaceMatchEnabled) {
            thirdPlaceMatchEnabled = false;
            for (TournamentFormat tournamentFormat : tournamentFormatsAccepted) {
                if (tournamentFormat.compareTo(TournamentFormat.SINGLE_ELIMINATION) == 0) {
                    thirdPlaceMatchEnabled = true;
                }
            }
        }
    }

    @Override
    public CompetitionCreationParamPhase cloneCompetitionCreationParamPhase() {
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = new CompetitionCreationParamPhaseFinal();
        competitionCreationParamPhaseFinal.groupSizeMinimum = groupSizeMinimum;
        competitionCreationParamPhaseFinal.groupSizeMaximum = groupSizeMaximum;
        competitionCreationParamPhaseFinal.groupSizeFinalEnabled = groupSizeFinalEnabled;
        competitionCreationParamPhaseFinal.groupSizeFinalThreshold = groupSizeFinalThreshold;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum = numberOfPlayFinalMinimum;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum = numberOfPlayFinalMaximum;
        competitionCreationParamPhaseFinal.numberOfParallelPlayFinalMaximum = numberOfParallelPlayFinalMaximum;
        competitionCreationParamPhaseFinal.resetPolicy = resetPolicy;
        competitionCreationParamPhaseFinal.mergePolicy = mergePolicy;
        competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled = thirdPlaceMatchEnabled;
        competitionCreationParamPhaseFinal.copyFrom(this);
        return competitionCreationParamPhaseFinal;
    }

    public Integer getEliminationLevelMinimum() {
        Integer eliminationLevelMinimum = null;
        for (TournamentFormat tournamentFormat : tournamentFormatsAccepted) {
            if (tournamentFormat.getEliminationLevel() != null
                    || eliminationLevelMinimum == null
                    || (tournamentFormat.getEliminationLevel() != null && eliminationLevelMinimum.compareTo(tournamentFormat.getEliminationLevel()) > 0)) {
                eliminationLevelMinimum = tournamentFormat.getEliminationLevel();
            }
        }
        return eliminationLevelMinimum;
    }

    public Integer getEliminationLevelMaximum() {
        Integer eliminationLevelMaximum = null;
        for (TournamentFormat tournamentFormat : tournamentFormatsAccepted) {
            if (tournamentFormat.getEliminationLevel() != null
                    || eliminationLevelMaximum == null
                    || (tournamentFormat.getEliminationLevel() != null && eliminationLevelMaximum.compareTo(tournamentFormat.getEliminationLevel()) < 0)) {
                eliminationLevelMaximum = tournamentFormat.getEliminationLevel();
            }
        }
        return eliminationLevelMaximum;
    }

    public SortedSet<CompetitionCreationParamPhase> createCompetitionCreationParamPhaseListToTry() {
        SortedSet<CompetitionCreationParamPhase> competitionCreationParamPhaseListToTry = super.createCompetitionCreationParamPhaseListToTry();
        SortedSet<CompetitionCreationParamPhase> competitionCreationParamPhaseListToTryParent = super.createCompetitionCreationParamPhaseListToTry();
        if (groupSizeFinalEnabled != null && groupSizeFinalEnabled) {
            for (int i = numberOfPlayFinalMinimum; i <= numberOfPlayFinalMinimum; i = i + (allowEvenNumberOfPlay ? 1 : 2)) {
                for (int j = i; j <= numberOfPlayFinalMaximum; j = j + (allowEvenNumberOfPlay ? 1 : 2)) {
                    for (CompetitionCreationParamPhase competitionCreationParamPhaseCurrent : competitionCreationParamPhaseListToTryParent) {
                        CompetitionCreationParamPhaseFinal competitionCreationParamPhase = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhaseCurrent.cloneCompetitionCreationParamPhase();
                        competitionCreationParamPhase.numberOfPlayFinalMinimum = i;
                        competitionCreationParamPhase.numberOfPlayFinalMaximum = j;
                        competitionCreationParamPhase.checkAndCorrectValues();
                        competitionCreationParamPhaseListToTry.add(competitionCreationParamPhase);
                    }
                }
            }
        }
        return Sets.sort(competitionCreationParamPhaseListToTry);

    }

    public CompetitionCreationParamPhase diff(CompetitionCreationParamPhase competitionCreationParamPhase) {
        CompetitionCreationParamPhase competitionCreationParamPhaseResult = super.diff(competitionCreationParamPhase);
        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
            if (competitionCreationParamPhaseResult == null) {
                if (this.groupSizeFinalEnabled != null && this.groupSizeFinalEnabled) {
                    CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
                    if (this.numberOfPlayFinalMinimum.compareTo(competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum) != 0 || this.numberOfPlayFinalMaximum.compareTo(competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum) != 0) {
                        competitionCreationParamPhaseResult = this.cloneCompetitionCreationParamPhase();
                        competitionCreationParamPhaseResult.numberOfPlayMinimum = null;
                        competitionCreationParamPhaseResult.numberOfPlayMaximum = null;
                    }
                }
            } else {
                ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhaseResult).numberOfPlayFinalMinimum = null;
                ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhaseResult).numberOfPlayFinalMaximum = null;
            }
        }
        return competitionCreationParamPhaseResult;
    }

}
