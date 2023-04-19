package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.tree.CompetitionRoundTree;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionCreationParamPhaseFinal;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.utils.Sets;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionRound extends CompetitionObjectWithResult<CompetitionGroup, CompetitionMatch> implements Simplify<CompetitionRound> {
    public static String CLASS = CompetitionRound.class.getSimpleName();
//    public static Logger LOGGER_initializeRound = Logger.getLogger(CLASS + ".initializeRound");

    @XmlAttribute(name = "groupId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("groupId")
    public CompetitionGroup competitionGroup;
    @XmlAttribute(name = "seedId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("seedId")
    public CompetitionSeed competitionSeed;
    @XmlAttribute(name = "phaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;
    //    @XmlElementWrapper(name = "matchIds")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "matchIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("matchIds")
    public SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
    @XmlAttribute(name = "nextRoundId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("nextRoundId")
    public CompetitionRound competitionRoundNext;
    @XmlAttribute(name = "previousRoundId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("previousRoundId")
    public CompetitionRound competitionRoundPrevious;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlElementWrapper(name = "pairings")
    @XmlElement(name = "pairing")
    @JsonProperty("pairings")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("results")
    public SortedSet<ParticipantResult> participantResults = new TreeSet<>();
    @XmlAttribute(name = "round")
    public Integer round;
    @XmlAttribute(name = "seedSequence")
    public Integer seedSequence;
    @XmlAttribute(name = "phaseSequence")
    public Integer phaseSequence;
    @XmlAttribute(name = "phaseLane")
    public Integer phaseLane;
    @XmlAttribute(name = "competitionSequence")
    public Integer competitionSequence;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;

    @XmlList
    @XmlAttribute(name = "beforeDependenciesRoundIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("beforeDependenciesRoundIds")
    public SortedSet<CompetitionRound> beforeDependenciesCompetitionRounds = new TreeSet<>();


    @XmlList
    @XmlAttribute(name = "afterDependenciesRoundIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("afterDependenciesRoundIds")
    public SortedSet<CompetitionRound> afterDependenciesCompetitionRounds = new TreeSet<>();


    public CompetitionRound() {
        super();
    }

    private CompetitionRound(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static CompetitionRound createInstance(IdGenerator idGenerator) {
        return new CompetitionRound(idGenerator);
    }

    //    @XmlTransient
//    @JsonIgnore
    public CompetitionGroup getCompetitionGroup() {
//        if (competitionGroupCache == null && competitionGroup != null) {
//            competitionGroupCache = this.getCompetitionInstance().getCompetitionGroup(competitionGroup);
//        }
        return competitionGroup;
    }

//    @XmlAttribute
//    private boolean winnerPushed;
//    @XmlAttribute
//    private boolean looserPushed;

    //    @XmlTransient
//    @JsonIgnore
    public SortedSet<CompetitionMatch> getCompetitionMatches() {
//        if (competitionMatchesCache == null && !competitionMatches.isEmpty()) {
//            competitionMatchesCache = this.getCompetitionMatches(this.competitionMatches);
//        }
        return competitionMatches;
    }

    //    @XmlTransient
//    @JsonIgnore
    public CompetitionRound getCompetitionRoundNext() {
//        if (competitionRoundNextCache == null && competitionRoundNext != null) {
//            competitionRoundNextCache = this.getCompetitionRound(competitionRoundNext);
//        }
        return competitionRoundNext;
    }

    //    @XmlTransient
//    @JsonIgnore
    public CompetitionRound getCompetitionRoundPrevious() {
//        if (competitionRoundPreviousCache == null && competitionRoundPrevious != null) {
//            competitionRoundPreviousCache = this.getCompetitionRound(competitionRoundPrevious);
//        }
        return competitionRoundPrevious;
    }


    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
    }

    @Override
    public boolean allParticipantResultsSet() {
        boolean allParticipantResultsSet = true;
        for (ParticipantPairing participantPairing : this.participantPairings) {
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                allParticipantResultsSet = allParticipantResultsSet &&
                        isParticipantResultSet(participant);
                if (!allParticipantResultsSet)
                    break;
            }
        }
        return allParticipantResultsSet;
    }

    public void clear() {
        this.participantPairings.clear();
        this.participantResults.clear();
//        for (CompetitionMatch competitionMatch : getCompetitionMatches())
//            competitionMatch.clear();
        this.participantResults.clear();
        this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);

    }


//    public void fillParticipantResultWithFakeValue() {
//        for (CompetitionMatch competitionMatch : competitionMatches) {
//            if (competitionMatch.participantPairing == null)
//                competitionMatch.initializeMatch();
//            competitionMatch.fillParticipantResultWithFakeValue();
//        }
//    }

//    @Override
//    public int compareTo(CompetitionObjectWithResult competitionObjectWithResult) {
//        int compare = this.getClass().getName().compareTo(competitionObjectWithResult.getClass().getName());
//        if (compare == 0)
//            compare = compareTo(this.getClass().cast(competitionObjectWithResult));
//        return compare;
//    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        return new ParticipantScoreRound(this);
    }

    public void fillCompetitionMatchLink() {
//        Set<CompetitionRound> competitionRoundsBefore = new HashSet<>();
        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
            competitionMatch.fillCompetitionMatchLink();
        }
    }


    public void fillCompetitionRoundLink() {
        Set<CompetitionRound> competitionRoundsBefore = new HashSet<>();
        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
            if (competitionMatch.previousCompetitionMatchLinks != null && !competitionMatch.previousCompetitionMatchLinks.isEmpty()) {
                for (CompetitionMatchLink competitionMatchPreviousLink : competitionMatch.previousCompetitionMatchLinks) {
                    CompetitionMatch previousCompetitionMatch = competitionMatchPreviousLink.previousCompetitionMatch;
                    competitionRoundsBefore.add(previousCompetitionMatch.competitionRound);
                }
            }
        }
        for (CompetitionRound competitionRound : competitionRoundsBefore) {
            addBeforeDependenciesCompetitionRound(competitionRound);
        }
        if (competitionRoundPrevious != null)
            addBeforeDependenciesCompetitionRound(competitionRoundPrevious);
    }

    private void addBeforeDependenciesCompetitionRound(CompetitionRound competitionRound) {
        this.beforeDependenciesCompetitionRounds.add(competitionRound);
        competitionRound.afterDependenciesCompetitionRounds.add(this);
    }

    public void fillCompetitionRoundResultFromCompetitionMatch() {
        this.getCompetitionGroup().competitionGroupFormat.fillCompetitionRoundResult(this);
    }

    public CompetitionMatch getCompetitionMatchFor(Participant participant) {
        CompetitionMatch competitionMatchForParticipant = null;
        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
            if (competitionMatch.isForParticipant(participant)) {
                competitionMatchForParticipant = competitionMatch;
                break;
            }
        }
        return competitionMatchForParticipant;
    }

    public Collection<CompetitionPlay> getCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
            competitionPlays.addAll(competitionMatch.getCompetitionPlays());
        }
        Sets.sort(competitionPlays);
        return competitionPlays;
    }

    //    @XmlTransient
