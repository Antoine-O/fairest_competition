package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.adaptater.DurationAdapter;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.json.JSONUtils;
import com.qc.competition.ws.simplestructure.Duration;
import org.apache.commons.codec.digest.DigestUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionComputationParam extends CompetitionCreationParam {
    //    private static Logger LOGGER_checkAndCorrectValues = Logger.getLogger(CompetitionComputationParam.class.getName() + ".checkAndCorrectValues");
    @XmlAttribute(name = "competitionDuration")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration competitionDuration;
    @XmlAttribute(name = "competitionName")
    public String competitionName;

    public CompetitionComputationParam() {
        super();
    }


    public void checkAndCorrectValues() {
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = null;

        if (getFinalPhaseParameter() != null) {
            competitionCreationParamPhaseFinal = getFinalPhaseParameter();
            if (numberOfParticipantCompetition != null)
                if (competitionCreationParamPhaseFinal.groupSizeMaximum > numberOfParticipantCompetition) {
                    competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantCompetition;
                }
            competitionCreationParamPhaseFinal.checkAndCorrectValues();
            if ((competitionCreationParamPhaseFinal.numberOfParticipantMatch == 2 && competitionCreationParamPhaseFinal.groupSizeMaximum > 64) || (competitionCreationParamPhaseFinal.numberOfParticipantMatch > 2 && competitionCreationParamPhaseFinal.groupSizeMaximum > competitionCreationParamPhaseFinal.numberOfParticipantMatch * 4)) {
                competitionCreationParamPhaseFinal.tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.isEmpty()) {
                    competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                }
            }
        }
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = null;
        if (getQualificationPhaseParameter() != null) {
            competitionCreationParamPhaseQualification = getQualificationPhaseParameter();

            if (competitionCreationParamPhaseQualification.numberOfParticipant == null && numberOfParticipantCompetition != null) {
                competitionCreationParamPhaseQualification.numberOfParticipant = numberOfParticipantCompetition;
            }
            competitionCreationParamPhaseQualification.checkAndCorrectValues();
            if (competitionCreationParamPhaseFinal != null) {

                if (numberOfParticipantCompetition != null) {
                    int qualificationGroupSizeMaximum = numberOfParticipantCompetition / competitionCreationParamPhaseFinal.groupSizeMaximum;
                    int multiplicator = competitionCreationParamPhaseQualification.numberOfParticipantMatch / competitionCreationParamPhaseQualification.participantQualifiedPerMatch;
                    if (multiplicator == 1)
                        multiplicator = 2;
                    for (int i = competitionCreationParamPhaseQualification.numberOfParticipantMatch; i < numberOfParticipantCompetition; i = i * multiplicator) {
                        if (qualificationGroupSizeMaximum <= i) {
                            qualificationGroupSizeMaximum = i;
                            break;
                        }
                    }
                    int qualificationGroupSizeMinimum = numberOfParticipantCompetition / qualificationGroupSizeMaximum;
                    for (int i = competitionCreationParamPhaseQualification.numberOfParticipantMatch; i < numberOfParticipantCompetition; i = i * multiplicator) {
                        if (qualificationGroupSizeMinimum <= i) {
                            qualificationGroupSizeMinimum = i;
                            break;
                        }
                    }
                    if (qualificationGroupSizeMinimum > qualificationGroupSizeMaximum) {
                        int temp = qualificationGroupSizeMinimum;
                        qualificationGroupSizeMinimum = qualificationGroupSizeMaximum;
                        qualificationGroupSizeMaximum = temp;
                    }
                    if (qualificationGroupSizeMaximum > numberOfParticipantCompetition)
                        qualificationGroupSizeMaximum = numberOfParticipantCompetition;
                    if (qualificationGroupSizeMinimum < competitionCreationParamPhaseQualification.numberOfParticipantMatch + 1)
                        qualificationGroupSizeMinimum = competitionCreationParamPhaseQualification.numberOfParticipantMatch + 1;

                    if (qualificationGroupSizeMaximum > competitionCreationParamPhaseQualification.groupSizeMaximum)
                        qualificationGroupSizeMaximum = competitionCreationParamPhaseQualification.groupSizeMaximum;
                    if (qualificationGroupSizeMaximum < qualificationGroupSizeMinimum)
                        qualificationGroupSizeMaximum = qualificationGroupSizeMinimum;

                    competitionCreationParamPhaseQualification.groupSizeMinimum = qualificationGroupSizeMinimum;
                    competitionCreationParamPhaseQualification.groupSizeMaximum = qualificationGroupSizeMaximum;

                }

                if ((competitionCreationParamPhaseQualification.numberOfParticipantMatch == 2 && competitionCreationParamPhaseQualification.groupSizeMaximum > 64) || (competitionCreationParamPhaseQualification.numberOfParticipantMatch > 2 && competitionCreationParamPhaseQualification.groupSizeMaximum > competitionCreationParamPhaseQualification.numberOfParticipantMatch * 5)) {
                    competitionCreationParamPhaseQualification.tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                    if (competitionCreationParamPhaseQualification.tournamentFormatsAccepted.isEmpty()) {
                        if (competitionCreationParamPhaseQualification.numberOfParticipantMatch == 2) {
                            if (competitionCreationParamPhaseQualification.groupSizeMinimum <= 64) {
                                competitionCreationParamPhaseQualification.groupSizeMaximum = 64;
                                competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                            } else {
                                competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                            }
                        } else {
                            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                        }
                    }
                }
            }
        }
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = null;
        if (getMixingPhaseParameter() != null) {
            competitionCreationParamPhaseMixing = getMixingPhaseParameter();

            if (numberOfParticipantCompetition != null) {
                if (competitionCreationParamPhaseMixing.numberOfParticipant == null) {
                    competitionCreationParamPhaseMixing.numberOfParticipant = numberOfParticipantCompetition;
                }
                competitionCreationParamPhaseMixing.checkAndCorrectValues();
                if (getQualificationPhaseParameter() != null) {

                    int sliceSize = (int) Math.ceil((double) numberOfParticipantCompetition / (double) competitionCreationParamPhaseQualification.groupSizeMaximum);
                    int numberOfParticipantRemaining = numberOfParticipantCompetition;
                    int numberOfRoundMinimum;
                    int divisor = competitionCreationParamPhaseMixing.numberOfParticipantMatch;
                    if (divisor == 1)
                        divisor = 2;
                    for (numberOfRoundMinimum = 1; numberOfParticipantRemaining > sliceSize; numberOfRoundMinimum++) {
                        numberOfParticipantRemaining = (int) Math.ceil((double) numberOfParticipantRemaining / (double) divisor);
                    }
                    if (numberOfRoundMinimum == 1 && competitionCreationParamPhaseMixing.numberOfParticipant % divisor != 0)
                        numberOfRoundMinimum++;
                    if (numberOfRoundMinimum > competitionCreationParamPhaseMixing.numberOfRoundMaximum)
                        numberOfRoundMinimum = competitionCreationParamPhaseMixing.numberOfRoundMaximum;
                    if (competitionCreationParamPhaseMixing.numberOfRoundMinimum < numberOfRoundMinimum)
                        competitionCreationParamPhaseMixing.numberOfRoundMinimum = numberOfRoundMinimum;
                } else if (getFinalPhaseParameter() != null) {

                    if (competitionCreationParamPhaseFinal.groupSizeMaximum > competitionCreationParamPhaseMixing.numberOfParticipant) {
                        int numberOfRoundMinimum = (int) Math.sqrt(competitionCreationParamPhaseMixing.numberOfParticipant);
                        if (numberOfRoundMinimum == 1 && numberOfParticipantCompetition > competitionCreationParamPhaseMixing.numberOfParticipantMatch)
                            numberOfRoundMinimum++;
                        if (numberOfRoundMinimum > competitionCreationParamPhaseMixing.numberOfRoundMaximum) {
                            numberOfRoundMinimum = competitionCreationParamPhaseMixing.numberOfRoundMaximum;
                        }
                        if (competitionCreationParamPhaseMixing.numberOfRoundMinimum < numberOfRoundMinimum)
                            competitionCreationParamPhaseMixing.numberOfRoundMinimum = numberOfRoundMinimum;
                    }
                } else {
                    if (competitionCreationParamPhaseMixing.numberOfRoundMinimum == 1) {
                        int numberOfRoundMinimum = (int) Math.sqrt(competitionCreationParamPhaseMixing.numberOfParticipant);
                        if (numberOfRoundMinimum == 1 && numberOfParticipantCompetition > competitionCreationParamPhaseMixing.numberOfParticipantMatch)
                            numberOfRoundMinimum++;
                        if (numberOfRoundMinimum > competitionCreationParamPhaseMixing.numberOfRoundMaximum) {
                            numberOfRoundMinimum = competitionCreationParamPhaseMixing.numberOfRoundMaximum;
                        }
                        if (competitionCreationParamPhaseMixing.numberOfRoundMinimum < numberOfRoundMinimum)
                            competitionCreationParamPhaseMixing.numberOfRoundMinimum = numberOfRoundMinimum;
                    }
                }

            }
        }

        if (this.getFirstPhase().registrationOnTheFly) {
            for (CompetitionCreationParamPhase competitionCreationParamPhase : this.phases) {
                if (competitionCreationParamPhase.phaseIndex.compareTo(this.getFirstPhase().phaseIndex) != 0) {
                    competitionCreationParamPhase.registrationOnTheFly = false;
                }
            }
        }
        if (competitionDuration != null)
            checkForPhaseDurations();

    }

    public void checkForPhaseDurations() {
        Duration remainingDuration = competitionDuration;
//        boolean mixingDefined = false;
//        boolean qualificationDefined = false;
//        boolean finalDefined = false;
        int undefined = phases.size();
        CompetitionCreationParamPhase competitionCreationParamPhaseUndefined = null;
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            if (competitionCreationParamPhase.phaseDuration != null) {
                remainingDuration = remainingDuration.minus(Duration.parse(competitionCreationParamPhase.phaseDuration));
                undefined--;
//                if (gameEnvironmentConfigurationPhase instanceof GameEnvironmentConfigurationPhaseFinal) {
//                } else if (gameEnvironmentConfigurationPhase instanceof GameEnvironmentConfigurationPhaseMixing) {
//                } else if (gameEnvironmentConfigurationPhase instanceof GameEnvironmentConfigurationPhaseQualification) {
//                }
            } else {
                competitionCreationParamPhaseUndefined = competitionCreationParamPhase;
            }
        }
        if (undefined == 0) {
            CompetitionCreationParamPhase competitionCreationParamPhaseLast = getLastPhase();
            competitionCreationParamPhaseLast.phaseDuration = Duration.parse(getLastPhase().phaseDuration).plus(remainingDuration).toString();
        } else if (undefined == 1) {
            competitionCreationParamPhaseUndefined.phaseDuration = remainingDuration.toString();
        } else {
            Duration remainingDurationDivided = remainingDuration.dividedBy(undefined);
            for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
                if (competitionCreationParamPhase.phaseDuration == null) {
                    competitionCreationParamPhase.phaseDuration = remainingDurationDivided.toString();
                }
            }
        }

    }

    @Override
    public String toString() {
        return "CompetitionComputationParam{" +
                "competitionDuration=" + competitionDuration +
                '}' + super.toString();
    }

    public CompetitionComputationParam cloneCompetitionComputationParam() {
        CompetitionComputationParam competitionComputationParam = cloneCompetitionComputationParamWithoutPhase();
        for (CompetitionCreationParamPhase competitionCreationParamPhase :
                phases) {
            competitionComputationParam.phases.add(competitionCreationParamPhase.cloneCompetitionCreationParamPhase());
        }
        return competitionComputationParam;
    }


    public CompetitionComputationParam cloneCompetitionComputationParamWithoutPhase() {
        CompetitionComputationParam competitionComputationParam = (CompetitionComputationParam) super.clone(CompetitionComputationParam.class);
        competitionComputationParam.competitionDuration = competitionDuration;
        competitionComputationParam.competitionName = competitionName;
        competitionComputationParam.phases = new TreeSet<>();
        return competitionComputationParam;
    }


    public SortedSet<CompetitionComputationParam> createCompetitionComputationParamsListToTry() {
        SortedMap<Integer, SortedSet<CompetitionCreationParamPhase>> competitionCreationParamPhases = new TreeMap<>();
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            competitionCreationParamPhases.put(competitionCreationParamPhase.phaseIndex, competitionCreationParamPhase.createCompetitionCreationParamPhaseListToTry());
        }
