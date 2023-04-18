package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.template.CompetitionCreationParamPhase;
import com.qc.competition.service.template.ScoringConfigurationPlayElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 15/02/2015.
 */

@XmlType(name = "ParticipantScorePlay")
public class ParticipantScorePlay extends ParticipantScore<CompetitionPlay> {
    public static String SCORE_POINTS = ParticipantScoreType.POINTS.name();
    public static String RANK = ParticipantScoreType.RANK.name();
    public static String SCORE_GOAL = ParticipantScoreType.GOAL.name();
    public static String SCORE_TIMELAST = ParticipantScoreType.TIMELAST.name();
    public static String SCORE_TIMERUN = ParticipantScoreType.TIMERUN.name();
    public static String SCORE_BAD_POINTS = ParticipantScoreType.BAD_POINTS.name();
    public static String SCORE_BAD_POINTS_OPPONENTS = ParticipantScoreType.BAD_POINTS_OPPONENTS.name();
    public static String SCORE_GOAL_OPPONENTS = ParticipantScoreType.GOAL_OPPONENTS.name();
    public static String SCORE_POINTS_OPPONENTS = ParticipantScoreType.POINTS_OPPONENTS.name();
    public static List<String> SCORES = new ArrayList<>();

    static {
        SCORES.add(RANK);
        SCORES.add(SCORE_POINTS);
        SCORES.add(SCORE_BAD_POINTS);
        SCORES.add(SCORE_GOAL);
        SCORES.add(SCORE_TIMELAST);
        SCORES.add(SCORE_TIMERUN);
        SCORES.add(SCORE_GOAL_OPPONENTS);
        SCORES.add(SCORE_POINTS_OPPONENTS);
        SCORES.add(SCORE_BAD_POINTS_OPPONENTS);

    }

    @XmlElementWrapper(name = "scores")
    @XmlElement(name = "score")
    @JsonProperty("scores")
    public List<ParticipantScore> participantScoresSubNotTransient = new ArrayList<>();

    public ParticipantScorePlay() {
    }

    public ParticipantScorePlay(CompetitionPlay competitionPlay) {
        super(competitionPlay);
        int priority = 1;

        CompetitionCreationParamPhase competitionCreationParamPhase = competitionPlay.competitionSeed.competitionPhase.competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionPlay.competitionSeed.competitionPhase.competitionCreationParamPhase;

        if (competitionCreationParamPhase.scoringConfiguration != null && competitionCreationParamPhase.scoringConfiguration.scoringConfigurationPlay != null && competitionCreationParamPhase.scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null && !competitionCreationParamPhase.scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.isEmpty()) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement : competitionCreationParamPhase.scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                ParticipantScoreValue participantScoreValueCurrent = getParticipantScoreValue(scoringConfigurationPlayElement.participantScoreType.name());
                if (participantScoreValueCurrent == null) {
                    ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
                    participantScoreValue.name = scoringConfigurationPlayElement.participantScoreType.name();
                    participantScoreValue.scoreScale = scoringConfigurationPlayElement.scoreScaleType;
                    participantScoreValue.userInput = scoringConfigurationPlayElement.userInput;
                    participantScoreValue.priority = scoringConfigurationPlayElement.priority;
                    participantScoreValues.add(participantScoreValue);
                } else {
                    participantScoreValueCurrent.scoreScale = scoringConfigurationPlayElement.scoreScaleType;
                    participantScoreValueCurrent.userInput = scoringConfigurationPlayElement.userInput;
                    participantScoreValueCurrent.priority = scoringConfigurationPlayElement.priority;
                }
                if (scoringConfigurationPlayElement.priority != null && priority < scoringConfigurationPlayElement.priority)
                    priority = scoringConfigurationPlayElement.priority + 1;
            }
        }
        if (getParticipantScoreValue(RANK) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = RANK;
            participantScoreValue.value = null;
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
        if (getParticipantScoreValue(SCORE_BAD_POINTS) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_BAD_POINTS;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_GOAL) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_GOAL;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_TIMELAST) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_TIMELAST;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }
        if (getParticipantScoreValue(SCORE_TIMERUN) == null) {
            ParticipantScoreValue participantScoreValue = new ParticipantScoreValue();
            participantScoreValue.name = SCORE_TIMERUN;
            participantScoreValue.scoreScale = ScoreScaleType.ABSOLUTE_INTEGER;
            participantScoreValue.priority = priority++;
            participantScoreValues.add(participantScoreValue);
        }

    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
//        if (competitionObjectWithResultId != null)
//            this.competitionObjectWithResult = competitionInstance.getCompetitionPlay(competitionObjectWithResultId);
        super.initFromXmlInput(competitionInstance);
        participantScoresSub.addAll(participantScoresSubNotTransient);
    }

    @Override
    public void initForXmlOutput() {
//        participantScoresSubNotTransient.addAll(participantScoresSub);
//        super.initForXmlOutput();
//        participantScoresSubIds.clear();
    }

//    @Override
//    public CompetitionObjectWithResult initCompetitionObjectWithResultCache() {
//        return this.getCompetitionInstance().getCompetitionPlay(competitionObjectWithResultId);
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
