package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 09/01/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ParticipantTeamMember implements Comparable<ParticipantTeamMember>, Serializable {
    public static String CLASS = ParticipantTeamMember.class.getSimpleName();
//    private static NumberFormat numberFormat;
//
//    static {
//        numberFormat = NumberFormat.getInstance();
//        numberFormat.setMinimumIntegerDigits(4);
//        numberFormat.setGroupingUsed(false);
//    }

    @XmlAttribute(name = "localId")

    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    //    @XmlAttribute(name = "participantId")
//    public String participantId;
//    @XmlAttribute(name = "teamId")
//    public String participantTeamId;
    @XmlAttribute(name = "manager")
    public boolean manager = false;
    @XmlAttribute(name = "name")
    public String name = "";
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "participantId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("participantId")
    protected Participant participant;
    @XmlAttribute(name = "participantTeamId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("participantTeamId")
    protected ParticipantTeam participantTeam;

    public ParticipantTeamMember() {
        super();
    }

    protected ParticipantTeamMember(IdGenerator idGenerator) {
        this();
        this.localId = idGenerator.getId(this.getClass().getSimpleName());
        this.id = idGenerator.getId();
        this.name = "ParticipantTeamMember " + this.localId;
    }

    public static ParticipantTeamMember createInstance(IdGenerator idGenerator) {
        return new ParticipantTeamMember(idGenerator);
    }

    public ParticipantTeamMember clone() {
        return this.clone();
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    @Override
    public int compareTo(ParticipantTeamMember participantTeamMember) {
        int compareResult = 0;
        if (participantTeamMember == null)
            return 1;
        compareResult = this.id.compareTo(participantTeamMember.id);
        if (compareResult == 0)
            return compareResult;
//        if (compareResult == 0)
//            if (participantTeamMember.participant.internationalizedLabel != null && this.participant.internationalizedLabel == null)
//                compareResult = -1;
//        if (compareResult == 0)
//            if (participantTeamMember.participant.internationalizedLabel == null && this.participant.internationalizedLabel != null)
//                compareResult = 1;
//        if (compareResult == 0)
//            if (participantTeamMember.participant.internationalizedLabel != null && this.participant.internationalizedLabel != null)
//                compareResult = this.participant.internationalizedLabel.compareTo(participantTeamMember.participant.internationalizedLabel);
//        if (compareResult == 0)
//            if (participantTeamMember.localId != null && this.localId != null)
//                compareResult = this.localId.compareTo(participantTeamMember.localId);
        return compareResult;
    }

    public String toDescription() {
        StringBuilder description = new StringBuilder();
        description.append(toString());
        if (participant != null) {
            description.append(participant.toString()).append(System.lineSeparator());
        }
        return description.toString();
    }

    public Element toDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        element.setAttribute("participantId", "" + (participant != null ? participant.localId : null));
//        element.setAttribute("name", name);
//        element.setAttribute("name", "" + manager);
        if (participant != null) {
            element.appendChild(getParticipant().toDescriptionXml(document));
        }
        return element;
    }

    public String toSimpleDescription() {
        StringBuilder description = new StringBuilder();
        if (participant != null) {
            description.append(getParticipant().toSimpleDescription());
        }
        return description.toString();
    }

    @Override
    public String toString() {
        return "ParticipantTeamMember{" +
                "localId=" + localId +
                ", participantId=" + (participant != null ? participant.localId : null) +
//                ", name='" + name + '\'' +
                '}';
    }

    public void initForXmlOutput(boolean initialOutput) {
//        if (this.participantTeamId == null)
//            this.participantTeamId = participantTeam.localId;
//        if (!initialOutput)
//            this.participantId = this.participant.localId;
//        else
//            this.participant.initForXmlOutput(initialOutput);
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
//        if (this.participantId != null)
//            this.participant = competitionInstance.getParticipant(this.participantId);
    }

    public Participant getParticipant(String localId) {
        Participant participant = null;
        if (this.participant != null)
            participant = this.getParticipant().getParticipant(localId);
        return participant;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public ParticipantTeam getParticipantTeam() {
        return participantTeam;
    }

    public void setParticipantTeam(ParticipantTeam participantTeam) {
        this.participantTeam = participantTeam;
//        this.participantTeamCache = null;
//        this.participantTeamCache = null;
    }

    public List<Participant> getAllRealParticipantsAsArray() {
        List<Participant> participantArrayList = new ArrayList<>();
        if (participant != null)
            participantArrayList.addAll(getParticipant().getAllRealParticipantsAsArray());

        return participantArrayList;
    }

//    public void fillCache(boolean up, boolean down) {
//        resetCache();
//        if (down && participant != null) {
//            if (getParticipant().competitionInstance == null)
//                getParticipant().competitionInstance = competitionInstance;
//            getParticipant().fillCache(false, false);
//        }
//        if (up && participantTeam != null) {
//            if (getParticipantTeam().competitionInstance == null)
//                getParticipantTeam().competitionInstance = competitionInstance;
//            getParticipantTeam().fillCache(false, false);
//        }
//
//    }

//    public void resetCache() {
//        participantCache = null;
//        participantCache = null;
//        participantTeamCache = null;
//        participantTeamCache = null;
//    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
    }

    public ParticipantTeamMember cloneForContext() {
        ParticipantTeamMember participantTeamMember = new ParticipantTeamMember();
        participantTeamMember.id = this.id;
        participantTeamMember.localId = this.localId;
        participantTeamMember.participantTeam = new ParticipantTeam();
        participantTeamMember.participantTeam.id = participantTeam.id;
        participantTeamMember.participant = this.participant.cloneForContext();
        return participantTeamMember;
    }
}