//    @JsonIgnore
    public SortedSet<ParticipantResult> getParticipantResults() {
        return participantResults;
    }

    @Override
    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        super.setParticipantResults(participantResults);

    }

    //    @XmlTransient
//    @JsonIgnore
    public SortedSet<ParticipantResult> getParticipantResultsEliminated() {
        SortedSet<ParticipantResult> participantResultsEliminated = new TreeSet<>();
//        participantResultsEliminated.addAll(this.competitionGroup.competitionGroupFormat.getParticipantResultsEliminated(this));
        SortedSet<ParticipantResult> participantResultsTemp = new TreeSet<>();
        participantResultsTemp.addAll(this.getCompetitionGroup().competitionGroupFormat.getParticipantResultsEliminated(this));
        ParticipantResult participantResultCloned = null;
        for (ParticipantResult participantResult : participantResultsTemp) {
            participantResultCloned = participantResult.cloneParticipantResult();
            participantResultsEliminated.add(participantResultCloned);
        }
        return participantResultsEliminated;
    }

    public ParticipantResult getParticipantResultsForParticipant(Participant participant) {
        for (ParticipantResult participantResult : participantResults)
            if (participantResult.participant.compareTo(participant) == 0)
                return participantResult;
        return null;
    }

    //    @XmlTransient
//    @JsonIgnore
    public SortedSet<ParticipantResult> getParticipantResultsQualified() {
        SortedSet<ParticipantResult> participantResults = new TreeSet<>();
        participantResults.addAll(this.getCompetitionGroup().competitionGroupFormat.getParticipantResultsQualified(this));
        return participantResults;
    }

    public boolean initializeRound() {
//        Logger logger = LOGGER_initializeRound;
//        String logPrefix = this.toString();
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
        } else {
            if (competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {


//            if (!isParticipantResultsSet()) {
//                initialized = false;
                if (!isParticipantPairingDefined()) {
                    CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
                    if (competitionCreationParamPhase == null)
                        competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

                    this.getCompetitionGroup().competitionGroupFormat.createParticipantPairingForCompetitionGroupRound(this, competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.participantQualifiedPerMatch, competitionCreationParamPhase.playVersusType, competitionCreationParamPhase.participantTypeSplittable);
                }

                if (isParticipantPairingDefined()) {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
                    setChanged();
                    notifyObservers(this.localId);
                } else {
                    this.participantPairings.clear();
                    if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0)
                        this.competitionGroup.removeCompetitionRound(this);

                }

//                }
//            }
            }
        }
        return isInitialized();
    }

    //    @XmlTransient
//    @JsonIgnore
    public List<Participant> getParticipantsQualified() {
        List<Participant> participants = new ArrayList<>();
        if (competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch == 1) {
            if (this.isClosed()) {
                CompetitionCreationParamPhase competitionCreationParamPhase = competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;
                this.fillCompetitionRoundResultFromCompetitionMatch();
                int participantQualifiedSize = competitionGroup.competitionGroupFormat.computeNumberOfQualifiedParticipantPerMatch(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, competitionCreationParamPhase.participantQualifiedPerMatch, competitionMatches.size());
                if (!this.participantResults.isEmpty()) {
                    ParticipantResult[] participantResultsArray = new ParticipantResult[participantResults.size()];
                    this.participantResults.toArray(participantResultsArray);
                    for (int i = 0; i < participantQualifiedSize && i < this.participantResults.size(); i++) {
                        participants.add(participantResultsArray[i].participant);
                    }
                }
            }
        } else {
            SortedSet<CompetitionMatch> competitionMatches = Sets.sort(getCompetitionMatches());
            for (CompetitionMatch competitionMatch : competitionMatches) {
                participants.addAll(competitionMatch.getParticipantsQualified());
            }
        }
        return participants;
    }

    public List<Participant> getParticipantsEliminated() {
        List<Participant> participants = new ArrayList<>();
        if (competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch == 1) {
            if (this.isClosed()) {
                List<Participant> participantsQualified = getParticipantsQualified();
                for (CompetitionMatch competitionMatch : competitionMatches) {
                    participants.addAll(competitionMatch.getParticipants());
                }
                participants.removeAll(participantsQualified);
            }
        } else {
            SortedSet<CompetitionMatch> competitionMatches = Sets.sort(getCompetitionMatches());
            for (CompetitionMatch competitionMatch : competitionMatches) {
                participants.addAll(competitionMatch.getParticipantsEliminated());
            }
        }
//        Sets.sort(participants);
        return participants;
    }

    public boolean isFinished() {
        for (CompetitionMatch competitionMatch : getCompetitionMatches())
            if (!competitionMatch.isFinished())
                return false;
        return true;
    }

    @Override
    public boolean isParticipantResultSet(Participant participant) {
        boolean participantResultSet = false;
        for (ParticipantResult participantResult : this.participantResults) {
            participantResultSet = participantResult.participant.compareTo(participant) == 0;
            if (participantResultSet)
                break;
        }
        return participantResultSet;
    }

    @Override
    public boolean isParticipantResultsSet() {
        boolean participantResultSet = false;
        for (ParticipantPairing participantPairing : this.participantPairings) {
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                participantResultSet = isParticipantResultSet(participant);
                if (!participantResultSet)
                    break;
            }
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

    @Override
    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = true;
        for (CompetitionMatch competitionMatch : this.getCompetitionMatches()) {
            participantResultSet = competitionMatch.allParticipantResultsSet();
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

//    public void reset() {
//        this.clear();
//        this.participantResults.clear();
//        for (CompetitionMatch competitionMatch : this.getCompetitionMatches()) {
//            competitionMatch.reset();
//        }
//
//        this.setCompetitionObjectStatus (CompetitionObjectStatus.NOT_INITIALIZED;
//        resetCache();
//    }

//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        if (competitionRoundPrevious != null)
//            description.append("[competitionRoundPrevious]").append(System.lineSeparator()).append(getCompetitionRoundPrevious().toString()).append(System.lineSeparator());
//        if (competitionRoundNext != null)
//            description.append("[competitionRoundNext]").append(System.lineSeparator()).append(getCompetitionRoundNext().toDescription()).append(System.lineSeparator());
//        description.append("[participantPairings]").append(System.lineSeparator());
//        for (ParticipantPairing participantPairing : participantPairings) {
//            description.append(participantPairing.toString()).append(System.lineSeparator());
//        }
//        description.append("[competitionMatches]").append(System.lineSeparator());
//        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
//            description.append(competitionMatch.toDescription()).append(System.lineSeparator());
//        }
//        return description.toString();
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        if (!participantPairings.isEmpty()) {
//            description.append(indentation).append("[participantPairings]").append(System.lineSeparator());
//            for (ParticipantPairing participantPairing : participantPairings) {
//                description.append(participantPairing.toDescriptionTree(level + 1));
//            }
//        }
//        if (!competitionMatches.isEmpty()) {
//            description.append(indentation).append("[competitionMatches]").append(System.lineSeparator());
//            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
//                description.append(competitionMatch.toDescriptionTree(level + 1));
//            }
//        }
//        if (competitionRoundNext != null)
//            description.append(indentation).append("[competitionRoundNext]").append(System.lineSeparator()).append(getCompetitionRoundNext().toDescriptionTree(level + 1));
//
//        return description.toString();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("roundDetails", "" + round);
//
//        Element elementParticipantPairings = document.createElement(ParticipantPairing.class.getSimpleName() + "s");
//        if (!participantPairings.isEmpty()) {
//            for (ParticipantPairing participantPairing : participantPairings) {
//                elementParticipantPairings.appendChild(participantPairing.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantPairings);
//
//        Element elementCompetitionMatchs = document.createElement(CompetitionMatch.class.getSimpleName() + "s");
//        if (!competitionMatches.isEmpty()) {
//            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
//                elementCompetitionMatchs.appendChild(competitionMatch.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementCompetitionMatchs);
//
//        return element;
//    }
//
//    public Element toSimpleDescriptionXml(Document document, boolean withResult) {
//
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("roundDetails", "" + round);
//        element.setAttribute("numberOfMatch", "" + competitionMatches.size());
//
//
//        if (!competitionMatches.isEmpty()) {
//            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
//                element.appendChild(competitionMatch.toSimpleDescriptionXml(document, withResult));
//                if (!withResult)
//                    break;
//            }
//        }
//
//        if (withResult && participantResults != null && !participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        return element;
//    }

    @Override
    public String toString() {
        return "CompetitionRound{" +
                "localId=" + localId +
                ", round=" + round +
                ", status=" + competitionObjectStatus +
                (participantPairings != null ? ", participantPairings.size()=" + participantPairings.size() : "") +
                (competitionGroup != null ? ", competitionGroup=" + competitionGroup : "") +
                '}';
    }

    @Override
    public void updateResultDependencies() throws CompetitionInstanceGeneratorException {
        if (isSubClosed()) {
            setChanged();
            notifyObservers(this.localId);
            if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) != 0) {
                fillCompetitionRoundResultFromCompetitionMatch();
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                this.close();
            }
        }
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            fillCompetitionRoundResultFromCompetitionMatch();
            this.competitionGroup.fillCompetitionGroupResultFromCompetitionGroupRounds();
        }
    }
