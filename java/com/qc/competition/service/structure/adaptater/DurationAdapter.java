package com.qc.competition.service.structure.adaptater;

import com.qc.competition.ws.simplestructure.Duration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Duncan on 14/07/2015.
 */
public class DurationAdapter
        extends XmlAdapter<String, Duration> {

    public Duration unmarshal(String v) {
        return Duration.parse(v);
    }

    public String marshal(Duration v) {
        if (v != null)
            return v.toString();
        return null;
    }

}