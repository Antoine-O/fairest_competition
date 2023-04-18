package com.qc.competition.service;

import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.ParticipantScoreAggregateType;
import com.qc.competition.service.structure.ParticipantScoreType;
import com.qc.competition.service.structure.ScoreScaleType;
import com.qc.competition.service.template.*;
import com.qc.competition.ws.simplestructure.Duration;
import org.testng.annotations.Test;

import java.util.TreeSet;

public class CompetitionDetailsUpdateTest11v11_scoringmode extends CompetitionDetailsUpdateTest1v1 {
    @Override
    public void createAndLaunchCompetition_10_6_DoubleElimination() {
        super.createAndLaunchCompetition_10_6_DoubleElimination();
    }


    @Override
    protected boolean isChangedWinner() {
        return true;
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
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT12M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT12M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT12M";
        competitionComputationParam.getMixingPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);
        competitionComputationParam.removeMixingPhase();

        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 2;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 2;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT12M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT12M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT12M";
        competitionComputationParam.getQualificationPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getQualificationPhaseParameter().tournamentFormatsAccepted = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);
        competitionComputationParam.getQualificationPhaseParameter().groupSizeMinimum = 4;
        competitionComputationParam.getQualificationPhaseParameter().groupSizeMaximum = 4;

        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 2;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 2;
        competitionComputationParam.getFinalPhaseParameter().allowEvenNumberOfPlay = true;
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.SINGLE_ELIMINATION);
        competitionComputationParam.getFinalPhaseParameter().groupSizeMinimum = 2;
        competitionComputationParam.getFinalPhaseParameter().groupSizeMaximum = 16;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT12M";
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT12M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT12M";
        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalThreshold = 4;
        competitionComputationParam.getFinalPhaseParameter().groupSizeFinalEnabled = true;
        competitionComputationParam.getFinalPhaseParameter().numberOfParallelPlayFinalMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMinimum = 1;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayFinalMaximum = 1;
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration = new ScoringConfiguration();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay = new ScoringConfigurationPlay();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements.add(scoringConfigurationPlayElement2);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement2);

        return competitionComputationParam;

    }


    public void createAndLaunchCompetition_50_fifa() {
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.numberOfParticipantCompetition = 50;
        competitionComputationParam.competitionDuration = Duration.ofHours(12);
        competitionComputationParam.checkAndCorrectValues();
        super.createAndLaunchCompetitionWithParam(competitionComputationParam, false);
    }


    public void createAndLaunchCompetition_2_6_SimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 2, 6, true);
    }


    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

}

