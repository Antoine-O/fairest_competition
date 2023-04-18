package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.db.entity.game.ResetPolicy;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTree;
import com.qc.competition.service.structure.tree.CompetitionSeedTree;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionCreationParamPhaseFinal;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceGeneratorImpl;
import com.qc.competition.utils.Sets;
import com.qc.competition.ws.simplestructure.Duration;
import org.goochjs.glicko2.Rating;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionSeed extends CompetitionObjectWithResult<CompetitionPhase, CompetitionGroup> implements Simplify<CompetitionSeed> {
    public static String CLASS = CompetitionSeed.class.getSimpleName();

    //    @XmlElementWrapper(name = "groupResultsPrevious")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "groupResultPreviousIds")

    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("groupResultPreviousIds")
    public SortedSet<CompetitionGroupResult> competitionGroupResultsPrevious = new TreeSet<>();
    //    @XmlElement(name = "phaseParameter")
//    @JsonProperty("phaseParameter")
//    @Deprecated
//    public CompetitionCreationParamPhase competitionCreationParamPhase;
    //    @XmlElementWrapper(name = "groups")
//    @XmlElement(name = "groupId")
    @XmlList
    @XmlAttribute(name = "groupIds")
    @JsonProperty("groupIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>();
    @XmlAttribute(name = "filteringUnit")
    public Unit filteringUnit;
    @XmlAttribute(name = "stepType")
    public StepType stepType;
    @XmlAttribute(name = "filteringValue")
    public int filteringValue;
    @XmlAttribute(name = "participantQualified")
    @JsonProperty("participantQuantity")
    public Integer participantQuantity;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlAttribute(name = "filteringMethod")
    @JsonProperty("filteringMethod")
    public ParticipantFilteringMethod participantFilteringMethod;
    @XmlAttribute(name = "pairingMethod")
    @JsonProperty("pairingMethod")
    public ParticipantPairingMethod participantPairingMethod;
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
    //    @XmlElementWrapper(name = "nextCompetitionPhases")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "nextCompetitionSeedIds")
    @JsonProperty("nextCompetitionSeedIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionSeed> nextCompetitionSeeds = new TreeSet<>();
    @XmlList
    @XmlAttribute(name = "previousCompetitionSeedIds")
//    @XmlElementWrapper(name = "previousCompetitionPhases")
//    @XmlElement(name = "localId")
    @JsonProperty("previousCompetitionSeedIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionSeed> previousCompetitionSeeds = new TreeSet<>();
    @XmlAttribute(name = "empty")
    @JsonProperty("empty")
    public boolean emptyPhase = false;
    @XmlAttribute(name = "round")
    public Integer round;
    @XmlAttribute(name = "roundInPhase")
    public Integer roundInPhase;
    //    @XmlAttribute(name = "phase")
//    @Deprecated
//    public Integer phase;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionInstanceId")
    public CompetitionInstance competitionInstance;
    //    @Deprecated
//    @XmlAttribute(name = "phaseType")
//    public PhaseType phaseType;
    @XmlAttribute(name = "phaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;
    @XmlElementWrapper(name = "seats")
    @XmlElement(name = "seat")
    @JsonProperty("seats")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();

//    public PhaseParameter getPhaseParameter() {
//        return phaseParameter;
//    }

    //    @XmlTransient
//    public String competitionInstanceId;
//    @XmlTransient
//    @JsonProperty("competitionGroupResultsPreviousCache")
//    protected SortedSet<CompetitionGroupResult> competitionGroupResultsPreviousCache = null;
//    @XmlTransient
//    @JsonProperty("competitionGroupsCache")
//    protected SortedSet<CompetitionGroup> competitionGroupsCache = null;
//    @XmlTransient
//    @JsonIgnore
//    private Integer competitionGroupResultSize = null;
    //    @XmlTransient
//    @JsonIgnore
//    private boolean allPreviousParticipantPairingsComputed = false;
//    @XmlTransient
//    @JsonIgnore
//    private boolean allPreviousParticipantResultsComputed = false;
//    @XmlTransient
//    @JsonIgnore
//    private Integer quantityPairingCache;
//    @XmlTransient
//    @JsonIgnore
//    private SortedSet<ParticipantPairing> previousParticipantPairingsCache = null;
//    @XmlTransient
//    @JsonIgnore
//    private SortedMap<CompetitionObjectWithResult, SortedSet<ParticipantResult>> previousParticipantResultsMapCache = null;

    public CompetitionSeed() {
        super();
    }

    private CompetitionSeed(IdGenerator idGenerator) {
        super(idGenerator);
    }
//    @XmlTransient
//    @JsonIgnore
//    private int previousParticipantResultSize;

    public static CompetitionSeed createInstance(IdGenerator idGenerator) {
        return new CompetitionSeed(idGenerator);
    }

    public SortedSet<CompetitionGroupResult> getCompetitionGroupResultsPrevious() {
        return competitionGroupResultsPrevious;
    }


    public SortedSet<CompetitionGroup> getCompetitionGroups() {

        return competitionGroups;
    }


    public void initForXmlOutput() {
        for (ParticipantPairing participantPairing : participantPairings) {
            participantPairing.initForXmlOutput();
        }

        for (ParticipantResult participantResult : participantResults) {
            participantResult.initForXmlOutput();
        }
//        if (competitionInstance.getCompetitionSeed() != null && competitionInstance.getCompetitionSeed().localId.compareTo(this.localId) == 0 && !competitionGroupResultsPrevious.isEmpty()) {
//            competitionGroupResultPrevious = competitionGroupResultsPrevious.first();
//            competitionGroupResultPreviousIds .clear();
//            competitionGroupResultPreviousIds .add(competitionGroupResultPrevious.localId);
//        }


    }

    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
    }

    @Override
    public boolean allParticipantResultsSet() {
        boolean allParticipantResultsSet = true;
        for (ParticipantPairing participantPairing : this.participantPairings) {
            for (Participant participantId : participantPairing.getRealParticipantsAsArray()) {
                allParticipantResultsSet = allParticipantResultsSet &&
                        isParticipantResultSet(participantId);
                if (!allParticipantResultsSet)
                    break;
            }
        }
        return allParticipantResultsSet;
    }


    public SortedSet<ParticipantPairing> getPreviousParticipantPairings() {
        SortedSet<ParticipantPairing> previousParticipantPairings = new TreeSet<>();
//        if (previousParticipantPairingsCache == null || previousParticipantPairingsCache.isEmpty()) {
//            previousParticipantPairingsCache = new TreeSet<>();
        for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious())
            previousParticipantPairings.addAll(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().participantPairings);
        Sets.sort(previousParticipantPairings);
//        }
        return previousParticipantPairings;
    }

    public SortedMap<CompetitionObjectWithResult, SortedSet<ParticipantResult>> getPreviousParticipantResultsMap() {
        SortedMap<CompetitionObjectWithResult, SortedSet<ParticipantResult>> competitionObjectWithResultSortedSetSortedMap = new TreeMap<>();
//        if (competitionGroupResultsPrevious == null || competitionGroupResultsPrevious.isEmpty()) {
        SortedSet<CompetitionSeed> competitionSeeds = getPreviousCompetitionSeeds();
        if (competitionSeeds.size() == 1 && competitionSeeds.iterator().next().getCompetitionGroups().size() == competitionGroupResultsPrevious.size()) {
            SortedSet<ParticipantResult> participantResults = new TreeSet<>();
            participantResults.addAll(competitionSeeds.iterator().next().participantResults);
            competitionObjectWithResultSortedSetSortedMap.put(competitionSeeds.iterator().next(), participantResults);
        } else {
            Map<CompetitionGroup, SortedSet<ParticipantResult>> previousParticipantResultsMapCacheTemp = new HashMap<>();
            for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
                SortedSet<ParticipantResult> participantResults = new TreeSet<>();
                participantResults.addAll(competitionGroupResult.participantResults);
                previousParticipantResultsMapCacheTemp.put(competitionGroupResult.getCompetitionGroup(), participantResults);
            }
            competitionObjectWithResultSortedSetSortedMap.putAll(previousParticipantResultsMapCacheTemp);
        }
//        }
        return competitionObjectWithResultSortedSetSortedMap;
    }

    private SortedSet<CompetitionSeed> getPreviousCompetitionSeeds() {
        SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
        for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
            competitionSeeds.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
        }
        Sets.sort(competitionSeeds);
        return competitionSeeds;
    }

    private SortedSet<ParticipantResult> removeRank(SortedSet<ParticipantResult> participantResults) {
        for (ParticipantResult participantResult : participantResults) {
            participantResult.removeRank();
        }
        return participantResults;
    }

    public int getPreviousParticipantResultsSize() {
//        getPreviousParticipantResultsMap();
        SortedSet<ParticipantResult> participantResultsAll = new TreeSet<>();
        SortedMap<CompetitionObjectWithResult, SortedSet<ParticipantResult>> competitionObjectWithResultSortedSetSortedMap = getPreviousParticipantResultsMap();
        if (!competitionObjectWithResultSortedSetSortedMap.isEmpty()) {
            for (SortedSet<ParticipantResult> participantResults : competitionObjectWithResultSortedSetSortedMap.values()) {
                participantResultsAll.addAll(participantResults);
            }
//            participantResultsAll = removeRank(participantResultsAll);
//            Sets.sort(participantResultsAll);
            participantResultsAll = removeDoublons(participantResultsAll, false);
        }
        return participantResultsAll.size();
    }

    public int getQuantityPairing() {

        CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
        int quantityPairing = (int) Math.ceil((double) participantQuantity / (double) competitionCreationParamPhase.numberOfParticipantMatch);
        return quantityPairing;
    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        return new ParticipantScoreSeed(this);
    }

    @Override
    public void open() throws CompetitionInstanceGeneratorException {
        if (this.isInitialized()) {
            this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            initializeCompetitionGroups();
        }
    }

//    public void fillParticipantResultWithFakeValue() {
//        SortedSet<Integer> competitionGroupIds = new TreeSet<Integer>();
//
//        for (CompetitionGroup competitionGroup : competitionGroupsCache) {
//            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0 || competitionGroup.round == 1) {
//                competitionGroup.fillParticipantResultWithFakeValue();
//                competitionGroupIds.add(competitionGroup.localId);
//            }
//        }
//        for (CompetitionGroup competitionGroup : competitionGroupsCache) {
//            if (!competitionGroupIds.contains(competitionGroup.localId))
//                competitionGroup.fillCompetitionGroupResultFromCompetitionGroupRounds();
//        }
//        fillCompetitionSeedResultFromCompetitionGroups();
//        Set<CompetitionSeed> competitionSeeds = new HashSet<CompetitionSeed>();
//        CompetitionSeed competitionSeedNext = null;
//        for (CompetitionGroup competitionGroup : competitionGroupsCache) {
//            competitionSeedNext = competitionGroup.competitionGroupResult.competitionSeedNext;
//            if (competitionSeedNext != null && !competitionSeeds.contains(competitionSeedNext)) {
//                competitionSeedNext.doPairing();
//                competitionSeedNext.initializeCompetitionGroups();
//                competitionSeedNext.fillParticipantResultWithFakeValue();
//                competitionSeeds.add(competitionSeedNext);
//            }
//        }
//    }

    protected void doPairing() throws CompetitionInstanceGeneratorException {
        if (participantPairingMethod != null) {
            participantPairingMethod.doPairing(this);
            this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
            if (this.competitionPhase != null) {
                if (this.competitionPhase.competitionObjectStatus == null || !this.competitionPhase.competitionObjectStatus.isOpen()) {
                    this.competitionPhase.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
                }
            }
            setChanged();
            notifyObservers(this.localId);
        }
    }

    public void fillCompetitionMatchLink() {

        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionGroup.fillCompetitionMatchLink();
            }
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionGroup.fillCompetitionRoundLink();
            }
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionGroup.fillCompetitionRoundsSequence();
            }
        }
        for (CompetitionSeed competitionSeed : computeNextCompetitionSeeds()) {
            competitionSeed.fillCompetitionMatchLink();
        }
    }

    public void fillCompetitionSeedResultFromCompetitionGroup(CompetitionGroup competitionGroup) {

        boolean createIfNorExists = true;
        boolean rankByGlicko2 = false;
        if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.NONE) == 0) {

            for (ParticipantResult participantResultGroup : competitionGroup.getCompetitionGroupResult().participantResults) {
                ParticipantResult participantResult = getParticipantResultFor(participantResultGroup.participant, createIfNorExists);
                participantResult.rank = participantResultGroup.rank;
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_LANE, competitionGroup.lane);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_RANK, participantResult.rank);
                if (!createIfNorExists)
                    addParticipantResult(participantResult);
            }

        } else {
            int score = 0;
            int win = 0;
            int draw = 0;
            int loss = 0;
//        int divider = 1;


            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                rankByGlicko2 = true;
            }
            Map<Participant, Rating> participantRatingMap = getParticipantRatingMap();

            for (Map.Entry<Participant, Rating> participantRatingEntry : participantRatingMap.entrySet()) {

                ParticipantResult participantResult = getParticipantResultFor(participantRatingEntry.getKey(), createIfNorExists);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScore.GLICKO2_RATING, participantRatingEntry.getValue().getRating());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScore.GLICKO2_RATING_DEVIATION, participantRatingEntry.getValue().getRatingDeviation());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScore.GLICKO2_VOLATILITY, participantRatingEntry.getValue().getVolatility());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScore.GLICKO2_NUMBER_OF_RESULTS, participantRatingEntry.getValue().getNumberOfResults());
            }


            if (!competitionGroups.isEmpty()) {

                SortedSet<CompetitionGroup> competitionGroupsSorted = new TreeSet<>(competitionGroups);
                for (Participant participant : this.getRealParticipantsAsArray()) {
                    for (CompetitionGroup competitionGroupElt : competitionGroupsSorted) {

//                            int malus = MathUtils.getPointMalus(competitionGroupElt.lane);
//                            int multiplicator = 1;
//                            if (competitionGroupElt.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0)
//                                multiplicator = MathUtils.getMultiplicatorBonus(competitionGroupElt.lane, competitionGroups.size());
                        boolean participantFound = false;
                        for (ParticipantResult participantResultGroup : competitionGroupElt.getParticipantResults()) {
                            if (participant.compareTo(participantResultGroup.participant) == 0) {
                                participantFound = true;
                                ParticipantResult participantResult = getParticipantResultFor(participantResultGroup.participant, createIfNorExists);
//                                    score = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).computeNumberValue().intValue() * multiplicator;
                                score = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).computeNumberValue().intValue();
                                score = score + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).computeNumberValue().intValue();
                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS, score);

                                win = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_WIN).computeNumberValue().intValue();
                                win = win + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_WIN).computeNumberValue().intValue();
                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN, win);

                                draw = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_DRAW).computeNumberValue().intValue();
                                draw = draw + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_DRAW).computeNumberValue().intValue();
                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW, draw);

                                loss = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_LOSS).computeNumberValue().intValue();
                                loss = loss + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_LOSS).computeNumberValue().intValue();
                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS, loss);

                                if (competitionGroupElt.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                                    boolean thirdPlaceMatchEnabled = ((CompetitionCreationParamPhaseFinal) competitionGroupElt.competitionPhase.competitionCreationParamPhase).thirdPlaceMatchEnabled;
                                    boolean finalPhase = competitionGroupElt.competitionPhase.competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal;
                                    boolean firstGroup = competitionGroupElt.lane == 1;
                                    boolean lastGroup = competitionGroupElt.lane == competitionGroups.size();
                                    Number lastActiveGroupLane = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_LANE) != null ? participantResult.participantScore.getParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_LANE).computeNumberValue() : null;
                                    Number lastActiveRound = participantResultGroup.participantScore.getParticipantScoreValue(ParticipantScoreGroup.LAST_ACTIVE_ROUND).computeNumberValue();
                                    if (!finalPhase) {
                                        if (lastActiveGroupLane == null || competitionGroupElt.lane > lastActiveGroupLane.intValue()) {
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_LANE, competitionGroupElt.lane);
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_RANK, participantResultGroup.rank);
                                            if (thirdPlaceMatchEnabled)
                                                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_ROUND, lastActiveRound);
                                        }
                                    } else if (finalPhase) {
                                        if (!thirdPlaceMatchEnabled || (thirdPlaceMatchEnabled && firstGroup)) {
                                            if (lastActiveGroupLane == null || competitionGroupElt.lane > lastActiveGroupLane.intValue()) {
                                                if (lastActiveGroupLane.intValue() < competitionGroupElt.lane) {
                                                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_LANE, competitionGroupElt.lane);
                                                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_RANK, participantResultGroup.rank);
//                                                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_ROUND, lastActiveRound);
                                                }
                                                if (thirdPlaceMatchEnabled)
                                                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.LAST_ACTIVE_GROUP_ROUND, lastActiveRound);
                                            }
                                        } else if (thirdPlaceMatchEnabled && lastGroup) {
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.THIRD_PLACE_GROUP_RANK, participantResultGroup.rank);
                                        }
                                    }
                                }
                                participantResult.participantScore.participantScoresSub.add(participantResultGroup.participantScore);
                                break;
                            }
                        }
