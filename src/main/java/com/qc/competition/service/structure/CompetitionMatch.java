package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.service.structure.tree.CompetitionMatchTree;
import com.qc.competition.service.template.*;
import com.qc.competition.utils.Sets;

import javax.xml.bind.annotation.*;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Duncan on 22/12/2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionMatch extends CompetitionObjectWithResult<CompetitionRound, CompetitionPlay> implements Simplify<CompetitionMatch> {
    public static String CLASS = CompetitionMatch.class.getSimpleName();
    //    @XmlElementWrapper(name = "playIds")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "playIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("playIds")
    public SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
    @XmlAttribute(name = "lane")
    public Integer lane;


    @XmlAttribute(name = "roundId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("roundId")
    public CompetitionRound competitionRound;
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
    @XmlAttribute(name = "matchType")
    public MatchType matchType;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlElement(name = "pairing")
    @JsonProperty("pairing")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public ParticipantPairing participantPairing;
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("result")
    public SortedSet<ParticipantResult> participantResults = new TreeSet<>();
    //    @XmlElementWrapper(name = "nextIds")
//    @XmlElement(name = "localId")
//    @XmlList
//    @XmlAttribute(name = "nextMatchesIds")
//    @XmlIDREF
//    @JsonIdentityReference(alwaysAsId = true)
//    @JsonProperty("nextMatchesIds")
//    @Deprecated
//    public SortedSet<CompetitionMatch> nextCompetitionMatches = new TreeSet<>();


    @XmlList
    @XmlAttribute(name = "nextMatchLinksIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("nextMatchLinksIds")
    public SortedSet<CompetitionMatchLink> nextCompetitionMatchLinks = new TreeSet<>();


    @XmlList
    @XmlAttribute(name = "previousMatchLinksIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("previousMatchLinksIds")
    public SortedSet<CompetitionMatchLink> previousCompetitionMatchLinks = new TreeSet<>();

    //    @XmlElementWrapper(name = "previousIds")
//    @XmlElement(name = "localId")
//    @XmlList
//    @XmlAttribute(name = "previousMatchesIds")
//    @XmlIDREF
//    @JsonIdentityReference(alwaysAsId = true)
//    @JsonProperty("previousMatchesIds")
//    @Deprecated
//    public SortedSet<CompetitionMatch> previousCompetitionMatches = new TreeSet<>();
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;//    @XmlTransient
    @XmlAttribute(name = "participantQuantity")
    @JsonProperty("participantQuantity")
    public int participantQuantity;
    @JsonIgnore
    @XmlTransient
    public int missingParticipantQuantity;
    @JsonIgnore
    @XmlTransient
    public boolean generation = false;
//    @JsonProperty("competitionRoundCache")
//    protected CompetitionRound competitionRoundCache;
//    @XmlTransient
//    @JsonProperty("competitionPlaysCache")
//    protected SortedSet<CompetitionPlay> competitionPlaysCache = null;
//    @XmlTransient
//    @JsonIgnore
//    private SortedSet<CompetitionMatch> nextCompetitionMatches = new TreeSet<>();
//    @XmlTransient
//    @JsonIgnore
//    private SortedSet<CompetitionMatch> previousCompetitionMatches = new TreeSet<>();

    public CompetitionMatch() {
        super();
    }

    private CompetitionMatch(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static CompetitionMatch createInstance(IdGenerator idGenerator) {
        return new CompetitionMatch(idGenerator);
    }

    public SortedSet<CompetitionPlay> getCompetitionPlays() {
//        if ((competitionPlaysCache == null || competitionPlaysCache.isEmpty()) && !competitionPlays.isEmpty()) {
//            competitionPlaysCache = this.getCompetitionPlays(this.competitionPlays);
//        }
        return competitionPlays;
    }

    public CompetitionRound getCompetitionRound() {
//        if (competitionRoundCache == null && competitionRoundId != null) {
//            competitionRoundCache = this.getCompetitionInstance().getCompetitionRound(competitionRoundId);
//        }
        return competitionRound;
    }


    public CompetitionMatchLink addNextCompetitionMatch(CompetitionMatch competitionMatchNext, Integer participantIndexPrevious, Integer participantRankPrevious, Integer participantIndexNext, Integer participantRankNext) {
        CompetitionMatchLink competitionMatchLink = getNextCompetitionMatchLinksFor(competitionMatchNext);
        if (competitionMatchLink == null) {
            competitionMatchLink = getCompetitionInstance().createCompetitionMatchLink(this, competitionMatchNext, participantIndexPrevious, participantRankPrevious, participantIndexNext, participantRankNext);

        } else {
            competitionMatchLink.linkThickness++;
        }
        return competitionMatchLink;
    }

    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
        Sets.sort(this.participantResults);
    }

    public CompetitionMatchLink addPreviousCompetitionMatch(CompetitionMatch competitionMatchPrevious, Integer participantIndexPrevious, Integer participantRankPrevious, Integer participantIndexNext, Integer participantRankNext) {
        CompetitionMatchLink competitionMatchLink = getPreviousCompetitionMatchLinksFor(competitionMatchPrevious);
        if (competitionMatchLink == null) {
            competitionMatchLink = getCompetitionInstance().createCompetitionMatchLink(competitionMatchPrevious, this, participantIndexPrevious, participantRankPrevious, participantIndexNext, participantRankNext);
            Sets.sort(this.previousCompetitionMatchLinks);
        } else {
            competitionMatchLink.linkThickness++;
        }
        return competitionMatchLink;

    }

    public CompetitionMatchLink getNextCompetitionMatchLinksFor(CompetitionMatch competitionMatchNext) {
        CompetitionMatchLink result = null;
        if (nextCompetitionMatchLinks != null && !nextCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : nextCompetitionMatchLinks) {
                if (competitionMatchLink.nextCompetitionMatch.compareTo(competitionMatchNext) == 0) {
                    result = competitionMatchLink;
                    break;
                }
            }
        }
        return result;
    }

    public CompetitionMatchLink getPreviousCompetitionMatchLinksFor(CompetitionMatch competitionMatchPrevious) {
        CompetitionMatchLink result = null;
        if (previousCompetitionMatchLinks != null && !previousCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : previousCompetitionMatchLinks) {
                if (competitionMatchLink.previousCompetitionMatch.compareTo(competitionMatchPrevious) == 0) {
                    result = competitionMatchLink;
                    break;
                }
            }
        }
        return result;
    }


    public boolean nextCompetitionMatchLinksContains(CompetitionMatch competitionMatchNext) {
        boolean result = false;
        if (nextCompetitionMatchLinks != null && !nextCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : nextCompetitionMatchLinks) {
                result = competitionMatchLink.nextCompetitionMatch.compareTo(competitionMatchNext) == 0;
                if (result)
                    break;
            }
        }
        return result;
    }

    public boolean previousCompetitionMatchLinksContains(CompetitionMatch competitionMatchPrevious) {
        boolean result = false;
        if (previousCompetitionMatchLinks != null && !previousCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : previousCompetitionMatchLinks) {
                result = competitionMatchLink.previousCompetitionMatch.compareTo(competitionMatchPrevious) == 0;
                if (result)
                    break;
            }
        }
        return result;
    }

    @Override
    public boolean allParticipantResultsSet() {
        boolean allParticipantResultsSet = false;
        if (this.isParticipantResultsSet()) {
            allParticipantResultsSet = true;
            for (Participant participant : this.participantPairing.getRealParticipantsAsArray()) {
                allParticipantResultsSet = allParticipantResultsSet &&
                        isParticipantResultSet(participant);
                if (!allParticipantResultsSet)
                    break;
            }
        }
        return allParticipantResultsSet;
    }

//    public void fillParticipantResultWithFakeValue() {
//        for (CompetitionPlay competitionPlay : competitionPlays) {
//            competitionPlay.fillParticipantResultWithFakeValue();
//        }
//        fillResultFromCompetitionPlay();
//    }

    public void clear() {
//        for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//            competitionPlay.clear();
//        }
        participantResults.clear();
        participantPairing = null;
        setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
    }

    private void computeParticipantResultRank() {

        if (competitionPhase.competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch != null && competitionPhase.competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy != null && competitionPhase.competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy instanceof RankingComputationPolicyScore) {
            RankingComputationPolicyScore rankingComputationPolicyScore = (RankingComputationPolicyScore) competitionPhase.competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy;
            List<String> scores = new ArrayList<>();
            scores.add(rankingComputationPolicyScore.scoreName);
            scores.add(ParticipantScoreMatch.SCORE_POINTS);
            scores.add(ParticipantScoreMatch.SCORE_WIN);
            ParticipantResultCompare participantResultCompare = new ParticipantResultCompare(scores);
            List<ParticipantResult> participantResultsAsList = new ArrayList<>(participantResults);
            Collections.sort(participantResultsAsList, participantResultCompare);
            ParticipantScore.fillRank(participantResultsAsList, ParticipantScoreMatch.RANK, scores);
            participantResults.clear();
            participantResults.addAll(participantResultsAsList);
        } else {
            Sets.sort(participantResults);
            ParticipantScore.fillRank(this.participantResults, ParticipantScoreMatch.RANK);
        }

        setChanged();
        notifyObservers(this.localId);

    }

