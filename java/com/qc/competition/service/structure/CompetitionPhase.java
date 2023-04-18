package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.db.entity.competition.CompetitionObjectStatus;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.tree.CompetitionPhaseTree;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.service.template.automatic.participation.optimization.PhaseType;
import com.qc.competition.utils.Sets;
import com.qc.competition.ws.simplestructure.Duration;
import org.goochjs.glicko2.Rating;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.*;

//import com.qc.competition.service.structure.tree.CompetitionPhaseTree;

/**
 * Created by Duncan on 21/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionPhase extends CompetitionObjectWithResult<CompetitionInstance, CompetitionSeed> implements Simplify<CompetitionPhase> {
    public static String CLASS = CompetitionPhase.class.getSimpleName();

    @XmlElement(name = "phaseParameter")
    @JsonProperty("phaseParameter")
    public CompetitionCreationParamPhase competitionCreationParamPhase;


    @XmlList
    @XmlAttribute(name = "seedIds")
    @JsonProperty("seedIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionSeed> competitionSeeds = new TreeSet<>();
    @XmlAttribute(name = "filteringUnit")
    public Unit filteringUnit;
    @XmlAttribute(name = "filteringValue")
    public int filteringValue;
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
    @XmlAttribute(name = "nextCompetitionPhaseIds")
    @JsonProperty("nextCompetitionPhaseIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionPhase> nextCompetitionPhases = new TreeSet<>();
    @XmlList
    @XmlAttribute(name = "previousCompetitionPhaseIds")
    @JsonProperty("previousCompetitionPhaseIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<CompetitionPhase> previousCompetitionPhases = new TreeSet<>();
    @XmlAttribute(name = "empty")
    @JsonProperty("empty")
    public boolean emptyPhase = false;
    @XmlAttribute(name = "round")
    public Integer round;
    @XmlAttribute(name = "lane")
    public Integer lane;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionInstanceId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "phaseType")
    public PhaseType phaseType;
    @XmlElementWrapper(name = "seats")
    @XmlElement(name = "seat")
    @JsonProperty("seats")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public SortedSet<ParticipantSeat> participantSeats = new TreeSet<>();
    DescriptionTable descriptionTable = null;

    public CompetitionPhase() {
        super();
    }

    private CompetitionPhase(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static CompetitionPhase createInstance(IdGenerator idGenerator) {
        return new CompetitionPhase(idGenerator);
    }

    @Override
    public CompetitionInstance getCompetitionInstance() {
        return competitionInstance;
    }

    @Override
    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    @Override
    void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
    }

    @Override
    boolean allParticipantResultsSet() {
        int participantResultSize = this.participantResults.size();
        int participantsSize = 0;
        for (ParticipantPairing participantPairing : this.participantPairings) {
            participantsSize += participantPairing.getRealParticipantQuantity();
        }
        return participantsSize == participantResultSize;
    }

    @Override
    protected Integer getRoundOrLane() {
        return round;
    }

    @Override
    ParticipantScore createInitialParticipantScore() {
        return new ParticipantScorePhase(this);
    }

    @Override
    CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return this.competitionInstance;
    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionInstance != null ? competitionInstance.localId : null;
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (this.previousCompetitionPhases != null)
            competitionObjectWithResults.addAll(this.previousCompetitionPhases);
        return competitionObjectWithResults;
    }

    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (this.nextCompetitionPhases != null)
            competitionObjectWithResults.addAll(this.nextCompetitionPhases);
        return competitionObjectWithResults;
    }


    @Override
    void updateResultDependencies() throws CompetitionInstanceGeneratorException {
        if (isSubClosed()) {
            setChanged();
            notifyObservers(this.localId);
            fillCompetitionPhaseResultFromCompetitionSeeds();
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
            this.close();
        }
    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
            if (this.nextCompetitionPhases == null || this.nextCompetitionPhases.isEmpty()) {
                this.competitionInstance.updateResultDependencies();
            } else {
                for (CompetitionPhase competitionPhaseNext : nextCompetitionPhases) {
                    competitionPhaseNext.initializePhase();
                    competitionPhaseNext.open();
                }
            }
        }
    }

    public void fillCompetitionPhaseResultFromCompetitionSeeds() {
        ParticipantResult participantResultPhase = null;
        boolean createIfNorExists = true;
        boolean rankByGlicko2 = false;
        if (this.phaseType.compareTo(PhaseType.SEEDING) != 0 && competitionCreationParamPhase.tournamentFormatsAccepted.first().compareTo(TournamentFormat.LADDER) == 0 && competitionCreationParamPhase.tournamentFormatsAccepted.size() == 1) {
            rankByGlicko2 = true;
            Map<Participant, Rating> participantRatingMap = getParticipantRatingMap();

            for (Map.Entry<Participant, Rating> participantRatingEntry : participantRatingMap.entrySet()) {
                participantResultPhase = getParticipantResultFor(participantRatingEntry.getKey(), createIfNorExists);
                participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING, participantRatingEntry.getValue().getRating());
                participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.GLICKO2_RATING_DEVIATION, participantRatingEntry.getValue().getRatingDeviation());
                participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.GLICKO2_VOLATILITY, participantRatingEntry.getValue().getVolatility());
                participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.GLICKO2_NUMBER_OF_RESULTS, participantRatingEntry.getValue().getNumberOfResults());
            }
        }
//        int divider = 1;
//        int stepMax = getCompetitionSeedStepMax();

        if (this.phaseType.compareTo(PhaseType.SEEDING) == 0) {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                int step = getCompetitionSeedStep(competitionSeed);
                int round = competitionSeed.round;
                for (ParticipantResult participantResultSeed : competitionSeed.getParticipantResults()) {
                    participantResultPhase = getParticipantResultFor(participantResultSeed.participant, createIfNorExists);
                    fillParticipantResultPhaseFormParticipantResultSeed(participantResultPhase, participantResultSeed, step, round, false);
                }
            }

        } else {
            for (Participant participant : this.getRealParticipantsAsArray()) {
                if (!competitionSeeds.isEmpty()) {
                    for (CompetitionSeed competitionSeed : competitionSeeds) {
                        int step = getCompetitionSeedStep(competitionSeed);
                        int round = competitionSeed.round;
                        for (ParticipantResult participantResultSeed : competitionSeed.getParticipantResults()) {
                            if (participant.compareTo(participantResultSeed.participant) == 0) {
                                participantResultPhase = getParticipantResultFor(participantResultSeed.participant, createIfNorExists);
                                fillParticipantResultPhaseFormParticipantResultSeed(participantResultPhase, participantResultSeed, step, round, true);
                            }
                        }
                    }
                }
            }
        }

        if (!participantResults.isEmpty()) {
            participantResults = removeDoublons(participantResults);
            ParticipantScore.fillRank(this.participantResults, ParticipantScorePhase.RANK, rankByGlicko2);
            ParticipantScore.fillPoints(this.participantResults, ParticipantScorePhase.SCORE_POINTS);
        }

        setChanged();
        notifyObservers(this.localId);

        Sets.sort(this.participantResults);
    }


    private int getCompetitionSeedStep(CompetitionSeed competitionSeed) {
        int step = 0;
        for (CompetitionSeed competitionSeedCurrent : competitionSeeds) {
            step++;
            if (competitionSeedCurrent.compareTo(competitionSeed) == 0)
                break;

        }
        return step;
    }


//    private int getCompetitionSeedStepMax() {
//         TODO : make a real algorithm based on precedence
//        return this.competitionSeeds.size();
//    }


    private SortedSet<ParticipantResult> removeDoublons(SortedSet<ParticipantResult> participantResults) {
        Sets.sort(participantResults);
        List<ParticipantResult> participantResultsArray = new ArrayList<>();
        participantResultsArray.addAll(participantResults);
        for (int i = 0; i < participantResultsArray.size(); i++) {
            for (int j = i + 1; j < participantResultsArray.size(); j++) {
                if (participantResultsArray.get(i).participant.compareTo(participantResultsArray.get(j).participant) == 0) {
                    participantResultsArray.remove(j);
                    j--;
                }
            }
        }
        participantResults.clear();
        participantResults.addAll(participantResultsArray);
        Sets.sort(participantResults);
        return participantResults;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    @Override
    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            if (!competitionSeeds.isEmpty()) {
                for (CompetitionSeed competitionSeed : competitionSeeds) {
                    overForParticipant = competitionSeed.isOverForParticipant(participant);
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
    public SortedSet<CompetitionSeed> getSubCompetitionObjectWithResults() {
        return competitionSeeds;
    }

    @Override
    public CompetitionInstance getUpperCompetitionObjectWithResult() {
        return competitionInstance;
    }

    @Override
    public boolean isParticipantPairingDefined() {
        return participantPairings != null && !participantPairings.isEmpty();
    }

    @Override
    protected void sortParticipantPairings() {
        if (participantPairings != null)
            Sets.sort(participantPairings);
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return participantResults;
    }

    @Override
    public SortedSet<ParticipantPairing> getParticipantPairings() {
        return participantPairings;
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionPhase competitionPhase = new CompetitionPhase();
        competitionPhase.id = this.id;
        competitionPhase.databaseId = this.databaseId;
        competitionPhase.localId = this.localId;
        return competitionPhase;
    }

    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionPhase competitionPhase = (CompetitionPhase) super.clone();
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionPhase.nextCompetitionPhases = null;
        competitionPhase.previousCompetitionPhases = null;
        competitionPhase.competitionInstance = new CompetitionInstance();
        competitionPhase.competitionInstance.id = competitionInstanceId;
        competitionPhase.competitionInstance.version = version;
        competitionPhase.competitionSeeds = null;
        SortedSet<CompetitionSeed> competitionSeedsCloned = new TreeSet<>();
        if (this.competitionSeeds != null && !this.competitionSeeds.isEmpty()) {
            competitionPhase.competitionSeeds = new TreeSet<>();
            for (CompetitionSeed competitionSeed : this.competitionSeeds) {
                competitionSeedsCloned.add((CompetitionSeed) competitionSeed.cloneForContext());
            }
            competitionPhase.competitionSeeds.addAll(competitionSeedsCloned);
        }
        return competitionPhase;
    }


    public void clearDatabaseId() {
        this.databaseId = null;
        if (getCompetitionSeeds() != null)
            for (CompetitionSeed competitionSeed : getCompetitionSeeds()) {
                competitionSeed.clearDatabaseId();
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
    public void clearForContext() {
        competitionSeeds = null;
        nextCompetitionPhases = null;
        previousCompetitionPhases = null;
        participantResults = null;
        participantPairings = null;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionInstance = new CompetitionInstance();
        competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;

    }

    @Override
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

    @Override
    public CompetitionPhase cloneSimplified() {
        CompetitionPhase competitionPhase = null;
        try {
            competitionPhase = (CompetitionPhase) this.clone();
            if (this.participantResults != null) {
                competitionPhase.participantResults = new TreeSet<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    competitionPhase.participantResults.add(participantResult.cloneSimplified());
                }
                Sets.sort(competitionPhase.participantResults);
            }
        } catch (CloneNotSupportedException e) {
        }
        return competitionPhase;
    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionCreationParamPhase.phaseDuration != null && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            statisticsStructure.min += Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes();
            statisticsStructure.max += Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes();
            statisticsStructure.avg += Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes();
        } else {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                statisticsStructure.min += competitionSeed.getExpectedGlobalDuration().min;
                statisticsStructure.max += competitionSeed.getExpectedGlobalDuration().max;
                statisticsStructure.avg += competitionSeed.getExpectedGlobalDuration().avg;
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
        if (competitionCreationParamPhase.phaseDuration != null && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
//            double multiplicand = this.getParticipantResults().size() / ((competitionCreationParamPhase.maximumNumberOfParallelPlay != null && competitionCreationParamPhase.maximumNumberOfParallelPlay >0 )?(double)competitionCreationParamPhase.maximumNumberOfParallelPlay :1.0 );
            double divisor = (competitionCreationParamPhase.maximumNumberOfParallelPlay != null && competitionCreationParamPhase.maximumNumberOfParallelPlay > 0) ? (double) competitionCreationParamPhase.maximumNumberOfParallelPlay : 1.0;
            statisticsStructure.min += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / divisor);
            statisticsStructure.max += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / divisor);
            statisticsStructure.avg += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / divisor);
        } else {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                statisticsStructure.min += competitionSeed.getExpectedGlobalPlay().min;
                statisticsStructure.max += competitionSeed.getExpectedGlobalPlay().max;
                statisticsStructure.avg += competitionSeed.getExpectedGlobalPlay().avg;
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
        if (competitionCreationParamPhase.phaseDuration != null && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            statisticsStructure = getExpectedParticipantPlay();
            if (getParticipantResults().size() == competitionInstance.participantSeats.size())
                statisticsStructure.min = statisticsStructure.min * Duration.fromString(competitionCreationParamPhase.minimumPlayDuration).toMinutes();
            statisticsStructure.max = statisticsStructure.max * Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes();
            statisticsStructure.avg = statisticsStructure.avg * Duration.fromString(competitionCreationParamPhase.averagePlayDuration).toMinutes();
        } else {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                if (getParticipantResults().size() == competitionInstance.participantSeats.size())
                    statisticsStructure.min += competitionSeed.getExpectedParticipantDuration().min;
                statisticsStructure.max += competitionSeed.getExpectedParticipantDuration().max;
                statisticsStructure.avg += competitionSeed.getExpectedParticipantDuration().avg;
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
        if (competitionCreationParamPhase.phaseDuration != null && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            double divisor = ((competitionCreationParamPhase.maximumNumberOfParallelPlay != null && competitionCreationParamPhase.maximumNumberOfParallelPlay > 0) ? (double) competitionCreationParamPhase.maximumNumberOfParallelPlay : (double) this.getParticipantResults().size()) / this.getParticipantResults().size();
            double phaseDurationMinutes = (double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes();
            statisticsStructure.min += (long) Math.ceil(phaseDurationMinutes / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / divisor);
            statisticsStructure.max += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.minimumPlayDuration).toMinutes() / divisor);
            statisticsStructure.avg += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.averagePlayDuration).toMinutes() / divisor);
        } else {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                statisticsStructure.min += competitionSeed.getExpectedParticipantPlay().min;
                statisticsStructure.max += competitionSeed.getExpectedParticipantPlay().max;
                statisticsStructure.avg += competitionSeed.getExpectedParticipantPlay().avg;
            }
        }
        return statisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        StatisticsStructure statisticsStructure = new StatisticsStructure();
        statisticsStructure.min = 0L;
        statisticsStructure.max = 0L;
        statisticsStructure.avg = 0L;
        if (competitionCreationParamPhase.phaseDuration != null && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            statisticsStructure.min += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / (double) competitionCreationParamPhase.numberOfPlayMinimum);
            statisticsStructure.max += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / (double) competitionCreationParamPhase.numberOfPlayMinimum);
            statisticsStructure.avg += (long) Math.ceil((double) Duration.fromString(competitionCreationParamPhase.phaseDuration).toMinutes() / (double) Duration.fromString(competitionCreationParamPhase.maximumPlayDuration).toMinutes() / (double) competitionCreationParamPhase.numberOfPlayMinimum);
        } else {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                if (competitionSeed.getExpectedGlobalStep() != null) {
                    if (competitionSeed.getExpectedGlobalStep().min != null)
                        statisticsStructure.min += competitionSeed.getExpectedGlobalStep().min;
                    if (competitionSeed.getExpectedGlobalStep().max != null)
                        statisticsStructure.max += competitionSeed.getExpectedGlobalStep().max;
                    if (competitionSeed.getExpectedGlobalStep().avg != null)
                        statisticsStructure.avg += competitionSeed.getExpectedGlobalStep().avg;
                }
            }
        }
        return statisticsStructure;
    }

    public void updateCompetitionObjectStatus() {
        Set<CompetitionObjectStatus> competitionObjectStatuses = new HashSet<>();
        if (this.phaseType == null)
            this.emptyPhase = true;
        for (CompetitionSeed competitionSeed : this.competitionSeeds) {
            competitionObjectStatuses.add(competitionSeed.competitionObjectStatus);
            if (this.phaseType == null) {
//                this.phaseType = competitionSeed.phaseType;
                this.emptyPhase = this.emptyPhase && competitionSeed.emptyPhase;
            }
        }
        if (competitionObjectStatuses.size() == 1) {
            this.setCompetitionObjectStatus(competitionObjectStatuses.iterator().next());
        } else {
            int open = 0;
            int openClosingAllowed = 0;
            int closed = 0;
            int notYetAvailable = 0;
            for (CompetitionObjectStatus competitionObjectStatus : competitionObjectStatuses) {
                if (competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
                    openClosingAllowed++;
                }

                if (competitionObjectStatus.isOpen()) {
                    open++;
                } else if (competitionObjectStatus.isClosed()) {
                    closed++;
                } else if (competitionObjectStatus.isNotYetAvailable()) {
                    notYetAvailable++;
                }
            }
            if (open > 0) {
                if (openClosingAllowed > 0 && notYetAvailable == 0) {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                } else {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                }
            } else if (closed == 0) {
                if (openClosingAllowed > 0 && notYetAvailable == 0) {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                } else {
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CompetitionPhase{" +
                "localId=" + localId +
                ",phaseType=" + phaseType +
                ",name=" + internationalizedLabel.defaultLabel +
                ",status=" + competitionObjectStatus +
                ",participantPairingMethod=" + participantPairingMethod +
                ",participantFilteringMethod=" + participantFilteringMethod +
                ",filteringValue=" + filteringValue +
                ",filteringUnit=" + filteringUnit +
                '}';
    }

    public SortedSet<CompetitionSeed> getCompetitionSeeds() {
        Sets.sort(competitionSeeds);
        return competitionSeeds;
    }

    private void fillParticipantResultPhaseFormParticipantResultSeed(ParticipantResult participantResultPhase, ParticipantResult participantResultSeed, Integer competitionSeedStep, Integer competitionSeedRound, boolean addSubScore) {
        int score = 0;
        int win = 0;
        int draw = 0;
        int loss = 0;
        score = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS).computeNumberValue().intValue();
        score = score + participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS).computeNumberValue().intValue() + (competitionSeedStep - 1);
        participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_POINTS, score);

        win = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN).computeNumberValue().intValue();
        win = win + participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN).computeNumberValue().intValue();
        participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_WIN, win);

        draw = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW).computeNumberValue().intValue();
        draw = draw + participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW).computeNumberValue().intValue();
        participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_DRAW, draw);

        loss = participantResultSeed.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS).computeNumberValue().intValue();
        loss = loss + participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS).computeNumberValue().intValue();
        participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScoreSeed.SCORE_LOSS, loss);

        ParticipantScoreValue participantScoreValueLastActivePhase = participantResultPhase.participantScore.getParticipantScoreValue(ParticipantScorePhase.LAST_ACTIVE_SEED_ROUND);
        if (participantScoreValueLastActivePhase == null || participantScoreValueLastActivePhase.value == null || participantScoreValueLastActivePhase.computeNumberValue() == null || competitionSeedRound > participantScoreValueLastActivePhase.computeNumberValue().intValue()) {
            participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.LAST_ACTIVE_SEED_ROUND, competitionSeedRound);
            participantResultPhase.participantScore.setParticipantScoreValue(ParticipantScorePhase.LAST_ACTIVE_SEED_RANK, participantResultSeed.rank);
        }
        if (addSubScore)
            participantResultPhase.participantScore.participantScoresSub.add(participantResultSeed.participantScore);

    }

    public void fillCompetitionMatchLink() {
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionSeed.fillCompetitionMatchLink();
        }
    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (previousCompetitionPhases != null && !previousCompetitionPhases.isEmpty()) {
            for (CompetitionPhase competitionPhase : previousCompetitionPhases) {
                competitionPhase.fillExpectedRelativeTime();
                if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionPhase.expectedRelativeEndTime) < 0)
                    expectedRelativeStartTime = competitionPhase.expectedRelativeEndTime;
            }
        } else {
            expectedRelativeStartTime = this.competitionInstance.expectedRelativeStartTime;
        }
//        for (CompetitionSeed competitionSeed : competitionSeeds) {
//            competitionSeed.fillExpectedRelativeTime();
//        }
        if (this.phaseType.compareTo(PhaseType.SEEDING) == 0) {
            expectedDuration = Duration.ofMinutes(0);
            expectedRelativeEndTime = expectedRelativeStartTime;
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                competitionSeed.fillExpectedRelativeTime();
            }
        } else {
            if (competitionCreationParamPhase.tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                expectedRelativeEndTime = expectedRelativeStartTime.plus(Duration.parse(competitionCreationParamPhase.phaseDuration));
                for (CompetitionSeed competitionSeed : competitionSeeds) {
                    competitionSeed.fillExpectedRelativeTime();
                }
            } else {
                for (CompetitionSeed competitionSeed : competitionSeeds) {
                    competitionSeed.fillExpectedRelativeTime();
                    if (expectedRelativeEndTime == null || expectedRelativeEndTime.compareTo(competitionSeed.expectedRelativeEndTime) < 0)
                        expectedRelativeEndTime = competitionSeed.expectedRelativeEndTime;
                    if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionSeed.expectedRelativeStartTime) > 0)
                        expectedRelativeStartTime = competitionSeed.expectedRelativeStartTime;
                }
            }
            expectedDuration = expectedRelativeEndTime.minus(expectedRelativeStartTime);
        }

    }


    public SortedSet<CompetitionGroupResult> getAllCompetitionGroupResults() {
        SortedSet<CompetitionGroupResult> competitionGroupResults = new TreeSet<>();
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionGroupResults.addAll(competitionSeed.getAllCompetitionGroupResults());
        }
        Sets.sort(competitionGroupResults);
        return competitionGroupResults;
    }

    public Set<CompetitionGroupResult> computeLastCompetitionGroupResults() {
        Set<CompetitionGroupResult> competitionGroupResults = new HashSet<>();

        if (!competitionSeeds.isEmpty()) {
            for (CompetitionSeed competitionSeed : competitionSeeds)
                competitionGroupResults.addAll(competitionSeed.computeLastCompetitionGroupResults());
        }
        return competitionGroupResults;
    }

    public boolean isAllSubParticipantResultsSet() {
        boolean subParticipantResultsSet = isSubParticipantResultsSet();
        if (subParticipantResultsSet)
            if (!competitionSeeds.isEmpty()) {
                for (CompetitionSeed competitionSeed : this.getCompetitionSeeds()) {
                    subParticipantResultsSet = competitionSeed.isParticipantResultsSet() && competitionSeed.isAllSubParticipantResultsSet();
                    if (!subParticipantResultsSet)
                        break;
                }
            }
        return subParticipantResultsSet;
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
        descriptionTable.append("phaseType", this.phaseType);
        descriptionTable.append("round", this.round);
        descriptionTable.append("lane", this.lane);
        descriptionTable.append("Participant", competitionCreationParamPhase.participantType);
//        descriptionTable.append("Versus", getCompetitionComputationParam().playVersusType);
        descriptionTable.append("pairingQty", this.participantPairings.size());
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            descriptionTable.append(competitionSeed.toDescriptionTable());
        }

    }

    public void continueCompetition() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.isClosed()) {
            if (this.nextCompetitionPhases != null && !this.nextCompetitionPhases.isEmpty()) {
                for (CompetitionPhase competitionPhaseNext : nextCompetitionPhases) {
                    competitionPhaseNext.continueCompetition();
                }
            } else {
                for (CompetitionSeed competitionSeed : competitionSeeds) {
                    if (!competitionSeed.isClosed()) {
                        competitionSeed.initializeSeed();
                        competitionSeed.continueCompetition();
                    }
                }
            }
        } else if (!this.competitionObjectStatus.isOpen()) {
            if (this.previousCompetitionPhases != null) {
                for (CompetitionPhase competitionPhasePrevious : previousCompetitionPhases) {
                    competitionPhasePrevious.close();
                }
            }
            this.initializePhase();
            this.open();
        } else if (this.competitionObjectStatus.isOpen() && this.competitionCreationParamPhase.registrationOnTheFly) {
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                if (!competitionSeed.competitionObjectStatus.isClosed()) {
                    boolean allowClosing = true;
                    for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                        allowClosing = allowClosing && competitionGroup.isClosed();
                        if (!allowClosing)
                            break;
                    }
                    if (allowClosing && competitionSeed.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) != 0) {
                        competitionSeed.competitionObjectStatus = CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED;
                    }
                    competitionSeed.close();
                }
            }
            if (this.isClosed())
                this.continueCompetition();

        }
    }

    public void initializePhase() throws CompetitionInstanceGeneratorException {
        boolean reduceCheck = true;
        initializePhase(reduceCheck);
    }

    public void initializePhase(boolean reduceCheck) throws CompetitionInstanceGeneratorException {
        if (!this.isInitialized() && !this.isOpen()) {
            boolean previousClosed = true;
            boolean previousRegistrationOnTheFly = false;
            if (previousCompetitionPhases != null && !previousCompetitionPhases.isEmpty()) {
//                boolean checkIfRegistrationOnTheFlyInFirstPhase = false;
                for (CompetitionPhase previousCompetitionPhase : previousCompetitionPhases) {
                    if (previousCompetitionPhase.phaseType != null && previousCompetitionPhase.phaseType.compareTo(PhaseType.SEEDING) != 0) {
                        previousClosed = previousClosed && previousCompetitionPhase.isClosed();
                        previousRegistrationOnTheFly = previousRegistrationOnTheFly || (previousCompetitionPhase.competitionCreationParamPhase.registrationOnTheFly != null && previousCompetitionPhase.competitionCreationParamPhase.registrationOnTheFly);
//                        checkIfRegistrationOnTheFlyInFirstPhase = true;
                        if (!previousClosed)
                            break;
                    }
                }
//                if (checkIfRegistrationOnTheFlyInFirstPhase) {
//                    Set<CompetitionPhase> competitionPhasesFirst = competitionInstance.getCompetitionPhasesFirst();
//                    for (CompetitionPhase competitionPhaseFirst : competitionPhasesFirst) {
//                        previousRegistrationOnTheFly = previousRegistrationOnTheFly || (competitionPhaseFirst.competitionCreationParamPhase.registrationOnTheFly != null && competitionPhaseFirst.competitionCreationParamPhase.registrationOnTheFly);
//                        if (previousRegistrationOnTheFly)
//                            break;
//                    }
//
//                }

            }
            if (previousClosed) {
                if (previousRegistrationOnTheFly && reduceCheck) {
                    this.spreadCompetitionObserver();
                    boolean reduced = reduceCompetitionPhaseFormatToPreviousRegistrations();
                    if (reduced) {
                        this.spreadCompetitionObserver();
                        this.setChanged(true);
                    }
                }
                this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
            }
        }
    }

    public boolean reduceCompetitionPhaseFormatToPreviousRegistrations() throws CompetitionInstanceGeneratorException {
        List<CompetitionSeed> competitionSeedList = new ArrayList<>();
        competitionSeedList.addAll(this.competitionSeeds);
        boolean reduced = false;
        for (CompetitionSeed competitionSeed : competitionSeedList) {
            if (!competitionSeed.deleted && (!competitionSeed.isElimination() || competitionSeed.roundInPhase.compareTo(1) == 0))
                reduced = competitionSeed.reduceCompetitionPhaseFormatToPreviousRegistrations();
        }
        return reduced;
    }

    public void open() throws CompetitionInstanceGeneratorException {
        if (this.isInitialized()) {
            this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            initializeCompetitionSeeds();
        }
    }

    private void initializeCompetitionSeeds() throws CompetitionInstanceGeneratorException {
        if (!competitionSeeds.isEmpty()) {
//            Sets.sort(competitionSeeds);
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                competitionSeed.initializeSeed();
            }
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                competitionSeed.open();
            }
        }
    }

    public boolean isNextCompetitionPhasesOpen() {
        boolean isNextCompetitionPhasesOpen = nextCompetitionPhases != null && !nextCompetitionPhases.isEmpty();
        if (isNextCompetitionPhasesOpen) {
            for (CompetitionPhase nextCompetitionPhase : nextCompetitionPhases) {
                isNextCompetitionPhasesOpen = isNextCompetitionPhasesOpen && nextCompetitionPhase.isOpen();
                if (!isNextCompetitionPhasesOpen)
                    break;
            }
        }
        return isNextCompetitionPhasesOpen;
    }

    public SortedSet<CompetitionMatch> getCompetitionMatchForPhaseSequence(Integer phaseSequence) {
        SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>();
        if (this.competitionSeeds != null)
            for (CompetitionSeed competitionSeed : this.competitionSeeds) {
                competitionMatches.addAll(competitionSeed.getCompetitionMatchForPhaseSequence(phaseSequence));
            }

        return competitionMatches;
    }

    public SortedSet<CompetitionRound> getCompetitionRoundsForDate(Duration relativeDuration) {
        SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
        if (this.competitionSeeds != null)
            for (CompetitionSeed competitionSeed : this.competitionSeeds) {
                competitionRounds.addAll(competitionSeed.getCompetitionRoundsForDate(relativeDuration));
            }

        return competitionRounds;
    }

    public boolean isRegistrationOnTheFly() {
        boolean result = false;
        if (competitionCreationParamPhase.registrationOnTheFly != null && competitionCreationParamPhase.registrationOnTheFly) {
            if (competitionSeeds != null) {
                for (CompetitionSeed competitionSeed : competitionSeeds) {
                    result = competitionSeed.isOpen() && competitionSeed.isRegistrationOnTheFly();
                    if (result)
                        break;
                }
            }
        }
        return result;
    }

    public List<Participant> getRealParticipantsAsArray() {
        Set<Participant> participantSet = new HashSet<>();
        if (!competitionSeeds.isEmpty() && !competitionSeeds.iterator().next().competitionGroups.isEmpty() && competitionSeeds.iterator().next().competitionGroups.iterator().next().competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
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

    public boolean isParticipantResultsSet() {
        boolean participantResultSet = true;
        for (Participant participant : getRealParticipantsAsArray()) {
            participantResultSet = participantResultSet && isParticipantResultSet(participant);
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

    @Override
    public void reset(boolean recursive) {
        if (this.participantSeats != null)
            this.participantSeats.clear();
        super.reset(recursive);

    }

    public Set<CompetitionMatch> getCompetitionMatches() {
        Set<CompetitionMatch> competitionMatches = new HashSet<>();
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionMatches.addAll(competitionSeed.getCompetitionMatches());
        }
        return competitionMatches;
    }


    public Set<CompetitionPlay> getCompetitionPlays() {
        Set<CompetitionPlay> competitionPlays = new HashSet<>();
        for (CompetitionSeed competitionSeed : competitionSeeds) {
            competitionPlays.addAll(competitionSeed.getCompetitionPlays());
        }
        return competitionPlays;
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
        this.setChanged();
        notifyObservers(this.localId);
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
        for (ParticipantSeat participantSeat : participantPairing.
                getParticipantSeatsAsArray()) {
            if (participantSeat.participant != null && !(participantSeat.participant instanceof ParticipantTeamVoid) && this.getParticipantSeat(participantSeat.participant) == null) {
                this.addParticipantSeat(participantSeat);
            }
        }
    }

    public void reopen() {
        this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
        if (this.competitionSeeds.first().isElimination()) {
            boolean first = true;
            for (CompetitionSeed competitionSeed : competitionSeeds) {
                if (first) {
                    competitionSeed.reopen();
                } else {
                    competitionSeed.reset(true);
                }
                first = false;
            }
        } else {
            this.competitionSeeds.last().reopen();
        }
    }

    public boolean recompute() throws CompetitionInstanceGeneratorException {
        List<CompetitionPlay> competitionPlays = new ArrayList<>();
        competitionPlays.addAll(getOpenCompetitionPlays());
        boolean generation = true;
        boolean changeWinner = true;
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
            getCompetitionInstance().launchSimulationForCompetitionPlay(competitionPlaySelected, generation, changeWinner);
            competitionPlays.clear();
            competitionPlays.addAll(getOpenCompetitionPlays());

        }
        return this.isClosed();

    }


    public CompetitionPhaseTree getCompetitionPhaseTree() {
        CompetitionPhaseTree competitionPhaseTree = new CompetitionPhaseTree();
        competitionPhaseTree.internationalizedLabel = internationalizedLabel;
        competitionPhaseTree.localId = getLocalId();
        competitionPhaseTree.databaseId = databaseId;
        competitionPhaseTree.participantFilteringMethod = participantFilteringMethod;
        competitionPhaseTree.participantPairingMethod = participantPairingMethod;
        competitionPhaseTree.filteringUnit = filteringUnit;
        competitionPhaseTree.filteringValue = filteringValue;
        competitionPhaseTree.expectedDuration = this.expectedDuration;
        competitionPhaseTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionPhaseTree.expectedRelativeEndTime = this.expectedRelativeEndTime;
//        if (phase != null)
//            competitionPhaseTree.phase = phase;
//        else
//            competitionPhaseTree.phase = competitionPhase.round;
        competitionPhaseTree.phaseType = phaseType;

        competitionPhaseTree.over = isParticipantResultsSet();
        competitionPhaseTree.filled = !participantPairings.isEmpty();
        if (nextCompetitionPhases != null)
            for (CompetitionPhase nextCompetitionPhase : nextCompetitionPhases) {
                competitionPhaseTree.nextCompetitionPhaseIds.add(nextCompetitionPhase.localId);
            }
        for (CompetitionPhase previousCompetitionPhase : previousCompetitionPhases) {
            competitionPhaseTree.previousCompetitionPhaseIds.add(previousCompetitionPhase.localId);
        }
        if (!competitionSeeds.isEmpty()) {
            SortedSet<CompetitionSeed> competitionSeeds = getCompetitionSeeds();
//            competitionSeeds = Sets.sort(competitionSeeds);

            for (CompetitionSeed competitionSeed : competitionSeeds)
                competitionPhaseTree.competitionSeedTrees.add(competitionSeed.getCompetitionSeedTree());
        }
        ParticipantResultTree participantResultTree = null;
        if (participantResults != null) {
            for (ParticipantResult participantResult : participantResults) {
                participantResultTree = participantResult.toParticipantResultTree(false, new HashSet<>());
                participantResultTree.points =
                        participantResult.participantScore.getParticipantScoreValue(ParticipantScoreCompetition.SCORE_POINTS).computeNumberValue().intValue();
                competitionPhaseTree.participantResultTrees.add(participantResultTree);
            }
        }

        return competitionPhaseTree;
    }

}
