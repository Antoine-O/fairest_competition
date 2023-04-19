package com.qc.competition.service.template.automatic.participation.optimization;


import com.qc.competition.service.structure.*;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTree;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeGroup;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeMatch;
import com.qc.competition.service.structure.format.CompetitionGroupFormatTreeRound;
import com.qc.competition.service.template.*;
import com.qc.competition.utils.Sets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 21/12/2014.
 */
public class CompetitionInstanceGeneratorImpl implements CompetitionInstanceGenerator {
    public static int QUALIFICATION_PERCENTAGE = 25;
    public static String CLASS = CompetitionInstanceGeneratorImpl.class.getSimpleName();
    public static Logger LOGGER = Logger.getLogger("COMPETITION_COMPUTATION_SERVICE");
    //    public  static Logger LOGGER_createCompetitionInstanceFor =//  logger.getLogger(CLASS + ".createCompetitionInstanceFor");
//    public  static Logger LOGGER_createEmptyPhaseWithoutElimination =//  logger.getLogger(CLASS + ".createEmptyPhaseWithoutElimination");
//    public  static Logger LOGGER_createRandomPhaseWithoutElimination =//  logger.getLogger(CLASS + ".createRandomPhaseWithoutElimination");
//    public  static Logger LOGGER_initCompetitionGroupPhase =//  logger.getLogger(CLASS + ".initCompetitionGroupPhase");
//    //    public  static Logger LOGGER_initEmptyPhase =//  logger.getLogger(CLASS + ".initEmptyPhase");
//    public  static Logger LOGGER_initFinalPhase =//  logger.getLogger(CLASS + ".initFinalPhase");
//    public  static Logger LOGGER_initRoundRobinFinalPhase =//  logger.getLogger(CLASS + ".initRoundRobinFinalPhase");
//    public  static Logger LOGGER_initGroupPhase =//  logger.getLogger(CLASS + ".initGroupPhase");
//    public  static Logger LOGGER_initPreQualificationPhase =//  logger.getLogger(CLASS + ".initMixingPhase");
//    public  static Logger LOGGER_getQuantityOfParallelQualificationGroupPhase =//  logger.getLogger(CLASS + ".getQuantityOfParallelQualificationGroupPhase");
//    public  static Logger LOGGER_mergeCompetitionGroup =//  logger.getLogger(CLASS + ".mergeCompetitionGroup");
//    public  static Logger LOGGER_createCompetitionGroupsFromCompetitionGroupFormatTree =//  logger.getLogger(CLASS + ".createCompetitionGroupsFromCompetitionGroupFormatTree");
    public boolean test;


    @Override
    public CompetitionInstance computeCompetitionFormatFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException {
        int sizeResult = 1;
        List<CompetitionInstance> competitionInstances = computeCompetitionFormatFor(competitionComputationParam, sizeResult);
        CompetitionInstance competitionInstance = competitionInstances.get(0);
//        competitionInstance.fillCompetitionMatchLink();
        competitionInstance.reset(true);
        return competitionInstance;
    }


    @Override
    public List<CompetitionInstance> computeCompetitionFormatFor(CompetitionComputationParam competitionComputationParam, int sizeResult) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_computeCompetitionFormatFor;
//       //  logger.log(Level.FINE, "[START]");
        competitionComputationParam.checkAndCorrectValues();

        List<CompetitionInstance> competitionInstances = new ArrayList<>();
        List<CompetitionInstance> competitionInstancesNew = new ArrayList<>();
//        CompetitionInstance competitionInstance = null;
        CompetitionInstance newCompetitionInstance = null;
        int tryIndex = 0;
        int localSizeResult = sizeResult;
        if (localSizeResult < 1)
            localSizeResult = 1;

        competitionComputationParam.fitToNumberOfParticipantCompetition();

        int maxFinalGroupSizeIndex = 1;
        SortedSet<CompetitionComputationParam> competitionComputationParams = competitionComputationParam.createCompetitionComputationParamsListToTry();

        SortedSet<CompetitionComputationParam> competitionComputationParamSetTried = new TreeSet<>();
        String prefix = "";
        competitionInstances = computeCompetitionFormatFor(competitionComputationParams, competitionComputationParamSetTried, localSizeResult, prefix);

        CompetitionInstance competitionInstance = competitionInstances.get(0);

        java.time.Duration expectedDuration = competitionInstance.expectedRelativeEndTime.duration;
//        java.time.Duration maxDuration = java.time.Duration.ofMinutes(competitionInstance.getExpectedGlobalDuration().max);
        java.time.Duration thresholdExcedeed = java.time.Duration.ofMinutes(competitionComputationParam.competitionDuration.duration.toMinutes() * 20 / 100);
        java.time.Duration thresholdSuperExcedeed = java.time.Duration.ofMinutes(competitionComputationParam.competitionDuration.duration.toMinutes() * 40 / 100);
        java.time.Duration thresholdUnder = java.time.Duration.ofMinutes(Math.round(competitionComputationParam.competitionDuration.duration.toMinutes() / 10));
        java.time.Duration playDuration = java.time.Duration.parse(competitionComputationParam.getLastPhase().averagePlayDuration);
        if (thresholdExcedeed.compareTo(playDuration) < 0) {
            thresholdExcedeed = playDuration;
        }
        if (thresholdSuperExcedeed.compareTo(playDuration.multipliedBy(2)) < 0) {
            thresholdExcedeed = playDuration.multipliedBy(2);
        }
        java.time.Duration competitionDuration = competitionComputationParam.competitionDuration.duration;
        java.time.Duration exceededDuration = expectedDuration.minus(competitionDuration);
        java.time.Duration underDuration = competitionDuration.minus(expectedDuration);
        boolean hasFinalElimination = false;
        if (exceededDuration.compareTo(thresholdExcedeed) > 0 || underDuration.compareTo(thresholdUnder) > 0) {

            SortedSet<CompetitionComputationParam> competitionComputationParamSetToTry = competitionComputationParam.createCompetitionComputationParamsListToTry();
            SortedSet<CompetitionComputationParam> competitionComputationParamSetToTryWithoutMixing = new TreeSet<>();
            SortedSet<CompetitionComputationParam> competitionComputationParamSetToTryWithoutMixingWithoutQualification = new TreeSet<>();
            SortedSet<CompetitionComputationParam> competitionComputationParamSetToTryWithoutQualification = new TreeSet<>();

            for (CompetitionComputationParam competitionComputationParamElt : competitionComputationParamSetToTry) {
                if (competitionComputationParamElt.getMixingPhaseParameter() != null && competitionComputationParamElt.phases.size() > 1) {
                    CompetitionComputationParam competitionComputationParamToTryWithoutMixing = competitionComputationParamElt.cloneCompetitionComputationParam();
                    competitionComputationParamToTryWithoutMixing.removeMixingPhase();
                    competitionComputationParamSetToTryWithoutMixing.add(competitionComputationParamToTryWithoutMixing);
                    CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification = null;
                    CompetitionComputationParam competitionComputationParamToTryWithoutQualification = null;
                    if (competitionComputationParamToTryWithoutMixing.getQualificationPhaseParameter() != null && competitionComputationParamToTryWithoutMixing.phases.size() > 1) {
                        competitionComputationParamToTryWithoutMixingWithoutQualification = competitionComputationParamToTryWithoutMixing.cloneCompetitionComputationParam();
                        competitionComputationParamToTryWithoutMixingWithoutQualification.removeQualificationPhase();
                        competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification);
                    }
                    if (competitionComputationParamElt.getQualificationPhaseParameter() != null && competitionComputationParamElt.phases.size() > 2) {
                        competitionComputationParamToTryWithoutQualification = competitionComputationParamElt.cloneCompetitionComputationParam();
                        competitionComputationParamToTryWithoutQualification.removeQualificationPhase();
                        competitionComputationParamSetToTryWithoutQualification.add(competitionComputationParamToTryWithoutQualification);
                    }


                }
                if (competitionComputationParamElt.getQualificationPhaseParameter() != null && competitionComputationParamElt.phases.size() > 1 && competitionComputationParamElt.getQualificationPhaseParameter().tournamentFormatsAccepted.contains(TournamentFormat.SWISS)) {
                    CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification = competitionComputationParamElt.cloneCompetitionComputationParam();
                    CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionComputationParamToTryWithoutMixingWithoutQualification.getQualificationPhaseParameter();
                    competitionCreationParamPhaseQualification.groupSizeMaximum = competitionComputationParam.numberOfParticipantCompetition;
                    competitionCreationParamPhaseQualification.groupSizeMinimum = competitionComputationParam.numberOfParticipantCompetition;
                    competitionCreationParamPhaseQualification.tournamentFormatsAccepted.clear();
                    competitionCreationParamPhaseQualification.tournamentFormatsAccepted.add(TournamentFormat.SWISS);

                    if (competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter() != null) {

                        for (TournamentFormat tournamentFormat : competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted) {
                            if (tournamentFormat.compareTo(TournamentFormat.ANY_ELIMINATION) != 0) {
                                CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification1 = competitionComputationParamToTryWithoutMixingWithoutQualification.cloneCompetitionComputationParam();
                                competitionComputationParamToTryWithoutMixingWithoutQualification1.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                                competitionComputationParamToTryWithoutMixingWithoutQualification1.getFinalPhaseParameter().tournamentFormatsAccepted.add(tournamentFormat);
                                competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification1);
                            }
                        }
                    } else {
                        competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification);
                    }

                    competitionComputationParamToTryWithoutMixingWithoutQualification = competitionComputationParamElt.cloneCompetitionComputationParam();
                    competitionComputationParamToTryWithoutMixingWithoutQualification.removeQualificationPhase();
                    if (competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter() != null) {
                        for (TournamentFormat tournamentFormat : competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted) {
                            if (tournamentFormat.compareTo(TournamentFormat.ANY_ELIMINATION) != 0) {
                                CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification1 = competitionComputationParamToTryWithoutMixingWithoutQualification.cloneCompetitionComputationParam();
                                competitionComputationParamToTryWithoutMixingWithoutQualification1.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                                competitionComputationParamToTryWithoutMixingWithoutQualification1.getFinalPhaseParameter().tournamentFormatsAccepted.add(tournamentFormat);
                                competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification1);
                            }

                        }
                    } else {
                        competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification);
                    }
                }
                if (competitionComputationParamElt.getFinalPhaseParameter() != null) {
                    CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification = competitionComputationParamElt.cloneCompetitionComputationParam();
                    competitionComputationParamToTryWithoutMixingWithoutQualification.removeQualificationPhase();
                    competitionComputationParamToTryWithoutMixingWithoutQualification.removeMixingPhase();
                    competitionComputationParamSetToTryWithoutMixingWithoutQualification.add(competitionComputationParamToTryWithoutMixingWithoutQualification);
                }
                if (!hasFinalElimination && competitionComputationParamElt.getFinalPhaseParameter() != null) {
                    for (TournamentFormat tournamentFormat : competitionComputationParam.getFinalPhaseParameter().tournamentFormatsAccepted) {
                        if (TournamentFormat.allElimination().contains(tournamentFormat)) {
                            hasFinalElimination = true;
                            break;
                        }
                    }
                }
            }
            if (exceededDuration.compareTo(thresholdSuperExcedeed) > 0 && exceededDuration.toMinutes() >= 60) {
                if (hasFinalElimination) {
                    addLowerEliminationLevel(competitionComputationParamSetToTryWithoutMixing);
                    addLowerEliminationLevel(competitionComputationParamSetToTryWithoutQualification);
                    addLowerEliminationLevel(competitionComputationParamSetToTryWithoutMixingWithoutQualification);
                }
                addLowerMinimumPlay(competitionComputationParamSetToTryWithoutMixing);
                addLowerMinimumPlay(competitionComputationParamSetToTryWithoutQualification);
                addLowerMinimumPlay(competitionComputationParamSetToTryWithoutMixingWithoutQualification);

            }

            Sets.sort(competitionComputationParamSetToTryWithoutMixing);
            for (CompetitionComputationParam competitionComputationParamToTryWithoutMixing : competitionComputationParamSetToTryWithoutMixing) {
                competitionComputationParamToTryWithoutMixing.getFinalPhaseParameter().thirdPlaceMatchEnabled = competitionComputationParam.getFinalPhaseParameter().thirdPlaceMatchEnabled;

            }
            Sets.sort(competitionComputationParamSetToTryWithoutQualification);
            for (CompetitionComputationParam competitionComputationParamToTryWithoutQualification : competitionComputationParamSetToTryWithoutQualification) {
                competitionComputationParamToTryWithoutQualification.getFinalPhaseParameter().thirdPlaceMatchEnabled = competitionComputationParam.getFinalPhaseParameter().thirdPlaceMatchEnabled;

            }
            Sets.sort(competitionComputationParamSetToTryWithoutMixingWithoutQualification);
            for (CompetitionComputationParam competitionComputationParamToTryWithoutMixingWithoutQualification : competitionComputationParamSetToTryWithoutMixingWithoutQualification) {
                competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter().thirdPlaceMatchEnabled = competitionComputationParam.getFinalPhaseParameter().thirdPlaceMatchEnabled;
                if (competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter().tournamentFormatsAccepted.size() == 1 && TournamentFormat.allowFormat(competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter().tournamentFormatsAccepted, TournamentFormat.LADDER)) {
                    competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter().registrationOnTheFly = false;
                } else {
                    competitionComputationParamToTryWithoutMixingWithoutQualification.getFinalPhaseParameter().tournamentFormatsAccepted.remove(TournamentFormat.LADDER);
                }
            }

            List<CompetitionInstance> competitionInstancesAmputedTries = null;
            competitionInstance = null;
            if (!competitionComputationParamSetToTryWithoutMixing.isEmpty()) {
                competitionInstancesAmputedTries = computeCompetitionFormatFor(competitionComputationParamSetToTryWithoutMixing, competitionComputationParamSetTried, localSizeResult, prefix);
                if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty()) {
                    competitionInstances.addAll(competitionInstancesAmputedTries);
//                    expectedDuration = competitionInstance.expectedRelativeEndTime.duration;
                }
            }
//            if (competitionInstance == null || expectedDuration.compareTo(competitionComputationParam.competitionDuration.duration) > 0) {
            if (!competitionComputationParamSetToTryWithoutQualification.isEmpty()) {
                competitionInstancesAmputedTries = computeCompetitionFormatFor(competitionComputationParamSetToTryWithoutQualification, competitionComputationParamSetTried, localSizeResult, prefix);
                if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty()) {
                    competitionInstances.addAll(competitionInstancesAmputedTries);
                    expectedDuration = competitionComputationParam.competitionDuration.duration;
                }
            }
//                if (competitionInstance == null || expectedDuration.compareTo(competitionComputationParam.competitionDuration.duration) > 0) {
            if (!competitionComputationParamSetToTryWithoutMixingWithoutQualification.isEmpty()) {
                competitionInstancesAmputedTries = computeCompetitionFormatFor(competitionComputationParamSetToTryWithoutMixingWithoutQualification, competitionComputationParamSetTried, localSizeResult, prefix);
                if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty()) {
                    competitionInstances.addAll(competitionInstancesAmputedTries);
                }
            }
//                } else {
//                    if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty())
//                        competitionInstances.addAll(competitionInstancesAmputedTries);
//                }
//            } else {
//                if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty())
//                    competitionInstances.addAll(competitionInstancesAmputedTries);
//            }
            Collections.sort(competitionInstances, new CompetitionInstanceComparator(competitionComputationParam.competitionDuration));
            competitionInstance = competitionInstances.get(0);
            expectedDuration = competitionInstance.expectedRelativeEndTime.duration;
            underDuration = competitionDuration.minus(expectedDuration);

            if (underDuration.compareTo(thresholdUnder) > 0) {
                competitionComputationParamSetToTry = competitionComputationParam.createCompetitionComputationParamsListToTry();
                SortedSet<CompetitionComputationParam> competitionComputationParamSetToTryFiltered = new TreeSet<>();
//            SortedSet<CompetitionComputationParam> competitionComputationParamSetToTrySwissOrRoundRobin = new TreeSet<>();
                Integer numberOfPlayMinimum = null;
                Integer numberOfPlayMaximum = null;
                boolean allowRoundRobin = false;
                boolean allowSwiss = false;
                for (CompetitionComputationParam competitionComputationParamElt : competitionComputationParamSetToTry) {
//                    if (competitionComputationParamElt.getMixingPhaseParameter() != null) {
//                        if (numberOfPlayMinimum == null || numberOfPlayMinimum.compareTo(competitionComputationParamElt.getMixingPhaseParameter().numberOfPlayMinimum) > 0)
//                            numberOfPlayMinimum = competitionComputationParamElt.getMixingPhaseParameter().numberOfPlayMinimum;
//                        if (numberOfPlayMaximum == null || numberOfPlayMaximum.compareTo(competitionComputationParamElt.getMixingPhaseParameter().numberOfPlayMaximum) < 0)
//                            numberOfPlayMaximum = competitionComputationParamElt.getMixingPhaseParameter().numberOfPlayMaximum;
//                        allowRoundRobin = true;
//                        allowSwiss = true;
//                    }
//                    if (competitionComputationParamElt.getQualificationPhaseParameter() != null) {
//                        if (numberOfPlayMinimum == null || numberOfPlayMinimum.compareTo(competitionComputationParamElt.getQualificationPhaseParameter().numberOfPlayMinimum) > 0)
//                            numberOfPlayMinimum = competitionComputationParamElt.getQualificationPhaseParameter().numberOfPlayMinimum;
//                        if (numberOfPlayMaximum == null || numberOfPlayMaximum.compareTo(competitionComputationParamElt.getQualificationPhaseParameter().numberOfPlayMaximum) < 0)
//                            numberOfPlayMaximum = competitionComputationParamElt.getQualificationPhaseParameter().numberOfPlayMaximum;
//                        allowRoundRobin = true;
//                        allowSwiss = true;
//                    }
                    if (competitionComputationParamElt.getFinalPhaseParameter() != null) {
                        if (numberOfPlayMinimum == null || numberOfPlayMinimum.compareTo(competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMinimum) > 0)
                            numberOfPlayMinimum = competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMinimum;
                        if (numberOfPlayMaximum == null || numberOfPlayMaximum.compareTo(competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMaximum) < 0)
                            numberOfPlayMaximum = competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMaximum;

                        if (competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalEnabled != null && competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalEnabled && competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMinimum != null && (numberOfPlayMinimum == null || numberOfPlayMinimum.compareTo(competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMinimum) > 0))
                            numberOfPlayMinimum = competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMinimum;
                        if (competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalEnabled != null && competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalEnabled && competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMaximum != null && (numberOfPlayMaximum == null || numberOfPlayMaximum.compareTo(competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMaximum) < 0))
                            numberOfPlayMaximum = competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayFinalMaximum;

                        competitionComputationParamElt.removeMixingPhase();
                        competitionComputationParamElt.removeQualificationPhase();
                        if (competitionComputationParamElt.getFinalPhaseParameter() != null) {
                            boolean roundRobinTried = false;
                            for (TournamentFormat tournamentFormat : competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted) {
                                if (tournamentFormat.compareTo(TournamentFormat.ROUND_ROBIN) == 0) {
                                    roundRobinTried = true;
                                } else if (tournamentFormat.compareTo(TournamentFormat.SWISS) == 0) {
                                    allowSwiss = true;
                                }
                            }
                            allowRoundRobin = roundRobinTried;
                        }
                    }
                }
                if (allowRoundRobin || allowSwiss) {
                    for (CompetitionComputationParam competitionComputationParamElt : competitionComputationParamSetToTry) {
                        if (competitionComputationParamElt.getFinalPhaseParameter() != null) {
                            competitionComputationParamElt.removeMixingPhase();
                            competitionComputationParamElt.removeQualificationPhase();
                            competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                            competitionComputationParamElt.getFinalPhaseParameter().groupSizeMaximum = competitionComputationParam.numberOfParticipantCompetition;
                            competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalEnabled = false;
                            competitionComputationParamElt.getFinalPhaseParameter().groupSizeFinalThreshold = competitionComputationParam.numberOfParticipantCompetition;
                            competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMinimum = numberOfPlayMinimum;
                            competitionComputationParamElt.getFinalPhaseParameter().numberOfPlayMaximum = numberOfPlayMaximum;
                            competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                            if (allowRoundRobin) {
                                competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                                competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                                competitionComputationParamSetToTryFiltered.addAll(competitionComputationParamElt.createCompetitionComputationParamsListToTry());
                            }
                            if (allowSwiss) {
                                competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                                competitionComputationParamElt.getFinalPhaseParameter().tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                                competitionComputationParamSetToTryFiltered.addAll(competitionComputationParamElt.createCompetitionComputationParamsListToTry());
                            }
                        }
                    }
                }
                Sets.sort(competitionComputationParamSetToTryFiltered);
                if (!competitionComputationParamSetToTryFiltered.isEmpty()) {
                    Sets.sort(competitionComputationParamSetToTryFiltered);
                    competitionInstancesAmputedTries = computeCompetitionFormatFor(competitionComputationParamSetToTryFiltered, competitionComputationParamSetTried, localSizeResult, prefix);
                    if (competitionInstancesAmputedTries != null && !competitionInstancesAmputedTries.isEmpty()) {
                        competitionInstances.addAll(competitionInstancesAmputedTries);
                    }
                    Collections.sort(competitionInstances, new CompetitionInstanceComparator(competitionComputationParam.competitionDuration));
                }
            }
        }

        while (competitionInstances.size() > localSizeResult)
            competitionInstances.remove(localSizeResult);

