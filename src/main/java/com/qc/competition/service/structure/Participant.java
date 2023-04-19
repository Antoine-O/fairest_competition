package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.service.structure.tree.ParticipantTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ParticipantSingle.class, ParticipantTeam.class, ParticipantTeamVoid.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ParticipantSingle.class, name = "single"),
        @JsonSubTypes.Type(value = ParticipantTeam.class, name = "team"),
        @JsonSubTypes.Type(value = ParticipantTeamVoid.class, name = "void")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
abstract public class Participant implements Comparable<Participant>, Cloneable, Serializable {
    public static String CLASS = Participant.class.getSimpleName();
    //    public static Logger LOGGER_clone = Logger.getLogger(CLASS + ".clone()");
    public String type;

//    @XmlTransient
//    @JsonIgnore
//    public SortedSet<CompetitionPlay> competitionPlays = new TreeSet<CompetitionPlay>();

    //    @XmlElementWrapper(name = "playIds")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "playIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("playIds")
//    @JsonIgnore
    public SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();

    @XmlAttribute(name = "equalityComparison")
    public Double equalityComparison = null;
    @XmlAttribute(name = "localId")

    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;
    @XmlAttribute(name = "bib")
    @JsonProperty("bib")
    public String bibNumber = null;
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId = null;
    @XmlAttribute(name = "active")
    @JsonProperty("active")
//    @JsonDeserialize(using = JsonBooleanDeserialiser.class)
//    @JsonSerialize(using = JsonBooleanSerializer.class)
    public Boolean active;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();

//    @XmlList
//    @XmlAttribute(name = "participantSeatIds")
//    @JsonProperty("participantSeatIds")
//      @XmlIDREF  @JsonIdentityReference(alwaysAsId=true)
//    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();

    //    @XmlElementWrapper(name = "seatIds")
//    @XmlElement(name = "localId")
//    @JsonIgnore
//    public SortedSet<Integer> participantSeatIds = new TreeSet<Integer>();
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;

    public Participant() {
        super();
    }


//    public Participant(Integer localId, String name, Double equalityComparison) {
//        this();
//        if (localId != null)
//            this.localId = localId;
//        this.internationalizedLabel.defaultLabel = name;
//        if (equalityComparison != null)
//            this.equalityComparison = equalityComparison;
//        if (localId > 128) {
//            int i = 0;
//        }
//    }

//    public Participant(Integer localId, InternationalizedLabel internationalizedLabel, Double equalityComparison) {
//        this();
//        if (localId != null)
//            this.localId = localId;
//        this.internationalizedLabel = internationalizedLabel;
//        if (equalityComparison != null)
//            this.equalityComparison = equalityComparison;
//    }

    protected Participant(IdGenerator idGenerator) {
        this.localId = idGenerator.getId(this.getClass().getSimpleName());
        this.id = idGenerator.getId();
        this.internationalizedLabel.defaultLabel = this.getClass().getSimpleName() + " " + this.localId;
        this.equalityComparison = Math.random();
        this.active = Boolean.TRUE;

    }

//    public Participant clone() {
//        Participant participant = null;
//        try {
//            participant = this.getClass().newInstance();
//            participant.bibNumber = this.bibNumber;
//            participant.id = this.id;
//            participant.localId = this.localId;
//            participant.internationalizedLabel = (InternationalizedLabel) internationalizedLabel.clone();
//            participant.equalityComparison = this.equalityComparison;
//        } catch (Exception e) {
////            LOGGER_clone.log(Level.SEVERE, this.toString(), e);
//            e.printStackTrace();
//        }
//        return participant;
//    }

    public Participant cloneForContext() {
        Participant participant = null;
        try {
            participant = this.getClass().newInstance();
            participant.bibNumber = this.bibNumber;
            participant.id = this.id;
            participant.localId = this.localId;
            participant.databaseId = this.databaseId;
            participant.competitionPlays = null;
            participant.internationalizedLabel = (InternationalizedLabel) internationalizedLabel.clone();
//            participant.equalityComparison = this.equalityComparison;
        } catch (Exception e) {
//            LOGGER_clone.log(Level.SEVERE, this.toString(), e);
            e.printStackTrace();
        }
        return participant;
    }

