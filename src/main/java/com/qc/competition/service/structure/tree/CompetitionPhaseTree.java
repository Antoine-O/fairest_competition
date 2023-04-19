package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.CompetitionGroupFormat;
import com.qc.competition.service.structure.ParticipantFilteringMethod;
import com.qc.competition.service.structure.ParticipantPairingMethod;
import com.qc.competition.service.structure.Unit;
import com.qc.competition.service.template.automatic.participation.optimization.PhaseType;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlType(propOrder = {"competitionSeedTrees", "competitionPhaseTrees"})
public class CompetitionPhaseTree extends CompetitionObjectTree {

    @XmlAttribute(name = "nextCompetitionPhaseIds")
    public List<String> nextCompetitionPhaseIds = new ArrayList<>();

    @XmlAttribute(name = "previousCompetitionPhaseIds")
    public List<String> previousCompetitionPhaseIds = new ArrayList<>();

    @XmlAttribute(name = "participantFilteringMethod")
    @JsonProperty("participantFilteringMethod")
    public ParticipantFilteringMethod participantFilteringMethod;

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
    public PhaseType phaseType;


    public String toHtmlCartouche() {
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
        String phasePhaseType = "";
        if (previousCompetitionPhaseIds.isEmpty())
            phasePhaseType += " phase_first ";
        if (nextCompetitionPhaseIds.isEmpty())
            phasePhaseType += " phase_last ";
        stringBuilder.append("<div localId='phase_").append(localId).append("' class='phase ").append(cssClass).append(competitionFormatCss).append(phasePhaseType).append(" seeds_").append(competitionSeedTrees.size()).append(" '>");
        stringBuilder.append("<div class='label'>");
        stringBuilder.append("" + this.phaseType + " Phase");
        stringBuilder.append("</div>");


        stringBuilder.append("<div class='phaseStartDate").append(cssClass).append("'>");
        if (this.expectedRelativeStartTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='phaseDuration").append(cssClass).append("'>");
        if (this.expectedDuration != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='phaseEndDate").append(cssClass).append("'>");
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
        stringBuilder.append("<div class='seeds seed_").append(competitionSeedTrees.size()).append(" '>");
        for (CompetitionSeedTree competitionSeedTree : competitionSeedTrees) {
            stringBuilder.append(competitionSeedTree.toHtmlCartouche(this));
        }
        stringBuilder.append("</div>");
        stringBuilder.append("</div>");

        stringBuilder.append(System.lineSeparator());
        for (CompetitionPhaseTree competitionPhaseTree : competitionPhaseTrees) {
            stringBuilder.append(competitionPhaseTree.toHtmlCartouche());
        }
        return stringBuilder.toString();

    }

}
