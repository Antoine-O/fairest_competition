package com.qc.competition.service.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Created by Duncan on 09/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionTablePair implements Comparable<DescriptionTablePair>, Serializable {
    private static final DescriptionTablePair SEPARATOR = new DescriptionTablePair("|", "|");
    @XmlAttribute(name = "title")
    public String title;
    @XmlAttribute(name = "value")
    public String value;

    public DescriptionTablePair() {
        super();
    }

    public DescriptionTablePair(String title, String value) {
        super();
        this.title = title;
        this.value = value;
    }

    public static DescriptionTablePair separator() {
        return SEPARATOR;
    }

    public boolean isSeparator() {
        return this.compareTo(SEPARATOR) == 0;
    }

    @Override
    public int compareTo(DescriptionTablePair descriptionTablePair) {
        int compare = this.title.compareTo(descriptionTablePair.title);
        if (compare == 0)
            compare = this.value.compareTo(descriptionTablePair.value);
        return compare;
    }
}