//
//    public CompetitionMatch getCompetitionMatch(int localId) {
//        for (CompetitionMatch competitionMatch : competitionMatches) {
//            if (competitionMatch.localId.compareTo(localId) == 0)
//                return competitionMatch;
//        }
//        return null;
//    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        if (!this.isClosed() && (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0 && this.isSubClosed()))
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                this.expectedRelativeStartTime = Duration.ofMinutes((new Date().getTime() - this.competitionInstance.startDate.getTime().getTime()) / (60 * 1000));
            }

            super.close();
            CompetitionGroup competitionGroup = this.getCompetitionGroup();
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                if ((!(competitionGroup.competitionPhase.competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) || !((CompetitionCreationParamPhaseFinal) competitionGroup.competitionPhase.competitionCreationParamPhase).thirdPlaceMatchEnabled)) {
                    CompetitionGroup competitionGroupNextLane = competitionGroup.getCompetitionSeed().getCompetitionGroupForLane(competitionGroup.lane + 1);
                    if (competitionGroupNextLane != null) {
                        competitionGroupNextLane.pushParticipants(this.getParticipantsEliminated());
                    }
                } else if (competitionGroup.competitionPhase.competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal && ((CompetitionCreationParamPhaseFinal) competitionGroup.competitionPhase.competitionCreationParamPhase).thirdPlaceMatchEnabled && this.round == this.competitionGroup.competitionRounds.size() - 1) {
                    CompetitionGroup competitionGroupNextLane = competitionGroup.getCompetitionSeed().getCompetitionGroupForLane(competitionGroup.lane + 1);
                    if (competitionGroupNextLane != null) {
                        competitionGroupNextLane.pushParticipants(this.getParticipantsEliminated());
                    }
                }
            } else if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0) {
                competitionGroup.updateResultDependencies();
            }
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) != 0) {
                if (competitionRoundNext != null && !competitionRoundNext.isOpen()) {
                    if (!competitionRoundNext.isInitialized())
                        competitionRoundNext.initializeRound();
                    if (competitionRoundNext != null && competitionRoundNext.isInitialized())
                        competitionRoundNext.open();
                } else if (competitionRoundNext == null) {
                    competitionGroup.updateResultDependencies();
                } else if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) != 0) {
                    competitionGroup.updateResultDependencies();
                }
            }
        }
    }


//    public ParticipantScore findParticipantScore(Integer localId) {
//        ParticipantScore participantScore = null;
//        for (ParticipantResult participantResult : participantResults) {
//            participantScore = participantResult.findParticipantScore(localId);
//            if (participantScore != null)
//                break;
//        }
//        if (participantScore == null)
//            for (CompetitionMatch competitionMatch : competitionMatches) {
//                participantScore = competitionMatch.findParticipantScore(localId);
//                if (participantScore != null)
//                    break;
//            }
//        return participantScore;
//    }
//
//    public CompetitionPlay getCompetitionPlay(Integer localId) {
//        CompetitionPlay competitionPlay = null;
//        for (CompetitionMatch competitionMatch : competitionMatches) {
//            competitionPlay = competitionMatch.getCompetitionPlay(localId);
//            if (competitionPlay != null)
//                break;
//        }
//        return competitionPlay;
//    }

//    public void initFromXmlInputResult(CompetitionInstance competitionInstance) {
//        for (CompetitionMatch competitionMatch : competitionMatches) {
//            competitionMatch.initFromXmlInputResult(competitionInstance);
//        }
//        for (ParticipantResult participantResult : participantResults) {
//            participantResult.initFromXmlInput(competitionInstance);
//        }
//        List<ParticipantResult> participantResultsTmp = new ArrayList<ParticipantResult>();
//        participantResultsTmp.addAll(participantResults);
//        participantResults.clear();
//        participantResults.addAll(participantResultsTmp);
//    }

    @Override
    public boolean isClosed() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.CLOSED) == 0;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    public ParticipantPairing findParticipantPairing(String localId) {
        for (ParticipantPairing participantPairing : participantPairings) {
            if (participantPairing.localId.compareTo(localId) == 0)
                return participantPairing;

        }
        return null;
    }

    public void initForXmlOutput() {

    }

    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                overForParticipant = competitionMatch.isOverForParticipant(participant);
                if (!overForParticipant)
                    break;
            }
            if (!isParticipantResultSet(participant))
                overForParticipant = false;
        }
        return overForParticipant;
    }

    @Override
    public SortedSet<ParticipantPairing> getParticipantPairings() {
        return participantPairings;
    }

    public void setParticipantPairings(SortedSet<ParticipantPairing> participantPairings) {
        this.participantPairings.clear();
        for (ParticipantPairing participantPairing : participantPairings) {
            participantPairing.setCompetitionRound(this);
            this.addParticipantPairing(participantPairing);
        }
    }

    @Override
    public CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return this.getCompetitionGroup();
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        SortedSet<CompetitionRound> competitionGroupRounds = this.getCompetitionGroup().getCompetitionRounds();
//        Sets.sort(competitionGroupRounds);

        for (CompetitionRound competitionRound : competitionGroupRounds) {
            if (this.compareTo(competitionRound) < 0)
                competitionObjectWithResults.add(competitionRound);
        }
        return competitionObjectWithResults;
    }


    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        SortedSet<CompetitionRound> competitionGroupRounds = this.getCompetitionGroup().getCompetitionRounds();
