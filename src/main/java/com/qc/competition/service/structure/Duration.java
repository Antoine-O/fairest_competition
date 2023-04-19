package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Created by Duncan on 13/05/2015.
 */

@JsonDeserialize(using = DurationDeserializer.class)
@JsonSerialize(using = DurationSerializer.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Duration implements Serializable, Comparable<Duration> {
    @XmlTransient
    @JsonIgnore
    public java.time.Duration duration;

    public String durationValue;

    public Duration() {

    }

    public Duration(String durationValue) {
        this.durationValue = durationValue;
        if (durationValue != null)
            this.duration = java.time.Duration.parse(durationValue);
    }


    public Duration(java.time.Duration duration) {
        this.duration = duration;
        this.durationValue = this.duration.toString();
    }

    public static Duration parse(String durationValue) {
        return new Duration(durationValue);
    }

    public static Duration fromString(String durationValue) {
        return new Duration(durationValue);
    }

    public static Duration ofMinutes(long minutes) {
        return new Duration(java.time.Duration.ofMinutes(minutes));
    }

    public static Duration ofHours(int hours) {
        return new Duration(java.time.Duration.ofHours(hours));
    }

    @Override
    public String toString() {
        return durationValue;
    }

    public int compareTo(Duration expectedDuration) {

        return getDuration().compareTo(expectedDuration.getDuration());
    }

    private java.time.Duration getDuration() {
        java.time.Duration duration = this.duration;
        if (duration == null)
            duration = java.time.Duration.ZERO;
        return duration;
    }

    public long toMinutes() {
        return duration != null ? duration.toMinutes() : 0;
    }

    public Duration minus(Duration duration) {
        return new Duration(this.getDuration().minus(duration.duration));
    }

    public Duration plus(Duration duration) {
        return new Duration(this.getDuration().plus(duration.duration));
    }

    public Duration dividedBy(long divisor) {
        return new Duration(this.getDuration().dividedBy(divisor));
    }

    public Duration multipliedBy(long divisor) {
        return new Duration(this.getDuration().multipliedBy(divisor));
    }

    public Duration abs() {
        return new Duration(getDuration().abs());
    }
}