    @Override
    public int compareTo(Participant participant) {
        int compareResult = 0;
        if (participant == null || participant.id == null)
            return 1;
        if (this.getClass().getName().compareTo(participant.getClass().getName()) == 0) {

            if (this.id != null && participant.id != null && this.id.compareTo(participant.id) != 0) {
                compareResult = this.id.compareTo(participant.id);

            } else if (this.localId != null && participant.localId != null && this.localId.compareTo(participant.localId) == 0) {
                compareResult = 0;
            } else {
                if (this.localId == null)
                    compareResult = -1;
                else if (participant.localId == null)
                    compareResult = 1;

                if (compareResult == 0) {
                    compareResult = this.localId.compareTo(participant.localId);
//                    if (compareResult == 0) {
//                        if (this.bibNumber != null && participant.bibNumber != null) {
//                            compareResult = this.bibNumber.compareTo(participant.bibNumber);
//                            if (compareResult == 0)
//                                return compareResult;
//                        }
//                        if (compareResult == 0)
//                            if (participant.internationalizedLabel != null && this.internationalizedLabel == null)
//                                compareResult = -1;
//                        if (compareResult == 0)
//                            if (participant.internationalizedLabel == null && this.internationalizedLabel != null)
//                                compareResult = 1;
//                        if (compareResult == 0)
//                            if (participant.internationalizedLabel != null && this.internationalizedLabel != null)
//                                compareResult = this.internationalizedLabel.compareTo(participant.internationalizedLabel);
//        if (compareResult == 0)
//            if (participant.equalityComparison != null && this.equalityComparison != null)
//                compareResult = this.equalityComparison.compareTo(participant.equalityComparison );
//                    }
                }
            }
        } else {
            compareResult = this.getClass().getName().compareTo(participant.getClass().getName());
        }
        return compareResult;

    }

//    public SortedSet<CompetitionPlay> getCompetitionPlays() {
//        return competitionPlays;
//    }

    abstract public boolean isVoid();

    public String toDescription() {
        return toString() + System.lineSeparator();
    }

    public Element toDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        if (internationalizedLabel != null)
            if (internationalizedLabel.defaultLabel != null)
                element.setAttribute("name", internationalizedLabel.defaultLabel);
        return element;
    }

    abstract public String toSimpleDescription();

    public Element toSimpleDescriptionXml(Document document) {
        return this.toDescriptionXml(document);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "localId=" + localId +
                (internationalizedLabel != null ? ", name='" + internationalizedLabel.defaultLabel + '\'' : "") +
                '}';
    }

    public void initForXmlOutput(boolean initialOutput) {
//        for (CompetitionPlay competitionPlay : competitionPlays)
//            competitionPlays.add(competitionPlay.localId);
//        for (ParticipantSeat participantSeat : participantSeats)
//            participantSeatIds.add(participantSeat.localId);
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        for (Integer competitionPlayId : competitionPlays)
//            competitionPlays.add(competitionInstance.getCompetitionPlay(competitionPlayId));
//        for (Integer participantSeatId : participantSeatIds)
//            participantSeats.add(competitionInstance.getParticipantSeat(participantSeatId));
    }

//    public Participant getParticipant(String localId) {
//        if (this.localId.compareTo(localId) == 0)
//            return this;
//        return null;
//    }

    public ParticipantTree toParticipantTree() {
        ParticipantTree participantTree = new ParticipantTree();
        participantTree.databaseId = databaseId;
        participantTree.localId = localId;
        if (this instanceof ParticipantSingle || (this instanceof ParticipantTeam && ((ParticipantTeam) this).participantTeamMembers.size() == 1)) {
            participantTree.label = "(" + this.bibNumber + ") " + this.internationalizedLabel.defaultLabel;
        } else if (this instanceof ParticipantTeam && ((ParticipantTeam) this).participantTeamMembers.size() > 1) {
            participantTree.label = "(" + this.bibNumber + ") " + this.internationalizedLabel.defaultLabel;
            for (ParticipantTeamMember participantTeamMember : ((ParticipantTeam) this).participantTeamMembers) {
                participantTree.participantTrees.add(participantTeamMember.participant.toParticipantTree());
            }
        } else if (this instanceof ParticipantTeam && ((ParticipantTeam) this).participantTeamMembers.size() > 0) {
            Participant onlyParticipant = ((ParticipantTeam) this).participantTeamMembers.first().participant;
            participantTree.label = "(" + onlyParticipant.bibNumber + ") " + onlyParticipant.internationalizedLabel.defaultLabel;

        }
        return participantTree;
    }

    public Participant getParticipant(String localId) {
        Participant participant = null;
        if (this.localId != null && localId != null && this.localId.compareTo(localId) == 0)
            participant = this;
        return participant;
    }

    public abstract SortedSet<Participant> toParticipantSet();

    public void clearDatabaseId() {
        this.databaseId = null;
    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    public List<Participant> getAllRealParticipantsAsArray() {
        List<Participant> participants = new ArrayList<>();
        if (!this.isVoid())
            participants.add(this);
        return participants;
    }

    public abstract void fillCache(boolean up, boolean down);

    public abstract void resetCache();

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
    }

    public abstract List<Participant> getParticipantsAsArrayForContext(List<Participant> participantArrayList);

    public void activate() {
        this.active = true;
    }

    public void unactivate() {
        this.active = false;
    }
}
