package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.*;
import org.goochjs.glicko2.Rating;
import org.goochjs.glicko2.RatingCalculator;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by Duncan on 15/02/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ParticipantScoreCompetition.class, name = "ScoreCompetition"),
        @JsonSubTypes.Type(value = ParticipantScoreSeed.class, name = "ScoreSeed"),
        @JsonSubTypes.Type(value = ParticipantScorePhase.class, name = "ScorePhase"),
        @JsonSubTypes.Type(value = ParticipantScoreGroup.class, name = "ScoreGroup"),
        @JsonSubTypes.Type(value = ParticipantScoreRound.class, name = "ScoreRound"),
        @JsonSubTypes.Type(value = ParticipantScoreMatch.class, name = "ScoreMatch"),
        @JsonSubTypes.Type(value = ParticipantScorePlay.class, name = "ScorePlay"),

})
@XmlSeeAlso({ParticipantScoreCompetition.class,
        ParticipantScoreSeed.class,
        ParticipantScorePhase.class,
        ParticipantScoreMatch.class,
        ParticipantScoreGroup.class,
        ParticipantScorePlay.class,
        ParticipantScoreRound.class})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class ParticipantScore<T extends CompetitionObjectWithResult> implements Comparable<ParticipantScore<T>>, Serializable, Simplify<ParticipantScore<T>>, Cloneable {
    public static String CLASS = ParticipantScore.class.getSimpleName();
    public static String GLICKO2_RATING = "GLICKO2_RATING";
    public static String GLICKO2_RATING_DEVIATION = "GLICKO2_RATING_DEVIATION";
    public static String GLICKO2_VOLATILITY = "GLICKO2_VOLATILITY";
    public static String GLICKO2_NUMBER_OF_RESULTS = "GLICKO2_NUMBER_OF_RESULTS";
    //    public static Logger LOGGER = Logger.getLogger(CLASS);
    public String type;
    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;//= IdGenerator.getLocalId(CLASS);
    //    @XmlList
//    @XmlAttribute(name = "subScoresId")
//      @XmlIDREF  @JsonIdentityReference(alwaysAsId=true)
//    @JsonProperty("subScoresId")
    @XmlElementWrapper(name = "subScores")
    @XmlElement(name = "subScore")
    @JsonProperty("subScores")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public List<ParticipantScore> participantScoresSub = new ArrayList<>();
    @XmlElementWrapper(name = "values")
    @XmlElement(name = "value")
    @JsonProperty("values")
    public List<ParticipantScoreValue> participantScoreValues = new ArrayList<>();
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;
    @XmlAttribute(name = "competitionObjectId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionObjectId")
    protected T competitionObjectWithResult;
//    @XmlTransient
//    @JsonIgnore
//    protected boolean competitionObjectWithResultInitialized = false;
//    @XmlElementWrapper(name = "subScoresId")
//    @XmlElement(name = "subScoreId")

//    List<ParticipantScore> participantSubScores= new ArrayList<>();
//    @XmlAttribute(name = "competitionObjectId")
//    String competitionObjectWithResultId = null;

    public ParticipantScore() {
        super();
//        LOGGER.finest("(" + localId + ")");
    }

    public ParticipantScore(T competitionObjectWithResult) {
//        LOGGER.finest("(" + localId + ")" + competitionObjectWithResult.getClass().getSimpleName() + "(" + competitionObjectWithResult.getLocalId() + ")");
//        this.competitionObjectWithResultId = competitionObjectWithResult.getLocalId();
        this.setCompetitionInstance(competitionObjectWithResult.getCompetitionInstance());
        this.competitionObjectWithResult = competitionObjectWithResult;
        this.localId = competitionInstance.getIdGenerator().getId(this.getClass().getSimpleName());
        this.id = competitionInstance.getIdGenerator().getId();


        initGlickoRatings();


        ParticipantScoreRating participantScoreRating = new ParticipantScoreRating();
        updateParticipantScoreRatingFromParticipantScoreRating(participantScoreRating);

    }

    private void initGlickoRatings() {
        int priority = 100;
        ParticipantScoreValue glicko2Rating = this.getParticipantScoreValue(GLICKO2_RATING);
        if (glicko2Rating == null) {

            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = GLICKO2_RATING;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC;
            participantScoreValue.priority = priority;
            participantScoreValues.add(participantScoreValue);
        }
        priority++;
        ParticipantScoreValue glicko2RatingDeviation = this.getParticipantScoreValue(GLICKO2_RATING_DEVIATION);
        if (glicko2RatingDeviation == null) {

            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = GLICKO2_RATING_DEVIATION;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC_REVERSED;
            participantScoreValue.priority = priority;
            participantScoreValues.add(participantScoreValue);
        }
        priority++;
        ParticipantScoreValue glicko2RatingVolatility = this.getParticipantScoreValue(GLICKO2_VOLATILITY);
        if (glicko2RatingVolatility == null) {

            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = GLICKO2_VOLATILITY;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC_REVERSED;
            participantScoreValue.priority = priority;
            participantScoreValues.add(participantScoreValue);
        }
        priority++;
        ParticipantScoreValue glicko2NumberOfResults = this.getParticipantScoreValue(GLICKO2_NUMBER_OF_RESULTS);
        if (glicko2NumberOfResults == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = GLICKO2_NUMBER_OF_RESULTS;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_NUMERIC_REVERSED;
            participantScoreValue.priority = priority;
            participantScoreValues.add(participantScoreValue);
        }

    }

    public static void fillRank(SortedSet<ParticipantResult> participantResults, String rankKey) {
        fillRank(participantResults, rankKey, null, false);
    }

    public static void fillRank(List<ParticipantResult> participantResults, String rankKey, List<String> scores) {
        fillRank(participantResults, rankKey, scores, false);
    }

    public static void fillRank(SortedSet<ParticipantResult> participantResults, String rankKey, boolean rankByGlicko) {
        fillRank(participantResults, rankKey, null, rankByGlicko);
    }

    public static void fillRank(SortedSet<ParticipantResult> participantResults, String rankKey, List<String> scores, boolean rankByGlicko) {
        List<ParticipantResult> participantResultsAsList = new ArrayList<>(participantResults);
        fillRank(participantResultsAsList, rankKey, scores, rankByGlicko);
        participantResults.clear();
        participantResults.addAll(participantResultsAsList);
    }

    public static void fillRank(List<ParticipantResult> participantResults, String rankKey, List<String> scores, boolean rankByGlicko) {
        ParticipantScore<CompetitionPlay> participantScorePrevious = null;
        if (rankByGlicko) {
            fillRankByGlicko(participantResults, rankKey);
        } else {
            int rank = 1;
            Collections.sort(participantResults);
            for (ParticipantResult participantResult : participantResults) {
                if (participantResult.participantScore.getParticipantScoreValue(rankKey) != null && participantResult.participantScore.getParticipantScoreValue(rankKey).value != null && participantResult.participantScore.getParticipantScoreValue(rankKey).computeNumberValue().intValue() > 0) {
                    participantResult.rank = participantResult.participantScore.getParticipantScoreValue(rankKey).computeNumberValue().intValue();
                }
            }

            for (ParticipantResult participantResult : participantResults) {
                if (participantResult.rank == null) {
                    if (participantScorePrevious != null && participantScorePrevious.compareScoreTo(participantResult.participantScore, scores) != 0)
                        rank++;
                    participantResult.rank = rank;
                } else {
                    rank = participantResult.rank;
                }
                participantScorePrevious = participantResult.participantScore;
            }
        }
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.rank != null && participantResult.rank > 0) {
                participantResult.participantScore.setParticipantScoreValue(rankKey, participantResult.rank);
            }
        }
        int rank = 1;
        ParticipantResult participantResultPrevious = null;
        for (ParticipantResult participantResult : participantResults) {
            if (participantResultPrevious != null && participantResultPrevious.rank.compareTo(participantResult.rank) != 0)
                rank++;
            participantResult.rank = rank;
            participantResult.participantScore.setParticipantScoreValue(rankKey, rank);
            participantResultPrevious = participantResult;
        }
    }

    private static void fillRankByGlicko(List<ParticipantResult> participantResults, String rankKey) {
        GlickoParticipantResultComparator glickoParticipantResultComparator = new GlickoParticipantResultComparator();
        Collections.sort(participantResults, glickoParticipantResultComparator);
        ParticipantResult participantResultPrevious = null;
        int rank = 1;
        for (ParticipantResult participantResult : participantResults) {
            if (participantResultPrevious != null && glickoParticipantResultComparator.compare(participantResultPrevious, participantResult) != 0)
                rank++;
            participantResult.rank = rank;
            participantResult.participantScore.setParticipantScoreValue(rankKey, rank);
            participantResultPrevious = participantResult;
        }
    }

    public static void fillPoints(SortedSet<ParticipantResult> participantResults, String scorePointsKey) {
//        int points = 0;
//        ParticipantScoreValue participantScoreValue = null;
        for (ParticipantResult participantResult : participantResults) {
            int newPoints = PointsCalculation.getGroupPoints(participantResult.rank, participantResults);
            participantResult.participantScore.setParticipantScoreValue(scorePointsKey, newPoints);
        }

    }

    public void updateParticipantScoreRatingFromPreviousParticipantScoreRatingForParticipant(Participant participant) {
        if (competitionObjectWithResult.getPreviousCompetitionObjectWithResults() != null && !competitionObjectWithResult.getPreviousCompetitionObjectWithResults().isEmpty()) {
            CompetitionObjectWithResult competitionObjectWithResultForRating = (CompetitionObjectWithResult) competitionObjectWithResult.getPreviousCompetitionObjectWithResults().get(0);
            if (competitionObjectWithResult.getPreviousCompetitionObjectWithResults().size() > 1) {
                competitionObjectWithResultForRating = competitionObjectWithResultForRating.getParentCompetitionObjectWithResult();
            }
            ParticipantResult participantResult = null;

            while (participantResult == null && competitionObjectWithResultForRating != null) {
                participantResult = competitionObjectWithResultForRating.getParticipantResultFor(participant, false);
                if (participantResult == null) {
                    competitionObjectWithResultForRating = competitionObjectWithResultForRating.getUpperCompetitionObjectWithResult();
                }
            }
            if (participantResult != null && participantResult.participantScore != null) {
                ParticipantScoreRating participantScoreRating = participantResult.participantScore.getParticipantScoreRating();
                if (participantScoreRating != null)
                    updateParticipantScoreRatingFromParticipantScoreRating(participantScoreRating);
            }
        }
    }

    public void updateParticipantScoreRatingFromParticipantScoreRating(ParticipantScoreRating participantScoreRating) {
        setParticipantScoreValue(GLICKO2_RATING, participantScoreRating.rating);
        setParticipantScoreValue(GLICKO2_RATING_DEVIATION, participantScoreRating.ratingDeviation);
        setParticipantScoreValue(GLICKO2_VOLATILITY, participantScoreRating.volatility);
        setParticipantScoreValue(GLICKO2_NUMBER_OF_RESULTS, participantScoreRating.numberOfResults);

    }

    public ParticipantScoreRating getParticipantScoreRating() {
        initGlickoRatings();
        double rating = this.getParticipantScoreValue(GLICKO2_RATING).computeNumberValue().doubleValue();
        double ratingDeviation = this.getParticipantScoreValue(GLICKO2_RATING_DEVIATION).computeNumberValue().doubleValue();
        double volatility = this.getParticipantScoreValue(GLICKO2_VOLATILITY).computeNumberValue().doubleValue();
        int numberOfResults = this.getParticipantScoreValue(GLICKO2_NUMBER_OF_RESULTS).computeNumberValue().intValue();
        ParticipantScoreRating participantScoreRating = new ParticipantScoreRating(rating, ratingDeviation, volatility, numberOfResults);
        return participantScoreRating;
    }