//
//    @Override
//    public SortedSet<CompetitionPlay> getCompetitionPlays(boolean initialized, boolean open, boolean closed, Integer  participantId, boolean recursive) {
//        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
//        if ((!initialized || this.isInitialized()) && (!open || this.isOpen()) && (!closed || this.isClosed()) && (participantId== null || isForParticipant(participantId)))
//            competitionPlays.addAll(super.getCompetitionPlays(initialized, open, closed, participantId, recursive));
//        return competitionPlays;
//    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        return new ParticipantScoreMatch(this);
    }

    @Override
    public void generationDone() {
        super.generationDone();
        this.generation = false;
    }

    public void fillCompetitionMatchLink() {
        if (previousCompetitionMatchLinks.isEmpty() && nextCompetitionMatchLinks.isEmpty()) {
            this.getCompetitionRound().getCompetitionGroup().competitionGroupFormat.fillCompetitionMatchLink(this);
            this.participantQuantity = this.participantPairing.getRealParticipantQuantity();
        }
//        else if (previousCompetitionMatches.isEmpty() && nextCompetitionMatches.isEmpty())
//            this.getCompetitionRound().getCompetitionGroup().competitionGroupFormat.fillCompetitionMatchLink(this);
    }

    public void fillResultFromCompetitionPlay() {
        if (participantPairing != null) {
            List<Participant> participants = participantPairing.getRealParticipantsAsArray();
            participantResults.clear();
//        int score = 0;
//        int win = 0;
//        int draw = 0;
//        int loss = 0;
//        int bye = 0;
//        int numberOfParticipantInPlay = 0;

            Map<String, List<ParticipantScoreValue>> aggregationsParticipant = new HashMap<>();
            List<String> participantScoresOrder = new ArrayList<>();

            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;

            if (competitionCreationParamPhase.scoringConfiguration != null) {
                if (competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch != null) {
                    if (competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements != null) {
                        for (ScoringConfigurationMatchElement scoringConfigurationMatchElement : competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements) {
                            participantScoresOrder.add(scoringConfigurationMatchElement.participantScoreAggregateType.name());
                            aggregationsParticipant.put(scoringConfigurationMatchElement.participantScoreAggregateType.name(), new ArrayList<>());
                        }
                    }
                }
            }

            participantScoresOrder.add(ParticipantScoreMatch.SCORE_POINTS);
//        aggregations.put(ParticipantScoreMatch.SCORE_POINTS, new ArrayList<>());
            participantScoresOrder.add(ParticipantScoreMatch.SCORE_WIN);
            aggregationsParticipant.put(ParticipantScoreMatch.SCORE_WIN, new ArrayList<>());
            participantScoresOrder.add(ParticipantScoreMatch.SCORE_DRAW);
            aggregationsParticipant.put(ParticipantScoreMatch.SCORE_DRAW, new ArrayList<>());
            participantScoresOrder.add(ParticipantScoreMatch.SCORE_LOSS);
            aggregationsParticipant.put(ParticipantScoreMatch.SCORE_LOSS, new ArrayList<>());
            participantScoresOrder.add(ParticipantScoreMatch.SCORE_BYE);
            aggregationsParticipant.put(ParticipantScoreMatch.SCORE_BYE, new ArrayList<>());
            for (Participant participant : participants) {
                for (String key : aggregationsParticipant.keySet()) {
                    aggregationsParticipant.get(key).clear();
                }
                ParticipantResult participantResultMatch = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                participantResultMatch.setParticipant(getParticipantTeamFromParticipant(participant));
                ParticipantResult participantResult = null;
//            score = 0;
                int win = 0;
                int draw = 0;
                int loss = 0;
                int bye = 0;
                if (this.matchType.compareTo(MatchType.BYE) == 0) {
                    for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                        participantResult = competitionPlay.getParticipantResultFor(participant);
                        if (participantResult != null) {
                            participantResultMatch.participantScore.participantScoresSub.add(participantResult.participantScore);
                            updateAggregation(aggregationsParticipant, participantResult);
//                        score += participantResult.participantScore.getParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS).computeNumberValue().intValue();
//                        win += competitionPlay.isAWin(participant) ? 1 : 0;
//                        bye += competitionPlay.isABye(participant) ? 1 : 0;
                        }
                    }
                    for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                        competitionPlay.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
                    }
                } else {
                    for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                        participantResult = competitionPlay.getParticipantResultFor(participant);
                        if (participantResult != null) {
                            participantResultMatch.participantScore.participantScoresSub.add(participantResult.participantScore);
                            updateAggregation(aggregationsParticipant, participantResult);

//                        score += participantResult.participantScore.getParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS).computeNumberValue().intValue();
                            win += competitionPlay.isAWin(participant) ? 1 : 0;
                            draw += competitionPlay.isADraw(participant) ? 1 : 0;
                            loss += competitionPlay.isALoss(participant) ? 1 : 0;
                            bye += competitionPlay.isABye(participant) ? 1 : 0;
//                    } else {
//                        bye += competitionPlay.isABye(participant) ? 1 : 0;
                        }
//                    if (numberOfParticipantInPlay < competitionPlay.participantResults.size())
//                        numberOfParticipantInPlay = competitionPlay.participantResults.size();
                    }
                }
                for (String key : aggregationsParticipant.keySet()) {
                    if (!aggregationsParticipant.get(key).isEmpty())
                        participantResultMatch.participantScore.setParticipantScoreValue(key, aggregateScore(aggregationsParticipant.get(key)));
                }
                participantResultMatch.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_WIN, win);
                participantResultMatch.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_DRAW, draw);
                participantResultMatch.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_LOSS, loss);
                participantResultMatch.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_BYE, bye);
                participantResults.add(participantResultMatch);
            }
            computeParticipantResultRank();
        }

    }

    private void fixResults() {

        CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
        int optional = 0;
        for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
            if (competitionPlay.playType != null && competitionPlay.playType.compareTo(PlayType.OPTIONAL) == 0) {
                optional++;
            }
        }
        int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
        if (this.matchType.compareTo(MatchType.BYE) == 0) {
            for (ParticipantResult participantResult : participantResults) {
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_POINTS, 0);
            }
        } else {
            for (ParticipantResult participantResult : participantResults) {
                int newPoints = PointsCalculation.getMatchPoints(participantResult.rank, participantResults, competitionPlays.size() / 2 + 1, competitionPlays.size() - optional, participantQualifiedPerMatch);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScoreMatch.SCORE_POINTS, newPoints);
            }
        }
    }

    private String aggregateScore(List<ParticipantScoreValue> participantScoreValues) {
        String result = null;
        Number aggregation = 0;
        for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
            aggregation = participantScoreValue.computeNumberValue().intValue() + aggregation.intValue();

        }
        result = aggregation.toString();
        return result;
    }

    private void updateAggregation
            (Map<String, List<ParticipantScoreValue>> aggregationsParticipant, ParticipantResult participantResult) {
        for (String key : aggregationsParticipant.keySet()) {
            if (participantResult.participantScore.getParticipantScoreValue(key) != null)
                aggregationsParticipant.get(key).add(participantResult.participantScore.getParticipantScoreValue(key));
        }

//                                score += participantResult.participantScore.getParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS).computeNumberValue().intValue();
    }

    public ParticipantResult getParticipantResultsForParticipant(Participant participant) {
        for (ParticipantResult participantResult : participantResults)
            if (participantResult.participant.compareTo(participant) == 0)
                return participantResult;
        return null;
    }

    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
            if (competitionPlay.isOpen())
                competitionPlaysFound.add(competitionPlay);
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    public SortedSet<CompetitionPlay> getInitializedCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
            if (competitionPlay.isInitialized())
                competitionPlaysFound.add(competitionPlay);
        }
        Sets.sort(competitionPlaysFound);
        return competitionPlaysFound;
    }

    @Override
    protected void disableGenerationFlag() {
        super.disableGenerationFlag();
        this.generation = false;
    }

    public void initializeMatch() {
        if (competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
            if (!isParticipantPairingDefined() || (!generation && this.participantPairing.getRealParticipantQuantity() != this.participantQuantity)) {
                setChanged();
                notifyObservers(this.localId);
                if (getCompetitionRound().competitionGroup.lane == 1 && getCompetitionRound().round == 1 && getCompetitionRound().getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                    if (previousCompetitionMatchLinks == null || previousCompetitionMatchLinks.isEmpty()) {
                        ParticipantPairing[] participantPairings = new ParticipantPairing[getCompetitionRound().participantPairings.size()];
                        getCompetitionRound().participantPairings.toArray(participantPairings);
                        if ((lane - 1) % 2 == 0)
                            setParticipantPairing(participantPairings[(lane - lane / 2) - 1]);
                        else
                            setParticipantPairing(participantPairings[(participantPairings.length - (lane - 1) / 2) - 1]);
                    } else {
                        if ((generation || getCompetitionRound().getCompetitionGroup().getCompetitionSeed().stepType.compareTo(StepType.MAIN) == 0) && (getCompetitionRound().isInitialized() || getCompetitionRound().competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS) == 0)) {
                            ParticipantPairing[] participantPairings = new ParticipantPairing[getCompetitionRound().participantPairings.size()];
                            getCompetitionRound().participantPairings.toArray(participantPairings);
                            if ((lane - 1) % 2 == 0)
                                setParticipantPairing(participantPairings[(lane - lane / 2) - 1]);
                            else
                                setParticipantPairing(participantPairings[(participantPairings.length - (lane - 1) / 2) - 1]);
                        } else if (!getCompetitionRound().isInitialized() && (getCompetitionRound().getCompetitionGroup().getCompetitionSeed().stepType.compareTo(StepType.MERGE) == 0 || getCompetitionRound().getCompetitionGroup().getCompetitionSeed().stepType.compareTo(StepType.RESET) == 0)) {
                            if (!generation) {
                                TreeSet<Participant> participants = new TreeSet<>();
                                CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                                if (competitionCreationParamPhase == null)
                                    competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                                if (getCompetitionRound().getCompetitionGroup().getCompetitionSeed().stepType.compareTo(StepType.MERGE) == 0) {
                                    Map<CompetitionMatch, List<Participant>> participantsMap = new HashMap<>();
                                    for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
                                        CompetitionMatch competitionMatchPrevious = competitionMatchPreviousLink.previousCompetitionMatch;
                                        if (competitionMatchPrevious.isClosed()) {
                                            if (this.competitionGroup.lane == 1) {
                                                // get winners
                                                int i = 0;
                                                for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                    if (getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                        participants.add(participantResult.participant);
                                                    }
                                                    i++;
                                                    if (i >= competitionCreationParamPhase.participantQualifiedPerMatch)
                                                        break;
                                                }
                                            } else {
                                                // get loosers (3rd place match)
                                                int i = 0;
                                                for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                    if (i >= competitionCreationParamPhase.participantQualifiedPerMatch && participantResult.participant != null && getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                        participants.add(participantResult.participant);
                                                    }
                                                    i++;
                                                }
                                            }

                                        }
                                    }
                                } else if (getCompetitionRound().getCompetitionGroup().getCompetitionSeed().stepType.compareTo(StepType.RESET) == 0) {
                                    for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
                                        participants.addAll(competitionMatchPreviousLink.previousCompetitionMatch.participantPairing.getRealParticipantsAsArray());
                                    }
                                    participants = (TreeSet<Participant>) Sets.sort(participants);

                                }
                                if (participants != null && !participants.isEmpty()) {

                                    if (this.participantPairing == null)
                                        this.participantPairing = ParticipantPairing.createInstance(competitionCreationParamPhase.numberOfParticipantMatch, this.competitionInstance);
                                    for (Participant participant : participants) {
                                        this.participantPairing.addParticipant(participant);
                                    }
                                    this.participantPairing.setCompetitionRound(this.competitionRound);
                                    this.participantPairing.setCompetitionMatch(this);
                                    this.participantPairing.setCompetitionGroup(this.competitionGroup);
                                    this.participantPairing.setCompetitionSeed(this.competitionSeed);
                                    if (!this.competitionRound.isOpen()) {
                                        this.competitionRound.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                                    }
                                    if (!this.competitionGroup.isOpen()) {
                                        this.competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                                    }
                                    if (!this.competitionSeed.isOpen()) {
                                        this.competitionSeed.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                                    }
                                }
                            }

                        }
                    }
                } else {
                    if (getCompetitionRound().getCompetitionGroup().competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0) {
                        ParticipantPairing[] participantPairings = new ParticipantPairing[getCompetitionRound().participantPairings.size()];
                        if (participantPairings.length >= lane) {
                            Sets.sort(getCompetitionRound().participantPairings).toArray(participantPairings);
                            setParticipantPairing(participantPairings[lane - 1]);
                        }
                    } else {
                        if (generation) {
                            Map<CompetitionMatch, List<Participant>> participantsMap = new HashMap<>();
                            int participantFound = 0;
                            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                            if (competitionCreationParamPhase == null)
                                competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;

                            for (CompetitionMatchLink competitionMatchPreviousMatchLink : previousCompetitionMatchLinks) {
                                CompetitionMatch competitionMatchPrevious = competitionMatchPreviousMatchLink.previousCompetitionMatch;
                                if (competitionMatchPrevious.isClosed()) {
                                    if (competitionMatchPrevious.competitionSeed.id.compareTo(this.competitionSeed.id) != 0) {
                                        int participantPerMatchQualified = competitionCreationParamPhase.numberOfParticipantMatch / this.competitionSeed.getCompetitionGroupResultsPrevious().size();
                                        int i = 0;
                                        for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                            if (getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                if (!participantsMap.containsKey(competitionMatchPrevious))
                                                    participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                participantFound++;
                                            }
                                            i++;
                                            if (i >= participantPerMatchQualified)
                                                break;
                                        }
                                    } else {
                                        if (competitionMatchPrevious.competitionGroup.lane == this.competitionGroup.lane) {
                                            // get winners
                                            int i = 0;
                                            for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                if (getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                    if (!participantsMap.containsKey(competitionMatchPrevious))
                                                        participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                    participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                    participantFound++;
                                                }
                                                i++;
                                                if (i >= competitionCreationParamPhase.participantQualifiedPerMatch)
                                                    break;
                                            }
                                        } else {
                                            // get loosers
                                            if (competitionMatchPrevious.competitionGroup.lane + 1 == this.competitionGroup.lane) {
                                                int i = 0;
                                                for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                    if (i >= competitionCreationParamPhase.participantQualifiedPerMatch && participantResult.participant != null && getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                        if (!participantsMap.containsKey(competitionMatchPrevious))
                                                            participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                        participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                        participantFound++;
                                                    }
                                                    i++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            int numberOfParticipantExpected = participantFound;
                            List<Participant> participants = new ArrayList<>();
//                        if (numberOfParticipantExpected > competitionSeed.competitionCreationParamPhase.numberOfParticipantMatch) {
                            int expectedSize = 0;
                            Map<CompetitionMatch, Integer> competitionMatchSplit = new HashMap<>();
                            for (CompetitionMatchLink competitionMatchPreviousMatchLink : previousCompetitionMatchLinks) {
                                CompetitionMatch competitionMatchPrevious = competitionMatchPreviousMatchLink.previousCompetitionMatch;
//                            for (CompetitionMatch competitionMatch : competitionMatchPrevious.nextCompetitionMatches) {
                                if (competitionMatchPrevious.nextCompetitionMatchLinks.size() == 1) {
                                    if (participantsMap.containsKey(competitionMatchPrevious)) {
                                        participants.addAll(participantsMap.get(competitionMatchPrevious));
                                        participantsMap.remove(competitionMatchPrevious);
                                        expectedSize = participants.size();
                                    }
                                } else {
                                    if (competitionMatchPrevious.competitionGroup.lane == this.competitionGroup.lane) {
                                        if (participantsMap.containsKey(competitionMatchPrevious)) {
                                            participants.addAll(participantsMap.get(competitionMatchPrevious));
                                            participantsMap.remove(competitionMatchPrevious);
                                            expectedSize = participants.size();
                                        }
                                    } else {
                                        int numberOfNotInitialized = 0;
                                        for (CompetitionMatchLink competitionMatchNextMatchLink : competitionMatchPrevious.nextCompetitionMatchLinks) {
                                            CompetitionMatch competitionMatchNext = competitionMatchNextMatchLink.nextCompetitionMatch;
                                            if (!competitionMatchNext.isInitialized() && !competitionMatchNext.isOpen() && !competitionMatchNext.isClosed() && competitionMatchNext.competitionGroup.lane == this.competitionGroup.lane)
                                                numberOfNotInitialized++;
                                        }

                                        if (numberOfNotInitialized == 1) {
                                            if (participantsMap.containsKey(competitionMatchPrevious)) {
                                                participants.addAll(participantsMap.get(competitionMatchPrevious));
                                                participantsMap.remove(competitionMatchPrevious);
                                                expectedSize = participants.size();
                                            }
                                        } else {
                                            if (participantsMap.containsKey(competitionMatchPrevious))
                                                competitionMatchSplit.put(competitionMatchPrevious, numberOfNotInitialized);
                                        }
                                    }
                                }
//                            }
                            }
                            double missingParticipantSize = 0;
                            for (CompetitionMatch competitionMatch : competitionMatchSplit.keySet()) {
                                missingParticipantSize += (double) participantsMap.get(competitionMatch).size() / competitionMatchSplit.get(competitionMatch).doubleValue();
                            }
                            missingParticipantSize = Math.ceil(missingParticipantSize);
                            numberOfParticipantExpected = expectedSize + (int) missingParticipantSize;

//                        }

                            while (participants.size() < numberOfParticipantExpected && !participantsMap.isEmpty()) {
                                List<CompetitionMatch> competitionMatchKeysToRemove = new ArrayList<>();
                                for (CompetitionMatch competitionMatch : participantsMap.keySet()) {
                                    if (!participantsMap.get(competitionMatch).isEmpty()) {
                                        participants.add(participantsMap.get(competitionMatch).get(0));
                                        participantsMap.get(competitionMatch).remove(0);
                                        if (participants.size() == numberOfParticipantExpected)
                                            break;
                                    } else {
                                        competitionMatchKeysToRemove.add(competitionMatch);
                                    }
                                }
                                for (CompetitionMatch competitionMatch : competitionMatchKeysToRemove) {
                                    participantsMap.remove(competitionMatch);
                                }
                            }
                            if (this.participantPairing == null)
                                this.participantPairing = ParticipantPairing.createInstance(competitionCreationParamPhase.numberOfParticipantMatch, this.competitionInstance);
                            for (Participant participant : participants) {
                                this.participantPairing.addParticipant(participant);
                            }
                            this.participantPairing.setCompetitionRound(this.competitionRound);
                            this.participantPairing.setCompetitionMatch(this);
                            if (!this.competitionRound.isOpen())
                                this.competitionRound.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                            if (!this.competitionGroup.isOpen())
                                this.competitionRound.competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                        } else {

                            Map<CompetitionMatch, List<Participant>> participantsMap = new HashMap<>();
                            int participantFound = 0;
                            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                            if (competitionCreationParamPhase == null)
                                competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                            List<Participant> participants = new ArrayList<>();

                            for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
                                CompetitionMatch competitionMatchPrevious = competitionMatchPreviousLink.previousCompetitionMatch;
                                if (competitionMatchPrevious.isClosed()) {
                                    if (competitionMatchPrevious.competitionSeed.id.compareTo(this.competitionSeed.id) != 0) {
                                        int participantPerMatchQualified = competitionCreationParamPhase.numberOfParticipantMatch / this.competitionSeed.getCompetitionGroupResultsPrevious().size();
                                        int i = 0;
                                        for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                            if (getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                if (!participantsMap.containsKey(competitionMatchPrevious))
                                                    participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                participants.add(participantResult.participant);
                                                participantFound++;
                                            }
                                            i++;
                                            if (i >= participantPerMatchQualified)
                                                break;
                                        }
                                    } else {
                                        if (competitionMatchPrevious.competitionGroup.lane == this.competitionGroup.lane) {
                                            // get winners
                                            int i = 0;
                                            for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                if (getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                    if (!participantsMap.containsKey(competitionMatchPrevious))
                                                        participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                    participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                    participants.add(participantResult.participant);
                                                    participantFound++;
                                                }
                                                i++;
                                                if (i >= competitionCreationParamPhase.participantQualifiedPerMatch)
                                                    break;
                                            }
                                        } else {
                                            if (competitionMatchPrevious.competitionGroup.lane + 1 == this.competitionGroup.lane) {
                                                // get loosers
                                                int i = 0;
                                                for (ParticipantResult participantResult : competitionMatchPrevious.getParticipantResults()) {
                                                    if (i >= competitionCreationParamPhase.participantQualifiedPerMatch && participantResult.participant != null && getCompetitionRound().getCompetitionMatchFor(participantResult.participant) == null) {
                                                        if (!participantsMap.containsKey(competitionMatchPrevious))
                                                            participantsMap.put(competitionMatchPrevious, new ArrayList<>());
                                                        participantsMap.get(competitionMatchPrevious).add(participantResult.participant);
                                                        participants.add(participantResult.participant);
                                                        participantFound++;
                                                    }
                                                    i++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (this.participantPairing == null)
                                this.participantPairing = ParticipantPairing.createInstance(competitionCreationParamPhase.numberOfParticipantMatch, this.competitionInstance);
                            for (Participant participant : participants) {
                                this.participantPairing.addParticipant(participant);
                            }
                            this.participantPairing.setCompetitionRound(this.competitionRound);
                            this.participantPairing.setCompetitionMatch(this);
                            if (participantQuantity == participantPairing.getRealParticipantQuantity()) {
                                if (!this.competitionRound.isOpen())
                                    this.competitionRound.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                                if (!this.competitionGroup.isOpen())
                                    this.competitionRound.competitionGroup.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                            }
                        }
                    }
                }
            }
            if (isParticipantPairingDefined()) {
                if (generation || participantQuantity == participantPairing.getRealParticipantQuantity())
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
                else
                    this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
                setChanged();
                notifyObservers(this.localId);
                boolean participantsAvailable = true;
                if (this.getCompetitionRound().competitionRoundPrevious != null) {
                    CompetitionRound competitionRoundPrevious = this.getCompetitionRound().getCompetitionRoundPrevious();
                    CompetitionGroup competitionGroup = this.getCompetitionRound().getCompetitionGroup();
                    for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                        if (competitionRoundPrevious.isForParticipant(participant)) {
                            participantsAvailable = competitionRoundPrevious.isParticipantResultSet(participant);
                        } else if (competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 && competitionGroup.lane > 1) {
                            participantsAvailable = true;
                        }
                        if (!participantsAvailable)
                            break;
                    }
                }
            }
        }
    }

    public boolean isFinished() {
        return participantPairing != null && participantResults.size() == participantPairing.participantSeats.size();
    }

    public boolean isNextCompetitionMatchOf(CompetitionMatch competitionMatch) {
        boolean result = false;
        if (this.nextCompetitionMatchLinks != null && !this.nextCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : this.nextCompetitionMatchLinks) {
                result = competitionMatchLink.nextCompetitionMatch.compareTo(competitionMatch) == 0;
                if (result)
                    break;
            }
        }
        return result;
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
        if (isParticipantPairingDefined()) {
            participantResultSet = true;
            for (Participant participant : this.participantPairing.getRealParticipantsAsArray()) {
                participantResultSet = isParticipantResultSet(participant);
                if (!participantResultSet)
                    break;
            }
        }
        return participantResultSet;
    }

    public boolean isPreviousCompetitionMatchOf(CompetitionMatch competitionGroupMatch) {
        return competitionGroupMatch.previousCompetitionMatchLinksContains(this);
    }

    @Override
    public boolean isSubParticipantResultsSet() {
        boolean participantResultSet = true;
        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
            participantResultSet = competitionPlay.allParticipantResultsSet();
            if (!participantResultSet)
                break;
        }
        return participantResultSet;
    }

