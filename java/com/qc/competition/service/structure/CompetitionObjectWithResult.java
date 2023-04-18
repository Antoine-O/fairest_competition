package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.service.structure.adaptater.DurationAdapter;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.json.JSONObject;
import com.qc.competition.ws.simplestructure.Duration;
import org.goochjs.glicko2.Rating;
import org.goochjs.glicko2.RatingCalculator;
import org.goochjs.glicko2.RatingPeriodResults;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 15/02/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CompetitionGroup.class,
        CompetitionInstance.class,
        CompetitionMatch.class,
        CompetitionPlay.class,
        CompetitionRound.class,
        CompetitionSeed.class,
        CompetitionPhase.class})

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompetitionGroup.class, name = "CompetitionGroup"),
        @JsonSubTypes.Type(value = CompetitionInstance.class, name = "CompetitionInstance"),
        @JsonSubTypes.Type(value = CompetitionMatch.class, name = "CompetitionMatch"),
        @JsonSubTypes.Type(value = CompetitionPlay.class, name = "CompetitionPlay"),
        @JsonSubTypes.Type(value = CompetitionRound.class, name = "CompetitionRound"),
        @JsonSubTypes.Type(value = CompetitionSeed.class, name = "CompetitionSeed"),
        @JsonSubTypes.Type(value = CompetitionPhase.class, name = "CompetitionPhase"),
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class CompetitionObjectWithResult<UP extends CompetitionObjectWithResult, SUB extends CompetitionObjectWithResult> extends Observable implements JSONObject, Cloneable, StatisticsElement, Comparable<CompetitionObjectWithResult<UP, SUB>> {
    public String type;
    @XmlAttribute(name = "expectedRelativeStartTime")
    @JsonProperty("expectedRelativeStartTime")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedRelativeStartTime;
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId = null;


    @XmlAttribute(name = "expectedRelativeEndTime")
    @JsonProperty("expectedRelativeEndTime")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedRelativeEndTime;

    @XmlAttribute(name = "expectedDuration")
    @JsonProperty("expectedDuration")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedDuration;

    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    @XmlAttribute(name = "status")
    @JsonProperty("status")
    public CompetitionObjectStatus competitionObjectStatus = CompetitionObjectStatus.NOT_INITIALIZED;
    @XmlTransient
    @JsonIgnore
    public boolean deleted;
    @XmlAttribute(name = "openDate")
    public Calendar openDate;
    @XmlAttribute(name = "closeDate")
    public Calendar closeDate;
    @XmlTransient
    @JsonIgnore
    protected StatisticsStructure expectedGlobalDurationStatisticsStructure = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    protected boolean expectedGlobalDurationStatisticsStructureInitialized = false;
    @XmlTransient
    @JsonIgnore
    protected StatisticsStructure expectedGlobalPlayStatisticsStructure = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    protected boolean expectedGlobalPlayStatisticsStructureInitialized = false;
    @XmlTransient
    @JsonIgnore
    protected StatisticsStructure expectedParticipantDurationStatisticsStructure = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    protected boolean expectedParticipantDurationStatisticsStructureInitialized = false;
    @XmlTransient
    @JsonIgnore
    protected StatisticsStructure expectedParticipantPlayStatisticsStructure = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    protected boolean expectedParticipantPlayStatisticsStructureInitialized = false;
    @XmlTransient
    @JsonIgnore
    protected StatisticsStructure expectedGlobalStepStatisticsStructure = new StatisticsStructure();
    @XmlTransient
    @JsonIgnore
    protected boolean expectedGlobalStepStatisticsStructureInitialized = false;
    @XmlTransient
    @JsonIgnore
    protected IdGenerator idGeneratorCache;
    @XmlTransient
    @JsonIgnore
    protected CompetitionObserver competitionObserver;

    public CompetitionObjectWithResult() {
        super();
    }

    protected CompetitionObjectWithResult(IdGenerator idGenerator) {
        super();
        this.idGeneratorCache = idGenerator;
        this.localId = idGeneratorCache.getId(this.getClass().getSimpleName());
        this.id = idGeneratorCache.getId();
    }

    public abstract CompetitionInstance getCompetitionInstance();

    public abstract void setCompetitionInstance(CompetitionInstance competitionInstance);

    public void resetCache() {
        if (getParticipantResults() != null)
            for (ParticipantResult participantResult : getParticipantResults()) {
//                participantResult.resetCache();
            }
        if (getParticipantPairings() != null)
            for (ParticipantPairing participantPairing : getParticipantPairings()) {
                participantPairing.resetCache();
            }
    }

    abstract void addParticipantResult(ParticipantResult participantResult);

    abstract boolean allParticipantResultsSet();

    public int compareTo(CompetitionObjectWithResult o) {
        int compareResult;
        if (this.id != null && o.id != null && this.id.compareTo(o.id) == 0) {
            compareResult = this.id.compareTo(o.id);
        } else {
            compareResult = this.getClass().getName().compareTo(o.getClass().getName());
            if (compareResult == 0) {
                if (compareResult == 0 && this.getUpperCompetitionObjectWithResult() != null && o.getUpperCompetitionObjectWithResult() != null) {
                    compareResult = this.getUpperCompetitionObjectWithResult().compareTo(o.getUpperCompetitionObjectWithResult());
                    if (compareResult == 0 && this.getRoundOrLane() != null && o.getRoundOrLane() != null) {
                        compareResult = this.getRoundOrLane().compareTo(o.getRoundOrLane());
                    }
                }
                if (compareResult == 0 && this.getLocalId() != null && o.getLocalId() != null && this.getLocalId().compareTo(o.getLocalId()) == 0) {
                    compareResult = this.getLocalId().compareTo(o.getLocalId());
                } else {
                    compareResult = 0;
                    if (getCompetitionInstance() == null) {
                        if (getParentCompetitionObjectWithResultId() != null && o.getParentCompetitionObjectWithResultId() != null) {
                            compareResult = getParentCompetitionObjectWithResultId().compareTo(o.getParentCompetitionObjectWithResultId());
                        }
                    } else {
                        if (compareResult != 0) {
                            if (getParentCompetitionObjectWithResult() != null && o.getParentCompetitionObjectWithResult() != null) {
                                compareResult = getParentCompetitionObjectWithResult().compareTo(o.getParentCompetitionObjectWithResult());
                            }
                        }
                    }
                    if (compareResult == 0 && this.getRoundOrLane() != null && o.getRoundOrLane() != null) {
                        compareResult = this.getRoundOrLane().compareTo(o.getRoundOrLane());
                    }
                    if (compareResult == 0) {
                        if (this.getLocalId() != null && o.getLocalId() != null) {
                            compareResult = this.getLocalId().compareTo(o.getLocalId());
                        } else if (this.getLocalId() != null) {
                            compareResult = 1;
                        } else {
                            compareResult = -1;
                        }
                    }
                }
            }
        }
        return compareResult;
    }

    protected abstract Integer getRoundOrLane();

    abstract ParticipantScore createInitialParticipantScore();

    abstract CompetitionObjectWithResult getParentCompetitionObjectWithResult();

    abstract String getParentCompetitionObjectWithResultId();

    final public List<CompetitionObjectWithResult> getPreviousParentCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        CompetitionObjectWithResult competitionObjectWithResult = this.getParentCompetitionObjectWithResult();
        if (competitionObjectWithResult != null)
            competitionObjectWithResults.addAll(competitionObjectWithResult.getPreviousCompetitionObjectWithResults());
        return competitionObjectWithResults;
    }

    abstract List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults();

    abstract List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults();

    abstract void updateResultDependencies() throws CompetitionInstanceGeneratorException;

    public String getLocalId() {
        return localId;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public abstract boolean isOverForParticipant(Participant participant);

    public boolean isForParticipant(Participant participant) {
        boolean hasParticipant = false;
        if (participant == null)
            hasParticipant = true;
        else
            for (ParticipantPairing participantPairing : getParticipantPairings())
                if (participantPairing.getAllRealParticipantsAsArray().contains(participant))
                    hasParticipant = true;
        return hasParticipant;
    }

//    @XmlElementWrapper(name = "pairings")
//    @XmlElement(name = "pairing")
//    @JsonProperty("pairings")
//    public SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();

    final public boolean isOverForParticipants(List<Participant> participants) {
        boolean isOverForParticipants = true;
        for (Participant participant : participants) {
            isOverForParticipants = isOverForParticipant(participant);
            if (!isOverForParticipants)
                break;
        }
        return isOverForParticipants;
    }

    final public boolean isPreviousOverForParticipants(List<Participant> participants) {
        boolean isPreviousOver = true;
        for (Participant participant : participants) {
            isPreviousOver = isPreviousOverForParticipant(participant);
            if (!isPreviousOver) break;
        }
        return isPreviousOver;
    }

    final public boolean isPreviousOverForParticipant(Participant participant) {
        boolean previousOver = true;

        for (CompetitionObjectWithResult competitionObjectWithResult : getPreviousParentCompetitionObjectWithResults()) {
            previousOver = competitionObjectWithResult.isOverForParticipant(participant);
            if (!previousOver)
                break;
        }
        if (previousOver) {

            for (CompetitionObjectWithResult competitionObjectWithResult : getPreviousCompetitionObjectWithResults()) {
                previousOver = competitionObjectWithResult.isOverForParticipant(participant);
                if (!previousOver)
                    break;
            }
        }
        return previousOver;
    }

    public SortedSet<Participant> getAllOpponentsFor(Participant participant) {
        SortedSet<Participant> participants = new TreeSet<>();
        if (isForParticipant(participant)) {
            for (SUB competitionObjectWithResult : this.getSubCompetitionObjectWithResults()) {
                participants.addAll(competitionObjectWithResult.getAllOpponentsFor(participant));
            }
        }
        return participants;
    }

    public abstract SortedSet<SUB> getSubCompetitionObjectWithResults();

    public abstract UP getUpperCompetitionObjectWithResult();

    public abstract boolean isParticipantPairingDefined();

    public void close() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            SortedSet<SUB> competitionObjectWithResults = getSubCompetitionObjectWithResults();
            if (competitionObjectWithResults != null) {
                for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                    competitionObjectWithResult.close();
                }
            }
            this.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
            this.closeDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));

