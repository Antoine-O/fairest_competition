package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.db.entity.registration.RegistrationStatus;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.automatic.participation.optimization.PhaseType;
import com.qc.competition.utils.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Duncan on 09/01/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ParticipantSeat implements Comparable<ParticipantSeat>, Serializable, Cloneable, JSONObject {
    public static String CLASS = ParticipantSeat.class.getSimpleName();
    @XmlAttribute(name = "localId")

    @JsonProperty("localId")
    public String localId;

    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "competitionGroupId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionGroupId")
    public CompetitionGroup competitionGroup;

    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    //    @XmlTransient
//    @JsonIgnore
//    public Participant participantSave;
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @XmlAttribute(name = "participantId")
    @JsonProperty("participantId")
    public Participant participant;
    @XmlAttribute(name = "date")
    public Calendar registrationDate;
    @XmlAttribute(name = "status")
    public RegistrationStatus registrationStatus;
    @XmlAttribute(name = "competitionSeedId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionSeedId")
    public CompetitionSeed competitionSeed;
    @XmlAttribute(name = "competitionPhaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionPhaseId")
    public CompetitionPhase competitionPhase;
    public ParticipantScoreRating participantScoreRating;
    @XmlAttribute(name = "previousRanking")
    @JsonProperty("previousRanking")
    public Integer previousRanking;
    @XmlAttribute(name = "index")

    @JsonProperty("index")
    public Integer index;

    public ParticipantSeat() {
        super();
    }

    protected ParticipantSeat(IdGenerator idGenerator) {
        this();
        this.localId = idGenerator.getId(this.getClass().getSimpleName());
        this.id = idGenerator.getId();
    }

    public static ParticipantSeat createInstance(IdGenerator idGenerator, CompetitionInstance competitionInstance) {
        if (idGenerator == null)
            idGenerator = competitionInstance.getIdGenerator();
        ParticipantSeat participantSeat = new ParticipantSeat(idGenerator);
        participantSeat.setCompetitionInstance(competitionInstance);
        return participantSeat;
    }

    public CompetitionGroup getCompetitionGroup() {
        return competitionGroup;
    }

    public void setCompetitionGroup(CompetitionGroup competitionGroup) {
        this.competitionGroup = competitionGroup;
    }

    public CompetitionSeed getCompetitionSeed() {
        return competitionSeed;
    }
