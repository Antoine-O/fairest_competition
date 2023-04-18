package com.qc.competition.service.structure;


import com.fasterxml.jackson.annotation.*;
import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.db.entity.competition.MatchType;
import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.tree.CompetitionGroupTree;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.service.template.automatic.participation.optimization.PhaseType;
import com.qc.competition.utils.Sets;
import com.qc.competition.ws.simplestructure.Duration;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionGroup extends CompetitionObjectWithResult<CompetitionSeed, CompetitionRound> implements StatisticsElement, Simplify<CompetitionGroup> {
    public static String CLASS = CompetitionGroup.class.getSimpleName();
    @XmlAttribute(name = "format")
    @JsonProperty("format")
    public CompetitionGroupFormat competitionGroupFormat;
    @XmlAttribute(name = "groupResultId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("group_result_id")
    public CompetitionGroupResult competitionGroupResult;
    //    @XmlElementWrapper(name = "roundIds")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "roundIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("roundIds")
    public SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
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
    @XmlAttribute(name = "round")
    public int lane;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlElementWrapper(name = "pairings")
    @XmlElement(name = "pairing")
    @JsonProperty("pairings")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
    @XmlElement(name = "participantQueue")
    public ParticipantQueue participantQueue;
    @XmlTransient
    @JsonIgnore
    public Integer minNumberOfPlay = null;
    @XmlTransient
    @JsonIgnore
    public Integer maxNumberOfPlay = null;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "expectedParticipantQuantity")
    public int expectedParticipantQuantity;
    @XmlElementWrapper(name = "seats")
    @XmlElement(name = "seat")
    @JsonProperty("seats")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();
//    @XmlTransient
//    @JsonProperty("competitionRoundsCache")
//    protected SortedSet<CompetitionRound> competitionRoundsCache = null;
//
//    @XmlTransient
//    @JsonProperty("competitionGroupResultCache")
//    protected CompetitionGroupResult competitionGroupResultCache;
//    //    @XmlAttribute
////    public int stepDetails;
//    @XmlTransient
//    @JsonProperty("competitionSeedCache")
//    protected CompetitionSeed competitionSeedCache;

    public CompetitionGroup() {
        super();
    }

    private CompetitionGroup(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static CompetitionGroup createInstance(IdGenerator idGenerator) {
        return new CompetitionGroup(idGenerator);
    }

    /*
        public void createCompetitionGroupRounds(CompetitionInstance competitionInstance, int competitionRoundQuantity, int numberOfPlayMinimum) {
            CompetitionGroupRound competitionRoundPrevious = null;
            CompetitionGroupRound competitionRound = null;

            for (int i = 0; i < competitionRoundQuantity; i++) {
                competitionRound = new CompetitionGroupRound();
                if (competitionRoundPrevious != null) {
                    competitionRound.competitionRoundPrevious = competitionRoundPrevious;
                    competitionRoundPrevious.competitionRoundNext = competitionRound;
                }
                competitionRound.roundDetails = i + 1;
                competitionRound.competitionGroup = this;

                competitionRound.initializeRound();
                Set<ParticipantPairing> participantPairings = competitionRound.participantPairings;
                int j = 0;
                for (ParticipantPairing participantPairing : participantPairings) {
                    CompetitionMatch competitionMatch = new CompetitionMatch();
                    competitionMatch.competitionRound = competitionRound;
                    competitionMatch.round = j + 1;
                    competitionMatch.participantPairing = participantPairing;
                    CompetitionPlay competitionPlay = null;
                    for (int k = 0; k < numberOfPlayMinimum; k++) {
                        competitionPlay = new CompetitionPlay();
                        competitionPlay.roundDetails = k + 1;
                        competitionPlay.competitionMatch = competitionMatch;
                        competitionMatch.competitionPlays.add(competitionPlay);
                    }
                    competitionRound.competitionMatchs.add(competitionMatch);
                    j++;
                }
                competitionRound.fillParticipantResultWithFakeValue();
                competitionRoundPrevious = competitionRound;
                this.competitionRounds.add(competitionRound);
            }
            this.competitionInstance = competitionInstance;

        }
    */


    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        if (this.competitionGroupResult == null) {
            this.competitionGroupResult = competitionInstance.createCompetitionGroupResult(this);
//            competitionInstance.addCompetitionGroupResult(competitionGroupResult, this);
        }
        this.getCompetitionGroupResult().addParticipantResult(participantResult);
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
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) != 0) {
            if (getCompetitionRounds() != null) {
                for (CompetitionRound competitionRound : getCompetitionRounds()) {
                    competitionRound.clear();
                }
            }
            this.participantPairings.clear();
            this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);

            if (this.getCompetitionGroupResult() != null) {
                getCompetitionGroupResult().clear();
            }
        }

    }

//    public void fillParticipantResultWithFakeValue() {
////        boolean fillCompetitionGroupResultFromCompetitionGroupRounds = true;
//        for (CompetitionRound competitionRound : competitionRounds) {
//            if (competitionRound.participantPairings.isEmpty()) {
//                competitionRound.initializeRound();
//            }
//            competitionRound.fillParticipantResultWithFakeValue();
//            competitionRound.fillCompetitionRoundResultFromCompetitionMatch();
//        }
//        if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
//            SortedSet<CompetitionGroup> competitionGroupSortedSet = new TreeSet<CompetitionGroup>();
//            competitionGroupSortedSet.addAll(competitionSeed.competitionGroupsCache);
//            competitionGroupSortedSet = competitionGroupSortedSet.tailSet(this);
//            competitionGroupSortedSet.remove(this);
//            if (!competitionGroupSortedSet.isEmpty()) {
//                competitionGroupSortedSet.first().fillParticipantResultWithFakeValue();
//            }
//        }
//        competitionGroupFormat.fillCompetitionGroupResult(this);
////        if (fillCompetitionGroupResultFromCompetitionGroupRounds) {
////            fillCompetitionGroupResultFromCompetitionGroupRounds();
////        }
//    }

    public int compareTo(CompetitionGroup o) {
        int compareValue = super.compareTo(o);
        return compareValue;
    }

    @Override
    public int compareTo(CompetitionObjectWithResult competitionObjectWithResult) {
        int compare = this.getClass().getName().compareTo(competitionObjectWithResult.getClass().getName());
        if (compare == 0)
            compare = compareTo(this.getClass().cast(competitionObjectWithResult));
        return compare;
    }

    public void createCompetitionGroupRounds(int competitionRoundQuantity, int competitionMatchQuantity, int numberOfPlay, PlayVersusType playVersusType) {
        CompetitionRound competitionRoundPrevious = null;
        CompetitionRound competitionRound = null;


        for (int i = 0; i < competitionRoundQuantity; i++) {
            competitionRound = competitionInstance.createCompetitionRound(this, competitionRoundPrevious);
//            competitionRound.round = i + 1;
//            addCompetitionRound(competitionRound, this, competitionRoundPrevious);

            for (int j = 0; j < competitionMatchQuantity; j++) {

                CompetitionMatch competitionMatch = competitionInstance.createCompetitionMatch(competitionRound);

                competitionMatch.lane = j + 1;
                addCompetitionMatch(competitionMatch, competitionRound);
                CompetitionPlay competitionPlay = null;
                for (int k = 0; k < numberOfPlay; k++) {
                    IdGenerator idGenerator = this.getIdGenerator();
                    if (idGenerator == null && this.competitionInstance != null)
                        idGenerator = this.competitionInstance.getIdGenerator();
                    competitionPlay = competitionInstance.createCompetitionPlay(competitionMatch);
                    competitionPlay.playVersusType = playVersusType;
                    competitionPlay.round = k + 1;
                    addCompetitionPlay(competitionPlay, competitionMatch);
                }
            }
//            if(competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION)==0){
//                competitionRound.fillCompetitionMatchLink();
//            }

            competitionRoundPrevious = competitionRound;
        }
    }


//    public int getExpectedRoundSize() {
//        int expectedRoundSize = 0;
//        switch (competitionGroupFormat) {
//            case CUSTOM:
//                expectedRoundSize = competitionRounds.size();
//                break;
//            case NONE:
//                break;
//            default:
//
//                PlayVersusType playVersusType = this.competitionInstance.competitionCreationParam.playVersusType;
//                int numberOfParticipant = getParticipantsSize();
//                int participantQualifiedPerMatch = this.competitionInstance.competitionCreationParam.participantQualifiedPerMatch;
//                int competitionGroupQuantity = this.competitionSeed.competitionGroupsCache.size();
//                int round = this.round;
//
//                int numberOfParticipantOut = 1;
//                if (competitionGroupResult.competitionSeedNext != null)
//                    numberOfParticipantOut = this.competitionGroupResult.competitionSeedNext.participantFilteringMethod.getFilteredParticipantSize(this.getParticipantsSize(), this.competitionGroupResult.competitionSeedNext.filteringValue, this.competitionGroupResult.competitionSeedNext.filteringUnit);
//
//
//                expectedRoundSize = competitionGroupFormat.expectedCompetitionRoundQuantity(playVersusType, numberOfParticipant, numberOfParticipantOut, participantQualifiedPerMatch, competitionGroupQuantity, round);
//                break;
//        }
//        return expectedRoundSize;
//    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        ParticipantScoreGroup participantScoreGroup = new ParticipantScoreGroup(this);
        participantScoreGroup.setParticipantScoreValue(ParticipantScoreGroup.ACTIVE_GROUP, getLocalId());
        return participantScoreGroup;
    }

