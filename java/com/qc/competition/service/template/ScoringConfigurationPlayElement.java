package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.service.structure.ParticipantScoreType;
import com.qc.competition.service.structure.ScoreScaleType;
import com.qc.competition.utils.json.JSONObject;

import java.io.Serializable;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoringConfigurationPlayElement implements Serializable, Comparable<ScoringConfigurationPlayElement>, JSONObject, Cloneable {
    public Integer priority;
    public ParticipantScoreType participantScoreType;
    public ScoreScaleType scoreScaleType;
    public boolean userInput;
    public String name;


    @Override
    public int compareTo(ScoringConfigurationPlayElement o) {
        if (this.priority != null && o.priority != null)
            return this.priority.compareTo(o.priority);
        if (this.priority != null)
            return 1;
        if (o.priority != null)
            return -1;
        return name.compareTo(o.name);
    }

    public void checkAndCorrectValues() {
        if (participantScoreType == null)
            participantScoreType = ParticipantScoreType.RANK;

    }
}