//        Sets.sort(competitionGroupRounds);

        for (CompetitionRound competitionRound : competitionGroupRounds) {
            if (this.compareTo(competitionRound) > 0)
                competitionObjectWithResults.add(competitionRound);
        }
        return competitionObjectWithResults;
    }

    public CompetitionRoundTree getCompetitionRoundTree() {
        CompetitionRoundTree competitionRoundTree = new CompetitionRoundTree();
        competitionRoundTree.internationalizedLabel = internationalizedLabel;
        competitionRoundTree.localId = getLocalId();
        competitionRoundTree.id = id;
        competitionRoundTree.databaseId = databaseId;
        competitionRoundTree.round = round;
        competitionRoundTree.phaseSequence = phaseSequence;
        competitionRoundTree.competitionSequence = competitionSequence;
        competitionRoundTree.expectedDuration = this.expectedDuration;
        competitionRoundTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionRoundTree.expectedRelativeEndTime = this.expectedRelativeEndTime;
        if (beforeDependenciesCompetitionRounds != null) {
            competitionRoundTree.beforeRoundIds = new HashSet<>();
            for (CompetitionRound competitionRound : beforeDependenciesCompetitionRounds) {
                competitionRoundTree.beforeRoundIds.add(competitionRound.id);
            }
        }
        competitionRoundTree.over = isFinished();

        boolean pairingDefined = true;

        for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
            pairingDefined = pairingDefined && competitionMatch.isParticipantPairingDefined();
            competitionRoundTree.competitionMatchTrees.add(competitionMatch.getCompetitionMatchTree());
        }

        competitionRoundTree.filled = pairingDefined;

        CompetitionRound competitionRound = getCompetitionGroup().getCompetitionRoundForRound(this.round - 1);
        if (competitionRound != null)
            competitionRoundTree.competitionRoundTreePrevious = competitionRound.getCompetitionRoundTree();
        return competitionRoundTree;

    }

    public SortedSet<CompetitionMatch> getCompetitionMatches(boolean open, boolean closed) {
        return getCompetitionMatches(open, closed, null);
    }

    @Override
    public void open() throws CompetitionInstanceGeneratorException {
        if (this.getCompetitionGroup().isOpen()) {
            this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0 && this.expectedRelativeStartTime == null) {
                this.expectedRelativeStartTime = Duration.ofMinutes((new Date().getTime() - this.competitionInstance.startDate.getTime().getTime()) / (60 * 1000));
            }
            if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.WAIT_FOR_START) == 0 && this.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) != 0) {
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                setChanged();
                notifyObservers(this.localId);
                Sets.sort(competitionMatches);
                for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                    competitionMatch.initializeMatch();
                    competitionMatch.open();
                }
            } else if (this.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) {
                if (competitionRoundPrevious == null || getCompetitionRoundPrevious().isClosed()) {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);

                    setChanged();
                    notifyObservers(this.localId);
                    Sets.sort(competitionMatches);
                    for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                        competitionMatch.initializeMatch();
                        if (!competitionMatch.isOpen())
                            competitionMatch.open();
                        else
                            for (CompetitionPlay competitionPlay : competitionMatch.getCompetitionPlays())
                                competitionPlay.open();
                    }
                }
            }
            if (this.isOpen()) {
                for (CompetitionMatch competitionMatch : this.competitionMatches) {
                    if (competitionMatch.isOpen()) {
                        competitionMatch.checkBye();
                        if (competitionMatch.getMatchType() != null && competitionMatch.getMatchType().compareTo(MatchType.BYE) == 0) {
                            competitionMatch.checkCompetitionCompetitionObjectStatus(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void sortParticipantPairings() {
        if (participantPairings != null)
            Sets.sort(participantPairings);
    }

    public SortedSet<CompetitionMatch> getCompetitionMatches(boolean open, boolean closed, Participant participant) {
        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
        for (CompetitionMatch competitionMatch : this.getCompetitionMatches())
            if (competitionMatch.isPairingDone() && (((closed && competitionMatch.isFinished()) || (open && !competitionMatch.isFinished())) && (participant == null || competitionMatch.isForParticipant(participant)))) {
                competitionMatches.add(competitionMatch);
                Sets.sort(competitionMatches);
            }
        return competitionMatches;
    }

    @Override
    public SortedSet<CompetitionMatch> getSubCompetitionObjectWithResults() {
        return getCompetitionMatches();
    }

    @Override
    public CompetitionGroup getUpperCompetitionObjectWithResult() {
        return this.getCompetitionGroup();
    }

    @Override
    public boolean isParticipantPairingDefined() {
        boolean isParticipantPairingDefined = false;
        if (this.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
            isParticipantPairingDefined = participantPairings != null && !participantPairings.isEmpty() && participantPairings.size() == getCompetitionMatches().size();
            if (isParticipantPairingDefined) {
//                int numberOfTeam = this.competitionGroup.competitionInstance.competitionCreationParam.playVersusType.numberOfTeam;
                for (ParticipantPairing participantPairing : participantPairings) {
                    if (participantPairing.getRealParticipantQuantity() == 0) {
                        isParticipantPairingDefined = false;
                        break;
                    }
                }
            }

        } else {
            isParticipantPairingDefined = participantPairings.size() == getCompetitionMatches().size();
            if (isParticipantPairingDefined) {
                CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
                if (competitionCreationParamPhase == null)
                    competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

                int numberOfTeam = competitionCreationParamPhase.numberOfParticipantMatch;
                boolean participantTypeSplittable = competitionCreationParamPhase.participantTypeSplittable;
                int uncompleteTeam = 0;
                int realParticipantQuantity = 0;
                for (ParticipantPairing participantPairing : participantPairings) {
                    realParticipantQuantity = participantPairing.getRealParticipantQuantity();
                    if (!participantTypeSplittable && participantPairing.competitionMatch != null && participantPairing.competitionMatch.participantQuantity != realParticipantQuantity) {
                        isParticipantPairingDefined = false;
                    }
                    if (realParticipantQuantity > 0 && (realParticipantQuantity != numberOfTeam && participantTypeSplittable)) {
                        uncompleteTeam++;
                        if (round > 1 && competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0)
                            isParticipantPairingDefined = false;
                    } else if (realParticipantQuantity == 0) {
                        isParticipantPairingDefined = false;
                    }
                }
            }
        }
        return isParticipantPairingDefined;
    }

    public int getNumberOfPlay() {
        int numberOfPlay = 0;
        if (competitionMatches != null && !competitionMatches.isEmpty())
            numberOfPlay = competitionMatches.first().competitionPlays.size();
        return numberOfPlay;
    }

    public void clearDatabaseId() {
        this.databaseId = null;
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }
        if (participantPairings != null)
            for (ParticipantPairing participantPairing : participantPairings) {
                participantPairing.clearDatabaseId();
            }

        if (competitionMatches != null)
            for (CompetitionMatch competitionMatch : competitionMatches) {
                competitionMatch.clearDatabaseId();
            }

    }

    @Override
    public CompetitionRound cloneSimplified() {
        CompetitionRound competitionRound = null;
        try {
            competitionRound = (CompetitionRound) this.clone();
//            competitionRound.competitionMatches = new TreeSet<>();
            if (this.participantResults != null) {
                List<ParticipantResult> participantResults = new ArrayList<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    participantResults.add(participantResult.cloneSimplified());
                }
                competitionRound.participantResults = new TreeSet<>();
                competitionRound.participantResults.addAll(participantResults);
            }
//            competitionRound.fillPairingCache();
//            competitionRound.fillResultCache();
        } catch (CloneNotSupportedException e) {
        }
        return competitionRound;
    }

    @Override
    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        if (!expectedGlobalDurationStatisticsStructureInitialized) {
            expectedGlobalDurationStatisticsStructureInitialized = true;
            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;
            Long sum = 0L;
            Long count = 0L;
            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                max += competitionMatch.getExpectedGlobalDuration().max;
                min += competitionMatch.getExpectedGlobalDuration().min;
                avg += competitionMatch.getExpectedGlobalDuration().avg;
                sum += competitionMatch.getExpectedGlobalDuration().avg;
                count++;
            }
            max = max / count;
            min = min / count;
            avg = avg / count;
            expectedGlobalDurationStatisticsStructure.min = min;
            expectedGlobalDurationStatisticsStructure.max = max;
            expectedGlobalDurationStatisticsStructure.avg = avg;
            expectedGlobalDurationStatisticsStructure.sum = sum;
            expectedGlobalDurationStatisticsStructure.count = count;
        }


        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayStatisticsStructureInitialized) {
            expectedGlobalPlayStatisticsStructureInitialized = true;
            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;
            Long sum = 0L;
            Long count = 0L;
            if (getCompetitionMatches() != null && !getCompetitionMatches().isEmpty()) {
                for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                    max += competitionMatch.getExpectedGlobalPlay().max;
                    min += competitionMatch.getExpectedGlobalPlay().min;
                    avg += competitionMatch.getExpectedGlobalPlay().avg;
                    sum += competitionMatch.getExpectedGlobalPlay().avg;
                    count++;
                }
                max = max / count;
                min = min / count;
                avg = avg / count;
            }
            expectedGlobalPlayStatisticsStructure.min = min;
            expectedGlobalPlayStatisticsStructure.max = max;
            expectedGlobalPlayStatisticsStructure.avg = avg;
            expectedGlobalPlayStatisticsStructure.sum = sum;
            expectedGlobalPlayStatisticsStructure.count = count;
        }
        return expectedGlobalPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {

        if (!expectedParticipantDurationStatisticsStructureInitialized) {
            expectedParticipantDurationStatisticsStructureInitialized = true;
            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;
            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                if (max == 0 || max.compareTo(competitionMatch.getExpectedParticipantDuration().max) < 0)
                    max = competitionMatch.getExpectedParticipantDuration().max;

                if (min == 0 || min.compareTo(competitionMatch.getExpectedParticipantDuration().min) > 0)
                    min = competitionMatch.getExpectedParticipantDuration().min;
                if (avg == 0 || avg.compareTo(competitionMatch.getExpectedParticipantDuration().avg) < 0)
                    avg = competitionMatch.getExpectedParticipantDuration().avg;
            }
            expectedParticipantDurationStatisticsStructure.min = min;
            expectedParticipantDurationStatisticsStructure.max = max;
            expectedParticipantDurationStatisticsStructure.avg = avg;
        }
        return expectedParticipantDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        if (!expectedParticipantPlayStatisticsStructureInitialized) {
            expectedParticipantPlayStatisticsStructureInitialized = true;
            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;
            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                if (max == 0 || max.compareTo(competitionMatch.getExpectedParticipantDuration().max) < 0)
                    max = competitionMatch.getExpectedParticipantDuration().max;

                if (min == 0 || min.compareTo(competitionMatch.getExpectedParticipantDuration().min) > 0)
                    min = competitionMatch.getExpectedParticipantDuration().min;
                if (avg == 0 || avg.compareTo(competitionMatch.getExpectedParticipantDuration().avg) < 0)
                    avg = competitionMatch.getExpectedParticipantDuration().avg;
            }
            expectedParticipantPlayStatisticsStructure.min = min;
            expectedParticipantPlayStatisticsStructure.max = max;
            expectedParticipantPlayStatisticsStructure.avg = avg;
        }
        return expectedParticipantPlayStatisticsStructure;

    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        if (!expectedGlobalStepStatisticsStructureInitialized) {
            expectedGlobalStepStatisticsStructureInitialized = true;
            CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = this.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
            int maximumNumberOfParallelPlay = competitionCreationParamPhase.maximumNumberOfParallelPlay;
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
                CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
                if (competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled && competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null) {
                    if (competitionCreationParamPhaseFinal.groupSizeFinalThreshold >= competitionMatches.size() * competitionCreationParamPhaseFinal.numberOfParticipantMatch) {
                        maximumNumberOfParallelPlay = competitionCreationParamPhaseFinal.numberOfParallelPlayFinalMaximum;
                    }
                }
            }
            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;
            if (maximumNumberOfParallelPlay > 0) {

                for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                    max += competitionMatch.getExpectedGlobalStep().max;
                    min += competitionMatch.getExpectedGlobalStep().min;
                    avg += competitionMatch.getExpectedGlobalStep().avg;

                }
                max = Math.round(Math.ceil((double) max / (double) maximumNumberOfParallelPlay));
                min = Math.round(Math.ceil((double) min / (double) maximumNumberOfParallelPlay));
                avg = Math.round(Math.ceil((double) avg / (double) maximumNumberOfParallelPlay));
            } else {
                if (!competitionMatches.isEmpty()) {
                    max = competitionMatches.first().getExpectedGlobalStep().max;
                    min = competitionMatches.first().getExpectedGlobalStep().min;
                    avg = competitionMatches.first().getExpectedGlobalStep().avg;
                }
            }
            expectedGlobalStepStatisticsStructure.min = min;
            expectedGlobalStepStatisticsStructure.max = max;
            expectedGlobalStepStatisticsStructure.avg = avg;
        }
        return expectedGlobalStepStatisticsStructure;
    }

    @Override
    protected Integer getRoundOrLane() {
        return this.round;
    }

    public void addCompetitionMatch(CompetitionMatch competitionMatch) {
        competitionMatch.competitionRound = this;
        competitionMatches.add(competitionMatch);
//        competitionMatchesCache = null;
    }

    @Override
    public void resetCache() {
        super.resetCache();
//        this.competitionGroupCache = null;
//        this.competitionGroupCache = null;
//        this.competitionMatchesCache = null;
//        this.competitionMatchesCache = null;
//        this.competitionRoundNextCache = null;
//        this.competitionRoundNextCache = null;
//        this.competitionRoundPreviousCache = null;
//        this.competitionRoundPreviousCache = null;
    }

