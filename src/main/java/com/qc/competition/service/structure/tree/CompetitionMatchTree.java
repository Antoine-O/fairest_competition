package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionMatchTree extends CompetitionObjectTree {
    @XmlAttribute(name = "lane")
    public Integer lane;
    @XmlElementWrapper(name = "plays")
    @XmlElement(name = "play")
    @JsonProperty("plays")
    public List<CompetitionPlayTree> competitionPlayTrees = new ArrayList<>();

    @XmlAttribute(name = "bye")
    public Boolean bye;
    @XmlList
    @XmlAttribute(name = "previousMatchIds")
//    @XmlElementWrapper(name = "previousId")
//    @XmlElement(name = "localId")
    @JsonProperty("previousMatchIds")
    public List<String> previousCompetitionMatchIds = new ArrayList<>();
    @XmlList
    @XmlAttribute(name = "nextMatchIds")
//    @XmlElementWrapper(name = "previousId")
//    @XmlElement(name = "localId")
    @JsonProperty("nextMatchIds")
    public List<String> nextCompetitionMatchIds = new ArrayList<>();

    public String toHtmlCartouche() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        String cssClass = "";
        if (this.bye) {
            cssClass += " bye ";
        } else if (this.filled) {
            cssClass += " filled ";
            if (this.over)
                cssClass += " over ";
            else
                cssClass += " in_progress ";
        } else {
            cssClass += " empty ";
        }


        stringBuilder.append("<div class='matchDetails").append(cssClass).append("'>");

        stringBuilder.append("<div class='matchlane").append(cssClass).append("'");
        if (this.over) {
            stringBuilder.append(" style=' font-style: oblique; '");
        }
        stringBuilder.append(">");
        stringBuilder.append("Match ");
        stringBuilder.append(this.id);
        if (previousCompetitionMatchIds != null && !previousCompetitionMatchIds.isEmpty()) {
            stringBuilder.append("[");
            for (String previousCompetitionMatchId : previousCompetitionMatchIds) {
                stringBuilder.append(previousCompetitionMatchId);
                stringBuilder.append(" ");
            }
            stringBuilder.append("]->");
        }

        stringBuilder.append("[" + this.localId + "]");
        if (nextCompetitionMatchIds != null && !nextCompetitionMatchIds.isEmpty()) {
            stringBuilder.append("->[");
            for (String nextCompetitionMatchId : nextCompetitionMatchIds) {
                stringBuilder.append(nextCompetitionMatchId);
                stringBuilder.append(" ");
            }
            stringBuilder.append("]");
        }
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='matchStartDate").append(cssClass).append("'>");
        if (this.expectedRelativeStartTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='matchDuration").append(cssClass).append("'>");
        if (this.expectedDuration != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='matchEndDate").append(cssClass).append("'>");
        if (this.expectedRelativeEndTime != null)
            stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeEndTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");

        String participantMatchStatus = "";
        String participantMatchStatusContent = "";
        String participantMatchStatusQualificationStatus = "";
        stringBuilder.append("<div class='participants participants_").append(participantResultTrees.size()).append("'>");
        for (ParticipantResultTree participantResultMatchTree : participantResultTrees) {
            if (participantResultMatchTree.qualified != null && participantResultMatchTree.qualified) {
                participantMatchStatusQualificationStatus = "qualified";
            }
            if (participantResultMatchTree.rank != null && participantResultMatchTree.rank == 1) {
                participantMatchStatusContent = participantResultMatchTree.label;
                participantMatchStatus = "win";
            } else if (participantResultMatchTree.qualified != null && !participantResultMatchTree.qualified && participantResultMatchTree.rank != null && participantResultMatchTree.rank > 1) {
                participantMatchStatusContent = participantResultMatchTree.label;
                participantMatchStatus = "loss";
            } else {
                participantMatchStatusContent = participantResultMatchTree.label;
                if (participantResultMatchTree.label.isEmpty()) {
                    participantMatchStatus = "noplay";
                }
            }


            stringBuilder.append("<div class='participant ").append(participantMatchStatus).append(" ").append(participantMatchStatusQualificationStatus).append("'>");

            stringBuilder.append("<div class='participant_name'>");
            if (participantResultMatchTree.rank != null && participantResultMatchTree.rank > 0) {
                stringBuilder.append(participantResultMatchTree.rank).append(" - ");
            }
            stringBuilder.append(participantMatchStatusContent);
            stringBuilder.append("</div>");
            if (participantResultMatchTree.badPoints != null) {
                stringBuilder.append("<div class='participant_points'>");
                stringBuilder.append(participantResultMatchTree.badPoints).append("mpt(s)");
                stringBuilder.append("</div>");
            }
            if (participantResultMatchTree.points != null) {
                stringBuilder.append("<div class='participant_points'>");
                stringBuilder.append(participantResultMatchTree.points).append("pt(s)");
                stringBuilder.append("</div>");
            }
//            if (participantResultMatchTree.participantTree != null && participantResultMatchTree.participantTree.participantTrees.size() > 1) {
//                stringBuilder.append(participantResultMatchTree.participantTree.toHtmlCartouche(false));
//                for (ParticipantTree participantTree : participantResultMatchTree.participantTree.participantTrees) {
//                    stringBuilder.append(participantTree.label);
//
//                }
//            }
            if (!this.bye) {
                stringBuilder.append("<div class='plays plays_").append(competitionPlayTrees.size()).append("'>");

                String participantPlayStatus = "";
                String participantPlayContent = "";
                String tooltip = "";
                for (CompetitionPlayTree competitionPlayTree : competitionPlayTrees) {
                    for (ParticipantResultTree participantResultPlayTree : competitionPlayTree.participantResultTrees) {
                        participantPlayStatus = "noplay";
                        participantPlayContent = "&cir;";
                        tooltip = "";
                        if (participantResultPlayTree.localId != null && participantResultMatchTree.localId != null)
                            if (participantResultPlayTree.localId.compareTo(participantResultMatchTree.localId) == 0 || (participantResultPlayTree.teamLocalId != null && participantResultPlayTree.teamLocalId.compareTo(participantResultMatchTree.localId) == 0)) {

                                if (participantResultPlayTree.rank != null && participantResultPlayTree.rank == 1) {
                                    participantPlayStatus = "win";
                                    if (participantResultPlayTree.goal != null) {
                                        participantPlayContent = "" + participantResultPlayTree.goal;
                                    } else if (participantResultPlayTree.badPoints != null) {
                                        participantPlayContent = " " + participantResultPlayTree.badPoints + "pt(s)";
                                    } else if (participantResultPlayTree.points != null) {
                                        participantPlayContent = " " + participantResultPlayTree.points + "pt(s)";
                                    } else {
                                        if (competitionPlayTree.participantResultTrees.size() <= 2)
                                            participantPlayContent = "&check;";
                                        else
                                            participantPlayContent = "" + participantResultPlayTree.rank;
                                    }
                                } else if (participantResultPlayTree.rank != null && participantResultPlayTree.rank > 1) {
                                    participantPlayStatus = "loss";
                                    if (participantResultPlayTree.goal != null) {
                                        participantPlayContent = "" + participantResultPlayTree.goal;
                                    } else if (participantResultPlayTree.badPoints != null) {
                                        participantPlayContent = " " + participantResultPlayTree.badPoints + "mpt(s)";
                                    } else if (participantResultPlayTree.points != null) {
                                        participantPlayContent = " " + participantResultPlayTree.points + "pt(s)";
                                    } else {
                                        if (competitionPlayTree.participantResultTrees.size() <= 2)
                                            participantPlayContent = "&cross;";
                                        else
                                            participantPlayContent = "" + participantResultPlayTree.rank;
                                    }

                                } else {
                                    participantPlayStatus = "noplay";
                                    participantPlayContent = "&cir;";
                                }

                                if (participantResultPlayTree.teamLocalId != null && participantResultPlayTree.teamLocalId.compareTo(participantResultMatchTree.localId) == 0) {
                                    tooltip = participantResultPlayTree.participantTree.label.replace("\"", "'");
                                }
                                if (participantResultPlayTree.points != null)
                                    tooltip += " " + participantResultPlayTree.points + "pt(s)";
                                if (participantResultPlayTree.badPoints != null)
                                    tooltip += " " + participantResultPlayTree.badPoints + "mpt(s)";
                                if (participantResultPlayTree.goal != null)
                                    tooltip += " " + participantResultPlayTree.goal + " goal(s)";

                                break;
                            }
                    }
                    stringBuilder.append("<div class='playDetails ").append(participantPlayStatus).append("'>");
                    stringBuilder.append("<a href=\"#\"");
                    if (!tooltip.isEmpty())
                        stringBuilder.append(" data-tooltip=\"").append(tooltip).append("\"");
                    stringBuilder.append(">").append(participantPlayContent).append("</a></div>");

                }

                stringBuilder.append("</div>");
            }

            stringBuilder.append("</div>");
        }
        stringBuilder.append("</div>");
        stringBuilder.append("</div>");
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }

}