//    @Override
//    public SortedSet<CompetitionPlay> getCompetitionPlays(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionPlay> competitionPlays = super.getCompetitionPlays(initialized, open, closed, participantId, recursive);
//        if (recursive && this.competitionGroupResult != null && this.getCompetitionGroupResult().competitionSeedNext != null)
//            competitionPlays.addAll(this.getCompetitionGroupResult().getCompetitionSeedNext().getCompetitionPlays(initialized, open, closed, participantId, recursive));
//        return competitionPlays;
//    }
//
//    @Override
//    public SortedSet<CompetitionMatch> getCompetitionMatches(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionMatch> competitionMatches = super.getCompetitionMatches(initialized, open, closed, participantId, recursive);
//        if (recursive && this.competitionGroupResult != null && this.getCompetitionGroupResult().competitionSeedNext != null)
//            competitionMatches.addAll(this.getCompetitionGroupResult().getCompetitionSeedNext().getCompetitionMatches(initialized, open, closed, participantId, recursive));
//        return competitionMatches;
//    }

    public void fillCompetitionMatchLink() {
        if (getCompetitionRounds() != null && (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0)) {
//            Sets.sort(competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionRound.fillCompetitionMatchLink();
            }
        }

    }

    public void fillCompetitionRoundLink() {
//        Sets.sort(competitionRounds);
        for (CompetitionRound competitionRound : getCompetitionRounds()) {
            competitionRound.fillCompetitionRoundLink();
        }

    }

    public void fillCompetitionRoundsSequence() {
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && lane == 1) {
            SortedMap<Integer, Set<CompetitionRound>> competitionRoundsOrderedByDependency = new TreeMap<>();
            SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
            SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
//            competitionGroups  =Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionRounds.addAll(competitionGroup.getCompetitionRounds());
            }
            int depth = 1;
            competitionRounds = Sets.sort(competitionRounds);
            fillCompetitionRoundsSequence(competitionRounds, competitionRoundsOrderedByDependency, depth);
            for (Integer seedSequence : competitionRoundsOrderedByDependency.keySet()) {
                for (CompetitionRound competitionRound : competitionRoundsOrderedByDependency.get(seedSequence)) {
                    competitionRound.seedSequence = seedSequence;
                }
            }
//            Integer phaseSequence = -1;
//            for (CompetitionRound competitionRound : competitionRounds) {
//                if (competitionRound.afterDependenciesCompetitionRounds == null || competitionRound.afterDependenciesCompetitionRounds.isEmpty()) {
//                    if (!competitionRoundsOrderedByDependency.containsKey(phaseSequence)) {
//                        competitionRoundsOrderedByDependency.put(phaseSequence, new HashSet<>());
//                    }
//                    competitionRoundsOrderedByDependency.get(phaseSequence).add(competitionRound);
//                }
//            }
//            competitionRounds.removeAll(competitionRoundsOrderedByDependency.get(phaseSequence));
//            while (!competitionRounds.isEmpty()) {
//                phaseSequence--;
//                for (CompetitionRound competitionRound : competitionRounds) {
//                    boolean afterDependenciesFound = false;
//                    for (CompetitionRound competitionRoundAfterDependency : competitionRound.afterDependenciesCompetitionRounds) {
//                        for (CompetitionRound competitionRoundCurrent : competitionRounds) {
//                            if (competitionRoundCurrent.id.compareToIgnoreCase(competitionRoundAfterDependency.id) == 0) {
//                                afterDependenciesFound = true;
//                                break;
//                            }
//                        }
//                        if (afterDependenciesFound)
//                            break;
//                    }
//                    if (!afterDependenciesFound) {
//                        if (!competitionRoundsOrderedByDependency.containsKey(phaseSequence)) {
//                            competitionRoundsOrderedByDependency.put(phaseSequence, new HashSet<>());
//                        }
//                        competitionRoundsOrderedByDependency.get(phaseSequence).add(competitionRound);
//                    }
//                }
//                competitionRounds.removeAll(competitionRoundsOrderedByDependency.get(phaseSequence));
//            }
//            int slide = -phaseSequence + 1;
//            for (Integer key : competitionRoundsOrderedByDependency.keySet()) {
//                for (CompetitionRound competitionRound : competitionRoundsOrderedByDependency.get(key)) {
//                    competitionRound.seedSequence = key + slide;
//                }
//            }
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
            for (CompetitionRound competitionRound : getCompetitionRounds()) {
//                if (competitionRound.seedSequence == null || competitionRound.seedSequence == 0)
                competitionRound.seedSequence = competitionRound.round;
            }
        }

        int previousSeedSequenceMax = 0;
        int previousPhaseSequenceMax = 0;
        int previousCompetitionSequenceMax = 0;
        for (CompetitionGroupResult competitionGroupResult : getCompetitionSeed().getCompetitionGroupResultsPrevious()) {
            CompetitionGroup competitionGroup = competitionGroupResult.getCompetitionGroup();
            if (!competitionGroup.getCompetitionRounds().isEmpty()) {
                CompetitionRound competitionRoundLast = competitionGroup.getCompetitionRounds().last();
                if (competitionGroup.getCompetitionSeed().competitionPhase.compareTo(this.competitionSeed.competitionPhase) == 0) {
                    if (!competitionGroup.getCompetitionRounds().isEmpty()) {
                        if (competitionRoundLast.seedSequence.compareTo(previousSeedSequenceMax) > 0)
                            previousSeedSequenceMax = competitionRoundLast.seedSequence;
                        if (competitionRoundLast.phaseSequence.compareTo(previousPhaseSequenceMax) > 0)
                            previousPhaseSequenceMax = competitionRoundLast.phaseSequence;
                    }
                }
                if (competitionRoundLast.competitionSequence.compareTo(previousCompetitionSequenceMax) > 0)
                    previousCompetitionSequenceMax = competitionRoundLast.competitionSequence;
            }
        }

        for (CompetitionRound competitionRound : getCompetitionRounds()) {
            competitionRound.phaseSequence = competitionRound.seedSequence + previousPhaseSequenceMax;
            competitionRound.competitionSequence = competitionRound.seedSequence + previousCompetitionSequenceMax;
            competitionRound.phaseLane = competitionRound.competitionGroup.lane;
        }

    }

    private void fillCompetitionRoundsSequence(Set<CompetitionRound> competitionRounds, SortedMap<Integer, Set<CompetitionRound>> competitionRoundsOrderedByDependency, int depth) {
        if (!competitionRounds.isEmpty()) {
            if (competitionRounds.size() == 1) {
                Set<CompetitionRound> competitionRoundsCurrentDepth = new HashSet<>();
                competitionRoundsCurrentDepth.add(competitionRounds.iterator().next());
                competitionRoundsOrderedByDependency.put(depth, competitionRoundsCurrentDepth);
            } else {
                Set<CompetitionRound> competitionRoundsCurrentDepth = new HashSet<>();
                for (CompetitionRound competitionRound : competitionRounds) {
                    if (depth == 1 && competitionRound.beforeDependenciesCompetitionRounds.isEmpty()) {
                        competitionRoundsCurrentDepth.add(competitionRound);
                    } else if (depth > 1) {
                        boolean allPreviousAlreadyTreated = false;
                        for (CompetitionRound competitionRoundBefore : competitionRound.beforeDependenciesCompetitionRounds) {
                            allPreviousAlreadyTreated = false;
                            for (Set<CompetitionRound> competitionRoundsAlreadyTreated : competitionRoundsOrderedByDependency.values()) {
                                for (CompetitionRound competitionRoundAlreadyTreated : competitionRoundsAlreadyTreated) {
                                    if (competitionRoundAlreadyTreated.id.compareTo(competitionRoundBefore.id) == 0) {
                                        allPreviousAlreadyTreated = true;
                                        break;
                                    }
                                }
                                if (allPreviousAlreadyTreated)
                                    break;
                            }
                            if (!allPreviousAlreadyTreated)
                                break;
                        }
                        if (allPreviousAlreadyTreated)
                            competitionRoundsCurrentDepth.add(competitionRound);
                    }
                }
                competitionRounds.removeAll(competitionRoundsCurrentDepth);
                competitionRoundsOrderedByDependency.put(depth, competitionRoundsCurrentDepth);
                fillCompetitionRoundsSequence(competitionRounds, competitionRoundsOrderedByDependency, depth + 1);
            }
        }
    }

    public void fillCompetitionGroupResultFromCompetitionGroupRounds() {
//        for (CompetitionRound competitionRound : competitionRounds)
//            competitionRound.fillCompetitionRoundResultFromCompetitionMatch();
        setChanged();
        notifyObservers(this.localId);
        if (this.competitionPhase.phaseType.compareTo(PhaseType.SEEDING) == 0) {

        } else {
            competitionGroupFormat.fillCompetitionGroupResult(this);
        }
    }

    public Collection<CompetitionPlay> getCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
        if (getCompetitionRounds() != null) {
//            Sets.sort(competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionPlays.addAll(competitionRound.getCompetitionPlays());
            }
        }
        return competitionPlays;
    }

