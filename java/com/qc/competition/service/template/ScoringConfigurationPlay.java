package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.ParticipantScoreType;
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
public class ScoringConfigurationPlay implements Serializable, JSONObject, Cloneable {

    @XmlElementWrapper(name = "ScoringConfigurationPlayElements")
    @XmlElement(name = "ScoringConfigurationPlayElement")
    @JsonProperty("scoringConfigurationPlayElement")
    public SortedSet<ScoringConfigurationPlayElement> scoringConfigurationPlayElements = new TreeSet<>();
    public RankingComputationPolicy rankingComputationPolicy;


    public void checkAndCorrectValues() {
        if (scoringConfigurationPlayElements.isEmpty()) {
//            scoringConfigurationPlayElements.add(new ScoringConfigurationPlayElement());
        }
        boolean rank = true;
        if (!scoringConfigurationPlayElements.isEmpty()) {
            rank = false;
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfigurationPlayElements) {
                scoringConfigurationPlayElement.checkAndCorrectValues();
                if (scoringConfigurationPlayElement.participantScoreType.compareTo(ParticipantScoreType.RANK) == 0) {
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
