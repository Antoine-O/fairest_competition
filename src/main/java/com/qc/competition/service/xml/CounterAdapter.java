package com.qc.competition.service.xml;

import com.qc.competition.service.structure.Counter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CounterAdapter extends XmlAdapter<String, Counter> {

    public Counter unmarshal(String v) {
        return Counter.parse(v);
    }

    public String marshal(Counter v) {
        if (v != null)
            return "" + v.value;
        return null;
    }

}