//            this.fillStatistics();
        }
    }

    public boolean isSubClosed() {
        boolean isSubClosed = true;
        if (getSubCompetitionObjectWithResults() != null) {
            for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                if (!competitionObjectWithResult.isCancelled() && !competitionObjectWithResult.isClosed()) {
                    isSubClosed = false;
                    break;
                }
            }
        }
        return isSubClosed;
    }

    public boolean isClosed() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.CLOSED) == 0;
    }

    public boolean isCancelled() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.CANCELLED) == 0;
    }

    public boolean isOpen() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS) == 0;
    }

    public boolean isInitialized() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.WAIT_FOR_START) == 0;
    }

//    public SortedSet<CompetitionPlay> getOpenCompetitionPlaysForParticipant(Integer participantId) {
//        return getCompetitionPlays(false, true, false, participantId, true);
//
//    }
//
//    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
//        return getCompetitionPlays(false, true, false, null, true);
//    }
//
//    public SortedSet<CompetitionPlay> getInitializedCompetitionPlays() {
//        return getCompetitionPlays(true, false, false, null, true);
//    }
//
//    public SortedSet<CompetitionPlay> getCompetitionPlays(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<CompetitionPlay>();
//        if ((!initialized || this.isInitialized()) && (!open || this.isOpen()) && (!closed || this.isClosed()) && (participantId == null || isForParticipant(participantId))) {
//            for (CompetitionObjectWithResult competitionObjectWithResult : getSubCompetitionObjectWithResults())
//                competitionPlays.addAll(competitionObjectWithResult.getCompetitionPlays(initialized, open, closed, participantId, recursive));
//        }
//        return competitionPlays;
//    }
//
//    public SortedSet<CompetitionMatch> getInitializedCompetitionMatches() {
//        return getCompetitionMatches(true, false, false, null, true);
//    }
//
//    public SortedSet<CompetitionMatch> getOpenCompetitionMatches() {
//        return getCompetitionMatches(false, true, false, null, true);
//    }
//
////    public final CompetitionGroup addCompetitionGroup(CompetitionGroup competitionGroupNew, CompetitionSeed competitionSeed) {
////        CompetitionInstance competitionInstance = this.getCompetitionInstance();
////        for (CompetitionGroup competitionGroup : competitionInstance.competitionGroupsCache) {
////            if (competitionGroup.localId.compareTo(competitionGroupNew.localId) == 0) {
////                competitionInstance.competitionGroupsCache.remove(competitionGroupNew);
////                break;
////            }
////        }
////        competitionGroupNew.setCompetitionInstance(competitionInstance);
////        competitionGroupNew.setCompetitionSeed(competitionSeed);
////        competitionInstance.competitionGroupsCache.add(competitionGroupNew);
////
////        Sets.sort(competitionInstance.competitionGroupsCache);
////        return competitionGroupNew;
////    }
//
//    public SortedSet<CompetitionMatch> getCompetitionMatches(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<CompetitionMatch>();
//        for (CompetitionObjectWithResult competitionObjectWithResult : getSubCompetitionObjectWithResults())
//            competitionMatches.addAll(competitionObjectWithResult.getCompetitionMatches(initialized, open, closed, participantId, recursive));
//        return competitionMatches;
//    }

    public void open() throws CompetitionInstanceGeneratorException {
        if (this.isInitialized()) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
//            Sets.sort(getSubCompetitionObjectWithResults());
            for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults())
                competitionObjectWithResult.open();
        }
    }

    public boolean isSubOpen() {
        for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults())
            if (!competitionObjectWithResult.isOpen())
                return false;
        return true;
    }

    public void clearParticipantPairings() {
        if (getParticipantPairings() != null && !getParticipantPairings().isEmpty()) {
            getParticipantPairings().clear();
            this.setChanged();
            notifyObservers(this.getLocalId());
        }
    }

    public final ParticipantPairing getParticipantPairing(String localId) {
        ParticipantPairing participantPairingFound = null;
        if (getParticipantPairings() != null && !getParticipantPairings().isEmpty()) {
            for (ParticipantPairing participantPairing : getParticipantPairings()) {
                if (participantPairing.localId.compareTo(localId) == 0) {
                    participantPairingFound = participantPairing;
                }
            }

        }
        return participantPairingFound;
    }

    public void addParticipantPairing(ParticipantPairing participantPairing) {
        if (getParticipantPairings() != null) {
            participantPairing.setCompetitionInstance(this.getCompetitionInstance());
            participantPairing.sortParticipantSeats();
            getParticipantPairings().add(participantPairing);
            sortParticipantPairings();
            this.setChanged();
            notifyObservers(this.getLocalId());
        }
    }

    protected abstract void sortParticipantPairings();

    public void addAllParticipantPairings(Set<ParticipantPairing> participantPairings) {
        if (getParticipantPairings() != null) {
            getParticipantPairings().addAll(participantPairings);
            this.setChanged();
            notifyObservers(this.getLocalId());
        }
    }


    public void removeParticipant(Participant participant) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        for (Participant participantCurrent : competitionInstance.participants) {
            if (participantCurrent.compareTo(participant) == 0) {
                competitionInstance.participants.remove(participantCurrent);
                break;
            }
        }
        for (ParticipantSeat participantSeat : competitionInstance.participantSeats) {
            if (participantSeat.participant != null && participantSeat.participant.compareTo(participant) == 0) {
                participantSeat.participant = null;
                break;
            }
        }
        for (ParticipantPairing participantPairing : competitionInstance.participantPairings) {
            if (participantPairing.contains(participant)) {
                participantPairing.removeParticipant(participant);
                break;
            }
        }
        for (ParticipantResult participantResult : competitionInstance.participantResults) {
            if (participantResult.participant != null && participantResult.participant.compareTo(participant) == 0) {
                competitionInstance.participantResults.remove(participantResult);
                break;
            }
        }
    }

    public final void removeParticipantTeam(ParticipantTeam participantTeam) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        for (Participant participantCurrent : competitionInstance.participantTeams) {
            if (participantCurrent.localId.compareTo(participantTeam.localId) == 0) {
                competitionInstance.participantTeams.remove(participantCurrent);
                break;
            }
        }
    }

    public final Participant getParticipant(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        Participant participantFound = null;
        if (localId != null) {
            for (Participant participant : competitionInstance.participants) {
                if (participant != null && participant.localId.compareTo(localId) == 0) {
                    participantFound = participant;
                    break;
                }
            }
        }
//        if (participantFound == null) {
//            throw new IllegalArgumentException("participantId:" + localId + " not found");
//        }
        return participantFound;
    }

    public final CompetitionGroup getCompetitionGroup(String localId) {
        CompetitionGroup competitionGroupFound = null;
        for (CompetitionGroup competitionGroup : this.getCompetitionInstance().competitionGroups) {
            if (competitionGroup.localId.compareTo(localId) == 0) {
                competitionGroupFound = competitionGroup;
                break;
            }
        }
//        if(competitionGroupFound !=null && competitionGroupFound.getCompetitionInstance() == null)
//            competitionGroupFound.setCompetitionInstance(getCompetitionInstance());
        return competitionGroupFound;
    }

    public final CompetitionGroupResult getCompetitionGroupResult(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionGroupResult competitionGroupResultFound = null;
        for (CompetitionGroupResult competitionGroupResult : competitionInstance.competitionGroupResults) {
            if (competitionGroupResult.localId.compareTo(localId) == 0) {
                competitionGroupResultFound = competitionGroupResult;
                break;
            }
        }
        return competitionGroupResultFound;
    }

    public final void removeCompetitionGroupResult(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionGroupResult competitionGroupResultFound = null;
        for (CompetitionGroupResult competitionGroupResult : competitionInstance.competitionGroupResults) {
            if (competitionGroupResult.localId.compareTo(localId) == 0) {
                competitionGroupResultFound = competitionGroupResult;
                competitionInstance.competitionGroupResults.remove(competitionGroupResultFound);
                break;
            }
        }
    }

    public final CompetitionGroupResult addCompetitionGroupResult(CompetitionGroupResult competitionGroupResultNew, CompetitionGroup competitionGroup) {

        competitionGroupResultNew.competitionGroup = competitionGroup;
        competitionGroupResultNew.competitionSeed = competitionGroup.competitionSeed;
        if (competitionGroup.getCompetitionGroupResult() != null) {
            removeCompetitionGroupResult(competitionGroup.getCompetitionGroupResult().localId);
        }
        competitionGroup.competitionGroupResult = competitionGroupResultNew;
        getCompetitionInstance().competitionGroupResults.add(competitionGroupResultNew);
        return competitionGroupResultNew;
    }

    public final CompetitionMatch getCompetitionMatch(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionMatch competitionMatchFound = null;
        for (CompetitionMatch competitionMatch : competitionInstance.competitionMatches) {
            if (competitionMatch.localId.compareTo(localId) == 0) {
                competitionMatchFound = competitionMatch;
                break;
            }
        }
        return competitionMatchFound;
    }

    public final CompetitionMatch addCompetitionMatch(CompetitionMatch competitionMatchNew, CompetitionRound competitionRound) {
        competitionMatchNew.competitionRound = competitionRound;
        competitionRound.competitionMatches.add(competitionMatchNew);
        competitionMatchNew.internationalizedLabel.defaultLabel = "lane " + competitionMatchNew.lane;
        getCompetitionInstance().addCompetitionMatch(competitionMatchNew);
        return competitionMatchNew;
    }

    public final CompetitionRound getCompetitionRound(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionRound competitionRoundFound = null;
        for (CompetitionRound competitionRound : competitionInstance.competitionRounds) {
            if (competitionRound.localId.compareTo(localId) == 0) {
                competitionRoundFound = competitionRound;
                break;
            }
        }
        return competitionRoundFound;
    }

    public final CompetitionSeed getCompetitionSeed(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionSeed competitionSeedFound = null;
        for (CompetitionSeed competitionSeed : competitionInstance.competitionSeeds) {
            if (competitionSeed.localId.compareTo(localId) == 0) {
                competitionSeedFound = competitionSeed;
                break;
            }
        }
        return competitionSeedFound;
    }

    public final CompetitionRound addCompetitionRound(CompetitionRound competitionRoundNew, CompetitionGroup competitionGroup, CompetitionRound competitionRoundPrevious) {

        competitionRoundNew.competitionGroup = competitionGroup;
        competitionGroup.competitionRounds.add(competitionRoundNew);
        competitionRoundNew.internationalizedLabel.defaultLabel = competitionGroup.internationalizedLabel.defaultLabel + " - " + competitionRoundNew.round;
        if (competitionRoundPrevious != null) {
            competitionRoundNew.competitionRoundPrevious = competitionRoundPrevious;
            competitionRoundPrevious.competitionRoundNext = competitionRoundNew;
        }
        getCompetitionInstance().addCompetitionRound(competitionRoundNew);

        return competitionRoundNew;
    }

    public final CompetitionPlay getCompetitionPlay(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionPlay competitionPlayFound = null;
        for (CompetitionPlay competitionPlay : competitionInstance.competitionPlays) {
            if (competitionPlay.localId.compareTo(localId) == 0) {
                competitionPlayFound = competitionPlay;
                break;
            }
        }
        return competitionPlayFound;
    }

    public final CompetitionPlay addCompetitionPlay(CompetitionPlay competitionPlayNew, CompetitionMatch competitionMatch) {
        competitionPlayNew.competitionMatch = competitionMatch;
        competitionPlayNew.internationalizedLabel.defaultLabel = competitionPlayNew.playVersusType.name() + " - roundDetails " + competitionPlayNew.round;
        competitionMatch.competitionPlays.add(competitionPlayNew);
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        getCompetitionInstance().addCompetitionPlay(competitionPlayNew);
        return competitionPlayNew;
    }

    public final CompetitionPhase getCompetitionPhase(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionPhase competitionPhaseFound = null;
        if (competitionInstance.competitionPhases != null)
            for (CompetitionPhase competitionPhase : competitionInstance.competitionPhases) {
                if (competitionPhase.localId.compareTo(localId) == 0) {
                    competitionPhaseFound = competitionPhase;
                    break;
                }
            }
//        if (competitionPhaseFound != null && competitionPhaseFound.getCompetitionInstance() == null)
//            competitionPhaseFound.setCompetitionInstance(this.getCompetitionInstance());
        return competitionPhaseFound;
    }