//    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
//        return getCompetitionPlays(false, true, false, null, true);
//    }
//
//    public SortedSet<CompetitionPlay> getOpenCompetitionPlays(Integer participantId) {
//        return getCompetitionPlays(false, true, false, participantId, true);
//    }

    public CompetitionMatch getLastCompetitionMatchFor(Participant participant) {
//        Sets.sort(competitionRounds);
        Iterator<CompetitionRound> competitionRoundIteratorDescending = ((TreeSet<CompetitionRound>) getCompetitionRounds()).descendingIterator();
        CompetitionMatch lastCompetitionMatchForParticipant = null;
        while (competitionRoundIteratorDescending.hasNext()) {
            lastCompetitionMatchForParticipant = competitionRoundIteratorDescending.next().getCompetitionMatchFor(participant);
            if (lastCompetitionMatchForParticipant != null)
                break;
        }
        return lastCompetitionMatchForParticipant;
    }

    public Collection<CompetitionGroupResult> computeLastCompetitionGroupResults() {
        return getCompetitionGroupResult().computeLastCompetitionGroupResults();
    }

    public CompetitionRound getLastFinishedCompetitionGroupRound() {
        CompetitionRound competitionRoundLastFinished = null;
        if (getCompetitionRounds() != null) {
//            Sets.sort(competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                if (competitionRound.isFinished())
                    competitionRoundLastFinished = competitionRound;
            }
        }
        return competitionRoundLastFinished;
    }

    private int getParticipantsSize() {
        int participantSize = 0;
        for (ParticipantPairing participantPairing : participantPairings) {
            participantSize += participantPairing.participantSeats.size();
        }
        return participantSize;

    }

    public boolean initializeRound(int index) throws CompetitionInstanceGeneratorException {
        boolean roundInitialized = false;
        if (!competitionRounds.isEmpty() && competitionRounds.size() >= index) {
//            Sets.sort(competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                if (competitionRound.round == index) {
                    roundInitialized = competitionRound.initializeRound();
                    if (roundInitialized)
                        competitionRound.open();
                    break;
                }
            }
        }
        return roundInitialized;
    }

    private void initializeRounds() throws CompetitionInstanceGeneratorException {
//        if(competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN)==0){
//          competitionGroupFormat.createParticipantPairingForCompetitionGroup(this);
//        } else {
//        if (!competitionRounds.isEmpty())
//            Sets.sort(competitionRounds);
        if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            CompetitionRound competitionRound = getCompetitionRoundForRound(1);
            boolean roundInitialized = competitionRound.initializeRound();
            if (roundInitialized)
                competitionRound.open();
        } else {
            if (isParticipantPairingDefined() || (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && lane > 1 && this.getCompetitionSeed().getCompetitionGroupForLane(lane - 1).getCompetitionRounds().first().isClosed())) {
                if (!competitionRounds.isEmpty()) {
                    boolean roundInitialized;
                    for (int i = 0; i < competitionRounds.size(); i++) {
                        CompetitionRound competitionRound = getCompetitionRoundForRound(i + 1);
                        if (i < competitionRounds.size()) {
                            roundInitialized = competitionRound.initializeRound();
                            if (roundInitialized) {
                                competitionRound.open();
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
//                if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) {
                    for (int i = 0; i < competitionRounds.size(); i++) {
                        CompetitionRound competitionRound = getCompetitionRoundForRound(i + 1);
                        for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
                            if (competitionMatch.isOpen() || (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 && competitionMatch.isInitialized())) {
                                competitionMatch.checkBye();
                                if (competitionMatch.getMatchType() != null && competitionMatch.getMatchType().compareTo(MatchType.BYE) == 0) {
                                    competitionMatch.checkCompetitionCompetitionObjectStatus(true);
                                }
                            }
                        }
                    }
//                }
                } else {
//                if (this.competitionGroupResult != null && this.competitionGroupResult.competitionSeedNext != null) {
                    this.updateResultDependencies();
//                    competitionSeed.fillCompetitionSeedResultFromCompetitionGroups();
//                    this.competitionGroupResult.competitionSeedNext.doPairing();
//                this.competitionGroupResult.competitionSeedNext.initializeCompetitionGroups();
//                }
                }
            }
        }
//        }
    }

    public boolean isAllSubParticipantResultsSet() {
        boolean isAllSubParticipantResultsSet = isSubParticipantResultsSet();
        if (isAllSubParticipantResultsSet) {
            if (this.competitionGroupResult != null && this.getCompetitionGroupResult().competitionSeedNext != null && this.getCompetitionGroupResult().getCompetitionSeedNext().isCompetitionGroupResultsPreviousResultsSet()) {
//                if (!this.getCompetitionGroupResult().getCompetitionSeedNext().competitionObjectStatus.isCancelled())
                isAllSubParticipantResultsSet = this.getCompetitionGroupResult().getCompetitionSeedNext().isAllSubParticipantResultsSet();
            }
        }
        return isAllSubParticipantResultsSet;
    }

    @Override
    public boolean isParticipantResultSet(Participant participant) {
        boolean participantResultSet = false;
        if (this.competitionGroupResult != null)
            participantResultSet = this.getCompetitionGroupResult().isParticipantResultSet(participant);
        return participantResultSet;
    }

    @Override
    public boolean isParticipantResultsSet() {
        boolean participantResultSet = false;
        if (competitionGroupFormat == null || competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) == 0) {
            participantResultSet = !this.getCompetitionGroupResult().participantResults.isEmpty();
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0 || lane == 1) {
            for (Participant participant : this.getRealParticipantsAsArray()) {
                participantResultSet = isParticipantResultSet(participant);
                if (!participantResultSet)
                    break;
            }
        } else {
            participantResultSet = !this.getCompetitionGroupResult().participantResults.isEmpty();
        }
        return participantResultSet;
    }

    @Override
    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = true;
//        if (this.competitionRounds != null && !this.competitionRounds.isEmpty())
//            Sets.sort(competitionRounds);
        for (CompetitionRound competitionRound : competitionRounds) {
            participantResultSet = competitionRound.isParticipantResultsSet();
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

    public void reset(boolean recursive) {
        super.reset(recursive);
        if (this.participantQueue != null)
            this.participantQueue.reset();
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            if (this.participantSeats != null)
                this.participantSeats.clear();
            if (this.participantPairings != null)
                this.participantPairings.clear();
            if (this.competitionRounds != null) {

            }

        }
        if (recursive)
            if (this.competitionGroupResult != null) {
                this.getCompetitionGroupResult().reset(recursive);
            }
    }

