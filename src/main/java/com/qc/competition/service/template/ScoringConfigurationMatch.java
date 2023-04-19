package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.ParticipantScoreAggregateType;
import com.qc.competition.utils.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoringConfigurationMatch implements Serializable, JSONObject, Cloneable {

    @XmlElementWrapper(name = "ScoringConfigurationMatchElements")
    @XmlElement(name = "ScoringConfigurationMatchElement")
    @JsonProperty("scoringConfigurationMatchElements ")
    public SortedSet<ScoringConfigurationMatchElement> scoringConfigurationMatchElements = new TreeSet<>();

    public RankingComputationPolicy rankingComputationPolicy;

    public void checkAndCorrectValues() {
        if (scoringConfigurationMatchElements.isEmpty()) {
//            scoringConfigurationMatchElements.add(new ScoringConfigurationMatchElement());
        }
        boolean rank = true;
        if (!scoringConfigurationMatchElements.isEmpty()) {
            rank = false;
            for (ScoringConfigurationMatchElement scoringConfigurationMatchElement : scoringConfigurationMatchElements) {
                scoringConfigurationMatchElement.checkAndCorrectValues();
                if (scoringConfigurationMatchElement.participantScoreAggregateType.compareTo(ParticipantScoreAggregateType.RANK) == 0) {
                    rank = true;
                    break;
                }
            }
        }
        if (rankingComputationPolicy == null) {
            if (rank) {
                rankingComputationPolicy = new RankingComputationPolicyRank();
            } else {
                rankingComputationPolicy = new RankingComputationPolicyScore();
            }
        }
        rankingComputationPolicy.checkAndCorrectValues();
    }
}