/*
        boolean enableGap = true;
        if (enableGap) {
            //  logger.log(Level.FINE, "[GAP_CHECK]");

            Duration gap = null;
            for (CompetitionInstance competitionInstanceFound : competitionInstances) {
                if (gap == null || gap.compareTo(Duration.ofMinutes(competitionInstanceFound.getExpectedGlobalDuration().avg).minus(competitionComputationParam.competitionDuration).abs()) > 0)
                    gap = Duration.ofMinutes(competitionInstanceFound.getExpectedGlobalDuration().avg).minus(competitionComputationParam.competitionDuration);
            }
            Duration gapMaxBasedOnCompetitionDuration = competitionComputationParam.competitionDuration.dividedBy(5);
            Duration gapMaxBasedOnNumberOfParticipantCompetition = competitionComputationParam.averagePlayDuration.plus(competitionComputationParam.intermissionDuration).multipliedBy(1 + (int) Math.ceil(Math.log(competitionComputationParam.numberOfParticipantCompetition)));
            Duration gapMax = gapMaxBasedOnCompetitionDuration;
            if (gapMax.compareTo(gapMaxBasedOnNumberOfParticipantCompetition) < 0)
                gapMax = gapMaxBasedOnNumberOfParticipantCompetition;

            int numberOfBracketsMax = Math.min((int) Math.ceil(Math.log(competitionComputationParam.numberOfParticipantCompetition)), competitionComputationParam.maximumEliminationLevel);
            int numberOfBracketsMin = Math.max((int) Math.ceil(Math.log(competitionComputationParam.numberOfParticipantCompetition)), competitionComputationParam.minFinalGroupSize);
            if (numberOfBracketsMin < 1)
                numberOfBracketsMin = 1;
            if (gap == null || gap.compareTo(gapMax) > 0) {
                //  logger.log(Level.FINE, "[GAP_CHECK] gap:" + gap + " > gapMax:" + gapMax);
                Set<TournamentFormat> tournamentFormatsForGap = competitionComputationParam.getTournamentFormatForGap();
                bypass = false;
                previousComputationOvertime = false;
                minNumberOfPlayGlobalPrevious = 0;
                if (tournamentFormatsForGap.contains(TournamentFormat.SWISS)) {
                    for (int minNumberOfPlay = competitionComputationParam.minNumberOfPlayGlobal; minNumberOfPlay <= competitionComputationParam.maxNumberOfPlayGlobal; minNumberOfPlay += (competitionComputationParam.allowEvenNumberOfPlay ? 1 : 2)) {
                        for (int maxNumberOfPlay = minNumberOfPlay; maxNumberOfPlay <= competitionComputationParam.maxNumberOfPlayGlobal; maxNumberOfPlay += (competitionComputationParam.allowEvenNumberOfPlay ? 1 : 2)) {

//                        getIdGeneratorInstance().clear();
                            tryIndex++;
                            CompetitionComputationParam competitionComputationParamForSwiss = competitionComputationParam.cloneCompetitionComputationParam();
                            competitionComputationParamForSwiss.minNumberOfPlayGlobal = minNumberOfPlay;
                            competitionComputationParamForSwiss.maxNumberOfPlayGlobal = maxNumberOfPlay;
                            competitionComputationParamForSwiss.maxGroupSize = competitionComputationParamForSwiss.numberOfParticipantCompetition;
                            competitionComputationParamForSwiss.minGroupSize = competitionComputationParamForSwiss.numberOfParticipantCompetition;
                            if (TournamentFormat.allowEliminationFormat(tournamentFormatsForGap) && competitionComputationParam.numberOfParticipantCompetition > competitionComputationParam.numberOfParticipantMatch) {
                                competitionComputationParamForSwiss.setMixingPhase(null);

                                PhaseParameter phaseParameterQualification = competitionComputationParam.getQualificationPhaseParameter();
                                if (phaseParameterQualification == null)
                                    phaseParameterQualification = new PhaseParameter();
                                phaseParameterQualification.groupSizeMaximum = (int) Math.ceil((double) competitionComputationParamForSwiss.playVersusType.roundUp(competitionComputationParamForSwiss.numberOfParticipantCompetition * QUALIFICATION_PERCENTAGE / 100) * (double) competitionComputationParamForSwiss.numberOfParticipantMatch) * competitionComputationParamForSwiss.numberOfParticipantMatch;
                                if (phaseParameterQualification.groupSizeMaximum > competitionComputationParamForSwiss.numberOfParticipantCompetition)
                                    phaseParameterQualification.groupSizeMaximum = competitionComputationParamForSwiss.numberOfParticipantCompetition;
                                phaseParameterQualification.tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());
                                competitionComputationParamForSwiss.setQualificationPhase(phaseParameterQualification);

                                PhaseParameter phaseParameter = competitionComputationParam.getFinalPhaseParameter();
                                if (phaseParameter == null)
                                    phaseParameter = new PhaseParameter();
                                phaseParameter.groupSizeMaximum = (int) Math.ceil((double) competitionComputationParamForSwiss.playVersusType.roundUp(competitionComputationParamForSwiss.numberOfParticipantCompetition * QUALIFICATION_PERCENTAGE / 100) * (double) competitionComputationParamForSwiss.numberOfParticipantMatch) * competitionComputationParamForSwiss.numberOfParticipantMatch;
                                if (phaseParameter.groupSizeMaximum > competitionComputationParamForSwiss.numberOfParticipantCompetition)
                                    phaseParameter.groupSizeMaximum = competitionComputationParamForSwiss.numberOfParticipantCompetition;
                                phaseParameter.tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());
                                competitionComputationParamForSwiss.setFinalPhase(phaseParameter);
                            } else {

                                PhaseParameter phaseParameter = competitionComputationParam.getFinalPhaseParameter();
                                if (phaseParameter == null)
                                    phaseParameter = new PhaseParameter();
                                phaseParameter.groupSizeMaximum = (int) Math.ceil((double) competitionComputationParamForSwiss.playVersusType.roundUp(competitionComputationParamForSwiss.numberOfParticipantCompetition * QUALIFICATION_PERCENTAGE / 100) * (double) competitionComputationParamForSwiss.numberOfParticipantMatch) * competitionComputationParamForSwiss.numberOfParticipantMatch;
                                if (phaseParameter.groupSizeMaximum > competitionComputationParamForSwiss.numberOfParticipantCompetition)
                                    phaseParameter.groupSizeMaximum = competitionComputationParamForSwiss.numberOfParticipantCompetition;
                                phaseParameter.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                                competitionComputationParamForSwiss.setFinalPhase(phaseParameter);
                                competitionComputationParamForSwiss.setMixingPhase(null);
                                competitionComputationParamForSwiss.setQualificationPhase(null);
                            }
                            //                        competitionComputationParamForSwiss.minFinalGroupSize = competitionComputationParamForSwiss.playVersusType.roundUp(competitionComputationParamForSwiss.numberOfParticipantCompetition * QUALIFICATION_PERCENTAGE / 100);
                            competitionComputationParamForSwiss.checkAndCorrectValues();
                            //  logger.log(Level.FINE, "[START] try swiss " + tryIndex);


                            bypass = minNumberOfPlayGlobalPrevious.compareTo(competitionComputationParamForSwiss.minNumberOfPlayGlobal) == 0 && previousComputationOvertime;
                            if (bypass) {
                                //  logger.log(Level.FINE, "[END][BYPASSED]  (" + tryIndex + "/" + competitionComputationParams + ") ");
                            } else {
                                newCompetitionInstance = createCompetitionInstanceSwissFor(competitionComputationParamForSwiss);
                                newCompetitionInstance.localId = "" + tryIndex;
                                competitionInstancesNew.clear();
                                competitionInstancesNew.add(newCompetitionInstance);
                                competitionInstances = mergeCompetitionInstances(competitionInstances, competitionInstancesNew, competitionComputationParamForSwiss.competitionDuration, localSizeResult * 10);
                                previousComputationOvertime = false;
                                minNumberOfPlayGlobalPrevious = competitionComputationParamForSwiss.minNumberOfPlayGlobal;
                                Duration newCompetitionInstanceDuration = Duration.ofMinutes(newCompetitionInstance.getExpectedGlobalDuration().max);
                                Duration overtime = newCompetitionInstanceDuration.minus(competitionComputationParamForSwiss.competitionDuration);
                                Duration overtimeMax = competitionComputationParamForSwiss.competitionDuration.dividedBy(5);
                                if (overtimeMax.compareTo(competitionComputationParamForSwiss.maximumPlayDuration) < 0)
                                    overtimeMax = competitionComputationParamForSwiss.maximumPlayDuration;
                                if (overtime.compareTo(overtimeMax) > 0)
                                    previousComputationOvertime = true;
//                if (!getCompetitionLocalIdsFrom(competitionInstances).contains(newCompetitionInstance.localId))
//                    break;

                                //  logger.log(Level.FINE, "[END] try swiss " + tryIndex + " / " + newCompetitionInstance.localId + " / " + Duration.ofMinutes(newCompetitionInstance.getExpectedGlobalDuration().avg));
                            }

                        }
                    }
                }
                if (TournamentFormat.allowEliminationFormat(tournamentFormatsForGap)) {
                    int maxBrackets = 0;
                    for (int i = 10; i > 0; i--) {
                        if (TournamentFormat.allowEliminationFormat(tournamentFormatsForGap, i)) {
                            maxBrackets = i;
                            break;
                        }
                    }
                    if (numberOfBracketsMax > maxBrackets)
                        numberOfBracketsMax = maxBrackets;

                    for (int maxNumberOfPlay = competitionComputationParam.minNumberOfPlayGlobal; maxNumberOfPlay <= competitionComputationParam.maxNumberOfPlayGlobal; maxNumberOfPlay += (competitionComputationParam.allowEvenNumberOfPlay ? 1 : 2)) {

                        for (int minNumberOfPlay = competitionComputationParam.minNumberOfPlayGlobal; minNumberOfPlay <= maxNumberOfPlay; minNumberOfPlay += (competitionComputationParam.allowEvenNumberOfPlay ? 1 : 2)) {

                            CompetitionComputationParam competitionComputationParamForElimination = competitionComputationParam.cloneCompetitionComputationParam();
                            competitionComputationParamForElimination.minNumberOfPlayGlobal = minNumberOfPlay;
                            competitionComputationParamForElimination.maxNumberOfPlayGlobal = maxNumberOfPlay;
                            competitionComputationParamForElimination.maxGroupSize = competitionComputationParam.numberOfParticipantCompetition;
                            competitionComputationParamForElimination.minGroupSize = competitionComputationParam.numberOfParticipantCompetition;
                            competitionComputationParamForElimination.setMixingPhase(null);
                            competitionComputationParamForElimination.setQualificationPhase(null);

                            PhaseParameter phaseParameter = competitionComputationParam.getFinalPhaseParameter();
                            if (phaseParameter == null)
                                phaseParameter = new PhaseParameter();
                            phaseParameter.groupSizeMaximum = competitionComputationParam.numberOfParticipantCompetition;
                            phaseParameter.tournamentFormatsAccepted.addAll(TournamentFormat.allElimination());


                            competitionComputationParamForElimination.minNumberOfPlayGlobal = minNumberOfPlay;
                            competitionComputationParamForElimination.maxNumberOfPlayGlobal = maxNumberOfPlay;

                            int numberOfParticipantForElimination = 0;
                            int eliminationRounds = 1;
                            while (numberOfParticipantForElimination < competitionComputationParamForElimination.participantQualifiedPerMatch) {
                                numberOfParticipantForElimination = getNumberOfParticipantsNeededForFinalElimination(competitionComputationParamForElimination.numberOfParticipantMatch, competitionComputationParamForElimination.participantQualifiedPerMatch, competitionComputationParamForElimination.playVersusType, eliminationRounds);
                                eliminationRounds++;
                            }

                            if (numberOfParticipantForElimination == competitionComputationParam.numberOfParticipantCompetition) {
                                for (int numberOfBrackets = numberOfBracketsMin; numberOfBrackets < numberOfBracketsMax; numberOfBrackets++) {
                                    //  logger.log(Level.FINE, "[START] try elimination only " + tryIndex);
//                                    getIdGeneratorInstance().clear();
                                    tryIndex++;
                                    competitionComputationParamForElimination.minimumEliminationLevel = numberOfBrackets;
                                    competitionComputationParamForElimination.maximumEliminationLevel = numberOfBrackets;
                                    newCompetitionInstance = createCompetitionInstanceEliminationFor(competitionComputationParamForElimination, numberOfBrackets);
                                    newCompetitionInstance.localId = "" + tryIndex;
//                                    newCompetitionInstance.fillCharacteristicDatas();
                                    competitionInstancesNew.add(newCompetitionInstance);
                                    //  logger.log(Level.FINE, "[END] try elimination only " + tryIndex + " / " + newCompetitionInstance.localId + " / " + Duration.ofMinutes(newCompetitionInstance.getExpectedGlobalDuration().avg));
                                }
                            }

                        }
                    }
                }
                if (tournamentFormatsForGap.contains(TournamentFormat.ROUND_ROBIN)) {
                    for (int maxNumberOfPlay = competitionComputationParam.minNumberOfPlayGlobal; maxNumberOfPlay <= competitionComputationParam.maxNumberOfPlayGlobal; maxNumberOfPlay += (competitionComputationParam.allowEvenNumberOfPlay ? 1 : 2)) {

                        for (int minNumberOfPlay = competitionComputationParam.minNumberOfPlayGlobal; minNumberOfPlay <= maxNumberOfPlay; minNumberOfPlay += 2) {
                            CompetitionComputationParam competitionComputationParamForRoundRobin = competitionComputationParam.cloneCompetitionComputationParam();
                            if (competitionComputationParamForRoundRobin.averagePlayDuration.multipliedBy(competitionComputationParamForRoundRobin.numberOfParticipantCompetition / competitionComputationParamForRoundRobin.numberOfParticipantMatch).compareTo(competitionComputationParam.competitionDuration) <= 0) {


                                CompetitionComputationParam competitionComputationParamForElimination = competitionComputationParam.cloneCompetitionComputationParam();
                                competitionComputationParamForElimination.minNumberOfPlayGlobal = minNumberOfPlay;
                                competitionComputationParamForElimination.maxNumberOfPlayGlobal = maxNumberOfPlay;
                                competitionComputationParamForElimination.maxGroupSize = competitionComputationParam.numberOfParticipantCompetition;
                                competitionComputationParamForElimination.minGroupSize = competitionComputationParam.numberOfParticipantCompetition;
                                competitionComputationParamForElimination.setMixingPhase(null);
                                competitionComputationParamForElimination.setQualificationPhase(null);

                                PhaseParameter phaseParameter = competitionComputationParam.getFinalPhaseParameter();
                                if (phaseParameter == null)
                                    phaseParameter = new PhaseParameter();
                                phaseParameter.groupSizeMaximum = competitionComputationParam.numberOfParticipantCompetition;
                                phaseParameter.tournamentFormatsAccepted.add(TournamentFormat.ROUND_ROBIN);
                                competitionComputationParamForRoundRobin.checkAndCorrectValues();
                                tryIndex++;
                                //  logger.log(Level.FINE, "[START] try roundDetails robin " + tryIndex);
                                newCompetitionInstance = createCompetitionInstanceRoundRobinFor(competitionComputationParamForRoundRobin);
                                //  logger.log(Level.FINE, "[END] try roundDetails robin " + tryIndex + " / " + newCompetitionInstance.localId + " / " + Duration.ofMinutes(newCompetitionInstance.getExpectedGlobalDuration().avg));
                                newCompetitionInstance.localId = "" + tryIndex;
                                competitionInstancesNew.add(newCompetitionInstance);
                            }
                        }
                    }
                    competitionInstances = mergeCompetitionInstances(competitionInstances, competitionInstancesNew, competitionComputationParam.competitionDuration, localSizeResult);
                }
            }
        }*/
        for (CompetitionInstance competitionInstanceFound : competitionInstances) {
//            competitionInstanceFound.fillCharacteristicDatas();
            competitionInstanceFound.reset(true);
            competitionInstanceFound.internationalizedLabel.defaultLabel = competitionComputationParam.competitionName;
//            competitionInstanceFound.fillExpectedRelativeTime();
        }
        if (competitionInstances.isEmpty())
            throw new IllegalArgumentException(competitionComputationParam.toParams().toString());

        //  logger.log(Level.FINE, "[END]");
        return competitionInstances;
    }

    private void addLowerEliminationLevel(SortedSet<CompetitionComputationParam> competitionComputationParamElements) {
        SortedSet<CompetitionComputationParam> competitionComputationParamToAdd = new TreeSet<>();
        for (CompetitionComputationParam competitionComputationParamElement : competitionComputationParamElements) {
            boolean hasEliminationFormat = false;
            boolean addLowerEliminationLevel = false;
            if (competitionComputationParamElement.getFinalPhaseParameter() != null) {
                for (TournamentFormat tournamentFormat : competitionComputationParamElement.getFinalPhaseParameter().tournamentFormatsAccepted) {
                    if (!addLowerEliminationLevel && TournamentFormat.allElimination().contains(tournamentFormat)) {
                        hasEliminationFormat = true;
                        if (hasEliminationFormat) {
                            addLowerEliminationLevel = true;
                        }
                    }
                }
                if (addLowerEliminationLevel) {
                    for (TournamentFormat tournamentFormat : TournamentFormat.allElimination()) {
                        if (tournamentFormat.compareTo(TournamentFormat.ANY_ELIMINATION) != 0) {
                            CompetitionComputationParam competitionComputationParamElementNew = competitionComputationParamElement.cloneCompetitionComputationParam();
                            competitionComputationParamElementNew.getFinalPhaseParameter().tournamentFormatsAccepted.clear();
                            competitionComputationParamElementNew.getFinalPhaseParameter().tournamentFormatsAccepted.add(tournamentFormat);
                            competitionComputationParamToAdd.add(competitionComputationParamElementNew);
                        }
                    }
                }
            }
        }
        competitionComputationParamElements.addAll(competitionComputationParamToAdd);
    }


    private void addLowerMinimumPlay(SortedSet<CompetitionComputationParam> competitionComputationParamElements) {
        SortedSet<CompetitionComputationParam> competitionComputationParamToAdd = new TreeSet<>();
        for (CompetitionComputationParam competitionComputationParamElement : competitionComputationParamElements) {
            CompetitionComputationParam competitionComputationParamElementNew = null;
            CompetitionComputationParam competitionComputationParamElementCurrent = competitionComputationParamElement.cloneCompetitionComputationParam();
            do {
                competitionComputationParamElementNew = null;
                if (competitionComputationParamElementCurrent.getFinalPhaseParameter() != null && competitionComputationParamElementCurrent.getFinalPhaseParameter().groupSizeFinalEnabled != null && competitionComputationParamElementCurrent.getFinalPhaseParameter().groupSizeFinalEnabled) {
                    if (competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayMinimum > 1) {
                        int step = 2;
                        if (competitionComputationParamElementCurrent.getFinalPhaseParameter().allowEvenNumberOfPlay) {
                            step = 1;
                        }
                        if (competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayMinimum > step) {
                            if (competitionComputationParamElementNew == null)
                                competitionComputationParamElementNew = competitionComputationParamElementCurrent.cloneCompetitionComputationParam();
                            competitionComputationParamElementNew.getFinalPhaseParameter().numberOfPlayMinimum = competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayMinimum - step;
                            competitionComputationParamElementNew.getFinalPhaseParameter().numberOfPlayMaximum = competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayMaximum - step;
                            if (competitionComputationParamElementNew.getFinalPhaseParameter().numberOfPlayFinalMinimum != null && competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayFinalMinimum > 1) {
                                competitionComputationParamElementNew.getFinalPhaseParameter().numberOfPlayFinalMinimum = competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayFinalMinimum - step;
                                competitionComputationParamElementNew.getFinalPhaseParameter().numberOfPlayFinalMaximum = competitionComputationParamElementCurrent.getFinalPhaseParameter().numberOfPlayFinalMaximum - step;
                            }
                        }
                    }
                }
                if (competitionComputationParamElementCurrent.getQualificationPhaseParameter() != null) {
                    if (competitionComputationParamElementCurrent.getQualificationPhaseParameter().numberOfPlayMinimum > 1) {
                        int step = 2;
                        if (competitionComputationParamElementCurrent.getQualificationPhaseParameter().allowEvenNumberOfPlay) {
                            step = 1;
                        }
                        if (competitionComputationParamElementCurrent.getQualificationPhaseParameter().numberOfPlayMinimum > step) {
                            if (competitionComputationParamElementNew == null)
                                competitionComputationParamElementNew = competitionComputationParamElementCurrent.cloneCompetitionComputationParam();
                            competitionComputationParamElementNew.getQualificationPhaseParameter().numberOfPlayMinimum = competitionComputationParamElementCurrent.getQualificationPhaseParameter().numberOfPlayMinimum - step;
                            competitionComputationParamElementNew.getQualificationPhaseParameter().numberOfPlayMaximum = competitionComputationParamElementCurrent.getQualificationPhaseParameter().numberOfPlayMaximum - step;
                        }
                    }
                }
                if (competitionComputationParamElementCurrent.getMixingPhaseParameter() != null) {
                    if (competitionComputationParamElementCurrent.getMixingPhaseParameter().numberOfPlayMinimum > 1) {
                        int step = 2;
                        if (competitionComputationParamElementCurrent.getMixingPhaseParameter().allowEvenNumberOfPlay) {
                            step = 1;
                        }
                        if (competitionComputationParamElementCurrent.getMixingPhaseParameter().numberOfPlayMinimum > step) {
                            if (competitionComputationParamElementNew == null)
                                competitionComputationParamElementNew = competitionComputationParamElementCurrent.cloneCompetitionComputationParam();
                            competitionComputationParamElementNew.getMixingPhaseParameter().numberOfPlayMinimum = competitionComputationParamElementCurrent.getMixingPhaseParameter().numberOfPlayMinimum - step;
                            competitionComputationParamElementNew.getMixingPhaseParameter().numberOfPlayMaximum = competitionComputationParamElementCurrent.getMixingPhaseParameter().numberOfPlayMaximum - step;
                        }
                    }
                }
                if (competitionComputationParamElementNew != null) {
                    competitionComputationParamToAdd.add(competitionComputationParamElementNew);
                    competitionComputationParamElementCurrent = competitionComputationParamElementNew.cloneCompetitionComputationParam();
                }
            } while (competitionComputationParamElementNew != null);
        }
        competitionComputationParamElements.addAll(competitionComputationParamToAdd);
    }

    private void removeAllParamGreaterThan(SortedSet<CompetitionComputationParam> competitionComputationParams, CompetitionComputationParam competitionComputationParamPrevious, CompetitionComputationParam competitionComputationParamCurrent) {
        List<CompetitionComputationParam> competitionComputationParamsToRemove = new ArrayList<>();
        if (competitionComputationParamPrevious != null) {
            CompetitionComputationParam competitionComputationParamDiff = new CompetitionComputationParam();
            competitionComputationParamDiff.competitionDuration = competitionComputationParamPrevious.competitionDuration;
            competitionComputationParamDiff.competitionName = competitionComputationParamPrevious.competitionName;
            competitionComputationParamDiff = (CompetitionComputationParam) competitionComputationParamCurrent.diff(competitionComputationParamPrevious, competitionComputationParamDiff);
            if (competitionComputationParamDiff != null && !competitionComputationParamDiff.phases.isEmpty()) {
                for (CompetitionComputationParam competitionComputationParam : competitionComputationParams) {
                    if (competitionComputationParam.greaterPlayQuantityWithSameFormat(competitionComputationParamDiff)) {
                        competitionComputationParamsToRemove.add(competitionComputationParam);
//                    }else if (competitionComputationParam.greaterPlayQuantity(competitionComputationParamDiff)) {
//                        competitionComputationParam.removeTournamentFormatUsed(competitionComputationParamDiff);
                    }
                }
            }
            competitionComputationParams.removeAll(competitionComputationParamsToRemove);
        }
    }

    private List<CompetitionInstance> computeCompetitionFormatFor(SortedSet<CompetitionComputationParam> competitionComputationParams, SortedSet<CompetitionComputationParam> competitionComputationParamSetTried, int localSizeResult, String prefix) throws CompetitionInstanceGeneratorException {
        List<CompetitionInstance> competitionInstances = new ArrayList<>();
        int tryIndex = 0;
        Sets.sort(competitionComputationParams);
        SortedSet<CompetitionComputationParam> competitionComputationParamsCorrected = new TreeSet<>();
        for (CompetitionComputationParam competitionComputationParamElt : competitionComputationParams) {
            tryIndex++;
            competitionComputationParamElt.checkAndCorrectValues();
            competitionComputationParamsCorrected.add(competitionComputationParamElt);
            logCompetitionComputationParamElt(competitionComputationParamElt, tryIndex, competitionComputationParams.size(), "TRY LIST");
        }
        tryIndex = 0;
        Sets.sort(competitionComputationParamsCorrected);
        CompetitionComputationParam competitionComputationParamEltCorrected = null;
        CompetitionComputationParam competitionComputationParamEltCorrectedPrevious = null;
        while (!competitionComputationParamsCorrected.isEmpty()) {
//            CompetitionComputationParam competitionComputationParamElt= competitionComputationParams.first();
            competitionComputationParamEltCorrected = competitionComputationParamsCorrected.first();
            competitionComputationParamsCorrected.remove(competitionComputationParamEltCorrected);

//            getIdGeneratorInstance().clear();
            //  logger.log(Level.FINE, "[START] (" + tryIndex + "/" + competitionComputationParams + ")");
            tryIndex++;
/*            bypass = minNumberOfPlayGlobalPrevious.compareTo(competitionComputationParamElt.minNumberOfPlayGlobal) == 0 && previousComputationOvertime;
            if (bypass) {
                //  logger.log(Level.FINE, "[END][BYPASSED]  (" + tryIndex + "/" + competitionComputationParams + ") ");
            } else {*/
            logCompetitionComputationParamElt(competitionComputationParamEltCorrected, tryIndex, competitionComputationParamsCorrected.size(), "START");
            CompetitionComputationParam competitionComputationParamCurrentElt = competitionComputationParamEltCorrected.cloneCompetitionComputationParam();
            CompetitionInstance competitionInstance = createCompetitionInstanceFor(competitionComputationParamEltCorrected);
            competitionInstance.localId = prefix + "" + tryIndex;
            Duration expectedDuration = competitionInstance.expectedRelativeEndTime;
            logCompetitionComputationParamElt(competitionComputationParamEltCorrected, tryIndex, competitionComputationParamsCorrected.size(), "END - expectedDuration:" + expectedDuration.toString());
            if (expectedDuration.duration.compareTo(competitionComputationParamEltCorrected.competitionDuration.duration) > 0) {
                removeAllParamGreaterThan(competitionComputationParamsCorrected, competitionComputationParamEltCorrectedPrevious, competitionComputationParamCurrentElt);
            }

            List<CompetitionInstance> competitionInstancesToAdd = new ArrayList<>();
            competitionInstancesToAdd.add(competitionInstance);
            competitionInstances = mergeCompetitionInstances(competitionInstances, competitionInstancesToAdd, competitionComputationParamEltCorrected.competitionDuration, localSizeResult * 10);

//            if (expectedDuration.duration.compareTo(competitionComputationParamEltCorrected.competitionDuration.duration.minus(java.time.Duration.ofMinutes(competitionComputationParamEltCorrected.competitionDuration.duration.toMinutes() * 25 / 100))) < 0) {
//                removeAllParamLesserThan(competitionComputationParamsCorrected, competitionComputationParamCurrentElt);
//            }
            competitionComputationParamSetTried.add(competitionComputationParamEltCorrected);
            competitionComputationParamEltCorrectedPrevious = competitionComputationParamCurrentElt;

        }
//        for (CompetitionInstance competitionInstance : competitionInstances) {
//            competitionInstance.fillCompetitionMatchLink();
//
//        }
//        Collections.sort(competitionInstances, new CompetitionInstanceComparator(competitionInstances.get(0).competitionComputationParam.competitionDuration));
        return competitionInstances;
    }


    private void removeAllParamLesserThan(SortedSet<CompetitionComputationParam> competitionComputationParams, CompetitionComputationParam competitionComputationParamThreshold) {
        List<CompetitionComputationParam> competitionComputationParamsToRemove = new ArrayList<>();
        for (CompetitionComputationParam competitionComputationParam : competitionComputationParams) {
            if (competitionComputationParamThreshold.greaterPlayQuantityWithSameFormat(competitionComputationParam)) {
                competitionComputationParamsToRemove.add(competitionComputationParam);
            }
        }
        competitionComputationParams.removeAll(competitionComputationParamsToRemove);
    }


    public void logCompetitionComputationParamElt(CompetitionComputationParam competitionComputationParamElt, int tryIndex, int trySize, String info) {
        LOGGER.log(Level.INFO, "[" + CLASS + "]\t[logCompetitionComputationParamElt]\t" + competitionComputationParamElt.toString() + "\t" + tryIndex + "\t" + trySize + (info != null ? "\t" + info : ""));
    }


    public void logCompetitionCreationParamElt(CompetitionCreationParam competitionCreationParam, String info) {
        LOGGER.log(Level.INFO, "[" + CLASS + "]\t[logCompetitionCreationParamElt]\t" + competitionCreationParam.toString() + (info != null ? "\t" + info : ""));
    }

    public List<String> getCompetitionLocalIdsFrom(List<CompetitionInstance> competitionInstances) {
        List<String> competitionLocalIds = new ArrayList<>();
        for (CompetitionInstance competitionInstance : competitionInstances)
            competitionLocalIds.add(competitionInstance.localId);
        return competitionLocalIds;
    }

