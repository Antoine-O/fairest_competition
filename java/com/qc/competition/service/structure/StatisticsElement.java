package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Duncan on 18/09/2015.
 */
public interface StatisticsElement {
    @XmlTransient
    @JsonIgnore
    StatisticsStructure getExpectedGlobalDuration();

    @XmlTransient
    @JsonIgnore
    StatisticsStructure getExpectedGlobalPlay();

    @XmlTransient
    @JsonIgnore
    StatisticsStructure getExpectedParticipantDuration();

    @XmlTransient
    @JsonIgnore
    StatisticsStructure getExpectedParticipantPlay();

    @XmlTransient
    @JsonIgnore
    StatisticsStructure getExpectedGlobalStep();


    void fillStatistics();

    void resetStatistics();
}