//    public void reset() {
//        this.clear();
//        this.participantResults.clear();
//        for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
//            competitionPlay.reset();
//        }
//
//        this.setCompetitionObjectStatus (CompetitionObjectStatus.NOT_INITIALIZED;
//        resetCache();
//    }
//
//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        if (competitionRound != null)
//            description.append("[competitionRound]").append(System.lineSeparator()).append(getCompetitionRound().toString()).append(System.lineSeparator());
//        if (participantPairing != null)
//            description.append("[participantPairing]").append(System.lineSeparator()).append(participantPairing.toDescription());
//        description.append("[competitionPlays]").append(System.lineSeparator());
//        for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//            description.append(competitionPlay.toString()).append(System.lineSeparator());
//        }
//        description.append("[participantResults]").append(System.lineSeparator());
//        for (ParticipantResult participantResult : participantResults) {
//            description.append(participantResult.toDescription());
//        }
//        return description.toString();
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        if (participantPairing != null)
//            description.append(indentation).append("[participantPairing]").append(System.lineSeparator()).append(participantPairing.toDescriptionTree(level + 1));
//        if (!competitionPlays.isEmpty()) {
//            description.append(indentation).append("[competitionPlays]").append(System.lineSeparator());
//            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//                description.append(competitionPlay.toDescriptionTree(level + 1));
//            }
//        }
//        description.append(indentation).append("[participantResults]").append(System.lineSeparator());
//        for (ParticipantResult participantResult : participantResults) {
//            description.append(participantResult.toDescriptionTree(level + 1));
//        }
//        return description.toString();
//
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("round", "" + round);
//
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toDescriptionXml(document));
//        if (participantPairing != null)
//            element.appendChild(participantPairing.toDescriptionXml(document));
//
//        Element elementParticipantResults = document.createElement(ParticipantResult.class.getSimpleName() + "s");
//        if (!this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                elementParticipantResults.appendChild(participantResult.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantResults);
//
//
//        Element elementCompetitionPlays = document.createElement(CompetitionPlay.class.getSimpleName() + "s");
//        if (!competitionPlays.isEmpty()) {
//            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//                elementCompetitionPlays.appendChild(competitionPlay.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementCompetitionPlays);
//
//        return element;
//    }
//
//    public Element toSimpleDescriptionXml(Document document, boolean withResult) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        if (withResult) element.setAttribute("round", "" + round);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        element.setAttribute("numberOfPlay", "" + competitionPlays.size());
////        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
////            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//        if (withResult && participantPairing != null && !participantPairing.participantSeats.isEmpty()) {
//            for (ParticipantSeat participantSeat : participantPairing.participantSeats) {
//                element.appendChild(participantSeat.getParticipant().toSimpleDescriptionXml(document));
//            }
//        }
//
//
//        if (withResult && !competitionPlays.isEmpty()) {
//            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//                element.appendChild(competitionPlay.toSimpleDescriptionXml(document, withResult));
//            }
//        }
//        if (withResult && !this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//        return element;
//    }

    @Override
    public String toString() {
        return "CompetitionMatch{" +
                "localId=" + localId +
                ", name='" + internationalizedLabel.defaultLabel + '\'' +
                ", status=" + competitionObjectStatus +
                (participantPairing != null ? ", participantPairing=" + participantPairing : "") +
                ", round=" + lane +
                (competitionPlays != null ? ", competitionPlays.size()=" + competitionPlays.size() : "") +
                '}';
    }

    public void updateResultDependencies(boolean withClose) throws CompetitionInstanceGeneratorException {
        if (this.isSubClosed()) {
            fillResultFromCompetitionPlay();
            fixResults();
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
            setChanged();
            notifyObservers(this.localId);
            if (withClose)
                this.close();
        }
    }


    public boolean isSubClosed() {
        boolean isSubClosed = true;
        if (getSubCompetitionObjectWithResults() != null) {
            for (CompetitionPlay competitionObjectWithResult : getSubCompetitionObjectWithResults()) {
                if (!competitionObjectWithResult.isCancelled() && !competitionObjectWithResult.isClosed()) {
                    if (competitionObjectWithResult.playType.compareTo(PlayType.NORMAL) == 0) {
                        isSubClosed = false;
                        break;
                    } else {
                        if (competitionObjectWithResult.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
                            competitionObjectWithResult.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
                        }
                    }
                }
            }
        }
        return isSubClosed;
    }

    @Override
    public void updateResultDependencies() throws CompetitionInstanceGeneratorException {
        boolean withClose = this.matchType.compareTo(MatchType.BYE) == 0;
        updateResultDependencies(withClose);
    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        boolean closeOptionalPlay = true;
        if (isOpen() && !isClosed()) {
            if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                this.expectedRelativeEndTime = Duration.ofMinutes((new Date().getTime() - this.competitionInstance.startDate.getTime().getTime()) / (60 * 1000));
            }

            if (checkCompetitionCompetitionObjectStatus(closeOptionalPlay)) {
                this.fillResultFromCompetitionPlay();
                fixResults();
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
            }
        }
        if (competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);

            setChanged();
            notifyObservers(this.localId);