//
//    public  CompetitionInstance findBestCompetitionInstance(CompetitionInshetance competitionInstance1, CompetitionInstance competitionInstance2, Duration expectedDuration) {
//        CompetitionInstance competitionInstance = null;
//        if (competitionInstance1 == null) {
//            competitionInstance = competitionInstance2;
//        } else if (competitionInstance2 == null) {
//            competitionInstance = competitionInstance1;
//        } else {
//            if (competitionInstance1.getExpectedGlobalAverageDuration().compareTo(expectedDuration) > 0) {
//                if (competitionInstance2.getExpectedGlobalAverageDuration().compareTo(expectedDuration) > 0) {
//                    if (competitionInstance1.compareTo(competitionInstance2) > 0) {
//                        competitionInstance = competitionInstance2;
//                    } else {
//                        competitionInstance = competitionInstance1;
//                    }
//                } else {
//                    competitionInstance = competitionInstance2;
//                }
//            } else {
//                if (competitionInstance2.getExpectedGlobalAverageDuration().compareTo(expectedDuration) > 0) {
//                    competitionInstance = competitionInstance1;
//                } else {
//                    if (competitionInstance1.compareTo(competitionInstance2) > 0) {
//                        competitionInstance = competitionInstance1;
//                    } else {
//                        competitionInstance = competitionInstance2;
//                    }
//                }
//            }
//        }
//        return competitionInstance;
//    }


    public List<CompetitionInstance> mergeCompetitionInstances
            (List<CompetitionInstance> competitionInstances, List<CompetitionInstance> competitionInstancesNew, Duration
                    competitionDuration, int sizeResult) {
        List<CompetitionInstance> competitionInstancesMerged = new ArrayList<>();
        if (competitionInstances.size() + competitionInstancesNew.size() <= sizeResult) {
            competitionInstancesMerged.addAll(competitionInstances);
            competitionInstancesMerged.addAll(competitionInstancesNew);
            competitionInstancesMerged.sort(new CompetitionInstanceComparator(competitionDuration));
        } else {
            List<CompetitionInstance> competitionInstancesJoined = new ArrayList<>();
            competitionInstancesJoined.addAll(competitionInstances);
            competitionInstancesJoined.addAll(competitionInstancesNew);
            competitionInstancesJoined.sort(new CompetitionInstanceComparator(competitionDuration));
//            List<Integer> competitionLocalIds = getCompetitionLocalIdsFrom(competitionInstances);
//            SortedSet<CompetitionInstance> competitionInstancesJoinedSorted = new TreeSet<CompetitionInstance>();
//            competitionInstancesJoinedSorted.addAll(competitionInstancesJoined);
//            competitionInstances.clear();
            int index = 0;
            for (CompetitionInstance competitionInstance : competitionInstancesJoined) {
                competitionInstancesMerged.add(competitionInstance);
                index++;
                if (index >= sizeResult)
                    break;
            }
        }
        return competitionInstancesMerged;
    }


    @Override
    public CompetitionInstance createCompetitionInstanceFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionComputationParam.toString();
        //  logger.log(Level.FINE, "[START]" + logPrefix);


        CompetitionInstance competitionInstance = CompetitionInstance.createInstance();
        competitionInstance.internationalizedLabel.defaultLabel = competitionComputationParam.competitionName + " " + competitionComputationParam.toString();
        competitionInstance.setCompetitionComputationParam(competitionComputationParam);

        SortedSet<Participant> participants = new TreeSet<>();
        for (int i = 0; i < competitionComputationParam.numberOfParticipantCompetition; i++) {
            participants.add(competitionComputationParam.participantType.createParticipant(competitionInstance));
        }
        Sets.sort(participants);

        competitionInstance = updateCompetitionInstanceFor(competitionInstance, participants, competitionComputationParam);

        if (isTest()) {
            test(competitionInstance, competitionComputationParam.numberOfParticipantCompetition, competitionComputationParam.participantType);

        }

        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }

    public void test(CompetitionInstance competitionInstance, int numberOfParticipantCompetition, ParticipantType participantType) throws CompetitionInstanceGeneratorException {
        competitionInstance.reset(true);
        SortedSet<Participant> participants = new TreeSet<>();
        for (int i = 0; i < numberOfParticipantCompetition; i++) {
            Participant participant = participantType.createParticipant(competitionInstance);
            if (i == 0)
                participant.equalityComparison = 0.0;
            participants.add(participant);
        }
        Sets.sort(participants);
        String expected = "0";
        int i = 0;
        for (Participant participant : participants) {
            competitionInstance.subscribe(participant, i < competitionInstance.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? i + 1 : null);
            if (expected.compareTo("0") == 0)
                expected = competitionInstance.participantTeams.iterator().next().localId;
            i++;
        }
        competitionInstance.initialize();
        competitionInstance.open();
        boolean generation = false;
        competitionInstance.launchSimulation(generation);
        String result = competitionInstance.getParticipantCompetitionResults().first().participant.localId;
        assert result == expected || result == expected;

    }

    public CompetitionInstance updateCompetitionInstanceFor(CompetitionInstance competitionInstance, SortedSet<Participant> participants, CompetitionCreationParam competitionCreationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionCreationParam.toString();
        if (competitionInstance.competitionComputationParam.numberOfParticipantCompetition == 2) {
            logPrefix = competitionCreationParam.toString();
        }

        //  logger.log(Level.FINE, "[START] " + logPrefix);
        logCompetitionCreationParamElt(competitionCreationParam, "START");
        int numberOfParticipants = competitionCreationParam.numberOfParticipantCompetition;

        SortedSet<Participant> participantsToSubscribe = new TreeSet<>();
        SortedSet<Participant> participantsLeftToSubscribe = new TreeSet<>();
        if (competitionCreationParam.getFirstPhase().registrationOnTheFly) {
            List<Participant> participantsArray = new ArrayList<>(participants);
            for (int j = 0; j < participants.size() / 2 || (!participantsArray.isEmpty() && competitionCreationParam.getMixingPhaseParameter() != null && participantsToSubscribe.size() <= competitionCreationParam.getMixingPhaseParameter().participantQualifiedPerMatch); j++) {
                participantsToSubscribe.add(participantsArray.get(0));
                participantsArray.remove(0);
            }
            participantsLeftToSubscribe.addAll(participantsArray);
        } else {
            participantsToSubscribe.addAll(participants);
        }
        int i = 0;
        for (Participant participant : participantsToSubscribe) {
            competitionInstance.subscribe(participant, i < competitionInstance.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? i + 1 : null);
            i++;
        }
        if (!competitionCreationParam.getFirstPhase().registrationOnTheFly) {
            assert numberOfParticipants == competitionInstance.participantSeats.size();
            numberOfParticipants = competitionInstance.participantSeats.size();
        } else {
            assert participantsToSubscribe.size() == competitionInstance.participantSeats.size();
        }


        logCompetitionCreationParamElt(competitionCreationParam, "REGISTRATIONS - DONE");

        competitionInstance.initialize();

        logCompetitionCreationParamElt(competitionCreationParam, "INITIALIZATION - DONE");
        CompetitionGroupResult competitionGroupResult = competitionInstance.getCompetitionGroupResultFromRegistrations();
        if (competitionInstance.participantSeats.size() <= competitionCreationParam.getLastPhase().participantQualifiedPerMatch) {
            // IF not enough participants
            competitionInstance = initOneSingleMatch(competitionInstance);
        } else {

// PRE-QUALIFICATION PHASE - USED TO ORDER BY SKILL
            if (competitionCreationParam.getMixingPhaseParameter() != null) {
                //  logger.log(Level.FINE, "initMixingPhase");
                int initialPhaseSize = competitionInstance.competitionPhases.size();
                logCompetitionCreationParamElt(competitionCreationParam, "MIXING PHASE");
                competitionInstance = initMixingPhase(competitionInstance, competitionGroupResult, competitionCreationParam.getMixingPhaseParameter());
                logCompetitionCreationParamElt(competitionCreationParam, "MIXING PHASE - DONE");
//        } else {
//           //  logger.log(Level.FINE, "initEmptyPhase");
//            competitionInstance = initEmptyPhase(competitionInstance, competitionGroupResult, competitionCreationParam);
                int newPhaseSize = competitionInstance.competitionPhases.size();
                boolean mixingCreated = newPhaseSize != initialPhaseSize;
                if (!participantsLeftToSubscribe.isEmpty() && competitionInstance.participantSeats.size() < competitionInstance.competitionComputationParam.numberOfParticipantCompetition) {

                    competitionInstance.competitionPhases.last().reopen();
                    competitionInstance.competitionPhases.last().competitionSeeds.first().competitionGroups.first().expectedParticipantQuantity = competitionCreationParam.numberOfParticipantCompetition;
                    for (Participant participant : participantsLeftToSubscribe) {
                        competitionInstance.subscribe(participant, i < competitionInstance.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? i + 1 : null);
                        i++;
                        if (competitionInstance.participantSeats.size() >= competitionInstance.competitionComputationParam.numberOfParticipantCompetition)
                            break;
                    }
                    boolean generation = true;
                    competitionInstance.continueCompetition(generation);
                    competitionInstance.fillCompetitionMatchLink();
                    competitionInstance.resetParticipantResults();
                }
            }


//
//        if (numberOfParticipants <= maxFinalGroupSize) {
//            //     only final phase !
//            isGroupPhaseNeeded = false;
//            isFinalPhaseNeeded = true;
//        } else if (maxFinalGroupSize == 0 || numberOfParticipants > maxFinalGroupSize) {
//            isGroupPhaseNeeded = true;
//            isFinalPhaseNeeded = maxFinalGroupSize != 0;
//        }
//
//        double numberOfRound = Math.log((double) numberOfParticipants) / Math.log((double) competitionCreationParam.playVersusType.numberOfTeam / (double) competitionCreationParam.participantQualifiedPerMatch);
//        if (!isGroupPhaseNeeded && Math.rint(numberOfRound) != numberOfRound)
//            isGroupPhaseNeeded = true;
//

            if (competitionCreationParam.getQualificationPhaseParameter() != null) {
                //  logger.log(Level.FINE, "initGroupPhase");
                logCompetitionCreationParamElt(competitionCreationParam, "QUALIFICATION PHASE");

                CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = competitionCreationParam.getMixingPhaseParameter();
                CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionCreationParam.getQualificationPhaseParameter();
                CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionCreationParam.getFinalPhaseParameter();
//            PhaseParameter qualificationPhaseParameter = competitionCreationParam.getQualificationPhaseParameter();
//            PhaseParameter finalPhaseParameter = competitionCreationParam.getFinalPhaseParameter();
                competitionInstance = initGroupPhase(competitionInstance, competitionCreationParamPhaseQualification, competitionCreationParamPhaseFinal, competitionCreationParamPhaseMixing);

                logCompetitionCreationParamElt(competitionCreationParam, "QUALIFICATION PHASE - DONE");
            }

            if (competitionCreationParam.getFinalPhaseParameter() != null) {
                logCompetitionCreationParamElt(competitionCreationParam, "FINAL PHASE");
                CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionCreationParam.getFinalPhaseParameter();
                CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionCreationParam.getQualificationPhaseParameter();

                Set<Participant> previousParticipantResultParticipants = competitionInstance.computeLastCompetitionGroupResultsParticipants();
//            List<Participant> previousParticipantResultParticipants = competitionInstance.getLastCompetitionPhaseResultsParticipants();
                int remainingParticipants = competitionInstance.computeRemainingParticipants();
                if (!competitionCreationParamPhaseFinal.participantTypeSplittable && remainingParticipants < competitionCreationParamPhaseFinal.numberOfParticipantMatch + competitionCreationParamPhaseFinal.participantQualifiedPerMatch && TournamentFormat.allowEliminationFormat(competitionCreationParamPhaseFinal.tournamentFormatsAccepted)) {
                    competitionCreationParamPhaseFinal.tournamentFormatsAccepted.removeAll(TournamentFormat.allElimination());
                    if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.isEmpty()) {
                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.SWISS);
                    }

                }
                if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.ANY_ELIMINATION)) {
                    if (competitionCreationParamPhaseQualification != null) {
                        remainingParticipants = Math.min(remainingParticipants, competitionCreationParamPhaseFinal.groupSizeMaximum);
                    }
                    int numberOfBrackets = TournamentFormat.getMaximumBrackets(remainingParticipants, competitionCreationParamPhaseFinal.numberOfParticipantMatch);
                    competitionCreationParamPhaseFinal.tournamentFormatsAccepted.clear();
                    competitionCreationParamPhaseFinal.tournamentFormatsAccepted.add(TournamentFormat.eliminationWithNumberOfBrackets(numberOfBrackets));
                    competitionInstance = initFinalPhase(competitionInstance, competitionCreationParamPhaseFinal);
                } else if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.QUADRUPLE_ELIMINATION) ||
                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.TRIPLE_ELIMINATION) ||
                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.DOUBLE_ELIMINATION) ||
                        competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.SINGLE_ELIMINATION)) {
                    competitionInstance = initEliminationPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification, PhaseType.FINAL);
                } else if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN)
                        && (remainingParticipants < competitionCreationParamPhaseFinal.numberOfParticipantMatch
                        || (competitionCreationParamPhaseFinal.numberOfParticipantMatch <= 4 && competitionCreationParamPhaseFinal.groupSizeMaximum <= 16)
                        || (competitionCreationParamPhaseFinal.numberOfParticipantMatch <= 2 && competitionCreationParamPhaseFinal.groupSizeMaximum <= 32))) {
                    competitionInstance = initRoundRobinFinalPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);
                } else if (competitionCreationParamPhaseFinal.tournamentFormatsAccepted.contains(TournamentFormat.LADDER)) {
                    competitionInstance = initLadderPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);
                } else {
                    competitionInstance = initSwissFinalPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);
                }

                logCompetitionCreationParamElt(competitionCreationParam, "FINAL PHASE - DONE");
            }
        }
        if (competitionInstance != null) {
            competitionInstance.fillExpectedRelativeTime();
            competitionInstance.generationDone();
        }
