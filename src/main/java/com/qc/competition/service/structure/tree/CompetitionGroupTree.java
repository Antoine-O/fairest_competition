package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionGroupTree extends CompetitionObjectTree {
    @XmlAttribute(name = "lane")
    public Integer lane;
    @XmlAttribute(name = "format")
    public CompetitionGroupFormat competitionGroupFormat;
    @XmlElement(name = "lastRound")
    public CompetitionRoundTree competitionRoundTreeLast;

    public String toHtmlCartouche(CompetitionSeedTree competitionSeedTree) {
        StringBuilder stringBuilder = new StringBuilder();
        String cssClass = "";
        if (this.filled) {
            cssClass += " filled ";
            if (this.over)
                cssClass += " over ";
            else
                cssClass += " in_progress ";
        } else {
            cssClass += " empty ";
        }


        stringBuilder.append("<div class='group ").append(cssClass).append(competitionGroupFormat.toString().toLowerCase()).append(" '>");
        if (this.over) {
            stringBuilder.append("<div class='results'>");


            stringBuilder.append("<div class='participant'>");
            stringBuilder.append("<div class='participant_name title'>Name</div>");
            stringBuilder.append("<div class='participant_rank title'>Rank</div>");
            stringBuilder.append("<div class='participant_points  title'>Points</div>");
            stringBuilder.append("</div>");

            for (ParticipantResultTree participantResultTree : participantResultTrees) {
                stringBuilder.append(participantResultTree.toHtmlCartouche());
            }
            stringBuilder.append("</div>");
        }

        if (competitionRoundTreeLast != null) {
            stringBuilder.append(System.lineSeparator());
            if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                if (lane == 1 && competitionSeedTree.competitionGroupTrees.size() > 1) {
                    stringBuilder.append("<div class='groupname'>[").append(lane - 1).append("] Winner's Bracket");
                    stringBuilder.append("</div>");
                } else if (lane > 1) {
                    stringBuilder.append("<div class='groupname'>[").append(lane - 1).append("] Looser's Bracket");
                    stringBuilder.append("</div>");
                } else {
                    stringBuilder.append("<div class='groupname'>[").append(lane - 1).append("] " + competitionSeedTree.stepType);
                    stringBuilder.append("</div>");
                }
            } else if (competitionSeedTree.competitionGroupTrees.size() > 1) {
                stringBuilder.append("<div class='groupname'>Group ");
                stringBuilder.append(this.lane);
                stringBuilder.append("</div>");
            }

            stringBuilder.append("<div class='groupStartDate").append(cssClass).append("'>");
            if (this.expectedRelativeStartTime != null)
                stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
            stringBuilder.append("</div>");
            stringBuilder.append("<div class='groupDuration").append(cssClass).append("'>");
            if (this.expectedDuration != null)
                stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
            stringBuilder.append("</div>");
            stringBuilder.append("<div class='groupEndDate").append(cssClass).append("'>");
            if (this.expectedRelativeEndTime != null)
                stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeEndTime.duration.toMillis(), "HH:mm", true));
            stringBuilder.append("</div>");

            stringBuilder.append("<div class='rounds rounds_").append(competitionRoundTreeLast.getDepth()).append("'>");

            StringBuilder stringBuilderRounds = new StringBuilder();

            if (competitionRoundTreeLast != null) {
                stringBuilderRounds.append(competitionRoundTreeLast.toHtmlCartouche(this, null));
            }

            stringBuilder.append(stringBuilderRounds);
            stringBuilder.append("</div>");


            stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append("</div>");
        return stringBuilder.toString();

    }

}