//    @Override
//    public void fillCache(boolean up, boolean down) {
//        super.fillCache(up, down);
//        if (up)
//            if (this.getCompetitionRoundNext() != null)
//                this.getCompetitionRoundNext().fillCache(false, false);
//        if (down)
//            if (this.getCompetitionRoundPrevious() != null)
//                this.getCompetitionRoundPrevious().fillCache(false, false);
//    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionGroup != null ? competitionGroup.localId : null;
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionRound competitionRound = new CompetitionRound();
        competitionRound.id = this.id;
        competitionRound.localId = this.localId;
        competitionRound.databaseId = this.databaseId;
//        if (this.competitionRoundNext != null) {
//            competitionRound.competitionRoundNext = new CompetitionRound();
//            competitionRoundNext.id = this.competitionRoundNext.id;
//            competitionRoundNext.localId = this.competitionRoundNext.localId;
//            competitionRoundNext.databaseId = this.competitionRoundNext.databaseId;
//        } else {
        competitionRound.competitionRoundNext = null;
//        }
//        if (this.competitionRoundPrevious != null) {
//            competitionRound.competitionRoundPrevious = new CompetitionRound();
//            competitionRoundPrevious.id = this.competitionRoundPrevious.id;
//            competitionRoundPrevious.localId = this.competitionRoundPrevious.localId;
//            competitionRoundPrevious.databaseId = this.competitionRoundPrevious.databaseId;
//        } else {
        competitionRound.competitionRoundPrevious = null;
