package com.qc.competition.service;

import com.qc.competition.service.structure.MergePolicy;
import com.qc.competition.service.structure.ResetPolicy;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
@Test(threadPoolSize = 10)
public class CompetitionDetailsUpdateTest1v1_with_reset extends CompetitionDetailsUpdateTest {


    @Test
    public void createAndLaunchCompetition_8_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_10_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 10, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_8_6_RoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 8, 6, 1, 1, false);
    }

    protected Integer getMaximumNumberOfParallelPlay() {
        return 1;
    }

    @Test
    public void createAndLaunchCompetition_10_2_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 10, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_4_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, false);
    }

    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    @Override
    protected MergePolicy getMergePolicy() {
        return super.getMergePolicy();
    }

    @Override
    protected ResetPolicy getResetPolicy() {
        return ResetPolicy.WINNER_OF_LOOSER_WINS;
    }
}
