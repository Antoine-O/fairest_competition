package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.XmlManagedObject;
import com.qc.competition.service.structure.adaptater.DurationAdapter;
import com.qc.competition.utils.URLReader;
import com.qc.competition.utils.json.JSONObject;
import com.qc.competition.ws.simplestructure.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionInstanceTree extends CompetitionObjectTree implements XmlManagedObject, JSONObject {
//
//    @XmlElementWrapper(name = "seeds")
//    @XmlElement(name = "seed")
//    @JsonProperty("seeds")
//    public List<CompetitionSeedTree> competitionSeedTrees = new ArrayList<>();
//
@XmlElementWrapper(name = "phases")
@XmlElement(name = "phase")
@JsonProperty("phases")
public List<CompetitionPhaseTree> competitionPhaseTrees = new ArrayList<>();


    @XmlElementWrapper(name = "registrations")
    @XmlElement(name = "registration")
    @JsonProperty("registrations")
    public List<ParticipantResultTree> participantRegistrationTrees = new ArrayList<>();


    @XmlAttribute(name = "requestedDuration")
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration requestedDuration;


    @Override
    public void initFromXmlInput() {

    }

    @Override
    public void initForXmlOutput() {

    }

    public String toHtmlCartouche(boolean standalone, String cssUrl) {
        return toHtmlCartouche(standalone, cssUrl, null);
    }

    public String toHtmlCartouche(boolean standalone, String cssUrl, String cssContent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<!DOCTYPE html>");
        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        // stringBuilder.append("<meta http-equiv='refresh' content='60' />");
        stringBuilder.append("<meta charset='UTF-8'>");
        if (cssContent == null)
            cssContent = "";
        if (standalone) {
            //stringBuilder.append("<link rel='stylesheet' href='").append(cssUrl).append("'>");

            if (cssUrl != null && !cssUrl.trim().isEmpty()) {
                try {
                    URLReader urlReader = new URLReader();
                    String urlContent = urlReader.readToString(cssUrl);
                    if (!urlContent.isEmpty()) {
                        cssContent = cssContent + System.lineSeparator() + urlContent;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            URLReader urlReader = new URLReader();
            cssContent = urlReader.readToString(cssUrl);
            if (!cssContent.isEmpty()) {
                cssContent = "<style type=\"text/css\" href=\"" + cssUrl + "\" />";
            }
        }
        if (cssContent != null && !cssContent.trim().isEmpty()) {
            cssContent = System.lineSeparator() + "<style type = \"text/css\">" + System.lineSeparator() + cssContent + System.lineSeparator() + "</style>";
        }
        stringBuilder.append(cssContent);
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");
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


        stringBuilder.append("<div class='competition").append(cssClass).append("'>");

        stringBuilder.append("<div class='competition_label'>");
        if (this.internationalizedLabel != null)
            stringBuilder.append(this.internationalizedLabel.defaultLabel);
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='competition_requested_duration'>");
        if (this.requestedDuration != null)
            stringBuilder.append(this.requestedDuration);
        stringBuilder.append("</div>");


        stringBuilder.append("<div class='competitionStartDate").append(cssClass).append("'>");
        stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeStartTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='competitionDuration").append(cssClass).append("'>");
        stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedDuration.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");
        stringBuilder.append("<div class='competitionEndDate").append(cssClass).append("'>");
        stringBuilder.append(DurationFormatUtils.formatDuration(this.expectedRelativeEndTime.duration.toMillis(), "HH:mm", true));
        stringBuilder.append("</div>");

        if (this.over) {
            stringBuilder.append("<div class='results'>");


            stringBuilder.append("<div class='participant'>");
            stringBuilder.append("<div class='participant_name title'>Name</div>");
            stringBuilder.append("<div class='participant_rank title'>Rank</div>");
            stringBuilder.append("<div class='participant_points  title'>Points</div>");
            stringBuilder.append("<div class='participant_percent  title'>%</div>");
            stringBuilder.append("<div class='participant_prize  title'>Prize</div>");
            stringBuilder.append("</div>");

            for (ParticipantResultTree participantResultTree : participantResultTrees) {
                stringBuilder.append(participantResultTree.toHtmlCartouche());
            }
            stringBuilder.append("</div>");
        }


        stringBuilder.append("<div class='phase phases_").append(competitionPhaseTrees.size()).append(" '>");
        for (CompetitionPhaseTree competitionPhaseTree : competitionPhaseTrees) {
            stringBuilder.append(competitionPhaseTree.toHtmlCartouche());
        }

        stringBuilder.append("</div>");
        stringBuilder.append("</div>");
        stringBuilder.append(System.lineSeparator());


        if (this.over) {
            stringBuilder.append("<div class='results'>");


            stringBuilder.append("<div class='participant'>");
            stringBuilder.append("<div class='participant_name title'>Name</div>");
            stringBuilder.append("<div class='participant_rank title'>Rank</div>");
            stringBuilder.append("<div class='participant_points  title'>Points</div>");
            stringBuilder.append("<div class='participant_percent  title'>%</div>");
            stringBuilder.append("<div class='participant_prize  title'>Prize</div>");
            stringBuilder.append("</div>");

            for (ParticipantResultTree participantResultTree : participantRegistrationTrees) {
                stringBuilder.append(participantResultTree.toHtmlCartouche());
            }
            stringBuilder.append("</div>");
        }


        stringBuilder.append("<footer>");
        stringBuilder.append("<!--");
        stringBuilder.append(this.expectedDuration.toString());
        stringBuilder.append("-->");
        stringBuilder.append("</footer>");

        stringBuilder.append("</body>");
        if (standalone) {
//            stringBuilder.append("<script type='text/javascript' src='script.js'></script>");

            stringBuilder.append("</html>");
        }
        return stringBuilder.toString();

    }
}
