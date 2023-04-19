package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.*;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlType(propOrder = {"competitionGroupTrees", "competitionSeedTrees"})
public class CompetitionSeedTree extends CompetitionObjectTree {

    @XmlAttribute(name = "nextCompetitionSeedIds")
    public List<String> nextCompetitionSeedIds = new ArrayList<>();

    @XmlAttribute(name = "previousCompetitionSeedIds")
    public List<String> previousCompetitionSeedIds = new ArrayList<>();

    @XmlAttribute(name = "participantFilteringMethod")
    @JsonProperty("participantFilteringMethod")
    public ParticipantFilteringMethod participantFilteringMethod;

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    @JsonProperty("groups")
    public List<CompetitionGroupTree> competitionGroupTrees = new ArrayList<>();


    @XmlElementWrapper(name = "seeds")
    @XmlElement(name = "seed")
    @JsonProperty("seeds")
    public List<CompetitionSeedTree> competitionSeedTrees = new ArrayList<>();


    @XmlElementWrapper(name = "phases")
    @XmlElement(name = "phase")
    @JsonProperty("phases")
    public List<CompetitionPhaseTree> competitionPhaseTrees = new ArrayList<>();

    @XmlAttribute(name = "participantPairingMethod")
    public ParticipantPairingMethod participantPairingMethod;
    @XmlAttribute(name = "filteringUnit")
    public Unit filteringUnit;
    @XmlAttribute(name = "filteringValue")
    public int filteringValue;
    //    @XmlAttribute(name = "phase")
//    public int phase;
    public StepType stepType;


    public String toHtmlCartouche(CompetitionPhaseTree competitionPhaseTree) {
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

        String competitionFormatCss = "";
        CompetitionGroupFormat competitionGroupFormat = null;
        if (!competitionGroupTrees.isEmpty())
            competitionGroupFormat = competitionGroupTrees.get(0).competitionGroupFormat;
        if (competitionGroupFormat != null)
            competitionFormatCss = competitionGroupTrees.get(0).competitionGroupFormat.toString().toLowerCase();
        String seedStepType = "";
        if (previousCompetitionSeedIds.isEmpty())
            seedStepType += " seed_first ";
        if (nextCompetitionSeedIds.isEmpty())
            seedStepType += " seed_last ";
        stringBuilder.append("<div localId='seed_").append(localId).append("' class='seed ").append(cssClass).append(competitionFormatCss).append(seedStepType).append(" groups_").append(competitionGroupTrees.size()).append(" '>");
        stringBuilder.append("<div class='label'>");
        if (nextCompetitionSeedIds.isEmpty())
            stringBuilder.append("Final Phase");
        else if (competitionGroupFormat != null) {
            if (participantFilteringMethod.compareTo(ParticipantFilteringMethod.ALL) == 0) {
                stringBuilder.append("All players' Phase (").append(competitionGroupFormat.toString().toLowerCase()).append(")");
            } else {
                stringBuilder.append("Qualified Phase (").append(competitionGroupFormat.toString().toLowerCase()).append(")");
            }
        }
        stringBuilder.append("</div>");


        stringBuilder.append("<div class='seedStartDate").append(cssClass).append("'>");
        if (this.expectedRelativeStartTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='seedDuration").append(cssClass).append("'>");
        if (this.expectedDuration != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='seedEndDate").append(cssClass).append("'>");
        if (this.expectedRelativeEndTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeEndTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");

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
        stringBuilder.append("<div class='groups group_").append(competitionGroupTrees.size()).append(" '>");
        for (CompetitionGroupTree competitionGroupTree : competitionGroupTrees) {
            stringBuilder.append(competitionGroupTree.toHtmlCartouche(this));
        }
        stringBuilder.append("</div>");
        stringBuilder.append("</div>");

        stringBuilder.append(System.lineSeparator());
        for (CompetitionSeedTree competitionSeedTree : competitionSeedTrees) {
            stringBuilder.append(competitionSeedTree.toHtmlCartouche(competitionPhaseTree));
        }
        return stringBuilder.toString();

    }

}
