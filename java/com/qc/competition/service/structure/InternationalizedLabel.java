package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.utils.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Duncan on 19/04/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternationalizedLabel implements Comparable<InternationalizedLabel>, Cloneable, Serializable, JSONObject {
    @XmlAttribute(name = "default")
    @JsonProperty("default")
    public String defaultLabel;
    @XmlElement(name = "map")
    @JsonProperty("map")
    public Map<String, String> internationalizedLabelMap = new HashMap<>();

    @Override
    public Object clone() {
        InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
        internationalizedLabel.defaultLabel = defaultLabel;
        internationalizedLabel.internationalizedLabelMap.putAll(internationalizedLabelMap);
        return internationalizedLabel;
    }

    @Override
    public int compareTo(InternationalizedLabel o) {
        if (defaultLabel == null && o.defaultLabel == null)
            return 0;
        else if (defaultLabel == null && o.defaultLabel != null)
            return -1;
        else if (o.defaultLabel == null)
            return 1;
        return defaultLabel.compareTo(o.defaultLabel);
    }

    public Element toSimpleDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("defaultLabel", "" + defaultLabel);
        if (!internationalizedLabelMap.isEmpty()) {
            for (String key : internationalizedLabelMap.keySet()) {
                element.setAttribute(key, internationalizedLabelMap.get(key));
            }
        }
        return element;
    }

    public Element toDescriptionXml(Document document) {
        return toSimpleDescriptionXml(document);
    }
}
