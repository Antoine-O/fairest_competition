package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.utils.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 22/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ParticipantResult implements Comparable<ParticipantResult>, Serializable, Cloneable, Simplify<ParticipantResult>, JSONObject {
    public static String CLASS = ParticipantResult.class.getSimpleName();
    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    @XmlAttribute(name = "participantId")
    @JsonProperty("participantId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public Participant participant;
    @XmlElement(name = "score")
    @JsonProperty("score")
    public ParticipantScore participantScore = null;
    @XmlAttribute(name = "rank")
    @JsonProperty("rank")
    public Integer rank;
    //    @XmlAttribute(name = "p_id")
//    public Integer participantId = 0;
    @XmlAttribute(name = "participantEqualityComparison")
    @JsonProperty("participantEqualityComparison")
    public Double participantEqualityComparison;
    @XmlAttribute(name = "submissionDate")
    @JsonProperty("submissionDate")
    public Calendar submissionDate;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;//    @XmlTransient
//    @JsonProperty("participantCache")
//    protected Participant participantCache = null;

    private ParticipantResult() {
        submissionDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
    }

    //    static public ParticipantResult createParticipantResultFor(IdGenerator idGenerator, CompetitionObjectWithResult competitionObjectWithResult) {
//
//    }
    static public ParticipantResult createParticipantResultFor(IdGenerator idGenerator, CompetitionObjectWithResult competitionObjectWithResult) {
        ParticipantResult participantResult = new ParticipantResult();
        if (idGenerator == null && competitionObjectWithResult != null) {
            if (competitionObjectWithResult.getCompetitionInstance() != null) {
                idGenerator = competitionObjectWithResult.getCompetitionInstance().getIdGenerator();
            } else if (competitionObjectWithResult.idGeneratorCache != null) {
                idGenerator = competitionObjectWithResult.idGeneratorCache;
            }
        }

        participantResult.localId = idGenerator.getId(ParticipantResult.class.getSimpleName());
        participantResult.id = idGenerator.getId();
//        participantResult.participant = participant;

        if (competitionObjectWithResult != null) {
            participantResult.setCompetitionInstance(competitionObjectWithResult.getCompetitionInstance());
            participantResult.participantScore = competitionObjectWithResult.createInitialParticipantScore();
        }
        return participantResult;
    }

    public void clear() {
        participantScore.clear();
        rank = null;
        ParticipantTeamVoid participantTeamVoid = competitionInstance.createParticipantTeamVoid();
        setParticipant(participantTeamVoid);
//        participantId = participantTeamVoid.localId;
    }

    @Override
    public int compareTo(ParticipantResult o) {
        int compareValue = 0;
        if (compareValue == 0) {
            if (rank != null && o.rank != null) {
                compareValue = Integer.compare(rank, o.rank);
            } else if (rank != null) {
                compareValue = -1;
            } else if (o.rank != null) {
                compareValue = 1;
            }
        }
        if (compareValue == 0 && participantScore != null && o.participantScore != null)
            compareValue = participantScore.compareScoreTo(o.participantScore);
        if (compareValue == 0 && participantEqualityComparison != null && o.participantEqualityComparison != null)
            compareValue = participantEqualityComparison.compareTo(o.participantEqualityComparison);
        if (compareValue == 0) {
            if (this.id != null && o.id != null && this.id.compareTo(o.id) != 0) {
                compareValue = this.id.compareTo(o.id);
            } else if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {
                if (this.localId != null && o.localId != null && this.localId.compareTo(o.localId) == 0) {
                    compareValue = 0;
                }
            }
            if (compareValue != 0 && participant != null && o.participant != null)
                compareValue = participant.compareTo(o.participant);
        }
        return compareValue;
    }

//    public String toDescription() {
//        String description = toString() + System.lineSeparator() +
//                participantScore.toSimpleDescription() + System.lineSeparator();
//
//        return description;
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        String description = indentation + toString() + System.lineSeparator() +
//                participantScore.toDescriptionTree(level + 1);
//        return description;
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("rank", "" + rank);
//        if (this.participant != null) {
//            element.setAttribute("participantId ", "" + participant.localId);
//        }
//
//        if (this.participantScore != null) {
//            Element elementParticipant = participantScore.toDescriptionXml(document);
//            element.appendChild(elementParticipant);
//        }
//
//        return element;
//    }
//
//    public String toDetailedDescription() {
//
//        return toString() + "\t" + participantScore.toDetailedDescription();
//    }
//
//    public String toSimpleDescription() {
//        StringBuilder description = new StringBuilder();
//        if (this.participant != null) {
//            description.append(getParticipant().toSimpleDescription());
//        }
//        description.append(participantScore.toSimpleDescription());
//
//        return description.toString();
//    }

    public Participant getParticipant() {
//        if (participantCache == null && participantId != null) {
//            participantCache = getCompetitionInstance().getParticipantTeamOrSingle(participantId);
//        }
//        return participantCache;
        return participant;
    }

    public void setParticipant(Participant participant) {
//        this.participantId = participant.localId;
//        this.participantCache = null;
        this.participant = participant;
        if (participant != null) {
            this.participantEqualityComparison = participant.equalityComparison;
            if (this.participantScore != null)
                this.participantScore.updateParticipantScoreRatingFromPreviousParticipantScoreRatingForParticipant(participant);

        }
    }

    public Element toSimpleDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        element.setAttribute("rank", "" + rank);
        if (this.participant != null) {
            element.setAttribute("participantId", "" + participant.localId);
        }

        if (this.participantScore != null) {
            List<ParticipantScoreValue> participantScoreValues = this.participantScore.participantScoreValues;
            for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
                element.setAttribute(participantScoreValue.name, "" + participantScoreValue.value);
            }
        }

        return element;
    }

    @Override
    public String toString() {
        return "ParticipantResult{" +
                "localId:" + localId +
                ", rank=" + rank +
                ", participantId=" + (participant != null ? participant.localId : null) + (participantScore != null ? "," + participantScore.toString() : "") +
                "}";
    }

    public void initForXmlOutput() {
//        participantId = participant.localId;
//        participantEqualityComparison = participant.equalityComparison;
        if (this.participantScore != null)
            this.participantScore.initForXmlOutput();

    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
        if (this.participantScore != null)
            this.participantScore.initFromXmlInput(competitionInstance);
//
//        this.resetCache();
    }

    public void resetCache() {
//        this.participantCache = null;
    }


    public ParticipantScore findParticipantScore(String localId) {
        if (participantScore != null)
            return participantScore.findParticipantScore(localId);
        return null;

    }

    public ParticipantResultTree toParticipantResultTree(Set<String> scoringConfigurationPlayElementsName) {
        return toParticipantResultTree(false, scoringConfigurationPlayElementsName);
    }

    public ParticipantResultTree toParticipantResultTree(boolean qualified, Set<String> scoringConfigurationPlayElementsName) {
        ParticipantResultTree participantResultTree = new ParticipantResultTree();
        if (participant != null) {
            participantResultTree.localId = participant.localId;
            participantResultTree.databaseId = getParticipant().databaseId;
            participantResultTree.points = this.participantScore.getParticipantScoreValuePoints() != null ? this.participantScore.getParticipantScoreValuePoints().computeNumberValue().intValue() : 0;
            for (String scoringConfigurationPlayElementName :
                    scoringConfigurationPlayElementsName) {
                if (ParticipantScorePlay.SCORE_GOAL.compareTo(scoringConfigurationPlayElementName) == 0)
                    participantResultTree.goal = this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName) != null ? this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName).computeNumberValue().intValue() : null;
                if (ParticipantScorePlay.SCORE_POINTS.compareTo(scoringConfigurationPlayElementName) == 0)
                    participantResultTree.points = this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName) != null ? this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName).computeNumberValue().intValue() : null;
                if (ParticipantScorePlay.SCORE_BAD_POINTS.compareTo(scoringConfigurationPlayElementName) == 0)
                    participantResultTree.badPoints = this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName) != null ? this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName).computeNumberValue().intValue() : null;
                if (ParticipantScorePlay.RANK.compareTo(scoringConfigurationPlayElementName) == 0)
                    participantResultTree.rank = this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName) != null ? this.participantScore.getParticipantScoreValue(scoringConfigurationPlayElementName).computeNumberValue().intValue() : null;
            }
            participantResultTree.participantTree = getParticipant().toParticipantTree();
            participantResultTree.label = participantResultTree.participantTree.label;
        } else {
            participantResultTree.label = "";
        }
        participantResultTree.rank = rank;
        participantResultTree.qualified = qualified;
        return participantResultTree;

    }

