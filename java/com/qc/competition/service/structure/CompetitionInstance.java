package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.ScoreThresholdType;
import com.qc.competition.service.structure.adaptater.DurationAdapter;
import com.qc.competition.service.structure.tree.CompetitionInstanceTree;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.CompetitionComputationParam;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.service.template.automatic.participation.optimization.PhaseType;
import com.qc.competition.utils.MathUtils;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.json.JSONObject;
import com.qc.competition.ws.simplestructure.Duration;
import org.goochjs.glicko2.Rating;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionInstance extends CompetitionObjectWithResult<CompetitionInstance, CompetitionPhase> implements XmlManagedObject, JSONObject, Simplify<CompetitionInstance>, Cloneable {
    private static final NumberFormat bibNumberFormat = NumberFormat.getInstance();
    public static String CLASS = CompetitionInstance.class.getSimpleName();
    //    public static Logger logger = Logger.getLogger(CLASS);
    //    @JsonIgnore
//    @XmlTransient
//    private CompetitionObserver competitionListener;
    @XmlAttribute(name = "duration")
    @XmlJavaTypeAdapter(type = Duration.class,
            value = DurationAdapter.class)
    public Duration competitionInstanceDuration;
    //
//    @XmlAttribute(name = "ID")
//    public String localId = "1";
    @XmlAttribute(name = "version")
    @JsonProperty("version")
    @JsonSerialize(using = VersionSerializer.class)
    @JsonDeserialize(using = VersionDeserializer.class)
    public Version version;

    @XmlElementWrapper(name = "phases")
    @XmlElement(name = "phase")
    @JsonProperty("phases")
    public SortedSet<CompetitionPhase> competitionPhases = new TreeSet<>();

    @XmlAttribute(name = "phaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;

//    @XmlAttribute(name = "competitionSeed")
//    @XmlIDREF
//    @JsonIdentityReference(alwaysAsId = true)
//    @JsonProperty("competitionSeed")
//    @Deprecated
//    public CompetitionSeed competitionSeed;

    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    //    @XmlAttribute
//    public int numberOfParticipantMatch;
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("results")
    public SortedSet<ParticipantResult> participantResults = new TreeSet<>();
    @XmlElementWrapper(name = "_seatsAll")
    @XmlElement(name = "_seat")
    @JsonProperty("_seatsAll")
    public SortedSet<ParticipantSeat> participantSeatsAll = new TreeSet<>();
    @XmlElementWrapper(name = "seats")
    @XmlElement(name = "seat")
    @JsonProperty("seats")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();
    @XmlAttribute(name = "game")
    public String game;
    @XmlAttribute(name = "editor")
    public String gameEditor;
    @XmlAttribute(name = "date")
    public Calendar startDate;
    @XmlAttribute(name = "dbDefinitionId")
    public Integer databaseDefinitionId = null;
    @XmlAttribute(name = "potValueInCents")
    public Integer potValueInCents = null;
    @XmlElement(name = "parameters")
    @JsonProperty("parameters")
    public CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
    @XmlElementWrapper(name = "seeds")
    @XmlElement(name = "seed")
    @JsonProperty("seeds")
    public SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
    @XmlElementWrapper(name = "matches")
    @XmlElement(name = "match")
    @JsonProperty("matches")
    public SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
    @XmlElementWrapper(name = "matchlinks")
    @XmlElement(name = "matchlink")
    @JsonProperty("matchlinks")
    public SortedSet<CompetitionMatchLink> competitionMatchLinks = new TreeSet<>();
    @XmlElementWrapper(name = "plays")
    @XmlElement(name = "play")
    @JsonProperty("plays")
//    @XmlTransient
    public SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    @JsonProperty("groups")
    public SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>();
    @XmlElementWrapper(name = "rounds")
    @XmlElement(name = "round")
    @JsonProperty("rounds")
    public SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
    @XmlElementWrapper(name = "groupResults")
    @XmlElement(name = "groupResult")
    @JsonProperty("groupResults")
    public SortedSet<CompetitionGroupResult> competitionGroupResults = new TreeSet<>();
    @XmlElementWrapper(name = "participantSingles")
    @XmlElement(name = "participantSingle")
    @JsonProperty("participantSingles")
    public SortedSet<ParticipantSingle> participantSingles = new TreeSet<>();
    @XmlElementWrapper(name = "participantTeams")
    @XmlElement(name = "participantTeam")
    @JsonProperty("participantTeams")
    public SortedSet<ParticipantTeam> participantTeams = new TreeSet<>();
    @XmlElementWrapper(name = "participants")
    @XmlElement(name = "localId")
    @JsonProperty("participants")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<Participant> participants = new TreeSet<>();
    @XmlElementWrapper(name = "pairings")
    @XmlElement(name = "pairing")
    @JsonProperty("pairings")
    public SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
    //    @XmlAttribute(name = "globalDurationMin")
//    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
//    @JsonIgnore
//    @Deprecated
//    protected Duration expectedGlobalMinimumDuration;
//    @XmlAttribute(name = "globalDurationAvg")
//    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
//    @JsonIgnore
//    @Deprecated
//    protected Duration expectedGlobalAverageDuration;
//    @XmlAttribute(name = "globalDurationMax")
//    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
//    @JsonIgnore
//    @Deprecated
//    protected Duration expectedGlobalMaximumDuration;
    @XmlAttribute(name = "globalPlayMax")
    @JsonIgnore
    protected Long expectedGlobalMaximumPlay;
    //    @XmlTransient
//    @JsonProperty("participantsCache")
//    private SortedSet<Participant> participantsCache = null;
    @XmlTransient
    @JsonIgnore
    private final int participantsCacheSize = 0;
    @XmlAttribute(name = "globalPlayAvg")
    @JsonIgnore
    protected Long expectedGlobalAveragePlay;
    @XmlAttribute(name = "participantDurationMax")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    @JsonIgnore
    protected Duration expectedParticipantMaximumDuration;
    @XmlAttribute(name = "participantDurationMin")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    @JsonIgnore
    protected Duration expectedParticipantMinimumDuration;
    @XmlAttribute(name = "participantDurationAvg")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    @JsonIgnore
    protected Duration expectedParticipantAverageDuration;
    @XmlAttribute(name = "participantPlayMax")
    @JsonIgnore
    protected Long expectedParticipantMaximumPlay;
    @XmlAttribute(name = "participantPlayMin")
    @JsonIgnore
    protected Long expectedParticipantMinimumPlay;
    @XmlAttribute(name = "participantPlayAvg")
    @JsonIgnore
    protected Long expectedParticipantAveragePlay;
    @JsonIgnore
    @XmlTransient
    protected StatisticsStructure playPerParticipantStatistics = new StatisticsStructure();
    @JsonIgnore
    @XmlTransient
    protected boolean playPerParticipantStatisticsInitialized = false;
    //    @XmlTransient
//    @JsonIgnore
//    @JsonProperty("competitionSeedCache")
//    protected CompetitionSeed competitionSeedCache;
    @XmlTransient
    @JsonIgnore
    SortedSet<Participant> activeParticipantsCache = null;
    @XmlTransient
    @JsonIgnore
    SortedSet<Participant> inactiveParticipantsCache = null;
    @XmlAttribute(name = "globalStepMax")
    private Long expectedGlobalMaximumStep;
    @XmlAttribute(name = "globalStepMin")
    private Long expectedGlobalMinimumStep;
    @XmlAttribute(name = "globalStepAvg")
    private Long expectedGlobalAverageStep;
    @XmlAttribute(name = "wave")
    private Integer currentWave = 0;
    @XmlTransient
    @JsonIgnore
    private DescriptionTable descriptionTable;
    @XmlAttribute(name = "globalPlayMin")
    @JsonIgnore
//    @Deprecated
    protected Long expectedGlobalMinimumPlay;
    //    private CompetitionSeed competitionSeed;
//    @XmlAttribute(name = "lastBibNumber")
//    @JsonProperty("lastBibNumber")
//    private long lastBibNumberIndex = 0;
    @XmlElement(name = "idGenerator")
    @JsonProperty("idGenerator")
    private IdGenerator idGenerator;

    public CompetitionInstance() {
        super(new IdGenerator());
        this.idGenerator = this.idGeneratorCache;
        this.idGeneratorCache = null;
        this.version = Version.VERSION_1_0;
    }

    public static CompetitionInstance createInstance() {
        return new CompetitionInstance();
    }


//    private CompetitionInstance(IdGenerator idGenerator) {
//        super(idGenerator);
//    }
//
//    public static CompetitionInstance createInstance(IdGenerator idGenerator) {
//        return new CompetitionInstance(idGenerator);
//    }


    @Override
    public CompetitionInstance clone() throws CloneNotSupportedException {
        return (CompetitionInstance) super.clone();
    }

    public Integer getCurrentWave() {
        return currentWave;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        if (o instanceof CompetitionObserver)
            this.competitionObserver = (CompetitionObserver) o;
        super.addObserver(o);
        for (CompetitionPhase competitionPhase : this.competitionPhases) {
            competitionPhase.addObserver(o);
        }
        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            competitionSeed.addObserver(o);
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.addObserver(o);
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.addObserver(o);
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.addObserver(o);
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.addObserver(o);
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.addObserver(o);
        }

//        for (ParticipantSeat participantSeat : participantSeats) {
//            participantSeat.            addObserver(o);
//        }
//        for (ParticipantResult participantResult : participantResults) {
//            participantResult.            addObserver(o);
//        }
    }

    public SortedSet<Participant> getParticipants() {
//        if (participantsCache == null || participantsCache.isEmpty() || participantsCacheSize != participants.size()) {
//            participantsCache = new TreeSet<>();
////            for (String participantId : participants) {
////                participantsCache.add(getParticipantTeamOrSingle(participantId));
////            }
////            Sets.sort(participantsCache);
////            participantsCacheSize = participants.size();
//        }
        return participants;
    }

    public CompetitionComputationParam getCompetitionComputationParam() {
        return competitionComputationParam;
    }

    public void setCompetitionComputationParam(CompetitionComputationParam competitionComputationParam) {
        this.competitionComputationParam = competitionComputationParam;
    }

    public void initForXmlOutput() {
//        fillCompetitionInstanceTree();
//        competitionSeeds.clear();
//        if (this.getCompetitionSeed() != null) {
//            competitionSeed = this.getCompetitionSeed().localId;
//            competitionSeeds.clear();
//            competitionSeeds.add(this.getCompetitionSeed());
//            competitionSeeds.addAll(this.getCompetitionSeed().getAllCompetitionSeeds());
//        }
//        for (CompetitionSeed competitionSeed : competitionSeeds)
//            this.getCompetitionSeed().initForXmlOutput();
//        for (ParticipantResult participantResult : participantResults) {
//            participantResult.initForXmlOutput();
//        }
//        boolean emptyParticipant = false;
//        for (ParticipantSeat participantSeat : participantSeats) {
//            participantSeat.initForXmlOutput(emptyParticipant);
//        }
    }

//    @Deprecated
//    public CompetitionSeed getCompetitionSeed() {
////        if (competitionSeedCache == null && competitionSeed != null) {
////            competitionSeedCache = this.getCompetitionSeed(competitionSeed);
////        }
////        return competitionSeedCache;
//        return competitionSeed;
//    }
//
//    @Deprecated
//    public void setCompetitionSeed(CompetitionSeed competitionSeed) {
////        this.competitionSeed = competitionSeed.localId;
////        this.competitionSeedCache = null;
//        this.competitionSeed = competitionSeed;
//        this.addCompetitionSeed(competitionSeed);
//    }

    public void initFromXmlInput() {
//        for (CompetitionSeed competitionSeed : competitionSeeds) {
//            competitionSeed.initFromXmlInput(this);
//        }
//        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
//            competitionGroupResult.initFromXmlInput(this);
//        }
//        for (CompetitionGroup competitionGroup : competitionGroups) {
//            competitionGroup.initFromXmlInput(this);
//        }
//
//        for (CompetitionRound competitionRound : competitionRounds) {
//            competitionRound.initFromXmlInput(this);
//        }
//        for (CompetitionMatch competitionMatch : competitionMatches) {
//            competitionMatch.initFromXmlInput(this);
//        }
//        for (CompetitionPlay competitionPlay : competitionPlays) {
//            competitionPlay.initFromXmlInput(this);
//        }
//
//        for (ParticipantSeat participantSeat : participantSeats) {
//            participantSeat.initFromXmlInput(this);
//        }
//        for (ParticipantResult participantResult : participantResults) {
//            participantResult.initFromXmlInput(this);
//        }
//        super.resetCache();
//        this.resetStatistics();
    }

    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
    }

    @Override
    public void resetCache() {
        super.resetCache();
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.resetCache();
        }
        for (CompetitionPhase competitionPhase : competitionPhases) {
            competitionPhase.resetCache();
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.resetCache();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.resetCache();
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.resetCache();
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.resetCache();
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.resetCache();
        }

        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.resetCache();
        }
        for (ParticipantResult participantResult : participantResults) {
            participantResult.resetCache();
        }
        for (ParticipantSingle participantSingle : participantSingles) {
            participantSingle.resetCache();
        }
        for (ParticipantTeam participantTeam : participantTeams) {
            participantTeam.resetCache();
        }
//        this.competitionSeedCache = null;
//        this.competitionSeedCache = null;
//        this.participantsCache = null;
//        this.participantsCache = null;
    }

    public int compareTo(CompetitionInstance o) {
        int compareValue = 0;
        if (o == null)
            compareValue = 1;
        if (compareValue == 0)
            compareValue = this.getExpectedGlobalDuration().avg.compareTo(o.getExpectedGlobalDuration().avg);
        if (compareValue == 0)
            compareValue = this.getExpectedParticipantPlay().min.compareTo(o.getExpectedParticipantPlay().min);
        if (compareValue == 0)
            compareValue = this.getExpectedGlobalPlay().avg.compareTo(o.getExpectedGlobalPlay().avg);
        if (compareValue == 0)
            compareValue = this.getExpectedGlobalDuration().min.compareTo(o.getExpectedGlobalDuration().min);
        if (compareValue == 0)
            compareValue = this.getExpectedParticipantPlay().max.compareTo(o.getExpectedParticipantPlay().max);
        if (compareValue == 0)
            compareValue = this.getExpectedGlobalDuration().max.compareTo(o.getExpectedGlobalDuration().max);
        if (compareValue == 0)
            compareValue = this.getLocalId().compareTo(o.getLocalId());
        return compareValue;
    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        return new ParticipantScoreCompetition(this);
    }

    public void fillCompetitionMatchLink() {
        if (this.getCompetitionPhase() != null)
            this.getCompetitionPhase().fillCompetitionMatchLink();

    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = new Duration(java.time.Duration.ZERO);
//        if (this.getCompetitionPhase() != null)
//            this.getCompetitionPhase().fillExpectedRelativeTime();

        for (CompetitionPhase competitionPhase : competitionPhases) {
            competitionPhase.fillExpectedRelativeTime();
            if (expectedRelativeEndTime == null || expectedRelativeEndTime.compareTo(competitionPhase.expectedRelativeEndTime) < 0)
                expectedRelativeEndTime = competitionPhase.expectedRelativeEndTime;
        }
        expectedDuration = expectedRelativeEndTime.minus(expectedRelativeStartTime);

    }

    public CompetitionPhase getCompetitionPhase() {
        return competitionPhase;
    }

//    public void fillCompetitionParticipantResultFromCompetitionSeed() {
//        if (this.getCompetitionSeed() != null) {
//            fillCompetitionParticipantResultFromCompetitionSeed(this.getCompetitionSeed(), 1);
//        }
//        int score = 0;
//        int multiplier = 1;
//        for (ParticipantResult participantResult : participantResults) {
//            multiplier = 1;
//            score = 0;
//            List<ParticipantScore> participantScoresSub = participantResult.participantScore.participantScoresSub;
//            for (ParticipantScore participantScore : participantScoresSub) {
//                score = score + participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS).computeNumberValue().intValue() * multiplier;
//                multiplier = multiplier + 1;
//            }
//            Collections.reverse(participantResult.participantScore.participantScoresSub);
//            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS, score);
//        }
//
//
//    }