//    abstract public CompetitionObjectWithResult initCompetitionObjectWithResultCache();

    public void add(ParticipantScore participantScore) {
        add(participantScore, 1);
    }

    public void add(ParticipantScore participantScore, int multiplier) {
        ParticipantScoreValue participantScoreValueToAdd = null;
        for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
            participantScoreValueToAdd = participantScore.getParticipantScoreValue(participantScoreValue.name);
            if (participantScoreValueToAdd != null)
                participantScoreValue.value = participantScoreValue.scoreScale.getValueFor(participantScoreValue.computeNumberValue().doubleValue() + participantScoreValueToAdd.computeNumberValue().doubleValue() * multiplier).toString();
        }
    }

    public void clear() {
        for (ParticipantScoreValue participantScoreValue : participantScoreValues)
            participantScoreValue.clear();
    }

    public int compareTo(ParticipantScore<T> o) {
        int compareValue = 0;
        if (this.id != null && o.id != null) {
            compareValue = this.id.compareTo(o.id);
        }
        if (compareValue != 0) {
            if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {
                if (this.localId != null && o.localId != null) {
                    compareValue = this.localId.compareTo(o.localId);
                }
                if (compareValue != 0) {
                    for (String SCORE : getSCORES()) {
                        ParticipantScoreValue participantScoreValueLocal = getParticipantScoreValue(SCORE);
                        ParticipantScoreValue participantScoreValueOther = o.getParticipantScoreValue(SCORE);
                        if (participantScoreValueLocal != null && participantScoreValueOther != null) {
                            compareValue = participantScoreValueLocal.compareTo(participantScoreValueOther);
                            if (compareValue != 0)
                                break;
                        }
                    }
//        for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//        }
                    if (compareValue == 0 && participantScoresSub != null && o.participantScoresSub != null) {
//            int i = 0;
                        for (int i = 0; i < participantScoresSub.size(); i++) {
                            if (o.participantScoresSub.size() > i) {
                                compareValue = participantScoresSub.get(i).compareTo(o.participantScoresSub.get(i));
                                if (compareValue != 0)
                                    break;
                            }
                        }
                    }

                    if (compareValue == 0 && participantScoresSub != null && o.participantScoresSub != null) {
                        if (o.participantScoresSub.size() > participantScoresSub.size()) {
                            compareValue = -1;
                        }
                    }
                    if (compareValue == 0) {
                        compareValue = this.id.compareTo(o.id);
                    }
                }
            }
        }
        return compareValue;
    }


    public int compareScoreTo(ParticipantScore<T> o) {
        return compareScoreTo(o, getSCORES());
    }

    public int compareScoreTo(ParticipantScore<T> o, List<String> scores) {
        if (scores == null)
            scores = getSCORES();
        int compareValue = 0;
//        if (this.id != null && o.id != null) {
//            compareValue = this.id.compareTo(o.id);
//        }
//        if (compareValue != 0) {
        if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {
            if (this.localId != null && o.localId != null) {
                compareValue = this.localId.compareTo(o.localId);
            }
            if (compareValue != 0) {
                for (String SCORE : scores) {
                    if (SCORE != null) {
                        ParticipantScoreValue participantScoreValueLocal = getParticipantScoreValue(SCORE);
                        ParticipantScoreValue participantScoreValueOther = o.getParticipantScoreValue(SCORE);
                        if (participantScoreValueLocal != null && participantScoreValueOther != null) {
                            compareValue = participantScoreValueLocal.compareTo(participantScoreValueOther);
                            if (compareValue != 0)
                                break;
                        }
                    }
                }
//        for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//        }
                if (compareValue == 0 && participantScoresSub != null && o.participantScoresSub != null) {
//            int i = 0;
                    for (int i = 0; i < participantScoresSub.size(); i++) {
                        if (o.participantScoresSub.size() > i) {
                            compareValue = participantScoresSub.get(i).compareScoreTo(o.participantScoresSub.get(i));
                            if (compareValue != 0)
                                break;
                        }
                    }
                }

                if (compareValue == 0 && participantScoresSub != null && o.participantScoresSub != null) {
                    if (o.participantScoresSub.size() > participantScoresSub.size()) {
                        compareValue = -1;
                    }
                }
//                    if (compareValue == 0) {
//                        compareValue = this.id.compareTo(o.id);
//                    }
            }
//            }
        }
        return compareValue;
    }

    public ParticipantScoreValue getParticipantScoreValue(String name) {
        ParticipantScoreValue participantScoreValue = null;
        for (ParticipantScoreValue participantScoreValueElt : participantScoreValues) {
            if (participantScoreValueElt.name.compareToIgnoreCase(name) == 0) {
                participantScoreValue = participantScoreValueElt;
                break;
            }
        }
        return participantScoreValue;
    }

    public Rating getGlickoRating(Participant participant, RatingCalculator ratingCalculator) {
        Rating rating = new Rating(participant.localId, ratingCalculator);
        rating.setVolatility(this.getParticipantScoreValue(ParticipantScore.GLICKO2_VOLATILITY).computeNumberValue().doubleValue());
        rating.setRatingDeviation(this.getParticipantScoreValue(ParticipantScore.GLICKO2_RATING_DEVIATION).computeNumberValue().doubleValue());
        rating.setRating(this.getParticipantScoreValue(ParticipantScore.GLICKO2_RATING).computeNumberValue().doubleValue());
        rating.incrementNumberOfResults(this.getParticipantScoreValue(ParticipantScore.GLICKO2_NUMBER_OF_RESULTS).computeNumberValue().intValue());
        return rating;
    }

    public void setParticipantScoreValue(String name, Number value) {
        setParticipantScoreValue(name, value.toString());
    }

    public void setParticipantScoreValue(String name, String value) {
        if (name != null) {
            ParticipantScoreValue participantScoreValue = getParticipantScoreValue(name);
            if (participantScoreValue != null)
                participantScoreValue.value = value;
            else
                throw new IllegalArgumentException("Score with '" + name + "' not found");
        }
    }

    public void resetParticipantScoreValue(String name) {
        ParticipantScoreValue participantScoreValue = getParticipantScoreValue(name);
        if (participantScoreValue != null)
            participantScoreValue.value = null;
    }