//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        ParticipantResult participantResult = new ParticipantResult();
//        participantResult.participant = participant.clone();
//        participantResult.rank = rank;
//        participantResult.participantScore = participantScore;
//        participantResult.competitionInstance = this.competitionInstance;
////        participantResult.participantCache = null;
//        return participantResult;
//    }

    public ParticipantResult cloneParticipantResult() {
        try {
            return (ParticipantResult) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearDatabaseId() {
//        if (participant != null)
//            getParticipant().clearDatabaseId();
    }

    @Override
    public ParticipantResult cloneSimplified() {
        ParticipantResult participantResult = null;
        try {
            participantResult = (ParticipantResult) this.clone();
            if (this.participantScore != null)
                participantResult.participantScore = participantResult.participantScore.getClass().cast(this.participantScore.cloneSimplified());
        } catch (CloneNotSupportedException e) {
        }
        return participantResult;
    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    public void fillCache(boolean up, boolean down) {
//        resetCache();
        if (down) {
            if (this.getParticipant().getCompetitionInstance() == null)
                this.getParticipant().setCompetitionInstance(getCompetitionInstance());
            this.getParticipant().fillCache(false, down);
        }
    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    public void removeRank() {
        this.rank = null;
        if (this.participantScore != null) {
            this.participantScore.removeRank();
        }
    }

    public ParticipantResult cloneForContext(CompetitionObjectWithResult competitionObjectWithResult) throws
            CloneNotSupportedException, IllegalAccessException, InstantiationException {
        ParticipantResult participantResult = new ParticipantResult();
        if (participant != null)
            participantResult.participant = participant.cloneForContext();
        participantResult.rank = rank;
        if (participantScore != null)
            participantResult.participantScore = participantScore.cloneForContext(competitionObjectWithResult);
        participantResult.competitionInstance = (CompetitionInstance) this.competitionInstance.cloneForContext();
//        participantResult.participantCache = null;
        return participantResult;
    }

    public List<Participant> getParticipantsAsArrayForContext() {
        List<Participant> participantArrayList = new ArrayList<>();
        if (participant != null) {
            participantArrayList.addAll(this.participant.getParticipantsAsArrayForContext(participantArrayList));
        }
        return participantArrayList;
    }

    public boolean isEmpty() {
        boolean isEmpty = true;
        if (this.participantScore != null && this.participantScore.participantScoreValues != null) {
            List<ParticipantScoreValue> participantScoreValues = this.participantScore.participantScoreValues;
            for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
                isEmpty = participantScoreValue.value == null;
                if (!isEmpty)
                    break;
            }

        }
        return isEmpty;
    }
}

