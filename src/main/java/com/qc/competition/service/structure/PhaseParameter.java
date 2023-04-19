package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.service.structure.adaptater.DurationAdapter;
import com.qc.competition.utils.json.JSONObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhaseParameter implements JSONObject, Cloneable {
    @XmlAttribute
    public Integer numberOfPlayMinimum;
    @XmlAttribute
    public Integer numberOfPlayMaximum;
    @XmlAttribute
    public Integer numberOfRoundMinimum;
    @XmlAttribute
    public Integer numberOfRoundMaximum;
    @XmlAttribute
    public Integer groupSizeMinimum;
    @XmlAttribute
    public Integer groupSizeMaximum;
    @XmlAttribute
    public ParticipantType participantType;
    @XmlAttribute
    public Integer numberOfParticipantMatch;
    @XmlAttribute
    public PlayVersusType playVersusType;
    @XmlAttribute
    public Integer participantQualifiedPerMatch;
    @XmlAttribute
    public Boolean allowEvenNumberOfPlay;
    @XmlAttribute
    public Boolean progressiveNumberOfPlay;

    @XmlAttribute
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration playDurationAverage;
    @XmlAttribute
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration playDurationMaximum;
    @XmlAttribute
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration playDurationMinimum;
    @XmlAttribute
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration intermissionDuration;
    @XmlAttribute
    @XmlList
    public Set<TournamentFormat> tournamentFormatsAccepted = new HashSet<>();
    @XmlAttribute
    public Integer eliminationLevelMaximum;
    @XmlAttribute
    public Integer eliminationLevelMinimum;
    @XmlAttribute
    public Integer maximumNumberOfParallelPlay;
    @XmlAttribute
    public Integer numberOfParticipantCompetition;

/*
    public void checkAndCorrectValues() {
//        Logger logger = LOGGER_checkAndCorrectValues;

        if (numberOfParticipantMatch < playVersusType.numberOfTeam)
            numberOfParticipantMatch = playVersusType.numberOfTeam;

        if (allowEvenNumberOfPlay == null)
            allowEvenNumberOfPlay = false;
        if (numberOfPlayMaximum == null)
            numberOfPlayMaximum = CompetitionServiceBridgeBean.MAX_NUMBER_OF_PLAYS;
        if (numberOfPlayMinimum == null)
            numberOfPlayMinimum = 1;
//        if (maximumNumberOfParallelPlay == null)
//            maximumNumberOfParallelPlay = 0;
        if (intermissionDuration == null) {
            intermissionDuration = Duration.ofMinutes(2);
            intermissionDuration = Duration.ofMinutes((int) Math.ceil(intermissionDuration.toMinutes() * Math.log10(numberOfParticipantCompetition)));
        }

        if (participantQualifiedPerMatch >= numberOfParticipantMatch)
            participantQualifiedPerMatch = numberOfParticipantMatch - numberOfParticipantMatch / 2;
        if (participantQualifiedPerMatch <= 0)
            participantQualifiedPerMatch = 1;

        if (progressiveNumberOfPlay == null)
            progressiveNumberOfPlay = this.numberOfPlayMinimum != this.numberOfPlayMaximum;
        if (groupSizeMaximum == null) {
            groupSizeMaximum = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(numberOfParticipantMatch, participantQualifiedPerMatch, Math.min(numberOfParticipantMatch * CompetitionComputationParam.MAX_MATCH_IN_GROUP, numberOfParticipantCompetition), false);
        }
        if (groupSizeMaximum > numberOfParticipantMatch * CompetitionComputationParam.MAX_MATCH_IN_GROUP) {
            groupSizeMaximum = numberOfParticipantMatch * CompetitionComputationParam.MAX_MATCH_IN_GROUP;
        }
        if (groupSizeMaximum < numberOfParticipantMatch * CompetitionComputationParam.MIN_MATCH_IN_GROUP) {
            groupSizeMaximum = numberOfParticipantMatch * CompetitionComputationParam.MIN_MATCH_IN_GROUP;
        }
        if (groupSizeMinimum == null) {
            groupSizeMinimum = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(numberOfParticipantMatch, participantQualifiedPerMatch, Math.min(numberOfParticipantMatch * CompetitionComputationParam.MIN_MATCH_IN_GROUP, numberOfParticipantCompetition), true);
        }
        if (groupSizeMinimum > numberOfParticipantMatch * CompetitionComputationParam.MAX_MATCH_IN_GROUP) {
            groupSizeMinimum = numberOfParticipantMatch * CompetitionComputationParam.MAX_MATCH_IN_GROUP;
        }
        if (groupSizeMinimum < numberOfParticipantMatch * CompetitionComputationParam.MIN_MATCH_IN_GROUP) {
            groupSizeMinimum = numberOfParticipantMatch * CompetitionComputationParam.MIN_MATCH_IN_GROUP;
        }
//        logger.log(Level.FINE, this.toString());
    }*/

}
