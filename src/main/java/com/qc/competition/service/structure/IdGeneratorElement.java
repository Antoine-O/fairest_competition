package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qc.competition.service.json.CounterDeserialiser;
import com.qc.competition.service.json.CounterSerializer;
import com.qc.competition.service.xml.CounterAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.NumberFormat;

/**
 * Created by Duncan on 04/01/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdGeneratorElement {
    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(4);
        numberFormat.setGroupingUsed(false);
    }

    @XmlAttribute(name = "key")
    @JsonProperty("key")
    String key;
    @XmlAttribute(name = "value")
    @JsonProperty("value")
    @XmlJavaTypeAdapter(type = Counter.class, value = CounterAdapter.class)
    @JsonDeserialize(using = CounterDeserialiser.class)
    @JsonSerialize(using = CounterSerializer.class)
    Counter counter;

    public IdGeneratorElement() {
    }

    public IdGeneratorElement(String key) {
        this.key = key;
        this.counter = new Counter();
    }


    synchronized public Integer getId() {
        Integer id = counter.increment();
        return id;
    }

    synchronized public void reset(Integer value) {
        counter.reset(value);
    }
}