//        competitionInstance.reset();
//
//       //  logger.log(Level.FINE, "getExpectedGlobalAverageDuration  : " + competitionInstance.getExpectedGlobalAverageDuration());
//       //  logger.log(Level.FINE, "getExpectedGlobalMinimumDuration  : " + competitionInstance.getExpectedGlobalMinimumDuration());
//       //  logger.log(Level.FINE, "getExpectedGlobalMaximumDuration  : " + competitionInstance.getExpectedGlobalMaximumDuration());
//       //  logger.log(Level.FINE, "getExpectedParticipantAveragePlay : " + competitionInstance.getExpectedParticipantAveragePlay());
//       //  logger.log(Level.FINE, "getExpectedParticipantMinimumPlay : " + competitionInstance.getExpectedParticipantMinimumPlay());
//       //  logger.log(Level.FINE, "getExpectedParticipantMaximumPlay : " + competitionInstance.getExpectedParticipantMaximumPlay());

//        competitionInstance.fillCharacteristicDatas();
//        competitionInstance.fillStatistics();
//        competitionInstance.continueCompetition();
//        competitionInstance.fillCompetitionMatchLink();
//        competitionInstance.reset(true);

        //  logger.log(Level.FINE, "[END] " + logPrefix);


        logCompetitionCreationParamElt(competitionCreationParam, "DONE");
        return competitionInstance;
    }

    private CompetitionInstance initOneSingleMatch(CompetitionInstance competitionInstance) throws CompetitionInstanceGeneratorException {
        boolean generation = true;
        int numberOfParticipant = 0;
        SortedSet<Participant> lastCompetitionGroupResultsParticipants = new TreeSet<>();
        for (CompetitionGroupResult lastCompetitionGroupResult : competitionInstance.computeLastCompetitionGroupResults()) {
            for (ParticipantResult participantResult : lastCompetitionGroupResult.participantResults) {
                lastCompetitionGroupResultsParticipants.add(participantResult.getParticipant());
            }

        }
        numberOfParticipant += lastCompetitionGroupResultsParticipants.size();
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionInstance.competitionComputationParam.getLastPhase();
        if (competitionCreationParamPhase.participantTypeSplittable && numberOfParticipant > 0) {

            competitionCreationParamPhase.playVersusType = PlayVersusType.getValueFor(numberOfParticipant, competitionCreationParamPhase.playVersusType.teamSize);
            competitionCreationParamPhase.numberOfParticipantMatch = numberOfParticipant;
            competitionCreationParamPhase.participantQualifiedPerMatch = numberOfParticipant > 1 ? numberOfParticipant - 1 : 1;

//            PlayVersusType playVersusType = competitionCreationParamPhase.playVersusType;
//            int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
//            boolean participantTypeSplittable = !competitionCreationParamPhase.participantTypeSplittable;
            int eliminationLevel = 1;
            Set<CompetitionGroupResult> competitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
            Set<CompetitionPhase> competitionPhasesPrevious = new HashSet<>();
            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
                competitionPhasesPrevious.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase());
            }
            CompetitionPhase competitionPhase = competitionInstance.createCompetitionPhase(competitionPhasesPrevious);
            competitionPhase.competitionCreationParamPhase = competitionCreationParamPhase;
            competitionPhase.phaseType = PhaseType.FINAL;
            competitionInstance.addCompetitionPhase(competitionPhase);

            CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResults, StepType.MAIN);

            int previousParticipantsSize = competitionSeed.getPreviousParticipantResultsSize();
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;
            competitionSeed.filteringUnit = Unit.PERCENTAGE;
            competitionSeed.filteringValue = 100;
            competitionSeed.participantPairingMethod = ParticipantPairingMethod.BASED_ON_PREVIOUS_RESULT;
            competitionSeed.internationalizedLabel.defaultLabel = "Final Phase (" + eliminationLevel + " elimination)";
            int numberOfParticipantOut = 1;
            int participantQualifiedPerMatch = numberOfParticipant - 1;
            boolean participantSplittable = competitionCreationParamPhase.participantTypeSplittable;
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = null;
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
                competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
            }
            CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.ELIMINATION.getCompetitionGroupFormatTree(competitionCreationParamPhase.participantType,
                    competitionCreationParamPhase.numberOfParticipantMatch,
                    competitionCreationParamPhase.playVersusType,
                    numberOfParticipant,
                    numberOfParticipantOut,
                    participantQualifiedPerMatch,
                    eliminationLevel,
                    competitionCreationParamPhase.numberOfPlayMinimum,
                    competitionCreationParamPhase.numberOfPlayMaximum,
                    competitionCreationParamPhase.allowEvenNumberOfPlay,
                    null, null,
                    competitionCreationParamPhaseFinal != null && (competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled),
                    competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold : 0 : 0,
                    competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum : 0 : 0,
                    competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum : 0 : 0,
                    competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled : false,
                    !participantSplittable,
                    participantSplittable);

            createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);


            competitionInstance.resetParticipantResults();
            competitionPhase.initializePhase();
            competitionInstance.continueCompetition(generation);

            competitionInstance.fillCompetitionMatchLink();
            competitionInstance.resetParticipantResults();
        } else {
            competitionInstance = null;
        }
        //  logger.log(Level.FINE, "[END]");
        return competitionInstance;
    }

    public int getNumberOfParticipantsNeededForFinalElimination(int numberOfParticipantMatch, int participantQualifiedPerMatch, PlayVersusType playVersusType, int numberOfRounds) {
        int previousRoundNumberOfParticipant = 0;
        for (int i = 0; i < numberOfRounds; i++) {
            if (previousRoundNumberOfParticipant == 0) {
                previousRoundNumberOfParticipant = numberOfParticipantMatch;
            } else {
                previousRoundNumberOfParticipant = previousRoundNumberOfParticipant * numberOfParticipantMatch / participantQualifiedPerMatch;
            }
        }
        return previousRoundNumberOfParticipant;
    }

    @Override
    public CompetitionInstance createCompetitionInstanceEliminationFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionComputationParam.toString();
        //  logger.log(Level.FINE, "[START]" + logPrefix);
        CompetitionInstance competitionInstance = CompetitionInstance.createInstance();
        competitionInstance.internationalizedLabel.defaultLabel = competitionComputationParam.toString();
        competitionInstance.setCompetitionComputationParam(competitionComputationParam);

        SortedSet<Participant> participants = new TreeSet<>();
        for (int i = 0; i < competitionComputationParam.numberOfParticipantCompetition; i++) {
            participants.add(competitionComputationParam.participantType.createParticipant(competitionInstance));
        }
        competitionInstance = updateCompetitionInstanceEliminationFor(competitionInstance, participants, competitionComputationParam);
        competitionInstance.fillExpectedRelativeTime();

        if (isTest()) {
            test(competitionInstance, competitionComputationParam.numberOfParticipantCompetition, competitionComputationParam.participantType);

        }
        competitionInstance.generationDone();
        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }


    @Override
    public CompetitionInstance createCompetitionInstanceRoundRobinFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionComputationParam.toString();
        //  logger.log(Level.FINE, "[START]" + logPrefix);
        CompetitionInstance competitionInstance = CompetitionInstance.createInstance();
        competitionInstance.internationalizedLabel.defaultLabel = competitionComputationParam.toString();
        competitionInstance.setCompetitionComputationParam(competitionComputationParam);

        SortedSet<Participant> participants = new TreeSet<>();

        for (int i = 0; i < competitionComputationParam.numberOfParticipantCompetition; i++) {
            participants.add(competitionComputationParam.participantType.createParticipant(competitionInstance));
        }

        competitionInstance = updateCompetitionInstanceRoundRobinFor(competitionInstance, participants, competitionComputationParam);
        competitionInstance.fillExpectedRelativeTime();

        if (isTest()) {
            test(competitionInstance, competitionComputationParam.numberOfParticipantCompetition, competitionComputationParam.participantType);

        }
        competitionInstance.generationDone();

        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }

    @Override
    public CompetitionInstance createCompetitionInstanceSwissFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionComputationParam.toString();
        //  logger.log(Level.FINE, "[START]" + logPrefix);
        CompetitionInstance competitionInstance = CompetitionInstance.createInstance();
        competitionInstance.internationalizedLabel.defaultLabel = competitionComputationParam.toString();
        competitionInstance.setCompetitionComputationParam(competitionComputationParam);

        SortedSet<Participant> participants = new TreeSet<>();

        for (int i = 0; i < competitionComputationParam.numberOfParticipantCompetition; i++) {
            participants.add(competitionComputationParam.participantType.createParticipant(competitionInstance));
        }

        competitionInstance = updateCompetitionInstanceSwissFor(competitionInstance, participants, competitionComputationParam);
        competitionInstance.fillExpectedRelativeTime();


        if (isTest()) {
            test(competitionInstance, competitionComputationParam.numberOfParticipantCompetition, competitionComputationParam.participantType);

        }
        competitionInstance.generationDone();
        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }


    public CompetitionInstance updateCompetitionInstanceSwissFor(CompetitionInstance competitionInstance, SortedSet<Participant> participants, CompetitionCreationParam competitionCreationParam) throws CompetitionInstanceGeneratorException {
        return updateCompetitionInstanceFor(competitionInstance, participants, competitionCreationParam, CompetitionGroupFormat.SWISS);
    }

    public CompetitionInstance updateCompetitionInstanceFor(CompetitionInstance competitionInstance, SortedSet<Participant> participants, CompetitionCreationParam competitionCreationParam, CompetitionGroupFormat competitionGroupFormat) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionCreationParam.toString();
        //  logger.log(Level.FINE, "[START] " + logPrefix);


        for (int i = 0; i < participants.size(); i++) {
            competitionInstance.subscribe(competitionCreationParam.participantType.createParticipant(competitionInstance), i < competitionInstance.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? i + 1 : null);
        }

        competitionInstance.initialize();
        CompetitionGroupResult competitionGroupResult = competitionInstance.getCompetitionGroupResultFromRegistrations();


//       //  logger.log(Level.FINE, "initEmptyPhase");
//        competitionInstance = initEmptyPhase(competitionInstance, competitionGroupResult, competitionCreationParam);

        //  logger.log(Level.FINE, "initGroupPhase");
//        competitionCreationParam.minGroupSize = participants.size();
//        competitionCreationParam.maxGroupSize = participants.size();
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = competitionCreationParam.getQualificationPhaseParameter();
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = competitionCreationParam.getFinalPhaseParameter();
        CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing = competitionCreationParam.getMixingPhaseParameter();
        if (competitionCreationParamPhaseQualification != null)
            competitionInstance = initGroupPhase(competitionInstance, competitionCreationParamPhaseQualification, competitionCreationParamPhaseFinal, competitionCreationParamPhaseMixing);
//        PhaseParameter phaseParameterFinal = competitionCreationParam.getFinalPhaseParameter();
        if (competitionCreationParamPhaseFinal != null && TournamentFormat.allowEliminationFormat(competitionCreationParamPhaseFinal.tournamentFormatsAccepted)) {
            int qualified = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, competitionCreationParamPhaseFinal.playVersusType.roundUp(participants.size() * QUALIFICATION_PERCENTAGE / 100), true);
            int nextAcceptableSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, competitionInstance.getCompetitionComputationParam().numberOfParticipantCompetition / 2, true);
            if (nextAcceptableSize * 2 < competitionCreationParam.numberOfParticipantCompetition)
                qualified = nextAcceptableSize;
            if (qualified < competitionCreationParamPhaseFinal.numberOfParticipantMatch * competitionInstance.computeLastCompetitionGroupResults().size()) {
                qualified = CompetitionGroupFormat.ELIMINATION.getNextAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, competitionCreationParamPhaseFinal.numberOfParticipantMatch * competitionInstance.computeLastCompetitionGroupResults().size());
            }
            qualified = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, qualified, true);
            if (qualified > competitionCreationParam.numberOfParticipantCompetition)
                qualified = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, competitionCreationParam.numberOfParticipantCompetition, true);

            int eliminationLevel = (int) Math.ceil(Math.log(qualified));
            //  logger.log(Level.FINE, "initFinalPhase");


            competitionInstance = initEliminationPhase(competitionInstance, competitionCreationParamPhaseFinal, null, PhaseType.FINAL);
        } else if (competitionCreationParamPhaseFinal != null && !competitionCreationParamPhaseFinal.tournamentFormatsAccepted.isEmpty() && competitionGroupFormat.tournamentFormats.contains(competitionCreationParamPhaseFinal.tournamentFormatsAccepted.iterator().next())) {
            competitionInstance = initGroupPhase(competitionInstance, null, competitionCreationParamPhaseFinal, null);
        }
        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }


    public CompetitionInstance updateCompetitionInstanceRoundRobinFor(CompetitionInstance competitionInstance, SortedSet<Participant> participants, CompetitionCreationParam competitionCreationParam) throws CompetitionInstanceGeneratorException {
        return updateCompetitionInstanceFor(competitionInstance, participants, competitionCreationParam, CompetitionGroupFormat.ROUND_ROBIN);
    }


    public CompetitionInstance updateCompetitionInstanceEliminationFor(CompetitionInstance competitionInstance, SortedSet<Participant> participants, CompetitionCreationParam competitionCreationParam) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_createCompetitionInstanceFor;
        String logPrefix = competitionCreationParam.toString();
        //  logger.log(Level.FINE, "[START] " + logPrefix);

        for (int i = 0; i < participants.size(); i++) {
            competitionInstance.subscribe(competitionCreationParam.participantType.createParticipant(competitionInstance), i < competitionInstance.getCompetitionComputationParam().getFirstPhase().participantQualifiedPerMatch + 1 ? i + 1 : null);
        }


        competitionInstance.initialize();
//        CompetitionGroupResult competitionGroupResult = competitionInstance.getCompetitionGroupResultFromRegistrations();

//       //  logger.log(Level.FINE, "initEmptyPhase");
//        competitionInstance = initEmptyPhase(competitionInstance, competitionGroupResult, competitionCreationParam);

        //  logger.log(Level.FINE, "initFinalPhase");
        competitionInstance = initFinalPhase(competitionInstance, competitionCreationParam.getFinalPhaseParameter());
//        competitionInstance.fillCharacteristicDatas();
//        competitionInstance.reset();
        //  logger.log(Level.FINE, "[END] " + logPrefix);
        return competitionInstance;
    }