//                                if (!participantFound && competitionGroup.lane > 1 && competitionGroupElt.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
//                                    participantResult = getParticipantResultFor(participant, createIfNorExists);
//                                    score = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreGroup.SCORE_POINTS).computeNumberValue().intValue();
//                                    for (ParticipantPairing participantPairing1 : competitionGroup.getParticipantPairings()) {
//                                        score = score + participantPairing1.getRealParticipantsAsArray().size();
//                                    }
//                                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS, score);
//
//                                }
                    }
                }
            }

        }
//        if (rankByGlicko2)
        ParticipantScore.fillRank(this.participantResults, ParticipantScorePhase.RANK, rankByGlicko2);

//        Sets.sort(this.participantResults);
    }

    private void fillCompetitionSeedResultFromCompetitionGroups() {
        participantResults.clear();
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (competitionGroup.isClosed()) {
                    fillCompetitionSeedResultFromCompetitionGroup(competitionGroup);
                }
            }
            if (!participantResults.isEmpty()) {
                participantResults = removeDoublons(participantResults, true);
                ParticipantScore.fillRank(this.participantResults, ParticipantScoreSeed.RANK);
                ParticipantScore.fillPoints(this.participantResults, ParticipantScoreSeed.SCORE_POINTS);
            }
        }
        setChanged();
        notifyObservers(this.localId);

    }

    private SortedSet<ParticipantResult> removeDoublons(SortedSet<ParticipantResult> participantResults, boolean doSorting) {
        List<ParticipantResult> participantResultsArray = new ArrayList<>();
        participantResultsArray.addAll(participantResults);
        boolean hasChanged = false;
        for (int i = 0; i < participantResultsArray.size(); i++) {
            for (int j = i + 1; j < participantResultsArray.size(); j++) {
                if (participantResultsArray.get(i).participant.compareTo(participantResultsArray.get(j).participant) == 0) {
                    participantResultsArray.remove(j);
                    j--;
                    hasChanged = true;
                }
            }
        }
        if (hasChanged) {
            participantResults.clear();
            participantResults.addAll(participantResultsArray);
        }
        if (doSorting)
            Sets.sort(participantResults);
        return participantResults;
    }

    public CompetitionGroup getCompetitionGroupForLane(int lane) {
        CompetitionGroup competitionGroupFound = null;
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (competitionGroup.lane == lane) {
                    competitionGroupFound = competitionGroup;
                    break;
                }
            }
        }
        return competitionGroupFound;
    }

    public SortedSet<CompetitionPlay> getCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
        Set<CompetitionSeed> competitionSeeds = new HashSet<>();
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionPlays.addAll(competitionGroup.getCompetitionPlays());
                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null && !competitionSeeds.contains(competitionGroup.getCompetitionGroupResult().competitionSeedNext)) {
                    competitionSeeds.add(competitionGroup.getCompetitionGroupResult().competitionSeedNext);
                    competitionPlays.addAll(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().getCompetitionPlays());
                }
            }
        }
        return competitionPlays;
    }