//    public void fillCompetitionParticipantResultFromCompetitionSeed(CompetitionSeed this.getCompetitionSeed(), int depth) {
//        ParticipantResult participantResult = null;
//        boolean createIfNorExists = true;
//        int score = 0;
//        int divider = 1;
//        List<ParticipantScore> participantScoresSub = new ArrayList<ParticipantScore>();
//        for (ParticipantResult participantSeedResult : this.getCompetitionSeed().participantResults) {
//            participantResult = getParticipantResultFor(participantSeedResult.participant, createIfNorExists);
//            participantResult.participantScore.participantScoresSub.add(participantSeedResult.participantScore);
//        }
//
//        SortedSet<CompetitionSeed> nextCompetitionPhases = this.getCompetitionSeed().computeNextCompetitionSeeds();
//        for (CompetitionSeed nextCompetitionSeed : nextCompetitionPhases)
//            fillCompetitionParticipantResultFromCompetitionSeed(nextCompetitionSeed, depth + 1);
//
//    }

    @Override
    public boolean allParticipantResultsSet() {
        boolean allParticipantResultsSet = true;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            allParticipantResultsSet = allParticipantResultsSet &&
                    isParticipantResultSet(participantSeat.participant);
            if (!allParticipantResultsSet)
                break;
        }
        return allParticipantResultsSet;
    }

    public void setPotValueInCents(Integer potValueInCents) {
        this.potValueInCents = potValueInCents;
        if (this.isClosed()) {
            computeParticipantSlicePercent();
            computeParticipantSliceValue();
        }
    }


    public void computeParticipantSliceValue() {
        if (this.isClosed() && isParticipantResultsSet()) {
            if (potValueInCents != null && potValueInCents > 0) {
                boolean zero = false;
                Integer sliceValueForRanking = null;
                Integer potValueInCentsRemaining = potValueInCents;
                Integer potValueInCentsTmp = potValueInCents;
                while (potValueInCentsRemaining > 0) {
                    potValueInCentsTmp = potValueInCentsRemaining;
                    for (ParticipantResult participantResultCurrent : this.participantResults) {
                        ParticipantScoreValue participantScoreValueSlicePercent = participantResultCurrent.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT);

                        if (participantScoreValueSlicePercent == null || participantScoreValueSlicePercent.value == null || Integer.parseInt(participantScoreValueSlicePercent.value) == 0) {
                            break;
                        } else {
                            Integer slicePercent = Integer.parseInt(participantScoreValueSlicePercent.value);
                            ParticipantScoreValue participantScoreValueSliceValue = participantResultCurrent.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE);
                            Integer currentValue = 0;
                            if (participantScoreValueSliceValue.value != null) {
                                currentValue = Integer.parseInt(participantScoreValueSliceValue.value);
                                sliceValueForRanking = potValueInCentsTmp * slicePercent / 100;
                                if (sliceValueForRanking == 0 && potValueInCentsRemaining > 0)
                                    sliceValueForRanking = 10;
                            } else {
                                sliceValueForRanking = potValueInCentsTmp * slicePercent / 100;
                                sliceValueForRanking = (int) Math.ceil((double) sliceValueForRanking / 10.0 * 10.0);
                            }
                            if (sliceValueForRanking > potValueInCentsRemaining)
                                sliceValueForRanking = potValueInCentsRemaining;
                            potValueInCentsRemaining = potValueInCentsRemaining - sliceValueForRanking;
                            Integer newValue = currentValue + sliceValueForRanking;
                            participantResultCurrent.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE, newValue);
                            if (potValueInCentsRemaining == 0)
                                break;
                        }
                    }
                }
                Integer sum = 0;
                for (ParticipantResult participantResultCurrent : this.participantResults) {
                    ParticipantScoreValue participantScoreValue = participantResultCurrent.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE);
                    if (participantScoreValue.value != null) {
                        Integer value = Integer.parseInt(participantScoreValue.value);
                        sum = sum + value;
                    }
                }
                assert sum.compareTo(potValueInCents) == 0;

            }
        }
    }

    public void fillCompetitionParticipantResult() {
        boolean createIfNorExists = true;
        participantResults.clear();
        ParticipantResult participantResult;
        if (this.competitionPhases != null) {
            Map<Participant, Rating> participantRatingMap = getParticipantRatingMap();

            for (Map.Entry<Participant, Rating> participantRatingEntry : participantRatingMap.entrySet()) {
                participantResult = getParticipantResultFor(participantRatingEntry.getKey(), createIfNorExists);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_RATING, participantRatingEntry.getValue().getRating());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_RATING_DEVIATION, participantRatingEntry.getValue().getRatingDeviation());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_VOLATILITY, participantRatingEntry.getValue().getVolatility());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_NUMBER_OF_RESULTS, participantRatingEntry.getValue().getNumberOfResults());
            }

            for (CompetitionPhase competitionPhase : this.competitionPhases) {
                if (!competitionPhase.isCancelled() && competitionPhase.phaseType.compareTo(PhaseType.SEEDING) != 0) {
                    int bonus = MathUtils.getPointBonus(competitionPhase.round);
                    for (ParticipantSeat participantSeat : participantSeats) {
                        Participant participant = participantSeat.participant;
                        if (participant != null) {
                            participantResult = getParticipantResultFor(participant, createIfNorExists);
                            if (competitionPhase.participantResults != null) {
                                for (ParticipantResult participantResultPhase : competitionPhase.participantResults) {
                                    if (participantResultPhase.getParticipant().getAllRealParticipantsAsArray().contains(participant)) {
                                        int score = participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS).computeNumberValue().intValue();
                                        score = score + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue() + bonus;
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS, score);

                                        int win = participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN).computeNumberValue().intValue();
                                        win = win + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_WIN).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_WIN, win);

                                        int loss = participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS).computeNumberValue().intValue();
                                        loss = loss + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_LOSS).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_LOSS, loss);

                                        int draw = participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW).computeNumberValue().intValue();
                                        draw = draw + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_DRAW).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_DRAW, draw);


                                        ParticipantScoreValue participantScoreValueLastActivePhase = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_ROUND);
                                        if (participantScoreValueLastActivePhase != null && participantScoreValueLastActivePhase.computeNumberValue() != null && Integer.valueOf(competitionPhase.localId) > participantScoreValueLastActivePhase.computeNumberValue().intValue()) {
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_ROUND, competitionPhase.localId);
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_RANK, participantResultPhase.rank);
                                        }

                                        participantResult.participantScore.participantScoresSub.add(participantResultPhase.participantScore);

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (this.competitionSeeds != null) {
                for (CompetitionSeed competitionSeed : this.competitionSeeds) {
                    if (!competitionSeed.isCancelled() && !competitionSeed.emptyPhase) {
                        if (competitionSeed.participantResults != null) {
                            for (ParticipantResult participantResultSeed : competitionSeed.participantResults) {
                                for (Participant participant : participants) {
                                    participantResult = getParticipantResultFor(participant, createIfNorExists);
                                    if (participantResultSeed.getParticipant().getAllRealParticipantsAsArray().contains(participant)) {
                                        int score = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS).computeNumberValue().intValue();
                                        score = score + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS, score);

                                        int win = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN).computeNumberValue().intValue();
                                        win = win + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_WIN).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_WIN, win);

                                        int loss = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS).computeNumberValue().intValue();
                                        loss = loss + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_LOSS).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_LOSS, loss);

                                        int draw = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW).computeNumberValue().intValue();
                                        draw = draw + participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_DRAW).computeNumberValue().intValue();
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_DRAW, draw);


                                        ParticipantScoreValue participantScoreValueLastActiveSeed = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_ROUND);
                                        if (participantScoreValueLastActiveSeed != null && participantScoreValueLastActiveSeed.computeNumberValue() != null && Integer.valueOf(competitionSeed.localId) > participantScoreValueLastActiveSeed.computeNumberValue().intValue()) {
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_ROUND, competitionSeed.localId);
                                            participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.LAST_ACTIVE_PHASE_RANK, participantResultSeed.rank);
                                        }

                                        participantResult.participantScore.participantScoresSub.add(participantResultSeed.participantScore);

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.participantResults = Sets.sort(this.participantResults);
        if (!participantResults.isEmpty()) {
            ParticipantScore.fillRank(this.participantResults, ParticipantScoreCompetition.RANK);
            ParticipantScore.fillPoints(this.participantResults, ParticipantScoreCompetition.SCORE_POINTS);

            computeParticipantSlicePercent();
            computeParticipantSliceValue();
        }


    }

    public void computeParticipantSlicePercent() {
        BigDecimal sum = BigDecimal.ZERO;
        int numberOfParticipantCompetition = getCompetitionComputationParam().numberOfParticipantCompetition;
        int topTenRank = (int) Math.ceil((double) numberOfParticipantCompetition / 10.0);
        if (topTenRank % getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch != 0 && topTenRank % getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch < numberOfParticipantCompetition / 10.0) {
            topTenRank += getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch - topTenRank % getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch;
        }
        if (topTenRank < getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch && getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch < 4)
            topTenRank = getCompetitionComputationParam().getLastPhase().participantQualifiedPerMatch;
        if (topTenRank < getCompetitionComputationParam().getLastPhase().numberOfParticipantMatch && getCompetitionComputationParam().numberOfParticipantCompetition >= getCompetitionComputationParam().getLastPhase().numberOfParticipantMatch * 2)
            topTenRank = getCompetitionComputationParam().getLastPhase().numberOfParticipantMatch;
        if (topTenRank == 0)
            topTenRank = 1;
        SortedSet<ParticipantResult> participantResultsTopTen = new TreeSet<>();
        Integer sumPoints = 0;
        Integer previousRank = 0;
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.rank <= topTenRank && (participantResultsTopTen.size() <= topTenRank || participantResult.rank.compareTo(previousRank) == 0)) {
                previousRank = participantResult.rank;
                participantResultsTopTen.add(participantResult);
                sumPoints = sumPoints + Integer.parseInt(participantResult.participantScore.getParticipantScoreValuePoints().value);
            }
        }
        Integer percentRemaining = 100;
        participantResultsTopTen = Sets.sort(participantResultsTopTen);
        while (percentRemaining > 0) {
            for (ParticipantResult participantResult : participantResultsTopTen) {
                if (participantResult.rank <= topTenRank) {
                    String slicePercentValue = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT).value;
                    String pointsValue = participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).value;
                    Integer slicePercentValueInteger = 0;
                    if (slicePercentValue != null) {
                        slicePercentValueInteger = Integer.parseInt(slicePercentValue);
                    }
                    if (slicePercentValueInteger > 0) {
                        slicePercentValueInteger += percentRemaining;
                        percentRemaining = 0;
                    } else {
                        Integer points = Integer.parseInt(pointsValue);
                        slicePercentValueInteger = sumPoints > 0 ? points * 100 / sumPoints : 100;
                        if (slicePercentValueInteger < 1 && percentRemaining > 1)
                            slicePercentValueInteger = 1;
                        percentRemaining = percentRemaining - slicePercentValueInteger;
                    }
                    participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT, slicePercentValueInteger);
                    if (percentRemaining == 0) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        Integer sumPercent = 0;
        for (ParticipantResult participantResultCurrent : this.participantResults) {
            ParticipantScoreValue participantScoreValue = participantResultCurrent.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT);
            if (participantScoreValue.value != null) {
                Integer value = Integer.parseInt(participantScoreValue.value);
                sumPercent = sumPercent + value;
            }
        }
        assert sumPercent.compareTo(100) == 0;
    }

    private Set<ParticipantResult> getParticipantResultForRanking(int rank) {
        Set<ParticipantResult> participantResultSet = new HashSet<>();
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.rank == rank)
                participantResultSet.add(participantResult);
        }
        return participantResultSet;
    }

    private long getMultiplicatorForRank(int rank) {
        Map<Integer, Long> rankingWeightMap = getRankingWeightMap();
        long points = 1;
        if (rankingWeightMap.containsKey(rank))
            points = points * rankingWeightMap.get(rank);
        if (points == 0)
            points = 1;
        return points;
    }

//    private Integer getSlicePercentForRanking(int rank) {
//        Map<Integer, Integer> rankingSlicePercentMap = getRankingSlicePercentMap();
//        if (rankingSlicePercentMap.containsKey(rank))
//            return rankingSlicePercentMap.get(rank);
//        return 0;
//    }

    private Map<Integer, Long> getRankingWeightMap() {
        int lowLimit = this.participantResults.size() * competitionComputationParam.sharerPercentageLimit / 100;
        if (lowLimit <= 0)
            lowLimit = 1;
//        if (lowLimit < this.competitionComputationParam.numberOfParticipantMatch && this.participantResults.size() > Math.pow(this.competitionComputationParam.numberOfParticipantMatch, 2))
//            lowLimit = this.competitionComputationParam.numberOfParticipantMatch;

        int weight = 0;

        int currentTotalTemp = 0;
        int currentRanking = 0;
        while (currentTotalTemp <= lowLimit) {
            currentRanking++;
            currentTotalTemp += getParticipantResultForRanking(currentRanking).size();
        }

        int maxRankRetributed = currentRanking - 1;
        if (maxRankRetributed <= 0)
            maxRankRetributed = 1;

        int lcm = 0;
        Set<ParticipantResult> participantResultsForRanking = null;
        Map<Integer, Long> participantResultsSizeForRankingMap = new HashMap<>();
        for (int i = 1; i < maxRankRetributed + 1; i++) {
            participantResultsForRanking = getParticipantResultForRanking(i);
            participantResultsSizeForRankingMap.put(i, (long) participantResultsForRanking.size());
        }

        Long[] participantResultsSizeForRankingsArray = new Long[participantResultsSizeForRankingMap.size()];
        participantResultsSizeForRankingMap.values().toArray(participantResultsSizeForRankingsArray);

        long multiplicand = 1;
        if (participantResultsSizeForRankingsArray.length > 1) {
            multiplicand = MathUtils.lcm(participantResultsSizeForRankingsArray);
        }


        Map<Integer, Long> rankingWeight = new HashMap<>();
        int totalWeight = 0;
        for (int i = maxRankRetributed; i > 0; i--) {
            weight = 10;
            if (rankingWeight.containsKey(i + 1))
                weight += rankingWeight.get(i + 1) / 5;
//            if (rankingWeight.containsKey(i + 2))
//                weight += rankingWeight.get(i + 2);
            rankingWeight.put(i, weight * multiplicand / participantResultsSizeForRankingMap.get(i));
            totalWeight += rankingWeight.get(i);
        }
        double minimalWeightPercent = (double) rankingWeight.get(maxRankRetributed) / (double) totalWeight;
        double particpantPercent = (double) 1 / (double) this.participantResults.size();
        if (minimalWeightPercent < particpantPercent)
            rankingWeight.remove(maxRankRetributed);

        return rankingWeight;
    }

    public Integer getDistributedPotInCents() {
        Integer distributedPotInCents = 0;
        for (ParticipantResult participantResultCurrent : this.participantResults) {
            distributedPotInCents += participantResultCurrent.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE).computeNumberValue().intValue();
        }
        return distributedPotInCents;
    }

    private SortedSet<CompetitionGroupResult> getAllCompetitionGroupResults() {
        SortedSet<CompetitionGroupResult> competitionGroupResults = new TreeSet<>();
        if (this.getCompetitionPhase() != null) {
            competitionGroupResults.addAll(this.getCompetitionPhase().getAllCompetitionGroupResults());
            Sets.sort(competitionGroupResults);
//        } else if (this.getCompetitionSeed() != null) {
//            competitionGroupResults.addAll(this.getCompetitionSeed().getAllCompetitionGroupResults());
//            Sets.sort(competitionGroupResults);
        }
        return competitionGroupResults;
    }

    public SortedSet<CompetitionMatch> getCompetitionMatches() {
        return competitionMatches;
    }

    public SortedSet<CompetitionRound> getCompetitionRounds() {
        return competitionRounds;
    }

    public SortedSet<CompetitionPlay> getCompetitionPlays() {
        return competitionPlays;
    }

    public Set<CompetitionGroupResult> computeLastCompetitionGroupResults() {
        Set<CompetitionGroupResult> competitionGroupResults = null;
        if (this.getCompetitionPhase() != null) {
            competitionGroupResults = this.getCompetitionPhase().computeLastCompetitionGroupResults();
//        } else if (this.getCompetitionSeed() != null) {
//            competitionGroupResults = this.getCompetitionSeed().computeLastCompetitionGroupResults();
        }
        return competitionGroupResults;
    }

    public Set<Participant> computeLastCompetitionGroupResultsParticipants() {
        Set<Participant> participants = new HashSet<>();
        Set<CompetitionGroupResult> competitionGroupResults = computeLastCompetitionGroupResults();
        if (competitionGroupResults != null) {
            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
                for (ParticipantResult participantResult : competitionGroupResult.participantResults) {
                    participants.add(participantResult.participant);
                }
            }
        }
        return participants;
    }


    public SortedSet<ParticipantResult> getParticipantCompetitionResults() {
        return participantResults;
    }

//
//    public void setParticipantResults(SortedSet<ParticipantResult> participantResults, CompetitionObjectWithResult competitionObjectWithResult) {
//        competitionObjectWithResult.setParticipantResults(participantResults);
//    }

//    private Integer getSliceValueForRanking(int rank) {
//        Integer sliceValueForRanking = 0;
//        List<Integer> rankingSlicePercentList = getRankingSlicePercentList();
//        if (potValueInCents != null && potValueInCents > 0) {
//            Integer slicePercent = 0;
//            Integer exaequo = getParticipantResultForRanking(rank).size();
//            if (rankingSlicePercentList.size() >= rank && exaequo > 0)
//                slicePercent = rankingSlicePercentList.get(rank - 1) / exaequo;
//            Integer exaequoNext = getParticipantResultForRanking(rank + 1).size();
//            Integer slicePercentNext = 0;
//            if (rankingSlicePercentList.size() >= rank + 1 && exaequoNext > 0)
//                slicePercentNext = rankingSlicePercentList.get(rank + 1 - 1) / exaequoNext;
//            Integer remainingPotInCents = potValueInCents - getDistributedPotInCents();
//            if (remainingPotInCents > 0) {
//                sliceValueForRanking = potValueInCents * slicePercent / 100;
//                int cents = (100 - sliceValueForRanking % 100) % 100;
//                if (cents != 0) {
//
//                    if (cents < remainingPotInCents - sliceValueForRanking) {
//                        sliceValueForRanking += cents;
//                    } else {
//                        sliceValueForRanking = remainingPotInCents;
//                    }
//                }
//                if ((slicePercentNext == null || slicePercentNext == 0 || remainingPotInCents - sliceValueForRanking < 100))
//                    sliceValueForRanking = remainingPotInCents;
//            }
//        }
//        return sliceValueForRanking;
//    }

    @Override
    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = false;
        if (competitionPhases != null && !competitionPhases.isEmpty()) {
            participantResultSet = true;
            for (CompetitionPhase competitionPhase : competitionPhases) {
                participantResultSet = participantResultSet && (competitionPhase.phaseType.compareTo(PhaseType.SEEDING) == 0 || competitionPhase.isParticipantResultsSet());
                if (!participantResultSet)
                    break;
            }
        } else {
            if (this.getCompetitionPhase() != null) {
                participantResultSet = this.getCompetitionPhase().isAllSubParticipantResultsSet();
//            } else if (this.getCompetitionSeed() != null) {
//                participantResultSet = this.getCompetitionSeed().isAllSubParticipantResultsSet();
            }
        }
        return participantResultSet;
    }

    @Override
    public boolean isParticipantResultsSet() {
        boolean participantResultSet = true;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            participantResultSet = isParticipantResultSet(participantSeat.participant);
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

    public ParticipantSingle addParticipantSingle(ParticipantSingle participantSingle) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        for (ParticipantSingle participantSingleCurrent : competitionInstance.participantSingles) {
            if (participantSingleCurrent.localId.compareTo(participantSingle.localId) == 0) {
                competitionInstance.participantSingles.remove(participantSingleCurrent);
                break;
            }
        }
        participantSingle.setCompetitionInstance(competitionInstance);
        competitionInstance.participantSingles.add(participantSingle);
        competitionInstance.participants.add(participantSingle);
        return participantSingle;
    }

    public ParticipantTeam addParticipantTeam(ParticipantTeam participantTeam) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        for (ParticipantTeam participantTeamCurrent : competitionInstance.participantTeams) {
            if (participantTeamCurrent.localId.compareTo(participantTeam.localId) == 0) {
                competitionInstance.participantTeams.remove(participantTeamCurrent);
                break;
            }
        }
        participantTeam.setCompetitionInstance(competitionInstance);
        competitionInstance.participantTeams.add(participantTeam);
        competitionInstance.participants.add(participantTeam);
        return participantTeam;
    }

    public void reset(boolean recursive) {
        super.reset(false);
        this.participantTeams.clear();
        this.participantSingles.clear();
        this.participants.clear();
        this.participantPairings.clear();
        this.participantResults.clear();
        for (ParticipantSeat participantSeat : participantSeats) {
            participantSeat.reset();
        }

        getIdGenerator().reset(ParticipantSeat.class.getSimpleName());

        participantSeats.clear();
        participantSeatsAll.clear();
        getIdGenerator().resetBibNumber();
//        this.lastBibNumberIndex = 0;
        this.currentWave = 0;
        this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
        getIdGenerator().clearParticipantCounter();
        if (recursive) {
            for (CompetitionPhase competitionPhase : this.competitionPhases) {
                competitionPhase.reset(false);
            }
            Sets.sort(this.competitionPhases);
            for (CompetitionSeed competitionSeed : this.competitionSeeds) {
                competitionSeed.reset(false);
            }
            Sets.sort(this.competitionSeeds);
            for (CompetitionGroup competitionGroup : this.competitionGroups) {
                competitionGroup.reset(false);
            }
            Sets.sort(this.competitionGroups);
            for (CompetitionGroupResult competitionGroupResult : this.competitionGroupResults) {
                competitionGroupResult.reset(false);
            }
            Sets.sort(this.competitionGroupResults);
            List<CompetitionRound> competitionRounds = new ArrayList<>(this.competitionRounds);
            for (CompetitionRound competitionRound : competitionRounds) {
                competitionRound.reset(false);
            }
            Sets.sort(this.competitionRounds);

            List<CompetitionPlay> competitionPlays = new ArrayList<>(this.competitionPlays);
            for (CompetitionPlay competitionPlay : competitionPlays) {
                competitionPlay.reset(false);
            }
            Sets.sort(this.competitionPlays);

            List<CompetitionMatch> competitionMatches = new ArrayList<>(this.competitionMatches);
            for (CompetitionMatch competitionMatch : competitionMatches) {
                competitionMatch.reset(false);
            }
            Sets.sort(this.competitionMatches);


            List<CompetitionPlay> competitionPlaysToRemove = new ArrayList<>();
            for (CompetitionPlay competitionPlay : competitionPlays) {
                if (competitionPlay.competitionInstance == null)
                    competitionPlaysToRemove.add(competitionPlay);
            }
            this.competitionPlays.removeAll(competitionPlaysToRemove);

            List<CompetitionMatch> competitionMatchesToRemove = new ArrayList<>();
            for (CompetitionMatch competitionMatch : competitionMatches) {
                if (competitionMatch.competitionInstance == null)
                    competitionMatchesToRemove.add(competitionMatch);
            }
            this.competitionMatches.removeAll(competitionMatchesToRemove);

            for (CompetitionPhase competitionPhase : this.competitionPhases) {
                competitionPhase.reset(false);
            }
            Sets.sort(this.competitionPhases);
        }

    }

    private ParticipantSeat getParticipantSeatFree() {
        ParticipantSeat participantSeatFree = null;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (participantSeat.isFree()) {
                participantSeatFree = participantSeat;
                break;
            }
        }
        return participantSeatFree;

    }

    public boolean isParticipantSeatFreeAvailable() {
        boolean participantSeatFreeAvailable = false;
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (participantSeat.isFree()) {
                participantSeatFreeAvailable = true;
                break;
            }
        }
        return participantSeatFreeAvailable;

    }

