package com.qc.competition.service;

import com.qc.competition.service.template.CompetitionComputationParam;
import org.testng.annotations.Test;

/**
 * Created by Duncan on 01/02/2016.
 */
@Test(threadPoolSize = 10)
public abstract class CompetitionDetailsUpdateTest1v1 extends CompetitionDetailsUpdateTest {

    @Test
    public void createAndLaunchCompetition_Fixed() {
        int participantSize = 59;
        int durationInHour = 6;
////        59 16
////                59 18
////                59 23
////                67 18
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 16, 10, true);
    }

    @Test
    public void createAndLaunchCompetition_500_6() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 500, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_2_1() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 2, 1, false);
    }


    @Test
    public void createAndLaunchCompetition_512_6_NoMixing() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixing, 512, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_256_6_NoMixing() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoMixing, 256, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_500_2() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 500, 2, false);
    }

    @Test
    public void createAndLaunchCompetition_64_6() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 64, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_9_6() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 9, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_3_6() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 3, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_5_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 5, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_4_1_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 4, 1, false);
    }


    @Test
    public void createAndLaunchCompetition_5_1_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 5, 1, false);
    }

    @Test
    public void createAndLaunchCompetition_3_1_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 3, 1, false);
    }


    @Test
    public void createAndLaunchCompetition_6_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 6, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_5_6_SingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 5, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_5_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 5, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_9_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 9, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_10_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 10, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_8_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 8, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_17_6_Swiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 17, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_18_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 18, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_20_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 20, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_20_6_NoQualification() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoQualification, 20, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_4_6_NoQualification() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.NoQualification, 4, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_32_6_DoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 32, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseSimpleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 16, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_18_2_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 18, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_2_2_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 2, 2, true);
    }


    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 16, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_5_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 5, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseTripleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, 16, 12, false);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseQuadrupleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, 16, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 16, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_16_6_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 16, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseTripleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, 32, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseQuadrupleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, 32, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 32, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_32_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 32, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseTripleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, 30, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseQuadrupleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, 30, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 30, 6, true);
    }


    @Test
    public void createAndLaunchCompetition_40_2_FinalPhaseDoubleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, 40, 2, true);
    }

    @Test
    public void createAndLaunchCompetition_8_2_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 8, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_9_2_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 9, 2, false);
    }


    @Test
    public void createAndLaunchCompetition_22_6_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 22, 6, false);
    }

    @Override
    protected boolean isChangedWinner() {
        return false;
    }

    @Test
    public void createAndLaunchCompetition_30_6_FinalPhaseSingleElimination() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, 30, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_10_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 10, 6, true);
    }

    @Test
    public void createAndLaunchCompetition_14_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 14, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_18_6_FinalPhaseRoundRobin() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, 18, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_8_6_FinalPhaseSwiss() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, 8, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_33_12_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 33, 12, true);
    }

    @Test
    public void createAndLaunchCompetition_40_12_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 40, 12, true);
    }

    @Test
    public void createAndLaunchCompetition_64_10_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 64, 10, false);
    }


    @Test
    public void createAndLaunchCompetition_99_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 99, 6, false);
    }


    @Test
    public void createAndLaunchCompetition_3_6_AllMagic() {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, 3, 6, false);
    }


    @Override
    @Test(dataProvider = "computationParameterFormatManualProvider")
    public void createAndLaunchCompetitionForSizeAndDurationManual(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Override
    @Test(dataProvider = "computationParameterFormatQuickProvider")
    public void createAndLaunchCompetitionForSizeAndDurationQuick(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }

    @Override
    @Test(dataProvider = "createAndLaunchCompetitionParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationFull(TestGenerationConfiguration testGenerationConfiguration, Integer participantSize, Integer durationInHour, boolean doSaveAndReload) {
        super.createAndLaunchCompetitionForSizeAndDuration(testGenerationConfiguration, participantSize, durationInHour, doSaveAndReload);
    }


    @Test
    public void createAndLaunchCompetitionForSizeAndDurationQuickSimple() {
        Integer participantSize = 8;
        Integer durationInHour = 4;
        boolean doSaveAndReload = true;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, doSaveAndReload);
    }


    @Test
    public void createAndLaunchCompetitionForSizeAndDurationFinalPhaseSingleEliminationQuickSimple() {
        Integer participantSize = 17;
        Integer durationInHour = 6;
        boolean doSaveAndReload = false;
        super.createAndLaunchCompetitionForSizeAndDurationFinalPhaseSingleElimination(participantSize, durationInHour, doSaveAndReload);
    }


    @Test
    public void createAndLaunchCompetitionForSizeAndDurationQuickSimpleWithSaveAndReload() {
        Integer participantSize = 17;
        Integer durationInHour = 6;
        boolean doSaveAndReload = true;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, doSaveAndReload);
    }

    @Test
    public void createAndLaunchCompetitionForSizeAndDurationQuickSimpleWithSaveAndReload_1_play_par_match() {
        Integer participantSize = 17;
        Integer durationInHour = 3;
        boolean doSaveAndReload = true;
        int minPlay = 1;
        int maxPlay = 1;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, minPlay, maxPlay, doSaveAndReload);
    }

    @Test
    public void createAndLaunchCompetitionForSizeAndDurationQuickSimpleWithSaveAndReload_3_play_par_match() {
        Integer participantSize = 17;
        Integer durationInHour = 3;
        boolean doSaveAndReload = true;
        int minPlay = 3;
        int maxPlay = 3;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, minPlay, maxPlay, doSaveAndReload);
    }


    @Test
    public void createAndLaunchCompetition_SpecificPhase357() {
        Integer participantSize = 63;
        Integer durationInHour = 12;
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.numberOfParticipantCompetition = participantSize;
        competitionComputationParam.checkAndCorrectValues();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 5;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 7;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 7;
        boolean doSaveAndReload = true;
        super.createAndLaunchCompetitionWithPhaseParameters(durationInHour, participantSize, competitionComputationParam.getMixingPhaseParameter(), competitionComputationParam.getQualificationPhaseParameter(), competitionComputationParam.getFinalPhaseParameter(), doSaveAndReload);
    }


    @Test
    public void createAndLaunchCompetition_SpecificPhase1357() {
        Integer participantSize = 63;
        Integer durationInHour = 6;
        CompetitionComputationParam competitionComputationParam = getCompetitionComputationParamBase();
        competitionComputationParam.numberOfParticipantCompetition = participantSize;
        competitionComputationParam.checkAndCorrectValues();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 5;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 7;
        boolean doSaveAndReload = true;
        super.createAndLaunchCompetitionWithPhaseParameters(durationInHour, participantSize, competitionComputationParam.getMixingPhaseParameter(), competitionComputationParam.getQualificationPhaseParameter(), competitionComputationParam.getFinalPhaseParameter(), doSaveAndReload);

    }


    @Test
    public void createAndLaunchCompetition_29_6_FinalPhases() {
        int participantSize = 29;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, 6, false);
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, participantSize, 6, false);
    }

    @Test
    public void createAndLaunchCompetition_12_6_FinalPhases() {
        int participantSize = 12;
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, 6, false);
    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationFinalPhaseRoundRobin(Integer participantSize, Integer durationInHour) {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, durationInHour, false);
    }

    @Test(dataProvider = "computationParameterFormatProvider")
    public void createAndLaunchCompetitionForSizeAndDurationAllMagic(Integer participantSize, Integer durationInHour) {
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

    @Test(dataProvider = "computationParameterFormatProviderStupa")
    public void createAndLaunchCompetitionForSizeAndDurationStupa(Integer participantSize, Integer durationInHour) {
        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseQuadrupleElimination, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseRoundRobin, participantSize, durationInHour, true);
//        super.createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSwiss, participantSize, durationInHour, true);
    }


    protected CompetitionComputationParam getCompetitionComputationParamBase() {
        CompetitionComputationParam competitionComputationParam = super.getCompetitionComputationParamBase();
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMinimum = 1;
        competitionComputationParam.getMixingPhaseParameter().numberOfPlayMaximum = 3;
        competitionComputationParam.getMixingPhaseParameter().averagePlayDuration = "PT10M";
        competitionComputationParam.getMixingPhaseParameter().maximumPlayDuration = "PT10M";
        competitionComputationParam.getMixingPhaseParameter().minimumPlayDuration = "PT10M";
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMinimum = 3;
        competitionComputationParam.getQualificationPhaseParameter().numberOfPlayMaximum = 5;
        competitionComputationParam.getQualificationPhaseParameter().averagePlayDuration = "PT10M";
        competitionComputationParam.getQualificationPhaseParameter().maximumPlayDuration = "PT10M";
        competitionComputationParam.getQualificationPhaseParameter().minimumPlayDuration = "PT10M";
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMinimum = 5;
        competitionComputationParam.getFinalPhaseParameter().numberOfPlayMaximum = 9;
        competitionComputationParam.getFinalPhaseParameter().averagePlayDuration = "PT10M";
        competitionComputationParam.getFinalPhaseParameter().maximumPlayDuration = "PT10M";
        competitionComputationParam.getFinalPhaseParameter().minimumPlayDuration = "PT10M";
//        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
//        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
//        competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.DOUBLE_ELIMINATION);
        return competitionComputationParam;
    }

    public void createAndLaunchCompetitionForSizeAndDuration_10_6_FinalPhaseDoubleElimination() {
        Integer participantSize = 10, durationInHour = 6;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseDoubleElimination, participantSize, durationInHour, false);
    }

    public void createAndLaunchCompetitionForSizeAndDuration_10_6_FinalPhaseTripleElimination() {
        Integer participantSize = 10, durationInHour = 6;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseTripleElimination, participantSize, durationInHour, false);
    }


    public void createAndLaunchCompetitionForSizeAndDuration_2_6_AllMagic() {
        Integer participantSize = 2, durationInHour = 6;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }


    public void createAndLaunchCompetitionForSizeAndDuration_2_6_FinalPhaseSingleElimination() {
        Integer participantSize = 2, durationInHour = 6;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.FinalPhaseSingleElimination, participantSize, durationInHour, false);
    }


    public void createAndLaunchCompetitionForSizeAndDuration_128_10_AllMagic() {
        Integer participantSize = 128, durationInHour = 10;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

    public void createAndLaunchCompetitionForSizeAndDuration_512_10_AllMagic() {
        Integer participantSize = 512, durationInHour = 10;
        createAndLaunchCompetitionForSizeAndDuration(TestGenerationConfiguration.AllMagic, participantSize, durationInHour, false);
    }

}
