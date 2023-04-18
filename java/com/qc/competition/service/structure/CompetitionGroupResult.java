package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.utils.Sets;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Duncan on 05/01/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CompetitionGroupResult extends Observable implements Comparable<CompetitionGroupResult>, Serializable, StatisticsElement, Simplify<CompetitionGroupResult> {
    public static String CLASS = CompetitionGroupResult.class.getSimpleName();
    @XmlAttribute(name = "groupId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("groupId")
    public CompetitionGroup competitionGroup = null;
    @XmlAttribute(name = "seedId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("seedId")
    public CompetitionSeed competitionSeed = null;
    @XmlAttribute(name = "nextSeedId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("nextSeedId")
    public CompetitionSeed competitionSeedNext = null;
    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("results")
    public SortedSet<ParticipantResult> participantResults = new TreeSet<>();
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId;
    //    public CompetitionSeed findCompetitionSeed(int localId) {
//        CompetitionSeed competitionSeed = null;
//        if (this.competitionSeedNext != null)
//            competitionSeed = this.competitionSeedNext.findCompetitionSeed(localId);
//        return competitionSeed;
//    }
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "phaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;
    @XmlTransient
    @JsonIgnore
    public boolean deleted;
    //    @XmlTransient
//    @JsonProperty("competitionGroupCache")
//    protected CompetitionGroup competitionGroupCache;
//    @XmlTransient
//    @JsonProperty("competitionSeedNextCache")
//    protected CompetitionSeed competitionSeedNextCache;
    @XmlTransient
    @JsonIgnore
    private StatisticsStructure expectedGlobalDuration = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    private boolean expectedGlobalDurationInitialized = false;
    @XmlTransient
    @JsonIgnore
    private StatisticsStructure expectedParticipantDuration = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    private boolean expectedParticipantDurationInitialized = false;
    @XmlTransient
    @JsonIgnore
    private StatisticsStructure expectedGlobalStep = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    private boolean expectedGlobalStepInitialized = false;
    @XmlTransient
    @JsonIgnore
    private StatisticsStructure expectedParticipantPlay = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    private boolean expectedParticipantPlayInitialized = false;
    @XmlTransient
    @JsonIgnore
    private StatisticsStructure expectedGlobalPlay = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    private boolean expectedGlobalPlayInitialized = false;

    public CompetitionGroupResult() {
        super();
    }

    private CompetitionGroupResult(IdGenerator idGenerator) {
        super();
        this.localId = idGenerator.getId(this.getClass().getSimpleName());
        this.id = idGenerator.getId();
    }

    public static CompetitionGroupResult createInstance(IdGenerator idGenerator) {
        return new CompetitionGroupResult(idGenerator);
    }

    public String getLocalId() {
        return localId;
    }

    public void clear() {
//        if (this.competitionSeedNext != null)
//            this.getCompetitionSeedNext().clear();
        this.participantResults.clear();
    }

    @Override
    public int compareTo(CompetitionGroupResult o) {
        int compareValue = 0;
        if (compareValue == 0 && this.getLocalId() != null && o.getLocalId() != null && this.getLocalId().compareTo(o.getLocalId()) == 0) {
            compareValue = 0;
        } else {
            if (competitionGroup == null)
                if (o.competitionGroup == null)
                    compareValue = 0;
                else
                    compareValue = -1;
            else if (o.competitionGroup == null)
                compareValue = 1;
            else {
                if (getCompetitionInstance() == null) {
                    compareValue = competitionGroup.compareTo(o.competitionGroup);
                } else {
                    if (getCompetitionGroup().competitionSeed != null && o.getCompetitionGroup().competitionSeed != null)
                        compareValue = getCompetitionGroup().competitionSeed.compareTo(o.getCompetitionGroup().competitionSeed);
                    if (compareValue == 0)
                        compareValue = -Integer.compare(getCompetitionGroup().lane, o.getCompetitionGroup().lane);
                    else
                        compareValue = getCompetitionGroup().getCompetitionSeed().compareTo(o.getCompetitionGroup().getCompetitionSeed());
                }
            }
        }
        return compareValue;
    }

    public void fillRegistrationsAsParticipantResult() {
        Sets.sort(getCompetitionInstance().participantSeats);
        this.participantResults.clear();
        int maxRank = 1;
        for (ParticipantSeat participantSeat : getCompetitionInstance().participantSeats) {
            ParticipantResult participantResult = ParticipantResult.createParticipantResultFor(competitionInstance.getIdGenerator(), null);
            participantResult.setCompetitionInstance(getCompetitionInstance());
            participantResult.setParticipant(participantSeat.getParticipant());
            if (participantSeat.previousRanking != null) {
                participantResult.rank = participantSeat.previousRanking;
                if (maxRank < participantSeat.previousRanking)
                    maxRank = participantSeat.previousRanking;
                if (participantResult.participantScore == null)
                    participantResult.participantScore = new ParticipantScoreGroup(this.competitionGroup);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.RANK, participantResult.rank);
            }
            if (participantSeat.participantScoreRating != null) {
                if (participantResult.participantScore == null)
                    participantResult.participantScore = new ParticipantScoreGroup(this.competitionGroup);
                participantResult.participantScore.updateParticipantScoreRatingFromParticipantScoreRating(participantSeat.participantScoreRating);
            }
            this.participantResults.add(participantResult);
        }
        Sets.sort(this.participantResults);
        for (ParticipantResult participantResult : this.participantResults) {
            if (participantResult.rank == null) {
                participantResult.rank = maxRank + 1;
                participantResult.participantScore = new ParticipantScoreGroup(this.competitionGroup);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreGroup.RANK, maxRank + 1);
            }
        }
//        getCompetitionGroup().updateResultDependencies();
//        getCompetitionGroup().close();
    }

    public Set<CompetitionGroupResult> computeLastCompetitionGroupResults() {
        Set<CompetitionGroupResult> competitionGroupResults = new HashSet<>();
        if (competitionSeedNext == null)
            competitionGroupResults.add(this);
        else
            competitionGroupResults.addAll(getCompetitionSeedNext().computeLastCompetitionGroupResults());
        return competitionGroupResults;
    }

    public ArrayList<ParticipantResult> getParticipantResultsAsArray() {
        ArrayList<ParticipantResult> participantResultArrayList = new ArrayList<>();
        participantResultArrayList.addAll(participantResults);
        return participantResultArrayList;
    }

    public int getParticipantResultSize() {
        return participantResults.size();
    }

//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        description.append("ExpectedGlobalAverageDuration=").append(this.getExpectedGlobalDuration()).append(System.lineSeparator());
//        description.append("ParticipantResultSize=").append(this.getParticipantResultSize()).append(System.lineSeparator());
//        if (competitionGroup != null)
//            description.append("[competitionGroup]").append(System.lineSeparator()).append(getCompetitionGroup().toString()).append(System.lineSeparator());
//        description.append("[participantResults]").append(System.lineSeparator());
//        for (ParticipantResult participantResult : participantResults) {
//            description.append(participantResult.toString()).append(System.lineSeparator());
//        }
//        if (competitionSeedNext != null)
//            description.append("[competitionSeedNext]").append(System.lineSeparator()).append(getCompetitionSeedNext().toDescription()).append(System.lineSeparator());
//        return description.toString();
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        if (!participantResults.isEmpty()) {
//            description.append(indentation).append("[participantResults]").append(System.lineSeparator());
//            for (ParticipantResult participantResult : participantResults) {
//                description.append(participantResult.toDescriptionTree(level + 1));
//            }
//        }
//        if (competitionSeedNext != null)
//            description.append(indentation).append("[competitionSeedNext]").append(System.lineSeparator()).append(getCompetitionSeedNext().toDescriptionTreeShort(level + 1));
//        return description.toString();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//        Element elementParticipantResults = document.createElement(ParticipantResult.class.getSimpleName() + "s");
//        if (!this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                elementParticipantResults.appendChild(participantResult.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantResults);
//
//
//        if (competitionSeedNext != null) {
//            element.appendChild(getCompetitionSeedNext().toDescriptionXmlShort(document));
//        }
//        return element;
//    }

    @Override
    public String toString() {
        return "CompetitionGroupResult{" +
                "localId=" + localId +
                ",name=" + internationalizedLabel.defaultLabel +
                ",participantResultsSize=" + getParticipantResultSize() +
                '}';
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
//        this.resetCache();
//        this.resetStatistics();
    }

    public void initForXmlOutput() {
//        if (this.competitionSeedNext != null)
//            this.competitionSeedNext = this.competitionSeedNext.localId;
        for (ParticipantResult participantResult : participantResults) {
            participantResult.initForXmlOutput();
        }
//        if (this.competitionSeedNext != null ) {
//            this.competitionSeedNext.initForXmlOutput();
//        }
    }

    public void reset(boolean recursive) {
        this.participantResults.clear();
        if (recursive && this.competitionSeedNext != null)
            this.getCompetitionSeedNext().reset(recursive);
        resetCache();
    }

    public void reset() {
        this.reset(true);
    }

    public void initFromXmlInputResult(CompetitionInstance competitionInstance, boolean recursive) {
        this.setCompetitionInstance(competitionInstance);
        for (ParticipantResult participantResult : participantResults) {
            participantResult.initFromXmlInput(competitionInstance);
        }

        Sets.sort(participantResults);

//        if (competitionSeedNext != null && recursive)
//            competitionSeedNext.initFromXmlInputResult(competitionInstance);

    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        if (!expectedGlobalDurationInitialized && competitionSeedNext != null) {
            expectedGlobalDuration = getCompetitionSeedNext().getExpectedGlobalDuration();
            expectedGlobalDurationInitialized = true;
        } else {
            expectedGlobalDuration.avg = 0L;
            expectedGlobalDuration.min = 0L;
            expectedGlobalDuration.max = 0L;
        }
        return expectedGlobalDuration;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {
        if (!expectedParticipantDurationInitialized && competitionSeedNext != null) {
            expectedParticipantDuration = getCompetitionSeedNext().getExpectedParticipantDuration();
            expectedParticipantDurationInitialized = true;
        } else {
            expectedParticipantDuration.avg = 0L;
            expectedParticipantDuration.min = 0L;
            expectedParticipantDuration.max = 0L;
        }
        return expectedParticipantDuration;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        if (!expectedGlobalStepInitialized && competitionSeedNext != null) {
            expectedGlobalStep = getCompetitionSeedNext().getExpectedGlobalStep();
            expectedGlobalStepInitialized = true;
        } else {
            expectedGlobalStep.avg = 0L;
            expectedGlobalStep.min = 0L;
            expectedGlobalStep.max = 0L;
        }
        return expectedGlobalStep;
    }

    @Override
    public void fillStatistics() {
        getExpectedGlobalDuration();
        getExpectedGlobalPlay();
        getExpectedParticipantDuration();
        getExpectedParticipantPlay();
        getExpectedGlobalStep();
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        if (!expectedParticipantPlayInitialized && competitionSeedNext != null) {
            expectedParticipantPlay = getCompetitionSeedNext().getExpectedParticipantPlay();
            expectedParticipantPlayInitialized = true;
        } else {
            expectedParticipantPlay.avg = 0L;
            expectedParticipantPlay.min = 0L;
            expectedParticipantPlay.max = 0L;
        }
        return expectedParticipantPlay;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayInitialized && competitionSeedNext != null) {
            expectedGlobalPlay = getCompetitionSeedNext().getExpectedGlobalPlay();
            expectedGlobalPlayInitialized = true;
        }
        return expectedGlobalPlay;
    }

    public DescriptionTable toDescriptionTable() {
        DescriptionTable descriptionTable = new DescriptionTable();
        descriptionTable.append("ResultSize", this.getParticipantResultSize());
        if (this.competitionSeedNext != null)
            descriptionTable.append(this.getCompetitionSeedNext().toDescriptionTable());
        return descriptionTable;
    }

    public boolean isParticipantResultSet(Participant participant) {
        boolean participantResultSet = false;
        for (ParticipantResult participantResult : this.participantResults) {
            participantResultSet = participantResult.participant.compareTo(participant) == 0;
            if (participantResultSet)
                break;
        }
        return participantResultSet;
    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        for (ParticipantResult participantResult : participantResults) {
            participantScore = participantResult.findParticipantScore(localId);
            if (participantScore != null)
                break;
        }
        return participantScore;

    }

    public void addAllParticipantResults(SortedSet<ParticipantResult> participantResults) {
        this.participantResults.clear();
        this.participantResults.addAll(participantResults);
    }

    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
    }

    public void clearDatabaseId() {
        this.databaseId = null;
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }

    }

    protected void fillResultCache() {
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.fillCache(false, true);
            }

    }

    @Override
    public CompetitionGroupResult cloneSimplified() {
        CompetitionGroupResult competitionGroupResult = null;
        try {
            competitionGroupResult = (CompetitionGroupResult) this.clone();
            if (this.participantResults != null) {
                List<ParticipantResult> participantResults = new ArrayList<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    participantResults.add(participantResult.cloneSimplified());
                }
                competitionGroupResult.participantResults = new TreeSet<>();
                competitionGroupResult.participantResults.addAll(participantResults);
            }
            competitionGroupResult.fillResultCache();

        } catch (CloneNotSupportedException e) {
        }
        return competitionGroupResult;
    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    public CompetitionGroup getCompetitionGroup() {
//        if (competitionGroupCache == null && competitionGroup != null) {
//            competitionGroupCache = getCompetitionInstance().getCompetitionGroup(competitionGroup);
//        }
        return competitionGroup;
    }

    public CompetitionSeed getCompetitionSeedNext() {
//        if (competitionSeedNextCache == null && competitionSeedNext != null) {
//            competitionSeedNextCache = competitionInstance.getCompetitionSeed(competitionSeedNext);
//        }
        return competitionSeedNext;
    }

    @Override
    public void resetStatistics() {
        expectedParticipantPlayInitialized = false;
        expectedGlobalPlayInitialized = false;
        expectedParticipantDurationInitialized = false;
        expectedGlobalDurationInitialized = false;
        expectedGlobalStepInitialized = false;
    }

    public void resetCache() {
        for (ParticipantResult participantResult : participantResults) {
            participantResult.resetCache();
        }
//        this.competitionGroupCache = null;
//        this.competitionGroupCache = null;
//        this.competitionSeedNextCache = null;
//        this.competitionSeedNextCache = null;
    }