//        }
        Set<CompetitionRound> afterCompetitionRounds = null;
//        if (afterDependenciesCompetitionRounds != null && !afterDependenciesCompetitionRounds.isEmpty()) {
//            afterCompetitionRounds = new HashSet<>();
//            for (CompetitionRound competitionRoundAfter : afterDependenciesCompetitionRounds) {
//                CompetitionRound competitionRoundNew = new CompetitionRound();
//                competitionRoundNew.id = competitionRoundAfter.id;
//                afterCompetitionRounds.add(competitionRoundNew);
//            }
//            competitionRound.afterDependenciesCompetitionRounds.addAll(afterCompetitionRounds);
//        } else {
        competitionRound.afterDependenciesCompetitionRounds = null;
//        }
//        Set<CompetitionRound> beforeCompetitionRounds = null;
//        if (beforeDependenciesCompetitionRounds != null && !beforeDependenciesCompetitionRounds.isEmpty()) {
//            beforeCompetitionRounds = new HashSet<>();
//            for (CompetitionRound competitionRoundBefore : beforeDependenciesCompetitionRounds) {
//                CompetitionRound competitionRoundNew = new CompetitionRound();
//                competitionRoundNew.id = competitionRoundBefore.id;
//                beforeCompetitionRounds.add(competitionRoundNew);
//            }
//            competitionRound.beforeDependenciesCompetitionRounds.addAll(beforeCompetitionRounds);
//        } else {
        competitionRound.beforeDependenciesCompetitionRounds = null;
//        }
        return competitionRound;
    }

    @Override
    public void clearForContext() {
        participantResults = null;
        participantPairings = null;
        competitionMatches = null;
        competitionRoundNext = null;
        competitionRoundPrevious = null;
        participantResults = null;
        participantPairings = null;
        beforeDependenciesCompetitionRounds = null;
        afterDependenciesCompetitionRounds = null;
        String competitionGroupId = competitionGroup.id;
        competitionGroup = new CompetitionGroup();
        competitionGroup.id = competitionGroupId;
        String competitionSeedId = competitionSeed.id;
        competitionSeed = new CompetitionSeed();
        competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionPhase = new CompetitionPhase();
        competitionPhase.id = competitionPhaseId;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionInstance = new CompetitionInstance();
        competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;

    }

    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionRound competitionRound = (CompetitionRound) this.clone();
//        if (this.competitionRoundNext != null) {
//            competitionRound.competitionRoundNext = new CompetitionRound();
//            competitionRound.competitionRoundNext.id = this.competitionRoundNext.id;
//            competitionRound.competitionRoundNext.localId = this.competitionRoundNext.localId;
//            competitionRound.competitionRoundNext.databaseId = this.competitionRoundNext.databaseId;
//        } else {
        competitionRound.competitionRoundNext = null;
//        }
//        if (this.competitionRoundPrevious != null) {
//            competitionRound.competitionRoundPrevious = new CompetitionRound();
//            competitionRound.competitionRoundPrevious.id = this.competitionRoundPrevious.id;
//            competitionRound.competitionRoundPrevious.localId = this.competitionRoundPrevious.localId;
//            competitionRound.competitionRoundPrevious.databaseId = this.competitionRoundPrevious.databaseId;
//        } else {
        competitionRound.competitionRoundPrevious = null;
//        }
        String competitionGroupId = competitionGroup.id;
        competitionRound.competitionGroup = new CompetitionGroup();
        competitionRound.competitionGroup.id = competitionGroupId;
        String competitionSeedId = competitionSeed.id;
        competitionRound.competitionSeed = new CompetitionSeed();
        competitionRound.competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionRound.competitionPhase = new CompetitionPhase();
        competitionRound.competitionPhase.id = competitionPhaseId;
        String competitionInstanceId = competitionInstance.id;

        Version version = competitionInstance.version;
        competitionRound.competitionInstance = new CompetitionInstance();
        competitionRound.competitionInstance.id = competitionInstanceId;
        competitionRound.competitionInstance.version = version;

        competitionRound.competitionMatches = null;

        competitionRound.beforeDependenciesCompetitionRounds = null;
//        if (this.beforeDependenciesCompetitionRounds != null && !this.beforeDependenciesCompetitionRounds.isEmpty()) {
//            competitionRound.beforeDependenciesCompetitionRounds = new TreeSet<>();
//            SortedSet<CompetitionRound> competitionRoundsNext = new TreeSet<>();
//            for (CompetitionRound competitionRoundNext : this.beforeDependenciesCompetitionRounds) {
//                CompetitionRound competitionRoundNextForUpdateEvent = new CompetitionRound();
//                competitionRoundNextForUpdateEvent.id = competitionRoundNext.id;
//                competitionRoundNextForUpdateEvent.localId = competitionRoundNext.localId;
//                competitionRoundNextForUpdateEvent.databaseId = competitionRoundNext.databaseId;
//                competitionRoundsNext.add(competitionRoundNextForUpdateEvent);
//            }
//            competitionRound.beforeDependenciesCompetitionRounds.addAll(competitionRoundsNext);
//        } else {
//            competitionRound.beforeDependenciesCompetitionRounds = null;
//        }

        competitionRound.afterDependenciesCompetitionRounds = null;
