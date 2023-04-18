package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankingComputationPolicyRank extends RankingComputationPolicy {

    public Integer winnerGapRank;
    public Integer qualifiedGapRank;

    @Override
    public void checkAndCorrectValues() {
        if (winnerGapRank == null)
            winnerGapRank = 1;
        if (qualifiedGapRank == null)
            qualifiedGapRank = 1;
    }
}