//    @Deprecated
//    public ParticipantSeat subscribe(Participant participant) {
//        return subscribe(participant, null);
//    }

    public ParticipantSeat subscribe(Participant participant, Integer ranking) {
        ParticipantSeat participantSeatForRegistration = getParticipantSeatFor(participant);
        if (participantSeatForRegistration == null) {
            participantSeatForRegistration = getParticipantSeatFree();
            if (participantSeatForRegistration == null) {
                participantSeatForRegistration = createParticipantSeat();
            }
            if (participantSeatForRegistration != null && ranking != null && ranking > 0) {
                participantSeatForRegistration.previousRanking = ranking;
//            } else {
//                participantSeatForSubscription.previousRanking = null;
            }
            String bibNumber = getNextBibNumber();
            if (participant instanceof ParticipantSingle) {
                ParticipantSingle participantSingle = (ParticipantSingle) participant;
                addParticipantSingle(participantSingle);
                ParticipantTeam participantTeam = createParticipantTeam();
                participantTeam.equalityComparison = participant.equalityComparison;
                participantTeam.internationalizedLabel = participant.internationalizedLabel;
//                addParticipantSingleForSubscription((ParticipantSingle)participant);
//                addParticipantTeam(participantTeam);
                participantTeam.addParticipant(participantSingle);
                participant = participantTeam;
                addParticipantTeam(participantTeam);
            } else if (participant instanceof ParticipantTeam) {
                ParticipantTeam participantTeam = (ParticipantTeam) participant;
                addParticipantTeam(participantTeam);
            }
            if (participant instanceof ParticipantTeam) {
                ParticipantTeam participantTeam = (ParticipantTeam) participant;
                PlayVersusType playVersusType = null;
                if (playVersusType == null && getCompetitionComputationParam().getMixingPhaseParameter() != null) {
                    playVersusType = getCompetitionComputationParam().getMixingPhaseParameter().playVersusType;
                }
                if (playVersusType == null && getCompetitionComputationParam().getQualificationPhaseParameter() != null) {
                    playVersusType = getCompetitionComputationParam().getQualificationPhaseParameter().playVersusType;
                }
                if (playVersusType == null && getCompetitionComputationParam().getFinalPhaseParameter() != null) {
                    playVersusType = getCompetitionComputationParam().getFinalPhaseParameter().playVersusType;
                }
                if (competitionComputationParam.participantType.numberOfParticipants > playVersusType.teamSize) {
                    for (ParticipantTeamMember participantTeamMemberCurrent : participantTeam.participantTeamMembers) {
                        if (participantTeamMemberCurrent.getParticipant() instanceof ParticipantTeam) {
                            // TODO team(s) inside team
                        } else if (participantTeamMemberCurrent.getParticipant() instanceof ParticipantSingle) {
                            ParticipantTeam participantTeamSub = createParticipantTeam();
//                            participantTeamSub.localId = participantTeamMemberCurrent.participant.localId;
                            participantTeamSub.bibNumber = participantTeamMemberCurrent.getParticipant().bibNumber;
                            participantTeamSub.internationalizedLabel = (InternationalizedLabel) participantTeamMemberCurrent.getParticipant().internationalizedLabel.clone();
//                            addParticipantTeam(participantTeamSub);
                            participantTeamSub.addParticipant(participantTeamMemberCurrent.getParticipant());
                            participantTeamMemberCurrent.setParticipant(participantTeamSub);
                        }
                    }
                }
                participantSeatForRegistration.setParticipant(participant);
                if (((ParticipantTeam) participant).participantTeamMembers.size() == 1) {
                    setBibNumbers(participant, bibNumber, 0);
                } else {
                    setBibNumbers(participant, bibNumber, 1);
                }
                if (competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS) == 0) {
                    for (CompetitionPhase competitionPhase : competitionPhases) {
                        if (competitionPhase.isOpen() && competitionPhase.competitionCreationParamPhase.registrationOnTheFly) {
                            competitionPhase.addParticipantSeat(participantSeatForRegistration);
                            competitionPhase.setChanged(false);
                            participantSeatForRegistration.setCompetitionPhase(competitionPhase);
                            CompetitionSeed competitionSeed = competitionPhase.competitionSeeds.first();
                            competitionSeed.setChanged(false);
                            participantSeatForRegistration.setCompetitionSeed(competitionSeed);
                            competitionSeed.addParticipantSeat(participantSeatForRegistration);
                            CompetitionGroup competitionGroup = competitionPhase.competitionSeeds.first().competitionGroups.first();
                            participantSeatForRegistration.setCompetitionGroup(competitionGroup);
                            competitionGroup.addParticipantSeat(participantSeatForRegistration);
                            competitionGroup.setChanged(false);
                            this.setChanged(false);
                        }
                    }
                }
            }

        }
        return participantSeatForRegistration;
    }

    private int setBibNumbers(Participant participant, String bibNumber, int subBibIndex) {
        if (participant instanceof ParticipantSingle) {
            participant.bibNumber = bibNumber + (subBibIndex > 0 ? "/" + subBibIndex : "");
            if (subBibIndex > 0)
                subBibIndex++;
        } else if (participant instanceof ParticipantTeam) {
            participant.bibNumber = bibNumber + (subBibIndex > 0 ? "/" + subBibIndex : "");
//            if(subBibIndex>0)
//                subBibIndex++;
            subBibIndex = ((ParticipantTeam) participant).participantTeamMembers.size() > 1 ? 1 : (subBibIndex > 0 ? subBibIndex : 0);
            for (ParticipantTeamMember participantTeamMember : ((ParticipantTeam) participant).participantTeamMembers) {
                subBibIndex = setBibNumbers(participantTeamMember.participant, participant.bibNumber, subBibIndex);
            }
        }
        return subBibIndex;

    }

    private ParticipantSeat getParticipantSeatFor(Participant participant) {
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (participantSeat.contains(participant)) {
                return participantSeat;
            }
        }
        return null;
    }

//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toSimpleDescription());
//        description.append("[participantSeats]").append(System.lineSeparator());
//        for (ParticipantSeat participantSeat : participantSeats) {
//            description.append(participantSeat.toString()).append(System.lineSeparator());
//        }
//        description.append(this.getCompetitionSeed().toDescription()).append(System.lineSeparator());
//        return description.toString();
//    }

    private String getNextBibNumber() {
        return getIdGenerator().getBibNumber();
    }

//    public String toSimpleDescription() {
//        String description = toString() + System.lineSeparator() +
//                "ExpectedGlobalAverageDuration:" + this.getExpectedGlobalDuration().avg + System.lineSeparator() +
//                "ExpectedGlobalMaximumDuration:" + this.getExpectedGlobalDuration().max + System.lineSeparator() +
//                "ExpectedGlobalMinimumDuration:" + this.getExpectedGlobalDuration().min + System.lineSeparator() +
//                "ExpectedGlobalAveragePlay:" + this.getExpectedGlobalPlay().avg + System.lineSeparator() +
//                "ExpectedGlobalMaximumPlay:" + this.getExpectedGlobalPlay().max + System.lineSeparator() +
//                "ExpectedGlobalMinimumPlay:" + this.getExpectedGlobalPlay().min + System.lineSeparator() +
//                "ExpectedParticipantAverageDuration:" + this.getExpectedParticipantDuration().avg + System.lineSeparator() +
//                competitionComputationParam.toSimpleDescription();
//
//        return description;
//    }

//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalAverageDuration:").append(this.getExpectedGlobalDuration().avg).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalMaximumDuration:").append(this.getExpectedGlobalDuration().max).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalMinimumDuration:").append(this.getExpectedGlobalDuration().min).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalAveragePlay:").append(this.getExpectedGlobalPlay().avg).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalMaximumPlay:").append(this.getExpectedGlobalPlay().max).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedGlobalMinimumPlay:").append(this.getExpectedGlobalPlay().min).append(System.lineSeparator());
//        description.append(indentation).append("ExpectedParticipantAverageDuration:").append(this.getExpectedParticipantDuration().avg).append(System.lineSeparator());
//        description.append(indentation).append("ParticipantType:").append(getCompetitionComputationParam().participantType).append(System.lineSeparator());
//        description.append(indentation).append("PlayVersusType:").append(getCompetitionComputationParam().playVersusType).append(System.lineSeparator());
//        description.append(indentation).append("[participantSeats]").append(System.lineSeparator());
//        for (ParticipantSeat participantSeat : participantSeats) {
//            description.append(participantSeat.toDescriptionTree(level + 1));
//        }
//        description.append(this.getCompetitionSeed().toDescriptionTree(level + 1));
//        return description.toString();
//    }
//
//    public String toDetailedDescriptionXmlStringOutput() throws ParserConfigurationException, TransformerException {
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//        DOMSource source = new DOMSource(toDetailedDescriptionXml());
//        StringWriter writer = new StringWriter();
//        StreamResult result = new StreamResult(writer);
//        transformer.transform(source, result);
//        String resultString = writer.toString();
//        return resultString;
//    }

//    public Document toDetailedDescriptionXml() throws ParserConfigurationException {
//        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//        // root elements
//        Document document = docBuilder.newDocument();
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("numberOfParticipantMatch", "" + getCompetitionComputationParam().numberOfParticipantMatch);
//        element.setAttribute("playDurationAverage", "" + getExpectedGlobalDuration().avg);
//        element.setAttribute("participantType", "" + getCompetitionComputationParam().participantType);
//        element.setAttribute("playVersusType", "" + getCompetitionComputationParam().playVersusType);
//
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//
//        Element elementParticipantSeats = document.createElement(ParticipantSeat.class.getSimpleName() + "s");
//
//        for (ParticipantSeat participantSeat : participantSeats) {
//            elementParticipantSeats.appendChild(participantSeat.toDescriptionXml(document));
//        }
//
//        element.appendChild(elementParticipantSeats);
//
//        if (this.getCompetitionSeed() != null) {
//            element.appendChild(this.getCompetitionSeed().toDescriptionXml(document));
//        }
//        document.appendChild(element);
//        return document;
//    }

//    public String toResultListXmlStringOutput() throws ParserConfigurationException, TransformerException {
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//        DOMSource source = new DOMSource(toResultListXml());
//        StringWriter writer = new StringWriter();
//        StreamResult result = new StreamResult(writer);
//        transformer.transform(source, result);
//        String resultString = writer.toString();
//        return resultString;
//    }
//
//    public Document toResultListXml() throws ParserConfigurationException {
//        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//        // root elements
//        Document document = docBuilder.newDocument();
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", internationalizedLabel.defaultLabel);
//        element.setAttribute("numberOfParticipantMatch", "" + getCompetitionComputationParam().numberOfParticipantMatch);
//        element.setAttribute("playDurationAverage", "" + getExpectedGlobalDuration().avg);
//        element.setAttribute("participantType", "" + getCompetitionComputationParam().participantType);
//        element.setAttribute("playVersusType", "" + getCompetitionComputationParam().playVersusType);
//
//        if (participantResults != null && !participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        document.appendChild(element);
//        return document;
//    }

//    public String toSimpleDescriptionXmlStringOutput() {
//        String resultString = "";
//        try {
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = null;
//            transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//            DOMSource source = new DOMSource(toSimpleDescriptionXml(false));
//            StringWriter writer = new StringWriter();
//            StreamResult result = new StreamResult(writer);
//            transformer.transform(source, result);
//            resultString = writer.toString();
//        } catch (ParserConfigurationException | TransformerException e) {
//            e.printStackTrace();
//        }
//        return resultString;
//    }


//    public CompetitionMatch getCompetitionMatch(int localId) {
//        CompetitionMatch competitionMatch = null;
//        competitionMatch = this.getCompetitionSeed().getCompetitionMatch(localId);
//        if (competitionMatch == null)
//            for (CompetitionSeed competitionSeed : competitionSeeds) {
//                competitionMatch = this.getCompetitionSeed().getCompetitionMatch(localId);
//                if (competitionMatch != null)
//                    break;
//            }
//        return competitionMatch;
//    }

//    public Document toSimpleDescriptionXml(boolean withResult) throws ParserConfigurationException {
//        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//        // root elements
//        Document document = docBuilder.newDocument();
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", internationalizedLabel.defaultLabel);
//        element.setAttribute("game", game);
//        element.setAttribute("gameEditor", gameEditor);
//        element.setAttribute("numberOfParticipantMatch", "" + getCompetitionComputationParam().numberOfParticipantMatch);
//        element.setAttribute("playDurationAverage", "" + getExpectedGlobalDuration().avg);
//        element.setAttribute("participantType", "" + getCompetitionComputationParam().participantType);
//        element.setAttribute("playVersusType", "" + getCompetitionComputationParam().playVersusType);
//
//        if (this.getCompetitionSeed() != null) {
//            element.appendChild(this.getCompetitionSeed().toSimpleDescriptionXml(document, withResult));
//        }
//        if (withResult && participantResults != null && !participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        document.appendChild(element);
//        return document;
//    }

    @Override
    public void updateResultDependencies() {
        if (isSubParticipantResultsSet() && this.isSubClosed()) {
            setChanged();
            notifyObservers(this.localId);
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
            close();
        }
    }

    @Override
    public void close() {

        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
            fillCompetitionParticipantResult();
            resetStatistics();
            fillStatistics();
        }
    }

    @Override
    public String toString() {
        return "CompetitionInstance{" +
                "localId=" + localId +
                ",status=" + competitionObjectStatus +
//                ",GlobalDuration:" + getExpectedGlobalMinimumDuration() + "/" + getExpectedGlobalMaximumDuration() + "/" + getExpectedGlobalAverageDuration() +
//                ",GlobalStep:" + getExpectedGlobalMinimumStep() + "/" + getExpectedGlobalMaximumStep() + "/" + getExpectedGlobalAverageStep() +
//                ",ParticipantDuration:" + getExpectedParticipantMinimumDuration() + "/" + getExpectedParticipantMaximumDuration() + "/" + getExpectedParticipantAverageDuration() +
//                ",ParticipantPlay:" + getExpectedParticipantMinimumPlay() + "/" + getExpectedParticipantMaximumPlay() + "/" + getExpectedParticipantAveragePlay() +

//                ",game=" + game +
//                ",gameEditor=" + gameEditor +
//                ",participantSeats=" + participantSeats.size() +
//                ",playDurationAverage=" + getCompetitionComputationParam().playDurationAverage +
//                ",name=" + internationalizedLabel.defaultLabel +
                '}';
    }

    public ParticipantTeam findParticipantTeam(String localId) {
        ParticipantTeam participantTeam = null;
        Participant participant = null;
        for (ParticipantSeat participantSeat : participantSeats) {
            participant = participantSeat.findParticipant(localId);

            if (participant != null && participant instanceof ParticipantTeam) {
                participantTeam = (ParticipantTeam) participant;
                break;
            }
        }
        return participantTeam;
    }

    public Participant findParticipantForParticipantSeat(String localId) {
        Participant participant = null;
        for (ParticipantSeat participantSeat : participantSeats) {
//            if (participantSeat.participant != null && participantSeat.participant.localId.compareTo(localId) == 0 && participantSeat.participant instanceof ParticipantTeam) {
            participant = participantSeat.findParticipant(localId);
            if (participant != null)
                break;
//            }
        }
        return participant;
    }

    public ParticipantSingle findParticipantSingle(String localId) {
        ParticipantSingle participantSingle = null;
        Participant participant = null;
        for (ParticipantSeat participantSeat : participantSeats) {
            participant = participantSeat.findParticipant(localId);
            if (participant != null) {
                if (participant instanceof ParticipantSingle) {
                    participantSingle = (ParticipantSingle) participant;
                    break;
                }
            }
        }
        return participantSingle;
    }

    public CompetitionGroupResult getCompetitionGroupResultFromRegistrations() {
        CompetitionGroupResult competitionGroupResult = null;
        if (this.getCompetitionPhase() != null)
            competitionGroupResult = this.getCompetitionPhase().getCompetitionSeeds().first().getCompetitionGroups().first().getCompetitionGroupResult();
//        else if (this.getCompetitionSeed() != null)
//            competitionGroupResult = this.getCompetitionSeed().getCompetitionGroups().first().getCompetitionGroupResult();
        return competitionGroupResult;
    }

