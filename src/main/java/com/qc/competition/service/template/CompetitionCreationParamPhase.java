package com.qc.competition.service.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qc.competition.service.structure.*;
import com.qc.competition.utils.Sets;

import javax.xml.bind.annotation.*;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 26/05/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CompetitionCreationParamPhaseFinal.class,
        CompetitionCreationParamPhaseMixing.class,
        CompetitionCreationParamPhaseQualification.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompetitionCreationParamPhaseFinal.class, name = "CompetitionCreationParamPhaseFinal"),
        @JsonSubTypes.Type(value = CompetitionCreationParamPhaseMixing.class, name = "CompetitionCreationParamPhaseMixing"),
        @JsonSubTypes.Type(value = CompetitionCreationParamPhaseQualification.class, name = "CompetitionCreationParamPhaseQualification")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class CompetitionCreationParamPhase implements Comparable<CompetitionCreationParamPhase> {
    //    public static final int MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN = 32;
    public String type;
    @XmlAttribute
    public PlayVersusType playVersusType;
    @XmlAttribute
    public ParticipantType participantType;
    @XmlAttribute
    @JsonDeserialize(using = JsonBooleanDeserialiser.class)
    @JsonSerialize(using = JsonBooleanSerializer.class)
    public Boolean participantTypeSplittable;
    @XmlAttribute
    public Integer numberOfParticipantMatch;
    @XmlAttribute
    public Integer participantQualifiedPerMatch;
    @XmlAttribute
    @XmlList
    public SortedSet<TournamentFormat> tournamentFormatsAccepted = new TreeSet<>();
    @XmlAttribute
    public Integer phaseIndex;
    @XmlAttribute
    public Integer numberOfPlayMinimum;
    @XmlAttribute
    public Integer numberOfPlayMaximum;
    @XmlAttribute
    @JsonDeserialize(using = JsonBooleanDeserialiser.class)
    @JsonSerialize(using = JsonBooleanSerializer.class)
    public Boolean allowEvenNumberOfPlay;
    @XmlAttribute
    public Integer maximumNumberOfParallelPlay;
    @XmlAttribute
    public String intermissionDuration;
    @XmlAttribute
    public String averagePlayDuration;
    @XmlAttribute
    public String maximumPlayDuration;
    @XmlAttribute
    public String minimumPlayDuration;
    @XmlAttribute
    public String phaseDuration;
    @XmlElement
    public ScoringConfiguration scoringConfiguration;
    @XmlAttribute
    @JsonDeserialize(using = JsonBooleanDeserialiser.class)
    @JsonSerialize(using = JsonBooleanSerializer.class)
    public Boolean registrationOnTheFly;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "phaseIndex=" + phaseIndex +
                ", tournamentFormatsAccepted=" + tournamentFormatsAccepted +
                ", numberOfPlayMinimum=" + numberOfPlayMinimum +
                ", numberOfPlayMaximum=" + numberOfPlayMaximum +
                '}';
    }

    @Override
    public int compareTo(CompetitionCreationParamPhase competitionCreationParamPhase) {
        this.checkAndCorrectValues();
        competitionCreationParamPhase.checkAndCorrectValues();
        int compare = 0;
        String thisClassName = this.getClass().getSimpleName();
        String oClassName = competitionCreationParamPhase.getClass().getSimpleName();
        compare = thisClassName.compareTo(oClassName);
        if (compare != 0) {
            if (thisClassName.compareToIgnoreCase(CompetitionCreationParamPhaseMixing.class.getSimpleName()) == 0)
                compare = -1;
            else if (oClassName.compareToIgnoreCase(CompetitionCreationParamPhaseFinal.class.getSimpleName()) == 0)
                compare = -1;
            else
                compare = 1;
        } else {
            compare = this.phaseIndex.compareTo(competitionCreationParamPhase.phaseIndex);
        }


        if (compare == 0)
            compare = numberOfParticipantMatch.compareTo(competitionCreationParamPhase.numberOfParticipantMatch);
        if (compare == 0)
            compare = playVersusType.compareTo(competitionCreationParamPhase.playVersusType);

        if (compare == 0)
            compare = participantQualifiedPerMatch.compareTo(competitionCreationParamPhase.participantQualifiedPerMatch);

        if (compare == 0)
            compare = maximumNumberOfParallelPlay.compareTo(competitionCreationParamPhase.maximumNumberOfParallelPlay);

        if (compare == 0)
            compare = numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum);
        if (compare == 0)
            compare = numberOfPlayMaximum.compareTo(competitionCreationParamPhase.numberOfPlayMaximum);
        if (compare == 0)
            compare = minimumPlayDuration.compareTo(competitionCreationParamPhase.minimumPlayDuration);
        if (compare == 0)
            compare = maximumPlayDuration.compareTo(competitionCreationParamPhase.maximumPlayDuration);


        if (compare == 0 && tournamentFormatsAccepted != null && competitionCreationParamPhase.tournamentFormatsAccepted != null)
            compare = tournamentFormatsAccepted.containsAll(competitionCreationParamPhase.tournamentFormatsAccepted) ?
                    0
                    : (tournamentFormatsAccepted.size() != competitionCreationParamPhase.tournamentFormatsAccepted.size() ?
                    Integer.compare(tournamentFormatsAccepted.size(), competitionCreationParamPhase.tournamentFormatsAccepted.size())
                    : Sets.sort(tournamentFormatsAccepted).toString().compareTo(Sets.sort(competitionCreationParamPhase.tournamentFormatsAccepted).toString()))
                    ;
        if (compare == 0)
            compare = intermissionDuration.compareTo(competitionCreationParamPhase.intermissionDuration);


        if (compare == 0)
            compare = averagePlayDuration.compareTo(competitionCreationParamPhase.averagePlayDuration);


        return compare;
    }

    public void checkAndCorrectValues() {
        if (participantQualifiedPerMatch == null || participantQualifiedPerMatch == 0) {
            if (numberOfParticipantMatch == 1)
                participantQualifiedPerMatch = 1;
            else if (numberOfParticipantMatch % 2 == 0)
                participantQualifiedPerMatch = numberOfParticipantMatch / 2;
            else if (numberOfParticipantMatch % 2 == 1 && numberOfParticipantMatch > 1)
                participantQualifiedPerMatch = numberOfParticipantMatch - 1 / 2;
            else
                participantQualifiedPerMatch = 1;
        }
        if (isCompetitionMatchRankingScoreBased()) {
            numberOfPlayMinimum = 1;
            numberOfPlayMaximum = 1;
        } else {
            if (numberOfPlayMinimum == null) {
                numberOfPlayMinimum = 1;
            }
            if (numberOfPlayMaximum == null) {
                numberOfPlayMaximum = numberOfPlayMinimum;
            }
            if (allowEvenNumberOfPlay == null)
                allowEvenNumberOfPlay = false;
        }
        if (participantTypeSplittable == null)
            participantTypeSplittable = true;
        if (maximumNumberOfParallelPlay == null)
            maximumNumberOfParallelPlay = 0;
        if (intermissionDuration == null)
            intermissionDuration = Duration.ofMinutes(2).toString();
        if (scoringConfiguration == null)
            scoringConfiguration = new ScoringConfiguration();
        if (scoringConfiguration != null)
            this.scoringConfiguration.checkAndCorrectValues();

    }

    public void copyFrom(CompetitionCreationParamPhase competitionCreationParamPhase) {
        this.numberOfPlayMaximum = competitionCreationParamPhase.numberOfPlayMaximum;
        this.playVersusType = competitionCreationParamPhase.playVersusType;
        this.participantType = competitionCreationParamPhase.participantType;
        this.numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
        this.participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;
        this.tournamentFormatsAccepted = new TreeSet<>();
        this.tournamentFormatsAccepted.addAll(competitionCreationParamPhase.tournamentFormatsAccepted);
        this.phaseIndex = competitionCreationParamPhase.phaseIndex;
        this.numberOfPlayMinimum = competitionCreationParamPhase.numberOfPlayMinimum;
        this.numberOfPlayMaximum = competitionCreationParamPhase.numberOfPlayMaximum;
        this.allowEvenNumberOfPlay = competitionCreationParamPhase.allowEvenNumberOfPlay;
        this.maximumNumberOfParallelPlay = competitionCreationParamPhase.maximumNumberOfParallelPlay;
        this.intermissionDuration = competitionCreationParamPhase.intermissionDuration;
        this.averagePlayDuration = competitionCreationParamPhase.averagePlayDuration;
        this.maximumPlayDuration = competitionCreationParamPhase.maximumPlayDuration;
        this.minimumPlayDuration = competitionCreationParamPhase.minimumPlayDuration;
        this.registrationOnTheFly = competitionCreationParamPhase.registrationOnTheFly;
        this.phaseDuration = competitionCreationParamPhase.phaseDuration;
        this.participantTypeSplittable = competitionCreationParamPhase.participantTypeSplittable;
        try {
            if (competitionCreationParamPhase.scoringConfiguration != null)
                this.scoringConfiguration = (ScoringConfiguration) competitionCreationParamPhase.scoringConfiguration.clone();
        } catch (CloneNotSupportedException e) {

        }
    }

    abstract public CompetitionCreationParamPhase cloneCompetitionCreationParamPhase();

    public SortedSet<CompetitionCreationParamPhase> createCompetitionCreationParamPhaseListToTry() {
        SortedSet<CompetitionCreationParamPhase> competitionCreationParamPhaseListToTry = new TreeSet<>();
        if (numberOfPlayMinimum.compareTo(numberOfPlayMaximum) == 0) {
            competitionCreationParamPhaseListToTry.add(this.cloneCompetitionCreationParamPhase());
        } else {
            for (int i = numberOfPlayMinimum; i <= numberOfPlayMaximum; i = i + (allowEvenNumberOfPlay ? 1 : 2)) {
                for (int j = i; j <= numberOfPlayMaximum; j = j + (allowEvenNumberOfPlay ? 1 : 2)) {
                    CompetitionCreationParamPhase competitionCreationParamPhase = this.cloneCompetitionCreationParamPhase();
                    competitionCreationParamPhase.numberOfPlayMinimum = i;
                    competitionCreationParamPhase.numberOfPlayMaximum = j;
                    competitionCreationParamPhase.checkAndCorrectValues();
                    competitionCreationParamPhaseListToTry.add(competitionCreationParamPhase);
                }
            }
        }
        return competitionCreationParamPhaseListToTry;

    }

    public Duration getAveragePlayDuration() {
        return Duration.fromString(averagePlayDuration);
    }

    public Duration getMinimumPlayDuration() {
        return Duration.fromString(minimumPlayDuration);
    }

    public Duration getMaximumPlayDuration() {
        return Duration.fromString(maximumPlayDuration);
    }

    public Duration getPhaseDuration() {
        return Duration.fromString(phaseDuration);
    }

    public boolean greaterPlayQuantityWithSameFormat(CompetitionCreationParamPhase competitionCreationParamPhase) {
        boolean result = false;
        String tournamentFormatsAcceptedString = Sets.sort(tournamentFormatsAccepted).toString();
        String tournamentFormatsAcceptedStringOther = Sets.sort(competitionCreationParamPhase.tournamentFormatsAccepted).toString();
        if (tournamentFormatsAccepted != null && competitionCreationParamPhase.tournamentFormatsAccepted != null && tournamentFormatsAcceptedString.compareTo(tournamentFormatsAcceptedStringOther) == 0) {
            if (numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) >= 0) {
                result = true;
            }
        }
        return result;
    }

    public boolean greaterPlayQuantity(CompetitionCreationParamPhase competitionCreationParamPhase) {
        boolean result = false;
        if (numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) > 0 && numberOfPlayMaximum.compareTo(competitionCreationParamPhase.numberOfPlayMaximum) > 0) {
            result = true;
        }
        return result;
    }

    public boolean isCompetitionMatchRankingScoreBased() {
        boolean isCompetitionMatchRankingScoreBased = false;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationMatch != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy instanceof RankingComputationPolicyScore && !scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.isEmpty()) {
            RankingComputationPolicyScore rankingComputationPolicyScore = (RankingComputationPolicyScore) scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy;
            if (rankingComputationPolicyScore.scoreThresholdType != null && rankingComputationPolicyScore.scoreThresholdType.compareTo(ScoreThresholdType.NONE) != 0) {
//                ParticipantScoreAggregateType participantScoreAggregateType1 = scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.iterator().next().participantScoreAggregateType;
//                if (participantScoreAggregateType1.isSumOfUserInput()) {
                isCompetitionMatchRankingScoreBased = true;
//                }
            }
        }
        return isCompetitionMatchRankingScoreBased;
    }


    public boolean isCompetitionPlayScoreMode() {
        boolean isCompetitionPlayScoreMode = false;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                if (scoringConfigurationPlayElement.userInput) {
                    isCompetitionPlayScoreMode = true;
                }
            }
        }
        return isCompetitionPlayScoreMode;
    }

    public ParticipantScoreType getPlayParticipantScoreType() {
        ParticipantScoreType participantScoreType = null;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                if (scoringConfigurationPlayElement.userInput) {
                    participantScoreType = scoringConfigurationPlayElement.participantScoreType;
                }
            }


        }
        return participantScoreType;
    }

    public String getPlayParticipantScoreName() {
        String participantScoreName = null;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                if (scoringConfigurationPlayElement.userInput) {
                    participantScoreName = scoringConfigurationPlayElement.name;
                }
            }


        }
        return participantScoreName;
    }


    public ScoreScaleType getPlayParticipantScoreScaleType() {
        ScoreScaleType playParticipantScoreScaleType = null;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                if (scoringConfigurationPlayElement.userInput) {
                    playParticipantScoreScaleType = scoringConfigurationPlayElement.scoreScaleType;
                }
            }


        }
        return playParticipantScoreScaleType;
    }


    public String getCompetitionMatchRankingScoreThreshold() {
        String competitionMatchRankingScoreThreshold = null;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationMatch != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy instanceof RankingComputationPolicyScore) {
            competitionMatchRankingScoreThreshold = ((RankingComputationPolicyScore) scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy).threshold;
        }
        return competitionMatchRankingScoreThreshold;
    }


    public ScoreThresholdType getCompetitionMatchRankingScoreThresholdType() {
        ScoreThresholdType scoreThresholdType = null;
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationMatch != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy != null
                && scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy instanceof RankingComputationPolicyScore) {
            scoreThresholdType = ((RankingComputationPolicyScore) scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy).scoreThresholdType;
        }
        return scoreThresholdType;
    }

    public CompetitionCreationParamPhase diff(CompetitionCreationParamPhase competitionCreationParamPhase) {
        CompetitionCreationParamPhase competitionCreationParamPhaseResult = null;
        if (this.numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) != 0 || this.numberOfPlayMinimum.compareTo(competitionCreationParamPhase.numberOfPlayMinimum) != 0) {
            competitionCreationParamPhaseResult = this.cloneCompetitionCreationParamPhase();
        }
        return competitionCreationParamPhaseResult;
    }
}
