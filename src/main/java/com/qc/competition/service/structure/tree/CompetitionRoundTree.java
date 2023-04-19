package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionRoundTree extends CompetitionObjectTree {
    @XmlElementWrapper(name = "matches")
    @XmlElement(name = "match")
    @JsonProperty("matches")
    public List<CompetitionMatchTree> competitionMatchTrees = new ArrayList<>();

    @XmlAttribute(name = "round")
    public Integer round;
    @XmlAttribute(name = "phaseSequence")
    public Integer phaseSequence;
    @XmlAttribute(name = "competitionSequence")
    public Integer competitionSequence;
    @XmlElement(name = "previous")
    public CompetitionRoundTree competitionRoundTreePrevious;
    @XmlElement(name = "before")
    public HashSet<String> beforeRoundIds;


    public String toHtmlCartouche(CompetitionGroupTree competitionGroupTree, CompetitionRoundTree competitionRoundTreeNext) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());

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


        stringBuilder.append("<div class='round ").append(cssClass).append(competitionGroupTree.competitionGroupFormat.toString().toLowerCase()).append("' >");
//        if (competitionRoundTreeNext != null || competitionRoundTreePrevious != null) {
        stringBuilder.append("<div class='roundnumber'>Round ");
        stringBuilder.append(this.round);
        stringBuilder.append("[" + this.id + "]");
        stringBuilder.append("[PS:" + this.phaseSequence + "]");
        stringBuilder.append("[CS:" + this.competitionSequence + "]");
        if (beforeRoundIds != null && !beforeRoundIds.isEmpty()) {
            stringBuilder.append("[");
            for (String beforeRoundId : beforeRoundIds) {
                stringBuilder.append(beforeRoundId);
                stringBuilder.append(" ");
            }
            stringBuilder.append("]");
        }
        stringBuilder.append("</div>");
//        }


        stringBuilder.append("<div class='roundStartDate").append(cssClass).append("'>");
        if (this.expectedRelativeStartTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='roundDuration").append(cssClass).append("'>");
        if (this.expectedDuration != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='roundEndDate").append(cssClass).append("'>");
        if (this.expectedRelativeEndTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeEndTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");

        stringBuilder.append("<div class='match matches_").append(competitionMatchTrees.size()).append("'>");
        for (CompetitionMatchTree competitionMatchTree : competitionMatchTrees) {
            stringBuilder.append(competitionMatchTree.toHtmlCartouche());
        }
        stringBuilder.append("</div>");

        stringBuilder.append("</div>");

        stringBuilder.append(System.lineSeparator());

        if (competitionRoundTreePrevious != null)
            stringBuilder.append(competitionRoundTreePrevious.toHtmlCartouche(competitionGroupTree, this));

        return stringBuilder.toString();

    }

    public int getDepth() {
        int depth = 1;
        if (competitionRoundTreePrevious != null)
            depth += competitionRoundTreePrevious.getDepth();
        return depth;
    }
}