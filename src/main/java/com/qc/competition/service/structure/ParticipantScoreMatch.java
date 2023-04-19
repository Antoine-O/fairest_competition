package com.qc.competition.service.structure;

import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.ScoringConfigurationMatchElement;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 15/02/2015.
 */

@XmlType(name = "ParticipantScoreMatch")
public class ParticipantScoreMatch extends ParticipantScore<CompetitionMatch> {
    public static String RANK = ParticipantScoreAggregateType.RANK.name();
    public static String SCORE_DRAW = ParticipantScoreAggregateType.DRAW.name();
    public static String SCORE_LOSS = ParticipantScoreAggregateType.LOSS.name();
    public static String SCORE_POINTS = ParticipantScoreAggregateType.POINTS.name();
    public static String SCORE_WIN = ParticipantScoreAggregateType.WIN.name();
    public static String SCORE_BYE = ParticipantScoreAggregateType.BYE.name();
    public static String SCORE_GOAL = ParticipantScoreAggregateType.GOAL.name();
    public static String SCORE_TIMELAST = ParticipantScoreAggregateType.TIMELAST.name();
    public static String SCORE_TIMERUN = ParticipantScoreAggregateType.TIMERUN.name();
    public static String SCORE_BAD_POINTS = ParticipantScoreAggregateType.BAD_POINTS.name();
    public static String SCORE_GOAL_OPPONENTS = ParticipantScoreAggregateType.GOAL_OPPONENTS.name();
    public static String SCORE_POINTS_OPPONENTS = ParticipantScoreAggregateType.POINTS_OPPONENTS.name();
    public static String SCORE_BAD_POINTS_OPPONENTS = ParticipantScoreAggregateType.BAD_POINTS_OPPONENTS.name();
    public static List<String> SCORES = new ArrayList<>();

    static {
        SCORES.add(RANK);
        SCORES.add(SCORE_WIN);
        SCORES.add(SCORE_DRAW);
        SCORES.add(SCORE_LOSS);
        SCORES.add(SCORE_BYE);
        SCORES.add(SCORE_POINTS);
        SCORES.add(SCORE_BAD_POINTS);
        SCORES.add(SCORE_GOAL);
        SCORES.add(SCORE_TIMELAST);
        SCORES.add(SCORE_TIMERUN);
        SCORES.add(SCORE_GOAL_OPPONENTS);
        SCORES.add(SCORE_POINTS_OPPONENTS);
        SCORES.add(SCORE_BAD_POINTS_OPPONENTS);
    }

    public ParticipantScoreMatch() {
    }

    public ParticipantScoreMatch(CompetitionMatch competitionMatch) {
        super(competitionMatch);
        int priority = 1;
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionMatch.competitionSeed.competitionPhase.competitionCreationParamPhase;
//        if (competitionCreationParamPhase == null)
//            competitionCreationParamPhase = competitionMatch.competitionSeed.competitionPhase.competitionCreationParamPhase;

        if (competitionCreationParamPhase.scoringConfiguration != null && competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch != null && competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements != null && !competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.isEmpty()) {
            for (ScoringConfigurationMatchElement scoringConfigurationMatchElement : competitionCreationParamPhase.scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements) {
                if (getParticipantScoreValue(scoringConfigurationMatchElement.participantScoreAggregateType.name()) == null) {
                    ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
                    participantScoreValue.name = scoringConfigurationMatchElement.participantScoreAggregateType.name();
                    participantScoreValue.scoreScale = scoringConfigurationMatchElement.scoreScaleType;
                    participantScoreValue.priority = scoringConfigurationMatchElement.priority;
                    if (priority <= participantScoreValue.priority)
                        priority = participantScoreValue.priority + 1;
                    participantScoreValues.add(participantScoreValue);
                }
            }
        }


        if (getParticipantScoreValue(RANK) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = RANK;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.value = null;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_WIN) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_WIN;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_LOSS) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_LOSS;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_DRAW) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_DRAW;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_BYE) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_BYE;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_POINTS) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_POINTS;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }

    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        if (competitionObjectWithResultId != null)
//            this.competitionObjectWithResult = competitionInstance.getCompetitionMatch(competitionObjectWithResultId);
        super.initFromXmlInput(competitionInstance);
    }

//    @Override
//    public CompetitionObjectWithResult initCompetitionObjectWithResultCache() {
//        return this.getCompetitionInstance().getCompetitionMatch(competitionObjectWithResultId);
//    }

    @Override
    public List<String> getSCORES() {
        return SCORES;
    }

    @Override
    public void removeRank() {
        this.resetParticipantScoreValue(RANK);
    }

    @Override
    public ParticipantScoreValue getParticipantScoreValuePoints() {
        return getParticipantScoreValue(SCORE_POINTS);
    }
}
