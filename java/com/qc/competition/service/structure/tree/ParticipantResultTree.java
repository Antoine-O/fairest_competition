package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantResultTree {
    @XmlAttribute(name = "label")
    public String label;
    @XmlAttribute(name = "localId")
    public String localId;
    @XmlElement(name = "participants")
    public ParticipantTree participantTree;
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId;
    @XmlAttribute(name = "rank")
    public Integer rank;
    @XmlAttribute(name = "points")
    public Integer points;
    @XmlAttribute(name = "badPoints")
    public Integer badPoints;
    @XmlAttribute(name = "goal")
    public Integer goal;
    @XmlAttribute(name = "slicePercent")
    public Integer slicePercent;
    @XmlAttribute(name = "sliceValue")
    public Integer sliceValue;
    @XmlAttribute(name = "qualifed")
    public Boolean qualified;
    @XmlElement(name = "team")
    public ParticipantTree participantTeam;
    @XmlAttribute(name = "teamId")
    public String teamLocalId;

    public String toHtmlCartouche() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("<div class='participant");
        if (sliceValue != null && sliceValue > 0)
            stringBuilder.append(" prizer ");
        if (rank != null && rank == 1)
            stringBuilder.append(" first ");

        if (points != null && points > 0)
            stringBuilder.append(" points_level_").append((int) Math.ceil(Math.log10(points)));
        stringBuilder.append(" '>");

        stringBuilder.append("<div class='participant_name'>");
        if (participantTree != null && participantTree.participantTrees != null && !participantTree.participantTrees.isEmpty()) {
            stringBuilder.append(participantTree.toHtmlCartouche(true));
        } else {
            if (label != null)
                stringBuilder.append(label);
        }
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='participant_rank'>");
        if (rank != null)
            stringBuilder.append(rank);
        stringBuilder.append("</div>");

        stringBuilder.append("<div class='participant_points'>");
        if (points != null && points > 0) stringBuilder.append(points);
        stringBuilder.append("</div>");
        if (slicePercent != null) {
            stringBuilder.append("<div class='participant_percent'>");
            if (slicePercent > 0) stringBuilder.append(slicePercent);
            stringBuilder.append("</div>");
        }
        if (sliceValue != null) {
            stringBuilder.append("<div class='participant_prize'>");
            if (sliceValue != null && sliceValue > 0) stringBuilder.append(sliceValue);
            stringBuilder.append("</div>");
        }
        stringBuilder.append("</div>");

        return stringBuilder.toString();
    }
}
