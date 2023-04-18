package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.db.entity.game.ScoreThresholdType;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankingComputationPolicyScore extends RankingComputationPolicy {
    public ScoreThresholdType scoreThresholdType;
    public String threshold;
    public String winnerGapScore;
    public String qualifiedGapScore;
    public String scoreName;

    @Override
    public void checkAndCorrectValues() {
        if (winnerGapScore == null)
            winnerGapScore = "0";
        if (qualifiedGapScore == null)
            qualifiedGapScore = "0";
    }
}