//    public CompetitionPlay getCompetitionPlay(Integer localId) {
//        CompetitionPlay competitionPlay = null;
//        competitionPlay = this.getCompetitionSeed().getCompetitionPlay(localId);
//        if (competitionPlay == null)
//            for (CompetitionSeed competitionSeed : competitionSeeds) {
//                competitionPlay = this.getCompetitionSeed().getCompetitionPlay(localId);
//                if (competitionPlay != null)
//                    break;
//            }
//        return competitionPlay;
//    }

//    public CompetitionMatch getCompetitionMatch(Integer localId) {
//        CompetitionMatch competitionMatch = null;
//        if (this.getCompetitionSeed() != null)
//            competitionMatch = this.getCompetitionSeed().getCompetitionMatch(localId);
//        return competitionMatch;
//    }

    private CompetitionGroupResult fillFromRegistrations() throws CompetitionInstanceGeneratorException {
        CompetitionGroupResult competitionGroupResult = null;
//        if (this.competitionSeed == null) {
        if (this.competitionPhase == null) {
            CompetitionPhase competitionPhaseSeeding = getOrCreatePhaseSeeding();
            if (competitionPhaseSeeding.competitionSeeds.isEmpty()) {
                CompetitionSeed competitionSeed = this.createCompetitionSeed(competitionPhaseSeeding, null, StepType.EMPTY);
                competitionSeed.round = 0;
                competitionSeed.emptyPhase = true;
                competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;
                competitionSeed.participantPairingMethod = ParticipantPairingMethod.INITIAL;
//            this.competitionSeed = competitionSeed;
                this.competitionSeeds.add(competitionSeed);
                CompetitionGroup competitionGroup = this.createCompetitionGroup(competitionSeed);
                competitionGroup.competitionGroupFormat = CompetitionGroupFormat.NONE;
                competitionGroupResult = createCompetitionGroupResult(competitionGroup);
            }
        } else {
            competitionGroupResult = this.competitionPhase.competitionSeeds.first().getCompetitionGroups().first().competitionGroupResult;
        }
        competitionGroupResult.fillRegistrationsAsParticipantResult();
        CompetitionGroup competitionGroup = this.competitionPhase.competitionSeeds.first().getCompetitionGroups().first();
        competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
        competitionGroup.competitionSeed.fillCompetitionSeedResultFromCompetitionGroup(competitionGroup);
        competitionGroup.competitionSeed.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
        this.competitionPhase.fillCompetitionPhaseResultFromCompetitionSeeds();
        this.setChanged();
        notifyObservers(this.localId);
        this.competitionPhase.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
        if (this.competitionPhase.nextCompetitionPhases != null) {
            for (CompetitionPhase nextCompetitionPhase :
                    this.competitionPhase.nextCompetitionPhases) {
                nextCompetitionPhase.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
            }
        }
        if (this.competitionPhase.nextCompetitionPhases != null) {
            for (CompetitionPhase nextCompetitionPhase :
                    this.competitionPhase.nextCompetitionPhases) {
                nextCompetitionPhase.initializePhase();
            }
        }

//        this.competitionPhase.close();

        return competitionGroupResult;
    }

    private CompetitionPhase getOrCreatePhaseSeeding() {
        CompetitionPhase competitionPhaseSeeding = null;
        for (CompetitionPhase competitionPhaseCurrent : competitionPhases) {
            if (competitionPhaseCurrent.phaseType.compareTo(PhaseType.SEEDING) == 0)
                competitionPhaseSeeding = competitionPhaseCurrent;
        }
        if (competitionPhaseSeeding == null) {
            CompetitionPhase competitionPhase = this.createCompetitionPhase();
            competitionPhase.round = 0;
            competitionPhase.phaseType = PhaseType.SEEDING;
            competitionPhase.lane = 0;
            competitionPhaseSeeding = competitionPhase;
        }
        this.competitionPhases.add(competitionPhaseSeeding);
        this.competitionPhase = competitionPhaseSeeding;
        competitionPhaseSeeding.competitionInstance = this;
        competitionPhaseSeeding.competitionCreationParamPhase = this.competitionComputationParam.getFirstPhase();
        return competitionPhaseSeeding;
    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        for (ParticipantResult participantResult : participantResults) {
            participantScore = participantResult.findParticipantScore(localId);
            if (participantScore != null)
                break;
        }
        if (participantScore == null) {
            if (this.getCompetitionPhase() != null)
                this.getCompetitionPhase().findParticipantScore(localId);
//            else if (this.getCompetitionSeed() != null)
//                participantScore = this.getCompetitionSeed().findParticipantScore(localId);
        }
        if (participantScore == null)
            for (CompetitionPhase competitionPhase : competitionPhases) {
                participantScore = competitionPhase.findParticipantScore(localId);
                if (participantScore != null)
                    break;
            }
//        if (participantScore == null)
//            logger.severe("participantScore(" + localId + ") not found");
        return participantScore;
    }

    @Override
    @JsonIgnore
    @XmlTransient
    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public void fillDescriptionTable() {
        descriptionTable = new DescriptionTable();
        descriptionTable.append("type", this.getClass().getSimpleName());
        descriptionTable.append("localId", localId);
        descriptionTable.append("AvgDuration", this.getExpectedGlobalDuration().avg);
        descriptionTable.append("MaxDuration", this.getExpectedGlobalDuration().max);
        descriptionTable.append("MinDuration", this.getExpectedGlobalDuration().min);
        descriptionTable.append("AvgPlay", this.getExpectedGlobalPlay().avg);
        descriptionTable.append("MaxPlay", this.getExpectedGlobalPlay().max);
        descriptionTable.append("MinPlay", this.getExpectedGlobalPlay().min);
        descriptionTable.append("Participant", getCompetitionComputationParam().participantType);
//        descriptionTable.append("Versus", getCompetitionComputationParam().playVersusType);
        descriptionTable.append("SeatsQty", participantSeats.size());


        if (this.getCompetitionPhase() != null)
            this.getCompetitionPhase().findParticipantScore(localId);
//        else if (this.getCompetitionSeed() != null)
//            descriptionTable.append(this.getCompetitionSeed().toDescriptionTable());
    }


    public List<ParticipantResult> getRegistererParticipantRankingFinal() {
        List<ParticipantResult> registererResults = new ArrayList<>();
        ParticipantResult registererResult = null;
        PlayVersusType playVersusType = null;
        if (playVersusType == null && getCompetitionComputationParam().getMixingPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getMixingPhaseParameter().playVersusType;
        }
        if (playVersusType == null && getCompetitionComputationParam().getQualificationPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getQualificationPhaseParameter().playVersusType;
        }
        if (playVersusType == null && getCompetitionComputationParam().getFinalPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getFinalPhaseParameter().playVersusType;
        }

        for (ParticipantResult participantResult : participantResults) {
            registererResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
            registererResult.rank = participantResult.rank;
            registererResult.participantScore = participantResult.participantScore;
            if (playVersusType.teamSize > 1) {
                registererResult.setParticipant(participantResult.getParticipant());
            } else if (playVersusType.teamSize == 1) {
                registererResult.setParticipant(((ParticipantTeam) participantResult.getParticipant()).participantTeamMembers.first().getParticipant());
            }
            registererResults.add(registererResult);
        }
        return registererResults;
    }

    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            if (this.getCompetitionPhase() != null)
                overForParticipant = this.getCompetitionPhase().isOverForParticipant(participant);