//    public final CompetitionSeed getCompetitionSeed(String localId) {
//        CompetitionInstance competitionInstance = this.getCompetitionInstance();
//        CompetitionSeed competitionSeedFound = null;
//        if (competitionInstance.competitionSeeds != null)
//            for (CompetitionSeed competitionSeed : competitionInstance.competitionSeeds) {
//                if (competitionSeed.localId.compareTo(localId) == 0) {
//                    competitionSeedFound = competitionSeed;
//                    break;
//                }
//            }
////        if (competitionSeedFound != null && competitionSeedFound.getCompetitionInstance() == null)
////            competitionSeedFound.setCompetitionInstance(this.getCompetitionInstance());
//        return competitionSeedFound;
//    }

    public CompetitionSeed addCompetitionSeed(CompetitionSeed competitionSeedNew) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        competitionInstance.addCompetitionSeed(competitionSeedNew);
        return competitionSeedNew;
    }

    public final SortedSet<CompetitionSeed> getCompetitionSeeds(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionSeed> competitionSeedsFound = new TreeSet<>();
        for (CompetitionSeed competitionSeed : competitionInstance.competitionSeeds) {
            if (localIds.contains(competitionSeed.localId)) {
                competitionSeedsFound.add(competitionSeed);
            }
        }
        Sets.sort(competitionSeedsFound);
        return competitionSeedsFound;
    }

    public final SortedSet<CompetitionGroup> getCompetitionGroups(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionGroup> competitionGroupsFound = new TreeSet<>();
        for (CompetitionGroup competitionGroup : competitionInstance.competitionGroups) {
            if (localIds.contains(competitionGroup.localId)) {
                competitionGroupsFound.add(competitionGroup);
            }
        }
        Sets.sort(competitionGroupsFound);
        return competitionGroupsFound;
    }

    public final SortedSet<CompetitionGroupResult> getCompetitionGroupResults(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionGroupResult> competitionGroupResultsFound = new TreeSet<>();
        for (CompetitionGroupResult competitionGroupResult : competitionInstance.competitionGroupResults) {
            if (localIds.contains(competitionGroupResult.localId)) {
                competitionGroupResultsFound.add(competitionGroupResult);
            }
        }
        Sets.sort(competitionGroupResultsFound);
        return competitionGroupResultsFound;
    }

    public final SortedSet<CompetitionRound> getCompetitionRounds(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionRound> competitionRoundsFound = new TreeSet<>();
        for (CompetitionRound competitionRound : competitionInstance.competitionRounds) {
            if (localIds.contains(competitionRound.localId)) {
                competitionRoundsFound.add(competitionRound);
            }
        }
        Sets.sort(competitionRoundsFound);
        return competitionRoundsFound;
    }

    public final SortedSet<CompetitionMatch> getCompetitionMatches(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionMatch> competitionMatchesFound = new TreeSet<>();
        for (CompetitionMatch competitionMatch : competitionInstance.competitionMatches) {
            if (localIds.contains(competitionMatch.localId)) {
                competitionMatchesFound.add(competitionMatch);
            }
        }
        Sets.sort(competitionMatchesFound);
        return competitionMatchesFound;
    }

    public final SortedSet<CompetitionPlay> getCompetitionPlays(Collection<String> localIds) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (CompetitionPlay competitionPlay : competitionInstance.competitionPlays) {
            if (localIds.contains(competitionPlay.localId)) {
                competitionPlaysFound.add(competitionPlay);
            }
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    public ParticipantSeat getParticipantSeat(String localId) {
        ParticipantSeat participantSeatFound = null;
        CompetitionInstance competitionInstance = getCompetitionInstance();
        for (ParticipantSeat participantSeat : competitionInstance.participantSeats) {
            if (participantSeat.localId.compareTo(localId) == 0) {
                participantSeatFound = participantSeat;
                break;
            }
        }
        return participantSeatFound;
    }


    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        for (ParticipantResult participantResult : getParticipantResults()) {
            participantScore = participantResult.findParticipantScore(localId);
            if (participantScore != null)
                break;
        }
        if (participantScore == null)
            for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                participantScore = competitionObjectWithResult.findParticipantScore(localId);
                if (participantScore != null)
                    break;
            }
        return participantScore;
    }

    public abstract SortedSet<ParticipantResult> getParticipantResults();

    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        this.getParticipantResults().clear();
        for (ParticipantResult participantResult : participantResults) {
            participantResult.setCompetitionInstance(getCompetitionInstance());
            this.getParticipantResults().add(participantResult);
        }
        Sets.sort(this.getParticipantResults());
    }

    public CompetitionRound getCompetitionRoundForGroup(CompetitionGroup competitionGroup, Integer round) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionRound competitionRoundFound = null;
        for (CompetitionRound competitionRound : competitionInstance.competitionRounds) {
            if (competitionRound.competitionGroup.compareTo(competitionGroup) == 0 && competitionRound.round.compareTo(round) == 0) {
                competitionRoundFound = competitionRound;
                break;
            }
        }

        return competitionRoundFound;
    }


    public CompetitionPlay getCompetitionPlayForMatch(CompetitionMatch competitionMatch, Integer round) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        CompetitionPlay competitionPlayFound = null;
        for (CompetitionPlay competitionPlay : competitionInstance.competitionPlays) {
            if (competitionPlay.competitionMatch.compareTo(competitionMatch) == 0 && competitionPlay.round == round) {
                competitionPlayFound = competitionPlay;
                break;
            }
        }

        return competitionPlayFound;
    }

    final protected ParticipantResult getParticipantResultFor(Participant participant, boolean createIfNorExists) {
        ParticipantResult participantResult = null;
        for (ParticipantResult participantResultCurrent : getParticipantResults()) {
            if (participantResultCurrent.participant != null && participant != null && participantResultCurrent.participant.compareTo(participant) == 0) {
                participantResult = participantResultCurrent;
                break;
            }
        }
        if (participantResult == null && createIfNorExists) {
            participantResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
            participantResult.setParticipant(participant);
            getParticipantResults().add(participantResult);
        }

        return participantResult;
    }

    public IdGenerator getIdGenerator() {
        if (this.idGeneratorCache != null)
            return this.idGeneratorCache;
        else
            return this.getCompetitionInstance().idGeneratorCache;
    }

    public final void initFromXmlInput(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
        for (ParticipantPairing participantPairing : getParticipantPairings()) {
            participantPairing.initFromXmlInput(competitionInstance);
        }

        for (ParticipantResult participantResult : getParticipantResults()) {
            participantResult.initFromXmlInput(competitionInstance);
        }
//        this.resetCache();
//        this.resetStatistics();
    }

    public boolean isParticipantResultSet(Participant participant) {
        boolean participantResultSet = false;
        for (ParticipantResult participantResult : this.getParticipantResults()) {
            participantResultSet = participantResult.participant.compareTo(participant) == 0;
            if (participantResultSet)
                break;
        }
        return participantResultSet;
    }

    public boolean isParticipantResultsSet() {
        boolean participantResultSet = getParticipantPairings() != null && !getParticipantPairings().isEmpty();
        if (participantResultSet) {
            for (ParticipantPairing participantPairing : getParticipantPairings()) {
                for (Participant participant : participantPairing.getRealParticipantsAsArray())
                    participantResultSet = participantResultSet && isParticipantResultSet(participant);
                if (!participantResultSet)
                    break;
            }
        }
        return participantResultSet;
    }

    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = true;
        for (SUB competitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            participantResultSet = competitionObjectWithResult.isParticipantResultsSet();
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

//    @Deprecated
//    public CompetitionSeed createCompetitionSeed(Set<CompetitionGroupResult> competitionGroupResults, StepType stepType, PhaseType phaseType, CompetitionCreationParamPhase competitionCreationParamPhase) {
//        return getCompetitionInstance().createCompetitionSeed(competitionGroupResults, stepType, phaseType, competitionCreationParamPhase);
//    }

    public CompetitionSeed createCompetitionSeed(CompetitionPhase competitionPhase, Set<CompetitionGroupResult> competitionGroupResults, StepType stepType) {
        return getCompetitionInstance().createCompetitionSeed(competitionPhase, competitionGroupResults, stepType);
    }

    public CompetitionGroup createCompetitionGroup(CompetitionSeed competitionSeed) {
        return getCompetitionInstance().createCompetitionGroup(competitionSeed);
    }

    public CompetitionRound createCompetitionRound(CompetitionGroup competitionGroup, CompetitionRound competitionRoundPrevious, CompetitionObserver competitionObserver) {
        CompetitionRound competitionRound = getCompetitionInstance().createCompetitionRound(competitionGroup, competitionRoundPrevious);
        if (competitionObserver != null) {
            competitionRound.addObserver(competitionObserver);
            competitionRound.setChanged();
            competitionRound.notifyObservers(this.localId);
        }
        return competitionRound;
    }

    public CompetitionRound createCompetitionRound(CompetitionGroup competitionGroup, CompetitionRound competitionRoundPrevious) {
        return getCompetitionInstance().createCompetitionRound(competitionGroup, competitionRoundPrevious);
    }

    public CompetitionGroupResult createCompetitionGroupResult(CompetitionGroup competitionGroup) {
        return getCompetitionInstance().createCompetitionGroupResult(competitionGroup);
    }

    public CompetitionMatch createCompetitionMatch(CompetitionRound competitionRound, CompetitionObserver competitionObserver) {
        CompetitionMatch competitionMatch = getCompetitionInstance().createCompetitionMatch(competitionRound);
        if (competitionObserver != null) {
            competitionMatch.addObserver(competitionObserver);
            competitionMatch.setChanged();
            competitionMatch.notifyObservers(this.localId);
        }
        return competitionMatch;
    }

    public CompetitionMatch createCompetitionMatch(CompetitionRound competitionRound) {
        return getCompetitionInstance().createCompetitionMatch(competitionRound);
    }


    public CompetitionPlay createCompetitionPlay(CompetitionMatch competitionMatch, CompetitionObserver competitionObserver) {
        CompetitionPlay competitionPlay = getCompetitionInstance().createCompetitionPlay(competitionMatch);
        if (competitionObserver != null) {
            competitionPlay.addObserver(competitionObserver);
            competitionPlay.setChanged();
            competitionPlay.notifyObservers(this.localId);
        }
        return competitionPlay;
    }

    public CompetitionPlay createCompetitionPlay(CompetitionMatch competitionMatch) {
        return getCompetitionInstance().createCompetitionPlay(competitionMatch);
    }


    public void setParticipantResult(SortedSet<ParticipantResult> participantResults) {
        getParticipantResults().clear();
        for (ParticipantResult participantResult : participantResults) {
            participantResult.setCompetitionInstance(getCompetitionInstance());
            addParticipantResult(participantResult);
        }
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
    public void resetStatistics() {
        expectedParticipantPlayStatisticsStructureInitialized = false;
        expectedGlobalPlayStatisticsStructureInitialized = false;
        expectedParticipantDurationStatisticsStructureInitialized = false;
        expectedGlobalDurationStatisticsStructureInitialized = false;
        expectedGlobalStepStatisticsStructureInitialized = false;
    }

    public ParticipantResult getParticipantResultForParticipant(Participant participant) {
        ParticipantResult participantResultFound = null;
        SortedSet<ParticipantResult> participantResults = getParticipantResults();
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.getParticipant() != null && participantResult.getParticipant().compareTo(participant) == 0) {
                participantResultFound = participantResult;
                break;
            }
        }
        return participantResultFound;
    }