//    public int getFilteredParticipantSize() {
//
//        CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionCreationParamPhase;
//        if (competitionCreationParamPhase == null)
//            competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
//        return participantFilteringMethod.getFilteredParticipantSize(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, getPreviousParticipantResultsSize(), filteringValue, filteringUnit);
//    }

    public boolean isPreviousCompetitionGroupSeedEmpty() {
        boolean previousCompetitionGroupSeedEmpty = true;
        for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
            previousCompetitionGroupSeedEmpty = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().emptyPhase;
            if (!previousCompetitionGroupSeedEmpty)
                break;
        }
        return previousCompetitionGroupSeedEmpty;
    }

    public Set<CompetitionGroupResult> computeLastCompetitionGroupResults() {
        Set<CompetitionGroupResult> competitionGroupResults = new HashSet<>();

        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups)
                competitionGroupResults.addAll(competitionGroup.computeLastCompetitionGroupResults());
        }
        return competitionGroupResults;
    }

    public SortedSet<CompetitionSeed> computeNextCompetitionSeeds() {
        SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null) {
                    competitionSeeds.add(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext());
                }
            }
        }
        Sets.sort(competitionSeeds);
        return competitionSeeds;
    }

    public SortedSet<CompetitionSeed> computePreviousCompetitionSeeds() {
        SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
        for (CompetitionGroupResult competitionGroupResult : this.getCompetitionGroupResultsPrevious()) {
            if (competitionGroupResult.competitionGroup != null && competitionGroupResult.getCompetitionGroup().competitionSeed != null) {
                competitionSeeds.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
            }
        }
        return competitionSeeds;
    }

    private List<ParticipantPairing> computeParticipantPairingsAsArray() {
        List<ParticipantPairing> previousParticipantPairingArrayList = new ArrayList<>();
        previousParticipantPairingArrayList.addAll(participantPairings);
        return previousParticipantPairingArrayList;
    }

    private void initializeCompetitionGroups() throws CompetitionInstanceGeneratorException {
        if (!competitionGroups.isEmpty()) {
//            Sets.sort(competitionGroups);
            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (!competitionGroup.isInitialized())
                    competitionGroup.initializeGroup();
                competitionGroup.open();
            }
        }
    }

    public boolean isAllSubParticipantResultsSet() {
        boolean subParticipantResultsSet = isSubParticipantResultsSet();
        if (subParticipantResultsSet)
            if (!competitionGroups.isEmpty()) {
                SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    subParticipantResultsSet = competitionGroup.isParticipantResultsSet() && competitionGroup.isAllSubParticipantResultsSet();
                    if (!subParticipantResultsSet)
                        break;
                }
            }
        return subParticipantResultsSet;
    }

    public boolean isCompetitionGroupResultsPreviousResultsSet() {
        boolean competitionGroupResultsPreviousResultsSet = true;
        if (competitionGroupResultsPrevious != null)
            for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
                competitionGroupResultsPreviousResultsSet = competitionGroupResult.getCompetitionGroup().isSubParticipantResultsSet();
                if (!competitionGroupResultsPreviousResultsSet)
                    break;
            }
        return competitionGroupResultsPreviousResultsSet;
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
        boolean participantResultSet = true;
        for (Participant participant : this.getRealParticipantsAsArray()) {
            participantResultSet = isParticipantResultSet(participant);
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

    @Override
    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = false;
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                participantResultSet = competitionGroup.isClosed() && competitionGroup.isParticipantResultsSet();
                if (!participantResultSet)
                    break;
            }
        }
        return participantResultSet;
    }

