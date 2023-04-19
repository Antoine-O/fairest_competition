package com.qc.competition.service;


import com.qc.competition.service.structure.*;
import com.qc.competition.service.template.*;
import org.testng.annotations.Test;

import java.util.TreeSet;

/**
 * Created by Duncan on 01/02/2016.
 */
public class CompetitionDetailsUpdateTest1v1_scoring_threshold extends CompetitionDetailsUpdateTest {


    @Override
    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        ParticipantType participantType = ParticipantType.TEAM_ONE_PARTICIPANT;
        int numberOfParticipantMatch = 2;
        competitionComputationParam.sharerPercentageLimit = 10;
        PlayVersusType playVersusType = PlayVersusType.ONE_VS_ONE;
        int participantQualifiedPerMatch = 1;
        competitionComputationParam = this.getDefaultCompetitionComputationParam(null, null, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30));


        ScoringConfigurationPlayElement scoringConfigurationPlayElement = new ScoringConfigurationPlayElement();
        scoringConfigurationPlayElement.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER;
        scoringConfigurationPlayElement.name = ParticipantScoreType.BAD_POINTS.name();
        scoringConfigurationPlayElement.participantScoreType = ParticipantScoreType.BAD_POINTS;
        scoringConfigurationPlayElement.priority = 1;
        scoringConfigurationPlayElement.userInput = true;


        ScoringConfigurationMatchElement scoringConfigurationMatchElement = new ScoringConfigurationMatchElement();
        scoringConfigurationMatchElement.scoreScaleType = ScoreScaleType.ABSOLUTE_INTEGER;
        scoringConfigurationMatchElement.participantScoreAggregateType = ParticipantScoreAggregateType.BAD_POINTS;
        scoringConfigurationMatchElement.priority = 1;

        RankingComputationPolicy rankingComputationPolicyMatch = new RankingComputationPolicyScore();
        ((RankingComputationPolicyScore) rankingComputationPolicyMatch).qualifiedGapScore = "0";
        ((RankingComputationPolicyScore) rankingComputationPolicyMatch).winnerGapScore = "0";
        ((RankingComputationPolicyScore) rankingComputationPolicyMatch).scoreThresholdType = ScoreThresholdType.HIGH_THRESHOLD;
        ((RankingComputationPolicyScore) rankingComputationPolicyMatch).threshold = "100";
        ((RankingComputationPolicyScore) rankingComputationPolicyMatch).scoreName = ParticipantScoreAggregateType.BAD_POINTS.name();


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
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getMixingPhaseParameter().scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy = rankingComputationPolicyMatch;
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
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getQualificationPhaseParameter().scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy = rankingComputationPolicyMatch;
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
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch = new ScoringConfigurationMatch();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements = new TreeSet<>();
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.scoringConfigurationMatchElements.add(scoringConfigurationMatchElement);
        competitionComputationParam.getFinalPhaseParameter().scoringConfiguration.scoringConfigurationMatch.rankingComputationPolicy = rankingComputationPolicyMatch;


        return competitionComputationParam;
    }


    @Test
    public void createAndLaunchCompetition_2_6_SingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 2, 1, false);
    }


    @Test
    public void createAndLaunchCompetition_8_6_SingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 8, 1, false);
    }


    @Test
    public void createAndLaunchCompetition_64_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 64, 1, false);
    }

    @Override
    protected boolean isChangedWinner() {
        return true;
    }
}
