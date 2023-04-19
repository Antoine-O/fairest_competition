package com.qc.competition.service.structure;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 15/02/2015.
 */

@XmlType(name = "ParticipantScoreRound")
public class ParticipantScoreRound extends ParticipantScore<CompetitionRound> {
    public static String RANK = ParticipantScoreAggregateType.RANK.name();
    public static String SCORE_DRAW = ParticipantScoreAggregateType.DRAW.name();
    public static String SCORE_LOSS = ParticipantScoreAggregateType.LOSS.name();
    public static String SCORE_POINTS = ParticipantScoreAggregateType.POINTS.name();
    public static String SCORE_CUMULATIVE_POINTS = ParticipantScoreAggregateType.CUMULATIVE_POINTS.name();
    public static String SCORE_WIN = ParticipantScoreAggregateType.WIN.name();
    public static String SCORE_BYE = ParticipantScoreAggregateType.BYE.name();
    public static String SCORE_GOAL = ParticipantScoreAggregateType.GOAL.name();
    public static String SCORE_TIMELAST = ParticipantScoreAggregateType.TIMELAST.name();
    public static String SCORE_TIMERUN = ParticipantScoreAggregateType.TIMERUN.name();
    public static String SCORE_BAD_POINTS = ParticipantScoreAggregateType.BAD_POINTS.name();
    public static List<String> SCORES = new ArrayList<>();

    static {
        SCORES.add(RANK);
        SCORES.add(SCORE_WIN);
        SCORES.add(SCORE_LOSS);
        SCORES.add(SCORE_DRAW);
        SCORES.add(SCORE_BYE);
        SCORES.add(SCORE_POINTS);
        SCORES.add(SCORE_BAD_POINTS);
        SCORES.add(SCORE_GOAL);
        SCORES.add(SCORE_TIMELAST);
        SCORES.add(SCORE_TIMERUN);
        SCORES.add(SCORE_CUMULATIVE_POINTS);
    }

    public ParticipantScoreRound() {
    }

    public ParticipantScoreRound(CompetitionRound competitionGroupRound) {
        super(competitionGroupRound);
        int priority = 1;

        ParticipantScoreValue participantScoreValueRank = new ParticipantScoreValue();
        participantScoreValueRank.name = RANK;
        participantScoreValueRank.value = null;
        participantScoreValueRank.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValueRank.priority = priority++;
        participantScoreValues.add(participantScoreValueRank);
        ParticipantScoreValue participantScoreValuePoints = new ParticipantScoreValue();
        participantScoreValuePoints.name = SCORE_POINTS;
        participantScoreValuePoints.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        participantScoreValuePoints.priority = priority++;
        participantScoreValues.add(participantScoreValuePoints);
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
        ParticipantScoreValue participantScoreValueBye = new ParticipantScoreValue();
        participantScoreValueBye.name = SCORE_BYE;
        participantScoreValueBye.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
        participantScoreValueBye.priority = priority++;
        participantScoreValues.add(participantScoreValueBye);
        ParticipantScoreValue participantScoreValueCumulativePoints = new ParticipantScoreValue();
        participantScoreValueCumulativePoints.name = SCORE_CUMULATIVE_POINTS;
        participantScoreValueCumulativePoints.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        participantScoreValueCumulativePoints.priority = priority++;
        participantScoreValues.add(participantScoreValueCumulativePoints);
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        if (competitionObjectWithResultId != null)
//            this.competitionObjectWithResult = competitionInstance.getCompetitionRound(competitionObjectWithResultId);
        super.initFromXmlInput(competitionInstance);
    }

//    @Override
//    public CompetitionObjectWithResult initCompetitionObjectWithResultCache() {
//        return this.getCompetitionInstance().getCompetitionRound(competitionObjectWithResultId);
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

