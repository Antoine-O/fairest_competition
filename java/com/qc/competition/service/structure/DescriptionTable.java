package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 25/09/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DescriptionTable implements Serializable {
    @XmlElementWrapper(name = "pairs")
    @XmlElement(name = "pair")
    @JsonProperty("pairs")
    public List<DescriptionTablePair> descriptionTablePairs = new ArrayList<>();

    public void append(String title, Object value) {
        DescriptionTablePair descriptionTablePair = new DescriptionTablePair(title, value.toString());
        this.append(descriptionTablePair);
    }

    public void append(DescriptionTable descriptionTable) {
        descriptionTablePairs.add(DescriptionTablePair.separator());
        descriptionTablePairs.addAll(descriptionTable.descriptionTablePairs);
    }


    @Override
    public String toString() {
        return toString(true, true);
    }


    public String toString(boolean withTitle, boolean vertical) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderTitle = new StringBuilder();
        StringBuilder stringBuilderValue = new StringBuilder();
        if (withTitle) {
            for (DescriptionTablePair descriptionTablePair : descriptionTablePairs) {
                if (descriptionTablePair.isSeparator()) {
                    stringBuilder.append(stringBuilderTitle);
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append(stringBuilderValue);
                    stringBuilder.append(System.lineSeparator());
                    stringBuilderTitle = new StringBuilder();
                    stringBuilderValue = new StringBuilder();
                } else {
                    stringBuilderTitle.append(descriptionTablePair.title).append("\t");
                    stringBuilderValue.append(descriptionTablePair.value).append("\t");
                }
            }
        }
        stringBuilder.append(stringBuilderTitle);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(stringBuilderValue);
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public void prefix(String title, Object value) {
        DescriptionTablePair descriptionTablePair = new DescriptionTablePair(title, value.toString());
        this.prefix(descriptionTablePair);
    }

    public void append(DescriptionTablePair descriptionTablePair) {
        this.descriptionTablePairs.add(descriptionTablePair);
    }

    public void prefix(DescriptionTablePair descriptionTablePair) {
        this.descriptionTablePairs.add(0, descriptionTablePair);
    }
}