//        if (this.afterDependenciesCompetitionRounds != null && !this.afterDependenciesCompetitionRounds.isEmpty()) {
//            competitionRound.afterDependenciesCompetitionRounds = new TreeSet<>();
//            SortedSet<CompetitionRound> competitionRoundsNext = new TreeSet<>();
//            for (CompetitionRound competitionRoundNext : this.afterDependenciesCompetitionRounds) {
//                CompetitionRound competitionRoundNextForUpdateEvent = new CompetitionRound();
//                competitionRoundNextForUpdateEvent.id = competitionRoundNext.id;
//                competitionRoundNextForUpdateEvent.localId = competitionRoundNext.localId;
//                competitionRoundNextForUpdateEvent.databaseId = competitionRoundNext.databaseId;
//                competitionRoundsNext.add(competitionRoundNextForUpdateEvent);
//            }
//            competitionRound.afterDependenciesCompetitionRounds.addAll(competitionRoundsNext);
//        } else {
//            competitionRound.afterDependenciesCompetitionRounds = null;
//        }

        if (this.getCompetitionMatches() != null && !this.getCompetitionMatches().isEmpty()) {
            competitionRound.competitionMatches = new TreeSet<>();
            List<CompetitionMatch> competitionMatches = new ArrayList<>();
            for (CompetitionMatch competitionMatch :
                    this.getCompetitionMatches()) {
                competitionMatches.add((CompetitionMatch) competitionMatch.cloneForContext());
            }
            competitionRound.competitionMatches.addAll(competitionMatches);
        }
        return competitionRound;
    }

    public boolean isInvalid() {
        boolean invalid = false;
        if (isParticipantPairingDefined()) {
            ParticipantPairing[] participantPairingsArray = new ParticipantPairing[participantPairings.size()];
            participantPairingsArray = participantPairings.toArray(participantPairingsArray);
            for (int i = 0; i < participantPairingsArray.length; i++) {
                for (Participant participant_i : participantPairingsArray[i].getRealParticipantsAsArray()) {
                    for (int j = i + 1; j < participantPairingsArray.length; j++) {
                        for (Participant participant_j : participantPairingsArray[j].getRealParticipantsAsArray()) {
                            if (participant_i.compareTo(participant_j) == 0) {
                                invalid = true;
                                break;
                            }
                        }
                    }
                }
                if (invalid)
                    break;
            }
        }
        return invalid;
    }

    public void prepareForRelaunch() {
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && this.competitionGroup.lane > 1) {

            CompetitionCreationParamPhase competitionCreationParamPhase = competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;

            List<Participant> participantList = new ArrayList<>();
            for (ParticipantPairing participantPairing :
                    this.participantPairings) {
                participantList.addAll(participantPairing.getRealParticipantsAsArray());
            }
            if (competitionRoundPrevious != null) {
                int participantQualified = competitionRoundPrevious.getCompetitionMatches().size() * competitionCreationParamPhase.participantQualifiedPerMatch;
                List<Participant> participantsQualified = Arrays.asList(competitionRoundPrevious.participantResults.toArray(new Participant[competitionRoundPrevious.participantResults.size()]));
                while (participantsQualified.size() > participantQualified) {
                    participantsQualified.remove(participantsQualified.size() - 1);
                }
                for (Participant participant : participantsQualified) {
                    for (Participant participantElt : participantList) {
                        if (participantElt.compareTo(participant) == 0) {
                            participantList.remove(participantElt);
                            break;
                        }
                    }
                }
            }
            if (!participantList.isEmpty()) {
                for (int i = 0; i < participantList.size(); i++) {
                    for (int j = i + 1; j < participantList.size(); j++) {
                        if (participantList.get(i).compareTo(participantList.get(j)) == 0) {
                            participantList.remove(j);
                            break;
                        }
                    }
                }

                ParticipantQueueElement participantQueueElement = new ParticipantQueueElement();
                participantQueueElement.participants = participantList;
                competitionGroup.participantQueue.participantQueueElements.add(0, participantQueueElement);

            }

            for (int i = 0; i < competitionGroup.participantQueue.participantQueueElements.size(); i++) {
                for (int j = i + 1; j < competitionGroup.participantQueue.participantQueueElements.size(); j++) {
                    ParticipantQueueElement participantQueueElement_i = competitionGroup.participantQueue.participantQueueElements.get(i);
                    ParticipantQueueElement participantQueueElement_j = competitionGroup.participantQueue.participantQueueElements.get(j);
                    for (Participant participant_i : participantQueueElement_i.participants) {
                        boolean found = false;
                        for (Participant participant_j : participantQueueElement_j.participants) {
                            if (participant_i.compareTo(participant_j) == 0) {
                                found = true;
                                break;

                            }
                        }
                        if (found) {
                            competitionGroup.participantQueue.participantQueueElements.remove(j);
                            break;
                        }
                    }
                }
            }
            if (round == 1) {
                competitionGroup.participantPairings.clear();
                competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
            }
            this.reset(true);
            this.resetNextRounds();


        } else {
            if (round == 1) {
                competitionGroup.participantPairings.clear();
                competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
            }
            this.reset(true);
            this.resetNextRounds();

        }
    }

    private void resetNextRounds() {
        if (this.afterDependenciesCompetitionRounds != null && !this.afterDependenciesCompetitionRounds.isEmpty()) {
            for (CompetitionRound afterDependenciesCompetitionRound : this.afterDependenciesCompetitionRounds) {
                afterDependenciesCompetitionRound.reset(true);
                afterDependenciesCompetitionRound.resetNextRounds();
            }
        } else {
            if (this.competitionRoundNext != null) {
                this.competitionRoundNext.reset(true);
                this.competitionRoundNext.resetNextRounds();

            }
        }
    }

    public boolean isLatest() {
        return this.round == this.getCompetitionGroup().competitionRounds.size();
    }

    public boolean isFirst() {
        return round == 1;

    }


    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
        if (competitionMatches != null) {
            for (CompetitionMatch competitionMatch : competitionMatches) {
                Map<Participant, List<Participant>> participantOpponentsPlayMap = competitionMatch.getParticipantOpponentsMap();
                for (Participant participant : participantOpponentsPlayMap.keySet()) {
                    if (!participantOpponentsMap.containsKey(participant))
                        participantOpponentsMap.put(participant, new ArrayList<>());
                    participantOpponentsMap.get(participant).addAll(participantOpponentsMap.get(participant));
                }
            }
        }
        return participantOpponentsMap;
    }


    public Set<CompetitionMatch> getCompetitionMatchesWithParticipants(List<Participant> participants) {
        Set<CompetitionMatch> competitionMatches = new HashSet<>();
        if (getCompetitionMatches() != null) {
            for (CompetitionMatch competitionMatch : getCompetitionMatches()) {
                if (competitionMatch.isWithParticipants(participants))
                    competitionMatches.add(competitionMatch);
            }
        }
        return competitionMatches;
    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (this.beforeDependenciesCompetitionRounds != null && !this.beforeDependenciesCompetitionRounds.isEmpty()) {
            for (CompetitionRound competitionRound : this.beforeDependenciesCompetitionRounds) {
                if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionRound.expectedRelativeEndTime) < 0)
                    expectedRelativeStartTime = competitionRound.expectedRelativeEndTime;
            }
        } else {
            if (this.competitionRoundPrevious != null)
                expectedRelativeStartTime = this.competitionRoundPrevious.expectedRelativeEndTime;
            else
                expectedRelativeStartTime = this.competitionGroup.expectedRelativeStartTime;
        }
        SortedMap<Integer, SortedSet<CompetitionMatch>> byeLevelCompetitionMatches = new TreeMap<>();
        for (CompetitionMatch competitionMatch : this.competitionMatches) {
            Integer byeLevel = 0;
            if (competitionMatch.previousCompetitionMatchLinks == null || competitionMatch.previousCompetitionMatchLinks.isEmpty()) {

            } else {

                for (CompetitionMatchLink competitionMatchLinkPrevious :
                        competitionMatch.previousCompetitionMatchLinks) {
                    CompetitionMatch competitionMatchPrevious = competitionMatchLinkPrevious.previousCompetitionMatch;
                    if (competitionMatchPrevious.isBye()) {
                        byeLevel++;
                    }
                }

            }
            if (!byeLevelCompetitionMatches.containsKey(byeLevel)) {
                byeLevelCompetitionMatches.put(byeLevel, new TreeSet<>());
            }
            byeLevelCompetitionMatches.get(byeLevel).add(competitionMatch);
        }
        for (Integer byeLevel : byeLevelCompetitionMatches.keySet()) {
            for (CompetitionMatch competitionMatch : byeLevelCompetitionMatches.get(byeLevel)) {
                competitionMatch.fillExpectedRelativeTime();
                if (expectedRelativeEndTime == null || expectedRelativeEndTime.duration.compareTo(competitionMatch.expectedRelativeEndTime.duration) < 0) {
                    expectedRelativeEndTime = new Duration(competitionMatch.expectedRelativeEndTime.duration);
                }
                if (expectedRelativeStartTime == null || expectedRelativeStartTime.duration.compareTo(competitionMatch.expectedRelativeStartTime.duration) > 0) {
                    expectedRelativeStartTime = new Duration(competitionMatch.expectedRelativeStartTime.duration);
                }
                expectedDuration = expectedRelativeEndTime.minus(expectedRelativeStartTime);
            }
        }


    }


    public SortedSet<CompetitionMatch> getCompetitionMatchForPhaseSequence(Integer phaseSequence) {
        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
        if (this.competitionMatches != null && this.phaseSequence.compareTo(phaseSequence) == 0) {
            competitionMatches.addAll(this.competitionMatches);
        }
        return competitionMatches;
    }

    public SortedSet<CompetitionRound> getCompetitionRoundsForDate(Duration relativeDuration) {
        SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
        if (this.expectedRelativeStartTime != null && this.expectedRelativeEndTime != null && this.expectedRelativeEndTime.compareTo(relativeDuration) > 0 && this.expectedRelativeStartTime.compareTo(relativeDuration) <= 0)
            competitionRounds.add(this);

        return competitionRounds;
    }

    @Override
    public void reset(boolean recursive) {
        super.reset(recursive);
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            if (round > 1) {
                this.competitionInstance.competitionRounds.remove(this);
                this.competitionGroup.competitionRounds.remove(this);
                this.competitionInstance = null;
                this.competitionPhase = null;
                this.competitionSeed = null;
                this.competitionGroup = null;
            }
            this.beforeDependenciesCompetitionRounds.clear();
            this.afterDependenciesCompetitionRounds.clear();
            this.competitionRoundPrevious = null;
            this.competitionRoundNext = null;
        }
    }

    public CompetitionMatch createCompetitionMatchSimulationForLadder() {
        CompetitionMatch competitionMatch = null;
        if (this.competitionGroup.participantSeats.size() > competitionPhase.competitionCreationParamPhase.participantQualifiedPerMatch || this.competitionGroup.participantSeats.size() >= competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch) {
            List<ParticipantSeat> participantSeats = new ArrayList<>(this.competitionGroup.participantSeats);
            Collections.sort(participantSeats, new Comparator<ParticipantSeat>() {
                @Override
                public int compare(ParticipantSeat o1, ParticipantSeat o2) {
                    return o1.participant.compareTo(o2.participant);
                }
            });
            ParticipantSeat participantSeatFirst = participantSeats.get(0);
            participantSeats.remove(0);
            for (int i = 1; i < this.round; i++) {
                ParticipantSeat participantSeatSecond = participantSeats.get(0);
                participantSeats.remove(0);
                participantSeats.add(participantSeatSecond);
            }
            participantSeats.add(0, participantSeatFirst);
            int startIndex = this.competitionMatches.size() * competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch;
            if (this.competitionPhase.competitionCreationParamPhase.registrationOnTheFly != null && this.competitionPhase.competitionCreationParamPhase.registrationOnTheFly) {
                startIndex = (this.competitionMatches.size() + this.competitionGroup.competitionRounds.size() - 1) * competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch;
                if (startIndex < 0)
                    startIndex = 0;
            }
            List<Participant> participantList = new ArrayList<>();
            for (int i = 0; i < competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch; i++) {
                participantList.add(participantSeats.get((startIndex + i) % participantSeats.size()).participant);
            }

            competitionMatch = this.addCompetitionMatchForLadder(participantList);

        }
        return competitionMatch;
    }

    public CompetitionMatch addCompetitionMatchForLadder(List<Participant> participantList) {
        return addCompetitionMatchForLadder(participantList, null);
    }

    public CompetitionMatch addCompetitionMatchForLadder(List<Participant> participantList, CompetitionObserver competitionObserver) {
        CompetitionMatch competitionMatch = null;
        boolean participantAlreadyPlayed = false;
        for (Participant participant : participantList) {
            participantAlreadyPlayed = this.isForParticipant(participant);
            if (participantAlreadyPlayed)
                break;
        }
        if (!participantAlreadyPlayed) {
            competitionMatch = competitionInstance.createCompetitionMatch(this, competitionObserver);
            competitionMatch.matchType = MatchType.NORMAL;
            addCompetitionMatch(competitionMatch, this);
            CompetitionPlay competitionPlay = null;
            for (int k = 0; k < this.competitionPhase.competitionCreationParamPhase.numberOfPlayMinimum; k++) {
                IdGenerator idGenerator = this.getIdGenerator();
                if (idGenerator == null && this.competitionInstance != null)
                    idGenerator = this.competitionInstance.getIdGenerator();
                competitionPlay = competitionInstance.createCompetitionPlay(competitionMatch, competitionObserver);
                competitionPlay.playVersusType = this.competitionPhase.competitionCreationParamPhase.playVersusType;
                competitionPlay.round = k + 1;
                addCompetitionPlay(competitionPlay, competitionMatch);

            }
            competitionMatch.addParticipantsForLadder(participantList);
        }
        setChanged();
        notifyObservers(this.localId);
        return competitionMatch;
    }

    @Override
    public void delete() {
        super.delete();
        this.competitionInstance.competitionRounds.remove(this);
        this.competitionGroup.competitionRounds.remove(this);
    }

    public void reopen() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
    }
}