//    public final void reset() {
//        reset(true);
//    }

    public void reset(boolean recursive) {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
        this.getParticipantResults().clear();
        this.getParticipantPairings().clear();
        this.resetCache();
        if (recursive)
            for (SUB competitionObjectWithResult : this.getSubCompetitionObjectWithResults()) {
                competitionObjectWithResult.reset(recursive);
            }
    }

//    public void fillCache(boolean up, boolean down) {
//        resetCache();
//        if (up && getUpperCompetitionObjectWithResult() != null)
//            getUpperCompetitionObjectWithResult().fillCache(false, false);
//        if (down && this.getSubCompetitionObjectWithResults() != null) {
//            for (SUB competitionObjectWithResult : this.getSubCompetitionObjectWithResults()) {
//                competitionObjectWithResult.fillCache(false, false);
//            }
//        }
//        if (down) {
//            fillResultCache();
//            fillPairingCache();
//        }
//    }

    public void setCompetitionObjectStatus(CompetitionObjectStatus competitionObjectStatus) {
        if (this.competitionObjectStatus.compareTo(competitionObjectStatus) != 0) {
            this.competitionObjectStatus = competitionObjectStatus;
            setChanged();
            notifyObservers(this.localId);
        }
    }


    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        setCompetitionInstance(competitionInstance);
