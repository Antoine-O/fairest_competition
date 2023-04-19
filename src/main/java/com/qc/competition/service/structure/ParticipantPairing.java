package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.utils.Sets;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class ParticipantPairing implements Comparable<ParticipantPairing>, Serializable, Cloneable {
    public static String CLASS = ParticipantPairing.class.getSimpleName();
//    @XmlTransient
//    @JsonIgnore
//    public CompetitionSeed competitionSeedCache;
//    @XmlTransient
//    @JsonIgnore
//    public CompetitionGroup competitionGroupCache;
//    @XmlTransient
//    @JsonIgnore
//    public CompetitionRound competitionRoundCache;
//    @XmlTransient
//    @JsonIgnore
//    public CompetitionMatch competitionMatchCache;
//    @XmlTransient
//    @JsonIgnore
//    public CompetitionPlay competitionPlayCache;


    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "seedId")
    @JsonProperty("seedId")
    public CompetitionSeed competitionSeed;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "phaseId")
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "groupId")
    @JsonProperty("groupId")
    public CompetitionGroup competitionGroup;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "roundId")
    @JsonProperty("roundId")
    public CompetitionRound competitionRound;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "matchId")
    @JsonProperty("matchId")
    public CompetitionMatch competitionMatch;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "playId")
    @JsonProperty("playId")
    public CompetitionPlay competitionPlay;

    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    @XmlAttribute(name = "databaseId")
    public Integer databaseId;
    @XmlElementWrapper(name = "seats")
    @XmlElement(name = "seat")
    @JsonProperty("seats")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;

    public ParticipantPairing() {
        super();
    }


    private ParticipantPairing(int size, CompetitionInstance competitionInstance) {
        super();
        this.localId = competitionInstance.getIdGenerator().getId(this.getClass().getSimpleName());
        this.id = competitionInstance.getIdGenerator().getId();
        this.competitionInstance = competitionInstance;
        if (participantSeats == null)
            participantSeats = new TreeSet<>();
        for (int i = 0; i < size; i++) {
            ParticipantSeat participantSeat = getCompetitionInstance().createParticipantSeat();
            participantSeat.index = i + 1;
            participantSeats.add(participantSeat);
        }
//        Sets.sort(participantSeats);
        competitionInstance.addParticipantPairing(this);
    }

    public static ParticipantPairing createInstance(int size, CompetitionInstance competitionInstance) {
        return new ParticipantPairing(size, competitionInstance);
    }

    @Override
    public int compareTo(ParticipantPairing o) {
        int compareValue = 0;
        compareValue = 0;
        if (compareValue == 0 && competitionPlay != null && o.competitionPlay != null)
            compareValue = competitionPlay.compareTo(o.competitionPlay);
        if (compareValue == 0 && competitionMatch != null && o.competitionMatch != null)
            compareValue = competitionMatch.compareTo(o.competitionMatch);
        if (compareValue == 0 && competitionRound != null && o.competitionRound != null)
            compareValue = competitionRound.compareTo(o.competitionRound);
        if (compareValue == 0 && competitionGroup != null && o.competitionGroup != null)
            compareValue = competitionGroup.compareTo(o.competitionGroup);
        if (compareValue == 0 && competitionSeed != null && o.competitionSeed != null)
            compareValue = competitionSeed.compareTo(o.competitionSeed);
        if (compareValue == 0 && competitionPhase != null && o.competitionPhase != null)
            compareValue = competitionPhase.compareTo(o.competitionPhase);
//            if (compareValue == 0) {
//                List<Participant> currentParticipants = getRealParticipantsAsArray();
//                List<Participant> otherParticipants = o.getRealParticipantsAsArray();
//                compareValue = currentParticipants.size() - otherParticipants.size();
//                if (compareValue == 0) {
//                    for (int i = 0; i < currentParticipants.size(); i++) {
//                        compareValue = currentParticipants.get(i).compareTo(otherParticipants.get(i));
//                        if (compareValue != 0)
//                            break;
//                    }
//                }
//            }
        if (compareValue == 0) {
            if (localId != null && o.localId != null) {
                compareValue = localId.compareTo(o.localId);
            } else if (localId != null) {
                compareValue = 1;
            } else {
                compareValue = -1;
            }
        }
        if (compareValue == 0) {
            if (id != null && o.id != null) {
                compareValue = id.compareTo(o.id);
            } else if (id != null) {
                compareValue = 1;
            } else {
                compareValue = -1;
            }
        }
        return compareValue;
    }

    public List<Participant> getParticipantsAsArray() {
        List<Participant> participantArrayList = new ArrayList<>();
//        Sets.sort(participantSeats);
        for (ParticipantSeat participantSeat : participantSeats) {
            participantArrayList.add(participantSeat.participant);
        }
//        Collections.sort(participantArrayList);
        return participantArrayList;
    }

    public List<Participant> getRealParticipantsAsArray() {
        List<Participant> participantArrayList = new ArrayList<>();
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null) {
                Participant participant = participantSeat.getParticipant();
                if (!participant.isVoid())
                    participantArrayList.add(participantSeat.participant);
            }
        }
        return participantArrayList;
    }


    public List<Participant> getParticipantsAsArrayForContext() {
        List<Participant> participantArrayList = new ArrayList<>();
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null) {
                participantArrayList.addAll(participantSeat.getParticipantsAsArrayForContext());
            }
        }
        return participantArrayList;
    }


    public List<Participant> getAllRealParticipantsAsArray() {
        List<Participant> participantArrayList = new ArrayList<>();
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null) {
                participantArrayList.addAll(participantSeat.getParticipant().getAllRealParticipantsAsArray());
            }
        }
        return participantArrayList;
    }

    public List<ParticipantSeat> getParticipantSeatsAsArray() {
        List<ParticipantSeat> participantSeatArrayList = new ArrayList<>();
        participantSeatArrayList.addAll(Sets.sort(participantSeats));
        return participantSeatArrayList;
    }

    public boolean isVoid() {
        boolean isVoid = true;
        if (!participantSeats.isEmpty()) {
            for (ParticipantSeat participantSeat : participantSeats) {
                if (participantSeat.participant != null && participantSeat.getParticipant() != null && !participantSeat.getParticipant().isVoid()) {
                    isVoid = false;
                    break;
                }
            }

        }
        return isVoid;
    }

    public String toDescription() {
        StringBuilder description = new StringBuilder();
        description.append(toString()).append(System.lineSeparator());
        if (competitionPhase != null)
            description.append("[competitionPhase]").append(System.lineSeparator()).append(competitionPhase.toString()).append(System.lineSeparator());
        if (competitionSeed != null)
            description.append("[competitionSeed]").append(System.lineSeparator()).append(competitionSeed.toString()).append(System.lineSeparator());
        if (competitionGroup != null)
            description.append("[competitionGroup]").append(System.lineSeparator()).append(competitionGroup.toString()).append(System.lineSeparator());
        if (competitionRound != null)
            description.append("[competitionRound]").append(System.lineSeparator()).append(competitionRound.toString()).append(System.lineSeparator());
        if (competitionMatch != null)
            description.append("[competitionMatch]").append(System.lineSeparator()).append(competitionMatch.toString()).append(System.lineSeparator());
        if (competitionPlay != null)
            description.append("[competitionPlay]").append(System.lineSeparator()).append(competitionPlay.toString()).append(System.lineSeparator());

        description.append("[participantSeats]").append(System.lineSeparator());
        for (ParticipantSeat participantSeat : participantSeats) {
            description.append(participantSeat.toDescription());
        }
        return description.toString();
    }

    private CompetitionPhase getCompetitionPhase() {

        return competitionPhase;
    }

    public void setCompetitionPhase(CompetitionPhase competitionPhase) {
        this.competitionPhase = competitionPhase;
        competitionPhase.addParticipantPairing(this);

    }

    private CompetitionSeed getCompetitionSeed() {

        return competitionSeed;
    }

    public void setCompetitionSeed(CompetitionSeed competitionSeed) {
        this.competitionSeed = competitionSeed;
        competitionSeed.addParticipantPairing(this);

    }

    private CompetitionGroup getCompetitionGroup() {

        return competitionGroup;
    }

    public void setCompetitionGroup(CompetitionGroup competitionGroup) {
        this.competitionGroup = competitionGroup;
        competitionGroup.addParticipantPairing(this);
    }

    private CompetitionRound getCompetitionRound() {

        return competitionRound;
    }

    public void setCompetitionRound(CompetitionRound competitionRound) {
        this.competitionRound = competitionRound;
        competitionRound.addParticipantPairing(this);
    }

    private CompetitionMatch getCompetitionMatch() {

        return competitionMatch;
    }

    public void setCompetitionMatch(CompetitionMatch competitionMatch) {
        this.competitionMatch = competitionMatch;
        competitionMatch.addParticipantPairing(this);
    }


    private CompetitionPlay getCompetitionPlay() {

        return competitionPlay;
    }

    public void setCompetitionPlay(CompetitionPlay competitionPlay) {
        this.competitionPlay = competitionPlay;
        competitionPlay.addParticipantPairing(this);
        for (Participant participant : this.getRealParticipantsAsArray()) {
            participant.competitionPlays.add(competitionPlay);
        }
    }

    private CompetitionGroup getCompetitionGroup(String id) {
        return this.getCompetitionInstance().getCompetitionGroup(id);
    }

    private CompetitionRound getCompetitionRound(String id) {
        return this.getCompetitionInstance().getCompetitionRound(id);
    }

    private CompetitionSeed getCompetitionSeed(String id) {
        return this.getCompetitionInstance().getCompetitionSeed(id);
    }

    private CompetitionMatch getCompetitionMatch(String id) {
        return this.getCompetitionInstance().getCompetitionMatch(id);
    }

    private CompetitionPlay getCompetitionPlay(String id) {
        return this.getCompetitionInstance().getCompetitionPlay(id);
    }

    public String toDescriptionTree(int level) {
        String indentation = StringUtil.getIndentationForLevel(level);
        StringBuilder description = new StringBuilder();
        description.append(indentation).append(toString()).append(System.lineSeparator());
        if (!participantSeats.isEmpty()) {
            description.append(indentation).append("[participantSeats]").append(System.lineSeparator());
            for (ParticipantSeat participantSeat : participantSeats) {
                description.append(participantSeat.toDescriptionTree(level + 1));
            }
        }
        return description.toString();
    }

    public Element toDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);


        Element elementParticipantSeats = document.createElement(ParticipantSeat.class.getSimpleName() + "s");
        if (!participantSeats.isEmpty()) {
            for (ParticipantSeat participantSeat : participantSeats) {
                elementParticipantSeats.appendChild(participantSeat.toDescriptionXml(document));
            }
        }
        element.appendChild(elementParticipantSeats);


        return element;
    }

    public Element toSimpleDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);


        Element elementParticipants = document.createElement(Participant.class.getSimpleName() + "s");
        if (!participantSeats.isEmpty()) {
            for (ParticipantSeat participantSeat : participantSeats) {

                elementParticipants.appendChild(participantSeat.getParticipant().toSimpleDescriptionXml(document));
            }
        }
        element.appendChild(elementParticipants);


        return element;
    }

    @Override
    public String toString() {
        return "ParticipantPairing{" +
                "localId=" + localId +
                ", participantSeats.size()=" + participantSeats.size() + getParticipantSeatsAsArray() +
                '}';
    }

    public void initForXmlOutput() {
        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.initForXmlOutput(true);
        }