//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        description.append("[participantPairings]").append(System.lineSeparator());
//        for (ParticipantPairing participantPairing : participantPairings) {
//            description.append(participantPairing.toString()).append(System.lineSeparator());
//        }
//        description.append("[competitionRounds]").append(System.lineSeparator());
//        if (getCompetitionRounds() != null) {
//            for (CompetitionRound competitionRound : this.getCompetitionRounds()) {
//                description.append(competitionRound.toDescription()).append(System.lineSeparator());
//            }
//        }
//        description.append("[competitionGroupResult]").append(System.lineSeparator()).append(getCompetitionGroupResult().toDescription()).append(System.lineSeparator());
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
//                description.append(indentation).append(participantPairing.toDescriptionTree(level + 1));
//            }
//        }
//        if (!competitionRounds.isEmpty()) {
//            description.append(indentation).append("[competitionRounds]").append(System.lineSeparator());
//            description.append(competitionRounds.first().toDescriptionTree(level + 1));
//        }
//        description.append(indentation).append("[competitionGroupResult]").append(System.lineSeparator()).append(getCompetitionGroupResult().toDescriptionTree(level + 1));
//        return description.toString();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//
//        element.setAttribute("round", "" + round);
////        element.setAttribute("stepDetails", "" + stepDetails);
//
//
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toDescriptionXml(document));
//        Element elementParticipantPairings = document.createElement(ParticipantPairing.class.getSimpleName() + "s");
//        if (!participantPairings.isEmpty()) {
//            for (ParticipantPairing participantPairing : participantPairings) {
//                elementParticipantPairings.appendChild(participantPairing.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantPairings);
//
//        Element elementCompetitionGroupRounds = document.createElement(CompetitionRound.class.getSimpleName() + "s");
//        if (!competitionRounds.isEmpty()) {
//            for (CompetitionRound competitionRound : getCompetitionRounds()) {
//                elementCompetitionGroupRounds.appendChild(competitionRound.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementCompetitionGroupRounds);
//
//        if (competitionGroupResult != null) {
//            element.appendChild(getCompetitionGroupResult().toDescriptionXml(document));
//        }
//        return element;
//    }
//
////    public CompetitionMatch getCompetitionMatch(int localId) {
////        CompetitionMatch competitionMatch = null;
////        for (CompetitionRound competitionRound : competitionRounds) {
////            competitionMatch = competitionRound.getCompetitionMatch(localId);
////            if (competitionMatch != null)
////                break;
////        }
////        return competitionMatch;
////    }
//
//    public Element toSimpleDescriptionXml(Document document, boolean withResult) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        if (withResult || !(competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0)) {
//            element.setAttribute("round", "" + round);
////            element.setAttribute("stepDetails", "" + stepDetails);
//            element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        }
//        element.setAttribute("numberOfRound", "" + competitionRounds.size());
//        element.setAttribute("competitionGroupFormat", "" + this.competitionGroupFormat);
////        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
////            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//        if (!competitionRounds.isEmpty()) {
//            if (!withResult && (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0)) {
//                element.setAttribute("numberOfMatch", "" + competitionRounds.first().competitionMatches.size());
//                element.setAttribute("numberOfPlay", "" + competitionRounds.first().competitionMatches.first().competitionPlays.size());
//            } else {
//                if (getCompetitionRounds() != null) {
//                    for (CompetitionRound competitionRound : getCompetitionRounds()) {
//                        element.appendChild(competitionRound.toSimpleDescriptionXml(document, withResult));
//                    }
//                }
//            }
//        }
//        if (withResult && !this.getCompetitionGroupResult().participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : this.getCompetitionGroupResult().participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        return element;
//    }

    @Override
    public String toString() {
        return "CompetitionGroup{" +
                "localId=" + localId +
                ",name='" + internationalizedLabel.defaultLabel + '\'' +
                ",status=" + competitionObjectStatus +
                ",competitionGroupFormat=" + competitionGroupFormat +
                ",round=" + lane +
                (participantPairings != null ? ", participantPairings.size()=" + participantPairings.size() : "") +
                (competitionSeed != null ? ", competitionSeed=" + competitionSeed : "") +
                '}';
    }

    @Override
    public void updateResultDependencies() throws CompetitionInstanceGeneratorException {
//        if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
//            CompetitionGroup competitionGroup = this.competitionSeed.getCompetitionGroupForLane(this.round + 1);
//            if (competitionGroup != null) {
//                int roundparallel = 0;
//                for (CompetitionRound competitionRound : competitionGroup.competitionRounds) {
//                    if (competitionRound.isParticipantResultsSet()) {
//                        roundparallel = competitionRound.roundDetails;
//                    }
//                }
//                if (roundparallel < competitionGroup.competitionRounds.size())
//                    competitionGroup.initializeRound(roundparallel + 1);
//                else {
//                    competitionGroup.updateResultDependencies();
//                }
//            }
//        }
        if (isSubParticipantResultsSet()) {
            fillCompetitionGroupResultFromCompetitionGroupRounds();
            setChanged();
            notifyObservers(this.localId);
            if (this.isSubClosed()) {
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                this.close();
            }
        } else if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0) {
            fillCompetitionGroupResultFromCompetitionGroupRounds();
            setChanged();
            notifyObservers(this.localId);
        }
    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        if (this.isOpen() && this.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {

            for (CompetitionRound competitionRound : competitionRounds) {
                competitionRound.close();
            }
            this.fillCompetitionGroupResultFromCompetitionGroupRounds();
            if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.CLOSED) != 0 && this.isSubClosed())
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
        }
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            super.close();
//            if (competitionGroupResult != null)
//                getCompetitionGroupResult().fillStatistics();
            this.getCompetitionSeed().updateResultDependencies();