//            else if (this.getCompetitionSeed() != null)
//                overForParticipant = this.getCompetitionSeed().isOverForParticipant(participant);
            if (!isParticipantResultSet(participant))
                overForParticipant = false;
        }
        return overForParticipant;
    }

    @Override
    public CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return null;
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        return competitionObjectWithResults;
    }


    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        return competitionObjectWithResults;
    }

    @Override
    public SortedSet<ParticipantPairing> getParticipantPairings() {
        if (this.getCompetitionPhase() != null)
            return this.getCompetitionPhase().getParticipantPairings();
//        else if (this.getCompetitionSeed() != null)
//            return this.getCompetitionSeed().getParticipantPairings();
        return new TreeSet<>();
    }

    public void fillWithFakeRegistrations() {

        for (int i = 0; i < this.getCompetitionComputationParam().numberOfParticipantCompetition; i++) {
            createFakeRegistration(i);
        }


    }

    public ParticipantSeat createFakeRegistration(int index) {
        Participant participant = this.getCompetitionComputationParam().participantType.createParticipant(this);
        participant.equalityComparison = Double.parseDouble(participant.localId);
        ParticipantSeat participantSeat = this.subscribe(participant, index < this.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? index + 1 : null);
        return participantSeat;
    }

    public boolean isInitialized() {
        return competitionObjectStatus.compareTo(CompetitionObjectStatus.WAIT_FOR_START) == 0;
    }


    @Override
    public void open() throws CompetitionInstanceGeneratorException {
        if (this.isInitialized()) {
            this.startDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
            this.currentWave = 1;
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            if (this.getCompetitionPhase() != null) {
                if (this.getCompetitionPhase().nextCompetitionPhases != null) {
                    for (CompetitionPhase nextCompetitionPhase :
                            this.getCompetitionPhase().nextCompetitionPhases) {
//                        nextCompetitionPhase.initializePhase();
                        nextCompetitionPhase.open();
                    }
                }
//            } else if (this.getCompetitionSeed() != null) {
//                this.getCompetitionSeed().initializeSeed();
//                this.getCompetitionSeed().open();
            }
        }
    }

    public CompetitionInstanceTree getCompetitionInstanceTree() {
        CompetitionInstanceTree competitionInstanceTree = new CompetitionInstanceTree();
        competitionInstanceTree.requestedDuration = competitionComputationParam.competitionDuration;
        competitionInstanceTree.expectedDuration = this.expectedDuration;
        competitionInstanceTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionInstanceTree.expectedRelativeEndTime = this.expectedRelativeEndTime;
        competitionInstanceTree.over = isOver();
        competitionInstanceTree.filled = !getParticipantPairings().isEmpty();

        competitionInstanceTree.internationalizedLabel = internationalizedLabel;
        Set<CompetitionGroupResult> competitionGroupResults = computeLastCompetitionGroupResults();
        Set<CompetitionSeed> competitionSeedTreeIdsDone = new HashSet<>();
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            if (!competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionGroups.isEmpty() && !competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionGroups.first().competitionRounds.isEmpty()) {
                if (!competitionSeedTreeIdsDone.contains(competitionGroupResult.getCompetitionGroup().competitionSeed)) {
                    competitionInstanceTree.competitionPhaseTrees.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().getCompetitionPhaseTree());
                    competitionSeedTreeIdsDone.add(competitionGroupResult.getCompetitionGroup().competitionSeed);
                }
            }
        }


        ParticipantResultTree participantResultTree = null;
        for (ParticipantResult participantResult : participantResults) {
            participantResultTree = participantResult.toParticipantResultTree(false, new HashSet<>());
            participantResultTree.points =
                    participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue();
            participantResultTree.sliceValue =
                    participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_VALUE).computeNumberValue().intValue();
            participantResultTree.slicePercent =
                    participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_SLICE_PERCENT).computeNumberValue().intValue();
            competitionInstanceTree.participantResultTrees.add(participantResultTree);
        }


        ParticipantResultTree participantRegistrationTree = null;
        for (ParticipantSeat participantSeat : participantSeats) {
            participantRegistrationTree = participantSeat.toParticipantResultTree();
            competitionInstanceTree.participantRegistrationTrees.add(participantRegistrationTree);
        }


        return competitionInstanceTree;
    }

    public DescriptionTable getDescriptionTable() {
        if (descriptionTable == null)
            fillDescriptionTable();
        return descriptionTable;
    }

    public boolean isOver() {
//        return this.participantSeats.size() == this.participantResults.size();
        return this.competitionObjectStatus.compareTo(CompetitionObjectStatus.CLOSED) == 0;
    }

    public void fillCharacteristicDatas() {
//        this.fillCompetitionParticipantResult();
//        this.fillCompetitionMatchLink();
//        this.fillCompetitionInstanceTree();
        this.fillDescriptionTable();
    }

    public void launchSimulation(boolean generation) throws CompetitionInstanceGeneratorException {
        boolean changeWinner = false;
        launchSimulation(generation, changeWinner);
    }

    public boolean launchSimulationCurrentWave(boolean generation) throws CompetitionInstanceGeneratorException {
        boolean changeWinner = false;
        return launchSimulationCurrentWave(generation, changeWinner);
    }

    public void launchSimulation(boolean generation, boolean changeWinner) throws CompetitionInstanceGeneratorException {
        while (launchSimulationCurrentWave(generation, changeWinner)) ;
    }

    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (competitionPlay.isOpen())
                competitionPlaysFound.add(competitionPlay);
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    public SortedSet<CompetitionPlay> getInitializedCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (CompetitionPlay competitionPlay : this.competitionPlays) {
            if (competitionPlay.isInitialized())
                competitionPlaysFound.add(competitionPlay);
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    public SortedSet<CompetitionMatch> getOpenCompetitionMatches() {
        SortedSet<CompetitionMatch> competitionMatchesFound = new TreeSet<>();
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (competitionMatch.isOpen())
                competitionMatchesFound.add(competitionMatch);
        }
        Sets.sort(competitionMatchesFound);
        return competitionMatchesFound;
    }

    public SortedSet<CompetitionGroup> getOpenCompetitionGroups() {
        SortedSet<CompetitionGroup> competitionGroupsFound = new TreeSet<>();
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (competitionGroup.isOpen())
                competitionGroupsFound.add(competitionGroup);
        }
        Sets.sort(competitionGroupsFound);
        return competitionGroupsFound;
    }

    public SortedSet<CompetitionMatch> getInitializedCompetitionMatches() {
        SortedSet<CompetitionMatch> openCompetitionMatches = new TreeSet<>();
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (competitionMatch.isInitialized())
                openCompetitionMatches.add(competitionMatch);
        }
        Sets.sort(openCompetitionMatches);
        return openCompetitionMatches;
    }

    @Override
    protected void sortParticipantPairings() {

    }

    public void writeCartouche(String competitionCartoucheFileName, String cssUrl, String cssContent) throws FileNotFoundException {
        String htmlCartouche = getHtmlCartouche(true, cssUrl, cssContent);
        PrintWriter printWriter = new PrintWriter(competitionCartoucheFileName);
        printWriter.print(htmlCartouche);
        printWriter.flush();
        printWriter.close();

    }


    public String getHtmlCartouche(boolean standalone, String cssUrl, String cssContent) {
        return this.getCompetitionInstanceTree().toHtmlCartouche(standalone, cssUrl, cssContent);
    }

    public boolean launchSimulationFull(boolean generation, boolean changeWinner) throws CompetitionInstanceGeneratorException {
        List<CompetitionPlay> competitionPlays = new ArrayList<>();
        createCompetitionMatchSimulationForLadder();
        competitionPlays.addAll(getOpenCompetitionPlays());
        while (!competitionPlays.isEmpty()) {
            Collections.shuffle(competitionPlays);
            CompetitionPlay competitionPlaySelected = null;
//            int round = 10;
//            while (competitionPlaySelected == null) {
//                for (CompetitionPlay competitionPlay : competitionPlays) {
//                    if (competitionPlay.competitionGroup.round == round) {
//                        competitionPlaySelected = competitionPlay;
//                        break;
//                    }
//                }
//                if (competitionPlaySelected != null)
//                    break;
//                round--;
//            }
            if (competitionPlaySelected == null)
                competitionPlaySelected = competitionPlays.get(0);
            launchSimulationForCompetitionPlay(competitionPlaySelected, generation, changeWinner);
            competitionPlays.clear();
            competitionPlays.addAll(getOpenCompetitionPlays());
            if (competitionPlays.isEmpty()) {
                CompetitionMatch competitionMatch = createCompetitionMatchSimulationForLadder();
                competitionPlays.addAll(getOpenCompetitionPlays());

            }

        }
        return this.isOver();
    }

    public boolean launchSimulationCurrentWave(boolean generation, boolean changeWinner) throws CompetitionInstanceGeneratorException {
        return launchSimulationCurrentWave(generation, changeWinner, 0);
    }

    public boolean launchSimulationCurrentWave(boolean generation, boolean changeWinner, int matchPerWave) throws CompetitionInstanceGeneratorException {
        createCompetitionMatchSimulationForLadder();
        List<CompetitionPlay> competitionPlays = new ArrayList<>();
        competitionPlays.addAll(getOpenCompetitionPlays());
        Collections.sort(competitionPlays);
        Collections.reverse(competitionPlays);
//
//        if (competitionPlays.isEmpty()) {
//            competitionPlays.clear();
//            competitionPlays.addAll(getInitializedCompetitionPlays());
//            for (CompetitionPlay competitionPlay : competitionPlays)
//                competitionPlay.open();
//            competitionPlays.clear();
//            competitionPlays.addAll(getOpenCompetitionPlays());
//        }
//
//        if (competitionPlays.isEmpty()) {
//            SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
//            competitionMatches.addAll(getInitializedCompetitionMatches());
//            for (CompetitionMatch competitionMatch : competitionMatches)
//                competitionMatch.open();
//            competitionPlays.clear();
//            competitionPlays.addAll(getOpenCompetitionPlays());
//
//            if (competitionPlays.isEmpty()) {
//                competitionPlays.clear();
//                competitionPlays.addAll(getInitializedCompetitionPlays());
//                for (CompetitionPlay competitionPlay : competitionPlays)
//                    competitionPlay.open();
//                competitionPlays.clear();
//                competitionPlays.addAll(getOpenCompetitionPlays());
//            }
//        }
//
//        if (competitionPlays.isEmpty()) {
//            SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
//            competitionMatches.addAll(getInitializedCompetitionMatches());
//            for (CompetitionMatch competitionMatch : competitionMatches)
//                competitionMatch.open();
//            competitionPlays.clear();
//            competitionPlays.addAll(getOpenCompetitionPlays());
//
//            if (competitionPlays.isEmpty()) {
//                competitionPlays.clear();
//                competitionPlays.addAll(getInitializedCompetitionPlays());
//                for (CompetitionPlay competitionPlay : competitionPlays)
//                    competitionPlay.open();
//                competitionPlays.clear();
//                competitionPlays.addAll(getOpenCompetitionPlays());
//            }
//        }
        boolean maxMatchPerWaveSet = false;
        if (matchPerWave > 0)
            maxMatchPerWaveSet = true;
        if (!competitionPlays.isEmpty()) {
//            if (logger.isLoggable(Level.FINE))
//            logger.log(Level.FINE, "competitionPlays - Wave " + currentWave + " - competitionPlaySize : " + competitionPlays.size());
            for (CompetitionPlay competitionPlay : competitionPlays) {
                launchSimulationForCompetitionPlay(competitionPlay, generation, changeWinner);
                if (maxMatchPerWaveSet) {
                    matchPerWave--;
                    if (matchPerWave == 0)
                        break;
                }
            }
            currentWave++;
        }
        return !competitionPlays.isEmpty();
    }

    private CompetitionMatch createCompetitionMatchSimulationForLadder() {
        boolean recursion = true;
        return createCompetitionMatchSimulationForLadder(true);
    }

    private CompetitionMatch createCompetitionMatchSimulationForLadder(boolean recursion) {
        CompetitionMatch competitionMatch = null;
        if (getOpenCompetitionPlays().isEmpty()) {
            for (CompetitionGroup competitionGroup : getOpenCompetitionGroups()) {
                if (competitionGroup.isOpen() && competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                    competitionMatch = competitionGroup.createCompetitionMatchSimulationForLadder();
                    if (competitionMatch == null) {
                        if (competitionGroup.competitionGroupResult.isParticipantsResultSet() || (competitionGroup.competitionPhase.competitionCreationParamPhase.registrationOnTheFly && competitionGroup.participantSeats.size() < competitionGroup.expectedParticipantQuantity)) {
                            if (competitionMatch == null && competitionGroup.participantSeats.size() < competitionGroup.expectedParticipantQuantity) {
                                while (competitionMatch == null && competitionGroup.participantSeats.size() < competitionGroup.expectedParticipantQuantity) {
                                    competitionMatch = competitionGroup.createCompetitionMatchSimulationForLadder();
                                }
                            } else {
                                competitionMatch = competitionGroup.createCompetitionMatchSimulationForLadder();
                            }
                        }
                        if (competitionMatch == null) {
                            try {
                                competitionGroup.close();
                                if (recursion)
                                    createCompetitionMatchSimulationForLadder(false);
                            } catch (CompetitionInstanceGeneratorException e) {
                            }
                        }
                    }
                }
            }
        }
        return competitionMatch;
    }

    public void launchSimulationForCompetitionPlay(CompetitionPlay competitionPlay, boolean generation, boolean changeWinner) throws CompetitionInstanceGeneratorException {
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

        boolean highScoreMode = competitionCreationParamPhase.playVersusType.numberOfTeam == 1;

        boolean competitionPlayScoreMode = competitionCreationParamPhase.isCompetitionPlayScoreMode();
        boolean competitionMatchRankingScoreBased = competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
        ScoreScaleType playParticipantScoreScaleType = null;
        ParticipantScoreType playParticipantScoreType = null;
        String playParticipantScoreName = null;
        if (competitionPlayScoreMode) {
            playParticipantScoreType = competitionCreationParamPhase.getPlayParticipantScoreType();
            playParticipantScoreName = competitionCreationParamPhase.getPlayParticipantScoreName();
            playParticipantScoreScaleType = competitionCreationParamPhase.getPlayParticipantScoreScaleType();
        }
//        ScoringConfiguration scoringConfiguration = competitionCreationParamPhase.scoringConfiguration;
//        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
//            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
//                if (scoringConfigurationPlayElement.userInput) {
//                    scoringMode = true;
//                }
//            }
//        }
        String competitionMatchRankingScoreBasedThreshold = null;
        ScoreThresholdType scoreThresholdType = null;
        if (competitionMatchRankingScoreBased)
            competitionMatchRankingScoreBasedThreshold = competitionCreationParamPhase.getCompetitionMatchRankingScoreThreshold();
        scoreThresholdType = competitionCreationParamPhase.getCompetitionMatchRankingScoreThresholdType();

        launchSimulationForCompetitionPlay(competitionPlay, generation, changeWinner, competitionPlayScoreMode, playParticipantScoreType, playParticipantScoreScaleType, playParticipantScoreName, competitionMatchRankingScoreBased, competitionMatchRankingScoreBasedThreshold, scoreThresholdType, highScoreMode);
    }

    private void launchSimulationForCompetitionPlay(CompetitionPlay competitionPlay, boolean generation, boolean changeWinner, boolean competitionPlayScoreMode, ParticipantScoreType playParticipantScoreType, ScoreScaleType playParticipantScoreScaleType, String playParticipantScoreName, boolean competitionMatchRankingScoreBased, String competitionMatchRankingScoreBasedThreshold, ScoreThresholdType scoreThresholdType, boolean highScoreMode) throws CompetitionInstanceGeneratorException {
        competitionPlay.open();
//                if (logger.isLoggable(Level.FINE))
//                    if (index == competitionPlays.size())
//                        logger.log(Level.FINE, "competitionPlays - Wave " + currentWave + " - " + index + " / " + competitionPlays.size() + " - LAST");
//                    else
//                        logger.log(Level.FINE, "competitionPlays - Wave " + currentWave + " - " + index + " / " + competitionPlays.size());


        List<Participant> participants = competitionPlay.participantPairing.getRealParticipantsAsArray();
        Collections.sort(participants);
        if (changeWinner && competitionPlay.competitionSeed.stepType.compareTo(StepType.MERGE) == 0 && !competitionPlay.competitionSeed.nextCompetitionSeeds.isEmpty())
            Collections.reverse(participants);

        List<Integer> scores = new ArrayList<>();
        List<Integer> ranks = new ArrayList<>();

        if (competitionPlayScoreMode) {
            for (int i = 0; i < participants.size(); i++) {
                int score = 0;
                if (competitionMatchRankingScoreBased) {
                    int multiplicator = 1;
                    int divider = 1;
                    if (!generation) {
                        multiplicator = (competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().competitionRounds.size() - (competitionPlay.getCompetitionMatch().getCompetitionRound().round - 1));
                        multiplicator += (competitionPlay.getCompetitionInstance().competitionPhases.size() - (competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().round - 1));
                        if (multiplicator > participants.size() * 2)
                            multiplicator = participants.size() * 2;
                        divider = competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().competitionRounds.size() + 1;
                        divider += competitionPlay.getCompetitionInstance().competitionPhases.size() + 1;
                        if (divider > participants.size() * 2)
                            divider = participants.size() * 2;
                    }
                    int scoreNew = 0;
                    if (playParticipantScoreScaleType.isAscending()) {
                        if (competitionMatchRankingScoreBasedThreshold == null) {
                            if (playParticipantScoreScaleType.isAscending()) {
                                scoreNew = highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : i;
                            } else {
                                scoreNew = highScoreMode ? Integer.parseInt(participants.get(i).localId) : participants.size() - i;
                            }
                        } else {
                            score = highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : ((i + 1) * Integer.parseInt(competitionMatchRankingScoreBasedThreshold) / participants.size());
                            scoreNew = score * multiplicator / divider;
                        }
                    } else {
                        if (competitionMatchRankingScoreBasedThreshold == null) {
                            if (playParticipantScoreScaleType.isAscending()) {
                                scoreNew = highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : participants.size() - i;
                            } else {
                                scoreNew = highScoreMode ? -Integer.parseInt(participants.get(i).localId) : i;
                            }
                        } else {
                            score = highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : (Integer.parseInt(competitionMatchRankingScoreBasedThreshold) - (i * Integer.parseInt(competitionMatchRankingScoreBasedThreshold) / participants.size()));
                            scoreNew = score * multiplicator / divider;
                        }
                    }
                    score = scoreNew;
                } else {
                    if (playParticipantScoreScaleType.isAscending()) {
                        score = highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : i;
                    } else {
                        score = highScoreMode ? Integer.parseInt(participants.get(i).localId) : participants.size() - i;
                    }
                }
                scores.add(score);
            }
        } else {
            for (int i = 0; i < participants.size(); i++) {
                ranks.add(highScoreMode ? participants.size() - Integer.parseInt(participants.get(i).localId) : i + 1);
            }
        }
        SortedSet<ParticipantResult> participantResultsForPlay = new TreeSet<>();

        boolean reverseOrder;
        if (competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().hasFinalReset() && changeWinner) {
            reverseOrder = competitionPlay.round % 2 == 0;
        } else {
            if (competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionGroupResult().competitionSeedNext == null || !competitionPlay.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionGroupResult().competitionSeedNext.hasFinalReset()) {
                reverseOrder = competitionPlay.round % 2 == 1;
            } else {
                reverseOrder = competitionPlay.round % 2 == 0;
            }
        }

        if (changeWinner) {
            if (competitionPlayScoreMode) {
                scores.add(0, scores.get(scores.size() - 1));
                scores.remove(scores.size() - 1);
            } else {
                ranks.add(0, ranks.get(ranks.size() - 1));
                ranks.remove(ranks.size() - 1);
            }
        }
        if (reverseOrder && changeWinner) {
            if (competitionPlayScoreMode) {
                Collections.reverse(scores);
            } else {
                Collections.reverse(ranks);
            }
        }

        int index = 0;
        for (Participant participant : participants) {
            ParticipantResult participantResultForPlay = ParticipantResult.createParticipantResultFor(getIdGenerator(), competitionPlay);
            participantResultForPlay.participant = participant;
            if (!ranks.isEmpty()) {
                participantResultForPlay.rank = ranks.get(index);
            }
            if (!scores.isEmpty()) {
                participantResultForPlay.participantScore.setParticipantScoreValue(playParticipantScoreName, scores.get(index));
            }
            participantResultsForPlay.add(participantResultForPlay);
            index++;
        }
        competitionPlay.setParticipantResults(participantResultsForPlay);
        competitionPlay.close();

        if (!competitionPlay.getCompetitionMatch().isClosed() && competitionPlay.getCompetitionMatch().isSubClosed())
            competitionPlay.getCompetitionMatch().close();
        if (competitionPlay.getCompetitionMatch().isClosed() && competitionPlay.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0 && competitionPlay.competitionGroup.competitionRounds.size() > competitionPlay.competitionPhase.competitionCreationParamPhase.participantQualifiedPerMatch + 1) {
            competitionPlay.competitionRound.competitionGroup.close();
        }
    }

    @Override
    public SortedSet<CompetitionPhase> getSubCompetitionObjectWithResults() {
        SortedSet<CompetitionPhase> competitionObjectWithResults = new TreeSet<>();
        competitionObjectWithResults.addAll(this.competitionPhases);
        return competitionObjectWithResults;
    }

    @Override
    public boolean isParticipantPairingDefined() {
        boolean isParticipantPairingDefined = false;
        if (this.getCompetitionPhase() != null)
            isParticipantPairingDefined = this.getCompetitionPhase() != null && this.getCompetitionPhase().isParticipantPairingDefined();
//        else if (this.getCompetitionSeed() != null)
//            isParticipantPairingDefined = this.getCompetitionSeed() != null && this.getCompetitionSeed().isParticipantPairingDefined();
        return isParticipantPairingDefined;
    }

    @Override
    public CompetitionInstance getUpperCompetitionObjectWithResult() {
        return null;
    }

//    @Override
//    public boolean isClosed() {
//        return super.isClosed();
//    }

    @Override
    public boolean isOpen() {
        return isSubOpen();
    }

    public void initialize() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
            this.fillFromRegistrations();
            this.closeRegistrations();
//            if (this.getCompetitionPhase() != null) {
//                this.getCompetitionPhase().setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
//                this.getCompetitionPhase().competitionSeeds.first().setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
//            } else if (this.getCompetitionSeed() != null) {
//                this.getCompetitionSeed().setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
//            }
//            this.getCompetitionSeed().competitionPhase.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
        }
    }

    public void closeRegistrations() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
    }

    public void resetParticipantResults() {
        this.participantResults.clear();
        this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
    }

    public CompetitionInstance getCompetitionInstance(String localId) {
        CompetitionInstance competitionInstance = null;
        if (this.localId.compareTo(localId) == 0)
            competitionInstance = this;
        return competitionInstance;
    }

    public String getFilePrefix(boolean withTimestamp, boolean simplified) {
        String timestamp = "";
        if (withTimestamp) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss_SSS");
            timestamp = "_" + dateFormat.format(new Date());
        }
        PlayVersusType playVersusType = null;
        if (playVersusType == null && getCompetitionComputationParam().getMixingPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getMixingPhaseParameter().playVersusType;
        }
        if (playVersusType == null && getCompetitionComputationParam().getQualificationPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getQualificationPhaseParameter().playVersusType;
        }
        if (playVersusType == null && getCompetitionComputationParam().getFinalPhaseParameter() != null) {
            playVersusType = getCompetitionComputationParam().getFinalPhaseParameter().playVersusType;
        }
        return localId
                + (databaseId != null ? "_" + databaseId : "")
                + (competitionComputationParam.competitionName != null ? "_" + StringUtil.replaceRiskyCharacters(competitionComputationParam.competitionName) : "")
                + (participantSeats != null ? "_" + participantSeats.size() : null)
                + (!simplified ? ((competitionComputationParam != null ? "_" + competitionComputationParam.participantType + "_" + playVersusType : "")
                + (competitionComputationParam != null && competitionComputationParam.getMixingPhaseParameter() != null ? "_MIXING_" : "")
                + (competitionComputationParam != null && competitionComputationParam.getQualificationPhaseParameter() != null ? "_QUALIFICATION_" : "")
                + (competitionComputationParam != null && competitionComputationParam.getFinalPhaseParameter() != null ? "_FINAL_" : "")
//                + (competitionComputationParam != null && competitionComputationParam.getFinalPhaseParameter() != null && competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted != null && competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.size() == 1 ? "_" + competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.iterator().next() + "_" : "")
//                + (gameEditor != null ? "_" + gameEditor : "")
//                + (game != null ? "_" + game : "")
        ) : "") + timestamp;
    }

    public SortedSet<Participant> getActiveParticipants() {
        return getParticipants(true, false);
    }

    public SortedSet<Participant> getInactiveParticipants() {
        return getParticipants(false, true);
    }

    public SortedSet<Participant> getParticipants(boolean active, boolean inactive) {
        SortedSet<Participant> participantsCache = new TreeSet<>();
        participantsCache = new TreeSet<>();
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null) {
                boolean isOverForParticipant = isOverForParticipant(participantSeat.participant);
                if (active && !isOverForParticipant) {
                    participantsCache.add(participantSeat.participant);
                }
                if (inactive && isOverForParticipant) {
                    participantsCache.add(participantSeat.participant);
                }
            }
        }
        Sets.sort(participantsCache);
        return participantsCache;
    }

    public void setCompetitionParticipantResultSliceValues(Map<String, Integer> participantSliceValueMap) {
        for (ParticipantResult participantResult : this.participantResults) {
            if (participantSliceValueMap.containsKey(participantResult.participant)) {
                boolean hasSliceValue = false;
                List<ParticipantScoreValue> participantScoreValues = participantResult.participantScore.participantScoreValues;
                for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
                    if (participantScoreValue.name.compareTo(ParticipantScoreCompetition.SCORE_SLICE_VALUE) == 0) {
                        participantScoreValue.value = participantSliceValueMap.get(participantResult.participant).toString();
                        hasSliceValue = true;
                        break;
                    }
                }
                if (!hasSliceValue) {
                    ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
                    participantScoreValue.value = participantSliceValueMap.get(participantResult.participant).toString();
                    participantScoreValue.name = ParticipantScoreCompetition.SCORE_SLICE_VALUE;
                    participantScoreValue.priority = 2;
                    participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC;
                    participantResult.participantScore.participantScoreValues.add(participantScoreValue);
                }
            }
        }
    }

    public void clearDatabaseIds() {
        this.databaseId = null;
        this.databaseDefinitionId = null;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases)
                competitionPhase.clearDatabaseId();
        } else {
            if (competitionSeeds != null) {
                for (CompetitionSeed competitionSeed : competitionSeeds)
                    competitionSeed.clearDatabaseId();
            }
        }
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }
        if (participantSeats != null)
            for (ParticipantSeat participantSeat : participantSeats) {
                participantSeat.clearDatabaseId();
            }
    }

    @Override
    public CompetitionInstance getCompetitionInstance() {
        return this;
    }

    @Override
    public void setCompetitionInstance(CompetitionInstance competitionInstance) {

    }

    @Override
    public CompetitionInstance cloneSimplified() {
        CompetitionInstance competitionInstance = null;
        try {
            competitionInstance = this.clone();
            competitionInstance.competitionSeeds = new TreeSet<>();
            if (this.participantResults != null) {
                List<ParticipantResult> participantResults = new ArrayList<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    participantResults.add(participantResult.cloneSimplified());
                }
                competitionInstance.participantResults = new TreeSet<>();
                competitionInstance.participantResults.addAll(participantResults);
            }
//            competitionInstance.fillPairingCache();
//            competitionInstance.fillResultCache();
        } catch (CloneNotSupportedException e) {
        }
        return competitionInstance;
    }

    public ParticipantTeam getParticipantTeam(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        ParticipantTeam participantTeamFound = null;
        for (ParticipantTeam participantTeam : competitionInstance.participantTeams) {
            if (participantTeam.localId.compareTo(localId) == 0) {
                participantTeamFound = participantTeam;
                break;
            }
        }
        return participantTeamFound;
    }

    public ParticipantSingle getParticipantSingle(String localId) {
        CompetitionInstance competitionInstance = this.getCompetitionInstance();
        ParticipantSingle participantSingleFound = null;
        for (ParticipantSingle participantSingle : competitionInstance.participantSingles) {
            if (participantSingle.localId.compareTo(localId) == 0) {
                participantSingleFound = participantSingle;
                break;
            }
        }
        return participantSingleFound;
    }

    @Override
    protected Integer getRoundOrLane() {
        return Integer.valueOf(localId);
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return participantResults;
    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                StatisticsStructure statisticsStructurePhase = competitionPhase.getExpectedGlobalDuration();
                statisticsStructure.min += statisticsStructurePhase.min;
                statisticsStructure.max += statisticsStructurePhase.max;
                statisticsStructure.avg += statisticsStructurePhase.avg;
            }
        }
        return statisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                StatisticsStructure statisticsStructurePhase = competitionPhase.getExpectedGlobalPlay();
                statisticsStructure.min += statisticsStructurePhase.min;
                statisticsStructure.max += statisticsStructurePhase.max;
                statisticsStructure.avg += statisticsStructurePhase.avg;
            }
        }
        return statisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                StatisticsStructure statisticsStructurePhase = competitionPhase.getExpectedParticipantDuration();
                statisticsStructure.min += statisticsStructurePhase.min;
                statisticsStructure.max += statisticsStructurePhase.max;
                statisticsStructure.avg += statisticsStructurePhase.avg;
            }
        }
        return statisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                StatisticsStructure statisticsStructurePhase = competitionPhase.getExpectedParticipantPlay();
                statisticsStructure.min += statisticsStructurePhase.min;
                statisticsStructure.max += statisticsStructurePhase.max;
                statisticsStructure.avg += statisticsStructurePhase.avg;
            }
        }
        return statisticsStructure;
    }

    public ParticipantTeamVoid createParticipantTeamVoid() {
        ParticipantTeamVoid participantTeamVoid = ParticipantTeamVoid.createInstance(this.getIdGenerator());
        addParticipantTeam(participantTeamVoid);
        return participantTeamVoid;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                StatisticsStructure statisticsStructurePhase = competitionPhase.getExpectedGlobalStep();
                statisticsStructure.min += statisticsStructurePhase.min;
                statisticsStructure.max += statisticsStructurePhase.max;
                statisticsStructure.avg += statisticsStructurePhase.avg;
            }
        }
        return statisticsStructure;
    }