//    public CompetitionSeed createEmptyPhaseWithoutElimination(CompetitionInstance competitionInstance, CompetitionGroupResult competitionGroupResult, PlayVersusType playVersusType) {
//        // Logger logger = LOGGER_createEmptyPhaseWithoutElimination;
//        //  logger.log(Level.FINE, "[START]");
//        CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(null);
//        competitionSeed.emptyPhase = true;
//        competitionSeed.internationalizedLabel.defaultLabel = "EmptyPhaseWithoutElimination";
//        competitionSeed.participantPairingMethod = ParticipantPairingMethod.BASED_ON_PREVIOUS_RESULT;
//        competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;
//        competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResult);
//        //  logger.log(Level.FINE, "create competitionSeed:" + competitionSeed.toString());
//        competitionGroupResult.competitionSeedNext = competitionSeed;
//
//        CompetitionGroup competitionGroup = competitionInstance.createCompetitionGroup(competitionSeed);
//        competitionInstance.createCompetitionGroupResult(competitionGroup);
//        competitionGroup.round = 1;
//        competitionGroup.competitionGroupFormat = CompetitionGroupFormat.NONE;
//        competitionGroup.internationalizedLabel.defaultLabel = "round " + competitionGroup.round + " / " + competitionGroup.competitionGroupFormat;
//        int competitionMatchQuantity = 0;
//        int preQualificationGroupRoundQuantity = 0;
//
//        //  logger.log(Level.FINE, "create competitionGroup:" + competitionGroup.toString());
//
//        int numberOfPlay = competitionInstance.getCompetitionComputationParam().minNumberOfPlayGlobal;
//        numberOfPlay = numberOfPlay * competitionInstance.getCompetitionComputationParam().numberOfPlayMultiplicator;
//        competitionGroup.createCompetitionGroupRounds(preQualificationGroupRoundQuantity, competitionMatchQuantity, numberOfPlay, playVersusType);
//        //  logger.log(Level.FINE, "[END]");
//        return competitionSeed;
//    }

    public CompetitionPhase createRandomPhaseWithoutElimination(CompetitionInstance competitionInstance, CompetitionGroupResult competitionGroupResult, CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing) throws CompetitionInstanceGeneratorException {

        // Logger logger = LOGGER_createRandomPhaseWithoutElimination;
        //  logger.log(Level.FINE, "[START]");
        CompetitionPhase competitionPhase = null;
        if (TournamentFormat.allowFormat(competitionCreationParamPhaseMixing.tournamentFormatsAccepted, TournamentFormat.LADDER) && competitionCreationParamPhaseMixing.tournamentFormatsAccepted.size() == 1) {
            initLadderPhase(competitionInstance, competitionCreationParamPhaseMixing, null);
            competitionPhase = competitionInstance.competitionPhases.last();
        } else {
            CompetitionSeed competitionSeed = null;
            boolean participantSplittable = competitionCreationParamPhaseMixing.participantTypeSplittable;
//        if (competitionInstance.participantSeats.size() > competitionCreationParam.maxGroupSize)
            CompetitionGroupFormatTree competitionGroupFormatTreeSwiss = null;
            boolean fixedParticipantSize = !competitionCreationParamPhaseMixing.participantTypeSplittable;
            if (competitionCreationParamPhaseMixing.tournamentFormatsAccepted.contains(TournamentFormat.SWISS)) {
                competitionGroupFormatTreeSwiss = CompetitionGroupFormat.SWISS.getCompetitionGroupFormatTree(
                        competitionCreationParamPhaseMixing.participantType, competitionCreationParamPhaseMixing.numberOfParticipantMatch, competitionCreationParamPhaseMixing.playVersusType, competitionInstance.participantSeats.size(), competitionInstance.participantSeats.size(), competitionCreationParamPhaseMixing.participantQualifiedPerMatch, 1, competitionCreationParamPhaseMixing.numberOfPlayMinimum, competitionCreationParamPhaseMixing.numberOfPlayMaximum, competitionCreationParamPhaseMixing.allowEvenNumberOfPlay, competitionCreationParamPhaseMixing.numberOfRoundMinimum, competitionCreationParamPhaseMixing.numberOfRoundMaximum, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
            }
            CompetitionGroupFormatTree competitionGroupFormatTreeRoundRobin = null;
            CompetitionGroupFormatTree competitionGroupFormatTreeRoundRobinInCaseOf = null;
            if (((competitionCreationParamPhaseMixing.playVersusType.numberOfTeam == 2 && competitionInstance.participantSeats.size() < 64) || (competitionCreationParamPhaseMixing.playVersusType.numberOfTeam <= 4 && competitionInstance.participantSeats.size() < competitionCreationParamPhaseMixing.playVersusType.numberOfTeam * 5))) {
                competitionGroupFormatTreeRoundRobinInCaseOf = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(
                        competitionCreationParamPhaseMixing.participantType, competitionCreationParamPhaseMixing.numberOfParticipantMatch, competitionCreationParamPhaseMixing.playVersusType, competitionInstance.participantSeats.size(), competitionInstance.participantSeats.size(), competitionCreationParamPhaseMixing.participantQualifiedPerMatch, 1, competitionCreationParamPhaseMixing.numberOfPlayMinimum, competitionCreationParamPhaseMixing.numberOfPlayMaximum, competitionCreationParamPhaseMixing.allowEvenNumberOfPlay, competitionCreationParamPhaseMixing.numberOfRoundMinimum, competitionCreationParamPhaseMixing.numberOfRoundMaximum, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
                if (competitionCreationParamPhaseMixing.tournamentFormatsAccepted.contains(TournamentFormat.ROUND_ROBIN)) {
                    competitionGroupFormatTreeRoundRobin = competitionGroupFormatTreeRoundRobinInCaseOf;
                }
            }
            CompetitionGroupFormatTree competitionGroupFormatTree = null;
            if (competitionGroupFormatTreeSwiss != null) {
                competitionGroupFormatTree = competitionGroupFormatTreeSwiss;
                if (competitionGroupFormatTreeRoundRobin != null && (Math.abs(competitionGroupFormatTreeRoundRobin.getCompetitionRoundQuantityMaximum() - competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum()) <= 2)) {
                    competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                } else if (competitionGroupFormatTreeRoundRobinInCaseOf != null && (Math.abs(competitionGroupFormatTreeRoundRobinInCaseOf.getCompetitionRoundQuantityMaximum() - competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum()) <= 1)) {
                    competitionGroupFormatTree = competitionGroupFormatTreeRoundRobinInCaseOf;
                }
            } else if (competitionGroupFormatTreeRoundRobin != null) {
                competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
            }
            if (competitionGroupFormatTree != null) {
//        else
//            competitionGroupFormatTree = CompetitionGroupFormat.SWISS.getCompetitionGroupFormatTree(
//                    competitionCreationParam.participantType, competitionCreationParam.numberOfParticipantMatch, competitionCreationParam.playVersusType, competitionInstance.participantSeats.size(), competitionInstance.participantSeats.size() / 2, competitionCreationParam.participantQualifiedPerMatch, 1, 1, 1);

                if (!competitionGroupFormatTree.competitionGroupFormatTreeGroups.isEmpty()) {
                    Set<CompetitionGroupResult> competitionGroupResults = new HashSet<>();
                    Set<CompetitionPhase> competitionPhasesPrevious = new HashSet<>();
                    if (competitionGroupResult != null) {
                        competitionGroupResults.add(competitionGroupResult);
                        competitionPhasesPrevious.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase());
                    }
                    competitionPhase = competitionInstance.createCompetitionPhase(competitionPhasesPrevious);
                    competitionInstance.addCompetitionPhase(competitionPhase);
                    competitionPhase.competitionCreationParamPhase = competitionCreationParamPhaseMixing;
                    competitionPhase.phaseType = PhaseType.TRAINING;
                    competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResults, StepType.STANDALONE);
                    competitionSeed.internationalizedLabel.defaultLabel = "RandomPhaseWithoutElimination";
                    competitionSeed.participantPairingMethod = ParticipantPairingMethod.RANDOM;
                    competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;

                    int previousParticipantResultsSize = competitionSeed.getPreviousParticipantResultsSize();
                    competitionSeed.participantQuantity = competitionSeed.participantFilteringMethod.getFilteredParticipantSize(competitionCreationParamPhaseMixing.numberOfParticipantMatch, competitionCreationParamPhaseMixing.playVersusType, previousParticipantResultsSize, competitionSeed.filteringValue, competitionSeed.filteringUnit);
//            competitionSeed.competitionInstance = competitionInstance;
//            competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResult);
                    //  logger.log(Level.FINE, "create competitionSeed:" + competitionSeed.toString());
//            competitionGroupResult.competitionSeedNext = competitionSeed;

                    createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);
                    competitionPhase.initializePhase();
                }
                boolean generation = true;
                competitionInstance.continueCompetition(generation);
                //  logger.log(Level.FINE, "competitionSeed.open()");
//            competitionPhase.open();
//       //  logger.log(Level.FINE, "competitionSeed.initializeCompetitionGroups()");
//        competitionSeed.initializeCompetitionGroups();
                //  logger.log(Level.FINE, "competitionInstance.launchSimulation()");
//            competitionInstance.launchSimulation();
                //  logger.log(Level.FINE, "Duration:\t" + competitionInstance.getExpectedGlobalAverageDuration().toString());
                competitionInstance.fillCompetitionMatchLink();
                competitionInstance.resetParticipantResults();
            }
        }
        //  logger.log(Level.FINE, "[END]");
        return competitionPhase;

    }

//    private int getUniqueParticipantSizeFromCompetitionGroupResults(Set<CompetitionGroupResult> competitionGroupResults) {
//        Set<Participant> previousParticipants = new TreeSet<>();
//        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
//            for (ParticipantResult participantResult : competitionGroupResult.getParticipantResultsAsArray()) {
//                if (participantResult.participant != null)
//                    previousParticipants.add(participantResult.participant);
//            }
//
//        }
//        return previousParticipants.size();
//    }


    public int getQuantityOfQualificationGroupStep(PlayVersusType playVersusType, int numberOfParticipant, int numberOfParticipantQualified, int qualificationPercentage) {
        int numberOfQualificationLevel = 0;
        if (numberOfParticipantQualified == 0) {
            numberOfQualificationLevel = 1;
        } else {
            int remainingParticipant = playVersusType.roundUp(numberOfParticipant);
            numberOfQualificationLevel = 1;
            for (int i = 0; remainingParticipant > numberOfParticipantQualified; i++) {

                remainingParticipant = BigDecimal.valueOf(remainingParticipant).multiply(BigDecimal.valueOf(qualificationPercentage)).divide(BigDecimal.valueOf(100), RoundingMode.UP).setScale(0, RoundingMode.UP).intValue();
//                remainingParticipant = (int) Math.round(Math.ceil(qualified));
//                remainingParticipant = playVersusType.roundUp((int) Math.ceil(qualified));
                if (remainingParticipant > numberOfParticipantQualified)
                    numberOfQualificationLevel++;
            }
        }
        return numberOfQualificationLevel;
    }

    public CompetitionPhase initCompetitionGroupPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhase competitionCreationParamPhase, Set<CompetitionGroupResult> competitionGroupResultsPrevious, Integer previousParticipantResultsSize, int qualificationGroupStep, ParticipantFilteringMethod participantFilteringMethod, Unit filteringUnit, int filteringValue, CompetitionGroupFormatTree competitionGroupFormatTree) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_initCompetitionGroupPhase;
        //  logger.log(Level.FINE, "[START]");
        Set<CompetitionPhase> competitionPhasesPrevious = new HashSet<>();
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResultsPrevious) {
            competitionPhasesPrevious.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase());
        }
        CompetitionPhase competitionPhase = competitionInstance.createCompetitionPhase(competitionPhasesPrevious);
        competitionPhase.competitionCreationParamPhase = competitionCreationParamPhase;
        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseMixing)
            competitionPhase.phaseType = PhaseType.TRAINING;
        else if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification)
            competitionPhase.phaseType = PhaseType.GROUP;
        else if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal)
            competitionPhase.phaseType = PhaseType.FINAL;
        competitionInstance.addCompetitionPhase(competitionPhase);

        CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResultsPrevious, StepType.STANDALONE);
        competitionSeed.internationalizedLabel.defaultLabel = "CompetitionGroupPhase  " + qualificationGroupStep;
////        competitionSeed.competitionInstance = competitionInstance;
//        competitionSeed.stepType = StepType.STANDALONE;
//        competitionSeed.phaseType = PhaseType.GROUP;
        competitionSeed.participantPairingMethod = ParticipantPairingMethod.TOP_VS_MIDDLE;
        competitionSeed.participantFilteringMethod = participantFilteringMethod;
        competitionSeed.filteringUnit = filteringUnit;
        competitionSeed.filteringValue = filteringValue;
        if (previousParticipantResultsSize == null)
            previousParticipantResultsSize = competitionSeed.getPreviousParticipantResultsSize();
        competitionSeed.participantQuantity = competitionSeed.participantFilteringMethod.getFilteredParticipantSize(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, previousParticipantResultsSize, competitionSeed.filteringValue, competitionSeed.filteringUnit);

//        competitionSeed.competitionCreationParamPhase = competitionCreationParamPhase;
//        if (competitionSeed.competitionCreationParamPhase != null)
//            competitionSeed.setPhase(competitionSeed.competitionCreationParamPhase.phaseIndex);

        //  logger.log(Level.FINE, "create competitionSeed:" + competitionSeed.toString());

//        for (CompetitionGroupResult competitionGroupResultPrevious : competitionGroupResultsPrevious) {
//            competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResultPrevious);
//            competitionGroupResultPrevious.competitionSeedNext = competitionSeed;
//        }

        createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);
        competitionPhase.initializePhase();
        //  logger.log(Level.FINE, "competitionInstance.open()");
        boolean generation = true;
        competitionInstance.continueCompetition(generation);
        //  logger.log(Level.FINE, "competitionSeed.open()");
//        competitionPhase.open();
//       //  logger.log(Level.FINE, "competitionSeed.initializeCompetitionGroups()");
//        competitionSeed.initializeCompetitionGroups();
        //  logger.log(Level.FINE, "competitionInstance.launchSimulation()");
//        competitionInstance.launchSimulation();
        //  logger.log(Level.FINE, "Duration:\t" + competitionInstance.getExpectedGlobalAverageDuration().toString());
        competitionInstance.fillCompetitionMatchLink();
        competitionInstance.resetParticipantResults();

        //  logger.log(Level.FINE, "[END]");
        return competitionPhase;
    }

//    public  CompetitionInstance initEmptyPhase(CompetitionInstance competitionInstance, CompetitionGroupResult competitionGroupResult, CompetitionCreationParam competitionCreationParam) {
//        // Logger logger = LOGGER_initEmptyPhase;
//        PlayVersusType playVersusType = competitionCreationParam.playVersusType;
//       //  logger.log(Level.FINE, "[START]");
//        CompetitionSeed competitionSeed = createEmptyPhaseWithoutElimination(competitionInstance, competitionGroupResult, playVersusType);
//        competitionInstance.setCompetitionSeed(competitionSeed);
//       //  logger.log(Level.FINE, "competitionInstance.open()");
//        competitionInstance.open();
//       //  logger.log(Level.FINE, "competitionSeed.open()");
//        competitionSeed.open();
////       //  logger.log(Level.FINE, "competitionSeed.initializeCompetitionGroups()");
//        competitionSeed.close();
//       //  logger.log(Level.FINE, "competitionInstance.launchSimulation()");
//        competitionInstance.launchSimulation();
//        competitionInstance.resetParticipantResults();
//       //  logger.log(Level.FINE, "[END]");
//        return competitionInstance;
//    }


    public CompetitionInstance initFinalPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal) throws CompetitionInstanceGeneratorException {
        return initEliminationPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionInstance.getCompetitionComputationParam().getQualificationPhaseParameter(), PhaseType.FINAL);
    }

    public CompetitionInstance initEliminationPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification, PhaseType phaseType) throws CompetitionInstanceGeneratorException {

        // Logger logger = LOGGER_initFinalPhase;
        //  logger.log(Level.FINE, "[START]");
        boolean generation = true;
        competitionInstance.continueCompetition(generation);
        PlayVersusType playVersusType = competitionCreationParamPhaseFinal.playVersusType;
        int numberOfParticipantMatch = competitionCreationParamPhaseFinal.numberOfParticipantMatch;
        boolean fixedParticipantSize = !competitionCreationParamPhaseFinal.participantTypeSplittable;
        SortedSet<Participant> lastCompetitionGroupResultsParticipants = new TreeSet<>();
        int numberOfParticipant = 0;
        for (CompetitionGroupResult lastCompetitionGroupResult : competitionInstance.computeLastCompetitionGroupResults()) {
            for (ParticipantResult participantResult : lastCompetitionGroupResult.participantResults) {
                lastCompetitionGroupResultsParticipants.add(participantResult.getParticipant());
            }

        }
        numberOfParticipant += lastCompetitionGroupResultsParticipants.size();

        int eliminationLevel = competitionCreationParamPhaseFinal.getEliminationLevelMaximum();
//        eliminationLevel = Math.min(phaseParameter.eliminationLevelMaximum, eliminationLevel);


        // -> double elimination ?
        Set<CompetitionGroupResult> competitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
        //  logger.log(Level.FINE, "competitionGroupResults.size():" + competitionGroupResults.size());
        competitionInstance.launchSimulation(generation);


        Set<CompetitionPhase> competitionPhasesPrevious = new HashSet<>();
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionPhasesPrevious.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase());
        }
        CompetitionPhase competitionPhase = competitionInstance.createCompetitionPhase(competitionPhasesPrevious);
        competitionPhase.competitionCreationParamPhase = competitionCreationParamPhaseFinal;
        competitionPhase.phaseType = phaseType;
        competitionInstance.addCompetitionPhase(competitionPhase);

        CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResults, StepType.MAIN);

        int previousParticipantsSize = competitionSeed.getPreviousParticipantResultsSize();
        if (competitionCreationParamPhaseQualification == null || (competitionSeed.competitionPhase.previousCompetitionPhases.size() == 1 && competitionSeed.competitionPhase.previousCompetitionPhases.iterator().next().phaseType.compareTo(PhaseType.SEEDING) == 0)) {
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;
            competitionSeed.filteringUnit = Unit.ABSOLUTE;
            competitionSeed.filteringValue = competitionInstance.getCompetitionComputationParam().numberOfParticipantCompetition;
            competitionSeed.participantQuantity = competitionSeed.filteringValue;
        } else {
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
            competitionSeed.filteringUnit = Unit.ABSOLUTE;

            int filteredParticipantSize = competitionSeed.participantFilteringMethod.getFilteredParticipantSize(numberOfParticipantMatch, playVersusType, numberOfParticipant, QUALIFICATION_PERCENTAGE, Unit.PERCENTAGE);
            int qualifiedParticipantsSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, filteredParticipantSize, false);
            competitionSeed.filteringValue = qualifiedParticipantsSize;
            competitionSeed.participantQuantity = competitionSeed.filteringValue;
            //
//            int nextAcceptableSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, previousParticipantsSize / 2, true);
//            if (nextAcceptableSize * 2 < numberOfParticipant)
//                qualifiedParticipantsSize = nextAcceptableSize;
//            if (qualifiedParticipantsSize < competitionCreationParamPhaseFinal.numberOfParticipantMatch * competitionGroupResults.size()) {
//                qualifiedParticipantsSize = CompetitionGroupFormat.ELIMINATION.getNextAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, competitionCreationParamPhaseFinal.numberOfParticipantMatch * competitionGroupResults.size());
//            }
//            qualifiedParticipantsSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, qualifiedParticipantsSize, true);
//            if (qualifiedParticipantsSize > numberOfParticipant)
//                qualifiedParticipantsSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, previousParticipantsSize, true);
//            competitionSeed.filteringValue = qualifiedParticipantsSize;
        }
//        int currentMaxFinalGroupSize = CompetitionGroupFormat.ELIMINATION.getClosestAcceptableSize(numberOfParticipantMatch, phaseParameter.participantQualifiedPerMatch, competitionGroupResults.size());
//        if (currentMaxFinalGroupSize > maxFinalGroupSize)
//            competitionSeed.filteringValue = currentMaxFinalGroupSize;
        competitionSeed.participantPairingMethod = ParticipantPairingMethod.TOP_VS_MIDDLE;
//        competitionSeed.competitionInstance = competitionInstance;
//        int numberOfParticipantIn = 0;
//        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
//            competitionGroupResult.competitionSeedNext = competitionSeed;
//            competitionSeed.competitionGroupResultsPrevious.add(competitionGroupResult);
//        }
        if (competitionSeed.isPreviousCompetitionGroupSeedEmpty())
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;

        //  logger.log(Level.FINE, "create competitionSeed:" + competitionSeed.toString());
//        CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.getCompetitionPhase().competitionCreationParamPhase;
//        int previousParticipantResultsSize = competitionSeed.getPreviousParticipantResultsSize();
//
//        competitionSeed.participantQuantity = competitionSeed.participantFilteringMethod.getFilteredParticipantSize(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, previousParticipantResultsSize, competitionSeed.filteringValue, competitionSeed.filteringUnit);
//        //  logger.log(Level.FINE, "numberOfParticipantIn:" + numberOfParticipantIn);
        int quantityPairing = competitionSeed.getQuantityPairing();
        //  logger.log(Level.FINE, "quantityPairing:" + quantityPairing);
        if (quantityPairing <= 1)
            eliminationLevel = 1;
        if (eliminationLevel < competitionCreationParamPhaseFinal.getEliminationLevelMinimum())
            eliminationLevel = competitionCreationParamPhaseFinal.getEliminationLevelMinimum();
        if (quantityPairing <= 2 && numberOfParticipant % numberOfParticipantMatch != 0 && competitionCreationParamPhaseFinal.getEliminationLevelMaximum() > 1) {
            eliminationLevel = 2;
        }
        competitionSeed.internationalizedLabel.defaultLabel = "Final Phase (" + eliminationLevel + " elimination)";
        //  logger.log(Level.FINE, "competitionSeed.internationalizedLabel.defaultLabel:" + competitionSeed.internationalizedLabel.defaultLabel);
