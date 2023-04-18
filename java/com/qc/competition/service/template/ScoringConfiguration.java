package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.utils.json.JSONObject;

import java.io.Serializable;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoringConfiguration implements Serializable, JSONObject, Cloneable {
    public ScoringConfigurationPlay scoringConfigurationPlay;
    public ScoringConfigurationMatch scoringConfigurationMatch;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void checkAndCorrectValues() {
        if (scoringConfigurationPlay == null)
            scoringConfigurationPlay = new ScoringConfigurationPlay();
        scoringConfigurationPlay.checkAndCorrectValues();
        if (scoringConfigurationMatch == null)
            scoringConfigurationMatch = new ScoringConfigurationMatch();
        scoringConfigurationMatch.checkAndCorrectValues();
    }
}