//            notifyObservers(this.localId);
            boolean nextCompetitionMatchesDefined = !this.nextCompetitionMatchLinks.isEmpty();
            boolean previousCompetitionMatchesDefined = !this.previousCompetitionMatchLinks.isEmpty();
//            if (!this.getCompetitionRound().isClosed()) {
            if ((this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0 || this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) == 0) && !this.nextCompetitionMatchLinks.isEmpty()) {
//                    nextCompetitionMatchesDefined=true;
                List<CompetitionMatch> competitionMatchNextToOpen = new ArrayList<>();
                List<CompetitionMatch> competitionMatchNextToInitialize = new ArrayList<>();
                for (CompetitionMatchLink nextCompetitionMatchLink : this.nextCompetitionMatchLinks) {
                    CompetitionMatch competitionMatchNext = nextCompetitionMatchLink.nextCompetitionMatch;
                    boolean allPreviousClosed = true;
                    if (competitionMatchNext != null) {
                        for (CompetitionMatchLink previousCompetitionMatchLink : competitionMatchNext.previousCompetitionMatchLinks) {
                            CompetitionMatch competitionMatchPrevious = previousCompetitionMatchLink.previousCompetitionMatch;
                            if (competitionMatchPrevious != null && !competitionMatchPrevious.isClosed()) {
                                allPreviousClosed = false;
                                break;
                            }
                        }
                    }
                    if (allPreviousClosed) {
                        competitionMatchNextToOpen.add(competitionMatchNext);
                    } else {
                        competitionMatchNextToInitialize.add(competitionMatchNext);
                    }
                }
                boolean noOtherCompetitionMatchNotClosed = true;
                if (competitionRound.competitionMatches.size() > 1) {
                    for (CompetitionMatch competitionMatch : competitionRound.competitionMatches) {
                        if (competitionMatch.compareTo(this) != 0 && !competitionMatch.isClosed()) {
                            noOtherCompetitionMatchNotClosed = false;
                            break;
                        }
                    }
                }
                if (noOtherCompetitionMatchNotClosed) {
                    this.getCompetitionRound().updateResultDependencies();
                }
                if (!competitionMatchNextToOpen.isEmpty()) {
                    for (CompetitionMatch competitionMatch : competitionMatchNextToOpen) {
                        competitionMatch.initializeMatch();
                        if (competitionMatch.isInitialized()) {
                            competitionMatch.open();
                        }
                    }
                }
                if (!competitionMatchNextToInitialize.isEmpty()) {
                    for (CompetitionMatch competitionMatch : competitionMatchNextToInitialize) {
                        competitionMatch.initializeMatch();
                    }
                }

                if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                    int index = 0;
                    CompetitionCreationParamPhase competitionCreationParamPhase = competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;
                    if (competitionCreationParamPhase == null)
                        competitionCreationParamPhase = competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;

                    int maxIndex = competitionCreationParamPhase.participantQualifiedPerMatch;
                    for (ParticipantResult participantResult : this.getParticipantResults()) {
                        index++;
                        if (index <= maxIndex) {
                            participantResult.participant.activate();
                        } else {
                            if (this.competitionGroup.getCompetitionSeed().competitionGroups.size() > this.competitionGroup.lane)
                                participantResult.participant.activate();
                            else
                                participantResult.participant.unactivate();
                        }
                    }
                } else {
                    for (Participant participant : this.getParticipants()) {
                        participant.activate();
                    }
                }

            }
//            }
//            if (!nextCompetitionMatchesDefined && !previousCompetitionMatchesDefined) {
            this.getCompetitionRound().updateResultDependencies();
