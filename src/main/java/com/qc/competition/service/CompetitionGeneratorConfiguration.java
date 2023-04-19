package com.qc.competition.service;

import com.qc.competition.service.structure.*;
import com.qc.competition.service.template.*;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceGeneratorImpl;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 04/01/2015.
 */

public class CompetitionGeneratorConfiguration {

    protected final static String OUTPUT_PATH = System.getenv("outputPath") != null ? System.getenv("outputPath") : System.getProperty("java.io.tmpdir");
    protected static final Integer REGISTRATION_PRICE_IN_CENTS = 500;
    private static final int MAX_NUMBER_OF_PARTICIPANT = 100;
    private static final int MAX_NUMBER_OF_PARTICIPANT_QUICK_TEST = 100;
    private static final int MAX_NUMBER_OF_PARTICIPANT_STEP = 1;
    private static final int MAX_NUMBER_OF_PARALLEL_PLAYS = 20;
    private static final int MAX_NUMBER_OF_PARALLEL_PLAYS_STEP = 1;
    private static final int MAX_HOUR_DURATION = 24;
    private static final int MAX_HOUR_DURATION_STEP = 1;
    public static Duration DEFAULT_COMPETITION_DURATION_FOR_TEST = Duration.ofHours(6);
    public static int DEFAULT_NULMBER_OF_PARTICIPANT_COMPETITION = 64;
    private static final DecimalFormat decimalFormat = new DecimalFormat("###");
    private static final Logger LOGGER = Logger.getLogger(CompetitionGeneratorConfiguration.class.getName());