//
//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        description.append("previousParticipantResultSize=").append(getPreviousParticipantResultsSize()).append(System.lineSeparator());
//        description.append("[participantPairings]").append(System.lineSeparator());
//        for (ParticipantPairing participantPairing : participantPairings) {
//            description.append(participantPairing.toDescription());
//        }
//        description.append("[competitionGroupResultsPrevious]").append(System.lineSeparator());
//        for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
//            description.append(competitionGroupResult.toString()).append(System.lineSeparator());
//        }
//        if (!competitionGroups.isEmpty()) {
//            description.append("[competitionGroupsCache]").append(System.lineSeparator());
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                description.append(competitionGroup.toDescription());
//            }
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
//        if (!competitionGroups.isEmpty()) {
//            description.append(indentation).append("[competitionGroupsCache]").append(System.lineSeparator());
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                description.append(competitionGroup.toDescriptionTree(level + 1));
//            }
//        }
//
//        Set<CompetitionSeed> competitionSeeds = new HashSet<>();
//        if (!competitionGroups.isEmpty()) {
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null && !competitionSeeds.contains(competitionGroup.getCompetitionGroupResult().competitionSeedNext)) {
//                    description.append(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().toDescriptionTree(level + 1));
//                    competitionSeeds.add(competitionGroup.getCompetitionGroupResult().competitionSeedNext);
//                }
//            }
//        }
//        return description.toString();
//    }
//
//    public String toDescriptionTreeShort(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        return indentation + toString() + System.lineSeparator();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toDescriptionXml(document));
//        Element elementParticipantPairing = document.createElement(ParticipantPairingMethod.class.getSimpleName());
//        if (participantPairingMethod != null)
//            elementParticipantPairing.setAttribute("name", participantPairingMethod.name());
//        element.appendChild(elementParticipantPairing);
//
//        Element elementParticipantFilteringMethod = document.createElement(ParticipantFilteringMethod.class.getSimpleName());
//        if (participantFilteringMethod != null) {
//            elementParticipantFilteringMethod.setAttribute("name", participantFilteringMethod.name());
//            elementParticipantFilteringMethod.setAttribute("filteringValue", "" + filteringValue);
//            elementParticipantFilteringMethod.setAttribute("filteringUnit", "" + filteringUnit);
//        }
//        element.appendChild(elementParticipantFilteringMethod);
//
//
//        Element elementParticipantPairings = document.createElement(ParticipantPairing.class.getSimpleName() + "s");
//        if (!participantPairings.isEmpty()) {
//            for (ParticipantPairing participantPairing : participantPairings) {
//                elementParticipantPairings.appendChild(participantPairing.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantPairings);
//
//        Element elementCompetitionGroups = document.createElement(CompetitionGroup.class.getSimpleName() + "s");
//        if (!competitionGroups.isEmpty()) {
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                elementCompetitionGroups.appendChild(competitionGroup.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementCompetitionGroups);
//
//        Set<CompetitionSeed> competitionSeeds = new HashSet<>();
//        if (!competitionGroups.isEmpty()) {
//            Element elementcompetitionSeedNexts = document.createElement(CompetitionSeed.class.getSimpleName() + "s");
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null && !competitionSeeds.contains(competitionGroup.getCompetitionGroupResult().competitionSeedNext)) {
//                    elementcompetitionSeedNexts.appendChild(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().toDescriptionXml(document));
//                    competitionSeeds.add(competitionGroup.getCompetitionGroupResult().competitionSeedNext);
//                }
//            }
//            element.appendChild(elementcompetitionSeedNexts);
//        }
//
//        return element;
//    }
//
//    public Element toDescriptionXmlShort(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.appendChild(internationalizedLabel.toDescriptionXml(document));
//        return element;
//    }
//
//    public Element toSimpleDescriptionXml(Document document, boolean withResult) {
//
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
////        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
////            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//        if (participantPairingMethod != null)
//            element.setAttribute("participantPairingMethod", participantPairingMethod.name());
//        if (participantFilteringMethod != null) {
//            element.setAttribute("filteringMethod", participantFilteringMethod.name());
//            if (participantFilteringMethod.compareTo(ParticipantFilteringMethod.ALL) != 0) {
//                element.setAttribute("filteringValue", "" + filteringValue);
//                element.setAttribute("filteringUnit", "" + filteringUnit);
//            }
//        }
//
//        element.setAttribute("numberOfGroup", "" + competitionGroups.size());
//        if (!this.participantPairings.isEmpty())
//            element.setAttribute("numberOfParticipantPairing", "" + this.participantPairings.size());
//
//
//        if (!competitionGroups.isEmpty()) {
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                element.appendChild(competitionGroup.toSimpleDescriptionXml(document, withResult));
//
//                if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0 || competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0)
//                    break;
//            }
//        }
//        if (withResult && !this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        Set<CompetitionSeed> competitionSeeds = new HashSet<>();
//        if (!competitionGroups.isEmpty()) {
//            Element nexts = document.createElement("Next");
//            for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
//                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null && !competitionSeeds.contains(competitionGroup.getCompetitionGroupResult().competitionSeedNext)) {
//                    nexts.appendChild(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().toSimpleDescriptionXml(document, withResult));
//                    competitionSeeds.add(competitionGroup.getCompetitionGroupResult().competitionSeedNext);
//                }
//            }
//            if (nexts.hasChildNodes())
//                element.appendChild(nexts);
//        }
//        return element;
//    }


//    public CompetitionMatch getCompetitionMatch(int localId) {
//        CompetitionMatch competitionMatch = null;
//        for (CompetitionGroup competitionGroup : competitionGroupsCache) {
//            competitionMatch = competitionGroup.getCompetitionMatch(localId);
//            if (competitionMatch != null)
//                break;
//        }
//        return competitionMatch;
//    }

    @Override
    public String toString() {
        return "CompetitionSeed{" +
                "localId=" + localId +
                ",name=" + internationalizedLabel.defaultLabel +
                ",status=" + competitionObjectStatus +
                ",participantPairingMethod=" + participantPairingMethod +
                ",participantFilteringMethod=" + participantFilteringMethod +
                ",filteringValue=" + filteringValue +
                ",filteringUnit=" + filteringUnit +
                '}';
    }

    @Override
    public void updateResultDependencies() throws CompetitionInstanceGeneratorException {
        if (isSubParticipantResultsSet() && this.isSubClosed()) {
            setChanged();
            notifyObservers(this.localId);
            fillCompetitionSeedResultFromCompetitionGroups();
            if (this.isSubClosed()) {
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                this.close();
            }

        }
    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            super.close();
            boolean hasFinalReset = this.hasFinalReset();
            boolean reset = this.checkFinalReset();

            if (hasFinalReset && !reset) {
                removeFinalReset();
                if (this.competitionPhase != null) {
                    this.competitionPhase.updateResultDependencies();
//                } else {
//                    if (this.competitionInstance.getCompetitionSeed().compareTo(this) != 0)
//                        this.competitionInstance.updateResultDependencies();
                }
            }
            if (nextCompetitionSeeds == null || nextCompetitionSeeds.isEmpty()) {
                if (this.competitionPhase != null) {
                    this.competitionPhase.updateResultDependencies();
//                } else {
//                    if (this.competitionInstance.getCompetitionSeed().compareTo(this) != 0)
//                        this.competitionInstance.updateResultDependencies();
                }
            } else {
                Set<CompetitionSeed> competitionSeedsSamePhase = new HashSet<>();
                for (CompetitionSeed competitionSeed : nextCompetitionSeeds) {
                    if (competitionSeed.competitionPhase.compareTo(this.competitionPhase) == 0) {
                        competitionSeedsSamePhase.add(competitionSeed);
                    }
                }
                if (!competitionSeedsSamePhase.isEmpty()) {
                    for (CompetitionSeed competitionSeed : competitionSeedsSamePhase) {
                        competitionSeed.initializeSeed();
                        competitionSeed.open();
                    }
                } else {
                    if (this.competitionPhase != null) {
                        this.competitionPhase.updateResultDependencies();
//                    } else {
//                        if (this.competitionInstance.getCompetitionSeed().compareTo(this) != 0)
//                            this.competitionInstance.updateResultDependencies();
                    }
                }
            }
        }
    }

    public void removeFinalReset() {
        this.competitionGroups.first().getCompetitionGroupResult().competitionSeedNext.remove();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void remove() {
        List<CompetitionGroup> competitionGroups = Arrays.asList(this.competitionGroups.toArray(new CompetitionGroup[this.competitionGroups.size()]));
        int indexSeed = 0;
        int index = 0;
        this.setCompetitionObjectStatus(CompetitionObjectStatus.CANCELLED);
        for (CompetitionGroupResult competitionGroupResultPrevious : this.competitionGroupResultsPrevious) {
            CompetitionSeed competitionSeedPrevious = competitionGroupResultPrevious.competitionGroup.competitionSeed;

            for (CompetitionGroup competitionGroup : competitionSeedPrevious.competitionGroups) {
                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null &&
                        competitionGroup.getCompetitionGroupResult().competitionSeedNext.compareTo(this) == 0) {
                    competitionGroup.getCompetitionGroupResult().competitionSeedNext = null;
                    if (!competitionGroups.isEmpty()) {
                        int groupIndex = indexSeed * competitionGroups.size() / this.competitionGroupResultsPrevious.size() + index % (int) (Math.ceil((double) competitionGroups.size() / (double) this.competitionGroupResultsPrevious.size()));
                        if (competitionGroups.get(groupIndex).competitionGroupResult != null) {
                            competitionGroup.getCompetitionGroupResult().competitionSeedNext = competitionGroups.get(groupIndex).competitionGroupResult.competitionSeedNext;
//                            if(competitionGroups.get(groupIndex).competitionGroupResult.competitionSeedNext.previousCompetitionPhases != null) {
//                                competitionGroups.get(groupIndex).competitionGroupResult.competitionSeedNext.previousCompetitionPhases.remove(this);
//                                competitionGroups.get(groupIndex).competitionGroupResult.competitionSeedNext.previousCompetitionPhases.add(competitionSeedPrevious);
//                            }
                        }
                    }
                    index++;
                }
            }
            indexSeed++;
        }
        if (this.competitionGroupResultsPrevious != null)
            this.competitionGroupResultsPrevious.clear();
        if (this.nextCompetitionSeeds != null)
            this.nextCompetitionSeeds.clear();
        if (this.previousCompetitionSeeds != null)
            this.previousCompetitionSeeds.clear();
        super.remove();
        fillCompetitionMatchLink();
    }

    public boolean hasFinalReset() {
        boolean hasReset = competitionGroups.size() == 1 && competitionGroups.first().getCompetitionGroupResult().competitionSeedNext != null && competitionGroups.first().getCompetitionGroupResult().competitionSeedNext.stepType != null && competitionGroups.first().getCompetitionGroupResult().competitionSeedNext.stepType.compareTo(StepType.RESET) == 0;
//        CompetitionMatch competitionMatchFirstFinal = null;
//        if (this.competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
//            if (((CompetitionCreationParamPhaseFinal) this.competitionCreationParamPhase).resetPolicy != null || ((CompetitionCreationParamPhaseFinal) this.competitionCreationParamPhase).resetPolicy.compareTo(ResetPolicy.NONE) != 0) {
//                if (this.getPreviousCompetitionSeeds() != null && this.getPreviousCompetitionSeeds().size() == 1) {
//                    CompetitionSeed previousCompetitionSeed = this.getPreviousCompetitionSeeds().iterator().next();
//                    hasReset = previousCompetitionSeed.competitionGroups.size() > 1 && previousCompetitionSeed.competitionGroups.iterator().next().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0;
//
//                }
//            }
//        }
        return hasReset;
    }

    public boolean checkFinalReset() {
        boolean reset = false;
        if (hasFinalReset()) {
            CompetitionMatch competitionMatchFirstFinal = null;
            CompetitionSeed previousCompetitionSeed = this.getPreviousCompetitionSeeds().iterator().next();
            competitionMatchFirstFinal = this.getCompetitionGroupForLane(1).getCompetitionRoundForRound(1).getCompetitionMatches().iterator().next();
            CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
            if (competitionCreationParamPhase == null) {
                competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
            }

            int numberOfWinners = competitionCreationParamPhase.participantQualifiedPerMatch;
            Set<Participant> participantQualifiedForReset = new HashSet<>();
            int index = 0;
            for (ParticipantResult participantResult : competitionMatchFirstFinal.getParticipantResults()) {
                participantQualifiedForReset.add(participantResult.participant);
                index++;
                if (index >= numberOfWinners)
                    break;
            }

            CompetitionGroup competitionGroupWinner = previousCompetitionSeed.getCompetitionGroupForLane(1);
            index = 0;
            for (ParticipantResult participantResult : competitionGroupWinner.getCompetitionGroupResult().participantResults) {
                participantQualifiedForReset.add(participantResult.participant);
                index++;
                if (index >= numberOfWinners)
                    break;
            }
            boolean hasLoosersWinnerWins = false;
            if (participantQualifiedForReset.size() > numberOfWinners) {
                hasLoosersWinnerWins = true;
            }
            if (hasLoosersWinnerWins) {

//                CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionCreationParamPhase;
//                if (competitionCreationParamPhase == null)
//                    competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
                if (((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).resetPolicy.compareTo(ResetPolicy.WINNER_OF_LOOSER_WINS) == 0) {
                    reset = true;
                }
            }
        }
        return reset;
    }

    public Set<CompetitionMatch> getCompetitionMatchesWithParticipants(List<Participant> participants) {
        Set<CompetitionMatch> competitionMatches = new HashSet<>();
        if (competitionGroups != null) {
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionMatches.addAll(competitionGroup.getCompetitionMatchesWithParticipants(participants));
            }
        }
        return competitionMatches;
    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        for (ParticipantResult participantResult : participantResults) {
            participantScore = participantResult.findParticipantScore(localId);
            if (participantScore != null)
                break;
        }
        if (participantScore == null)

            if (!competitionGroups.isEmpty()) {
                SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//                competitionGroups = Sets.sort(competitionGroups);

                for (CompetitionGroup competitionGroup : competitionGroups) {
                    participantScore = competitionGroup.findParticipantScore(localId);
                    if (participantScore != null)
                        break;
                }
            }
        return participantScore;
    }

    public List<CompetitionSeed> getAllCompetitionSeeds() {
        List<CompetitionSeed> competitionSeeds = new ArrayList<>();
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionSeeds.addAll(competitionGroup.getAllCompetitionSeeds());

            }
        }
        return competitionSeeds;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    public void initFromXmlInputResult(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
        this.idGeneratorCache = competitionInstance.getIdGenerator();

        for (ParticipantResult participantResult : participantResults) {
            participantResult.initFromXmlInput(competitionInstance);
        }
        Sets.sort(participantResults);

    }

    public DescriptionTable toDescriptionTable() {
        DescriptionTable descriptionTable = new DescriptionTable();
        descriptionTable.append("type", this.getClass().getSimpleName());
        descriptionTable.append("localId", localId);
        descriptionTable.append("filtering", this.participantFilteringMethod);
        descriptionTable.append("pairing", this.participantPairingMethod);
        descriptionTable.append("pairingQty", this.participantPairings.size());
        descriptionTable.append("EmptyPhase", this.emptyPhase ? "yes" : "no");
        descriptionTable.append("GroupQty", this.competitionGroups.size());
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
            competitionGroups = Sets.sort(competitionGroups);
            descriptionTable.append("GroupFormat", competitionGroups.first().competitionGroupFormat);
            if (!isPreviousCompetitionGroupSeedEmpty()) {

                CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
                if (competitionCreationParamPhase == null)
                    competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
                descriptionTable.append("GroupSize", competitionGroups.first().participantPairings.size() * competitionCreationParamPhase.numberOfParticipantMatch);
            }
        }

        if (!this.emptyPhase) {
            if (!competitionGroups.isEmpty()) {

                if (this.getCompetitionGroups().iterator().next().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && this.competitionGroups.size() > 1) {
//            int roundQty = 0;
                    for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
                        descriptionTable.append(DescriptionTablePair.separator());
                        descriptionTable.append("type", CompetitionGroup.class.getSimpleName());

//                if(this.competitionGroupsCache.iterator().next().competitionRounds.size()>1) {
                        descriptionTable.append("Group" + competitionGroup.lane + "RoundQty", competitionGroup.competitionRounds.size());
                        //                }else{
//                    descriptionTable.appendTitle("GroupMerge" + competitionGroup.round + "RoundQty");
//                    descriptionTable.appendValue(this.competitionGroupsCache.iterator().next().competitionRounds.size());
//                }
                        if (!competitionGroup.competitionRounds.isEmpty()) {
//                            Sets.sort(competitionGroup.competitionRounds);

                            descriptionTable.append("MatchQtyStart", competitionGroup.competitionRounds.first().competitionMatches.size());
                            descriptionTable.append("PlayQtyStart", competitionGroup.competitionRounds.first().competitionMatches.first().competitionPlays.size());
                            descriptionTable.append("MatchQtyEnd", competitionGroup.getCompetitionRounds().last().competitionMatches.size());
                            descriptionTable.append("PlayQtyEnd", competitionGroup.getCompetitionRounds().last().competitionMatches.first().competitionPlays.size());
                        }
                    }
                }
            } else {
                descriptionTable.append(DescriptionTablePair.separator());
                descriptionTable.append("type", CompetitionGroup.class.getSimpleName());
                if (!competitionGroups.isEmpty()) {
                    descriptionTable.append("RoundQty", this.getCompetitionGroups().iterator().next().competitionRounds.size());
                    if (this.getCompetitionGroups().iterator().next().competitionRounds.size() > 0) {
                        descriptionTable.append("MatchQty", this.getCompetitionGroups().iterator().next().getCompetitionRounds().first().getCompetitionMatches().size());
                        descriptionTable.append("PlayQty", this.getCompetitionGroups().iterator().next().getCompetitionRounds().first().getCompetitionMatches().first().getCompetitionPlays().size());
                    }
                }
            }
        }
        for (CompetitionSeed competitionSeed : this.computeNextCompetitionSeeds()) {
//            if (competitionSeed.competitionGroupResultPrevious != null)
//                descriptionTable.append(competitionSeed.competitionGroupResultPrevious.toDescriptionTable());
//            else
            descriptionTable.append(competitionSeed.toDescriptionTable());
        }
        return descriptionTable;
    }

    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            if (!competitionGroups.isEmpty()) {
                for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                    overForParticipant = competitionGroup.isOverForParticipant(participant);
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
        return competitionInstance;
    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionInstance != null ? competitionInstance.localId : null;
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        for (CompetitionGroupResult competitionGroupResult : this.getCompetitionGroupResultsPrevious())
            competitionObjectWithResults.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
        if (this.getCompetitionGroupResultsPrevious() != null) {
            for (CompetitionGroupResult competitionGroupResult : this.getCompetitionGroupResultsPrevious()) {
                if (!competitionObjectWithResults.contains(competitionGroupResult.getCompetitionGroup().getCompetitionSeed()))
                    competitionObjectWithResults.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed());
            }
        }
        return competitionObjectWithResults;
    }

    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        for (CompetitionGroup competitionGroup : this.getCompetitionGroups()) {
            CompetitionGroupResult competitionGroupResult = competitionGroup.competitionGroupResult;
            if (competitionGroupResult.getCompetitionSeedNext() != null) {
                if (!competitionObjectWithResults.contains(competitionGroupResult.getCompetitionSeedNext()))
                    competitionObjectWithResults.add(competitionGroupResult.getCompetitionSeedNext());

            }
        }
        return competitionObjectWithResults;
    }

    public SortedSet<CompetitionGroupResult> getAllCompetitionGroupResults() {
        SortedSet<CompetitionGroupResult> competitionGroupResults = new TreeSet<>();
//        List<String> competitionGroupResultsId = new ArrayList<>();

        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (!competitionGroupResults.contains(competitionGroup.competitionGroupResult)) {
                    competitionGroupResults.add(competitionGroup.getCompetitionGroupResult());
//                    competitionGroupResultsId.add(competitionGroup.competitionGroupResult);
                }
            }
            List<CompetitionSeed> competitionSeedsId = new ArrayList<>();
            for (CompetitionGroup competitionGroup : competitionGroups) {
                if (competitionGroup.getCompetitionGroupResult().competitionSeedNext != null && !competitionSeedsId.contains(competitionGroup.getCompetitionGroupResult().competitionSeedNext)) {
                    competitionGroupResults.addAll(competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext().getAllCompetitionGroupResults());
                    competitionSeedsId.add(competitionGroup.getCompetitionGroupResult().competitionSeedNext);
                }
            }
        }
        return competitionGroupResults;
    }

    public CompetitionSeedTree getCompetitionSeedTree() {
        CompetitionSeedTree competitionSeedTree = new CompetitionSeedTree();
        competitionSeedTree.internationalizedLabel = internationalizedLabel;
        competitionSeedTree.localId = getLocalId();
        competitionSeedTree.databaseId = databaseId;
        competitionSeedTree.participantFilteringMethod = participantFilteringMethod;
        competitionSeedTree.participantPairingMethod = participantPairingMethod;
        competitionSeedTree.filteringUnit = filteringUnit;
        competitionSeedTree.filteringValue = filteringValue;
        competitionSeedTree.expectedDuration = this.expectedDuration;
        competitionSeedTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionSeedTree.expectedRelativeEndTime = this.expectedRelativeEndTime;
//        if (phase != null)
//            competitionSeedTree.phase = phase;
//        else
//            competitionSeedTree.phase = competitionPhase.round;
        competitionSeedTree.stepType = stepType;

        competitionSeedTree.over = isParticipantResultsSet();
        competitionSeedTree.filled = !participantPairings.isEmpty();

        for (CompetitionSeed nextCompetitionSeed : computeNextCompetitionSeeds()) {
            competitionSeedTree.nextCompetitionSeedIds.add(nextCompetitionSeed.localId);
        }
        for (CompetitionSeed previousCompetitionSeed : computePreviousCompetitionSeeds()) {
            competitionSeedTree.previousCompetitionSeedIds.add(previousCompetitionSeed.localId);
        }
        if (!competitionGroups.isEmpty()) {
            SortedSet<CompetitionGroup> competitionGroups = getCompetitionGroups();
//            competitionGroups = Sets.sort(competitionGroups);

            for (CompetitionGroup competitionGroup : competitionGroups)
                competitionSeedTree.competitionGroupTrees.add(competitionGroup.getCompetitionGroupTree());
        }
        Set<CompetitionSeed> competitionSeedTreeIdsDone = new HashSet<>();
        for (CompetitionGroupResult competitionGroupResultPrevious : this.getCompetitionGroupResultsPrevious()) {
            if (competitionGroupResultPrevious.competitionGroup != null && competitionGroupResultPrevious.getCompetitionGroup().competitionSeed != null) {
                if (!competitionSeedTreeIdsDone.contains(competitionGroupResultPrevious.getCompetitionGroup().competitionSeed)) {
                    competitionSeedTreeIdsDone.add(competitionGroupResultPrevious.getCompetitionGroup().competitionSeed);
                    if (!competitionGroupResultPrevious.getCompetitionGroup().getCompetitionSeed().competitionGroups.isEmpty() && !competitionGroupResultPrevious.getCompetitionGroup().getCompetitionSeed().competitionGroups.first().competitionRounds.isEmpty())
                        competitionSeedTree.competitionSeedTrees.add(competitionGroupResultPrevious.getCompetitionGroup().getCompetitionSeed().getCompetitionSeedTree());
                }
            }
        }
        ParticipantResultTree participantResultTree = null;
        if (participantResults != null) {
            for (ParticipantResult participantResult : participantResults) {
                participantResultTree = participantResult.toParticipantResultTree(false, new HashSet<>());
                participantResultTree.points =
                        participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue();
                competitionSeedTree.participantResultTrees.add(participantResultTree);
            }
        }

        return competitionSeedTree;
    }

    @Override
    public SortedSet<CompetitionGroup> getSubCompetitionObjectWithResults() {
        return getCompetitionGroups();
    }

    @Override
    public CompetitionPhase getUpperCompetitionObjectWithResult() {
        return this.getCompetitionPhase();
    }

    public CompetitionPhase getCompetitionPhase() {
        return competitionPhase;
    }

