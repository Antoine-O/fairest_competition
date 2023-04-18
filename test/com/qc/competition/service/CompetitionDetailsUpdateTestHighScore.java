package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.ParticipantScoreAggregateType;
import com.qc.competition.service.structure.ParticipantScoreType;
import com.qc.competition.service.structure.ScoreScaleType;
import com.qc.competition.service.template.*;
import org.testng.annotations.Test;

import java.util.TreeSet;

/**
 * Created by Duncan on 01/02/2016.
 */
@Test(threadPoolSize = 10)
public class CompetitionDetailsUpdateTestHighScore extends CompetitionDetailsUpdateTest {

    @Test
    public void createAndLaunchCompetition_32() {
        int participantSize = 32;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

    @Test
    public void createAndLaunchCompetition_8_DoubleElimination() {
        int participantSize = 8;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, durationInHour, false);
    }

    @Test
    public void createAndLaunchCompetition_6_DoubleElimination() {
        int participantSize = 6;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, durationInHour, false);
    }


    @Test
    public void createAndLaunchCompetition_7_DoubleElimination() {
        int participantSize = 7;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, durationInHour, false);
    }

    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDurationQuick(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        ScoringConfigurationPlayElement scoringConfigurationPlayElement = new ScoringConfigurationPlayElement();
        scoringConfigurationPlayElement.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        scoringConfigurationPlayElement.name = ParticipantScoreType.GOAL.name();
        scoringConfigurationPlayElement.participantScoreType = ParticipantScoreType.GOAL;
        scoringConfigurationPlayElement.priority = 1;
        scoringConfigurationPlayElement.userInput = true;
        ScoringConfigurationPlayElement scoringConfigurationPlayElement2 = new ScoringConfigurationPlayElement();
        scoringConfigurationPlayElement2.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER;
        scoringConfigurationPlayElement2.name = ParticipantScoreType.GOAL_OPPONENTS.name();
        scoringConfigurationPlayElement2.participantScoreType = ParticipantScoreType.GOAL_OPPONENTS;
        scoringConfigurationPlayElement2.priority = 2;

        ScoringConfigurationMatchElement scoringConfigurationMatchElement = new ScoringConfigurationMatchElement();
        scoringConfigurationMatchElement.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER_REVERSED;
        scoringConfigurationMatchElement.participantScoreAggregateType = ParticipantScoreAggregateType.GOAL;
        scoringConfigurationMatchElement.priority = 1;

        ScoringConfigurationMatchElement scoringConfigurationMatchElement2 = new ScoringConfigurationMatchElement();
        scoringConfigurationMatchElement2.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER;
        scoringConfigurationMatchElement2.participantScoreAggregateType = ParticipantScoreAggregateType.GOAL_OPPONENTS;
        scoringConfigurationMatchElement2.priority = 2;

        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 2;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 2;
        competitionComputationParam.getMixingPhaseParameter().numberOfParticipantMatch = 1;
        competitionComputationParam.getMixingPhaseParameter().playVersusType = PlayVersusType.ONE_SINGLE_PLAYER;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getMixingPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getMixingPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getMixingPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.LADDER);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);

        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 2;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 2;
        competitionComputationParam.getQualificationPhaseParameter().numberOfParticipantMatch = 1;
        competitionComputationParam.getQualificationPhaseParameter().playVersusType = PlayVersusType.ONE_SINGLE_PLAYER;
        competitionComputationParam.getQualificationPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);

        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 2;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 2;
        competitionComputationParam.getFinalPhaseParameter().numberOfParticipantMatch = 1;
        competitionComputationParam.getFinalPhaseParameter().playVersusType = PlayVersusType.ONE_SINGLE_PLAYER;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 2;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 2;
        competitionComputationParam.getFinalPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = java.time.Duration.ofMinutes(5).toString();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);

        return competitionComputationParam;


    }


    @Test
    public void createAndLaunchCompetition_2_SingleElimination() {
        int participantSize = 2;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoQualification, participantSize, durationInHour, false);
    }


}