//        if (competitionSeed != null)
//            competitionSeed = competitionSeed.localId;
//        if (competitionGroup != null)
//            competitionGroup = competitionGroup.localId;
//        if (competitionMatch != null)
//            competitionMatchId = competitionMatch.localId;
//        if (competitionRound != null)
//            competitionRoundId = competitionRound.localId;
//        if (competitionPlay != null)
//            competitionPlayId = competitionPlay.localId;
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.initFromXmlInput(this.getCompetitionInstance());
        }
        this.resetCache();
//        if (competitionSeed != null) {
//            this.competitionSeed = competitionInstance.getCompetitionSeed(competitionSeed);
//        }
//        if (competitionGroup != null) {
//            this.competitionGroup = competitionInstance.getCompetitionGroup(competitionGroup);
//        }
//        if (competitionRoundId != null) {
//            this.competitionRound = competitionInstance.getCompetitionRoundForRound(competitionRoundId);
//        }
//        if (competitionMatchId != null) {
//            this.competitionMatch = competitionInstance.getCompetitionMatch(competitionMatchId);
//        }
//        if (competitionPlayId != null) {
//            this.competitionPlay = competitionInstance.getCompetitionPlay(competitionPlayId);
//        }
    }
/*    public void initForXmlOutput() {
        for (int i = 0; i < ; i++) {

        }
    }*/

    public void removeParticipantTeamVoid() {
        List<ParticipantSeat> participantSeatsToRemove = new ArrayList<>();
        List<Participant> participantsToRemove = new ArrayList<>();
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant == null || participantSeat.participant.localId == null || participantSeat.participant.isVoid()) {
                participantSeatsToRemove.add(participantSeat);
                if (participantSeat.participant != null && participantSeat.participant.isVoid()) {
                    participantsToRemove.add(participantSeat.participant);
                }
            }
        }
        this.competitionInstance.participantTeams.removeAll(participantsToRemove);
        this.participantSeats.removeAll(participantSeatsToRemove);
    }

    public boolean addParticipant(Participant participant) {
        boolean added = false;
        if (participant == null || !contains(participant)) {
            Sets.sort(this.participantSeats);
            for (ParticipantSeat participantSeat : this.participantSeats) {
                if (participantSeat.participant == null) {
                    participantSeat.participant = participant;
                    added = true;
                    break;
                }
            }
//            Sets.sort(participantSeats);
        }
        return added;
    }

    public boolean addParticipantSeat(ParticipantSeat participantSeat) {
        boolean added = false;
        List<ParticipantSeat> newParticipantSeats = new ArrayList<>();

        for (ParticipantSeat participantSeatCurrent : this.participantSeats) {
            if (participantSeat.isFree() && !added) {
                newParticipantSeats.add(participantSeat);
                added = true;
            } else {
                newParticipantSeats.add(participantSeatCurrent);
            }
        }
        this.participantSeats.clear();
        this.participantSeats.addAll(newParticipantSeats);
        return added;
    }

    public boolean contains(Participant participant) {
        for (ParticipantSeat participantSeat : this.participantSeats)
            if (participantSeat.participant != null && participantSeat.participant.compareTo(participant) == 0)
                return true;
        return false;
    }

    public int getRealParticipantQuantity() {
        int realParticipant = 0;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (!participantSeat.isFree()) {
                realParticipant++;
            }
        }
        return realParticipant;
    }

    public int getReservedParticipantQuantity() {
        int reservedParticipantQuantity = 0;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (participantSeat.participant != null) {
                reservedParticipantQuantity++;
            }
        }
        return reservedParticipantQuantity;
    }

    public void sortParticipantSeats() {
        Sets.sort(participantSeats);
    }

    public boolean isFull() {
        return this.participantSeats.size() == this.getRealParticipantQuantity();
    }

    public void clearDatabaseId() {

        if (participantSeats != null)
            for (ParticipantSeat participantSeat : participantSeats) {
                participantSeat.clearDatabaseId();
            }

    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.setCompetitionInstance(this.getCompetitionInstance());
        }
    }

    public boolean existsIn(Collection<ParticipantPairing> participantPairings) {
        boolean exists = false;
        for (ParticipantPairing participantPairing : participantPairings) {
            if (participantPairing.localId.compareTo(localId) == 0) {
                exists = true;
                break;
            }

        }
        return exists;
    }

    public void resetCache() {
//        this.competitionGroupCache = null;
//        this.competitionGroupCache = null;
//        this.competitionMatchCache = null;
//        this.competitionMatchCache = null;
//        this.competitionPlayCache = null;
//        this.competitionPlayCache = null;
//        this.competitionRoundCache = null;
//        this.competitionRoundCache = null;
//        this.competitionSeedCache = null;
//        this.competitionSeedCache = null;
//        if (this.participantSeats != null) {
//            for (ParticipantSeat participantSeat :
//                    this.participantSeats) {
//                participantSeat.resetCache();
//            }
//        }
    }