//    @Override
//    public SortedSet<CompetitionPlay> getCompetitionPlays(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<CompetitionPlay>();
//        if (participantId == null || isForParticipant(participantId)) {
//            for (CompetitionObjectWithResult competitionObjectWithResult : getSubCompetitionObjectWithResults())
//                competitionPlays.addAll(competitionObjectWithResult.getCompetitionPlays(initialized, open, closed, participantId, recursive));
//        }
//        return competitionPlays;
//    }

    @Override
    public boolean isParticipantPairingDefined() {
        return participantPairings != null && !participantPairings.isEmpty();
    }

    @Override
    public boolean isClosed() {
        return isSubClosed();
    }

    public void clearDatabaseId() {
        this.databaseId = null;
        if (getCompetitionGroups() != null)
            for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                competitionGroup.clearDatabaseId();
            }
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }
        if (participantPairings != null)
            for (ParticipantPairing participantPairing : participantPairings) {
                participantPairing.clearDatabaseId();
            }
    }

    @Override
    public CompetitionSeed cloneSimplified() {
        CompetitionSeed competitionSeed = null;
        try {
            competitionSeed = (CompetitionSeed) this.clone();
//            competitionSeed.setCompetitionInstance(getCompetitionInstance());
//            competitionSeed.fillPairingCache();
//            competitionSeed.fillResultCache();

//            competitionSeed.competitionGroupsCache = new TreeSet<>();

            if (this.participantResults != null) {
                competitionSeed.participantResults = new TreeSet<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    competitionSeed.participantResults.add(participantResult.cloneSimplified());
                }
                Sets.sort(competitionSeed.participantResults);
            }
        } catch (CloneNotSupportedException e) {
        }
        return competitionSeed;
    }


    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