    public static CompetitionComputationParam getDefaultCompetitionComputationParamStatic(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration, MergePolicy mergePolicy, ResetPolicy resetPolicy, Boolean thirdPlaceMatchEnabled) {
        return new CompetitionGeneratorConfiguration().getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, minimumPlayDuration, averagePlayDuration, maximumPlayDuration, mergePolicy, resetPolicy, thirdPlaceMatchEnabled);
    }

    //    boolean changedWinner = false;
    //
    public CompetitionComputationParam getDefaultCompetitionComputationParam(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration) {
        return this.getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, minimumPlayDuration, averagePlayDuration, maximumPlayDuration, getMergePolicy(), getResetPolicy(), getThirdPlaceMatchEnabled());
    }

    public Boolean getThirdPlaceMatchEnabled() {
        return Boolean.FALSE;
    }

    protected MergePolicy getMergePolicy() {
        return MergePolicy.STANDARD;
    }

    protected ResetPolicy getResetPolicy() {
        return ResetPolicy.NONE;
    }

    public CompetitionComputationParam getDefaultCompetitionComputationParam(Integer numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch, Duration minimumPlayDuration, Duration averagePlayDuration, Duration maximumPlayDuration, MergePolicy mergePolicy, ResetPolicy resetPolicy, Boolean thirdPlaceMatchEnabled) {
        CompetitionComputationParam competitionComputationParam = new CompetitionComputationParam();
        competitionComputationParam.competitionDuration = competitionDuration;
        competitionComputationParam.numberOfParticipantCompetition = numberOfParticipantCompetition;
        competitionComputationParam.sharerPercentageLimit = 10;
        competitionComputationParam.participantType = participantType;


        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = new CompetitionCreationParamPhaseFinal();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());
        competitionCreationParamPhaseFinal.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseFinal.participantType = participantType;
        competitionCreationParamPhaseFinal.playVersusType = playVersusType;
        competitionCreationParamPhaseFinal.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseFinal.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseFinal.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseFinal.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseFinal.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseFinal.numberOfPlayMinimum = 5;
        competitionCreationParamPhaseFinal.numberOfPlayMaximum = 7;
        competitionCreationParamPhaseFinal.groupSizeMaximum = numberOfParticipantMatch * 8;
        competitionCreationParamPhaseFinal.groupSizeMinimum = participantQualifiedPerMatch + 1;
        competitionCreationParamPhaseFinal.groupSizeFinalEnabled = true;
        competitionCreationParamPhaseFinal.groupSizeFinalThreshold = numberOfParticipantMatch * 2;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum = 7;
        competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum = 9;
        competitionCreationParamPhaseFinal.mergePolicy = mergePolicy;
        competitionCreationParamPhaseFinal.resetPolicy = resetPolicy;
        competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled = getThirdPlaceMatchEnabled();
        competitionCreationParamPhaseFinal.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseFinal.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseFinal.checkAndCorrectValues();

        competitionComputationParam.setFinalPhase(competitionCreationParamPhaseFinal);

        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = new CompetitionCreationParamPhaseQualification();
        competitionCreationParamPhaseQualification.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseQualification.tournamentFormatsAccepted.addAll(TournamentFormat.allButElimination());
        competitionCreationParamPhaseQualification.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseQualification.participantType = participantType;
        competitionCreationParamPhaseQualification.playVersusType = playVersusType;
        competitionCreationParamPhaseQualification.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseQualification.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseQualification.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseQualification.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseQualification.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseQualification.numberOfPlayMinimum = 3;
        competitionCreationParamPhaseQualification.numberOfPlayMaximum = 5;
        competitionCreationParamPhaseQualification.groupSizeMaximum = numberOfParticipantMatch * 4;
        competitionCreationParamPhaseQualification.groupSizeMinimum = numberOfParticipantMatch * 2;
        competitionCreationParamPhaseQualification.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseQualification.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseQualification.checkAndCorrectValues();
        competitionComputationParam.setQualificationPhase(competitionCreationParamPhaseQualification);

        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = new CompetitionCreationParamPhaseMixing();
        competitionCreationParamPhaseMixing.tournamentFormatsAccepted.clear();
        competitionCreationParamPhaseMixing.tournamentFormatsAccepted.addAll(TournamentFormat.allButElimination());
        competitionCreationParamPhaseMixing.numberOfParticipantMatch = numberOfParticipantMatch;
        competitionCreationParamPhaseMixing.participantType = participantType;
        competitionCreationParamPhaseMixing.playVersusType = playVersusType;
        competitionCreationParamPhaseMixing.participantQualifiedPerMatch = participantQualifiedPerMatch;
        competitionCreationParamPhaseMixing.maximumPlayDuration = maximumPlayDuration.durationValue;
        competitionCreationParamPhaseMixing.minimumPlayDuration = minimumPlayDuration.durationValue;
        competitionCreationParamPhaseMixing.averagePlayDuration = averagePlayDuration.durationValue;
        competitionCreationParamPhaseMixing.intermissionDuration = Duration.ofMinutes(2).durationValue;
        competitionCreationParamPhaseMixing.numberOfPlayMinimum = 3;
        competitionCreationParamPhaseMixing.numberOfPlayMaximum = 3;
        competitionCreationParamPhaseMixing.numberOfRoundMinimum = 1;
        competitionCreationParamPhaseMixing.numberOfRoundMaximum = 3;
        competitionCreationParamPhaseMixing.maximumNumberOfParallelPlay = getMaximumNumberOfParallelPlay();
        competitionCreationParamPhaseMixing.registrationOnTheFly = registrationOnTheFly();
        competitionCreationParamPhaseMixing.checkAndCorrectValues();
        competitionComputationParam.setMixingPhase(competitionCreationParamPhaseMixing);

        competitionComputationParam.getFirstPhase().registrationOnTheFly = registrationOnTheFly();
        competitionComputationParam.checkAndCorrectValues();
        return competitionComputationParam;

    }

    protected Integer getMaximumNumberOfParallelPlay() {
        return 0;
    }

    protected boolean isChangedWinner() {
        return false;
    }

    protected boolean registrationOnTheFly() {
        return false;
    }

    protected CompetitionInstance getCompetitionInstance(int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        return getCompetitionInstances(1, numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch).get(0);
    }

    protected List<CompetitionInstance> getCompetitionInstances(int sizeResult, int numberOfParticipantCompetition, Duration competitionDuration, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int participantQualifiedPerMatch) {
        CompetitionComputationParam competitionComputationParam = getDefaultCompetitionComputationParam(numberOfParticipantCompetition, competitionDuration, participantType, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, Duration.ofMinutes(15), Duration.ofMinutes(15), Duration.ofMinutes(15));
        List<CompetitionInstance> competitionInstances = getCompetitionInstances(competitionComputationParam, sizeResult);
        return competitionInstances;
    }

    protected List<CompetitionInstance> getCompetitionInstances(CompetitionComputationParam competitionComputationParam, int sizeResult) {
        CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
        List<CompetitionInstance> competitionInstances = null;
        try {
            competitionInstances = competitionInstanceGeneratorImpl.computeCompetitionFormatFor(competitionComputationParam, sizeResult);
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }
        return competitionInstances;
    }



    protected void writeCartouches(List<CompetitionInstance> competitionInstances) {
        for (CompetitionInstance competitionInstance : competitionInstances) {
            writeCartouche(competitionInstance);
        }
    }

    protected String writeCartouche(CompetitionInstance competitionInstance) {
        Logger logger = LOGGER;
        String logPrefix = "writeCartouche";
        competitionInstance.reset(true);
        competitionInstance.fillWithFakeRegistrations();
        String suffix = null;
        try {
            competitionInstance.initialize();
            competitionInstance.open();
            competitionInstance.launchSimulation(isChangedWinner());
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss");
            suffix = "_" + competitionInstance.localId + "_" + dateFormat.format(new Date());
            String cssContent = Utils.getCssContent();
            String filename = OUTPUT_PATH + "competition_cartouche" + suffix + ".html";
            logger.info(logPrefix + "\t" + filename);
            try {
                competitionInstance.writeCartouche(OUTPUT_PATH + "competition_cartouche" + suffix + ".html", "", cssContent);
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE,logPrefix + "\t" + filename, e);
            }
        } catch (CompetitionInstanceGeneratorException e) {
            e.printStackTrace();
        }

        return suffix;
    }

}