//            if (this.competitionGroupResult != null && this.getCompetitionGroupResult().competitionSeedNext != null && this.getCompetitionGroupResult().getCompetitionSeedNext().isCompetitionGroupResultsPreviousResultsSet()) {
//                this.getCompetitionGroupResult().getCompetitionSeedNext().open();
//            }

        }
    }

    public ParticipantPairing findParticipantPairing(String localId) {
        for (ParticipantPairing participantPairing : participantPairings) {
            if (participantPairing.localId.compareTo(localId) == 0)
                return participantPairing;

        }
        ParticipantPairing participantPairing = null;
        if (getCompetitionRounds() != null) {
//            Sets.sort(competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                participantPairing = competitionRound.findParticipantPairing(localId);
                if (participantPairing != null)
                    break;
            }
        }
        return participantPairing;
    }

    public void initForXmlOutput() {
//        if (competitionGroupResult != null)
//            competitionGroupResult.initForXmlOutput();


//        for (ParticipantPairing participantPairing : participantPairings) {
//            participantPairing.initForXmlOutput();
//        }


//        for (CompetitionRound competitionRound : competitionRounds) {
//            competitionRound.competitionGroup = this;
//            competitionRound.initForXmlOutput();
//        }
    }

    public List<CompetitionSeed> getAllCompetitionSeeds() {
        List<CompetitionSeed> competitionSeeds = new ArrayList<>();
        if (competitionGroupResult != null && getCompetitionGroupResult().competitionSeedNext != null) {
            competitionSeeds.add(getCompetitionGroupResult().getCompetitionSeedNext());
            competitionSeeds.addAll(getCompetitionGroupResult().getCompetitionSeedNext().getAllCompetitionSeeds());
        }
        return competitionSeeds;
    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = super.findParticipantScore(localId);
        if (participantScore == null && competitionGroupResult != null) {
            participantScore = getCompetitionGroupResult().findParticipantScore(localId);
        }
        return participantScore;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    public void initFromXmlInputResult(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
        this.idGeneratorCache = competitionInstance.getIdGenerator();
//        for (CompetitionRound competitionRound : competitionRounds) {
//            competitionRound.initFromXmlInputResult(competitionInstance);
//        }
//        if (competitionGroupResult != null)
//            competitionGroupResult.initFromXmlInputResult(competitionInstance, true);

    }

    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            if (getCompetitionRounds() != null) {
//                Sets.sort(competitionRounds);
                for (CompetitionRound competitionRound : competitionRounds) {
                    overForParticipant = competitionRound.isOverForParticipant(participant);
                    if (!overForParticipant)
                        break;
                }
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

    @Override
    public CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return this.getCompetitionSeed();
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();

        for (CompetitionGroup competitionGroup : this.getCompetitionSeed().getCompetitionGroups()) {
            if (this.compareTo(competitionGroup) != 0)
                competitionObjectWithResults.add(competitionGroup);
        }
        return competitionObjectWithResults;
    }

    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (this.getCompetitionGroupResult() != null && this.getCompetitionGroupResult().getCompetitionSeedNext() != null) {
            competitionObjectWithResults.add(this.getCompetitionGroupResult().getCompetitionSeedNext());
            for (CompetitionGroup competitionGroup : this.getCompetitionGroupResult().getCompetitionSeedNext().getCompetitionGroups()) {
                if (this.compareTo(competitionGroup) != 0)
                    competitionObjectWithResults.add(competitionGroup);
            }
        }
        return competitionObjectWithResults;
    }

    public CompetitionGroupTree getCompetitionGroupTree() {
        CompetitionGroupTree competitionGroupTree = new CompetitionGroupTree();
        competitionGroupTree.internationalizedLabel = internationalizedLabel;
        competitionGroupTree.localId = getLocalId();
        competitionGroupTree.databaseId = databaseId;
        competitionGroupTree.lane = lane;
        competitionGroupTree.competitionGroupFormat = this.competitionGroupFormat;
        competitionGroupTree.over = isParticipantResultsSet();
        competitionGroupTree.filled = !participantPairings.isEmpty();
        competitionGroupTree.expectedDuration = this.expectedDuration;
        competitionGroupTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionGroupTree.expectedRelativeEndTime = this.expectedRelativeEndTime;

        if (!competitionRounds.isEmpty()) {
            competitionGroupTree.competitionRoundTreeLast = competitionRounds.last().getCompetitionRoundTree();
            competitionGroupTree.filled = !competitionRounds.first().participantPairings.isEmpty();
        }

        ParticipantResultTree participantResultTree = null;
        if (competitionGroupResult.participantResults != null) {
            for (ParticipantResult participantResult : competitionGroupResult.participantResults) {
                participantResultTree = participantResult.toParticipantResultTree(false, new HashSet<>());
                participantResultTree.points =
                        participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue();
                competitionGroupTree.participantResultTrees.add(participantResultTree);
            }
        }
        return competitionGroupTree;

    }

    public CompetitionRound getCompetitionRoundForRound(int round) {
        CompetitionRound competitionRound = competitionInstance.getCompetitionRoundForGroup(this, round);
        return competitionRound;
    }

    public CompetitionSeed getCompetitionSeed() {
//        if (competitionSeedCache == null && competitionSeed != null) {
//            competitionSeedCache = getCompetitionSeed(competitionSeed);
//        }
        return competitionSeed;
    }

    public CompetitionGroup getCompetitionGroupPreviousLane() {

        return getCompetitionSeed().getCompetitionGroupForLane(this.lane - 1);
    }

    @Override
    public SortedSet<CompetitionRound> getSubCompetitionObjectWithResults() {
        return getCompetitionRounds();
    }

    @Override
    public CompetitionSeed getUpperCompetitionObjectWithResult() {
        return getCompetitionSeed();
    }

    public void pushParticipants(Collection<Participant> participantIdsEliminated) throws CompetitionInstanceGeneratorException {

        if (participantQueue == null)
            participantQueue = new ParticipantQueue();

        ParticipantQueueElement participantQueueElement = new ParticipantQueueElement();
        for (Participant participantIdEliminated : participantIdsEliminated) {
            if (participantIdEliminated != null)
                participantQueueElement.participants.add(participantIdEliminated);
        }
        if (!participantQueueElement.participants.isEmpty()) {
            participantQueue.participantQueueElements.add(participantQueueElement);
            if (this.isInitialized() && !this.isOpen()) {
                this.open();
            } else if (this.isOpen() || this.isInitialized()) {
                if (getCompetitionRounds() != null) {
//                    Sets.sort(competitionRounds);
                    for (CompetitionRound competitionRound : competitionRounds) {
                        if (!competitionRound.isClosed()) {
                            if (competitionRound.isOpen() || !competitionRound.initializeRound()) {
                                break;
                            } else {
                                if (this.isInitialized())
                                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                                competitionRound.open();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isParticipantPairingDefined() {
        return participantPairings != null && !participantPairings.isEmpty();
    }


    @Override
    public void sortParticipantPairings() {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
            if (isParticipantPairingDefined() || (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && lane > 1)) {
                Sets.sort(participantPairings);
                for (ParticipantPairing participantPairing : participantPairings)
                    participantPairing.sortParticipantSeats();
                this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
            } else if (!isParticipantPairingDefined() && competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                Sets.sort(participantSeats);
                this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
            }

        }
    }

    public ParticipantPairing pushParticipant(Participant participant, int numberOfParticipantPerMatch) {
        if (participantPairings.isEmpty()) {
            if (!this.competitionRounds.isEmpty() && !this.competitionRounds.first().competitionMatches.isEmpty()) {
                for (int i = 0; i < this.competitionRounds.first().competitionMatches.size(); i++) {
                    ParticipantPairing participantPairing = this.getCompetitionInstance().createParticipantPairing(numberOfParticipantPerMatch);
                    participantPairing.setCompetitionGroup(this);
                    participantPairing.setCompetitionSeed(this.competitionSeed);
                    this.addParticipantPairing(participantPairing);
                }
            }
        }

        ParticipantPairing participantPairingReturned = null;
        participantPairings = Sets.sort(participantPairings);
        boolean found = false;
        for (int i = 0; i < numberOfParticipantPerMatch; i++) {

            for (ParticipantPairing participantPairing : participantPairings) {
                if (competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                    if (participantPairing.getReservedParticipantQuantity() == i) {
                        participantPairing.addParticipant(participant);
                        participantPairingReturned = participantPairing;
                        found = true;
                        break;
                    }
                } else {
                    if (!participantPairing.isFull()) {

                        participantPairing.addParticipant(participant);
                        participantPairingReturned = participantPairing;
                        found = true;
                        break;
                    }
                }

            }
            if (found)
                break;
        }
        return participantPairingReturned;
    }

//    @Override
//    public void addParticipantPairing(ParticipantPairing participantPairing) {
//        if (getParticipantPairings() != null) {
//            if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && this.round == 1) {
//                participantPairing.setCompetitionInstance(this.getCompetitionInstance());
//                getParticipantPairings().add(participantPairing);
//                this.setChanged();
//                notifyObservers(this.getLocalId());
//            } else {
//                super.addParticipantPairing(participantPairing);
//            }
//        }
//    }


    @Override
    public boolean isClosed() {
        return isSubClosed();
    }

    @Override
    public void open() throws CompetitionInstanceGeneratorException {
        if (this.getCompetitionSeed().isOpen() && this.isInitialized()) {
            if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && lane > 1) {
                if (this.getCompetitionSeed().getCompetitionGroupForLane(lane - 1).getCompetitionRoundForRound(1).isClosed()) {
                    this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                    initializeRounds();
                    if (this.competitionRounds.first().isParticipantPairingDefined()) {
                        this.participantPairings.addAll(this.competitionRounds.first().participantPairings);
                        for (ParticipantPairing participantPairing :
                                this.competitionRounds.first().participantPairings) {
                            participantPairing.setCompetitionGroup(this);
                        }
                    }
                }
            } else if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                initializeRounds();
            } else {
                this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                initializeRounds();
            }
        }
    }

    public void clearDatabaseId() {

        this.databaseId = null;
        if (competitionRounds != null)
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionRound.clearDatabaseId();
            }
        if (competitionGroupResult != null)
            competitionGroupResult.clearDatabaseId();


        if (participantPairings != null)
            for (ParticipantPairing participantPairing : participantPairings) {
                participantPairing.clearDatabaseId();
            }
    }

    @Override
    public CompetitionGroup cloneSimplified() {
        CompetitionGroup competitionGroup = null;
        try {
            competitionGroup = (CompetitionGroup) this.clone();
//            competitionGroup.fillPairingCache();
//            competitionGroup.fillResultCache();

//            competitionGroup.competitionRounds = new TreeSet<>();
//            if (competitionGroup.competitionGroupResult != null)
//                competitionGroup.competitionGroupResult = competitionGroup.competitionGroupResult.cloneSimplified();
        } catch (CloneNotSupportedException e) {
        }
        return competitionGroup;
    }

    public CompetitionGroupResult getCompetitionGroupResult() {
//        if (competitionGroupResultCache == null && competitionGroupResult != null) {
//            competitionGroupResultCache = competitionInstance.getCompetitionGroupResult(competitionGroupResult);
//        }
        return competitionGroupResult;
    }

    public void setCompetitionGroupResult(CompetitionGroupResult competitionGroupResult) {
        addCompetitionGroupResult(competitionGroupResult, this);
    }

    @XmlTransient
    @JsonIgnore
    public SortedSet<CompetitionRound> getCompetitionRounds() {
//        if ((competitionRoundsCache == null || competitionRoundsCache.isEmpty()) && competitionRounds != null && !competitionRounds.isEmpty()) {
//            competitionRoundsCache = competitionInstance.getCompetitionRounds(competitionRounds);
//        }
        return competitionRounds;
    }

    @XmlTransient
    @JsonIgnore
    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    @Override
    protected Integer getRoundOrLane() {
        return this.lane;
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return getCompetitionGroupResult().participantResults;
    }

    @Override
    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        if (this.competitionGroupResult == null) {
            this.competitionGroupResult = competitionInstance.createCompetitionGroupResult(this);
        }
        super.setParticipantResults(participantResults);

    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        if (!expectedGlobalDurationStatisticsStructureInitialized) {
            expectedGlobalDurationStatisticsStructureInitialized = true;
            Long max = null;
            Long min = null;
            Long avg = null;
//            Long sum = null;
//            Long count = null;


            switch (competitionGroupFormat) {
                case CUSTOM:
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);
                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            if (max == null || competitionRound.getExpectedGlobalDuration().max.compareTo(max) > 0)
                                max = competitionRound.getExpectedGlobalDuration().max;
                            if (avg == null || competitionRound.getExpectedGlobalDuration().avg.compareTo(avg) > 0)
                                avg = competitionRound.getExpectedGlobalDuration().avg;
                            if (min == null || competitionRound.getExpectedGlobalDuration().min.compareTo(min) > 0)
                                min = competitionRound.getExpectedGlobalDuration().min;
                        }
                    }
                    break;
                case NONE:
                    avg = 0L;
                    max = 0L;
                    min = 0L;
                    break;
                default:
                    avg = 0L;
                    max = 0L;
                    min = 0L;
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);
                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            if (competitionRound.getExpectedGlobalDuration().max.compareTo(max) > 0)
                                max += competitionRound.getExpectedGlobalDuration().max;
                            if (competitionRound.getExpectedGlobalDuration().avg.compareTo(avg) > 0)
                                avg += competitionRound.getExpectedGlobalDuration().avg;
                            if (competitionRound.getExpectedGlobalDuration().min.compareTo(min) > 0)
                                min += competitionRound.getExpectedGlobalDuration().min;
                        }
                    }
                    break;
            }

            if (competitionGroupResult != null) {
                min += getCompetitionGroupResult().getExpectedGlobalDuration().min;
                avg += getCompetitionGroupResult().getExpectedGlobalDuration().avg;
                max += getCompetitionGroupResult().getExpectedGlobalDuration().max;
            }

            expectedGlobalDurationStatisticsStructure.min = min;
            expectedGlobalDurationStatisticsStructure.max = max;
            expectedGlobalDurationStatisticsStructure.avg = avg;
        }


        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayStatisticsStructureInitialized) {
            expectedGlobalPlayStatisticsStructureInitialized = true;
            Long max = null;
            Long min = null;
            Long avg = null;
//            Long sum = 0L;
//            Long count = 0L;
            switch (competitionGroupFormat) {
                case CUSTOM:
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);
                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            if (max == null || competitionRound.getExpectedGlobalPlay().max.compareTo(max) > 0)
                                max = competitionRound.getExpectedGlobalPlay().max;
                            if (avg == null || competitionRound.getExpectedGlobalPlay().avg.compareTo(avg) > 0)
                                avg = competitionRound.getExpectedGlobalPlay().avg;
                            if (avg == null || competitionRound.getExpectedGlobalPlay().min.compareTo(min) < 0)
                                min = competitionRound.getExpectedGlobalPlay().min;
                        }
                    }
                    break;
                case NONE:
                    max = 0L;
                    min = 0L;
                    avg = 0L;
                    break;
                default:
                    max = 0L;
                    min = 0L;
                    avg = 0L;
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);
                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            max += competitionRound.getExpectedGlobalPlay().max;
                            avg += competitionRound.getExpectedGlobalPlay().avg;
                            min += competitionRound.getExpectedGlobalPlay().min;
                        }
                    }
                    break;
            }

            if (competitionGroupResult != null && getCompetitionGroupResult().competitionSeedNext != null) {
                if (getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().min != null)
                    min += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().min;
                if (getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().avg != null)
                    avg += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().avg;
                if (getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().max != null)
                    max += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedGlobalPlay().max;
            }

            expectedGlobalPlayStatisticsStructure.min = min;
            expectedGlobalPlayStatisticsStructure.max = max;
            expectedGlobalPlayStatisticsStructure.avg = avg;