//        this.competitionInstanceId = competitionInstance.localId;
    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        if (!expectedGlobalDurationStatisticsStructureInitialized) {
            expectedGlobalDurationStatisticsStructureInitialized = true;
            if (!emptyPhase) {
                CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
                if (competitionCreationParamPhase == null)
                    competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
                expectedGlobalDurationStatisticsStructure.min = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(getExpectedGlobalStep().min).toMinutes();
                expectedGlobalDurationStatisticsStructure.max = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(getExpectedGlobalStep().max).toMinutes();
                expectedGlobalDurationStatisticsStructure.avg = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(getExpectedGlobalStep().avg).toMinutes();
            } else {
                expectedGlobalDurationStatisticsStructure.min = 0L;
                expectedGlobalDurationStatisticsStructure.max = 0L;
                expectedGlobalDurationStatisticsStructure.avg = 0L;
            }
        }


        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayStatisticsStructureInitialized || (competitionGroups == null || competitionGroups.isEmpty())) {
            expectedGlobalPlayStatisticsStructureInitialized = true;
            expectedGlobalPlayStatisticsStructure.max = 0L;
            expectedGlobalPlayStatisticsStructure.min = 0L;
            expectedGlobalPlayStatisticsStructure.avg = 0L;
            if (getCompetitionGroups() != null && !isPreviousCompetitionGroupSeedEmpty())
                for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                    expectedGlobalPlayStatisticsStructure.max += competitionGroup.getExpectedGlobalPlay().max;
                    expectedGlobalPlayStatisticsStructure.avg += competitionGroup.getExpectedGlobalPlay().avg;
                    expectedGlobalPlayStatisticsStructure.min += competitionGroup.getExpectedGlobalPlay().min;
                }

        }
        return expectedGlobalPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {
        if (!expectedParticipantDurationStatisticsStructureInitialized) {
            expectedParticipantDurationStatisticsStructureInitialized = true;
            if (!emptyPhase) {

                CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
                if (competitionCreationParamPhase == null)
                    competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
                StatisticsStructure expectedParticipantPlay = getExpectedParticipantPlay();
                expectedParticipantDurationStatisticsStructure.min = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(expectedParticipantPlay.min).toMinutes();
                expectedParticipantDurationStatisticsStructure.max = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(expectedParticipantPlay.max).toMinutes();
                expectedParticipantDurationStatisticsStructure.avg = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(expectedParticipantPlay.avg).toMinutes();
            } else {
                expectedParticipantDurationStatisticsStructure.min = 0L;
                expectedParticipantDurationStatisticsStructure.max = 0L;
                expectedParticipantDurationStatisticsStructure.avg = 0L;

            }
        }


        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        if (!expectedParticipantPlayStatisticsStructureInitialized) {
            expectedParticipantPlayStatisticsStructureInitialized = true;
            if (!emptyPhase) {
                CompetitionGroup competitionGroupFirst = competitionGroups.first();

                switch (competitionGroupFirst.competitionGroupFormat) {
                    case ELIMINATION:
                        expectedParticipantPlayStatisticsStructure.min = 0L;
                        expectedParticipantPlayStatisticsStructure.max = 0L;
                        expectedParticipantPlayStatisticsStructure.avg = 0L;

                        if (!competitionGroups.isEmpty()) {
                            for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                                Sets.sort(competitionGroup.competitionRounds);
                                if (competitionGroup.lane == 1)
                                    expectedParticipantPlayStatisticsStructure.max += competitionGroup.getExpectedParticipantPlay().max;
                                else
                                    expectedParticipantPlayStatisticsStructure.max += competitionGroup.getCompetitionRounds().last().getExpectedParticipantPlay().max;
                                expectedParticipantPlayStatisticsStructure.min += competitionGroup.getCompetitionRounds().first().getExpectedParticipantPlay().min;
                            }
                            expectedParticipantPlayStatisticsStructure.avg = (expectedParticipantPlayStatisticsStructure.min + expectedParticipantPlayStatisticsStructure.max) / 2;
                        }
                        break;
                    case NONE:
                        expectedParticipantPlayStatisticsStructure.min = 0L;
                        expectedParticipantPlayStatisticsStructure.max = 0L;
                        expectedParticipantPlayStatisticsStructure.avg = 0L;
                        break;
                    case CUSTOM:
                    case ROUND_ROBIN:
                    case LADDER:
                    case SWISS:
                        if (!competitionGroups.isEmpty()) {
                            for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                                if (expectedParticipantPlayStatisticsStructure.max == null || expectedParticipantPlayStatisticsStructure.max.compareTo(competitionGroup.getExpectedParticipantPlay().max) < 0)
                                    expectedParticipantPlayStatisticsStructure.max = competitionGroup.getExpectedParticipantPlay().max;
                                if (expectedParticipantPlayStatisticsStructure.avg == null || expectedParticipantPlayStatisticsStructure.avg.compareTo(competitionGroup.getExpectedParticipantPlay().avg) < 0)
                                    expectedParticipantPlayStatisticsStructure.avg = competitionGroup.getExpectedParticipantPlay().avg;
                                if (expectedParticipantPlayStatisticsStructure.min == null || expectedParticipantPlayStatisticsStructure.min.compareTo(competitionGroup.getExpectedParticipantPlay().min) > 0)
                                    expectedParticipantPlayStatisticsStructure.min = competitionGroup.getExpectedParticipantPlay().min;
                            }
                            expectedParticipantPlayStatisticsStructure.avg = expectedParticipantPlayStatisticsStructure.avg / competitionGroups.size();
                        }
                        break;
                }
            } else {
                expectedParticipantPlayStatisticsStructure.min = 0L;
                expectedParticipantPlayStatisticsStructure.max = 0L;
                expectedParticipantPlayStatisticsStructure.avg = 0L;

            }
        }


        return expectedParticipantPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        if (!expectedGlobalStepStatisticsStructureInitialized) {
            expectedGlobalStepStatisticsStructureInitialized = true;
            if (!emptyPhase) {

                CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
                if (competitionCreationParamPhase == null)
                    competitionCreationParamPhase = competitionPhase.competitionCreationParamPhase;
                int maximumNumberOfParallelPlay = competitionCreationParamPhase.maximumNumberOfParallelPlay;
                Long max = null;
                Long min = null;
                Long avg = null;
                if (maximumNumberOfParallelPlay > 0) {
                    max = 0L;
                    min = 0L;
                    avg = 0L;
                    if (!competitionGroups.isEmpty()) {
                        for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                            max += competitionGroup.getExpectedGlobalStep().max;
                            avg += competitionGroup.getExpectedGlobalStep().avg;
                            min += competitionGroup.getExpectedGlobalStep().min;
                        }
                    }
                } else {
                    if (!competitionGroups.isEmpty()) {
                        max = 0L;
                        min = 0L;
                        avg = 0L;
                        for (CompetitionGroup competitionGroup : getCompetitionGroups()) {
                            if (competitionGroup.lane > 1 && competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                                max++;
                                avg++;
                                min++;
                            } else {
                                if (max == 0 || competitionGroup.getExpectedGlobalStep().max.compareTo(max) > 0)
                                    max = competitionGroup.getExpectedGlobalStep().max;
                                if (avg == 0 || competitionGroup.getExpectedGlobalStep().avg.compareTo(avg) > 0)
                                    avg = competitionGroup.getExpectedGlobalStep().avg;
                                if (min == 0 || competitionGroup.getExpectedGlobalStep().min.compareTo(min) < 0)
                                    min = competitionGroup.getExpectedGlobalStep().min;
                            }
                        }
                    }
                }
                expectedGlobalStepStatisticsStructure.min = min;
                expectedGlobalStepStatisticsStructure.max = max;
                expectedGlobalStepStatisticsStructure.avg = avg;
            } else {
                expectedGlobalStepStatisticsStructure.min = 0L;
                expectedGlobalStepStatisticsStructure.max = 0L;
                expectedGlobalStepStatisticsStructure.avg = 0L;
            }
        }
        return expectedGlobalStepStatisticsStructure;
    }

    @Override
    protected Integer getRoundOrLane() {
        return this.round;
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return this.participantResults;
    }

    @Override
    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        super.setParticipantResults(participantResults);

    }

    public void addCompetitionGroup(CompetitionGroup competitionGroup) {
        competitionGroup.competitionSeed = this;
        competitionGroup.competitionPhase = this.competitionPhase;
        this.competitionGroups.add(competitionGroup);
//        this.competitionGroupsCache = null;
    }

    @Override
    public void resetCache() {
        super.resetCache();
//        this.competitionGroupResultsPreviousCache = null;
//        this.competitionGroupResultsPreviousCache = null;
//        this.competitionGroupsCache = null;
//        this.competitionGroupsCache = null;
//        this.previousParticipantPairingsCache = null;
//        this.previousParticipantResultsMapCache = null;
//        this.quantityPairingCache = null;
//        this.quantityPairingCache = null;
    }

    public List<ParticipantResult> getPreviousParticipantResults() {
        List<ParticipantResult> allPreviousParticipantResults = new ArrayList<>();
        int previousParticipantResultsSize = getPreviousParticipantResultsSize();


        SortedMap<CompetitionObjectWithResult, SortedSet<ParticipantResult>> competitionObjectWithResultSortedSetSortedMap = getPreviousParticipantResultsMap();
        if (competitionObjectWithResultSortedSetSortedMap.size() == 1)
            allPreviousParticipantResults.addAll(competitionObjectWithResultSortedSetSortedMap.values().iterator().next());
        else {
            List<List<ParticipantResult>> participantResultsArrays = new ArrayList<>();
            for (SortedSet<ParticipantResult> participantResults : competitionObjectWithResultSortedSetSortedMap.values()) {
                List participantResultsArray = new ArrayList();
                participantResultsArray.addAll(participantResults);
                participantResultsArrays.add(participantResultsArray);
            }
            while (allPreviousParticipantResults.size() < previousParticipantResultsSize) {
                for (List<ParticipantResult> participantResultsArray : participantResultsArrays) {
                    if (!participantResultsArray.isEmpty()) {
                        allPreviousParticipantResults.add(participantResultsArray.get(0));
                        participantResultsArray.remove(0);
                    }
                }
            }
        }
        return allPreviousParticipantResults;
    }