//        int minNumberOfPlay = 0;
//        int maxNumberOfPlay = 0;
//        minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum;
//        while (competitionCreationParamPhaseFinal.getAveragePlayDuration().multipliedBy(minNumberOfPlay).compareTo(Duration.ofMinutes(15)) < 0)
//            minNumberOfPlay = minNumberOfPlay + 2;
//
//        int previousMinPlay = 0;
//        int previousMaxPlay = 0;
//        if (!competitionGroupResults.isEmpty()) {
//            minNumberOfPlay = 0;
//            for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
//                if (competitionGroupResult.competitionGroup != null) {
//                    previousMinPlay = competitionGroupResult.getCompetitionGroup().getMinNumberOfPlay();
//                    previousMaxPlay = competitionGroupResult.getCompetitionGroup().getMaxNumberOfPlay();
//                    minNumberOfPlay = previousMinPlay + previousMaxPlay;
//                }
//            }
//            minNumberOfPlay = minNumberOfPlay / (2 * competitionGroupResults.size());
//        }
//        if (minNumberOfPlay % 2 == 0)
//            minNumberOfPlay--;
//        if (minNumberOfPlay < previousMinPlay)
//            minNumberOfPlay = previousMinPlay;
//
//        if (minNumberOfPlay < competitionCreationParamPhaseFinal.numberOfPlayMinimum)
//            minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum;
//
//        if (minNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayMaximum)
//            minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
//
//
//        maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
//
//
//        if (eliminationLevel > 1) {
//            maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum - 2;
//        }
//        if (maxNumberOfPlay < minNumberOfPlay)
//            maxNumberOfPlay = minNumberOfPlay;
//
//        if (maxNumberOfPlay < competitionCreationParamPhaseFinal.numberOfPlayMinimum)
//            maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum;
//
//        if (maxNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayMaximum)
//            maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
//
//        while (competitionCreationParamPhaseFinal.getAveragePlayDuration().multipliedBy(maxNumberOfPlay).compareTo(Duration.ofMinutes(15)) < 0 && maxNumberOfPlay + 2 <= competitionCreationParamPhaseFinal.numberOfPlayMaximum)
//            maxNumberOfPlay = maxNumberOfPlay + 2;
//        int numberOfParticipantOut = 1;
//        boolean minNumberOfPlayUp = false;
//        while (maxNumberOfPlay / minNumberOfPlay > 2) {
//            if (minNumberOfPlay == 1) {
//                minNumberOfPlay = minNumberOfPlay + 2;
//                minNumberOfPlayUp = true;
//            } else {
//                if (minNumberOfPlayUp) {
//                    maxNumberOfPlay = maxNumberOfPlay - 2;
//                    minNumberOfPlayUp = false;
//                } else {
//                    minNumberOfPlay = minNumberOfPlay + 2;
//                }
//            }
//        }
//
//        if (maxNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayMaximum)
//            maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
//        if (minNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayMaximum)
//            minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
//
//        if (minNumberOfPlay < competitionCreationParamPhaseFinal.numberOfPlayMinimum)
//            minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum;
//        if (maxNumberOfPlay < competitionCreationParamPhaseFinal.numberOfPlayMinimum)
//            maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum;
//

        int numberOfParticipantOut = 1;
        boolean thirdPlaceMatchEnabled = competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled;
        if (eliminationLevel > 1) {
            thirdPlaceMatchEnabled = false;
        }
        competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled = thirdPlaceMatchEnabled;
        boolean participantSplittable = competitionCreationParamPhaseFinal.participantTypeSplittable;
        CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.ELIMINATION.getCompetitionGroupFormatTree(competitionCreationParamPhaseFinal.participantType, competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.playVersusType, competitionSeed.participantQuantity, numberOfParticipantOut, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, eliminationLevel, competitionCreationParamPhaseFinal.numberOfPlayMinimum, competitionCreationParamPhaseFinal.numberOfPlayMaximum, competitionCreationParamPhaseFinal.allowEvenNumberOfPlay, null, null, competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled, competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum : 0, thirdPlaceMatchEnabled, fixedParticipantSize, participantSplittable);

        createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);


        competitionInstance.resetParticipantResults();
        //  logger.log(Level.FINE, "competitionInstance.open()");
        competitionPhase.initializePhase();
        if (competitionInstance.competitionComputationParam.numberOfParticipantCompetition == 2) {
            competitionInstance.continueCompetition(generation);
        } else {
            competitionInstance.continueCompetition(generation);
        }

        //  logger.log(Level.FINE, "competitionSeed.open()");
//        competitionPhase.open();
//        competitionSeed.initializeCompetitionGroups();
        //  logger.log(Level.FINE, "competitionInstance.launchSimulation()");
