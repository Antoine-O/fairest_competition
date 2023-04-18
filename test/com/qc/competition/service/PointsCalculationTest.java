package com.qc.competition.service;

import com.qc.competition.service.structure.IdGenerator;
import com.qc.competition.service.structure.ParticipantResult;
import com.qc.competition.service.structure.ParticipantSingle;
import com.qc.competition.service.structure.PointsCalculation;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Duncan on 02/05/2017.
 */
public class PointsCalculationTest {
    private static Logger LOGGER = Logger.getLogger(PointsCalculationTest.class);

    @DataProvider(name = "pointsCalculationFullProvider")
    public Object[][] pointsCalculationFullProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[computationParameterFormatQuickProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 2; i < 8; i++) {
            int participantsSize = i;
            for (int j = i; j < 8; j++) {
                int participantsMaxSize = j;
                for (int k = 0; k < 2; k++) {
                    boolean bye = ((k % 2) == 1);
                    for (int l = 0; (l < 9 && !bye) || (bye && l < 1); l++) {
                        int round = l + 1;
                        for (int m = l; m < 9; m++) {
                            int maxRound = m + 1;
                            List<Object> parameterSet = addParameterSet(parameterSetList, bye, participantsSize, participantsMaxSize, round, maxRound);
                            logger.info(logPrefix + "\t" + parameterSet);
                        }
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "pointsCalculationByeProvider")
    public Object[][] pointsCalculationByeProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[pointsCalculationByeProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            int participantsSize = i;
            for (int j = i + 1; j < 8; j++) {
                int participantsMaxSize = j;
                boolean bye = true;
                for (int l = 0; (l < 9 && !bye) || (bye && l < 1); l++) {
                    int round = l + 1;
                    for (int m = l; m < 9; m++) {
                        int maxRound = m + 1;
                        List<Object> parameterSet = addParameterSet(parameterSetList, bye, participantsSize, participantsMaxSize, round, maxRound);
                        logger.info(logPrefix + "\t" + parameterSet);
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }

    @DataProvider(name = "pointsCalculationNotByeProvider")
    public Object[][] pointsCalculationNotByeProvider() {
        Logger logger = LOGGER;
        String logPrefix = "[pointsCalculationNotByeProvider]";

        logger.info(logPrefix);
        List<List<Object>> parameterSetList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            int participantsSize = i;
            for (int j = i + 1; j < 8; j++) {
                int participantsMaxSize = j;
                boolean bye = false;
                for (int l = 0; (l < 9 && !bye) || (bye && l < 1); l++) {
                    int round = l + 1;
                    for (int m = l; m < 9; m++) {
                        int maxRound = m + 1;
                        List<Object> parameterSet = addParameterSet(parameterSetList, bye, participantsSize, participantsMaxSize, round, maxRound);
                        logger.info(logPrefix + "\t" + parameterSet);
                    }
                }
            }
        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        logger.info(logPrefix + "\t" + computationParameters.length + " parameter created");
        return computationParameters;
    }


    private List<Object> addParameterSet(List<List<Object>> parameterSetList, boolean bye, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        List<Object> parameterSet = new ArrayList<>();
        parameterSet.add(bye);
        parameterSet.add(participantsSize);
        parameterSet.add(participantsMaxSize);
        parameterSet.add(round);
        parameterSet.add(maxRound);
        parameterSetList.add(parameterSet);
        return parameterSet;

    }

    @Test(dataProvider = "pointsCalculationFullProvider")
    public void fullTest(boolean bye, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        test(bye, participantsSize, participantsMaxSize, round, maxRound);
    }

    @Test(dataProvider = "pointsCalculationByeProvider")
    public void byeTest(boolean bye, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        test(bye, participantsSize, participantsMaxSize, round, maxRound);
    }


    @Test(dataProvider = "pointsCalculationNotByeProvider")
    public void notByeTest(boolean bye, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        test(bye, participantsSize, participantsMaxSize, round, maxRound);
    }


    @SuppressWarnings("deprecation")
    public void test(boolean bye, int participantsSize, int participantsMaxSize, int round, int maxRound) {
        Integer lastPointValue = null;
        Integer newPointValue = null;
        String logPrefix = "";
        for (int i = 0; (!bye && i < participantsSize) || (bye && i < participantsMaxSize / 2); i++) {
            int rank = i + 1;
            logPrefix = "[rank:" + rank + "]";
            newPointValue = PointsCalculation.getPoints(bye, rank, participantsSize, participantsMaxSize, round, maxRound);
            LOGGER.info(logPrefix + "\t" + "lastPointValue=" + lastPointValue + "\tnewPointValue=" + newPointValue);
            if (newPointValue <= 0) {
                Assert.assertTrue(newPointValue <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
            }
            if (lastPointValue != null) {
                if (!bye && lastPointValue.compareTo(newPointValue) <= 0) {
                    Assert.assertFalse(lastPointValue.compareTo(newPointValue) <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                } else if (bye && lastPointValue.compareTo(newPointValue) != 0) {
                    Assert.assertFalse(lastPointValue.compareTo(newPointValue) != 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                }
            }
            lastPointValue = newPointValue;
        }
    }

    private Set<ParticipantResult> getParticipantResultsSample(Integer participantsSize) {
        Set<ParticipantResult> participantResults = new HashSet<>();
        IdGenerator idGenerator = new IdGenerator();
        for (int i = 0; i < participantsSize; i++) {
            ParticipantResult participantResult = ParticipantResult.createParticipantResultFor(idGenerator, null);
            participantResult.participant = ParticipantSingle.createInstance(idGenerator);
            participantResult.rank = i + 1;
            participantResults.add(participantResult);
        }
        return participantResults;
    }

    @Test
    public void testPlayPoints() {
        String logPrefix = "";


        for (int participantsSize = 2; participantsSize <= 100; participantsSize++) {
            Set<ParticipantResult> participantResults = getParticipantResultsSample(participantsSize);
            Integer lastPointValue = null;
            Integer newPointValue = null;
            for (int i = 0; i < participantsSize; i++) {
                int rank = i + 1;
                logPrefix = "[rank:" + rank + "]";
                newPointValue = PointsCalculation.getPlayPoints(i + 1, participantResults);
                LOGGER.info(logPrefix + "\t" + "lastPointValue=" + lastPointValue + "\tnewPointValue=" + newPointValue);
                if (newPointValue <= 0) {
                    Assert.assertTrue(newPointValue <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                }
                if (lastPointValue != null) {
                    if (lastPointValue.compareTo(newPointValue) <= 0) {
                        Assert.assertFalse(lastPointValue.compareTo(newPointValue) <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                    }
                }
                lastPointValue = newPointValue;
            }
        }
    }

    @Test
    public void testMatchPoints() {
        String logPrefix = "";
        for (int participantsSize = 2; participantsSize <= 100; participantsSize++) {
            Integer numberOfParticipantQualified = participantsSize / 2;
            Set<ParticipantResult> participantResults = getParticipantResultsSample(participantsSize);
            for (int maxMatchQuantity = 1; maxMatchQuantity < 9; maxMatchQuantity++) {
                for (int minMatchQuantity = 1; minMatchQuantity < maxMatchQuantity / 2 + 1; minMatchQuantity = minMatchQuantity + 2) {
                    for (int matchPlayedQuantity = minMatchQuantity; matchPlayedQuantity < maxMatchQuantity; matchPlayedQuantity++) {
                        Integer lastPointValue = null;
                        Integer newPointValue = null;
                        for (int i = 0; i < participantsSize; i++) {
                            int rank = i + 1;
                            logPrefix = "[rank:" + rank + "][participantsSize:" + participantsSize + "][maxMatchQuantity:" + maxMatchQuantity + "][minMatchQuantity:" + minMatchQuantity + "][matchPlayedQuantity:" + matchPlayedQuantity + "][numberOfParticipantQualified:" + numberOfParticipantQualified + "]";
                            newPointValue = PointsCalculation.getMatchPoints(rank, participantResults, minMatchQuantity, matchPlayedQuantity, numberOfParticipantQualified);
                            LOGGER.info(logPrefix + "\t" + "lastPointValue=" + lastPointValue + "\tnewPointValue=" + newPointValue);
                            if (newPointValue <= 0) {
                                Assert.assertTrue(newPointValue <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                            }
                            if (lastPointValue != null) {
                                if (lastPointValue.compareTo(newPointValue) <= 0) {
                                    Assert.assertFalse(lastPointValue.compareTo(newPointValue) <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                                }
                            }
                            lastPointValue = newPointValue;
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testMatchPoints2players3plays() {
        String logPrefix = "";
        int participantsSize = 2;
        Integer numberOfParticipantQualified = participantsSize / 2;
        int maxMatchQuantity = 5;
        int minMatchQuantity = 3;
        {
            int matchPlayedQuantity = 3;
            Integer lastPointValue = null;
            Integer newPointValue = null;
            for (int i = 0; i < participantsSize; i++) {
                Set<ParticipantResult> participantResults = getParticipantResultsSample(participantsSize);
                int rank = i + 1;
                logPrefix = "[rank:" + rank + "][pSize:" + participantsSize + "][maxMatchQty:" + maxMatchQuantity + "][minMatchQty:" + minMatchQuantity + "][matchPlayedQty:" + matchPlayedQuantity + "][qualified:" + numberOfParticipantQualified + "]";
                newPointValue = PointsCalculation.getMatchPoints(rank, participantResults, minMatchQuantity, matchPlayedQuantity, numberOfParticipantQualified);
                LOGGER.info(logPrefix + "\t" + "lastPointValue=" + lastPointValue + "\tnewPointValue=" + newPointValue);
                if (newPointValue <= 0) {
                    Assert.assertTrue(newPointValue <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                }
                if (lastPointValue != null) {
                    if (lastPointValue.compareTo(newPointValue) <= 0) {
                        Assert.assertFalse(lastPointValue.compareTo(newPointValue) <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                    }
                }
                lastPointValue = newPointValue;
            }
        }
        maxMatchQuantity = 7;
        minMatchQuantity = 4;
        {
            int matchPlayedQuantity = 4;
            Integer lastPointValue = null;
            Integer newPointValue = null;
            for (int i = 0; i < participantsSize; i++) {
                Set<ParticipantResult> participantResults = getParticipantResultsSample(participantsSize);
                int rank = i + 1;
                logPrefix = "[rank:" + rank + "][pSize:" + participantsSize + "][maxMatchQty:" + maxMatchQuantity + "][minMatchQty:" + minMatchQuantity + "][matchPlayedQty:" + matchPlayedQuantity + "][qualified:" + numberOfParticipantQualified + "]";
                newPointValue = PointsCalculation.getMatchPoints(rank, participantResults, minMatchQuantity, matchPlayedQuantity, numberOfParticipantQualified);
                LOGGER.info(logPrefix + "\t" + "lastPointValue=" + lastPointValue + "\tnewPointValue=" + newPointValue);
                if (newPointValue <= 0) {
                    Assert.assertTrue(newPointValue <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                }
                if (lastPointValue != null) {
                    if (lastPointValue.compareTo(newPointValue) <= 0) {
                        Assert.assertFalse(lastPointValue.compareTo(newPointValue) <= 0, "lastPointValue=" + lastPointValue + " newPointValue=" + newPointValue);
                    }
                }
                lastPointValue = newPointValue;
            }
        }
    }
}