//    @Override
//    public void fillCache(boolean up, boolean down) {
//        super.fillCache(up, down);
////        for (CompetitionGroupResult competitionGroupResult : competitionGroupResultsPrevious) {
////            CompetitionSeed competitionSeed= competitionGroupResult.competitionSeed;
////            if (competitionSeed!= null)
////                competitionGroupResultsPrevious.add(competitionSeed);
////        }
////
////        for (CompetitionGroup competitionGroup: competitionGroups) {
////            CompetitionSeed competitionSeed= competitionGroup.getCompetitionGroupResult().getCompetitionSeedNext();
////            if (competitionSeed!= null)
////                nextCompetitionPhases.add(competitionSeed);
////        }
//
//    }

    public void removeParticipantTeamVoid() {
        for (ParticipantPairing participantPairing : participantPairings) {
            participantPairing.removeParticipantTeamVoid();
        }

    }

    @Override
    public void sortParticipantPairings() {
//        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
        Sets.sort(participantPairings);
//            for (ParticipantPairing participantPairing : participantPairings)
//                participantPairing.sortParticipantSeats();
//        }
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionSeed competitionSeed = new CompetitionSeed();
        competitionSeed.id = this.id;
        competitionSeed.databaseId = this.databaseId;
        competitionSeed.localId = this.localId;
        return competitionSeed;
    }


    @Override
    public void clearForContext() {
        competitionGroups = null;
        competitionGroupResultsPrevious = null;
        nextCompetitionSeeds = null;
        previousCompetitionSeeds = null;
        participantResults = null;
        participantPairings = null;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionInstance = new CompetitionInstance();
        competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;
        String competitionPhaseId = null;
        String competitionPhaseLocalId = null;
        if (competitionPhase != null) {
            competitionPhaseId = competitionPhase.id;
            competitionPhaseLocalId = competitionPhase.localId;
            competitionPhase = new CompetitionPhase();
            competitionPhase.id = competitionPhaseId;
            competitionPhase.localId = competitionPhaseLocalId;
        }
    }


    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionSeed competitionSeed = (CompetitionSeed) this.clone();
        competitionSeed.nextCompetitionSeeds = null;
        competitionSeed.previousCompetitionSeeds = null;
        competitionSeed.competitionGroupResultsPrevious = null;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;

        competitionSeed.competitionInstance = new CompetitionInstance();
        competitionSeed.competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;

        competitionSeed.competitionGroups = null;
        if (competitionSeed.getCompetitionGroups() != null) {
            competitionSeed.competitionGroups = new TreeSet<>();
            List<CompetitionGroup> competitionGroups = new ArrayList<>();
            for (CompetitionGroup competitionGroup :
                    competitionSeed.getCompetitionGroups()) {
                competitionGroups.add((CompetitionGroup) competitionGroup.cloneForContext());
            }
            competitionSeed.competitionGroups.addAll(competitionGroups);
        }
        return competitionSeed;
    }

    public void continueCompetition() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.isClosed()) {
            if (this.nextCompetitionSeeds != null) {
                for (CompetitionSeed competitionSeedNext : nextCompetitionSeeds) {
                    competitionSeedNext.continueCompetition();
                }
            }
        } else {
            this.open();
        }
    }

    public boolean isPreviousCompetitionResultClosed() {
        boolean isPreviousCompetitionResultClosed = true;
        if (this.getCompetitionGroupResultsPrevious() != null && !this.getCompetitionGroupResultsPrevious().isEmpty()) {
            for (CompetitionGroupResult competitionGroupResultPrevious : this.getCompetitionGroupResultsPrevious()) {
                if (competitionGroupResultPrevious.getCompetitionGroup() != null && !competitionGroupResultPrevious.getCompetitionGroup().isClosed()) {
                    isPreviousCompetitionResultClosed = false;
                    break;
                }

            }
        }
        return isPreviousCompetitionResultClosed;
    }

    public void initializeSeed() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
            if (this.emptyPhase && (this.competitionGroupResultsPrevious == null || this.competitionGroupResultsPrevious.isEmpty())) {
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                if (this.competitionPhase != null) {
                    this.competitionPhase.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                }
                if (!competitionGroups.isEmpty()) {
//                    Sets.sort(competitionGroups);
                    for (CompetitionGroup competitionGroup : competitionGroups) {
                        competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                        competitionGroup.close();
                    }
                }
            } else {
                boolean previousAreClosed = true;
                if (this.competitionGroupResultsPrevious != null && !this.competitionGroupResultsPrevious.isEmpty()) {
                    for (CompetitionGroupResult competitionGroupResult : getCompetitionGroupResultsPrevious()) {
                        previousAreClosed = previousAreClosed && competitionGroupResult.getCompetitionGroup().isClosed();
                        if (!previousAreClosed)
                            break;
                    }
                }
                if (previousAreClosed) {
                    doPairing();
                }
            }
        }

    }

    public SortedSet<CompetitionSeed> getPreviousPhaseCompetitionSeeds() {
        SortedSet<CompetitionPhase> previousCompetitionPhases = this.competitionPhase.previousCompetitionPhases;
        SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
        for (CompetitionPhase previousCompetitionPhase : previousCompetitionPhases) {
            competitionSeeds.addAll(previousCompetitionPhase.getCompetitionSeeds());
        }
        competitionSeeds = Sets.sort(competitionSeeds);
        return competitionSeeds;
    }

    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
        if (competitionGroups != null) {
            for (CompetitionGroup competitionGroup : competitionGroups) {
                Map<Participant, List<Participant>> participantOpponentsPlayMap = competitionGroup.getParticipantOpponentsMap();
                for (Participant participant : participantOpponentsPlayMap.keySet()) {
                    if (!participantOpponentsMap.containsKey(participant))
                        participantOpponentsMap.put(participant, new ArrayList<>());
                    participantOpponentsMap.get(participant).addAll(participantOpponentsMap.get(participant));
                }
            }
        }
        return participantOpponentsMap;
    }

