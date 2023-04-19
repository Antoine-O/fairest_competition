package com.qc.competition.service;

import org.testng.annotations.Test;

/**
 * Created by Duncan on 21/01/2016.
 */
public class CompetitionDetailsGeneratorAllMatchTest extends CompetitionDetailsGeneratorTest {
    @Override
    public boolean isChangedWinner() {
        return true;
    }

    @Override
    public void testComputationSameResult() {
        super.testComputationSameResult();
    }

    @Override
    public void testComputationRankingParticipantEliminationSimple(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantEliminationSimple(numberOfParticipantCompetition, hourDuration);
    }

    @Test
    public void testComputationRankingParticipantEliminationDouble5() {
        super.testComputationRankingParticipantEliminationDouble(5, 6);
    }


    @Test
    public void testComputationRankingParticipantEliminationDouble20() {
        super.testComputationRankingParticipantEliminationDouble(20, 6);
    }


    @Override
    public void testComputationRankingParticipantEliminationDouble(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantEliminationDouble(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantEliminationTriple(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantEliminationTriple(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantSwiss(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantSwiss(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantRoundRobin(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantRoundRobin(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantRoundRobin1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantRoundRobin1V1V1V1_1_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantRoundRobin1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantRoundRobin1V1V1V1_2_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantElimination1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantElimination1V1V1V1_1_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantElimination1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantElimination1V1V1V1_2_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantSwiss1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantSwiss1V1V1V1_1_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipantSwiss1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipantSwiss1V1V1V1_2_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipant1V1V1V1_1_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipant1V1V1V1_1_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testComputationRankingParticipant1V1V1V1_2_qualified(int numberOfParticipantCompetition, int hourDuration) {
        super.testComputationRankingParticipant1V1V1V1_2_qualified(numberOfParticipantCompetition, hourDuration);
    }

    @Override
    public void testMariokart8(int numberOfParticipantCompetition, int maximumNumberOfParallelPlay, int durationInHour) {
        super.testMariokart8(numberOfParticipantCompetition, maximumNumberOfParallelPlay, durationInHour);
    }

    @Override
    public void testHearthStone(int numberOfParticipantCompetition, int maximumNumberOfParallelPlay, int durationInHour) {
        super.testHearthStone(numberOfParticipantCompetition, maximumNumberOfParallelPlay, durationInHour);
    }
}