/*    public ParticipantSeat addParticipantSeat(ParticipantSeat participantSeat) {
        participantSeat.setCompetitionInstance(this);
        this.participantSeats.add(participantSeat);
//        Sets.sort(participantSeats);
        return participantSeat;
    }*/

    public Participant getParticipantTeamOrSingle(String participantId) {
        Participant participant = getParticipantSingle(participantId);
        if (participant == null)
            participant = getParticipantTeam(participantId);
        return participant;
    }

//    @Deprecated
//    public CompetitionSeed createCompetitionSeed(Set<CompetitionGroupResult> competitionGroupResults, StepType stepType, PhaseType phaseType, CompetitionCreationParamPhase competitionCreationParamPhase) {
//        boolean firstCompetitionSeed = false;
//        if (competitionSeed == null && (competitionGroupResults == null || competitionGroupResults.isEmpty())) {
//            firstCompetitionSeed = true;
//        }
//        CompetitionSeed competitionSeed = null;
////        competitionSeed.setCompetitionInstance(getCompetitionInstance());
//        competitionSeed = CompetitionSeed.createInstance(getIdGenerator());
//        if (competitionCreationParamPhase != null) {
//            competitionSeed.competitionCreationParamPhase = competitionCreationParamPhase;
//            competitionSeed.phase = competitionCreationParamPhase.phaseIndex;
//        }
//        competitionSeed.stepType = stepType;
//        competitionSeed.phaseType = phaseType;
//
//        if (firstCompetitionSeed) {
//            competitionSeed.emptyPhase = true;
//            competitionSeed.round = 0;
//            competitionSeed.phase = 0;
//            competitionSeed.stepType = StepType.EMPTY;
//            competitionSeed.phaseType = PhaseType.SEEDING;
//            this.competitionSeed = competitionSeed;
//        } else {
//            Integer roundMax = null;
//            Integer phasePrevious = null;
//            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
//                competitionGroupResult.competitionSeedNext = competitionSeed;
//                phasePrevious = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionPhase.round;
//                competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResult);
//                if (roundMax == null || (competitionGroupResult.competitionGroup != null && roundMax.compareTo(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().round) < 0))
//                    roundMax = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().round;
//            }
//            competitionSeed.round = roundMax + 1;
//            if (stepType.compareTo(StepType.MERGE) != 0)
//                competitionSeed.phase = phasePrevious + 1;
//            else
//                competitionSeed.phase = phasePrevious;
//        }
//        addCompetitionSeed(competitionSeed);
////        updateCompetitionPhase(competitionSeed.phase);
//
//        return competitionSeed;
//    }


    public CompetitionSeed createCompetitionSeed(CompetitionPhase competitionPhase, Set<CompetitionGroupResult> competitionGroupResults, StepType stepType) {
//        boolean firstCompetitionSeed = false;
//        if (competitionSeed == null && (competitionGroupResults == null || competitionGroupResults.isEmpty())) {
//            firstCompetitionSeed = true;
//        }
        CompetitionSeed competitionSeed = null;
//        competitionSeed.setCompetitionInstance(getCompetitionInstance());
        competitionSeed = CompetitionSeed.createInstance(getIdGenerator());
        competitionSeed.competitionInstance = competitionPhase.competitionInstance;
//        if (competitionCreationParamPhase != null) {
//            competitionSeed.competitionCreationParamPhase = competitionCreationParamPhase;
//            competitionSeed.phase = competitionCreationParamPhase.phaseIndex;
//        }
        competitionSeed.competitionPhase = competitionPhase;
        competitionPhase.competitionSeeds.add(competitionSeed);
        competitionSeed.stepType = stepType;
//        competitionSeed.phaseType = phaseType;

        if (competitionPhase.round == 0) {
            competitionSeed.emptyPhase = true;
            competitionSeed.round = 0;
            competitionSeed.roundInPhase = 1;
//            competitionSeed.phase = 0;
            competitionSeed.stepType = StepType.EMPTY;
//            competitionSeed.phaseType = PhaseType.SEEDING;
//            this.competitionSeed = competitionSeed;
        } else {
            Integer roundMax = null;
            Integer roundInPhaseMax = 0;
//            Integer phasePrevious = null;
            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
                competitionGroupResult.competitionSeedNext = competitionSeed;
                if (competitionGroupResult.competitionSeed.competitionPhase.compareTo(competitionPhase) == 0 && (roundInPhaseMax == null || competitionGroupResult.competitionSeed.roundInPhase.compareTo(roundInPhaseMax) > 0)) {
                    roundInPhaseMax = competitionGroupResult.competitionSeed.roundInPhase;
                }
//                phasePrevious = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionPhase.round;
//                if (competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionPhase.compareTo(competitionPhase) == 0)
//                    phasePrevious--;
                competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResult);
                competitionSeed.previousCompetitionSeeds.add(competitionGroupResult.competitionGroup.competitionSeed);
                competitionGroupResult.competitionGroup.competitionSeed.nextCompetitionSeeds.add(competitionSeed);
                if (roundMax == null || (competitionGroupResult.competitionGroup != null && roundMax.compareTo(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().round) < 0))
                    roundMax = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().round;
            }
            competitionSeed.round = roundMax + 1;
            competitionSeed.roundInPhase = roundInPhaseMax + 1;
//            competitionPhase.round = phasePrevious + 1;
        }
//        if (this.competitionPhase == null)
//            this.competitionPhase = competitionPhase;
//        for (CompetitionPhase competitionPhaseCurrent : competitionPhases) {
//            if (competitionPhaseCurrent.compareTo(competitionPhase) != 0) {
//                if (competitionPhaseCurrent.round.compareTo(competitionPhase.round) == 0)
//                    competitionPhase.lane++;
//                else if (competitionPhaseCurrent.round.compareTo(competitionPhase.round) < 0) {
//                    competitionPhaseCurrent.nextCompetitionPhases.add(competitionPhase);
//                    competitionPhase.previousCompetitionPhases.add(competitionPhaseCurrent);
//                }
//            }
//        }
//        competitionPhases.add(competitionPhase);
        competitionSeeds.add(competitionSeed);
//        addCompetitionSeed(competitionSeed);
//        updateCompetitionPhase(competitionSeed.phase);

        return competitionSeed;
    }

/*
    public void updateCompetitionPhase(Integer round) {
        if (this.competitionPhases == null) {
            this.competitionPhases = new TreeSet<>();
        }

        CompetitionPhase competitionPhase = getCompetitionPhaseForRound(round);
        if (competitionPhase == null)
            competitionPhase = createCompetitionPhaseForRound(round);
        competitionPhase.updateCompetitionObjectStatus();
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.competitionPhase = getCompetitionPhaseForRound(competitionSeed.phase);
            competitionSeed.competitionPhase.phaseType = competitionSeed.phaseType;
            competitionSeed.competitionPhase.emptyPhase = competitionSeed.emptyPhase;
            competitionSeed.competitionPhase.competitionSeeds.add(competitionSeed);
            competitionSeed.competitionPhase.competitionCreationParamPhase = competitionSeed.competitionCreationParamPhase;
        }
//        for (CompetitionPhase competitionPhaseCurrent : competitionPhases) {
//            competitionPhaseCurrent.competitionSeeds.clear();
//            if (competitionPhase == null || competitionPhase.round.compareTo(competitionPhaseCurrent.round) > 0)
//                competitionPhase = competitionPhaseCurrent;
//            for (CompetitionSeed competitionSeed : competitionSeeds) {
//                if (competitionSeed.phase.compareTo(competitionPhaseCurrent.round) == 0)
//                    competitionPhaseCurrent.competitionSeeds.add(competitionSeed);
//            }
//        }
    }
*/

    public CompetitionPhase createCompetitionPhaseForRound(Integer round) {
        CompetitionPhase competitionPhase = createCompetitionPhase();
        competitionPhase.round = round;
        competitionPhase.lane = 1;
        for (CompetitionPhase competitionPhaseOther : this.competitionPhases) {
            if (competitionPhaseOther.round != null) {
                if (competitionPhaseOther.round - 1 == competitionPhase.round) {
                    if (competitionPhaseOther.previousCompetitionPhases == null)
                        competitionPhaseOther.previousCompetitionPhases = new TreeSet<>();
                    competitionPhaseOther.previousCompetitionPhases.add(competitionPhase);
                    if (competitionPhaseOther.nextCompetitionPhases == null)
                        competitionPhaseOther.nextCompetitionPhases = new TreeSet<>();
                    competitionPhase.nextCompetitionPhases.add(competitionPhaseOther);
                } else if (competitionPhaseOther.round + 1 == competitionPhase.round) {
                    if (competitionPhaseOther.nextCompetitionPhases == null)
                        competitionPhaseOther.nextCompetitionPhases = new TreeSet<>();
                    competitionPhaseOther.nextCompetitionPhases.add(competitionPhase);
                    if (competitionPhase.previousCompetitionPhases == null)
                        competitionPhase.previousCompetitionPhases = new TreeSet<>();
                    competitionPhase.previousCompetitionPhases.add(competitionPhaseOther);
                }
                if (competitionPhaseOther.round == competitionPhase.round) {
                    competitionPhase.lane++;
                }
            }
        }
        return competitionPhase;
    }

    public CompetitionPhase createCompetitionPhase() {
        CompetitionPhase competitionPhase = CompetitionPhase.createInstance(getIdGenerator());
        addCompetitionPhase(competitionPhase);
        return competitionPhase;
    }

    public CompetitionPhase getCompetitionPhaseForRound(Integer round) {
        CompetitionPhase competitionPhaseFound = null;
        if (round != null && competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                if (competitionPhase.round.compareTo(round) == 0) {
                    competitionPhaseFound = competitionPhase;
                    break;
                }
            }
        }
        return competitionPhaseFound;
    }

    public CompetitionGroup createCompetitionGroup(CompetitionSeed competitionSeed) {
        CompetitionGroup competitionGroup = CompetitionGroup.createInstance(getIdGenerator());
        competitionGroup.lane = competitionSeed.competitionGroups.size() + 1;
        competitionGroup.competitionPhase = competitionSeed.competitionPhase;
        competitionSeed.addCompetitionGroup(competitionGroup);
        getCompetitionInstance().addCompetitionGroup(competitionGroup);
        return competitionGroup;
    }


    public CompetitionPhase createCompetitionPhase(Set<CompetitionPhase> competitionPhasesPrevious) {
        CompetitionPhase competitionPhase = CompetitionPhase.createInstance(getIdGenerator());
        competitionPhase.round = 0;
        competitionPhase.lane = 0;
        if (competitionPhasesPrevious != null && !competitionPhasesPrevious.isEmpty()) {
            for (CompetitionPhase competitionPhasePrevious : competitionPhasesPrevious) {
                if (competitionPhase.round <= competitionPhasePrevious.round) {
                    competitionPhase.round = competitionPhasePrevious.round + 1;
                }

            }
        }
        if (competitionPhases != null && !competitionPhases.isEmpty()) {
            for (CompetitionPhase competitionPhaseCurrent : competitionPhases) {
                if (competitionPhase.round == competitionPhaseCurrent.round) {
                    competitionPhase.lane++;
                }
            }
        }
        if ((competitionPhase.previousCompetitionPhases == null || competitionPhase.previousCompetitionPhases.isEmpty()) && competitionPhasesPrevious != null && !competitionPhasesPrevious.isEmpty()) {
            competitionPhase.previousCompetitionPhases.addAll(competitionPhasesPrevious);
        }
        return competitionPhase;
    }

    public CompetitionGroupResult createCompetitionGroupResult(CompetitionGroup competitionGroup) {
        CompetitionGroupResult competitionGroupResult = CompetitionGroupResult.createInstance(getIdGenerator());
        competitionGroupResult.setCompetitionInstance(getCompetitionInstance());
        competitionGroupResult.competitionPhase = competitionGroup.competitionPhase;
        if (competitionGroup != null) {
            competitionGroup.setCompetitionGroupResult(competitionGroupResult);
        }
        competitionGroupResults.add(competitionGroupResult);
        return competitionGroupResult;
    }

    public CompetitionRound createCompetitionRound(CompetitionGroup competitionGroup, CompetitionRound competitionRoundPrevious) {
        CompetitionRound competitionRound = CompetitionRound.createInstance(getIdGenerator());
        competitionRound.setCompetitionInstance(getCompetitionInstance());
        competitionRound.competitionGroup = competitionGroup;
        competitionRound.competitionSeed = competitionGroup.competitionSeed;
        competitionRound.competitionPhase = competitionGroup.competitionPhase;
        if (competitionRoundPrevious != null) {
            competitionRound.competitionRoundPrevious = competitionRoundPrevious;
            competitionRoundPrevious.competitionRoundNext = competitionRound;
            competitionRound.round = competitionRoundPrevious.round + 1;
        } else {
            competitionRound.round = 1;
        }
        competitionGroup.addCompetitionRound(competitionRound);
        competitionRounds.add(competitionRound);

        return competitionRound;
    }

    public CompetitionMatch createCompetitionMatch(CompetitionRound competitionRound) {
        CompetitionMatch competitionMatch = CompetitionMatch.createInstance(getIdGenerator());
        competitionMatch.lane = competitionRound.competitionMatches.size() + 1;
        competitionMatch.competitionRound = competitionRound;
        competitionMatch.competitionGroup = competitionRound.competitionGroup;
        competitionMatch.competitionSeed = competitionRound.competitionSeed;
        competitionMatch.competitionPhase = competitionRound.competitionPhase;

        competitionMatch.setCompetitionInstance(getCompetitionInstance());
        competitionRound.addCompetitionMatch(competitionMatch);
        competitionMatches.add(competitionMatch);
        return competitionMatch;
    }

    public CompetitionMatchLink createCompetitionMatchLink(CompetitionMatch previousCompetitionMatch, CompetitionMatch nextCompetitionMatch, Integer participantIndexPrevious, Integer participantRankPrevious, Integer participantIndexNext, Integer participantRankNext) {
        CompetitionMatchLink competitionMatchLink = CompetitionMatchLink.createInstance(getIdGenerator());
        competitionMatchLink.previousCompetitionMatch = previousCompetitionMatch;
        competitionMatchLink.nextCompetitionMatch = nextCompetitionMatch;
        competitionMatchLink.linkThickness = 1;
        competitionMatchLink.matchLinkType = nextCompetitionMatch.competitionGroup.competitionGroupFormat.getMatchLinkType();
        if (competitionMatchLink.matchLinkType.compareTo(MatchLinkType.SELECTING) == 0) {
            if (participantRankNext != null)
                if (competitionMatchLink.nextLinkIndex == null || competitionMatchLink.nextLinkIndex.compareTo(participantIndexNext) > 0)
                    competitionMatchLink.nextLinkIndex = participantIndexNext;
            if (participantRankPrevious != null)
                if (competitionMatchLink.previousLinkIndex == null || competitionMatchLink.previousLinkIndex.compareTo(participantRankPrevious) > 0)
                    competitionMatchLink.previousLinkIndex = participantRankPrevious;
        } else {
            if (participantIndexNext != null)
                if (competitionMatchLink.nextLinkIndex == null || competitionMatchLink.nextLinkIndex.compareTo(participantIndexNext) > 0)
                    competitionMatchLink.nextLinkIndex = participantIndexNext;
            if (participantIndexPrevious != null)
                if (competitionMatchLink.previousLinkIndex == null || competitionMatchLink.previousLinkIndex.compareTo(participantIndexPrevious) > 0)
                    competitionMatchLink.previousLinkIndex = participantIndexPrevious;
        }

        competitionMatchLinks.add(competitionMatchLink);
        previousCompetitionMatch.nextCompetitionMatchLinks.add(competitionMatchLink);
        nextCompetitionMatch.previousCompetitionMatchLinks.add(competitionMatchLink);
        return competitionMatchLink;
    }

    public ParticipantPairing createParticipantPairing(int size) {
        ParticipantPairing participantPairing = ParticipantPairing.createInstance(size, this);
        participantPairing.setCompetitionInstance(this);
        return participantPairing;

    }

    public ParticipantSingle createParticipantSingle() {
        ParticipantSingle participantSingle = ParticipantSingle.createInstance(getIdGenerator());
        addParticipantSingle(participantSingle);
        return participantSingle;
    }

    public ParticipantTeam createParticipantTeam() {
        ParticipantTeam participantTeam = ParticipantTeam.createInstance(getIdGenerator());
        addParticipantTeam(participantTeam);
        return participantTeam;
    }

    public CompetitionPlay createCompetitionPlay(CompetitionMatch competitionMatch) {
        CompetitionPlay competitionPlay = CompetitionPlay.createInstance(getIdGenerator());
        competitionPlay.setCompetitionInstance(getCompetitionInstance());
        competitionPlay.competitionMatch = competitionMatch;
        competitionPlay.competitionRound = competitionMatch.competitionRound;
        competitionPlay.competitionGroup = competitionMatch.competitionGroup;
        competitionPlay.competitionSeed = competitionMatch.competitionSeed;
        competitionPlay.competitionPhase = competitionMatch.competitionPhase;

        competitionMatch.addCompetitionPlay(competitionPlay);
        competitionPlays.add(competitionPlay);
        return competitionPlay;
    }

    public List<CompetitionPlay> getAddedCompetitionPlays() {
        List<CompetitionPlay> addedCompetitionPlays = new ArrayList<>();
        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (competitionPlay.databaseId == null)
                addedCompetitionPlays.add(competitionPlay);
        }
        return addedCompetitionPlays;
    }


