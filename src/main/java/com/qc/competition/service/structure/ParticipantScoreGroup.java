package com.qc.competition.service.structure;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 15/02/2015.
 */
@XmlType(name = "ParticipantScoreGroup")
public class ParticipantScoreGroup extends ParticipantScore<CompetitionGroup> {
    public static String ACTIVE_GROUP = "ACTIVE_GROUP";
    public static String LAST_ACTIVE_ROUND = "LAST_ACTIVE_ROUND";
    public static String LAST_ACTIVE_ROUND_RANK = "LAST_ACTIVE_ROUND_RANK";
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
    public static List<String> SCORES = new ArrayList<>();


    static {
        SCORES.add(RANK);
        SCORES.add(LAST_ACTIVE_ROUND);
        SCORES.add(LAST_ACTIVE_ROUND_RANK);
        SCORES.add(SCORE_POINTS);
        SCORES.add(SCORE_BYE);
        SCORES.add(ACTIVE_GROUP);
        SCORES.add(SCORE_WIN);
        SCORES.add(SCORE_DRAW);
        SCORES.add(SCORE_LOSS);
        SCORES.add(SCORE_POINTS);
        SCORES.add(SCORE_BAD_POINTS);
        SCORES.add(SCORE_GOAL);
        SCORES.add(SCORE_TIMELAST);
        SCORES.add(SCORE_TIMERUN);

    }

    public ParticipantScoreGroup() {
    }

    public ParticipantScoreGroup(CompetitionGroup competitionGroup) {
        super(competitionGroup);
        int priority = 1;

        ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = RANK;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValue.priority = priority++;
//        participantScoreValue.value = null;
        participantScoreValues.add(participantScoreValue);


        participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = LAST_ACTIVE_ROUND;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        participantScoreValue.priority = priority++;
        participantScoreValues.add(participantScoreValue);

        participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = LAST_ACTIVE_ROUND_RANK;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValue.priority = priority++;
        participantScoreValues.add(participantScoreValue);

        ParticipantScoreValue participantScoreValueWin = new ParticipantScoreValue();
        participantScoreValueWin.name = SCORE_WIN;
        participantScoreValueWin.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        participantScoreValueWin.priority = priority++;
        participantScoreValues.add(participantScoreValueWin);
        ParticipantScoreValue participantScoreValueLoss = new ParticipantScoreValue();
        participantScoreValueLoss.name = SCORE_LOSS;
        participantScoreValueLoss.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValueLoss.priority = priority++;
        participantScoreValues.add(participantScoreValueLoss);
        ParticipantScoreValue participantScoreValueDraw = new ParticipantScoreValue();
        participantScoreValueDraw.name = SCORE_DRAW;
        participantScoreValueDraw.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValueDraw.priority = priority++;
        participantScoreValues.add(participantScoreValueDraw);

        participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = SCORE_POINTS;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        participantScoreValue.priority = priority++;
        participantScoreValues.add(participantScoreValue);

        participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = SCORE_BYE;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValue.priority = priority++;
        participantScoreValues.add(participantScoreValue);

        participantScoreValue = new ParticipantScoreValue();
        participantScoreValue.name = ACTIVE_GROUP;
        participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
//        participantScoreValue.value = null;
        participantScoreValue.priority = priority++;
        participantScoreValues.add(participantScoreValue);

    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        if (competitionObjectWithResultId != null)
//            this.competitionObjectWithResult = competitionInstance.findCompetitionGroup(competitionObjectWithResultId);
        super.initFromXmlInput(competitionInstance);

    }

//    @Override
//    public CompetitionObjectWithResult initCompetitionObjectWithResultCache() {
//        return this.getCompetitionInstance().getCompetitionGroup(competitionObjectWithResultId);
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