//            } else {
//                if (this.getCompetitionRound().isSubClosed()) {
//                    this.getCompetitionRound().updateResultDependencies();
//                    setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
//                    this.getCompetitionRound().setCompetitionObjectStatus(CompetitionObjectStatus.CLOSED);
//                    this.getCompetitionRound().fillCompetitionRoundResultFromCompetitionMatch();
//                    if (this.getCompetitionRound().getCompetitionRoundNext() == null) {
//                        this.getCompetitionRound().getCompetitionGroup().updateResultDependencies();
//                    }
//                }
//            }

        }

    }

    @Override
    public void open() throws CompetitionInstanceGeneratorException {
        if (this.isInitialized()) {
            this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
                this.expectedRelativeStartTime = Duration.ofMinutes((new Date().getTime() - this.competitionInstance.startDate.getTime().getTime()) / (60 * 1000));

            }
            this.participantPairing.removeParticipantTeamVoid();
            Sets.sort(competitionPlays);
            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                competitionPlay.initializePlay();
                competitionPlay.open();
            }
            checkBye();
            if (this.matchType.compareTo(MatchType.BYE) == 0) {
                if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) != 0) {
                    checkCompetitionCompetitionObjectStatus(true);
                }
            } else {
                checkMergePolicy();
            }
            setChanged();
            notifyObservers(this.localId);
        }
    }

    public void checkMergePolicy() throws CompetitionInstanceGeneratorException {
        if (competitionSeed.stepType.compareTo(StepType.MERGE) == 0) {
            if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS) == 0) {
                for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                    competitionPlay.checkMergePolicy();
                }
            }
        }
    }

    public boolean checkCompetitionCompetitionObjectStatus(boolean closePlay) throws CompetitionInstanceGeneratorException {
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionRound.competitionGroup.competitionSeed.competitionPhase.competitionCreationParamPhase;
        boolean thresholdScoreEnabled = false;
        ScoreThresholdType scoreThresholdType = null;
        Integer threshold = null;
        Integer winningGapScore = null;
        Integer qualifiedGapScore = null;
        String participantScoreAggregateTypeName = null;
        if (competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy != null &&
                competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy instanceof RankingComputationPolicyScore) {
            RankingComputationPolicyScore rankingComputationPolicyScore = (RankingComputationPolicyScore) competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy;
            if (rankingComputationPolicyScore.scoreThresholdType != null && rankingComputationPolicyScore.scoreThresholdType.compareTo(ScoreThresholdType.NONE) != 0) {
                if (rankingComputationPolicyScore.threshold != null) {
                    threshold = Integer.parseInt(rankingComputationPolicyScore.threshold);
                    thresholdScoreEnabled = true;
                    scoreThresholdType = rankingComputationPolicyScore.scoreThresholdType;
                    if (rankingComputationPolicyScore.winnerGapScore != null)
                        winningGapScore = Integer.parseInt(rankingComputationPolicyScore.winnerGapScore);
                    if (rankingComputationPolicyScore.qualifiedGapScore != null)
                        qualifiedGapScore = Integer.parseInt(rankingComputationPolicyScore.qualifiedGapScore);
                    participantScoreAggregateTypeName = rankingComputationPolicyScore.scoreName;
                }
            }
        }
        boolean isBye = this.matchType.compareTo(MatchType.BYE) == 0;
        boolean remainingAreOptional = isBye;
        boolean newPlayNeeded = false;

//        Map<Participant, Integer> scorePerParticipant = new HashMap<>();
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>(getCompetitionPlays());
        Sets.sort(competitionPlays);
        boolean nextIsInProgress = false;
        if (thresholdScoreEnabled && !isBye) {
            newPlayNeeded = false;

            fillResultFromCompetitionPlay();
            setChanged();
            notifyObservers(this.localId);
            if (participantScoreAggregateTypeName != null) {
                boolean winnerFound = false;
                for (ParticipantResult participantResult : participantResults) {
                    Integer score = Integer.parseInt(participantResult.participantScore.getParticipantScoreValue(participantScoreAggregateTypeName).value);
                    if ((score.compareTo(threshold) >= 0 && scoreThresholdType.compareTo(ScoreThresholdType.HIGH_THRESHOLD) == 0)
                            || (score.compareTo(threshold) > 0 && scoreThresholdType.compareTo(ScoreThresholdType.HIGH_THRESHOLD_STRICT) == 0)
                            || (score.compareTo(threshold) <= 0 && scoreThresholdType.compareTo(ScoreThresholdType.LOW_THRESHOLD) == 0)
                            || (score.compareTo(threshold) < 0 && scoreThresholdType.compareTo(ScoreThresholdType.LOW_THRESHOLD_STRICT) == 0)) {
                        if (winningGapScore != null && winningGapScore != 0) {
// TODO : handle gap between 1st winner and next place
                        } else if (qualifiedGapScore != null && qualifiedGapScore != 0) {
// TODO : handle gap between qualified participants and remainings one
                        } else {
                            winnerFound = true;
                        }
                    }
                }
                if (!winnerFound) {
                    newPlayNeeded = this.getOpenCompetitionPlays().isEmpty();
                } else {
                    remainingAreOptional = true;
                }
            }
        } else {
            Map<Participant, Integer> numberOfWinsPerParticipant = new HashMap<>();
            for (CompetitionPlay competitionPlay : competitionPlays) {
                if (nextIsInProgress && competitionPlay.isInitialized()) {
                    competitionPlay.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                    nextIsInProgress = false;
                } else {
                    int numberOfPlayWin = (int) Math.floor((double) competitionPlays.size() / (double) 2) + 1;
                    numberOfPlayWin = Math.min(numberOfPlayWin, competitionPlays.size());
                    if (this.participantPairing != null) {
                        for (Participant participant : this.participantPairing.getRealParticipantsAsArray()) {
                            if (!numberOfWinsPerParticipant.containsKey(participant))
                                numberOfWinsPerParticipant.put(participant, 0);
                            if (!remainingAreOptional && competitionPlay.isParticipantResultsSet()) {
                                if (competitionPlay.isAWin(participant)) {
                                    numberOfWinsPerParticipant.put(participant, numberOfWinsPerParticipant.get(participant) + 1);
                                    if (numberOfWinsPerParticipant.get(participant) >= numberOfPlayWin) {
                                        remainingAreOptional = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (newPlayNeeded) {
            remainingAreOptional = false;
            addNewCompetitionPlay();
        }
        if (remainingAreOptional) {
            for (CompetitionPlay competitionPlay : competitionPlays) {
                if (remainingAreOptional && !competitionPlay.isClosed()) {
                    if (isBye)
                        competitionPlay.setPlayType(PlayType.BYE);
                    else
                        competitionPlay.setPlayType(PlayType.OPTIONAL);
                    if (closePlay) {
                        if (isBye) {
                            competitionPlay.createParticipantsByeResults();
                        }
                        competitionPlay.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                        competitionPlay.close();
                    } else {
                        competitionPlay.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
                    }
                }
            }
        }
        return remainingAreOptional;
    }

    private void addNewCompetitionPlay() {
        PlayVersusType playVersusType = this.competitionPlays.first().playVersusType;
        CompetitionPlay competitionPlay = this.createCompetitionPlay(this);
        competitionPlay.round = this.competitionPlays.size();
        competitionPlay.playVersusType = playVersusType;
        competitionPlay.playType = PlayType.NORMAL;
        competitionPlay.internationalizedLabel.defaultLabel = competitionPlay.playVersusType.name() + " - roundDetails " + competitionPlay.round;
        Sets.sort(this.competitionPlays);
        competitionPlay.initializePlay();
        competitionPlay.open();
    }


    //    public CompetitionPlay getCompetitionPlay(int localId) {
//        for (CompetitionPlay competitionPlay : competitionPlays) {
//            if (competitionPlay.localId.compareTo(localId) == 0)
//                return competitionPlay;
//        }
//        return null;
//    }
    public boolean checkBye() {
        CompetitionGroupFormat competitionGroupFormat = this.getCompetitionRound().getCompetitionGroup().competitionGroupFormat;
        CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;
        int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
        int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
        boolean participantTypeSplittable = competitionCreationParamPhase.participantTypeSplittable != null && competitionCreationParamPhase.participantTypeSplittable;
        int matchInRoundSize = this.getCompetitionRound().getCompetitionMatches().size();
        return checkBye(competitionGroupFormat, participantQualifiedPerMatch, matchInRoundSize, numberOfParticipantMatch, participantTypeSplittable);
    }

    public boolean checkBye(CompetitionGroupFormat competitionGroupFormat, int participantQualifiedPerMatch,
                            int matchInRoundSize, int numberOfParticipantMatch, boolean participantTypeSplittable) {
        boolean isBye = false;
        if (numberOfParticipantMatch >= 1 && this.participantPairing != null) {
            int participantQuantity = this.participantPairing.getRealParticipantQuantity();
            boolean onlyMatchInRound = matchInRoundSize == 1;

            switch (competitionGroupFormat) {
                case ELIMINATION:
//                    isBye = participantQuantity <= participantQualifiedPerMatch && onlyMatchInRound;
                    isBye = participantQuantity <= participantQualifiedPerMatch || (!participantTypeSplittable && participantQuantity < numberOfParticipantMatch);
                    break;
                case ROUND_ROBIN:
                case SWISS:
                    isBye = participantQuantity <= participantQualifiedPerMatch || (!participantTypeSplittable && participantQuantity < numberOfParticipantMatch);
                    break;
            }
        }
        if (isBye)
            this.matchType = MatchType.BYE;
        else
            this.matchType = MatchType.NORMAL;
        return isBye;
    }

    public void initForXmlOutput() {
//        previousCompetitionMatches.clear();
//        for (CompetitionMatch previousCompetitionMatch : previousCompetitionMatches) {
//            previousCompetitionMatches.add(previousCompetitionMatch.localId);
//        }
//        nextCompetitionMatches.clear();
//        for (CompetitionMatch nextCompetitionMatch : nextCompetitionMatches) {
//            nextCompetitionMatches.add(nextCompetitionMatch.localId);
//        }
//        for (CompetitionPlay competitionPlay : competitionPlays) {
//            competitionPlay.initForXmlOutput();
//        }
//        for (ParticipantResult participantResult : participantResults) {
//            participantResult.initForXmlOutput();
//        }
//        if (participantPairing != null) participantPairing.initForXmlOutput();

    }

    @Override
    public String getLocalId() {
        return localId;
    }

    public void initFromXmlInputResult(CompetitionInstance competitionInstance) {
        this.idGeneratorCache = competitionInstance.getIdGenerator();

//        for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
//            competitionPlay.initFromXmlInputResult(competitionInstance);
//        }
        for (ParticipantResult participantResult : participantResults) {
            participantResult.initFromXmlInput(competitionInstance);
        }
        Sets.sort(participantResults);
//        List<ParticipantResult> participantResultsTmp = new ArrayList<ParticipantResult>();
//        participantResultsTmp.addAll(participantResults);
//        participantResults.clear();
//        participantResults.addAll(participantResultsTmp);
    }

    @Override
    @JsonIgnore
    public CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return this.getCompetitionRound();
    }

    @Override
    @JsonIgnore
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (previousCompetitionMatchLinks != null && !previousCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : previousCompetitionMatchLinks) {
                competitionObjectWithResults.add(competitionMatchLink.previousCompetitionMatch);
            }
        }
        return competitionObjectWithResults;
    }

    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();
        if (nextCompetitionMatchLinks != null && !nextCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchLink : nextCompetitionMatchLinks) {
                competitionObjectWithResults.add(competitionMatchLink.previousCompetitionMatch);
            }
        }
        return competitionObjectWithResults;
    }


    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant)) {
            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                overForParticipant = competitionPlay.isOverForParticipant(participant);
                if (!overForParticipant)
                    break;
            }
        }
        return overForParticipant;
    }

    public boolean isParticipantPairingDefined() {
        return participantPairing != null && !participantPairing.isVoid();
    }

    @Override
    public SortedSet<ParticipantPairing> getParticipantPairings() {
        SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
        if (isParticipantPairingDefined())
            participantPairings.add(participantPairing);
        return participantPairings;
    }


    @Override
    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        if (this.participantPairing != null)
            this.participantPairing.setCompetitionInstance(competitionInstance);
        super.spreadCompetitionInstance(competitionInstance);
    }

    public CompetitionMatchTree getCompetitionMatchTree() {
        CompetitionMatchTree competitionMatchTree = new CompetitionMatchTree();
        competitionMatchTree.internationalizedLabel = internationalizedLabel;
        competitionMatchTree.id = id;
        competitionMatchTree.localId = getLocalId();
        competitionMatchTree.databaseId = databaseId;
        competitionMatchTree.lane = lane;
        competitionMatchTree.over = isFinished();
        competitionMatchTree.bye = isBye();
        competitionMatchTree.filled = isParticipantPairingDefined();
        competitionMatchTree.expectedDuration = this.expectedDuration;
        competitionMatchTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionMatchTree.expectedRelativeEndTime = this.expectedRelativeEndTime;

        CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;

        ScoringConfiguration scoringConfiguration = competitionCreationParamPhase.scoringConfiguration;
        Set<String> scoringConfigurationMatchElementNames = new HashSet<>();
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationMatch != null && scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements != null) {
            for (ScoringConfigurationMatchElement scoringConfigurationMatchElement :
                    scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements) {
                scoringConfigurationMatchElementNames.add(scoringConfigurationMatchElement.participantScoreAggregateType.name());
            }
        }
        for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
            CompetitionMatch competitionMatchPrevious = competitionMatchPreviousLink.previousCompetitionMatch;
            competitionMatchTree.previousCompetitionMatchIds.add(competitionMatchPrevious.localId);
        }

        for (CompetitionMatchLink nextCompetitionMatchLink : this.nextCompetitionMatchLinks) {
            competitionMatchTree.nextCompetitionMatchIds.add(nextCompetitionMatchLink.nextCompetitionMatch.localId);
        }

        for (CompetitionPlay competitionPlay : getCompetitionPlays())
            competitionMatchTree.competitionPlayTrees.add(competitionPlay.getCompetitionPlayTree());

        if (!participantResults.isEmpty()) {
            int numberOfQualifiedParticipant = getCompetitionRound().getCompetitionGroup().competitionGroupFormat.getNumberOfParticipantQualifiedPerMatch(this);
            int index = 0;
            for (ParticipantResult participantResult : participantResults) {
                competitionMatchTree.participantResultTrees.add(participantResult.toParticipantResultTree(numberOfQualifiedParticipant > index, scoringConfigurationMatchElementNames));
                index++;
            }
        } else if (isParticipantPairingDefined()) {
            ParticipantResult participantResult = null;
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                participantResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                participantResult.setParticipant(getParticipantTeamFromParticipant(participant));
                competitionMatchTree.participantResultTrees.add(participantResult.toParticipantResultTree(scoringConfigurationMatchElementNames));
            }
        } else {
            ParticipantResult participantResult = null;
            for (int i = 0; i < competitionCreationParamPhase.playVersusType.numberOfTeam; i++) {
                participantResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                competitionMatchTree.participantResultTrees.add(participantResult.toParticipantResultTree(scoringConfigurationMatchElementNames));
            }

        }
        return competitionMatchTree;

    }

    public Boolean isBye() {
        return this.matchType != null && this.matchType.compareTo(MatchType.BYE) == 0;
    }

    public String toScreenDescription() {

        StringBuilder description = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(3);
        numberFormat.setGroupingUsed(false);

        description.append("Match(").append(numberFormat.format(this.localId)).append(")\tRound(").append(numberFormat.format(this.getCompetitionRound().round)).append(")\tLane(").append(numberFormat.format(this.getCompetitionRound().getCompetitionGroup().lane)).append(")\tPhase(").append(numberFormat.format(this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().localId)).append(")");
        if (!participantPairing.isVoid()) {
            List<Participant> participants = participantPairing.getRealParticipantsAsArray();
            for (Participant participant : participants) {
                description.append(System.lineSeparator());
                if (participant instanceof ParticipantSingle || (participant instanceof ParticipantTeam && ((ParticipantTeam) participant).participantTeamMembers.size() > 1))
                    description.append("\t").append(participant.internationalizedLabel.defaultLabel).append("(").append(numberFormat.format(participant.localId)).append(")");
                else if (participant instanceof ParticipantTeam && ((ParticipantTeam) participant).participantTeamMembers.size() > 0)
                    description.append("\t").append(((ParticipantTeam) participant).participantTeamMembers.first().getParticipant().internationalizedLabel.defaultLabel).append("(").append(numberFormat.format(((ParticipantTeam) participant).participantTeamMembers.first().localId)).append(")");
                for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                    ParticipantResult participantResult = competitionPlay.getParticipantResultFor(participant);
                    if (participantResult != null)
                        description.append("\t").append(participantResult.rank);
                    else
                        description.append("\t0");
                }

//                ParticipantScore participantScore = this.findParticipantScore(participant.localId);
//                if (participantScore != null)
//                    description.append("\t").append(participantScore.toSimpleDescription());
//                description.append(System.lineSeparator());
            }
        }
        return description.toString();
    }

    public boolean isPairingDone() {
        return participantPairing != null && participantPairing.isFull();
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return this.participantResults;
    }

    @Override
    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        super.setParticipantResults(participantResults);

    }

    @Override
    public SortedSet<CompetitionPlay> getSubCompetitionObjectWithResults() {
        return getCompetitionPlays();
    }

    @Override
    public CompetitionRound getUpperCompetitionObjectWithResult() {
        return this.getCompetitionRound();
    }

    public List<Participant> getParticipantsQualified() {
        return getParticipants(true, false);
    }

    public List<Participant> getParticipants(boolean qualified, boolean eliminated) {
        List<Participant> participants = new ArrayList<>();
        CompetitionGroupFormat competitionGroupFormat = this.getCompetitionRound().getCompetitionGroup().competitionGroupFormat;

        CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;


        PlayVersusType playVersusType = competitionCreationParamPhase.playVersusType;
        int numberOfParticipantQualified = competitionCreationParamPhase.participantQualifiedPerMatch;
        int numberOfParticipantPerMatch = competitionCreationParamPhase.numberOfParticipantMatch;
        int participantPresent = this.participantPairing.participantSeats.size();
        int participantQualifiedQuantity = competitionGroupFormat.computeNumberOfQualifiedParticipantPerMatch(numberOfParticipantPerMatch, playVersusType, numberOfParticipantQualified, participantPresent);
        int index = 0;
        for (ParticipantResult participantResult : this.participantResults) {
            if (index < participantQualifiedQuantity && qualified)
                participants.add(participantResult.participant);
            else if (eliminated && index >= participantQualifiedQuantity)
                participants.add(participantResult.participant);
            else if (!eliminated)
                break;
            index++;
        }
        return participants;
    }

    public List<Participant> getParticipantsEliminated() {
        List<Participant> participantsEliminated = new ArrayList<>();
        if (isClosed()) {

            CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = this.getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

            participantsEliminated = getParticipants(false, true);
            int numberOfParticipantQualified = competitionCreationParamPhase.participantQualifiedPerMatch;
            int numberOfParticipantPerMatch = competitionCreationParamPhase.numberOfParticipantMatch;
            while (participantsEliminated.size() < numberOfParticipantPerMatch - numberOfParticipantQualified) {
                participantsEliminated.add(null);
            }
        }
        return participantsEliminated;
    }

    public Participant getParticipantTeamFromParticipant(Participant participant) {
        Participant participantTeam = null;
        for (ParticipantSeat participantSeat : participantPairing.participantSeats) {
            participantTeam = participantSeat.getParticipant().getParticipant(participant.localId);
            if (participantTeam != null) {
                participantTeam = participantSeat.getParticipant();
                break;
            }
        }
        return participantTeam;
    }

    @Override
    public void addParticipantPairing(ParticipantPairing participantPairing) {
        setParticipantPairing(participantPairing);
    }

    @Override
    protected void sortParticipantPairings() {
        if (this.participantPairing != null) {
            this.participantPairing.sortParticipantSeats();
        }
    }

    public void setParticipantPairing(ParticipantPairing participantPairing) {
        if (this.participantPairing == null || this.participantPairing.localId.compareTo(participantPairing.localId) != 0) {
            this.participantPairing = participantPairing;
            this.participantPairing.setCompetitionInstance(getCompetitionInstance());
            this.participantPairing.setCompetitionMatch(this);
            this.participantPairing.sortParticipantSeats();
            setChanged();
            notifyObservers(this.localId);
        }
    }

    public void clearDatabaseId() {
        this.databaseId = null;
        if (competitionPlays != null)
            for (CompetitionPlay competitionPlay : competitionPlays) {
                competitionPlay.clearDatabaseId();
            }
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }


        if (participantPairing != null)
            participantPairing.clearDatabaseId();


    }

    @Override
    public CompetitionMatch cloneSimplified() {
        CompetitionMatch competitionMatch = null;
        try {
            competitionMatch = (CompetitionMatch) this.clone();
//            competitionMatch.competitionPlays = new TreeSet<>();
            if (this.participantResults != null) {
                List<ParticipantResult> participantResults = new ArrayList<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    participantResults.add(participantResult.cloneSimplified());
                }
                competitionMatch.participantResults = new TreeSet<>();
                competitionMatch.participantResults.addAll(participantResults);
            }
//            competitionMatch.fillPairingCache();
//            competitionMatch.fillResultCache();

        } catch (CloneNotSupportedException e) {
        }
        return competitionMatch;
    }

    @Override
    protected Integer getRoundOrLane() {
        return this.lane;
    }


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
            boolean isCompetitionMatchRankingScoreBased = this.competitionPhase.competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
            if (isCompetitionMatchRankingScoreBased) {
                expectedGlobalDurationStatisticsStructure.min = this.competitionPhase.competitionCreationParamPhase.getMinimumPlayDuration().toMinutes();
                expectedGlobalDurationStatisticsStructure.max = this.competitionPhase.competitionCreationParamPhase.getMaximumPlayDuration().toMinutes();
                expectedGlobalDurationStatisticsStructure.avg = this.competitionPhase.competitionCreationParamPhase.getAveragePlayDuration().toMinutes();
            } else {
                expectedGlobalDurationStatisticsStructure.min = getExpectedGlobalPlay().min * competitionPlays.first().getExpectedGlobalDuration().min;
                expectedGlobalDurationStatisticsStructure.max = getExpectedGlobalPlay().max * competitionPlays.first().getExpectedGlobalDuration().max;
                expectedGlobalDurationStatisticsStructure.avg = getExpectedGlobalPlay().avg * competitionPlays.first().getExpectedGlobalDuration().avg;
            }
        }


        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayStatisticsStructureInitialized) {
            expectedGlobalPlayStatisticsStructureInitialized = true;
            boolean isCompetitionMatchRankingScoreBased = this.competitionPhase.competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
            if (isCompetitionMatchRankingScoreBased) {
                expectedGlobalPlayStatisticsStructure.min = (long) 1;
                expectedGlobalPlayStatisticsStructure.max = (long) 1;
                expectedGlobalPlayStatisticsStructure.avg = (long) 1;
            } else {
                expectedGlobalPlayStatisticsStructure.min = (long) (competitionPlays.size() / 2 + 1);
                expectedGlobalPlayStatisticsStructure.max = (long) competitionPlays.size();
                expectedGlobalPlayStatisticsStructure.avg = (expectedGlobalPlayStatisticsStructure.min + expectedGlobalPlayStatisticsStructure.max) / 2;
                if (expectedGlobalPlayStatisticsStructure.avg == 0)
                    expectedGlobalPlayStatisticsStructure.avg = expectedGlobalPlayStatisticsStructure.min;
            }
        }
        return expectedGlobalPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {

        if (!expectedParticipantDurationStatisticsStructureInitialized) {
            expectedParticipantDurationStatisticsStructureInitialized = true;
            expectedParticipantDurationStatisticsStructure.max = 0L;
            expectedParticipantDurationStatisticsStructure.min = 0L;
            expectedParticipantDurationStatisticsStructure.avg = 0L;
            expectedParticipantDurationStatisticsStructure.count = 0L;
            expectedParticipantDurationStatisticsStructure.sum = 0L;
            boolean isCompetitionMatchRankingScoreBased = this.competitionPhase.competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
            if (isCompetitionMatchRankingScoreBased) {
                expectedParticipantDurationStatisticsStructure.min = this.competitionPhase.competitionCreationParamPhase.getMinimumPlayDuration().toMinutes();
                expectedParticipantDurationStatisticsStructure.max = this.competitionPhase.competitionCreationParamPhase.getMaximumPlayDuration().toMinutes();
                expectedParticipantDurationStatisticsStructure.avg = this.competitionPhase.competitionCreationParamPhase.getAveragePlayDuration().toMinutes();
            } else {
                for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                    expectedParticipantDurationStatisticsStructure.max += competitionPlay.getExpectedParticipantDuration().max;
                    expectedParticipantDurationStatisticsStructure.min += competitionPlay.getExpectedParticipantDuration().min;
                    expectedParticipantDurationStatisticsStructure.avg += competitionPlay.getExpectedParticipantDuration().avg;
//                expectedParticipantDurationStatisticsStructure.count += competitionPlay.getExpectedParticipantDuration().count;
//                expectedParticipantDurationStatisticsStructure.sum += competitionPlay.getExpectedParticipantDuration().sum;
                }
            }
        }
        return expectedParticipantDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        if (!expectedParticipantPlayStatisticsStructureInitialized) {
            expectedParticipantPlayStatisticsStructureInitialized = true;
            boolean isCompetitionMatchRankingScoreBased = this.competitionPhase.competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
            if (isCompetitionMatchRankingScoreBased) {
                expectedParticipantPlayStatisticsStructure.min = (long) 1;
                expectedParticipantPlayStatisticsStructure.max = (long) 1;
                expectedParticipantPlayStatisticsStructure.avg = (long) 1;

            } else {
                for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                    expectedParticipantPlayStatisticsStructure.max += competitionPlay.getExpectedParticipantPlay().max;
                    expectedParticipantPlayStatisticsStructure.min += competitionPlay.getExpectedParticipantPlay().min;
                    expectedParticipantPlayStatisticsStructure.avg += competitionPlay.getExpectedParticipantPlay().avg;
                }
            }
        }
        return expectedParticipantPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        if (!expectedGlobalStepStatisticsStructureInitialized) {
            expectedGlobalStepStatisticsStructureInitialized = true;
            boolean isCompetitionMatchRankingScoreBased = this.competitionPhase.competitionCreationParamPhase.isCompetitionMatchRankingScoreBased();
            if (isCompetitionMatchRankingScoreBased) {
                expectedGlobalStepStatisticsStructure.min = (long) 1;
                expectedGlobalStepStatisticsStructure.max = (long) 1;
                expectedGlobalStepStatisticsStructure.avg = (long) 1;

            } else {
                expectedGlobalStepStatisticsStructure.min = (long) (competitionPlays.size() / 2);
                if (expectedGlobalStepStatisticsStructure.min == 0)
                    expectedGlobalStepStatisticsStructure.min = (long) competitionPlays.size();
                expectedGlobalStepStatisticsStructure.max = (long) competitionPlays.size();
                expectedGlobalStepStatisticsStructure.avg = (expectedGlobalStepStatisticsStructure.min + expectedGlobalStepStatisticsStructure.max) / 2;
                if (expectedGlobalStepStatisticsStructure.avg == 0)
                    expectedGlobalStepStatisticsStructure.avg = (long) competitionPlays.size();
            }
        }
        return expectedGlobalStepStatisticsStructure;
    }

    public void addCompetitionPlay(CompetitionPlay competitionPlay) {
        competitionPlay.competitionMatch = this;
        competitionPlays.add(competitionPlay);
//        competitionPlaysCache = null;

    }

    @Override
    public void resetCache() {
        super.resetCache();
//        this.competitionPlaysCache = null;
//        this.competitionRoundCache = null;
//        if (this.participantPairing != null)
//            this.participantPairing.resetCache();
//        if (this.participantResults != null) {
//            for (ParticipantResult participantResult : this.participantResults) {
//                participantResult.resetCache();
//            }
//        }
    }

    @Override
    public void reset(boolean recursive) {
        super.reset(recursive);
        this.participantPairing = null;
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0 || this.competitionPhase.competitionCreationParamPhase.playVersusType.numberOfTeam == 1 || (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) != 0 && this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.ROUND_ROBIN) != 0 && this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) != 0)) {
//            this.previousCompetitionMatches.clear();
//            this.nextCompetitionMatches.clear();
            this.competitionInstance.competitionMatchLinks.removeAll(this.previousCompetitionMatchLinks);
            this.competitionInstance.competitionMatchLinks.removeAll(this.nextCompetitionMatchLinks);
            this.previousCompetitionMatchLinks.clear();
            this.nextCompetitionMatchLinks.clear();
        }
        if (this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            competitionInstance.competitionMatches.remove(this);
            this.competitionRound.competitionMatches.remove(this);
            this.competitionInstance = null;
            this.competitionPhase = null;
            this.competitionSeed = null;
            this.competitionGroup = null;
            this.competitionRound = null;
        }
        resetCache();
    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionRound != null ? competitionRound.localId : null;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionMatch competitionMatch = new CompetitionMatch();
        competitionMatch.id = this.id;
        competitionMatch.databaseId = this.databaseId;
        competitionMatch.localId = this.localId;
        return competitionMatch;
    }

    @Override
    public void clearForContext() {
        competitionPlays = null;
        if (nextCompetitionMatchLinks != null) {
            SortedSet<CompetitionMatchLink> nextCompetitionMatchLinksNew = new TreeSet<>();
            for (CompetitionMatchLink competitionMatchLink :
                    nextCompetitionMatchLinks) {
                CompetitionMatchLink competitionMatchLinkNew = new CompetitionMatchLink();
                competitionMatchLinkNew.id = competitionMatchLink.id;
                competitionMatchLinkNew.nextCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.nextCompetitionMatch.id = competitionMatchLink.nextCompetitionMatch.id;
                competitionMatchLinkNew.previousCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.previousCompetitionMatch.id = competitionMatchLink.previousCompetitionMatch.id;
                nextCompetitionMatchLinksNew.add(competitionMatchLinkNew);
            }
            nextCompetitionMatchLinks = nextCompetitionMatchLinksNew;
        }
        if (previousCompetitionMatchLinks != null) {
            SortedSet<CompetitionMatchLink> previousCompetitionMatchLinksNew = new TreeSet<>();
            for (CompetitionMatchLink competitionMatchLink :
                    previousCompetitionMatchLinks) {
                CompetitionMatchLink competitionMatchLinkNew = new CompetitionMatchLink();
                competitionMatchLinkNew.id = competitionMatchLink.id;
                competitionMatchLinkNew.nextCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.nextCompetitionMatch.id = competitionMatchLink.nextCompetitionMatch.id;
                competitionMatchLinkNew.previousCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.previousCompetitionMatch.id = competitionMatchLink.previousCompetitionMatch.id;
                previousCompetitionMatchLinksNew.add(competitionMatchLinkNew);
            }
            previousCompetitionMatchLinks = previousCompetitionMatchLinksNew;
        }
        participantResults = null;
        participantPairing = null;
        competitionRound.clearForContext();
        String competitionRoundId = competitionRound.id;
        competitionRound = new CompetitionRound();
        competitionRound.id = competitionRoundId;
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
        CompetitionMatch competitionMatch = (CompetitionMatch) this.clone();


        if (nextCompetitionMatchLinks != null) {
            SortedSet<CompetitionMatchLink> nextCompetitionMatchLinksNew = new TreeSet<>();
            for (CompetitionMatchLink competitionMatchLink :
                    nextCompetitionMatchLinks) {
                CompetitionMatchLink competitionMatchLinkNew = new CompetitionMatchLink();
                competitionMatchLinkNew.id = competitionMatchLink.id;
                competitionMatchLinkNew.nextCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.nextCompetitionMatch.id = competitionMatchLink.nextCompetitionMatch.id;
                competitionMatchLinkNew.previousCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.previousCompetitionMatch.id = competitionMatchLink.previousCompetitionMatch.id;
                nextCompetitionMatchLinksNew.add(competitionMatchLinkNew);
            }
            competitionMatch.nextCompetitionMatchLinks = nextCompetitionMatchLinksNew;
        }
        if (previousCompetitionMatchLinks != null) {
            SortedSet<CompetitionMatchLink> previousCompetitionMatchLinksNew = new TreeSet<>();
            for (CompetitionMatchLink competitionMatchLink :
                    previousCompetitionMatchLinks) {
                CompetitionMatchLink competitionMatchLinkNew = new CompetitionMatchLink();
                competitionMatchLinkNew.id = competitionMatchLink.id;
                competitionMatchLinkNew.nextCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.nextCompetitionMatch.id = competitionMatchLink.nextCompetitionMatch.id;
                competitionMatchLinkNew.previousCompetitionMatch = new CompetitionMatch();
                competitionMatchLinkNew.previousCompetitionMatch.id = competitionMatchLink.previousCompetitionMatch.id;
                previousCompetitionMatchLinksNew.add(competitionMatchLinkNew);
            }
            competitionMatch.previousCompetitionMatchLinks = previousCompetitionMatchLinksNew;
        }


        String competitionRoundId = competitionRound.id;
        competitionMatch.competitionRound = new CompetitionRound();
        competitionMatch.competitionRound.id = competitionRoundId;
        String competitionGroupId = competitionGroup.id;
        competitionMatch.competitionGroup = new CompetitionGroup();
        competitionMatch.competitionGroup.id = competitionGroupId;
        String competitionSeedId = competitionSeed.id;
        competitionMatch.competitionSeed = new CompetitionSeed();
        competitionMatch.competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionMatch.competitionPhase = new CompetitionPhase();
        competitionMatch.competitionPhase.id = competitionPhaseId;

        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionMatch.competitionInstance = new CompetitionInstance();
        competitionMatch.competitionInstance.id = competitionInstanceId;
        competitionMatch.competitionInstance.version = version;

        competitionMatch.competitionPlays = null;
        if (this.getCompetitionPlays() != null && !this.getCompetitionPlays().isEmpty()) {
            competitionMatch.competitionPlays = new TreeSet<>();
            SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
            for (CompetitionPlay competitionPlay : this.getCompetitionPlays()) {
                competitionPlays.add((CompetitionPlay) competitionPlay.cloneForContext());
            }
            competitionMatch.competitionPlays.addAll(competitionPlays);
        }
        if (getParticipantPairings() != null && getParticipantPairings().isEmpty()) {
            competitionMatch.participantPairing = null;
        }

        return competitionMatch;
    }

    //    public void addParticipant(Participant participant) {