//    public void fillCache(boolean up, boolean down) {
//        resetCache();
//        if (up) {
//            if (this.getCompetitionSeed() != null)
//                this.getCompetitionSeed().fillCache(false, false);
//            if (this.getCompetitionGroup() != null)
//                this.getCompetitionGroup().fillCache(false, false);
//            if (this.getCompetitionRound() != null)
//                this.getCompetitionRound().fillCache(false, false);
//            if (this.getCompetitionMatch() != null)
//                this.getCompetitionMatch().fillCache(false, false);
//            if (this.getCompetitionPlay() != null)
//                this.getCompetitionPlay().fillCache(false, false);
//        }
//        if (down && participantSeats != null)
//            for (ParticipantSeat participantSeat : participantSeats) {
//                if (participantSeat.competitionInstance == null)
//                    participantSeat.competitionInstance = getCompetitionInstance();
//                participantSeat.fillCache(false, down);
//            }
//    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);

        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.spreadCompetitionInstance(competitionInstance);
        }
    }

    public ParticipantPairing cloneForContext(CompetitionObjectWithResult competitionObjectWithResult) {
        ParticipantPairing participantPairing = new ParticipantPairing();
        participantPairing.id = this.id;
        participantPairing.localId = this.localId;
        participantPairing.databaseId = this.databaseId;
        Collection<? extends ParticipantSeat> participantSeats = this.competitionInstance.getParticipantSeats(this.participantSeats);
        participantPairing.participantSeats.addAll(participantSeats);
        return participantPairing;
    }

    public void removeParticipant(Participant participant) {
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null && participantSeat.participant.compareTo(participant) == 0) {
                participantSeat.participant = null;
                break;
            }
        }
    }
}