//        fillResultCache();
//        fillPairingCache();
    }


//    protected void fillResultCache() {
//        if (getParticipantResults() != null)
//            for (ParticipantResult participantResult : getParticipantResults()) {
//                if (participantResult.getCompetitionInstance() == null)
//                    participantResult.setCompetitionInstance(getCompetitionInstance());
//                participantResult.fillCache(false, true);
//            }
//
//    }

//    protected void fillPairingCache() {
//        if (getParticipantPairings() != null)
//            for (ParticipantPairing participantPairing : getParticipantPairings()) {
//                if (participantPairing.getCompetitionInstance() == null)
//                    participantPairing.setCompetitionInstance(getCompetitionInstance());
//                participantPairing.fillCache(false, true);
//            }
//    }

    abstract public SortedSet<ParticipantPairing> getParticipantPairings();

    public SortedSet<ParticipantPairing> getParticipantPairingsForContext() {
        SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
        for (ParticipantPairing participantPairing : getParticipantPairings()) {
            participantPairings.add(participantPairing.cloneForContext(this));
        }

        return participantPairings;
    }

    public SortedSet<ParticipantSeat> getParticipantSeatsForContext() {
        SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();
        for (ParticipantPairing participantPairing : getParticipantPairings()) {
            participantSeats.addAll(getCompetitionInstance().getParticipantSeats(participantPairing.participantSeats));
        }

        return participantSeats;
    }

    public SortedSet<ParticipantResult> getParticipantResultsForContext() throws CloneNotSupportedException, InstantiationException, IllegalAccessException {
        SortedSet<ParticipantResult> participantResults = new TreeSet<>();
        for (ParticipantResult participantResult : getParticipantResults()) {
            participantResults.add(participantResult.cloneForContext(this));
        }

        return participantResults;
    }


    public void sortContent() {
        sortParticipantPairings();
        for (ParticipantPairing participantPairing :
                getParticipantPairings()) {
            participantPairing.sortParticipantSeats();
        }
        if (getSubCompetitionObjectWithResults() != null) {
            Sets.sort(getSubCompetitionObjectWithResults());
        }
    }

    public SortedSet<Participant> getParticipants() {
        SortedSet<Participant> participants = new TreeSet<>();
        for (ParticipantPairing participantPairing : getParticipantPairings()) {
            participants.addAll(participantPairing.getRealParticipantsAsArray());
        }
        return participants;
    }


    public List<Participant> getParticipantsForContext() {
        Set<Participant> participants = new TreeSet<>();
        if (getParticipantPairings() != null) {
            for (ParticipantPairing participantPairing : getParticipantPairings()) {
                participants.addAll(participantPairing.getParticipantsAsArrayForContext());
            }
        }
        if (getParticipantResults() != null) {
            for (ParticipantResult participantResult : getParticipantResults()) {
                participants.addAll(participantResult.getParticipantsAsArrayForContext());
            }
        }

        List<Participant> participantsArray = new ArrayList<>();
        participantsArray.addAll(participants);
        return participantsArray;
    }


    public List<CompetitionObjectWithResult> getSuperCompetitionObjectWithResultsForContext() {
        boolean first = true;
        return getSuperCompetitionObjectWithResultsForContext(first);
    }

    protected List<CompetitionObjectWithResult> getSuperCompetitionObjectWithResultsForContext(boolean first) {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (this.getUpperCompetitionObjectWithResult() != null) {
            List<CompetitionObjectWithResult> competitionObjectWithResultsUp = this.getUpperCompetitionObjectWithResult().getSuperCompetitionObjectWithResultsForContext(false);
            if (competitionObjectWithResultsUp != null)
                competitionObjectWithResults.addAll(competitionObjectWithResultsUp);
        }
        if (!first) {
            CompetitionObjectWithResult competitionObjectWithResult = this.cloneForContext();
            competitionObjectWithResults.add(competitionObjectWithResult);
        }
        return competitionObjectWithResults;
    }


    public List<CompetitionObjectWithResult> getSubCompetitionObjectWithResultsForContext() {
        SortedSet<CompetitionObjectWithResult> competitionObjectWithResultsSet = new TreeSet<>();
        if (this.getSubCompetitionObjectWithResults() != null && !this.getSubCompetitionObjectWithResults().isEmpty()) {
            for (CompetitionObjectWithResult competitionObjectWithResult : this.getSubCompetitionObjectWithResults()) {
                CompetitionObjectWithResult competitionObjectWithResultCloned = competitionObjectWithResult.cloneForContext();
                competitionObjectWithResultsSet.add(competitionObjectWithResultCloned);
            }
        }
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        competitionObjectWithResults.addAll(competitionObjectWithResultsSet);
        return competitionObjectWithResults;
    }

    public abstract CompetitionObjectWithResult cloneForContext();

    public abstract CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException;

    public abstract void clearForContext();

    public List<ParticipantScore> getParticipantScoresSubForContext() throws IllegalAccessException, CloneNotSupportedException, InstantiationException {
        List<ParticipantScore> participantScoresSub = null;
        if (getParticipantResults() != null && !getParticipantResults().isEmpty()) {
            Set<ParticipantScore> participantScoresSubSet = new TreeSet<>();
            for (ParticipantResult participantResult : getParticipantResults()) {
                if (participantResult.participantScore != null && participantResult.participantScore.participantScoresSub != null && !participantResult.participantScore.participantScoresSub.isEmpty()) {
                    List<ParticipantScore> participantScores = participantResult.participantScore.participantScoresSub;
                    for (ParticipantScore participantScore : participantScores) {
                        participantScoresSubSet.add(participantScore.cloneForContext(this));
                    }
                }

            }
            if (!participantScoresSubSet.isEmpty()) {
                participantScoresSub = new ArrayList<>();
                participantScoresSub.addAll(participantScoresSubSet);
            }
        }
        return participantScoresSub;
    }

    protected void forceChanged() {
        this.setChanged();
        notifyObservers(this.getLocalId());
    }

    public void cancel() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.CANCELLED);

        for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            if (!subCompetitionObjectWithResult.isClosed())
                subCompetitionObjectWithResult.cancel();

        }
    }

    public void remove() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.CANCELLED);

        for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            subCompetitionObjectWithResult.remove();
        }
    }

    public abstract Map<Participant, List<Participant>> getParticipantOpponentsMap();

    public abstract void fillExpectedRelativeTime();

    public void delete() {
        this.deleted = true;
        this.setCompetitionObjectStatus(CompetitionObjectStatus.CANCELLED);
        List<SUB> subList = new ArrayList<>(getSubCompetitionObjectWithResults());
        for (SUB subCompetitionObjectWithResult : subList) {
            subCompetitionObjectWithResult.delete();
        }
        if (this.getParticipantResults() != null)
            this.getCompetitionInstance().participantResults.removeAll(this.getParticipantResults());
        if (this.getParticipantPairings() != null)
            this.getCompetitionInstance().participantPairings.removeAll(this.getParticipantPairings());
        this.notifyObservers();
    }

    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            if (subCompetitionObjectWithResult.isOpen())
                competitionPlaysFound.addAll(subCompetitionObjectWithResult.getOpenCompetitionPlays());
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    protected void disableGenerationFlag() {
        for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            subCompetitionObjectWithResult.disableGenerationFlag();
        }
    }


    protected void spreadCompetitionObserver() {
        if (this.getCompetitionObserver() != null) {
            for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                subCompetitionObjectWithResult.addObserver(this.getCompetitionObserver());
                subCompetitionObjectWithResult.spreadCompetitionObserver();
            }
        }
    }

    public void generationDone() {
        for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
            subCompetitionObjectWithResult.generationDone();
        }
    }

    public CompetitionObserver getCompetitionObserver() {
        if (competitionObserver == null)
            competitionObserver = this.getCompetitionInstance().getCompetitionObserver();
        return competitionObserver;
    }


    public synchronized void addObserver(CompetitionObserver competitionObserver, boolean recursive) {
        super.addObserver(competitionObserver);
        this.competitionObserver = competitionObserver;
        if (recursive) {
            spreadCompetitionObserver();
        }

    }

    public void setChanged(boolean recursive) {
        this.setChanged();
        this.notifyObservers(this);
        if (recursive) {
            for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                subCompetitionObjectWithResult.setChanged(recursive);
            }
        }

    }

    public Map<Participant, Rating> getParticipantRatingMap() {
            RatingCalculator ratingCalculator = new RatingCalculator(0.06, 0.5);
        RatingPeriodResults ratingPeriodResults = new RatingPeriodResults();
        Map<Participant, Rating> participantRatingMap = new HashMap<>();
        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
            for (Participant participant : competitionPlay.getParticipants()) {
                if (!participantRatingMap.containsKey(participant)) {
                    participantRatingMap.put(participant, new Rating(participant.localId, ratingCalculator));
                }
            }
        }
        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
            if (competitionPlay.isClosed() && !competitionPlay.competitionMatch.isBye()) {
                SortedMap<Integer, List<Participant>> rankParticipants = new TreeMap<>();
                for (ParticipantResult participantResult : competitionPlay.getParticipantResults()) {
                    if (!rankParticipants.containsKey(participantResult.rank)) {
                        rankParticipants.put(participantResult.rank, new ArrayList<>());
                    }
                    rankParticipants.get(participantResult.rank).add(participantResult.participant);
                }
                for (Map.Entry<Integer, List<Participant>> entry : rankParticipants.entrySet()) {
                    List<Participant> winners = new ArrayList<>(entry.getValue());
                    List<Participant> loosers = new ArrayList<>();
                    for (Map.Entry<Integer, List<Participant>> entryLoosers : rankParticipants.entrySet()) {
                        if (entry.getKey().compareTo(entryLoosers.getKey()) < 0) {
                            loosers.addAll(entryLoosers.getValue());
                        }
                    }
                    if (winners.size() > 1) {
                        for (int i = 0; i < winners.size(); i++) {
                            for (int j = i + 1; j < winners.size(); j++) {
                                ratingPeriodResults.addDraw(participantRatingMap.get(winners.get(i)), participantRatingMap.get(winners.get(j)));
                            }
                        }
                    }
                    if (!loosers.isEmpty()) {
                        for (Participant winner : winners) {
                            for (Participant looser : loosers) {
                                ratingPeriodResults.addResult(participantRatingMap.get(winner), participantRatingMap.get(looser));
                            }
                        }
                    }
                }
            } else if (competitionPlay.competitionMatch.isBye()) {

            }

        }
        ratingCalculator.updateRatings(ratingPeriodResults);
        return participantRatingMap;
    }

    protected abstract Iterable<? extends CompetitionPlay> getCompetitionPlays();

    public void forceNotifyAround() {
        this.setChanged();
        this.notifyObservers(this.getLocalId());
        if (getSubCompetitionObjectWithResults() != null)
            for (SUB subCompetitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                subCompetitionObjectWithResult.setChanged();
                subCompetitionObjectWithResult.notifyObservers(subCompetitionObjectWithResult.getLocalId());
            }
        if (getPreviousCompetitionObjectWithResults() != null)
            for (CompetitionObjectWithResult previousCompetitionObjectWithResult : getPreviousCompetitionObjectWithResults()) {
                previousCompetitionObjectWithResult.setChanged();
                previousCompetitionObjectWithResult.notifyObservers(previousCompetitionObjectWithResult.getLocalId());
            }
        if (getNextCompetitionObjectWithResults() != null)
            for (CompetitionObjectWithResult nextCompetitionObjectWithResult : getNextCompetitionObjectWithResults()) {
                nextCompetitionObjectWithResult.setChanged();
                nextCompetitionObjectWithResult.notifyObservers(nextCompetitionObjectWithResult.getLocalId());
            }
    }
}