//        if (!participantPairing.isFull()) {
//            participantPairing.addParticipant(participant);
//        }
//    }
    public boolean acceptParticipant() {
        return participantPairing != null && participantQuantity > participantPairing.getRealParticipantQuantity();
    }

    public CompetitionPlay getCompetitionPlayForRound(int round) {
        CompetitionPlay competitionPlay = competitionInstance.getCompetitionPlayForMatch(this, round);
        return competitionPlay;

    }

    public void cancel() {
        super.cancel();
    }

    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantOpponentsMap = new HashMap<>();
        if (competitionPlays != null) {
            for (CompetitionPlay competitionPlay : competitionPlays) {
                Map<Participant, List<Participant>> participantOpponentsPlayMap = competitionPlay.getParticipantOpponentsMap();
                for (Participant participant : participantOpponentsPlayMap.keySet()) {
                    if (!participantOpponentsMap.containsKey(participant))
                        participantOpponentsMap.put(participant, new ArrayList<>());
                    participantOpponentsMap.get(participant).addAll(participantOpponentsMap.get(participant));
                }
            }
        }
        return participantOpponentsMap;
    }

    public boolean isWithParticipants(List<Participant> participants) {
        boolean result = false;
        if (isParticipantPairingDefined() && !participants.isEmpty()) {
            result = true;
            for (Participant participant : participants) {
                if (!this.participantPairing.contains(participant)) {
                    result = false;
                    break;
                }
            }

        }
        return result;
    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (this.previousCompetitionMatchLinks != null && !this.previousCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
                CompetitionMatch competitionMatch = competitionMatchPreviousLink.previousCompetitionMatch;
                if (expectedRelativeStartTime == null || expectedRelativeStartTime.compareTo(competitionMatch.expectedRelativeEndTime) < 0)
                    expectedRelativeStartTime = competitionMatch.expectedRelativeEndTime;
            }
        } else {
            expectedRelativeStartTime = this.competitionRound.expectedRelativeStartTime;
        }
        if (isBye()) {
            expectedRelativeEndTime = expectedRelativeStartTime;
            expectedDuration = Duration.ofMinutes(0);
        } else {
            for (CompetitionPlay competitionPlay : this.competitionPlays) {
                competitionPlay.fillExpectedRelativeTime();
            }
            expectedDuration = competitionPlays.last().expectedRelativeEndTime.minus(competitionPlays.first().expectedRelativeStartTime).plus(Duration.parse(this.competitionPhase.competitionCreationParamPhase.intermissionDuration));
            expectedRelativeEndTime = expectedRelativeStartTime.plus(expectedDuration);

//            Duration expectedPlayDuration = Duration.parse(this.competitionPhase.competitionCreationParamPhase.averagePlayDuration);
//            Duration intermission = Duration.parse(this.competitionPhase.competitionCreationParamPhase.intermissionDuration);
            Integer maximumNumberOfParallelPlay = this.competitionPhase.competitionCreationParamPhase.maximumNumberOfParallelPlay;
            if (maximumNumberOfParallelPlay != null && maximumNumberOfParallelPlay > 0) {
                int simultaneousMatch = 0;

                do {
                    simultaneousMatch = 1;
                    List<CompetitionMatch> competitionMatchList = new ArrayList<>();
                    SortedSet<CompetitionRound> competitionRounds = new TreeSet<>();
                    competitionRounds.addAll(this.competitionPhase.getCompetitionRoundsForDate(expectedRelativeStartTime));
                    for (CompetitionRound competitionRound : competitionRounds) {
                        competitionMatchList.addAll(competitionRound.competitionMatches);
                    }

                    SortedSet<CompetitionMatch> competitionMatches = new TreeSet<>(competitionMatchList);
                    Duration nextStep = null;
                    for (CompetitionMatch competitionMatch : competitionMatches) {
                        if (competitionMatch.id.compareTo(this.id) != 0 && !competitionMatch.isBye() && competitionMatch.expectedRelativeStartTime != null && competitionMatch.expectedRelativeEndTime != null && competitionMatch.expectedRelativeEndTime.compareTo(expectedRelativeStartTime) > 0 && competitionMatch.expectedRelativeStartTime.compareTo(expectedRelativeEndTime) < 0) {
                            if (nextStep == null || nextStep.compareTo(competitionMatch.expectedRelativeEndTime) > 0) {
                                nextStep = competitionMatch.expectedRelativeEndTime;
                            }
                            simultaneousMatch++;
                        }
                        if (simultaneousMatch > maximumNumberOfParallelPlay) {
                            expectedRelativeStartTime = nextStep;
                            expectedRelativeEndTime = expectedRelativeStartTime.plus(expectedDuration);
                            break;
                        }
                    }
                } while (simultaneousMatch > maximumNumberOfParallelPlay);
                for (CompetitionPlay competitionPlay : this.competitionPlays) {
                    competitionPlay.fillExpectedRelativeTime();
                }
            }
//            expectedDuration = competitionPlays.last().expectedRelativeEndTime.minus(competitionPlays.first().expectedRelativeStartTime).plus(Duration.parse(this.competitionPhase.competitionCreationParamPhase.intermissionDuration));
//            expectedRelativeEndTime = expectedRelativeStartTime.plus(expectedDuration);
        }
    }

    public List<CompetitionPlay> getCompetitionPlayForParticipants(SortedSet<Participant> participants) {
        List<CompetitionPlay> competitionPlays = new ArrayList<>();
        for (CompetitionPlay competitionPlay : this.competitionPlays) {
            for (Participant participant : participants) {
                if (competitionPlay.getParticipants().contains(participant)) {
                    competitionPlays.add(competitionPlay);
                    break;
                }
            }

        }
        return competitionPlays;
    }

    public void removeNextCompetitionMatches() {
        if (nextCompetitionMatchLinks != null) {
            for (CompetitionMatchLink competitionMatchNextLink : nextCompetitionMatchLinks) {
                competitionMatchNextLink.previousCompetitionMatch.removePreviousCompetitionMatch(this);
            }
            nextCompetitionMatchLinks.clear();
        }
    }

    public void removePreviousCompetitionMatch(CompetitionMatch competitionMatch) {
        List<CompetitionMatchLink> competitionMatchLinksToRemove = new ArrayList<>();
        if (previousCompetitionMatchLinks != null && !previousCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchPreviousLink : previousCompetitionMatchLinks) {
                if (competitionMatchPreviousLink.previousCompetitionMatch.compareTo(competitionMatch) == 0) {
                    competitionMatchLinksToRemove.add(competitionMatchPreviousLink);
                }
            }
            previousCompetitionMatchLinks.removeAll(competitionMatchLinksToRemove);
        }

    }


    public void removeNextCompetitionMatch(CompetitionMatch competitionMatch) {
        List<CompetitionMatchLink> competitionMatchLinksToRemove = new ArrayList<>();
        if (nextCompetitionMatchLinks != null && !nextCompetitionMatchLinks.isEmpty()) {
            for (CompetitionMatchLink competitionMatchNextLink : nextCompetitionMatchLinks) {
                if (competitionMatchNextLink.nextCompetitionMatch.compareTo(competitionMatch) == 0) {
                    competitionMatchLinksToRemove.add(competitionMatchNextLink);
                }
            }
            nextCompetitionMatchLinks.removeAll(competitionMatchLinksToRemove);
        }

    }

    public ParticipantSeat getParticipantSeatForParticipant(Participant participant) {
        if (participantPairing != null && participantPairing.participantSeats != null)
            for (ParticipantSeat participantSeat : participantPairing.participantSeats)
                if (participantSeat.participant.compareTo(participant) == 0)
                    return participantSeat;
        return null;
    }

    public void addParticipantsForLadder(List<Participant> participants) {
        if (participantPairing == null)
            participantPairing = ParticipantPairing.createInstance(participants.size(), this.competitionInstance);
//        if (!this.competitionPhase.competitionCreationParamPhase.registrationOnTheFly) {
//            this.competitionInstance.participantPairings.remove(participantPairing);
//            participantPairing.setCompetitionInstance(null);
//        }
        for (Participant participant : participants) {
            this.participantPairing.addParticipant(participant);
        }
        this.participantPairing.setCompetitionRound(this.competitionRound);
        this.participantPairing.setCompetitionMatch(this);
        this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
        if (this.isInitialized()) {

            this.fillCompetitionMatchLink();
            this.competitionRound.fillCompetitionRoundLink();
            this.competitionGroup.fillCompetitionRoundsSequence();

            try {
                this.open();
            } catch (CompetitionInstanceGeneratorException e) {

            }
            this.participantPairing.removeParticipantTeamVoid();
            Sets.sort(competitionPlays);
            for (CompetitionPlay competitionPlay : getCompetitionPlays()) {
                competitionPlay.initializePlay();
                competitionPlay.open();
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        this.competitionInstance.competitionMatches.remove(this);
    }


}


