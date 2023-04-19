package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.ParticipantType;
import com.qc.competition.service.structure.TournamentFormat;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.json.JSONObject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionCreationParam implements Serializable, Comparable<CompetitionCreationParam>, JSONObject {
    @XmlAttribute(name = "numberOfParticipantCompetition")
    public Integer numberOfParticipantCompetition;
    @XmlAttribute(name = "participantType")
    public ParticipantType participantType;
    @XmlElementWrapper(name = "phases")
    @XmlElement(name = "phase")
    @JsonProperty("phase")
    public SortedSet<CompetitionCreationParamPhase> phases = new TreeSet<>();
    @XmlAttribute(name = "sharerPercentageLimit")
    public Integer sharerPercentageLimit;

    public CompetitionCreationParam() {

    }

    public CompetitionCreationParamPhaseMixing getMixingPhaseParameter() {
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = null;
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseMixing) {
                competitionCreationParamPhaseMixing = (CompetitionCreationParamPhaseMixing) competitionCreationParamPhase;
                break;
            }

        }
        return competitionCreationParamPhaseMixing;
    }

    public CompetitionCreationParamPhaseQualification getQualificationPhaseParameter() {
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = null;
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification) {
                competitionCreationParamPhaseQualification = (CompetitionCreationParamPhaseQualification) competitionCreationParamPhase;
                break;
            }

        }
        return competitionCreationParamPhaseQualification;
    }

    public CompetitionCreationParamPhaseFinal getFinalPhaseParameter() {
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = null;
        for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
                competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
                break;
            }

        }
        return competitionCreationParamPhaseFinal;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return "CompetitionCreationParam{" +
                "numberOfParticipantCompetition=" + numberOfParticipantCompetition +
                "participantType=" + participantType +
                '}'
                + "\t" + (getMixingPhaseParameter() != null ? getMixingPhaseParameter().tournamentFormatsAccepted + "\t" + getMixingPhaseParameter().numberOfPlayMinimum + "-" + getMixingPhaseParameter().numberOfPlayMaximum + "\t" : "\t")
                + "\t" + (getQualificationPhaseParameter() != null ? getQualificationPhaseParameter().tournamentFormatsAccepted + "\t" + getQualificationPhaseParameter().numberOfPlayMinimum + "-" + getQualificationPhaseParameter().numberOfPlayMaximum + "\t" : "\t")
                + "\t" + (getFinalPhaseParameter() != null ? getFinalPhaseParameter().tournamentFormatsAccepted + "\t" + getFinalPhaseParameter().numberOfPlayMinimum + "-" + getFinalPhaseParameter().numberOfPlayMaximum + "\t" : "\t")
                ;
    }

    public String toSimpleDescription() {
        String description = toString() + System.lineSeparator() +
                "numberOfParticipantCompetition\t:" + this.numberOfParticipantCompetition + System.lineSeparator() +
                "participantType\t:" + this.participantType + System.lineSeparator();

        return description;
    }

    public CompetitionCreationParam clone(Class<? extends CompetitionCreationParam> competitionCreationParamClass) {
        CompetitionCreationParam competitionCreationParam = null;
        try {
            competitionCreationParam = competitionCreationParamClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        competitionCreationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;
        competitionCreationParam.participantType = participantType;
        competitionCreationParam.sharerPercentageLimit = sharerPercentageLimit;


        return competitionCreationParam;
    }

    @Override
    public int compareTo(CompetitionCreationParam competitionCreationParam) {
        int compare = 0;
        if (compare == 0)
            compare = numberOfParticipantCompetition.compareTo(competitionCreationParam.numberOfParticipantCompetition);
        if (compare == 0)
            compare = participantType.compareTo(competitionCreationParam.participantType);
        if (compare == 0) {
            if (getMixingPhaseParameter() == null && competitionCreationParam.getMixingPhaseParameter() == null) {
            } else if (competitionCreationParam.getMixingPhaseParameter() == null) {
                compare = 1;
            } else if (this.getMixingPhaseParameter() == null) {
                compare = -1;
            } else {
                compare = getMixingPhaseParameter().compareTo(competitionCreationParam.getMixingPhaseParameter());
            }
        }
        if (compare == 0) {
            if (getQualificationPhaseParameter() == null && competitionCreationParam.getQualificationPhaseParameter() == null) {
            } else if (competitionCreationParam.getQualificationPhaseParameter() == null) {
                compare = 1;
            } else if (this.getQualificationPhaseParameter() == null) {
                compare = -1;
            } else {
                compare = getQualificationPhaseParameter().compareTo(competitionCreationParam.getQualificationPhaseParameter());
            }
        }
        if (compare == 0) {
            if (getFinalPhaseParameter() == null && competitionCreationParam.getFinalPhaseParameter() == null) {
            } else if (competitionCreationParam.getFinalPhaseParameter() == null) {
                compare = 1;
            } else if (this.getFinalPhaseParameter() == null) {
                compare = -1;
            } else {
                compare = getFinalPhaseParameter().compareTo(competitionCreationParam.getFinalPhaseParameter());
            }
        }


        return compare;
    }

    public String[] toParams() {
        SortedSet<String> paramsSorted = new TreeSet<>();
        paramsSorted.add("numberOfParticipantCompetition:\t" + numberOfParticipantCompetition);
        paramsSorted.add("participantType:\t" + participantType);

        paramsSorted = Sets.sort(paramsSorted);
        String[] paramsArray = new String[paramsSorted.size()];
        paramsSorted.toArray(paramsArray);
        return paramsArray;
    }

    public CompetitionCreationParamPhase getFirstPhase() {
        CompetitionCreationParamPhase competitionCreationParamPhaseFirst = null;
        if (this.phases != null && !this.phases.isEmpty()) {
            for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
                if (competitionCreationParamPhaseFirst == null || competitionCreationParamPhaseFirst.phaseIndex.compareTo(competitionCreationParamPhase.phaseIndex) > 0)
                    competitionCreationParamPhaseFirst = competitionCreationParamPhase;
            }
        }
        return competitionCreationParamPhaseFirst;
    }


    public CompetitionCreationParamPhase getLastPhase() {
        CompetitionCreationParamPhase competitionCreationParamPhaseLast = null;
        if (this.phases != null && !this.phases.isEmpty()) {
            for (CompetitionCreationParamPhase competitionCreationParamPhase : phases) {
                if (competitionCreationParamPhaseLast == null || competitionCreationParamPhaseLast.phaseIndex.compareTo(competitionCreationParamPhase.phaseIndex) < 0)
                    competitionCreationParamPhaseLast = competitionCreationParamPhase;
            }
        }
        return competitionCreationParamPhaseLast;
    }

    public void removeQualificationPhase() {
        CompetitionCreationParamPhase competitionCreationParamPhase = getQualificationPhaseParameter();
        if (competitionCreationParamPhase != null) {
            phases.remove(competitionCreationParamPhase);
            if (competitionCreationParamPhase.phaseIndex.compareTo(getFirstPhase().phaseIndex) == 0) {
                boolean registrationOnTheFly = false;
                registrationOnTheFly = competitionCreationParamPhase.registrationOnTheFly;
                if (getFirstPhase().tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(getFirstPhase().tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                    getFirstPhase().registrationOnTheFly = registrationOnTheFly;
                }

            }

        }
        reIndexPhase();

    }

    private void reIndexPhase() {
        int previousIndex = 0;
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = getMixingPhaseParameter();
        if (competitionCreationParamPhaseMixing != null) {
            previousIndex++;
            competitionCreationParamPhaseMixing.phaseIndex = previousIndex;
        }
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = getQualificationPhaseParameter();
        if (competitionCreationParamPhaseQualification != null) {
            previousIndex++;
            competitionCreationParamPhaseQualification.phaseIndex = previousIndex;
            if (competitionCreationParamPhaseQualification.phaseIndex > 1)
                competitionCreationParamPhaseQualification.registrationOnTheFly = false;
        }
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = getFinalPhaseParameter();
        if (competitionCreationParamPhaseFinal != null) {
            previousIndex++;
            competitionCreationParamPhaseFinal.phaseIndex = previousIndex;
            if (competitionCreationParamPhaseFinal.phaseIndex > 1)
                competitionCreationParamPhaseFinal.registrationOnTheFly = false;
        }
    }

    public void removeMixingPhase() {
        CompetitionCreationParamPhase competitionCreationParamPhase = getMixingPhaseParameter();

        if (competitionCreationParamPhase != null) {
            boolean registrationOnTheFly = competitionCreationParamPhase.registrationOnTheFly;
            phases.remove(competitionCreationParamPhase);
            if (getFirstPhase().tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(getFirstPhase().tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                getFirstPhase().registrationOnTheFly = registrationOnTheFly;
            }
        }
        reIndexPhase();

    }

    public void removeFinalPhase() {
        CompetitionCreationParamPhase competitionCreationParamPhase = getFinalPhaseParameter();
        if (competitionCreationParamPhase != null)
            phases.remove(competitionCreationParamPhase);
        reIndexPhase();

    }

    public void setQualificationPhase(CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification) {
        boolean registrationOnTheFly = getFirstPhase() != null && getFirstPhase().registrationOnTheFly;
        removeQualificationPhase();
        if (competitionCreationParamPhaseQualification != null) {

            phases.add(competitionCreationParamPhaseQualification);
        }
        reIndexPhase();
    }

    public void setMixingPhase(CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing) {
        boolean registrationOnTheFly = getFirstPhase() != null && getFirstPhase().registrationOnTheFly;
        removeMixingPhase();
        if (competitionCreationParamPhaseMixing != null)
            phases.add(competitionCreationParamPhaseMixing);
        reIndexPhase();
        getFirstPhase().registrationOnTheFly = registrationOnTheFly;
    }


    public void setFinalPhase(CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal) {
        boolean registrationOnTheFly = getFirstPhase() != null && getFirstPhase().registrationOnTheFly;
        removeFinalPhase();
        if (competitionCreationParamPhaseFinal != null)
            phases.add(competitionCreationParamPhaseFinal);
        reIndexPhase();
        getFirstPhase().registrationOnTheFly = registrationOnTheFly;
    }

    protected void addPhase(CompetitionCreationParamPhase competitionCreationParamPhase) {
        phases.add(competitionCreationParamPhase);
        reIndexPhase();
        if (competitionCreationParamPhase.phaseIndex.compareTo(getFirstPhase().phaseIndex) != 0) {
            competitionCreationParamPhase.registrationOnTheFly = false;
        }
    }

    public boolean greaterPlayQuantityWithSameFormat(CompetitionComputationParam competitionCreationParam) {
        boolean result = false;
        if (competitionCreationParam.getMixingPhaseParameter() != null && this.getMixingPhaseParameter() != null) {
            result = getMixingPhaseParameter().greaterPlayQuantityWithSameFormat(competitionCreationParam.getMixingPhaseParameter());
        }
        if (!result) {
            if (competitionCreationParam.getQualificationPhaseParameter() != null && this.getQualificationPhaseParameter() != null) {
                result = getQualificationPhaseParameter().greaterPlayQuantityWithSameFormat(competitionCreationParam.getQualificationPhaseParameter());
            }
        }
        if (!result) {
            if (competitionCreationParam.getFinalPhaseParameter() != null && this.getFinalPhaseParameter() != null) {
                result = getFinalPhaseParameter().greaterPlayQuantityWithSameFormat(competitionCreationParam.getFinalPhaseParameter());
            }
        }


        return result;
    }

    public boolean greaterPlayQuantity(CompetitionComputationParam competitionCreationParam) {
        boolean result = false;
        if (competitionCreationParam.getMixingPhaseParameter() != null && this.getMixingPhaseParameter() != null) {
            result = getMixingPhaseParameter().greaterPlayQuantity(competitionCreationParam.getMixingPhaseParameter());
        }
        if (!result) {
            if (competitionCreationParam.getQualificationPhaseParameter() != null && this.getQualificationPhaseParameter() != null) {
                result = getQualificationPhaseParameter().greaterPlayQuantity(competitionCreationParam.getQualificationPhaseParameter());
            }
        }
        if (!result) {
            if (competitionCreationParam.getFinalPhaseParameter() != null && this.getFinalPhaseParameter() != null) {
                result = getFinalPhaseParameter().greaterPlayQuantity(competitionCreationParam.getFinalPhaseParameter());
            }
        }


        return result;
    }

    public CompetitionCreationParam diff(CompetitionCreationParam competitionCreationParam, CompetitionCreationParam competitionCreationParamResult) {
        List<CompetitionCreationParamPhase> competitionCreationParamPhaseArray = new ArrayList(this.phases);
        List<CompetitionCreationParamPhase> competitionCreationParamPhasesOtherArray = new ArrayList(competitionCreationParam.phases);
        for (int i = 0; i < competitionCreationParamPhaseArray.size(); i++) {
            if (competitionCreationParamPhasesOtherArray.size() > i) {
                CompetitionCreationParamPhase competitionCreationParamPhaseDiff = competitionCreationParamPhaseArray.get(i).diff(competitionCreationParamPhasesOtherArray.get(i));
                if (competitionCreationParamPhaseDiff != null)
                    competitionCreationParamResult.addPhase(competitionCreationParamPhaseDiff);
            }
        }

        return competitionCreationParamResult;
    }
}