//    public String toDescription() {
//        return toString() + System.lineSeparator();
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//            description.append(participantScoreValue.toDescriptionTree(level + 1));
//        }
//        if (!participantScoresSub.isEmpty()) {
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                description.append(participantScoreSub.toDescriptionTree(level + 2));
//            }
//        }
//        return description.toString();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        if (!participantScoreValues.isEmpty()) {
//            Element elementParticipantScoreValues = document.createElement(ParticipantScoreValue.class.getSimpleName() + "s");
//            for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//                elementParticipantScoreValues.appendChild(participantScoreValue.toDescriptionXml(document));
//            }
//            element.appendChild(elementParticipantScoreValues);
//        }
//        if (!participantScoresSub.isEmpty()) {
//            Element elementParticipantScoreValues = document.createElement(ParticipantScore.class.getSimpleName() + "s");
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                elementParticipantScoreValues.appendChild(participantScoreSub.toDescriptionXml(document));
//            }
//            element.appendChild(elementParticipantScoreValues);
//        }
//
//        return element;
//    }
//
//    public String toDetailedDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(this.toString()).append("\t");
//        if (!participantScoreValues.isEmpty()) {
//            for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//                description.append("\t").append(participantScoreValue.toString());
//            }
//        }
//        if (!participantScoresSub.isEmpty()) {
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                description.append("\t").append(participantScoreSub.toDetailedDescription());
//            }
//        }
//        return description.toString();
//    }
//
//    public String toSimpleDescription() {
//        StringBuilder description = new StringBuilder();
//        if (!participantScoreValues.isEmpty()) {
//            for (ParticipantScoreValue participantScoreValue : participantScoreValues) {
//                description.append("\t").append(participantScoreValue.toString());
//            }
//        }
//        if (!participantScoresSub.isEmpty()) {
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                description.append("\t").append(participantScoreSub.toSimpleDescription());
//            }
//        }
//        return description.toString();
//    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "[" + this.id + "/" + this.localId + "]" + (this.participantScoreValues != null ? participantScoreValues.toString() : "") + "}";
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        this.setCompetitionInstance(competitionInstance);
        /*ParticipantScore participantScore = null;
        participantScoresSub.clear();
        if (!participantScoresSubIds.isEmpty()) {
            for (Integer participantScoreSubId : participantScoresSubIds) {
                participantScore = competitionInstance.findParticipantScore(participantScoreSubId);
                LOGGER.finest("participantScoreSubId:" + participantScoreSubId);
                if (participantScore != null) {
                    participantScore.initFromXmlInput(competitionInstance);
                    participantScoresSub.add(participantScore);
                } else {
                    LOGGER.warning("participantScoreSubId:" + participantScoreSubId + " is not found");
                }
            }
            participantScoresSubIds.clear();
        }*/
