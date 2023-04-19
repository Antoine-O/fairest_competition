package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantTree {
    @XmlAttribute(name = "label")
    public String label;
    @XmlAttribute(name = "localId")
    public String localId;
    @XmlElementWrapper(name = "participants")
    @XmlElement(name = "participants")
    @JsonProperty("participants")
    public List<ParticipantTree> participantTrees = new ArrayList<>();
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId;

    public String toHtmlCartouche(boolean withMember) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("<div class='participant");

        if (!participantTrees.isEmpty() && withMember) {
            stringBuilder.append(" participant_team'>");
            stringBuilder.append("<div class='name'>").append(label).append("</div>");
            stringBuilder.append("<ul class='participant_team_members'>");
            for (ParticipantTree participantTree : participantTrees) {
                stringBuilder.append("<li class='participant_team_member'>");
                stringBuilder.append(participantTree.toHtmlCartouche(withMember));
                stringBuilder.append("</li>");
            }
            stringBuilder.append("</ul>");
            stringBuilder.append("</div>");
        } else {
            stringBuilder.append("'>");
            stringBuilder.append("<div class='name'>").append(label).append("</div>");
            stringBuilder.append("</div>");

        }

        return stringBuilder.toString();
    }
}