//        competitionInstance.launchSimulation();
        competitionInstance.fillCompetitionMatchLink();
        competitionInstance.resetParticipantResults();
        if (competitionSeed.competitionGroups.size() == 1) {
//            CompetitionGroup competitionGroup =            competitionSeed.getCompetitionGroups().first();
//
//            for (:
//                 ) {
//
//            }
//            int remainingParticipant = (competitionPhase.getCompetitionSeeds().first().getCompetitionGroups().size() - competitionPhase.getCompetitionSeeds().size()) * competitionCreationParamPhaseFinal.numberOfParticipantMatch;
//            if (competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null && remainingParticipant <= competitionCreationParamPhaseFinal.groupSizeFinalThreshold) {
//
//            }

        } else if (competitionSeed.competitionGroups.size() > 1 && (eliminationLevel > 1 && competitionCreationParamPhaseFinal.mergePolicy != null && competitionCreationParamPhaseFinal.mergePolicy.compareTo(MergePolicy.NO_MERGE) != 0)) {
            //  logger.log(Level.FINE, "competitionSeed.competitionGroupsCache.size() > 1 / merging brackets");
            initEliminationPhaseMerges(competitionInstance, competitionSeed, eliminationLevel, competitionCreationParamPhaseFinal, phaseType);

        }

        //  logger.log(Level.FINE, "[END]");
        return competitionInstance;
    }

    public void initEliminationPhaseMerges(CompetitionInstance competitionInstance, CompetitionSeed competitionSeed, int eliminationLevel, CompetitionCreationParamPhase competitionCreationParamPhase, PhaseType phaseType) throws CompetitionInstanceGeneratorException {
        if (eliminationLevel > 1) {
            SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>();
            competitionGroups.addAll(competitionSeed.getCompetitionGroups());
            SortedSet<CompetitionGroup> competitionGroupsToMerge = new TreeSet<>();
            competitionGroupsToMerge.addAll(competitionSeed.getCompetitionGroups());
            CompetitionGroup competitionGroup = null;
            int regroupLevel = 0;
            int competitionGroupsSize = competitionGroups.size();
            ResetPolicy resetPolicy = null;
            int numberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;
            int numberOfPlayMax = competitionCreationParamPhase.numberOfPlayMaximum;
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {

                CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
                resetPolicy = competitionCreationParamPhaseFinal.resetPolicy;
                if (competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled) {
                    numberOfPlayMax = competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum;
                    if (competitionGroups.size() == 2) {
                        numberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum;
                    } else {
                        numberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum + (competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum - competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum) * 100 * (competitionGroupsSize - competitionGroups.size()) / competitionGroupsSize / 100;
                    }
                } else {
                    numberOfPlayMax = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
                    if (competitionGroups.size() == 2) {
                        numberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMaximum;
                    } else {
                        numberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayMinimum + (competitionCreationParamPhaseFinal.numberOfPlayMaximum - competitionCreationParamPhaseFinal.numberOfPlayMinimum) * 100 * (competitionGroupsSize - competitionGroups.size()) / competitionGroupsSize / 100;
                    }
                }
            }
            while (competitionGroups.size() > 1) {
//                if (competitionGroupsCache.size() > 2)
//                    numberOfPlay = (numberOfPlayMaximum - eliminationLevel + regroupLevel + 1);
//                else
//                    numberOfPlay = numberOfPlayMaximum;
//                if (numberOfPlay > numberOfPlayMaximum)
//                    numberOfPlay = numberOfPlayMaximum;
//                if (numberOfPlay < numberOfPlayMinimum)
//                    numberOfPlay = numberOfPlayMinimum;


                competitionGroupsToMerge.clear();
                int mergingSize = competitionCreationParamPhase.numberOfParticipantMatch / competitionCreationParamPhase.participantQualifiedPerMatch;
                if (competitionCreationParamPhase.numberOfParticipantMatch == 1 && competitionGroups.size() > 1)
                    mergingSize = 2;
                for (int i = 0; i < mergingSize; i++) {
                    if (competitionGroups.size() > 0) {
                        competitionGroupsToMerge.add(competitionGroups.last());
                        competitionGroups.remove(competitionGroups.last());
                    }
                }
//                int currentNumberOfPlay = numberOfPlay * competitionCreationParamPhaseFinal.numberOfPlayMultiplicator;
                //  logger.log(Level.FINE, "currentNumberOfPlay :" + currentNumberOfPlay);
                boolean qualified = true;
                competitionGroup = mergeCompetitionGroup(competitionInstance, competitionGroupsToMerge, qualified, competitionCreationParamPhase, numberOfPlay, numberOfPlayMax, phaseType).getCompetitionGroups().first();
                competitionGroups.add(competitionGroup);

            }

            if (eliminationLevel > 1 && resetPolicy != null && resetPolicy.compareTo(ResetPolicy.NONE) != 0) {
                competitionGroup.getCompetitionSeed().reset(true);
                CompetitionSeed competitionSeedReset = createFinalReset(competitionGroup.getCompetitionSeed());
                competitionInstance.resetParticipantResults();
                competitionInstance.competitionPhase.initializePhase();
                competitionInstance.competitionPhase.continueCompetition();
                competitionInstance.launchSimulation(true);
                competitionInstance.fillCompetitionMatchLink();
//                competitionInstance.fillExpectedRelativeTime();
            }
        }
    }


    public CompetitionSeed createFinalReset(CompetitionSeed competitionSeed) {
        CompetitionMatch competitionMatchFirstFinal = competitionSeed.getCompetitionGroupForLane(1).getCompetitionRoundForRound(1).getCompetitionMatches().iterator().next();
        CompetitionSeed competitionSeedReset = competitionSeed.createCompetitionSeed(competitionSeed.competitionPhase, competitionSeed.getAllCompetitionGroupResults(), StepType.RESET);
//        competitionSeed.setChanged();
        competitionSeedReset.internationalizedLabel.defaultLabel = "bracket.reset";
//        competitionSeedReset.round = competitionSeed.round + 1;

        competitionSeedReset.participantFilteringMethod = ParticipantFilteringMethod.ALL;
        competitionSeedReset.participantPairingMethod = ParticipantPairingMethod.BASED_ON_PREVIOUS_RESULT;
        competitionSeedReset.filteringUnit = Unit.PERCENTAGE;
        competitionSeedReset.filteringValue = 100;
        SortedSet<CompetitionGroup> competitionGroups = competitionSeed.getCompetitionGroups();
        Sets.sort(competitionGroups);
        competitionGroups.first().getCompetitionGroupResult().competitionSeedNext = competitionSeedReset;
        competitionSeedReset.previousCompetitionSeeds.add(competitionSeed);
//        competitionSeedReset.competitionPhase = competitionSeed.competitionPhase;
        CompetitionGroup competitionGroup = competitionSeed.createCompetitionGroup(competitionSeedReset);
//        competitionSeed.notifyObservers(competitionGroup);
        CompetitionGroupResult competitionGroupResult = competitionSeed.createCompetitionGroupResult(competitionGroup);
//        competitionSeed.notifyObservers(competitionGroupResult);
        competitionGroup.competitionGroupFormat = CompetitionGroupFormat.ELIMINATION;
        CompetitionRound competitionRound = competitionSeed.createCompetitionRound(competitionGroup, null);
//        competitionRound.beforeDependenciesCompetitionRounds.add(competitionMatchFirstFinal.competitionRound);
//        competitionMatchFirstFinal.competitionRound.afterDependenciesCompetitionRounds.add(competitionRound);
//        competitionSeed.notifyObservers(competitionRound);
        CompetitionMatch competitionMatch = competitionSeed.createCompetitionMatch(competitionRound);
        competitionMatch.addPreviousCompetitionMatch(competitionMatchFirstFinal, 1, null, 1, null);
        competitionMatch.addPreviousCompetitionMatch(competitionMatchFirstFinal, 2, null, 2, null);
        competitionMatch.participantQuantity = competitionMatchFirstFinal.participantQuantity;
//                    competitionMatch.competitionRound = competitionRound;
//                    competitionMatch.round = matchLane;
        competitionMatch.internationalizedLabel.defaultLabel = "matchDetails." + competitionMatch.lane;
//        competitionSeed.notifyObservers(competitionMatch);
        int numberOfPlays = competitionMatchFirstFinal.getCompetitionPlays().size();
        for (int i = 0; i < numberOfPlays; i++) {
            CompetitionPlay competitionPlay = competitionSeed.createCompetitionPlay(competitionMatch);
            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeedReset.competitionPhase.competitionCreationParamPhase;
//            if (competitionCreationParamPhase == null) {
//                competitionCreationParamPhase = competitionSeedReset.competitionPhase.competitionCreationParamPhase;
//            }
            competitionPlay.playVersusType = competitionCreationParamPhase.playVersusType;
            competitionPlay.round = i + 1;
            competitionPlay.internationalizedLabel.defaultLabel = "playDetails." + competitionPlay.round;
//            competitionSeed.notifyObservers(competitionPlay);
        }
        int previousParticipantResultsSize = competitionSeed.getPreviousParticipantResultsSize();
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.getCompetitionPhase().competitionCreationParamPhase;
        competitionSeedReset.participantQuantity = competitionSeed.participantFilteringMethod.getFilteredParticipantSize(competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, previousParticipantResultsSize, competitionSeed.filteringValue, competitionSeed.filteringUnit);

        return competitionSeedReset;
//        fillCompetitionMatchLink();
    }


    public CompetitionSeed createCompetitionGroupsFromCompetitionGroupFormatTree(CompetitionInstance competitionInstance, CompetitionSeed competitionSeed, CompetitionGroupFormatTree competitionGroupFormatTree) {
        return createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree, null);
    }

    public CompetitionSeed createCompetitionGroupsFromCompetitionGroupFormatTree(CompetitionInstance competitionInstance, CompetitionSeed competitionSeed, CompetitionGroupFormatTree competitionGroupFormatTree, Set<CompetitionGroup> competitionGroupsMerged) {
        // Logger logger = LOGGER_createCompetitionGroupsFromCompetitionGroupFormatTree;
        SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>();
//        int groupLane = 1;
        for (CompetitionGroupFormatTreeGroup competitionGroupFormatTreeGroup : competitionGroupFormatTree.competitionGroupFormatTreeGroups) {
            CompetitionGroup competitionGroup = competitionInstance.createCompetitionGroup(competitionSeed);
            competitionGroup.expectedParticipantQuantity = competitionGroupFormatTreeGroup.participantQuantity;
            //  logger.log(Level.FINE, "Group(" + competitionGroupFormatTree.competitionGroupFormat + ")[" + competitionGroup.round + "]");
            boolean thirdPlaceGroup = competitionSeed.getCompetitionPhase().competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal && ((CompetitionCreationParamPhaseFinal) competitionSeed.getCompetitionPhase().competitionCreationParamPhase).thirdPlaceMatchEnabled;
            if (competitionGroupFormatTree.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
                if (competitionGroupsMerged != null && competitionGroupsMerged.size() > 1) {
                    competitionGroup.internationalizedLabel.defaultLabel = "bracket.merge ";
                    for (CompetitionGroup competitionGroupMerged : competitionGroupsMerged) {
                        competitionGroup.internationalizedLabel.defaultLabel += competitionGroupMerged.internationalizedLabel.defaultLabel + " ";
                    }
                    competitionSeed.internationalizedLabel.defaultLabel = competitionGroup.internationalizedLabel.defaultLabel;
                } else if (competitionGroup.lane == 1) {
                    if (competitionGroupFormatTree.competitionGroupFormatTreeGroups.size() > 1) {
                        competitionGroup.internationalizedLabel.defaultLabel = "bracket.winner";
                    } else {
                        competitionGroup.internationalizedLabel.defaultLabel = "bracket.main";
                    }
                } else {
                    if (thirdPlaceGroup) {
                        competitionGroup.internationalizedLabel.defaultLabel = "bracket.third_place";
                    } else {
                        competitionGroup.internationalizedLabel.defaultLabel = "bracket.looser." + competitionGroup.lane;
                    }
                }
            } else {
                competitionGroup.internationalizedLabel.defaultLabel = "groupDetails." + competitionGroup.lane;
            }
            competitionGroup.competitionGroupFormat = competitionGroupFormatTree.competitionGroupFormat;
            competitionInstance.createCompetitionGroupResult(competitionGroup);

            //  logger.log(Level.FINE, "create competitionGroup:" + competitionGroup.toString());

            CompetitionRound competitionRoundPrevious = null;
            CompetitionRound competitionRound = null;
//            int round = 1;
            //  logger.log(Level.FINE, "Round Quantity:" + competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds.size());
            for (CompetitionGroupFormatTreeRound competitionGroupFormatTreeRound : competitionGroupFormatTreeGroup.competitionGroupFormatTreeRounds) {
                if (competitionRound != null)
                    competitionRoundPrevious = competitionRound;
//                if (thirdPlaceGroup && competitionGroupFormatTreeGroup.lane > 1) {
//                    competitionRoundPrevious = ((SortedSet<CompetitionRound>) Sets.sort(competitionSeed.competitionGroups.first().competitionRounds)).last().competitionRoundPrevious;
//                }
                competitionRound = competitionInstance.createCompetitionRound(competitionGroup, competitionRoundPrevious);
//                competitionRound.round = round;
                competitionRound.internationalizedLabel.defaultLabel = "roundDetails." + competitionRound.round;
//                competitionRound.competitionGroup = competitionGroup;
                //  logger.log(Level.FINE, "create competitionRound:" + competitionRound.toString());
                if (competitionGroupFormatTreeRound.competitionGroupFormatTreeMatches != null) {
                    CompetitionMatch competitionMatch = null;
                    //  logger.log(Level.FINE, "Match Quantity:" + competitionGroupFormatTreeRound.competitionGroupFormatTreeMatches.size());
//                int matchLane = 1;
                    for (CompetitionGroupFormatTreeMatch competitionGroupFormatTreeMatch : competitionGroupFormatTreeRound.competitionGroupFormatTreeMatches) {
                        competitionMatch = competitionInstance.createCompetitionMatch(competitionRound);
                        competitionMatch.participantQuantity = competitionGroupFormatTreeMatch.participantQuantity;
                        competitionMatch.missingParticipantQuantity = competitionGroupFormatTreeMatch.missingParticipants;
                        competitionMatch.generation = true;
//                    competitionMatch.competitionRound = competitionRound;
//                    competitionMatch.round = matchLane;
                        competitionMatch.internationalizedLabel.defaultLabel = "matchDetails." + competitionMatch.lane;
                        //  logger.log(Level.FINE, "create competitionMatch:" + competitionMatch.toString());
                        // competitionMatch.initializeMatch();
                        CompetitionPlay competitionPlay = null;


                        //  logger.log(Level.FINE, "currentNumberOfPlay :" + competitionGroupFormatTreeMatch.playQuantity);

                        for (int l = 0; l < competitionGroupFormatTreeMatch.playQuantity; l++) {
                            competitionPlay = competitionInstance.createCompetitionPlay(competitionMatch);
                            CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
//                        if (competitionCreationParamPhase == null)
//                            competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
                            competitionPlay.playVersusType = competitionCreationParamPhase.playVersusType;
                            competitionPlay.round = l + 1;
                            competitionPlay.internationalizedLabel.defaultLabel = "playDetails." + competitionPlay.round;
//                        competitionPlay.competitionMatch = competitionMatch;
//                        competitionMatch.competitionPlays.add(competitionPlay);
                            //  logger.log(Level.FINE, "create competitionPlay:" + competitionPlay.toString());
                        }
//                    competitionRound.competitionMatches.add(competitionMatch);
//                    matchLane++;
                    }

                }

                //competitionRound.fillParticipantResultWithFakeValue();
//                competitionGroup.competitionRounds.add(competitionRound);


//                round++;
            }


            competitionGroups.add(competitionGroup);
//            groupLane++;
        }
//        if (competitionGroupFormatTree.competitionGroupFormat.compareTo(CompetitionGroupFormat.ELIMINATION) == 0) {
//        competitionSeed.fillCompetitionMatchLink();
//        }
        return competitionSeed;
    }

    public CompetitionInstance initGroupPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_initGroupPhase;
        //  logger.log(Level.FINE, "[START]");
        CompetitionCreationParamPhase competitionCreationParamPhase = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification : competitionCreationParamPhaseFinal;
        if (TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.LADDER)) {
            initLadderPhase(competitionInstance, competitionCreationParamPhaseQualification, competitionCreationParamPhaseFinal);
        } else {
            boolean fixedParticipantSize = !competitionCreationParamPhase.participantTypeSplittable;
            boolean participantSplittable = competitionCreationParamPhase.participantTypeSplittable;
            if (competitionInstance.participantSeats.size() > competitionCreationParamPhase.participantQualifiedPerMatch) {
//        int maxFinalGroupSize = finalPhaseParameter.groupSizeMaximum;
                int maxNumberOfParticipantGroup = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMaximum : competitionCreationParamPhaseFinal.groupSizeMaximum;
                PlayVersusType playVersusType = competitionCreationParamPhase.playVersusType;
                ParticipantType participantType = competitionCreationParamPhase.participantType;
                boolean isFinalPhaseElimination = competitionCreationParamPhaseQualification != null && (competitionCreationParamPhaseFinal != null && TournamentFormat.allowEliminationFormat(competitionCreationParamPhaseFinal.tournamentFormatsAccepted));
                Duration averagePlayDuration = competitionCreationParamPhase.getAveragePlayDuration();
                int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
                int groupSizeMinimum = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMinimum : competitionCreationParamPhaseFinal.groupSizeMinimum;
                int groupSizeMaximum = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMaximum : competitionCreationParamPhaseFinal.groupSizeMaximum;
                int groupSizeMinimumFinal = competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMinimum : competitionCreationParamPhaseQualification.groupSizeMinimum;
                int groupSizeMaximumFinal = competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMaximum : competitionCreationParamPhaseQualification.groupSizeMaximum;
                // multiple level depending on the number or participant
                Set<CompetitionGroupResult> lastCompetitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
                Set<Participant> participants = new HashSet<>();
                for (CompetitionGroupResult competitionGroupResult :
                        lastCompetitionGroupResults) {
                    for (ParticipantResult participantResult : competitionGroupResult.participantResults) {
                        participants.add(participantResult.getParticipant());
                    }
                }
                int numberOfParticipant = participants.size();
                //  logger.log(Level.FINE, "numberOfParticipant:" + numberOfParticipant);
                int quantityOfQualificationGroupStep = 1;
                if (isFinalPhaseElimination)
                    quantityOfQualificationGroupStep = getQuantityOfQualificationGroupStep(playVersusType, numberOfParticipant, groupSizeMaximumFinal, QUALIFICATION_PERCENTAGE);
                ParticipantFilteringMethod nextStepParticipantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
//        if (quantityOfQualificationGroupStep == 0) {
//            maxFinalGroupSize = nextStepParticipantFilteringMethod.getFilteredParticipantSize(numberOfParticipant, nextStepFilteringValue, nextStepFilteringUnit);
//            quantityOfQualificationGroupStep = getQuantityOfQualificationGroupStep(numberOfParticipant, maxFinalGroupSize);
//        }
                int previousMaxNumberOfPlay = 0;
                int previousMinNumberOfPlay = 0;
                Set<CompetitionGroupResult> competitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
                if (competitionGroupResults != null && !competitionGroupResults.isEmpty()) {
                    previousMaxNumberOfPlay = competitionGroupResults.iterator().next().getCompetitionGroup().getMaxNumberOfPlay();
                    previousMinNumberOfPlay = competitionGroupResults.iterator().next().getCompetitionGroup().getMinNumberOfPlay();
                }
                //  logger.log(Level.FINE, "quantityOfQualificationGroupStep :" + quantityOfQualificationGroupStep);


//        int roundNumber = competitionInstance.getMaxRoundNumber();
//        int firstQualificationGroupSize = (int) Math.ceil((double) numberOfParticipant / (double) quantityOfInitialParallelQualificationGroupPhase);


//            Unit filteringUnit = null;
//            int filteringValue = 0;

                ParticipantFilteringMethod participantFilteringMethod = ParticipantFilteringMethod.ALL;
                int participantQuantityOut = 0;
                int competitionRoundQuantity = 0;

                int minNumberOfPlay = previousMinNumberOfPlay;
                int maxNumberOfPlay = previousMaxNumberOfPlay;

//        numberOfPlayMinimum += numberOfPlayMaximum - numberOfPlayMinimum / (quantityOfQualificationGroupStep +3);
//        numberOfPlayMaximum += numberOfPlayMaximum - numberOfPlayMinimum / (quantityOfQualificationGroupStep +2);


                if (minNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                    minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

                if (minNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                    minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

                if (maxNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                    maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

                if (maxNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                    maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

                Unit nextStepFilteringUnit = null;
                int nextStepFilteringValue = 0;
                for (int i = 0; i < quantityOfQualificationGroupStep; i++) {
                    if (i > 0) {
                        numberOfParticipant = participantQuantityOut;
                        lastCompetitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
                        participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
                    }

                    minNumberOfPlay += (maxNumberOfPlay - minNumberOfPlay) / (quantityOfQualificationGroupStep - i + 3);
                    if (minNumberOfPlay <= previousMinNumberOfPlay)
                        minNumberOfPlay = previousMinNumberOfPlay + 2;

                    maxNumberOfPlay += (maxNumberOfPlay - minNumberOfPlay) / (quantityOfQualificationGroupStep - i + 2);

                    if (minNumberOfPlay % 2 == 0)
                        minNumberOfPlay = minNumberOfPlay - 1;
                    if (maxNumberOfPlay % 2 == 0)
                        maxNumberOfPlay = maxNumberOfPlay - 1;

                    if (minNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                        minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

                    if (minNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                        minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

                    if (maxNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                        maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

                    if (maxNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                        maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

                    int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;


                    //  logger.log(Level.FINE, "qualificationGroupStep(i):" + i);
                    //  logger.log(Level.FINE, "numberOfParticipant:" + numberOfParticipant);
                    participantQuantityOut = ParticipantFilteringMethod.HIGH_PASS.computeFilteredParticipantSize(numberOfParticipantMatch, playVersusType, numberOfParticipant, QUALIFICATION_PERCENTAGE, Unit.PERCENTAGE, groupSizeMinimum, groupSizeMaximum);
                    if (participantQuantityOut < groupSizeMinimumFinal)
                        participantQuantityOut = groupSizeMinimumFinal;
                    assert participantQuantityOut % numberOfParticipantMatch == 0 || participantQuantityOut == numberOfParticipant;
                    boolean participantTypeSplittable = competitionCreationParamPhase.participantTypeSplittable;

                    int quantityOfParallelQualificationGroupPhase = getQuantityOfParallelQualificationGroupPhase(groupSizeMinimum, participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, maxNumberOfParticipantGroup, participantQuantityOut, participantQualifiedPerMatch, isFinalPhaseElimination, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, fixedParticipantSize, participantTypeSplittable);


                    CompetitionGroupFormatTree competitionGroupFormatTree = null;

                    boolean isEliminationPhaseAllowed = false;
                    double numberOfRound = CompetitionGroupFormat.ELIMINATION.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, participantSplittable);
                    if (numberOfRound == (int) numberOfRound && competitionCreationParamPhaseMixing != null && TournamentFormat.allowEliminationFormat(competitionCreationParamPhase.tournamentFormatsAccepted))
                        isEliminationPhaseAllowed = true;

//            if (competitionGroupFormatForced == null) {
                    CompetitionGroupFormatTree competitionGroupFormatTreeSwiss = CompetitionGroupFormat.SWISS.getCompetitionGroupFormatTree(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, quantityOfParallelQualificationGroupPhase, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
                    CompetitionGroupFormatTree competitionGroupFormatTreeRoundRobin = null;
                    CompetitionGroupFormatTree competitionGroupFormatTreeRoundRobinInCaseOf = null;
                    if (competitionCreationParamPhase != null && competitionCreationParamPhaseFinal != null && competitionCreationParamPhaseFinal.numberOfParticipantMatch <= 4 && groupSizeMaximum <= 16) {
                        competitionGroupFormatTreeRoundRobinInCaseOf = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, quantityOfParallelQualificationGroupPhase, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
                        if (TournamentFormat.allowFormat(competitionCreationParamPhase.tournamentFormatsAccepted, TournamentFormat.ROUND_ROBIN))
                            competitionGroupFormatTreeRoundRobin = competitionGroupFormatTreeRoundRobinInCaseOf;
                    }

                    CompetitionGroupFormatTree competitionGroupFormatTreeElimination = null;
                    int competitionRoundQuantityElimination = 0;
                    if (isEliminationPhaseAllowed) {
                        competitionGroupFormatTreeElimination = CompetitionGroupFormat.ELIMINATION.getCompetitionGroupFormatTree(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, quantityOfParallelQualificationGroupPhase, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
                        competitionRoundQuantityElimination = competitionGroupFormatTreeElimination.getCompetitionRoundQuantityMaximum();
                        //  logger.log(Level.FINE, "competitionRoundQuantityElimination :" + competitionRoundQuantityElimination);
                    }
                    int competitionRoundQuantitySwiss = competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum();
                    //  logger.log(Level.FINE, "competitionRoundQuantitySwiss :" + competitionRoundQuantitySwiss);
                    if (competitionGroupFormatTreeRoundRobin != null && (competitionCreationParamPhase.tournamentFormatsAccepted.contains(TournamentFormat.SWISS) || isEliminationPhaseAllowed)) {
                        int competitionRoundQuantityRoundRobin = competitionGroupFormatTreeRoundRobin.getCompetitionRoundQuantityMaximum();
                        //  logger.log(Level.FINE, "competitionRoundQuantityRoundRobin :" + competitionRoundQuantityRoundRobin);
                        double ratioSwissRoundRobin = (double) competitionRoundQuantitySwiss / (double) competitionRoundQuantityRoundRobin;
                        if (isEliminationPhaseAllowed && numberOfParticipant <= competitionCreationParamPhaseFinal.groupSizeMaximum) {
                            competitionGroupFormatTree = competitionGroupFormatTreeElimination;
                        } else {
                            if (ratioSwissRoundRobin >= 0.9 || competitionRoundQuantityRoundRobin - competitionRoundQuantitySwiss <= 1) {
                                competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                            } else {
                                competitionGroupFormatTree = competitionGroupFormatTreeSwiss;
                            }
                        }
                    } else {
                        if (isEliminationPhaseAllowed && numberOfParticipant <= groupSizeMaximum) {
                            competitionGroupFormatTree = competitionGroupFormatTreeElimination;
                        } else {
                            if (competitionCreationParamPhase.tournamentFormatsAccepted.contains(TournamentFormat.SWISS)) {
                                if (competitionGroupFormatTreeRoundRobin != null && (Math.abs(competitionGroupFormatTreeRoundRobin.getCompetitionRoundQuantityMaximum() - competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum()) <= 1)) {
                                    competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                                } else {
                                    if (competitionGroupFormatTreeRoundRobinInCaseOf != null && competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum() == competitionGroupFormatTreeRoundRobinInCaseOf.getCompetitionRoundQuantityMaximum())
                                        competitionGroupFormatTree = competitionGroupFormatTreeRoundRobinInCaseOf;
                                    else
                                        competitionGroupFormatTree = competitionGroupFormatTreeSwiss;
                                }
                            } else {
                                if (competitionGroupFormatTreeRoundRobin != null)
                                    competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                                else {
                                    if (competitionGroupFormatTreeRoundRobinInCaseOf != null && competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum() == competitionGroupFormatTreeRoundRobinInCaseOf.getCompetitionRoundQuantityMaximum())
                                        competitionGroupFormatTree = competitionGroupFormatTreeRoundRobinInCaseOf;
                                    else
                                        competitionGroupFormatTree = competitionGroupFormatTreeSwiss;
                                }
                            }
                        }
                    }
                    boolean swissChosen = false;
                    if (competitionGroupFormatTree == null) {
                        if (competitionGroupFormatTreeSwiss != null) {
                            competitionGroupFormatTree = competitionGroupFormatTreeSwiss;
                            swissChosen = true;
                        } else if (competitionGroupFormatTreeRoundRobin != null) {
                            competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                        } else if (competitionGroupFormatTreeElimination != null) {
                            competitionGroupFormatTree = competitionGroupFormatTreeElimination;
                        }
                    }
                    int numberOfRoundForRoundRobin = (int) Math.ceil((double) numberOfParticipant / (double) quantityOfParallelQualificationGroupPhase) - 1;
                    if (competitionGroupFormatTree.competitionGroupFormat.compareTo(CompetitionGroupFormat.SWISS) == 0 && playVersusType.numberOfTeam == 2 && competitionGroupFormatTree.getCompetitionRoundQuantityMaximum() == numberOfRoundForRoundRobin) {
                        if (competitionGroupFormatTreeRoundRobin == null && competitionGroupFormatTreeRoundRobinInCaseOf == null) {
                            competitionGroupFormatTreeRoundRobinInCaseOf = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(
                                    participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, quantityOfParallelQualificationGroupPhase, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);
                        }
                        if (competitionGroupFormatTreeRoundRobin != null && competitionGroupFormatTreeSwiss != null && competitionGroupFormatTreeRoundRobin.getCompetitionRoundQuantityMaximum() == competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum()) {
                            competitionGroupFormatTree = competitionGroupFormatTreeRoundRobin;
                        } else if (competitionGroupFormatTreeRoundRobinInCaseOf != null && competitionGroupFormatTreeSwiss != null && competitionGroupFormatTreeRoundRobinInCaseOf.getCompetitionRoundQuantityMaximum() == competitionGroupFormatTreeSwiss.getCompetitionRoundQuantityMaximum()) {
                            competitionGroupFormatTree = competitionGroupFormatTreeRoundRobinInCaseOf;
                        }
                    }

                    CompetitionPhase competitionPhase = initCompetitionGroupPhase(competitionInstance, competitionCreationParamPhase, lastCompetitionGroupResults, null, i + 1, participantFilteringMethod, Unit.ABSOLUTE, numberOfParticipant, competitionGroupFormatTree);


                }
            }
        }
        return competitionInstance;
    }

    public int getQuantityOfParallelQualificationGroupPhase(int minGroupSize, ParticipantType participantType, int numberOfParticipantMatch, PlayVersusType playVersusType, int numberOfParticipant, int maxNumberOfParticipantGroup, int participantQuantityOut, int participantQualifiedPerMatch, boolean isFinalPhaseElimination, boolean allowEvenNumberOfPlay, Integer numberOfRoundMinimum, Integer numberOfRoundMaximum, boolean fixedParticipantSize, boolean participantSplittable) {
        // Logger logger = LOGGER_getQuantityOfParallelQualificationGroupPhase;

        int quantityOfParallelQualificationGroupPhase = 0;
        if (isFinalPhaseElimination) {
            if ((numberOfParticipant <= 16 && numberOfParticipantMatch <= 4) || (numberOfParticipant <= 32 && numberOfParticipantMatch <= 2))
                quantityOfParallelQualificationGroupPhase = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfGroups(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, 2 * participantType.numberOfParticipants * playVersusType.numberOfTeam, maxNumberOfParticipantGroup, participantQuantityOut, participantQualifiedPerMatch, allowEvenNumberOfPlay, numberOfRoundMinimum, numberOfRoundMaximum, false, fixedParticipantSize, participantSplittable);
            else
                quantityOfParallelQualificationGroupPhase = CompetitionGroupFormat.SWISS.getNumberOfGroups(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, 2 * participantType.numberOfParticipants * playVersusType.numberOfTeam, maxNumberOfParticipantGroup, participantQuantityOut, participantQualifiedPerMatch, allowEvenNumberOfPlay, numberOfRoundMinimum, numberOfRoundMaximum, false, fixedParticipantSize, participantSplittable);
        } else {
            quantityOfParallelQualificationGroupPhase = 1;
        }
        //  logger.log(Level.FINE, "quantityOfParallelQualificationGroupPhase:" + quantityOfParallelQualificationGroupPhase);
        return quantityOfParallelQualificationGroupPhase;
    }

    public CompetitionInstance initMixingPhase(CompetitionInstance competitionInstance, CompetitionGroupResult competitionGroupResult, CompetitionCreationParamPhaseMixing competitionCreationParamPhaseMixing) throws CompetitionInstanceGeneratorException {

        if (competitionInstance.participantSeats.size() > competitionCreationParamPhaseMixing.participantQualifiedPerMatch) {
            CompetitionPhase competitionPhase = createRandomPhaseWithoutElimination(competitionInstance, competitionGroupResult, competitionCreationParamPhaseMixing);

        }
        return competitionInstance;
    }


    public CompetitionInstance initRoundRobinFinalPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification) throws CompetitionInstanceGeneratorException {
        CompetitionSeed competitionSeed = initCompetitionSeedFinalPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);

        boolean generation = true;

        boolean fixedParticipantSize = !competitionCreationParamPhaseFinal.participantTypeSplittable;
        boolean participantSplittable = competitionCreationParamPhaseFinal.participantTypeSplittable;

        CompetitionGroupFormatTree competitionGroupFormatTree = null;
        if (competitionCreationParamPhaseFinal.numberOfParticipantMatch >= competitionSeed.participantQuantity || ((competitionCreationParamPhaseFinal.numberOfParticipantMatch <= 4 && competitionCreationParamPhaseFinal.groupSizeMaximum <= 16) || (competitionCreationParamPhaseFinal.numberOfParticipantMatch <= 2 && competitionCreationParamPhaseFinal.groupSizeMaximum <= 32))) {
            competitionGroupFormatTree = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(competitionCreationParamPhaseFinal.participantType, competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.playVersusType, competitionSeed.participantQuantity, 1, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, 1, competitionCreationParamPhaseFinal.numberOfPlayMinimum, competitionCreationParamPhaseFinal.numberOfPlayMaximum, competitionCreationParamPhaseFinal.allowEvenNumberOfPlay, null, null, competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled, competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum : 0, false, fixedParticipantSize, participantSplittable);
        } else {
            competitionGroupFormatTree = CompetitionGroupFormat.SWISS.getCompetitionGroupFormatTree(competitionCreationParamPhaseFinal.participantType, competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.playVersusType, competitionSeed.participantQuantity, 1, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, 1, competitionCreationParamPhaseFinal.numberOfPlayMinimum, competitionCreationParamPhaseFinal.numberOfPlayMaximum, competitionCreationParamPhaseFinal.allowEvenNumberOfPlay, null, null, competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled, competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum : 0, false, fixedParticipantSize, participantSplittable);
        }
        if (competitionSeed.participantQuantity > competitionCreationParamPhaseFinal.groupSizeMaximum)
            competitionCreationParamPhaseFinal.groupSizeMaximum = competitionSeed.participantQuantity;
        if (competitionSeed.participantQuantity < competitionCreationParamPhaseFinal.groupSizeMinimum)
            competitionCreationParamPhaseFinal.groupSizeMinimum = competitionSeed.participantQuantity;

        createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);

        competitionInstance.continueCompetition(generation);

        competitionInstance.fillCompetitionMatchLink();
        competitionInstance.resetParticipantResults();
//        }
        //  logger.log(Level.FINE, "[END]");
        return competitionInstance;
    }

    public CompetitionSeed initCompetitionSeedFinalPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification) throws CompetitionInstanceGeneratorException {

        boolean generation = true;
        competitionInstance.continueCompetition(generation);

        Set<CompetitionGroupResult> competitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
        List<Participant> previousParticipantResultParticipants = competitionInstance.getLastCompetitionPhaseResultsParticipants();
//        Set<Participant> previousParticipantResultParticipants = competitionInstance.computeLastCompetitionGroupResultsParticipants();
        int previousParticipantResultSize = previousParticipantResultParticipants.size();
        //  logger.log(Level.FINE, "competitionGroupResults.size():" + competitionGroupResults.size());
        Set<CompetitionPhase> competitionPhasesPrevious = new HashSet<>();
        for (CompetitionGroupResult competitionGroupResult : competitionGroupResults) {
            competitionPhasesPrevious.add(competitionGroupResult.getCompetitionGroup().getCompetitionSeed().getCompetitionPhase());
        }
        CompetitionPhase competitionPhase = competitionInstance.createCompetitionPhase(competitionPhasesPrevious);
        competitionPhase.competitionCreationParamPhase = competitionCreationParamPhaseFinal;
        competitionPhase.phaseType = PhaseType.FINAL;
        competitionInstance.addCompetitionPhase(competitionPhase);

        CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResults, StepType.STANDALONE);

        if (competitionCreationParamPhaseQualification != null) {
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
            competitionSeed.filteringUnit = Unit.ABSOLUTE;
            competitionSeed.filteringValue = competitionCreationParamPhaseFinal.groupSizeMaximum;
        } else {
            competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.ALL;
            competitionSeed.filteringUnit = Unit.ABSOLUTE;
            competitionSeed.filteringValue = competitionInstance.competitionComputationParam.numberOfParticipantCompetition;
            ((CompetitionCreationParamPhaseFinal) competitionPhase.competitionCreationParamPhase).groupSizeMaximum = competitionInstance.competitionComputationParam.numberOfParticipantCompetition;
        }
        competitionSeed.participantQuantity = competitionSeed.filteringValue;

        competitionSeed.participantPairingMethod = ParticipantPairingMethod.TOP_VS_MIDDLE;
        return competitionSeed;
    }


    public CompetitionInstance initLadderPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhase competitionCreationParamPhase, CompetitionCreationParamPhase competitionCreationParamPhasePrevious) throws CompetitionInstanceGeneratorException {
        CompetitionSeed competitionSeed = null;

        // multiple level depending on the number or participant
        Set<CompetitionGroupResult> lastCompetitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
        Set<Participant> participants = new HashSet<>();
        int numberOfParticipant = 0;
        if (!competitionCreationParamPhase.registrationOnTheFly) {
            for (CompetitionGroupResult competitionGroupResult :
                    lastCompetitionGroupResults) {
                for (ParticipantResult participantResult : competitionGroupResult.participantResults) {
                    participants.add(participantResult.getParticipant());
                }
            }
            numberOfParticipant = participants.size();
        } else {
            numberOfParticipant = competitionInstance.competitionComputationParam.numberOfParticipantCompetition;
        }


        int quantityOfQualificationGroupStep = 1;
        CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = null;
        CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification = null;
        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
            competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
            if ((competitionCreationParamPhasePrevious == null || competitionCreationParamPhasePrevious instanceof CompetitionCreationParamPhaseQualification)) {
                competitionCreationParamPhaseQualification = (CompetitionCreationParamPhaseQualification) competitionCreationParamPhasePrevious;
//                competitionSeed = initCompetitionSeedFinalPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);
            } else {
//                competitionSeed = initCompetitionSeedFinalPhase(competitionInstance, (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase, null);
            }
        } else {
            if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification) {
                competitionCreationParamPhaseQualification = (CompetitionCreationParamPhaseQualification) competitionCreationParamPhase;
            }
        }
        int maxNumberOfParticipantGroup = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMaximum : (competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMaximum : numberOfParticipant);
        PlayVersusType playVersusType = competitionCreationParamPhase.playVersusType;
        ParticipantType participantType = competitionCreationParamPhase.participantType;
        boolean isFinalPhaseElimination = competitionCreationParamPhaseQualification != null && (competitionCreationParamPhaseFinal != null && TournamentFormat.allowEliminationFormat(competitionCreationParamPhaseFinal.tournamentFormatsAccepted));
        Duration averagePlayDuration = competitionCreationParamPhase.getAveragePlayDuration();
        int numberOfParticipantMatch = competitionCreationParamPhase.numberOfParticipantMatch;
        int groupSizeMinimum = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMinimum : (competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMinimum : (numberOfParticipantMatch == 1 ? 2 : numberOfParticipantMatch * 4));
        int groupSizeMaximum = competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMaximum : (competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMaximum : numberOfParticipant);
        int groupSizeMinimumFinal = competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMinimum : (competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMinimum : groupSizeMinimum);
        int groupSizeMaximumFinal = competitionCreationParamPhaseFinal != null ? competitionCreationParamPhaseFinal.groupSizeMaximum : (competitionCreationParamPhaseQualification != null ? competitionCreationParamPhaseQualification.groupSizeMaximum : groupSizeMaximum);
        //  logger.log(Level.FINE, "numberOfParticipant:" + numberOfParticipant);
        if (competitionCreationParamPhaseQualification != null && competitionCreationParamPhaseFinal != null) {
            if (numberOfParticipant > competitionCreationParamPhaseFinal.groupSizeMaximum) {
                numberOfParticipant = competitionCreationParamPhaseFinal.groupSizeMaximum;
            }
        }

        if (isFinalPhaseElimination)
            quantityOfQualificationGroupStep = getQuantityOfQualificationGroupStep(playVersusType, numberOfParticipant, groupSizeMaximumFinal, QUALIFICATION_PERCENTAGE);

        boolean generation = true;
        int participantQuantityOut = 0;
        int competitionRoundQuantity = 0;
        ParticipantFilteringMethod participantFilteringMethod = ParticipantFilteringMethod.ALL;
        if (numberOfParticipant < participants.size() && !competitionCreationParamPhase.registrationOnTheFly) {
            participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
        }
        int previousMaxNumberOfPlay = 0;
        int previousMinNumberOfPlay = 0;
        if (lastCompetitionGroupResults != null && !lastCompetitionGroupResults.isEmpty()) {
            previousMaxNumberOfPlay = lastCompetitionGroupResults.iterator().next().getCompetitionGroup().getMaxNumberOfPlay();
            previousMinNumberOfPlay = lastCompetitionGroupResults.iterator().next().getCompetitionGroup().getMinNumberOfPlay();
        }
        //  logger.log(Level.FINE, "quantityOfQualificationGroupStep :" + quantityOfQualificationGroupStep);


//        int roundNumber = competitionInstance.getMaxRoundNumber();
//        int firstQualificationGroupSize = (int) Math.ceil((double) numberOfParticipant / (double) quantityOfInitialParallelQualificationGroupPhase);


//            Unit filteringUnit = null;
//            int filteringValue = 0;


        int minNumberOfPlay = previousMinNumberOfPlay;
        int maxNumberOfPlay = previousMaxNumberOfPlay;

//        numberOfPlayMinimum += numberOfPlayMaximum - numberOfPlayMinimum / (quantityOfQualificationGroupStep +3);
//        numberOfPlayMaximum += numberOfPlayMaximum - numberOfPlayMinimum / (quantityOfQualificationGroupStep +2);


        if (minNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
            minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

        if (minNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
            minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

        if (maxNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
            maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

        if (maxNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
            maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

        boolean fixedParticipantSize = !competitionCreationParamPhase.participantTypeSplittable;
        boolean participantSplittable = competitionCreationParamPhase.participantTypeSplittable;
        for (int i = 0; i < quantityOfQualificationGroupStep; i++) {
            if (i > 0) {
                numberOfParticipant = participantQuantityOut;
//                lastCompetitionGroupResults = competitionInstance.computeLastCompetitionGroupResults();
                participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
            }

            minNumberOfPlay += (maxNumberOfPlay - minNumberOfPlay) / (quantityOfQualificationGroupStep - i + 3);
            if (minNumberOfPlay <= previousMinNumberOfPlay)
                minNumberOfPlay = previousMinNumberOfPlay + 2;

            maxNumberOfPlay += (maxNumberOfPlay - minNumberOfPlay) / (quantityOfQualificationGroupStep - i + 2);

            if (minNumberOfPlay % 2 == 0)
                minNumberOfPlay = minNumberOfPlay - 1;
            if (maxNumberOfPlay % 2 == 0)
                maxNumberOfPlay = maxNumberOfPlay - 1;

            if (minNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

            if (minNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                minNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

            if (maxNumberOfPlay < competitionCreationParamPhase.numberOfPlayMinimum)
                maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMinimum;

            if (maxNumberOfPlay > competitionCreationParamPhase.numberOfPlayMaximum)
                maxNumberOfPlay = competitionCreationParamPhase.numberOfPlayMaximum;

            int participantQualifiedPerMatch = competitionCreationParamPhase.participantQualifiedPerMatch;


            //  logger.log(Level.FINE, "qualificationGroupStep(i):" + i);
            //  logger.log(Level.FINE, "numberOfParticipant:" + numberOfParticipant);
            participantQuantityOut = ParticipantFilteringMethod.HIGH_PASS.computeFilteredParticipantSize(numberOfParticipantMatch, playVersusType, numberOfParticipant, QUALIFICATION_PERCENTAGE, Unit.PERCENTAGE, groupSizeMinimum, groupSizeMaximum);
            if (participantQuantityOut < groupSizeMinimumFinal)
                participantQuantityOut = groupSizeMinimumFinal;
            assert participantQuantityOut % numberOfParticipantMatch == 0 || participantQuantityOut == numberOfParticipant;
            boolean participantTypeSplittable = competitionCreationParamPhase.participantTypeSplittable;

            int quantityOfParallelQualificationGroupPhase = getQuantityOfParallelQualificationGroupPhase(groupSizeMinimum, participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, maxNumberOfParticipantGroup, participantQuantityOut, participantQualifiedPerMatch, isFinalPhaseElimination, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, fixedParticipantSize, participantTypeSplittable);


//            double numberOfRound = CompetitionGroupFormat.ROUND_ROBIN.getNumberOfRounds(numberOfParticipant, participantQuantityOut, numberOfParticipantMatch, playVersusType, participantQualifiedPerMatch, participantSplittable);

//            if (competitionGroupFormatForced == null) {
            CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.LADDER.getCompetitionGroupFormatTree(participantType, numberOfParticipantMatch, playVersusType, numberOfParticipant, participantQuantityOut, participantQualifiedPerMatch, quantityOfParallelQualificationGroupPhase, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, participantSplittable);


            CompetitionPhase competitionPhase = initCompetitionGroupPhase(competitionInstance, competitionCreationParamPhase, lastCompetitionGroupResults, numberOfParticipant, i + 1, participantFilteringMethod, Unit.ABSOLUTE, numberOfParticipant, competitionGroupFormatTree);

        }
        return competitionInstance;
    }


    public CompetitionInstance initSwissFinalPhase(CompetitionInstance competitionInstance, CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal, CompetitionCreationParamPhaseQualification competitionCreationParamPhaseQualification) throws CompetitionInstanceGeneratorException {
        CompetitionSeed competitionSeed = initCompetitionSeedFinalPhase(competitionInstance, competitionCreationParamPhaseFinal, competitionCreationParamPhaseQualification);

        boolean generation = true;

        boolean fixedParticipantSize = !competitionCreationParamPhaseFinal.participantTypeSplittable;
        boolean participantSplittable = competitionCreationParamPhaseFinal.participantTypeSplittable;


        CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.SWISS.getCompetitionGroupFormatTree(competitionCreationParamPhaseFinal.participantType, competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.playVersusType, competitionSeed.participantQuantity, 1, competitionCreationParamPhaseFinal.participantQualifiedPerMatch, 1, competitionCreationParamPhaseFinal.numberOfPlayMinimum, competitionCreationParamPhaseFinal.numberOfPlayMaximum, competitionCreationParamPhaseFinal.allowEvenNumberOfPlay, null, null, competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled, competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null ? competitionCreationParamPhaseFinal.groupSizeFinalThreshold : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum : 0, competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum != null ? competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum : 0, false, fixedParticipantSize, participantSplittable);

        CompetitionGroupFormatTree competitionGroupFormatTreeRoundRobinInCaseOf = null;
        if (((competitionCreationParamPhaseFinal.playVersusType.numberOfTeam == 2 && competitionInstance.participantSeats.size() < 64) || (competitionCreationParamPhaseFinal.playVersusType.numberOfTeam <= 4 && competitionInstance.participantSeats.size() < competitionCreationParamPhaseFinal.playVersusType.numberOfTeam * 5))) {
            competitionGroupFormatTreeRoundRobinInCaseOf = CompetitionGroupFormat.ROUND_ROBIN.getCompetitionGroupFormatTree(
                    competitionCreationParamPhaseFinal.participantType, competitionCreationParamPhaseFinal.numberOfParticipantMatch, competitionCreationParamPhaseFinal.playVersusType, competitionInstance.participantSeats.size(), competitionInstance.participantSeats.size(), competitionCreationParamPhaseFinal.participantQualifiedPerMatch, 1, competitionCreationParamPhaseFinal.numberOfPlayMinimum, competitionCreationParamPhaseFinal.numberOfPlayMaximum, competitionCreationParamPhaseFinal.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, false, fixedParticipantSize, competitionCreationParamPhaseFinal.participantTypeSplittable);
        }
        if (competitionGroupFormatTreeRoundRobinInCaseOf != null && (Math.abs(competitionGroupFormatTreeRoundRobinInCaseOf.getCompetitionRoundQuantityMaximum() - competitionGroupFormatTree.getCompetitionRoundQuantityMaximum()) <= 1)) {
            competitionGroupFormatTree = competitionGroupFormatTreeRoundRobinInCaseOf;
        }
        createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree);

        if (competitionSeed.participantQuantity > competitionCreationParamPhaseFinal.groupSizeMaximum)
            competitionCreationParamPhaseFinal.groupSizeMaximum = competitionSeed.participantQuantity;
        if (competitionSeed.participantQuantity < competitionCreationParamPhaseFinal.groupSizeMinimum)
            competitionCreationParamPhaseFinal.groupSizeMinimum = competitionSeed.participantQuantity;

        competitionInstance.continueCompetition(generation);

        competitionInstance.fillCompetitionMatchLink();
        competitionInstance.resetParticipantResults();
        return competitionInstance;
    }

    public CompetitionSeed mergeCompetitionGroup(CompetitionInstance competitionInstance, SortedSet<CompetitionGroup> competitionGroupsToMerge, boolean qualified, CompetitionCreationParamPhase competitionCreationParamPhase, int minNumberOfPlayGlobal, int maxNumberOfPlayGlobal, PhaseType phaseType) throws CompetitionInstanceGeneratorException {
        // Logger logger = LOGGER_mergeCompetitionGroup;
        //  logger.log(Level.FINE, "[START]");
        competitionInstance.resetParticipantResults();
        Set<CompetitionGroupResult> competitionGroupResults = new HashSet<>();
        int minNumberOfPlay = minNumberOfPlayGlobal;
        int maxNumberOfPlay = maxNumberOfPlayGlobal;
        CompetitionPhase competitionPhase = competitionGroupsToMerge.iterator().next().competitionSeed.competitionPhase;
        boolean fixedParticipantSize = !competitionCreationParamPhase.participantTypeSplittable;
        for (CompetitionGroup competitionGroup : competitionGroupsToMerge) {
            competitionGroupResults.add(competitionGroup.getCompetitionGroupResult());
            int lastNumberOfPlaySize = competitionGroup.getCompetitionRounds().last().getCompetitionMatches().first().competitionPlays.size();
            if (minNumberOfPlay < lastNumberOfPlaySize)
                minNumberOfPlay = lastNumberOfPlaySize;
            if (maxNumberOfPlay < lastNumberOfPlaySize)
                maxNumberOfPlay = lastNumberOfPlaySize;
        }
        boolean participantSplittable = competitionCreationParamPhase.participantTypeSplittable;
        boolean thirdPlaceMatch = false;
        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
            thirdPlaceMatch = competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled != null && competitionCreationParamPhaseFinal.thirdPlaceMatchEnabled.booleanValue();
            int remainingParticipant = (competitionPhase.getCompetitionSeeds().first().getCompetitionGroups().size() - competitionPhase.getCompetitionSeeds().size()) * competitionCreationParamPhaseFinal.numberOfParticipantMatch;
            if (competitionCreationParamPhaseFinal.groupSizeFinalEnabled != null && competitionCreationParamPhaseFinal.groupSizeFinalEnabled && competitionCreationParamPhaseFinal.groupSizeFinalThreshold != null && remainingParticipant <= competitionCreationParamPhaseFinal.groupSizeFinalThreshold) {
                if (minNumberOfPlay < competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum) {
                    minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayFinalMinimum;
                }
                if (minNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum) {
                    minNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum;
                }
                if (maxNumberOfPlay > competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum) {
                    maxNumberOfPlay = competitionCreationParamPhaseFinal.numberOfPlayFinalMaximum;
                }
            }
        }
        CompetitionSeed competitionSeed = competitionInstance.createCompetitionSeed(competitionPhase, competitionGroupResults, StepType.MERGE);
//        competitionSeed.stepType = StepType.MERGE;
//        competitionSeed.phaseType = phaseType;
//        competitionSeed.setPhase(competitionGroupsToMerge.iterator().next().competitionSeed.phase);
//        competitionSeed.competitionCreationParamPhase = competitionCreationParamPhase;
//        if (competitionSeed.competitionCreationParamPhase != null)
//            competitionSeed.setPhase(competitionSeed.competitionCreationParamPhase.phaseIndex);

        competitionSeed.participantPairingMethod = ParticipantPairingMethod.TOP_VS_MIDDLE;
        int expectedParticipant = 0;
        if (competitionCreationParamPhase.numberOfParticipantMatch == 1) {
            for (CompetitionGroup competitionGroupToMerge : competitionGroupsToMerge) {
                expectedParticipant++;
                competitionSeed.internationalizedLabel.defaultLabel += " " + competitionGroupToMerge.internationalizedLabel.defaultLabel;
            }
        } else {
            for (CompetitionGroup competitionGroupToMerge : competitionGroupsToMerge) {
                Sets.sort(competitionGroupToMerge.competitionRounds);

                for (CompetitionMatch competitionMatch : competitionGroupToMerge.getCompetitionRounds().last().getCompetitionMatches()) {
                    if (qualified)
                        expectedParticipant += CompetitionGroupFormat.ELIMINATION.getNumberOfParticipantQualifiedPerMatch(competitionMatch);
                    else
                        expectedParticipant += CompetitionGroupFormat.ELIMINATION.getNumberOfParticipantEliminatedPerMatch(competitionMatch);
                }
//            competitionGroupToMerge.competitionGroupResult.competitionSeedNext = competitionSeed;
//            competitionSeed.competitionGroupResultsPrevious.add(competitionGroupToMerge.competitionGroupResult);
                competitionSeed.internationalizedLabel.defaultLabel += " " + competitionGroupToMerge.internationalizedLabel.defaultLabel;
            }
        }
        competitionSeed.participantFilteringMethod = ParticipantFilteringMethod.HIGH_PASS;
        competitionSeed.filteringUnit = Unit.ABSOLUTE;
        competitionSeed.filteringValue = expectedParticipant;
        if (competitionSeed.filteringValue < competitionCreationParamPhase.numberOfParticipantMatch)
            competitionSeed.filteringValue = competitionCreationParamPhase.numberOfParticipantMatch;

        competitionSeed.participantQuantity = competitionSeed.filteringValue;

        int numberOfParticipantOut = competitionCreationParamPhase.participantQualifiedPerMatch;

        CompetitionGroupFormatTree competitionGroupFormatTree = CompetitionGroupFormat.ELIMINATION.getCompetitionGroupFormatTree(competitionCreationParamPhase.participantType, competitionCreationParamPhase.numberOfParticipantMatch, competitionCreationParamPhase.playVersusType, expectedParticipant, numberOfParticipantOut, competitionCreationParamPhase.participantQualifiedPerMatch, 1, minNumberOfPlay, maxNumberOfPlay, competitionCreationParamPhase.allowEvenNumberOfPlay, null, null, false, 0, 0, 0, thirdPlaceMatch, fixedParticipantSize, participantSplittable);
        createCompetitionGroupsFromCompetitionGroupFormatTree(competitionInstance, competitionSeed, competitionGroupFormatTree, competitionGroupsToMerge);
        if (competitionCreationParamPhase.numberOfParticipantMatch > 1) {
            for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                CompetitionMatch competitionMatchMerge = competitionGroup.competitionRounds.first().competitionMatches.first();
                int index = 1;
                for (CompetitionGroup competitionGroupToMerge : competitionGroupsToMerge) {
                    for (CompetitionMatch competitionMatch : competitionGroupToMerge.competitionRounds.last().competitionMatches) {
//                    competitionMatch.removeNextCompetitionMatches();

                        competitionMatchMerge.addPreviousCompetitionMatch(competitionMatch, 1, 1, null, 1);
                        competitionMatch.addNextCompetitionMatch(competitionMatchMerge, 1, 1, index, null);

                        competitionMatch.getCompetitionRound().afterDependenciesCompetitionRounds.add(competitionMatchMerge.getCompetitionRound());
                        competitionMatchMerge.getCompetitionRound().beforeDependenciesCompetitionRounds.add(competitionMatch.getCompetitionRound());
                    }
                    index++;
                }
            }
        } else {
            for (CompetitionGroup competitionGroup : competitionSeed.competitionGroups) {
                CompetitionRound competitionRoundMerge = competitionGroup.competitionRounds.first();
                int index = 0;
                for (CompetitionMatch competitionMatchMerge : competitionRoundMerge.competitionMatches) {
                    CompetitionGroup competitionGroupPrevious = (CompetitionGroup) competitionGroupsToMerge.toArray()[index];
                    CompetitionRound competitionRoundLast = competitionGroupPrevious.getCompetitionRounds().last();
                    Participant winner = competitionRoundLast.getParticipantResults().first().participant;
                    CompetitionMatch competitionMatchPrevious = competitionRoundLast.getCompetitionMatchFor(winner);


                    competitionMatchMerge.addPreviousCompetitionMatch(competitionMatchPrevious, 1, 1, null, 1);
                    competitionMatchPrevious.addNextCompetitionMatch(competitionMatchMerge, 1, 1, index, null);

                    competitionMatchPrevious.getCompetitionRound().afterDependenciesCompetitionRounds.add(competitionMatchMerge.getCompetitionRound());
                    competitionMatchMerge.getCompetitionRound().beforeDependenciesCompetitionRounds.add(competitionMatchPrevious.getCompetitionRound());
                    index++;
                }
            }
        }

        competitionPhase.initializePhase();
        competitionSeed.initializeSeed();


        //  logger.log(Level.FINE, "competitionInstance.open()");
        boolean generation = true;
        competitionInstance.continueCompetition(generation);
        //  logger.log(Level.FINE, "competitionSeed.open()");
//        competitionSeed.open();
//        competitionSeed.initializeCompetitionGroups();
        //  logger.log(Level.FINE, "competitionInstance.launchSimulation()");
//        competitionInstance.launchSimulation();
        competitionInstance.fillCompetitionMatchLink();
//        competitionInstance.fillExpectedRelativeTime();
        //  logger.log(Level.FINE, "[END]");


        return competitionSeed;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}

