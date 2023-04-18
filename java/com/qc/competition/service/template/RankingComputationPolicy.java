package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.qc.competition.utils.json.JSONObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
//import java.time.Duration;

/**
 * Created by Duncan on 20/09/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({RankingComputationPolicyScore.class,
        RankingComputationPolicyRank.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RankingComputationPolicyScore.class, name = "RankingComputationPolicyScore"),
        @JsonSubTypes.Type(value = RankingComputationPolicyRank.class, name = "RankingComputationPolicyRank")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RankingComputationPolicy implements Serializable, JSONObject, Cloneable {
    public String type;

    public abstract void checkAndCorrectValues();
}