//    public void fillCache(boolean up, boolean down) {
//        resetCache();
//        if (down) {
//            for (ParticipantResult participantResult : participantResults) {
//                participantResult.fillCache(false, down);
//            }
//        }
//        if (down && getCompetitionSeedNext() != null)
//            getCompetitionSeedNext().fillCache(false, false);
//        if (up && getCompetitionGroup() != null)
//            getCompetitionGroup().fillCache(false, false);
//    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults)
                participantResult.spreadCompetitionInstance(competitionInstance);
    }

    public void sortContent() {
        Sets.sort(participantResults);
    }


    public void forceChanged() {
        this.setChanged();
    }

    public void removeParticipant(Participant participant) {
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.participant != null && participantResult.participant.compareTo(participant) == 0) {
                participantResults.remove(participantResult);
                break;
            }
        }

    }

    public boolean isParticipantsResultSet() {
        boolean participantsResultSet = !participantResults.isEmpty();
        if (participantsResultSet) {
            for (ParticipantResult participantResult : participantResults) {
                participantsResultSet = !participantResult.isEmpty();
                if (!participantsResultSet)
                    break;
            }
        }
        return participantsResultSet;
    }

    public void delete() {
        if (competitionSeedNext != null && competitionSeedNext.competitionPhase.compareTo(this.competitionPhase) == 0) {
            if (competitionSeedNext.getCompetitionGroupResultsPrevious().isEmpty() || competitionSeedNext.getCompetitionGroupResultsPrevious().size() == 1) {
                competitionSeedNext.delete();
            } else if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && competitionGroup.lane > 1) {
                competitionSeedNext.delete();
                /*if (competitionGroup.lane > 2) {
                    Set<CompetitionGroupResult> competitionGroupResultNextCompetitionSeed = new HashSet<>();
                    for (CompetitionGroup competitionGroup : competitionSeedNext.competitionGroups) {
                        competitionGroupResultNextCompetitionSeed.add(competitionGroup.competitionGroupResult);
                    }
                    CompetitionGroup competitionGroupLaneMinus1 = competitionGroup.competitionSeed.getCompetitionGroupForLane(competitionGroup.lane - 1);
                    CompetitionGroup competitionGroupLaneMinus2 = competitionGroup.competitionSeed.getCompetitionGroupForLane(competitionGroup.lane - 2);

                    competitionGroupLaneMinus1.competitionGroupResult.competitionSeedNext = competitionGroupLaneMinus2.competitionGroupResult.competitionSeedNext;

                    competitionGroupLaneMinus2.competitionGroupResult.competitionSeedNext.getCompetitionGroupResultsPrevious().remove(this);
                    competitionGroupLaneMinus2.competitionGroupResult.competitionSeedNext.getCompetitionGroupResultsPrevious().add(competitionGroupLaneMinus1.competitionGroupResult);

                }*/
            }
        }

        this.deleted = true;
        this.competitionInstance.competitionGroupResults.remove(this);
        if (this.participantResults != null)
            this.getCompetitionInstance().participantResults.removeAll(this.participantResults);

    }
}