//            expectedGlobalPlayStatisticsStructure.sum = sum;
//            expectedGlobalPlayStatisticsStructure.count = count;
        }
        return expectedGlobalPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {

        if (!expectedParticipantDurationStatisticsStructureInitialized) {
            expectedParticipantDurationStatisticsStructureInitialized = true;
            CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = this.getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

            Long max = competitionCreationParamPhase.getMaximumPlayDuration().toMinutes() * getExpectedParticipantPlay().max;
            Long min = competitionCreationParamPhase.getMinimumPlayDuration().toMinutes() * getExpectedParticipantPlay().min;
            Long avg = competitionCreationParamPhase.getAveragePlayDuration().toMinutes() * getExpectedParticipantPlay().avg;
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
            Long max = null;
            Long min = null;
            Long avg = null;
//            Long sum = 0L;
//            Long count = 0L;
            switch (competitionGroupFormat) {
                case CUSTOM:
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);
                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            if (max == null || competitionRound.getExpectedParticipantPlay().max.compareTo(max) > 0)
                                max = competitionRound.getExpectedParticipantPlay().max;
                            if (avg == null || competitionRound.getExpectedParticipantPlay().avg.compareTo(avg) > 0)
                                avg = competitionRound.getExpectedParticipantPlay().avg;
                            if (avg == null || competitionRound.getExpectedParticipantPlay().min.compareTo(min) < 0)
                                min = competitionRound.getExpectedParticipantPlay().min;
                        }
                    }
                    break;
                case NONE:
                    max = 0L;
                    min = 0L;
                    avg = 0L;
                    break;
                default:
                    max = 0L;
                    min = 0L;
                    avg = 0L;
                    if (getCompetitionRounds() != null) {
//                        Sets.sort(competitionRounds);

                        for (CompetitionRound competitionRound : getCompetitionRounds()) {
                            max += competitionRound.getExpectedParticipantPlay().max;
                            avg += competitionRound.getExpectedParticipantPlay().avg;
                            min += competitionRound.getExpectedParticipantPlay().min;
                        }
                    }
                    break;
            }

            if (competitionGroupResult != null && getCompetitionGroupResult().competitionSeedNext != null) {
                min += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedParticipantPlay().min;
                avg += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedParticipantPlay().avg;
                max += getCompetitionGroupResult().getCompetitionSeedNext().getExpectedParticipantPlay().max;
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

            Long max = 0L;
            Long min = 0L;
            Long avg = 0L;

            if (getCompetitionRounds() != null) {
//                Sets.sort(competitionRounds);
                for (CompetitionRound competitionRound : getCompetitionRounds()) {
                    max += competitionRound.getExpectedGlobalStep().max;
                    min += competitionRound.getExpectedGlobalStep().min;
                    avg += competitionRound.getExpectedGlobalStep().avg;
                }
            }
            expectedGlobalStepStatisticsStructure.max = max;
            expectedGlobalStepStatisticsStructure.min = min;
            expectedGlobalStepStatisticsStructure.avg = avg;
        }
        return expectedGlobalStepStatisticsStructure;
    }

    public Integer getMinNumberOfPlay() {
        if (minNumberOfPlay == null) {
            minNumberOfPlay = getExpectedGlobalPlay().min.intValue();
        }
        return minNumberOfPlay;
    }

    public Integer getMaxNumberOfPlay() {
        if (maxNumberOfPlay == null) {
            maxNumberOfPlay = getExpectedGlobalPlay().max.intValue();
        }
        return maxNumberOfPlay;
    }

    public void addCompetitionRound(CompetitionRound competitionRound) {
        competitionRound.competitionGroup = this;
        competitionRounds.add(competitionRound);
//        competitionRoundsCache = null;
    }

    @Override
    public void resetCache() {
        super.resetCache();
//        this.competitionSeedCache = null;
//        this.competitionSeedCache = null;
//        this.competitionGroupResultCache = null;
//        this.competitionGroupResultCache = null;
//        this.competitionRoundsCache = null;
//        this.competitionRoundsCache = null;
    }


