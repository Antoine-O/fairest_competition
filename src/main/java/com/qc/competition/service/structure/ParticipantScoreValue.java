package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Created by Duncan on 15/02/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipantScoreValue implements Comparable<ParticipantScoreValue>, Serializable {
    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute(name = "scale")
    @JsonProperty("scale")
    public ScoreScaleType scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC;
    @XmlAttribute(name = "value")
    public String value;
    public boolean userInput;
    public Integer priority;

    public void clear() {
        value = null;
    }

    @Override
    public int compareTo(ParticipantScoreValue o) {
        int compareValue = 0;
        if (o == null)
            compareValue = 1;
        if (compareValue == 0) {
            if (this.scoreScale.compareTo(o.scoreScale) != 0)
                throw new IllegalArgumentException(this.scoreScale.name() + " != " + o.scoreScale.name());
            if (value != null && o.value != null) {
                compareValue = this.scoreScale.compareValue(value, o.value);
            } else if (o.value != null) {
                compareValue = -1;
            } else if (value != null) {
                compareValue = 1;
            }
        }
        return compareValue;
    }

    public Number computeNumberValue() {
        return scoreScale.getValueFor(value);
    }


    @Override
    public String toString() {
        return name + "{" + value + "," + scoreScale + "}";
    }
}