//        competitionObjectWithResultId = null;
    }

    public void initForXmlOutput() {
//        participantSubScores.clear();
//        if (!participantScoresSub.isEmpty()) {
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                participantSubScoresSubIds.add(participantScoreSub.localId);
//            }
//        }

    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        if (this.localId.compareTo(localId) == 0)
            participantScore = this;
//        if (participantScore == null)
//            for (ParticipantScore participantScoreSub : participantScoresSub) {
//                if (participantScoreSub.localId.compareTo(localId) == 0) {
//                    participantScore = participantScoreSub;
//                    break;
//                }
//            }
        return participantScore;
    }

    @Override
    public ParticipantScore<T> cloneSimplified() {
        ParticipantScore<T> participantScore = null;
        try {
            participantScore = (ParticipantScore<T>) this.clone();
        } catch (CloneNotSupportedException e) {
        }
        return participantScore;
    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    public CompetitionObjectWithResult getCompetitionObjectWithResult() {
//        if (!competitionObjectWithResultInitialized) {
//            initCompetitionObjectWithResultCache();
//        }
        return competitionObjectWithResult;
    }

    public abstract List<String> getSCORES();

    public abstract ParticipantScoreValue getParticipantScoreValuePoints();

    public abstract void removeRank();

    public ParticipantScore<T> cloneForContext(T competitionObjectWithResult) throws CloneNotSupportedException, InstantiationException, IllegalAccessException {
        ParticipantScore<T> participantScore = (ParticipantScore<T>) this.clone();
        participantScore.participantScoresSub = null;
//        if (participantScore.participantScoreValues != null) {
//            List<ParticipantScoreValue> participantScoreValuesToRemove = new ArrayList<>();
//            for (ParticipantScoreValue participantScoreValue : participantScore.participantScoreValues) {
//                if (participantScoreValue.value == null) {
//                    participantScoreValuesToRemove.add(participantScoreValue);
//                }
//            }
//            participantScore.participantScoreValues.remove(participantScoreValuesToRemove);
//        }

        if (competitionObjectWithResult != null && this.competitionObjectWithResult != null && competitionObjectWithResult.getClass().getName().compareTo(this.competitionObjectWithResult.getClass().getName()) == 0
                && this.id != null && competitionObjectWithResult.id != null && this.id.compareTo(competitionObjectWithResult.id) == 0
                && this.participantScoresSub != null && !this.participantScoresSub.isEmpty()) {
            this.participantScoresSub = new ArrayList<>();
            for (ParticipantScore participantScoreSub : this.participantScoresSub) {
                this.participantScoresSub.add(participantScoreSub.cloneForContext());
            }
        }
        return participantScore;
    }

    private ParticipantScore cloneForContext() throws IllegalAccessException, InstantiationException {
        ParticipantScore participantScore = this.getClass().newInstance();
        participantScore.id = this.id;
        participantScore.competitionObjectWithResult = this.competitionObjectWithResult.getClass().newInstance();
        participantScore.competitionObjectWithResult.id = this.competitionObjectWithResult.id;
        participantScore.competitionObjectWithResult.localId = this.competitionObjectWithResult.localId;
        return participantScore;
    }
}