//    @Override
//    public void fillCache(boolean up, boolean down) {
//        super.fillCache(up, down);
//        if (down && this.getCompetitionGroupResult() != null)
//            this.getCompetitionGroupResult().fillCache(false, true);
//    }


    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionSeed != null ? competitionSeed.localId : null;
    }

    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        setCompetitionInstance(competitionInstance);
        if (getParticipantPairings() != null)
            for (ParticipantPairing participantPairing : getParticipantPairings()) {
                participantPairing.spreadCompetitionInstance(competitionInstance);
            }
    }

    public int seatsAvalaible() {
        int seatsTaken = seatsTaken();
        return expectedParticipantQuantity - seatsTaken;
    }

    public int seatsTaken() {
        int seatsTaken = 0;
        for (ParticipantPairing participantPairing : participantPairings) {
            seatsTaken += participantPairing.getReservedParticipantQuantity();
        }
        return seatsTaken;
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionGroup competitionGroup = new CompetitionGroup();
        competitionGroup.id = this.id;
        competitionGroup.databaseId = this.databaseId;
        competitionGroup.localId = this.localId;
        return competitionGroup;
    }

    @Override
    public void clearForContext() {
        competitionRounds = null;
        competitionGroupResult = null;
        participantQueue = null;
        participantPairings = null;
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
//        competitionSeed.clearForContext();
//        competitionInstance.clearForContext();
    }

    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionGroup competitionGroup = (CompetitionGroup) super.clone();
        competitionGroup.competitionGroupResult = null;
        competitionGroup.participantQueue = null;
        SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
        String competitionSeedId = competitionSeed.id;
        competitionGroup.competitionSeed = new CompetitionSeed();
        competitionGroup.competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionGroup.competitionPhase = new CompetitionPhase();
        competitionGroup.competitionPhase.id = competitionPhaseId;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionGroup.competitionInstance = new CompetitionInstance();
        competitionGroup.competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;

        competitionGroup.competitionRounds = null;
        if (this.getCompetitionRounds() != null && !this.getCompetitionRounds().isEmpty()) {
//            Sets.sort(competitionRounds);
            competitionGroup.competitionRounds = new TreeSet<>();
            for (CompetitionRound competitionRound :
                    this.getCompetitionRounds()) {
                competitionRounds.add((CompetitionRound) competitionRound.cloneForContext());
            }
            competitionGroup.competitionRounds = competitionRounds;
        }
        return competitionGroup;
    }

    public void initializeGroup() {
        this.sortParticipantPairings();
        this.sortParticipantSeats();
    }


    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
        if (competitionRounds != null) {
            for (CompetitionRound competitionRound : competitionRounds) {
                Map<Participant, List<Participant>> participantOpponentsPlayMap = competitionRound.getParticipantOpponentsMap();
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
        if (competitionRounds != null) {
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionMatches.addAll(competitionRound.getCompetitionMatchesWithParticipants(participants));
            }
        }
        return competitionMatches;
    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (!competitionRounds.isEmpty()) {
            SortedSet<CompetitionRound> beforeDependenciesCompetitionRounds = competitionRounds.first().beforeDependenciesCompetitionRounds;
            if (beforeDependenciesCompetitionRounds != null && !beforeDependenciesCompetitionRounds.isEmpty()) {
                for (CompetitionRound competitionRound : beforeDependenciesCompetitionRounds) {
                    if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionRound.expectedRelativeEndTime) < 0)
                        expectedRelativeStartTime = competitionRound.expectedRelativeEndTime;
                }
            } else {
                expectedRelativeStartTime = this.competitionSeed.expectedRelativeStartTime;
            }
        } else {
            expectedRelativeStartTime = this.competitionSeed.expectedRelativeStartTime;
        }
        if (competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            expectedRelativeEndTime = competitionPhase.expectedRelativeEndTime;
        } else {
            if (!competitionRounds.isEmpty()) {
                for (CompetitionRound competitionRound : competitionRounds) {
                    competitionRound.fillExpectedRelativeTime();
                }
                this.expectedRelativeEndTime = this.competitionRounds.last().expectedRelativeEndTime;
                this.expectedRelativeStartTime = this.competitionRounds.first().expectedRelativeStartTime;
            } else {
                expectedRelativeEndTime = expectedRelativeStartTime;
            }
        }
        expectedDuration = expectedRelativeEndTime.minus(expectedRelativeStartTime);
    }

    public SortedSet<CompetitionMatch> getCompetitionMatchForPhaseSequence(Integer phaseSequence) {
        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
        if (this.competitionRounds != null)
            for (CompetitionRound competitionRound : this.competitionRounds) {
                competitionMatches.addAll(competitionRound.getCompetitionMatchForPhaseSequence(phaseSequence));
            }

        return competitionMatches;
    }

    public SortedSet<CompetitionRound> getCompetitionRoundsForDate(Duration relativeDuration) {
        SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
        if (this.competitionRounds != null)
            for (CompetitionRound competitionRound : this.competitionRounds) {
                competitionRounds.addAll(competitionRound.getCompetitionRoundsForDate(relativeDuration));
            }

        return competitionRounds;
    }

    public void removeCompetitionRound(CompetitionRound competitionRound) {
        competitionRound.hasChanged();
        competitionRound.deleted = true;
        if (competitionRound.competitionRoundPrevious != null) {
            competitionRound.competitionRoundPrevious.competitionRoundNext = competitionRound.competitionRoundNext;
        }
        if (competitionRound.competitionRoundNext != null) {
            competitionRound.competitionRoundNext.competitionRoundPrevious = competitionRound.competitionRoundPrevious;
        }
        competitionRound.competitionRoundNext = null;
        competitionRound.competitionRoundPrevious = null;

        for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
            competitionMatch.deleted = true;
            competitionMatch.hasChanged();
            for (CompetitionPlay competitionPlay : competitionMatch.competitionPlays) {
                competitionPlay.deleted = true;
                competitionPlay.hasChanged();
            }
        }
        if (competitionRound.beforeDependenciesCompetitionRounds != null) {
            for (CompetitionRound competitionRoundBefore : competitionRound.beforeDependenciesCompetitionRounds) {
                competitionRoundBefore.hasChanged();
                competitionRoundBefore.afterDependenciesCompetitionRounds.remove(competitionRound);
                if (competitionRound.afterDependenciesCompetitionRounds != null)
                    competitionRoundBefore.afterDependenciesCompetitionRounds.addAll(competitionRound.afterDependenciesCompetitionRounds);
                for (CompetitionMatch competitionMatch : competitionRoundBefore.competitionMatches) {
                    for (CompetitionMatch competitionMatchNext : competitionRound.competitionMatches) {
                        competitionMatch.removeNextCompetitionMatch(competitionMatchNext);
                        competitionMatch.hasChanged();
                    }
                }
            }
        }
        if (competitionRound.afterDependenciesCompetitionRounds != null) {
            for (CompetitionRound competitionRoundAfter : competitionRound.afterDependenciesCompetitionRounds) {
                competitionRoundAfter.hasChanged();
                competitionRoundAfter.beforeDependenciesCompetitionRounds.remove(competitionRound);
                if (competitionRound.beforeDependenciesCompetitionRounds != null)
                    competitionRoundAfter.beforeDependenciesCompetitionRounds.addAll(competitionRound.beforeDependenciesCompetitionRounds);
                for (CompetitionMatch competitionMatch : competitionRoundAfter.competitionMatches) {
                    for (CompetitionMatch competitionMatchPrevious : competitionRound.competitionMatches) {
                        competitionMatch.removePreviousCompetitionMatch(competitionMatchPrevious);
                        competitionMatch.hasChanged();
                    }
                }
            }
        }
        this.competitionRounds.remove(competitionRound);
        this.competitionInstance.competitionRounds.remove(competitionRound);
        if (competitionRound.competitionMatches != null) {
            this.competitionInstance.competitionMatches.removeAll(competitionRound.competitionMatches);
            for (CompetitionMatch competitionMatch :
                    competitionRound.competitionMatches) {
                this.competitionInstance.competitionPlays.removeAll(competitionMatch.competitionPlays);
            }
        }
        this.competitionRounds.remove(competitionRound);
        int roundValue = 0;
        for (CompetitionRound competitionRoundElt : this.competitionRounds) {
            roundValue++;
            if (competitionRoundElt.round != roundValue) {
                competitionRoundElt.round = roundValue;
                competitionRoundElt.hasChanged();
            }
        }
    }

    public boolean isRegistrationOnTheFly() {
        boolean registrationOnTheFly = competitionGroupFormat != null && competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0 && competitionPhase.competitionCreationParamPhase.registrationOnTheFly != null && competitionPhase.competitionCreationParamPhase.registrationOnTheFly;
        return registrationOnTheFly;
    }

    public void addParticipantPairing(ParticipantPairing participantPairing) {
        super.addParticipantPairing(participantPairing);
        for (ParticipantSeat participantSeat : participantPairing.participantSeats) {
            if (this.getParticipantSeat(participantSeat.participant) == null) {
                this.addParticipantSeat(participantSeat);
            }
        }
    }

    public void sortParticipantSeats() {
        Sets.sort(participantSeats);
    }

    @Override
    public boolean isForParticipant(Participant participant) {
        boolean isForParticipant = super.isForParticipant(participant);
        if (!isForParticipant && !participantSeats.isEmpty()) {
            for (ParticipantSeat participantSeat :
                    participantSeats) {
                isForParticipant = participantSeat.participant != null && participantSeat.participant.compareTo(participant) == 0;
                if (isForParticipant) break;

            }
        }
        return isForParticipant;
    }

    public boolean isFull() {
        boolean isFull = false;
        int numberOfParticipants = getRealParticipantsAsArray().size();
        isFull = numberOfParticipants == expectedParticipantQuantity;
        return isFull;
    }

    public List<Participant> getRealParticipantsAsArray() {
        Set<Participant> participantSet = new HashSet<>();
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            for (ParticipantSeat participantSeat : participantSeats) {
                if (participantSeat.participant != null)
                    participantSet.add(participantSeat.participant);
            }
        } else {
            for (ParticipantPairing participantPairing : participantPairings) {
                participantSet.addAll(participantPairing.getRealParticipantsAsArray());
            }
        }
        List<Participant> participantList = new ArrayList<>(participantSet);
        return participantList;
    }

    public ParticipantSeat createParticipantSeat(Participant participant) {

        ParticipantSeat participantSeat = ParticipantSeat.createInstance(getIdGenerator(), this.competitionInstance);
        participantSeat.setParticipant(participant);
        participantSeat.setCompetitionGroup(this);
        this.participantSeats.add(participantSeat);
        if (this.competitionSeed.getParticipantSeat(participant) == null)
            this.competitionSeed.addParticipantSeat(participantSeat);
        if (this.competitionPhase.getParticipantSeat(participant) == null)
            this.competitionPhase.addParticipantSeat(participantSeat);
        return participantSeat;
    }

    public CompetitionMatch createCompetitionMatchSimulationForLadder() {
        CompetitionMatch competitionMatch = null;

        Participant participant = null;
        if (participantSeats.size() < expectedParticipantQuantity) {
            if (competitionPhase.competitionCreationParamPhase.registrationOnTheFly != null && competitionPhase.competitionCreationParamPhase.registrationOnTheFly) {
                int index = competitionPhase.participantSeats.size() + 1;
                String name = "Participant OTF " + index;
                participant = this.competitionInstance.getCompetitionComputationParam().participantType.createParticipant(this.competitionInstance);
                participant.internationalizedLabel = new InternationalizedLabel();
                participant.internationalizedLabel.defaultLabel = name;
                subscribeOnTheFly(participant, index % 2 == 1 ? index : null);
            }
        }
        boolean canAddCompetitionMatch = false;
        if (competitionPhase.participantSeats.size() > this.competitionPhase.competitionCreationParamPhase.participantQualifiedPerMatch || (competitionPhase.participantSeats.size() > 0 && this.competitionPhase.competitionCreationParamPhase.numberOfParticipantMatch == 1)) {
            if (competitionPhase.competitionCreationParamPhase.phaseDuration != null) {
                double phaseDurationInMinutes = (double) Duration.fromString(competitionPhase.competitionCreationParamPhase.phaseDuration).toMinutes();
                double playDurationInMinutes = (double) Duration.fromString(competitionPhase.competitionCreationParamPhase.averagePlayDuration).toMinutes();
                double numberOfPlayMinimum = (double) competitionPhase.competitionCreationParamPhase.numberOfPlayMinimum;
                double maximumNumberOfParallelPlay = (double) competitionPhase.competitionCreationParamPhase.maximumNumberOfParallelPlay;

                double numberOfParticipant = competitionPhase.getRealParticipantsAsArray().size();
                if (maximumNumberOfParallelPlay == 0)
                    maximumNumberOfParallelPlay = numberOfParticipant;
                int maxNumberOfMatchesInPhase = (int) Math.ceil(phaseDurationInMinutes / numberOfPlayMinimum / playDurationInMinutes / (maximumNumberOfParallelPlay / numberOfParticipant));
                canAddCompetitionMatch = maxNumberOfMatchesInPhase > competitionPhase.getCompetitionMatches().size() / maximumNumberOfParallelPlay;
            } else {
                canAddCompetitionMatch = competitionRounds.size() < expectedParticipantQuantity;
            }
        }
        if (canAddCompetitionMatch) {
            CompetitionRound competitionRound = getCompetitionRoundForRound(1);
            competitionMatch = competitionRound.createCompetitionMatchSimulationForLadder();
            while (competitionMatch == null && competitionRounds.size() < expectedParticipantQuantity) {
                if (competitionMatch == null && competitionRounds.size() < expectedParticipantQuantity) {
                    if (competitionRound.competitionRoundNext == null) {
                        competitionRound = competitionInstance.createCompetitionRound(this, competitionRound);
                        competitionRound.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                    } else {
                        competitionRound = competitionRound.competitionRoundNext;
                    }
                    competitionMatch = competitionRound.createCompetitionMatchSimulationForLadder();
                }
            }
        }
        return competitionMatch;
    }

    public ParticipantSeat subscribeOnTheFly(Participant participant, Integer ranking) {
        ParticipantSeat participantSeat = null;
        if (!isForParticipant(participant)) {
            if (competitionPhase.competitionCreationParamPhase.registrationOnTheFly && competitionPhase.participantSeats.size() < competitionInstance.competitionComputationParam.numberOfParticipantCompetition) {
                participantSeat = this.competitionInstance.subscribe(participant, ranking);
                participantSeats.add(participantSeat);
                participantSeat.setCompetitionSeed(competitionSeed);
                competitionSeed.addParticipantSeat(participantSeat);
                this.setChanged();
                notifyObservers(this.localId);
//            ParticipantSeat participantSeatGroup = createParticipantSeat(participantSeat.participant);
            }
        }
        return participantSeat;
    }


    public Set<CompetitionMatch> getCompetitionMatches() {
        Set<CompetitionMatch> competitionMatches = new HashSet<>();
        for (CompetitionRound competitionRound : competitionRounds) {
            competitionMatches.addAll(competitionRound.getCompetitionMatches());
        }
        return competitionMatches;
    }

    public void removeParticipant(Participant participant) {
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null && participantSeat.participant.compareTo(participant) == 0) {
                participantSeat.participant = null;
                break;
            }
        }
        for (ParticipantPairing participantPairing : participantPairings) {
            if (participantPairing.contains(participant)) {
                participantPairing.removeParticipant(participant);
                break;
            }
        }

        competitionGroupResult.removeParticipant(participant);
    }

    public final ParticipantResult addParticipantSeat(ParticipantSeat participantSeat) {
        ParticipantResult participantResult = null;
        participantSeats.add(participantSeat);
        if (participantSeat.participant != null) {
            boolean createIfNorExists = true;
            participantResult = getParticipantResultFor(participantSeat.participant, createIfNorExists);
        }
        return participantResult;
    }

    public final ParticipantSeat getParticipantSeat(Participant participant) {
        ParticipantSeat participantSeat = null;
        if (participant != null) {
            for (ParticipantSeat participantSeatCurrent : participantSeats) {
                if (participantSeatCurrent.participant != null && participantSeatCurrent.participant.compareTo(participant) == 0) {
                    participantSeat = participantSeatCurrent;
                    break;
                }
            }
        }
        return participantSeat;
    }

    public CompetitionMatch addCompetitionMatchForLadder(List<Participant> participantList) {
        return addCompetitionMatchForLadder(participantList, null);
    }

    public CompetitionMatch addCompetitionMatchForLadder(List<Participant> participantList, CompetitionObserver competitionObserver) {
        CompetitionMatch competitionMatch = null;
        int round = 0;
        while (competitionMatch == null) {
            round++;
            CompetitionRound competitionRound = this.getCompetitionRoundForRound(round);
            if (competitionRound == null) {
                competitionRound = this.getCompetitionRoundForRound(round - 1);
                competitionRound = competitionInstance.createCompetitionRound(this, competitionRound, competitionObserver);
            }
            competitionMatch = competitionRound.addCompetitionMatchForLadder(participantList, competitionObserver);
        }
        this.setChanged();
        this.notifyObservers(this);
        return competitionMatch;
    }

    public List<Participant> getParticipantsFromParticipantInstanceDatabaseId(List<Integer> participantDatabaseIds) {
        List<Participant> participantList = new ArrayList<>();
        for (Integer participantDatabaseId : participantDatabaseIds) {

            boolean found = false;
            for (ParticipantSeat participantSeat : participantSeats) {
                if (participantSeat.participant != null) {
                    if (participantSeat.participant.databaseId.compareTo(participantDatabaseId) == 0) {
                        participantList.add(participantSeat.participant);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                ParticipantSeat participantSeat = this.competitionInstance.getParticipantSeatForParticipantInstanceDatabaseId(participantDatabaseId);
                if (participantSeat != null) {
                    this.createParticipantSeat(participantSeat.participant);
                    participantList.add(participantSeat.participant);
                }
            }
        }
        return participantList;
    }

    @Override
    public void delete() {
        super.delete();
        if (competitionGroupResult != null)
            competitionGroupResult.delete();
        this.competitionInstance.competitionGroups.remove(this);
        this.competitionSeed.competitionGroups.remove(this);

        Set<ParticipantSeat> participantSeatsToRemove = new HashSet<>();
        for (ParticipantSeat participantSeat : this.getCompetitionInstance().participantSeatsAll) {
            if (participantSeat.competitionGroup != null && participantSeat.competitionGroup.compareTo(this) == 0) {
                participantSeatsToRemove.add(participantSeat);
            }
        }
        this.getCompetitionInstance().participantSeatsAll.removeAll(participantSeatsToRemove);
    }


    public void reopen() {
        if (this.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionRound.reopen();
            }
        }
    }

    @Override
    public void generationDone() {
        super.generationDone();
        if (competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            List<CompetitionRound> competitionRounds = new ArrayList<>(this.competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                if (competitionRound.round == 1) {
                    List<CompetitionMatch> competitionMatches = new ArrayList<>(competitionRound.competitionMatches);
                    for (CompetitionMatch competitionMatch : competitionMatches) {
                        competitionMatch.delete();
                    }
                    competitionRound.competitionMatches.clear();
                    competitionRound.competitionObjectStatus = CompetitionObjectStatus.NOT_INITIALIZED;
                } else {
                    competitionRound.delete();
                }
            }
        }
    }

}
