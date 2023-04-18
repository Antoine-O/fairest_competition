package com.qc.competition.service;

import com.qc.competition.db.entity.game.MergePolicy;
import com.qc.competition.db.entity.game.ResetPolicy;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
@Test(threadPoolSize = 10)
public class CompetitionDetailsUpdateTest1v1_without_merge extends CompetitionDetailsUpdateTest {


    @Test
    public void createAndLaunchCompetition_8_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, false);
    }

    @Override
    protected boolean isChangedWinner() {
        return true;
    }

    @Override
    protected MergePolicy getMergePolicy() {
        return MergePolicy.NO_MERGE;
    }

    @Override
    protected ResetPolicy getResetPolicy() {
        return ResetPolicy.NONE;
    }
}