//    public Duration getExpectedGlobalMaximumDuration() {
//        fillDurationAndPlayAttributes();
//        return expectedGlobalMaximumDuration;
//    }
//
//    public Duration getExpectedGlobalMinimumDuration() {
//        fillDurationAndPlayAttributes();
//        return expectedGlobalMinimumDuration;
//    }
//
//    public Duration getExpectedGlobalAverageDuration() {
//        fillDurationAndPlayAttributes();
//        return expectedGlobalAverageDuration;
//    }

    public Long getExpectedGlobalMaximumPlay() {
        fillDurationAndPlayAttributes();
        return expectedGlobalMaximumPlay;
    }

    public Long getExpectedGlobalMinimumPlay() {
        fillDurationAndPlayAttributes();
        return expectedGlobalMinimumPlay;
    }

    public Long getExpectedGlobalAveragePlay() {
        fillDurationAndPlayAttributes();
        return expectedGlobalAveragePlay;
    }

    public Duration getExpectedParticipantMaximumDuration() {
        fillDurationAndPlayAttributes();
        return expectedParticipantMaximumDuration;
    }

    public Duration getExpectedParticipantMinimumDuration() {
        fillDurationAndPlayAttributes();
        return expectedParticipantMinimumDuration;
    }

    public Duration getExpectedParticipantAverageDuration() {
        fillDurationAndPlayAttributes();
        return expectedParticipantAverageDuration;
    }

    public Long getExpectedParticipantMaximumPlay() {
        fillDurationAndPlayAttributes();
        return expectedParticipantMaximumPlay;
    }

    public Long getExpectedParticipantMinimumPlay() {
        fillDurationAndPlayAttributes();
        return expectedParticipantMinimumPlay;
    }

    public Long getExpectedParticipantAveragePlay() {
        fillDurationAndPlayAttributes();
        return expectedParticipantAveragePlay;
    }

    public Long getExpectedGlobalMaximumStep() {
        fillDurationAndPlayAttributes();
        return expectedGlobalMaximumStep;
    }

    public Long getExpectedGlobalMinimumStep() {
        fillDurationAndPlayAttributes();
        return expectedGlobalMinimumStep;
    }

    public Long getExpectedGlobalAverageStep() {
        fillDurationAndPlayAttributes();
        return expectedGlobalAverageStep;
    }

    @Override
    public void fillStatistics() {
        super.fillStatistics();
        getPlayPerParticipantStatistics();
//        expectedGlobalAverageDuration = Duration.ofMinutes(this.getExpectedGlobalDuration().avg);
//        expectedGlobalMaximumDuration = Duration.ofMinutes(this.getExpectedGlobalDuration().max);
//        expectedGlobalMinimumDuration = Duration.ofMinutes(this.getExpectedGlobalDuration().min);
        expectedGlobalAveragePlay = this.getExpectedGlobalPlay().avg;
        expectedGlobalMaximumPlay = this.getExpectedGlobalPlay().max;
        expectedGlobalMinimumPlay = this.getExpectedGlobalPlay().min;
        expectedGlobalAverageStep = this.getExpectedGlobalPlay().avg;
        expectedGlobalMaximumStep = this.getExpectedGlobalPlay().max;
        expectedGlobalMinimumStep = this.getExpectedGlobalPlay().min;
        expectedParticipantAverageDuration = Duration.ofMinutes(this.getExpectedParticipantDuration().avg);
        expectedParticipantMaximumDuration = Duration.ofMinutes(this.getExpectedParticipantDuration().max);
        expectedParticipantMinimumDuration = Duration.ofMinutes(this.getExpectedParticipantDuration().min);
        expectedParticipantAveragePlay = this.getExpectedParticipantPlay().avg;
        expectedParticipantMinimumPlay = this.getExpectedParticipantPlay().min;
        expectedParticipantMaximumPlay = this.getExpectedParticipantPlay().max;
    }


    @Override
    public void resetStatistics() {
        super.resetStatistics();
        playPerParticipantStatisticsInitialized = false;
        for (CompetitionPhase competitionPhase : competitionPhases) {
            competitionPhase.resetStatistics();
        }
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.resetStatistics();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.resetStatistics();
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.resetStatistics();
        }
        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.resetStatistics();
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.resetStatistics();
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.resetStatistics();
        }
    }

    public void fillDurationAndPlayAttributes() {
        StatisticsStructure statisticsStructureStep = getExpectedGlobalStep();
        if (competitionPhase != null) {
            if (expectedGlobalMaximumStep == null) {
                expectedGlobalMaximumStep = statisticsStructureStep.max;
            }
            if (expectedGlobalMinimumStep == null) {
                expectedGlobalMinimumStep = statisticsStructureStep.min;
            }
            if (expectedGlobalAverageStep == null) {
                expectedGlobalAverageStep = statisticsStructureStep.avg;
            }
            StatisticsStructure statisticsStructureDuration = getExpectedGlobalDuration();
//            if (expectedGlobalMaximumDuration == null) {
//                expectedGlobalMaximumDuration = Duration.ofMinutes(statisticsStructureDuration.max);
//            }
//            if (expectedGlobalAverageDuration == null) {
//                expectedGlobalAverageDuration = Duration.ofMinutes(statisticsStructureDuration.avg);
//            }
//            if (expectedGlobalMinimumDuration == null) {
//                expectedGlobalMinimumDuration = Duration.ofMinutes(statisticsStructureDuration.min);
//            }

            StatisticsStructure statisticsStructurePlay = getExpectedGlobalPlay();
            if (expectedGlobalMaximumPlay == null) {
                expectedGlobalMaximumPlay = statisticsStructurePlay.max;
            }
            if (expectedGlobalAveragePlay == null) {
                expectedGlobalAveragePlay = statisticsStructurePlay.avg;
            }
            if (expectedGlobalMinimumPlay == null) {
                expectedGlobalMinimumPlay = statisticsStructurePlay.min;
            }
            StatisticsStructure statisticsStructureParticipantDuration = getExpectedParticipantDuration();

            if (expectedParticipantMaximumDuration == null) {
                expectedParticipantMaximumDuration = Duration.ofMinutes(statisticsStructureParticipantDuration.max);
            }
            if (expectedParticipantAverageDuration == null) {
                expectedParticipantAverageDuration = Duration.ofMinutes(statisticsStructureParticipantDuration.avg);
            }
            if (expectedParticipantMinimumDuration == null) {
                expectedParticipantMinimumDuration = Duration.ofMinutes(statisticsStructureParticipantDuration.min);
            }

            StatisticsStructure statisticsStructureParticipantPlay = getExpectedParticipantPlay();
            if (expectedParticipantMaximumPlay == null) {
                expectedParticipantMaximumPlay = statisticsStructureParticipantPlay.max;
            }
            if (expectedParticipantAveragePlay == null) {
                expectedParticipantAveragePlay = statisticsStructureParticipantPlay.avg;
            }
            if (expectedParticipantMinimumPlay == null) {
                expectedParticipantMinimumPlay = statisticsStructureParticipantPlay.min;
            }
        }
    }

    public CompetitionRound addCompetitionRound(CompetitionRound competitionRoundNew) {

        for (CompetitionRound competitionRound : this.competitionRounds) {
            if (competitionRound.localId.compareTo(competitionRoundNew.localId) == 0) {
                this.competitionRounds.remove(competitionRoundNew);
                break;
            }
        }
        competitionRoundNew.setCompetitionInstance(this);

        Sets.sort(this.competitionRounds);
        return competitionRoundNew;
    }

    public CompetitionPlay addCompetitionPlay(CompetitionPlay competitionPlayNew) {
        for (CompetitionPlay competitionPlay : this.competitionPlays) {
            if (competitionPlay.localId.compareTo(competitionPlayNew.localId) == 0) {
                this.competitionPlays.remove(competitionPlayNew);
                break;
            }
        }
        competitionPlayNew.setCompetitionInstance(this);
        this.competitionPlays.add(competitionPlayNew);
        Sets.sort(this.competitionPlays);
        return competitionPlayNew;
    }


    public CompetitionGroup addCompetitionGroup(CompetitionGroup competitionGroupNew) {
        for (CompetitionGroup competitionGroup : this.competitionGroups) {
            if (competitionGroup.localId.compareTo(competitionGroupNew.localId) == 0) {
                this.competitionGroups.remove(competitionGroupNew);
                break;
            }
        }
        competitionGroupNew.setCompetitionInstance(this);
        this.competitionGroups.add(competitionGroupNew);
        Sets.sort(this.competitionGroups);
        return competitionGroupNew;
    }

    public CompetitionMatch addCompetitionMatch(CompetitionMatch competitionMatchNew) {
        for (CompetitionMatch competitionMatch : this.competitionMatches) {
            if (competitionMatch.localId.compareTo(competitionMatchNew.localId) == 0) {
                this.competitionMatches.remove(competitionMatchNew);
                break;
            }
        }
        competitionMatchNew.setCompetitionInstance(this);
        this.competitionMatches.add(competitionMatchNew);
        Sets.sort(this.competitionMatches);
        return competitionMatchNew;
    }

    public CompetitionSeed addCompetitionSeed(CompetitionSeed competitionSeedNew) {

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (competitionSeed.localId.compareTo(competitionSeedNew.localId) == 0) {
                this.competitionSeeds.remove(competitionSeedNew);
                break;
            }
        }
        competitionSeedNew.setCompetitionInstance(this);
        this.competitionSeeds.add(competitionSeedNew);
        Sets.sort(this.competitionSeeds);
        return competitionSeedNew;
    }

    public CompetitionPhase addCompetitionPhase(CompetitionPhase competitionPhaseNew) {
//        competitionPhaseNew.previousCompetitionPhases.clear();
//        competitionPhaseNew.nextCompetitionPhases.clear();
        this.competitionPhases.remove(competitionPhaseNew);
        for (CompetitionPhase competitionPhase : this.competitionPhases) {
            if (competitionPhase.previousCompetitionPhases != null)
                competitionPhase.previousCompetitionPhases.remove(competitionPhaseNew);
            if (competitionPhase.nextCompetitionPhases != null)
                competitionPhase.nextCompetitionPhases.remove(competitionPhaseNew);
        }
        if (competitionPhaseNew.previousCompetitionPhases != null) {
            for (CompetitionPhase previousCompetitionPhase : competitionPhaseNew.previousCompetitionPhases) {
                for (CompetitionPhase competitionPhase : this.competitionPhases) {
                    if (previousCompetitionPhase.compareTo(competitionPhase) == 0) {
                        if (competitionPhase.nextCompetitionPhases == null)
                            competitionPhase.nextCompetitionPhases = new TreeSet<>();
                        competitionPhase.nextCompetitionPhases.add(competitionPhaseNew);
                    }
                }
            }
        }

        if (competitionPhaseNew.nextCompetitionPhases != null) {
            for (CompetitionPhase nextCompetitionPhase : competitionPhaseNew.nextCompetitionPhases) {
                for (CompetitionPhase competitionPhase : this.competitionPhases) {
                    if (nextCompetitionPhase.compareTo(competitionPhase) == 0) {
                        if (competitionPhase.previousCompetitionPhases == null)
                            competitionPhase.previousCompetitionPhases = new TreeSet<>();
                        competitionPhase.previousCompetitionPhases.add(competitionPhaseNew);
                    }
                }
            }
        }
        competitionPhaseNew.setCompetitionInstance(this);
        this.competitionPhases.add(competitionPhaseNew);
        Sets.sort(this.competitionPhases);
        return competitionPhaseNew;
    }

//    public CompetitionGroupResult addCompetitionGroupResult(CompetitionGroupResult competitionGroupResultNew) {
//        for (CompetitionGroupResult competitionGroupResult : this.competitionGroupResults) {
//            if (competitionGroupResult.localId.compareTo(competitionGroupResultNew.localId) == 0) {
//                this.competitionGroupResults.remove(competitionGroupResultNew);
//                break;
//            }
//        }
//        competitionGroupResultNew.setCompetitionInstance(this);
//        this.competitionGroupResults.add(competitionGroupResultNew);
//        Sets.sort(this.competitionGroupResults);
//        return competitionGroupResultNew;
//
//    }

    public ParticipantTeamMember createParticipantTeamMember() {

        ParticipantTeamMember participantTeamMember = ParticipantTeamMember.createInstance(getIdGenerator());
        return participantTeamMember;
    }

    public ParticipantSeat createParticipantSeat() {
        ParticipantSeat participantSeat = ParticipantSeat.createInstance(getIdGenerator(), this);
//        participantSeat.setParticipant(createParticipantTeamVoid());
        return participantSeat;
    }

    public StatisticsStructure getPlayPerParticipantStatistics() {
        if (!playPerParticipantStatisticsInitialized) {
            playPerParticipantStatistics.reset();
            int playSize;
            playPerParticipantStatistics.sum = 0L;
            playPerParticipantStatistics.count = 0L;
            for (ParticipantSeat participantSeat : getCompetitionInstance().participantSeats) {
                if (participantSeat.participant != null) {
                    playSize = participantSeat.participant.competitionPlays.size();
                    if (playPerParticipantStatistics.max == null || playSize > playPerParticipantStatistics.max)
                        playPerParticipantStatistics.max = (long) playSize;
                    if (playPerParticipantStatistics.min == null || playSize < playPerParticipantStatistics.min)
                        playPerParticipantStatistics.min = (long) playSize;
                    playPerParticipantStatistics.sum += (long) playSize;
                    playPerParticipantStatistics.count++;
                }
            }
            playPerParticipantStatistics.computeAverage();
        }
        return playPerParticipantStatistics;
    }

    public CompetitionGroup createCompetitionGroup() {
        CompetitionGroup competitionGroup = CompetitionGroup.createInstance(getIdGenerator());
        competitionGroup.setCompetitionInstance(this);
        return competitionGroup;
    }

    public CompetitionGroupResult createCompetitionGroupResult() {
        CompetitionGroupResult competitionGroupResult = CompetitionGroupResult.createInstance(getIdGenerator());
        competitionGroupResult.setCompetitionInstance(this);
        return competitionGroupResult;
    }

    public CompetitionRound createCompetitionRound() {
        CompetitionRound competitionRound = CompetitionRound.createInstance(getIdGenerator());
        competitionRound.setCompetitionInstance(this);
        return competitionRound;
    }

    public CompetitionMatch createCompetitionMatch() {
        CompetitionMatch competitionMatch = CompetitionMatch.createInstance(getIdGenerator());
        competitionMatch.setCompetitionInstance(this);
        return competitionMatch;
    }

    public CompetitionPlay createCompetitionPlay() {
        CompetitionPlay competitionPlay = CompetitionPlay.createInstance(getIdGenerator());
        competitionPlay.setCompetitionInstance(this);
        return competitionPlay;
    }


//    @Override
//    public void fillCache(boolean up, boolean down) {
//        super.fillCache(false, down);
//    }

    public CompetitionSeed createCompetitionSeed() {
        CompetitionSeed competitionSeed = CompetitionSeed.createInstance(getIdGenerator());
        addCompetitionSeed(competitionSeed);
        return competitionSeed;
    }

    public void spreadCompetitionInstance() {
        this.spreadCompetitionInstance(this);
    }

    @Override
    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        super.spreadCompetitionInstance(competitionInstance);
        for (CompetitionPhase competitionPhase : competitionPhases) {
            competitionPhase.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.spreadCompetitionInstance(competitionInstance);
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.spreadCompetitionInstance(competitionInstance);
        }
    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return null;
    }

    public void sortContent() {
        Sets.sort(competitionGroupResults);
        Sets.sort(participantResults);
        Sets.sort(competitionPhases);
        Sets.sort(competitionSeeds);
        Sets.sort(competitionGroups);
        Sets.sort(competitionRounds);
        Sets.sort(competitionMatches);
        Sets.sort(competitionPlays);
        Sets.sort(participantSeats);
        Sets.sort(participantTeams);
        Sets.sort(participantSingles);
        Sets.sort(participants);
        for (CompetitionPhase competitionPhase : competitionPhases) {
            competitionPhase.sortContent();
        }
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.sortContent();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.sortContent();
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.sortContent();
        }
        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.sortContent();
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.sortContent();
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.sortContent();
        }
    }

    public void addParticipantPairing(ParticipantPairing participantPairing) {
        participantPairings.add(participantPairing);
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionInstance competitionInstance = new CompetitionInstance();
        competitionInstance.id = this.id;
        competitionInstance.databaseId = this.databaseId;
        competitionInstance.localId = this.localId;
        competitionInstance.version = this.version;
        return competitionInstance;
    }

    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionInstance competitionInstance = this.clone();
        competitionInstance.competitionPlays = null;
        competitionInstance.competitionSeeds = null;
        competitionInstance.competitionGroupResults = null;
        competitionInstance.competitionGroups = null;
        competitionInstance.competitionRounds = null;
        competitionInstance.competitionMatches = null;
        competitionInstance.competitionPhases = null;
//        participantSeats = null;
        competitionInstance.participantTeams = null;
        competitionInstance.participantSingles = null;
        competitionInstance.participants = null;
        competitionInstance.idGenerator = null;
        competitionInstance.competitionPhase = null;
        competitionInstance.participantSeats = null;
//        participantResults = null;
//        participantPairings = null;
        if (competitionPhase != null) {
            competitionInstance.competitionPhase = (CompetitionPhase) competitionPhase.cloneForContext();
//        } else if (competitionSeed != null) {
//            competitionInstance.competitionSeed = (CompetitionSeed) competitionSeed.cloneForContext();
//        }else{
//            if (competitionSeed != null) {
//                competitionInstance.competitionSeed = (CompetitionSeed) competitionSeed.cloneForContext();
//            }
        }