//        SortedMap<Integer, SortedSet<CompetitionCreationParamPhase>> competitionCreationParamPhasesSorted = new TreeMap<>();
//        competitionCreationParamPhasesSorted.putAll(competitionCreationParamPhases);
        SortedSet<CompetitionComputationParam> competitionComputationParams = flatten(competitionCreationParamPhases);
        Sets.sort(competitionComputationParams);
        return competitionComputationParams;
    }

    private SortedSet<CompetitionComputationParam> flatten(SortedMap<Integer, SortedSet<CompetitionCreationParamPhase>> competitionCreationParamPhases) {
        SortedSet<CompetitionComputationParam> competitionComputationParams = flatten(null, competitionCreationParamPhases);
        return competitionComputationParams;
    }

    private SortedSet<CompetitionComputationParam> flatten(SortedSet<CompetitionComputationParam> competitionComputationParams, SortedMap<Integer, SortedSet<CompetitionCreationParamPhase>> competitionCreationParamPhases) {
//        SortedSet<CompetitionComputationParam> competitionComputationParamsResult = new TreeSet<>();
        SortedSet<CompetitionComputationParam> competitionComputationParamsNew = new TreeSet<>();
        if (competitionComputationParams != null && !competitionComputationParams.isEmpty()) {
            for (CompetitionComputationParam competitionComputationParam : competitionComputationParams) {
                for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionCreationParamPhases.get(competitionCreationParamPhases.firstKey())) {
                    if (competitionComputationParam.getLastPhase().numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) <= 0 &&
                            competitionComputationParam.getLastPhase().numberOfPlayMaximum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) <= 0) {
                        CompetitionComputationParam competitionComputationParamNew = competitionComputationParam.cloneCompetitionComputationParam();
                        competitionComputationParamNew.addPhase(competitionCreationParamPhase);
                        competitionComputationParamsNew.add(competitionComputationParamNew);
                    }
                }
            }
        } else {
            for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionCreationParamPhases.get(competitionCreationParamPhases.firstKey())) {
                CompetitionComputationParam competitionComputationParamNew = this.cloneCompetitionComputationParamWithoutPhase();
                competitionComputationParamNew.addPhase(competitionCreationParamPhase);
                competitionComputationParamsNew.add(competitionComputationParamNew);
            }
        }

        if (competitionCreationParamPhases.keySet().size() > 1) {
            Integer nextKey = (Integer) competitionCreationParamPhases.keySet().toArray()[1];
            SortedMap<Integer, SortedSet<CompetitionCreationParamPhase>> tailMap = competitionCreationParamPhases.tailMap(nextKey);
            if (!tailMap.isEmpty() && !tailMap.get(tailMap.firstKey()).isEmpty()) {
                competitionComputationParams = flatten(competitionComputationParamsNew, tailMap);
            } else {
                competitionComputationParams = competitionComputationParamsNew;
            }
        } else {
            competitionComputationParams = competitionComputationParamsNew;
        }
        return competitionComputationParams;
    }


    public Set<TournamentFormat> getTournamentFormatForGap() {
        Set<TournamentFormat> tournamentFormats = new HashSet<>();
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            tournamentFormats.addAll(competitionCreationParamPhase.tournamentFormatsAccepted);
        }
        if (tournamentFormats.isEmpty()) {
            tournamentFormats.add(TournamentFormat.SINGLE_ELIMINATION);
            tournamentFormats.add(TournamentFormat.DOUBLE_ELIMINATION);
        }
        return tournamentFormats;
    }

    public void fitToNumberOfParticipantCompetition() {
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = getFinalPhaseParameter();
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = getQualificationPhaseParameter();
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = getMixingPhaseParameter();
        if (getQualificationPhaseParameter() != null) {
            if (competitionCreationParamPhaseFinal != null) {
                if (competitionCreationParamPhaseFinal.groupSizeMaximum != null && numberOfParticipantCompetition <= competitionCreationParamPhaseFinal.groupSizeMaximum) {
                    boolean removeQualificationPhase = true;
                    if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN)) {
                        if (numberOfParticipantCompetition > 6 * competitionCreationParamPhaseFinal.numberOfParticipantMatch) {
                            if (competitionCreationParamPhaseQualification == null) {
                                if (numberOfParticipantCompetition % competitionCreationParamPhaseFinal.numberOfParticipantMatch != 0) {
                                    competitionCreationParamPhaseFinal.tournamentFormatsAccepted.remove(TournamentFormat.ROUND_ROBIN);
                                    if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.isEmpty())
                                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                                }
                            } else {
                                removeQualificationPhase = false;
                                int finalSize = competitionCreationParamPhaseFinal.numberOfParticipantMatch;
                                while (finalSize < numberOfParticipantCompetition) {
                                    finalSize = finalSize * competitionCreationParamPhaseFinal.numberOfParticipantMatch / competitionCreationParamPhaseFinal.participantQualifiedPerMatch;
                                }
                                competitionCreationParamPhaseFinal.groupSizeMaximum = finalSize;
                            }
                        }
                    }
                    if (removeQualificationPhase) {
                        removeQualificationPhase();
                        if (competitionCreationParamPhaseQualification != null && getMixingPhaseParameter() == null && numberOfParticipantCompetition > 2 * competitionCreationParamPhaseFinal.numberOfParticipantMatch && numberOfParticipantCompetition > competitionCreationParamPhaseFinal.groupSizeMaximum) {
                            competitionCreationParamPhaseMixing.numberOfRoundMaximum = 3;
                            competitionCreationParamPhaseMixing.numberOfRoundMinimum = 1;
                            competitionCreationParamPhaseMixing.numberOfParticipant = numberOfParticipantCompetition;
                            competitionCreationParamPhaseMixing.copyFrom(competitionCreationParamPhaseQualification);
                            if (competitionCreationParamPhaseQualification.registrationOnTheFly && TournamentFormat.allowFormat(competitionCreationParamPhaseQualification.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                                competitionCreationParamPhaseMixing.tournamentFormatsAccepted.add(TournamentFormat.LADDER);
                                competitionCreationParamPhaseMixing.registrationOnTheFly = competitionCreationParamPhaseQualification.registrationOnTheFly;
                            } else {
                                competitionCreationParamPhaseMixing.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                            }
                            setMixingPhase(competitionCreationParamPhaseMixing);
                        }
                    }
                }
            }
        }
        competitionCreationParamPhaseFinal = getFinalPhaseParameter();
        competitionCreationParamPhaseQualification = getQualificationPhaseParameter();
        competitionCreationParamPhaseMixing = getMixingPhaseParameter();
        if (competitionCreationParamPhaseFinal != null) {
            if (numberOfParticipantCompetition <= competitionCreationParamPhaseFinal.numberOfParticipantMatch) {
                competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
                if (competitionCreationParamPhaseMixing != null) {
                    removeMixingPhase();
                    if (competitionCreationParamPhaseMixing.registrationOnTheFly && TournamentFormat.allowFormat(competitionCreationParamPhaseMixing.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                        if (competitionCreationParamPhaseQualification != null) {
                            competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.LADDER);
                            competitionCreationParamPhaseQualification.registrationOnTheFly = competitionCreationParamPhaseMixing.registrationOnTheFly;
                        } else if (competitionCreationParamPhaseFinal != null) {
                            competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.LADDER);
                            competitionCreationParamPhaseFinal.registrationOnTheFly = competitionCreationParamPhaseMixing.registrationOnTheFly;
                        }
                    } else {
                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                    }
                }
            }
        }

    }

    public String toFingerPrint() {
        String json = JSONUtils.jsonObjectToString(this);
        String fingerprint = DigestUtils.md5Hex(json);
        return fingerprint;
    }

}