//    @JsonProperty("participantCache")
//    public Participant participantCache;

    public void setCompetitionSeed(CompetitionSeed competitionSeed) {
        this.competitionSeed = competitionSeed;
    }

    public CompetitionPhase getCompetitionPhase() {
        return competitionPhase;
    }

    public void setCompetitionPhase(CompetitionPhase competitionPhase) {
        this.competitionPhase = competitionPhase;
    }


    public void clear() {
        setParticipant(competitionInstance.createParticipantTeamVoid());
    }

    @Override
    public int compareTo(ParticipantSeat o) {
        int compare = 0;
//        if (localId.compareTo(o.localId) != 0) {
//            if (this.participant != null && o.participant != null) {
//                compare = this.participant.compareTo(o.participant);
//            } else if (this.participant != null) {
//                compare = -1;
//            } else {
//                compare = 1;
//            }
        if (previousRanking != null && previousRanking > 0) {
            if (o.previousRanking != null && o.previousRanking > 0) {
                compare = previousRanking.compareTo(o.previousRanking);
            } else {
                compare = -1;
            }
        } else {
            if (o.previousRanking != null && o.previousRanking > 0) {
                compare = 1;
            }
        }
        if (compare == 0) {
            if (localId != null && o.localId != null) {
                compare = localId.compareTo(o.localId);
            } else if (localId != null) {
                compare = 1;
            } else {
                compare = -1;
            }
        }
//        }
        return compare;
    }

    public boolean isFree() {
        return getParticipant() == null || getParticipant().isVoid();
    }

    public void reset() {
        participant = null;
        previousRanking = null;
        resetCache();
    }

    public String toDescription() {
        StringBuilder description = new StringBuilder();
        description.append(toString());
        if (getParticipant() != null) {
            description.append(getParticipant().toDescription());
        }
        return description.toString();
    }

    public String toDescriptionTree(int level) {
        String indentation = StringUtil.getIndentationForLevel(level);
        return indentation + toString() + System.lineSeparator();
    }

    public Element toDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        if (getParticipant() != null)
            element.appendChild(getParticipant().toDescriptionXml(document));
        return element;
    }

    @Override
    public String toString() {
        return "ParticipantSeat{" +
                "localId=" + localId +
                ", participant=" + participant +
                '}';
    }

    public void initForXmlOutput(boolean emptyParticipant) {
//        if (getParticipant() != null && !getParticipant().isVoid()) {
//            if (participant != null) {
//                participant.initForXmlOutput(!emptyParticipant);
//            } else if (participantSave != null)
//                participantId = participantSave.localId;
//            if (emptyParticipant && participant != null) {
//                participantSave = participant;
//                participant = null;
//            }
//        }
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
        this.resetCache();
    }

    public void resetCache() {
//        this.participantCache = null;
//        this.participantCache = null;
    }

    public Participant findParticipant(String localId) {
        Participant participant = null;
        if (this.participant != null) {
            if (this.participant.localId.compareTo(localId) == 0) {
                participant = this.getParticipant();
            } else {
                this.getParticipant().getParticipant(localId);
            }
//        if (participant == null && this.participant instanceof ParticipantTeam)
//            participant = this.participant.getParticipant(localId);
        }

        return participant;
    }

    public Participant getParticipant() {
//        if ((participantCache == null && participantId != null) || (participantCache != null && participantId != null && participantCache.localId.compareTo(participantId) != 0)) {
//            participantCache = competitionInstance.getParticipantTeamOrSingle(participantId);
//        } else if (participantCache != null && participantId == null) {
//            participantCache = null;
//        }
//        return participantCache;
        return participant;
    }

    public void setParticipant(Participant participant) {
//        if (participantId != null) {
//            Participant currentParticipant = competitionInstance.getParticipant(participantId);
//            if (currentParticipant instanceof ParticipantTeamVoid) {
//                competitionInstance.removeParticipant(currentParticipant);
//                competitionInstance.removeParticipantTeam((ParticipantTeamVoid) currentParticipant);
//            }
//        }
//        participantId = participant.localId;
        Participant newParticipant = null;
        if (participant != null && participant.localId != null) {
            newParticipant = competitionInstance.getParticipant(participant.localId);
            if (newParticipant instanceof ParticipantTeamVoid) {
                competitionInstance.removeParticipant(newParticipant);
                competitionInstance.removeParticipantTeam((ParticipantTeamVoid) newParticipant);
                newParticipant = null;
            }
        }
        if (newParticipant != null) {
            boolean removeCurrentParticipant = false;
            boolean setCurrentParticipant = false;
            if (this.participant != null) {
                if (participant == null || participant.compareTo(this.participant) != 0) {
                    removeCurrentParticipant = true;
                }
                if (participant != null && participant.compareTo(this.participant) != 0) {
                    setCurrentParticipant = true;
                }
            } else {
                if (participant != null) {
                    setCurrentParticipant = true;
                }
            }
            if (removeCurrentParticipant) {
                if (competitionGroup != null) {
                    competitionGroup.removeParticipant(participant);
                    competitionGroup.participantSeats.remove(this);
                }
                if (competitionSeed != null) {
                    competitionSeed.removeParticipant(participant);
                    competitionSeed.participantSeats.remove(this);
                }
                if (competitionPhase != null) {
                    competitionPhase.removeParticipant(participant);
                    competitionPhase.participantSeats.remove(this);
                }
                if (competitionPhase != null && competitionPhase.round == 1 && competitionPhase.phaseType.compareTo(PhaseType.SEEDING) == 0 && competitionInstance != null) {
                    competitionInstance.removeParticipant(participant);
                    competitionInstance.participantSeats.remove(this);
                    competitionInstance.participantSeatsAll.remove(this);

                }
            }
            this.participant = participant;
            if (setCurrentParticipant) {
                if (competitionGroup != null) {
                    competitionGroup.addParticipantSeat(this);
                }
                if (competitionSeed != null && competitionSeed.getParticipantSeat(participant) == null) {
                    competitionSeed.addParticipantSeat(this);
                }
                if (competitionPhase != null && competitionPhase.getParticipantSeat(participant) == null) {
                    competitionPhase.addParticipantSeat(this);
                }
                if (competitionInstance != null && competitionInstance.getParticipantSeat(participant) == null) {
                    competitionInstance.addParticipantSeat(this);
                }

            }
        }

//        participantCache = null;
    }

    public List<Participant> getParticipantsAsArrayForContext() {
        List<Participant> participantArrayList = new ArrayList<>();
//        participantArrayList.add(participant);
        participantArrayList.addAll(participant.getParticipantsAsArrayForContext(participantArrayList));
        return participantArrayList;
    }

    public boolean contains(Participant participant) {

        Participant participantFound = null;
        if (this.getParticipant() != null)
            this.getParticipant().getParticipant(participant.localId);
        return participantFound != null;
    }

    public void clearDatabaseId() {
//        if (this.participantCache != null)
//            this.participantCache.clearDatabaseId();
    }

    public void fillCache(boolean up, boolean down) {
        resetCache();
//        if (down) {
//            if (this.participantId != null) {
//                this.participantCache = competitionInstance.getParticipantTeamOrSingle(this.participantId);
//                this.participantCache.setCompetitionInstance(competitionInstance);
//                this.participantCache.fillCache(false, down);
//            }
//        }
    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
        competitionInstance.participantSeats.add(this);

        getParticipant().spreadCompetitionInstance(competitionInstance);
    }


    public ParticipantResultTree toParticipantResultTree() {
        ParticipantResultTree participantResultTree = new ParticipantResultTree();
        if (participant != null) {
            participantResultTree.localId = participant.localId;
            participantResultTree.databaseId = getParticipant().databaseId;
            participantResultTree.rank = this.previousRanking;
            participantResultTree.participantTree = getParticipant().toParticipantTree();
            participantResultTree.label = participantResultTree.participantTree.label;
        } else {
            participantResultTree.label = "";
        }
        return participantResultTree;

    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
        competitionInstance.participantSeatsAll.add(this);

    }
}