//    public void setPhase(int phase) {
//        this.phase = phase;
//        competitionInstance.updateCompetitionPhase(phase);
//    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (previousCompetitionSeeds != null && !previousCompetitionSeeds.isEmpty()) {
            for (CompetitionSeed competitionSeed : previousCompetitionSeeds) {
//                competitionSeed.fillExpectedRelativeTime();
                if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionSeed.expectedRelativeEndTime) < 0)
                    expectedRelativeStartTime = competitionSeed.expectedRelativeEndTime;
            }
        } else {
            expectedRelativeStartTime = this.competitionPhase.expectedRelativeStartTime;
        }
        if (competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(competitionPhase.competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            expectedRelativeEndTime = competitionPhase.expectedRelativeEndTime;
            for (CompetitionGroup competitionGroup : competitionGroups) {
                competitionGroup.fillExpectedRelativeTime();
            }
        } else {
            if (!competitionGroups.isEmpty()) {
                for (CompetitionGroup competitionGroup : competitionGroups) {
                    competitionGroup.fillExpectedRelativeTime();
                    if (expectedRelativeEndTime == null || expectedRelativeEndTime.compareTo(competitionGroup.expectedRelativeEndTime) < 0)
                        expectedRelativeEndTime = competitionGroup.expectedRelativeEndTime;
                    if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionGroup.expectedRelativeStartTime) > 0)
                        expectedRelativeStartTime = competitionGroup.expectedRelativeStartTime;
                }

            } else {
                expectedRelativeEndTime = expectedRelativeStartTime;
            }
        }
        expectedDuration = expectedRelativeEndTime.minus(expectedRelativeStartTime);
    }

    public SortedSet<CompetitionMatch> getCompetitionMatchForPhaseSequence(Integer phaseSequence) {
        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
        if (this.competitionGroups != null)
            for (CompetitionGroup competitionGroup : this.competitionGroups) {
                competitionMatches.addAll(competitionGroup.getCompetitionMatchForPhaseSequence(phaseSequence));
            }

        return competitionMatches;
    }

    public SortedSet<CompetitionRound> getCompetitionRoundsForDate(Duration relativeDuration) {
        SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
        if (this.competitionGroups != null)
            for (CompetitionGroup competitionGroup : this.competitionGroups) {
                competitionRounds.addAll(competitionGroup.getCompetitionRoundsForDate(relativeDuration));
            }

        return competitionRounds;
    }

    public boolean isRegistrationOnTheFly() {
        boolean result = false;

        if (competitionPhase.competitionCreationParamPhase.registrationOnTheFly != null && competitionPhase.competitionCreationParamPhase.registrationOnTheFly && competitionGroups != null) {
            for (CompetitionGroup competitionGroup : competitionGroups) {
                result = competitionGroup.isOpen() && competitionGroup.isRegistrationOnTheFly();
                if (result)
                    break;
            }
        }

        return result;

    }


    public List<Participant> getRealParticipantsAsArray() {
        Set<Participant> participantSet = new HashSet<>();
        if (!competitionGroups.isEmpty() && competitionGroups.iterator().next().competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
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

    @Override
    public void reset(boolean recursive) {
        if (this.participantSeats != null)
            this.participantSeats.clear();
        super.reset(recursive);

    }

    public Set<CompetitionMatch> getCompetitionMatches() {
        Set<CompetitionMatch> competitionMatches = new HashSet<>();
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionMatches.addAll(competitionGroup.getCompetitionMatches());
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
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.participant != null && participantResult.participant.compareTo(participant) == 0) {
                participantResults.remove(participantResult);
                break;
            }
        }
    }

    public final ParticipantResult addParticipantSeat(ParticipantSeat participantSeat) {
        ParticipantResult participantResult = null;
        participantSeats.add(participantSeat);
        if (participantSeat.participant != null) {
            boolean createIfNorExists = true;
            participantResult = getParticipantResultFor(participantSeat.participant, createIfNorExists);
        }
        this.hasChanged();

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

    @Override
    public void addParticipantPairing(ParticipantPairing participantPairing) {
        super.addParticipantPairing(participantPairing);
        for (ParticipantSeat participantSeat :
                participantPairing.getParticipantSeatsAsArray()) {
            if (participantSeat.participant != null && !(participantSeat.participant instanceof ParticipantTeamVoid) && this.getParticipantSeat(participantSeat.participant) == null) {
                this.addParticipantSeat(participantSeat);
            }
        }
    }


    public boolean reduceCompetitionPhaseFormatToPreviousRegistrations() throws CompetitionInstanceGeneratorException {
        SortedSet<ParticipantResult> participantResults = this.participantFilteringMethod.filterParticipantResults(this.getPreviousParticipantResults(), this.filteringValue, this.filteringUnit);
        int expectedParticipantQuantity = 0;
        boolean reduced = false;
        for (CompetitionGroup competitionGroup : competitionGroups) {
            expectedParticipantQuantity += competitionGroup.expectedParticipantQuantity;
        }
        if (participantResults.size() != expectedParticipantQuantity) {
            reduced = true;
            CompetitionInstanceGeneratorImpl competitionInstanceGenerator = new CompetitionInstanceGeneratorImpl();
            CompetitionCreationParamPhase competitionCreationParamPhase = this.competitionPhase.competitionCreationParamPhase;
            ParticipantType participantType = competitionCreationParamPhase.participantType;
            int numberOfParticipantPerMatch = competitionCreationParamPhase.numberOfParticipantMatch;
            PlayVersusType playVersusType = competitionCreationParamPhase.playVersusType;
            int numberOfParticipant = participantResults.size();
            int minNumberOfParticipantGroup = participantResults.size();
            int maxNumberOfParticipantGroup = participantResults.size();
            int participantQuantityOut = competitionCreationParamPhase.participantQualifiedPerMatch;
            int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
            boolean allowEvenNumber = competitionCreationParamPhase.allowEvenNumberOfPlay != null && competitionCreationParamPhase.allowEvenNumberOfPlay;
            Integer numberOfRoundMinimum = 1;
            Integer numberOfRoundMaximum = participantResults.size();
            boolean thirdPlaceMatch = competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal ? ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).thirdPlaceMatchEnabled : false;
            boolean fixedParticipantSize = competitionCreationParamPhase.participantTypeSplittable != null && !competitionCreationParamPhase.participantTypeSplittable;
            boolean participantSplittable = competitionCreationParamPhase.participantTypeSplittable == null || competitionCreationParamPhase.participantTypeSplittable;
            int numberOfPlayPerMatchMinimum = competitionCreationParamPhase.numberOfPlayMinimum;
            int numberOfPlayPerMatchMaximum = competitionCreationParamPhase.numberOfPlayMaximum;
            boolean finalGroupSizeThresholdEnabled = false;
            int finalGroupSizeThreshold = 0;
            int numberOfPlayPerMatchMinimumFinalGroup = competitionCreationParamPhase.numberOfPlayMinimum;
            int numberOfPlayPerMatchMaximumFinalGroup = competitionCreationParamPhase.numberOfPlayMaximum;
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
                CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
                finalGroupSizeThresholdEnabled = competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled;
                if (competitionCreationParamPhaseFinal.numberOfParticipantMatch != null)
                    finalGroupSizeThreshold = competitionCreationParamPhaseFinal.numberOfParticipantMatch;
                if (competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null)
                    numberOfPlayPerMatchMinimumFinalGroup = competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum;
                if (competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null)
                    numberOfPlayPerMatchMaximumFinalGroup = competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum;
            }

            Set<CompetitionPhase> nextCompetitionPhases = new HashSet<>();
            if (competitionPhase.nextCompetitionPhases != null) {
                nextCompetitionPhases.addAll(competitionPhase.nextCompetitionPhases);
            }
            if (isElimination()) {
                int numberOfGroups = this.competitionGroups.size();
                int numberOfBrackets = TournamentFormat.getMaximumBrackets(participantResults.size(), numberOfParticipantPerMatch);
                if (numberOfBrackets < numberOfGroups)
                    numberOfGroups = numberOfBrackets;

                CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.ELIMINATION.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, numberOfGroups, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumber, numberOfRoundMinimum, numberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch, fixedParticipantSize, participantSplittable);

                List<CompetitionGroup> competitionGroupsReversed = new ArrayList<>(competitionGroups);
                Collections.reverse(competitionGroupsReversed);
                for (CompetitionGroup competitionGroup : competitionGroupsReversed) {
                    competitionGroup.delete();
                }


                competitionInstanceGenerator.createCompetitionGroupsFromCompetitionGroupFormatTree(this.getCompetitionInstance(), this, competitionGroupFormatTree);
                boolean generation = true;
                if (competitionPhase.nextCompetitionPhases != null) {
                    competitionPhase.nextCompetitionPhases.clear();
                }
                competitionPhase.continueCompetition();

                competitionPhase.recompute();
                competitionPhase.fillCompetitionMatchLink();


                competitionInstanceGenerator.initEliminationPhaseMerges(this.getCompetitionInstance(), this, this.competitionGroups.size(), competitionCreationParamPhase, competitionPhase.phaseType);
                competitionPhase.reset(true);
//                if (!nextCompetitionPhases.isEmpty())
//                    competitionPhase.nextCompetitionPhases.addAll(nextCompetitionPhases);
//                for (CompetitionPhase competitionPhase : nextCompetitionPhases) {
//                    competitionPhase.reduceCompetitionPhaseFormatToPreviousRegistrations();
//                }
//                competitionPhase.reset(true);
//                for (CompetitionPhase competitionPhase : nextCompetitionPhases) {
//                    competitionPhase.reset(true);
//                }
                competitionInstance.resetParticipantResults();
//                competitionPhase.initializePhase(false);
//                competitionPhase.disableGenerationFlag();


//            int index = 0;

//            List<CompetitionGroup> competitionGroupsToDelete = new ArrayList<>();
//            for (CompetitionGroup competitionGroup : competitionGroups) {
//                index++;
//                if (index > numberOfGroups)
//                    competitionGroupsToDelete.add(0, competitionGroup);
//
//            }

            } else {

                CompetitionGroupFormat competitionGroupFormat = competitionGroups.first().competitionGroupFormat;

                int numberOfGroups = competitionGroupFormat.getNumberOfGroups(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, minNumberOfParticipantGroup, maxNumberOfParticipantGroup, participantQuantityOut, participantQualifiedPerMatch, allowEvenNumber, numberOfRoundMinimum, numberOfRoundMaximum, thirdPlaceMatch, fixedParticipantSize, participantSplittable);

                CompetitionGroupFormatTree competitionGroupFormatTree = competitionGroupFormat.getCompetitionGroupFormatTree(participantType, numberOfParticipantPerMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, numberOfGroups, numberOfPlayPerMatchMinimum, numberOfPlayPerMatchMaximum, allowEvenNumber, numberOfRoundMinimum, numberOfRoundMaximum, finalGroupSizeThresholdEnabled, finalGroupSizeThreshold, numberOfPlayPerMatchMinimumFinalGroup, numberOfPlayPerMatchMaximumFinalGroup, thirdPlaceMatch, fixedParticipantSize, participantSplittable);

                List<CompetitionGroup> competitionGroupsReversed = new ArrayList<>(competitionGroups);
                Collections.reverse(competitionGroupsReversed);
                for (CompetitionGroup competitionGroup : competitionGroupsReversed) {
                    competitionGroup.delete();
                }


                competitionInstanceGenerator.createCompetitionGroupsFromCompetitionGroupFormatTree(this.getCompetitionInstance(), this, competitionGroupFormatTree);
                boolean generation = true;
                if (competitionPhase.nextCompetitionPhases != null) {
                    competitionPhase.nextCompetitionPhases.clear();
                }
                competitionPhase.continueCompetition();
                if (competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) != 0) {
                    competitionPhase.recompute();
                    competitionPhase.fillCompetitionMatchLink();
                }
                competitionInstance.resetParticipantResults();
//                competitionPhase.reset(true);
//                for (CompetitionPhase competitionPhase : nextCompetitionPhases) {
//                    competitionPhase.reset(true);
//                }
//                competitionInstance.resetParticipantResults();
//                competitionPhase.initializePhase(false);
            }

            competitionPhase.disableGenerationFlag();

            if (!nextCompetitionPhases.isEmpty())
                competitionPhase.nextCompetitionPhases.addAll(nextCompetitionPhases);

            for (CompetitionPhase competitionPhase : nextCompetitionPhases) {
                List<CompetitionGroupResult> competitionGroupResultToRemove = new ArrayList<>();
                if (competitionPhase.getCompetitionSeeds() != null && !competitionPhase.getCompetitionSeeds().isEmpty()) {
                    for (CompetitionGroupResult competitionGroupResult : competitionPhase.getCompetitionSeeds().first().getCompetitionGroupResultsPrevious()) {
                        if (competitionGroupResult != null && competitionGroupResult.deleted)
                            competitionGroupResultToRemove.add(competitionGroupResult);
                    }
                    competitionPhase.getCompetitionSeeds().first().getCompetitionGroupResultsPrevious().removeAll(competitionGroupResultToRemove);
                    for (CompetitionGroup competitionGroup : this.competitionPhase.competitionSeeds.last().competitionGroups) {
                        if (competitionGroup.competitionGroupResult != null)
                            competitionPhase.getCompetitionSeeds().first().getCompetitionGroupResultsPrevious().add(
                                    competitionGroup.competitionGroupResult);
                    }
                }
            }

            if (competitionPhase.nextCompetitionPhases != null && !competitionPhase.nextCompetitionPhases.isEmpty()) {
                for (CompetitionPhase competitionPhase : competitionPhase.nextCompetitionPhases) {
                    competitionPhase.reduceCompetitionPhaseFormatToPreviousRegistrations();
                }
            }
        }
        return reduced;
    }

    public boolean isElimination() {
        boolean isElimination = false;
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                isElimination = true;
                break;
            }
        }
        return isElimination;
    }

    @Override
    public void delete() {
        super.delete();
        this.competitionPhase.competitionSeeds.remove(this);
        this.competitionGroupResultsPrevious.clear();
//        for (CompetitionGroup competitionGroup: competitionGroups) {
//            competitionGroup.delete();
//        }
    }

    public void reopen() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
        this.competitionGroups.first().reopen();
    }

}

