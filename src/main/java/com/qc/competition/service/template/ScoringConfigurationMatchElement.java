package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.service.structure.ParticipantScoreAggregateType;
import com.qc.competition.service.structure.ScoreScaleType;
import com.qc.competition.utils.json.JSONObject;

import java.io.Serializable;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoringConfigurationMatchElement implements Serializable, Comparable<ScoringConfigurationMatchElement>, JSONObject, Cloneable {
    public Integer priority;
    public ParticipantScoreAggregateType participantScoreAggregateType;
    public ScoreScaleType scoreScaleType;


    @Override
    public int compareTo(ScoringConfigurationMatchElement o) {
        if (this.priority != null && o.priority != null)
            return this.priority.compareTo(o.priority);
        if (this.priority != null)
            return 1;
        if (o.priority != null)
            return -1;
        return participantScoreAggregateType.name().compareTo(o.participantScoreAggregateType.name());
    }


    public void checkAndCorrectValues() {
        if (participantScoreAggregateType == null)
            participantScoreAggregateType = ParticipantScoreAggregateType.RANK;


    }
}