//        SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
//        if (this.competitionSeeds != null &&!this.competitionSeeds .isEmpty()) {
//            competitionInstance.competitionSeeds = new TreeSet<>();
//            for (CompetitionSeed competitionSeed :
//                    this.competitionSeeds) {
//                competitionSeeds.add((CompetitionSeed) competitionSeed.cloneForContext());
//            }
//            competitionInstance.competitionSeeds.addAll(competitionSeeds);
//        }
        return competitionInstance;
    }

    @Override
    public void clearForContext() {
        competitionPlays = null;
        competitionSeeds = null;
        competitionGroupResults = null;
        competitionGroups = null;
        competitionRounds = null;
        competitionMatches = null;
        participantSeats = null;
        participantTeams = null;
        participantSingles = null;
        participants = null;
        idGenerator = null;
//        competitionSeed = null;
        participantResults = null;
        participantPairings = null;
    }

    public List<Participant> getParticipantsForContext() {
        SortedSet<Participant> participantsSet = new TreeSet<>(super.getParticipantsForContext());
        if (participantSeats != null) {
            for (ParticipantSeat participantSeat : participantSeats) {
                participantsSet.addAll(participantSeat.getParticipantsAsArrayForContext());
            }
        }
        List<Participant> participants = new ArrayList<>(participantsSet);
        return participants;
    }

    public List<CompetitionObjectWithResult> getSubCompetitionObjectWithResultsForContext() {
        SortedSet<CompetitionObjectWithResult> competitionObjectWithResultsSet = new TreeSet<>(super.getSubCompetitionObjectWithResultsForContext());
        SortedSet<String> ids = new TreeSet<>();
        if (participantPairings != null) {
            for (ParticipantPairing participantPairing : participantPairings) {
                if (participantPairing.competitionSeed != null && !ids.contains(participantPairing.competitionSeed.id)) {
                    CompetitionSeed competitionSeed = (CompetitionSeed) participantPairing.competitionSeed.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionSeed);
                    ids.add(competitionSeed.id);
                }
                if (participantPairing.competitionPhase != null && !ids.contains(participantPairing.competitionPhase.id)) {
                    CompetitionPhase competitionPhase = (CompetitionPhase) participantPairing.competitionPhase.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionPhase);
                    ids.add(competitionPhase.id);
                }
                if (participantPairing.competitionGroup != null && !ids.contains(participantPairing.competitionGroup.id)) {
                    CompetitionGroup competitionGroup = (CompetitionGroup) participantPairing.competitionGroup.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionGroup);
                    ids.add(competitionGroup.id);
                }
                if (participantPairing.competitionRound != null && !ids.contains(participantPairing.competitionRound.id)) {
                    CompetitionRound competitionRound = (CompetitionRound) participantPairing.competitionRound.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionRound);
                    ids.add(competitionRound.id);
                }
                if (participantPairing.competitionMatch != null && !ids.contains(participantPairing.competitionMatch.id)) {
                    CompetitionMatch competitionMatch = (CompetitionMatch) participantPairing.competitionMatch.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionMatch);
                    ids.add(competitionMatch.id);
                }
                if (participantPairing.competitionPlay != null && !ids.contains(participantPairing.competitionPlay.id)) {
                    CompetitionPlay competitionPlay = (CompetitionPlay) participantPairing.competitionPlay.cloneForContext();
                    competitionObjectWithResultsSet.add(competitionPlay);
                    ids.add(competitionPlay.id);
                }
            }
        }

        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>(competitionObjectWithResultsSet);
        return competitionObjectWithResults;
    }

    @Override
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }

    public void continueCompetition(boolean generation) throws CompetitionInstanceGeneratorException {
        if (!competitionObjectStatus.isClosed()) {
            if (!competitionObjectStatus.isOpen()) {
                open();
            } else {
                if (this.competitionPhase != null)
                    this.competitionPhase.continueCompetition();
//                else
//                    this.competitionSeed.continueCompetition();
            }
            this.launchSimulation(generation);
        }
    }


    public final void addParticipant(Participant participant) {
        removeParticipant(participant);
        participant.setCompetitionInstance(this.getCompetitionInstance());
        this.participants.add(participant);
    }

    public final ParticipantResult addParticipantSeat(ParticipantSeat participantSeat) {
        ParticipantResult participantResult = null;
        participantSeats.add(participantSeat);
        participantSeatsAll.add(participantSeat);
        if (participantSeat.participant != null) {
            boolean createIfNorExists = true;
            if (participantSeat.participant instanceof ParticipantSingle)
                participantSingles.add((ParticipantSingle) participantSeat.participant);
            if (participantSeat.participant instanceof ParticipantTeam)
                participantTeams.add((ParticipantTeam) participantSeat.participant);
            participants.add(participantSeat.participant);
            participantResult = getParticipantResultFor(participantSeat.participant, createIfNorExists);
        }
        return participantResult;
    }


    public void fillObserverWithAllObjects(CompetitionObserver competitionObserver) {
        this.addObserver(competitionObserver);
//        this.forceChanged();
        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            competitionSeed.forceChanged();
        }
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionGroupResult.forceChanged();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            competitionGroup.forceChanged();
        }
        for (CompetitionRound competitionRound : competitionRounds) {
            competitionRound.forceChanged();
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            competitionMatch.forceChanged();
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            competitionPlay.forceChanged();
        }
    }

    public void verifyStatus() throws CompetitionInstanceGeneratorException {


        // check rounds
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            Sets.sort(competitionSeed.competitionGroups);
            for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
//                Sets.sort(competitionGroup.competitionRounds);
                for (CompetitionRound competitionRound : competitionGroup.competitionRounds) {
                    if (competitionRound.isInvalid()) {
                        competitionRound.prepareForRelaunch();
                    }
                }
            }
        }

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (!competitionSeed.isClosed() && !competitionSeed.isOpen() && (competitionSeed.getCompetitionGroupResultsPrevious().isEmpty() || competitionSeed.isPreviousCompetitionResultClosed())) {
                if (!competitionSeed.isInitialized())
                    competitionSeed.initializeSeed();
                if (competitionSeed.isInitialized()) {
                    competitionSeed.open();
                }
            }
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (!competitionGroup.isClosed() && !competitionGroup.isOpen() && competitionGroup.getCompetitionSeed().isOpen()) {
                if (!competitionGroup.isInitialized()) {
                    competitionGroup.initializeGroup();
                }
                if (competitionGroup.isInitialized()) {
                    competitionGroup.open();
                }
            }
        }


        for (CompetitionRound competitionRound : competitionRounds) {
            if (!competitionRound.isClosed() && !competitionRound.isOpen() && competitionRound.getCompetitionGroup().isOpen() && (competitionRound.round == 1 || competitionRound.getCompetitionGroup().getCompetitionRoundForRound(competitionRound.round - 1).isClosed())) {
                if (!competitionRound.isInitialized()) {
                    competitionRound.initializeRound();
                }
                if (competitionRound.isInitialized()) {
                    competitionRound.open();
                }
            }
        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (!competitionMatch.isClosed() && !competitionMatch.isOpen() && competitionMatch.getCompetitionRound().isOpen()) {
                if (!competitionMatch.isInitialized()) {
                    competitionMatch.initializeMatch();
                }
                if (competitionMatch.isInitialized()) {
                    competitionMatch.open();
                }
            }
        }
        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (!competitionPlay.isClosed() && !competitionPlay.isOpen() && competitionPlay.getCompetitionMatch().isOpen() && (competitionPlay.round == 1 || competitionPlay.getCompetitionMatch().getCompetitionPlayForRound(competitionPlay.round - 1).isClosed()))
                if (!competitionPlay.isInitialized()) {
                    competitionPlay.initializePlay();
                }
            if (competitionPlay.isInitialized()) {
                competitionPlay.open();
            }

        }

        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (competitionPlay.isParticipantResultsSet() && !competitionPlay.isClosed())
                competitionPlay.close();

        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (competitionMatch.isSubClosed() && !competitionMatch.isClosed())
                competitionMatch.close();
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            if (competitionRound.isSubClosed() && !competitionRound.isClosed())
                competitionRound.close();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (competitionGroup.isSubClosed() && !competitionGroup.isClosed())
                competitionGroup.close();
        }

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (competitionSeed.isSubClosed() && !competitionSeed.isClosed())
                competitionSeed.close();
        }

    }

    public void cancel() {
        super.cancel();

        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (!competitionPlay.isClosed())
                competitionPlay.cancel();

        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (!competitionMatch.isClosed())
                competitionMatch.cancel();
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            if (!competitionRound.isClosed())
                competitionRound.cancel();
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (!competitionGroup.isClosed())
                competitionGroup.cancel();
        }

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (!competitionSeed.isClosed())
                competitionSeed.cancel();
        }
        for (CompetitionPhase competitionPhase : competitionPhases) {
            if (!competitionPhase.isClosed())
                competitionPhase.cancel();
        }
    }

    public SortedSet<CompetitionSeed> getCompetitionSeedsForPhase(int phase) {
        CompetitionPhase competitionPhase = getCompetitionPhaseForRound(phase);
        return competitionPhase.competitionSeeds;
    }

    public int computeRemainingParticipants() {
        int remainingParticipants = 0;
        Set<CompetitionGroupResult> competitionGroupResults = computeLastCompetitionGroupResults();

        if (competitionGroupResults != null) {
            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
                CompetitionCreationParamPhase competitionCreationParamPhase = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
//                if (competitionCreationParamPhase == null)
//                    competitionCreationParamPhase = competitionGroupResult.getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
                if (competitionGroupResult.getCompetitionGroup().lane == 1 || competitionGroupResult.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
                    CompetitionRound competitionRound = competitionGroupResult.getCompetitionGroup().getCompetitionRoundForRound(competitionGroupResult.getCompetitionGroup().competitionRounds.size());
                    if (competitionRound != null) {
                        for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
                            if (competitionGroupResult.getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
                                if (competitionMatch.participantQuantity > 0)
                                    remainingParticipants = remainingParticipants + competitionMatch.participantQuantity;
                                else
                                    remainingParticipants = remainingParticipants + competitionCreationParamPhase.numberOfParticipantMatch;
                            } else {
                                remainingParticipants = remainingParticipants + competitionCreationParamPhase.participantQualifiedPerMatch;
                            }

                        }
                    } else {
                        remainingParticipants = remainingParticipants + competitionGroupResult.getParticipantResultSize();
                    }

                }
            }
        }
        return remainingParticipants;
    }


    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
        if (competitionSeeds != null) {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                Map<Participant, List<Participant>> participantOpponentsPlayMap = competitionSeed.getParticipantOpponentsMap();
                for (Participant participant : participantOpponentsPlayMap.keySet()) {
                    if (!participantOpponentsMap.containsKey(participant))
                        participantOpponentsMap.put(participant, new ArrayList<>());
                    participantOpponentsMap.get(participant).addAll(participantOpponentsMap.get(participant));
                }
            }
        }
        return participantOpponentsMap;
    }

    public CompetitionObjectChangeList getOpenCompetitionObjects() {
        CompetitionObjectChangeList competitionObjectChangeList = new CompetitionObjectChangeListImpl();
        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (competitionPlay.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionPlay);

        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (competitionMatch.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionMatch);
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            if (competitionRound.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionRound);
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (competitionGroup.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionGroup);
        }

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (competitionSeed.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionSeed);
        }
        for (CompetitionPhase competitionPhase : competitionPhases) {
            if (competitionPhase.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionPhase);
        }
        return competitionObjectChangeList;
    }


    public CompetitionObjectChangeList getNotOpenCompetitionObjects() {
        CompetitionObjectChangeList competitionObjectChangeList = new CompetitionObjectChangeListImpl();
        for (CompetitionPlay competitionPlay : competitionPlays) {
            if (!competitionPlay.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionPlay);

        }
        for (CompetitionMatch competitionMatch : competitionMatches) {
            if (!competitionMatch.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionMatch);
        }

        for (CompetitionRound competitionRound : competitionRounds) {
            if (!competitionRound.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionRound);
        }
        for (CompetitionGroup competitionGroup : competitionGroups) {
            if (!competitionGroup.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionGroup);
        }

        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            if (!competitionSeed.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionSeed);
        }
        for (CompetitionPhase competitionPhase : competitionPhases) {
            if (!competitionPhase.isOpen())
                ((CompetitionObjectChangeListImpl) competitionObjectChangeList).add(competitionPhase);
        }
        return competitionObjectChangeList;
    }

    public boolean isRegistrationOnTheFly() {
        boolean result = false;

        if (competitionPhases != null) {
            for (CompetitionPhase competitionPhase : competitionPhases) {
                result = competitionPhase.isOpen() && competitionPhase.isRegistrationOnTheFly();
                if (result)
                    break;
            }
        }

        return result;

    }

    public boolean isRegistrationOpen() {
        boolean registrationOpen = false;
        if (this.isParticipantSeatFreeAvailable()) {
            if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
                registrationOpen = true;
            } else if (this.isRegistrationOnTheFly()) {
                registrationOpen = true;
            }
        }
        return registrationOpen;
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

    public ParticipantSeat getParticipantSeatForParticipantInstanceDatabaseId(Integer participantDatabaseId) {
        ParticipantSeat participantSeatFound = null;
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participantSeat.participant != null && participantSeat.participant.databaseId.compareTo(participantDatabaseId) == 0) {
                participantSeatFound = participantSeat;
                break;
            }
        }
        return participantSeatFound;

    }

    public List<Participant> getLastCompetitionPhaseResultsParticipants() {
        SortedSet<ParticipantResult> participantResults = competitionPhases.last().participantResults;
        List<Participant> participants = new ArrayList<>();
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participants.add(participantResult.participant);
            }
        for (ParticipantSeat participantSeat : participantSeats) {
            if (participants.contains(participantSeat.participant)) {
            } else {
                participants.add(participantSeat.participant);
            }
        }

        return participants;
    }

    public CompetitionObserver getCompetitionObserver() {
        return this.competitionObserver;
    }

    public Set<CompetitionPhase> getCompetitionPhasesFirst() {
        Set<CompetitionPhase> competitionPhasesFirst = new HashSet<>();

        for (CompetitionPhase competitionPhase : competitionPhases) {
            if (competitionPhase.phaseType.compareTo(PhaseType.SEEDING) != 0) {
                if (competitionPhasesFirst.isEmpty()) {
                    competitionPhasesFirst.add(competitionPhase);
                } else {
                    Set<CompetitionPhase> competitionPhasesFirstToRemove = new HashSet<>();
                    boolean addPhase = false;
                    for (CompetitionPhase competitionPhasesFirstCurrent : competitionPhasesFirst) {
                        if (competitionPhase.round.compareTo(competitionPhasesFirstCurrent.round) < 0) {
                            competitionPhasesFirstToRemove.add(competitionPhasesFirstCurrent);
                            addPhase = true;
                        } else if (competitionPhase.round.compareTo(competitionPhasesFirstCurrent.round) == 0) {
                            addPhase = true;
                        } else {

                        }
                    }
                    if (addPhase)
                        competitionPhasesFirst.add(competitionPhase);
                    competitionPhasesFirst.removeAll(competitionPhasesFirstToRemove);
                }
            }

        }
        return competitionPhasesFirst;
    }

    public Collection<? extends ParticipantSeat> getParticipantSeats(SortedSet<ParticipantSeat> participantSeats) {
        List<ParticipantSeat> seats = new ArrayList<>();
        if (participantSeats != null) {
            seats = new ArrayList<>();
            for (ParticipantSeat participantSeat : participantSeats) {
                ParticipantSeat participantSeatFound = getParticipantSeat(participantSeat);
                if (participantSeatFound != null) {
                    seats.add(participantSeatFound);
                } else {
                    seats.add(participantSeat);
//                    this.participantSeats.add(participantSeat);
                }
            }
        }
        return seats;
    }

    private ParticipantSeat getParticipantSeat(ParticipantSeat participantSeat) {
        ParticipantSeat participantSeatFound = null;
        for (ParticipantSeat participantSeatCurrent : this.participantSeatsAll) {
            if (participantSeatCurrent.localId.compareTo(participantSeat.localId) == 0) {
                participantSeatFound = participantSeatCurrent;
                break;
            }
        }
        return participantSeatFound;
    }

    @Override
    public void generationDone() {
        super.generationDone();
        this.getIdGenerator().compact(CompetitionMatch.class.getSimpleName(), new ArrayList<>(this.competitionMatches));
        this.getIdGenerator().compact(CompetitionRound.class.getSimpleName(), new ArrayList<>(this.competitionRounds));
    }


    public void setInitialParticipantsRatingsBasedOnCompetition(CompetitionInstance competitionInstance, Map<Integer, Integer> participantInstanceMapToCurrent) {
        // TODO : Set participant Ratings from external Ladder
        // competitionInstance.getParticipantResults()
        if (!competitionInstance.isParticipantResultsSet()) {

            Map<Participant, Rating> participantRatingMap = competitionInstance.getParticipantRatingMap();
            boolean createIfNorExists = true;
            for (Map.Entry<Participant, Rating> participantRatingEntry : participantRatingMap.entrySet()) {
                ParticipantResult participantResult = competitionInstance.getParticipantResultFor(participantRatingEntry.getKey(), createIfNorExists);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_RATING, participantRatingEntry.getValue().getRating());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_RATING_DEVIATION, participantRatingEntry.getValue().getRatingDeviation());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_VOLATILITY, participantRatingEntry.getValue().getVolatility());
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreCompetition.GLICKO2_NUMBER_OF_RESULTS, participantRatingEntry.getValue().getNumberOfResults());
            }
        }
//        boolean glicko2 = true;
//        ParticipantScore.fillRank(competitionInstance.participantResults, ParticipantScoreCompetition.RANK, glicko2);
        for (ParticipantSeat participantSeat : this.participantSeats) {
            if (!participantSeat.isFree()) {
                Integer currentDatabaseId = participantSeat.getParticipant().databaseId;
                for (ParticipantResult participantResult : competitionInstance.participantResults) {
                    if (participantInstanceMapToCurrent.containsKey(participantResult.participant.databaseId) && participantInstanceMapToCurrent.get(participantResult.participant.databaseId).compareTo(currentDatabaseId) == 0 && participantResult.participantScore != null) {
                        participantSeat.participantScoreRating = participantResult.participantScore.getParticipantScoreRating();
                        break;
                    }
                }
            }
        }
    }

}


