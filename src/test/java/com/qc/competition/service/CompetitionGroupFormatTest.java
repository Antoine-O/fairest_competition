package com.qc.competition.service;

import com.qc.competition.service.structure.CompetitionGroupFormat;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Duncan on 19/02/2015.
 */
public class CompetitionGroupFormatTest {

    private static Logger LOGGER_testNumberOfByes = Logger.getLogger(CompetitionGroupFormatTest.class.getName() + ".testNumberOfByes");

    @DataProvider(name = "numberOfByesProvider", parallel = false)
    public Object[][] numberOfByesProvider() {
        String logPrefix = "[numberOfByesProvider]";
        List<List<Object>> parameterSetList = new ArrayList<>();
        {
            for (int i = 2; i < 100; i++) {
                parameterSetList.add(createNumberOfByesProviderEntry(i, 2, 1, i % 2));
            }

            for (int i = 4; i < 100; i++) {
                parameterSetList.add(createNumberOfByesProviderEntry(i, 4, 1, 0));
            }

            parameterSetList.add(createNumberOfByesProviderEntry(5, 4, 2, 1));
            for (int i = 6; i < 100; i++) {
                parameterSetList.add(createNumberOfByesProviderEntry(i, 4, 2, 0));
            }

        }
        Object[][] computationParameters = Utils.toObjectMatrix(parameterSetList);
        return computationParameters;
    }

    private List<Object> createNumberOfByesProviderEntry(int participantQuantity, int numberOfParticipantPerMatch, int participantQualifiedPerMatch, int expectedNumberOfByes) {
        List<Object> parameterSet = new ArrayList<>();
        parameterSet.add(participantQuantity);
        parameterSet.add(numberOfParticipantPerMatch);
        parameterSet.add(participantQualifiedPerMatch);
        parameterSet.add(expectedNumberOfByes);
        return parameterSet;
    }

    @Test(dataProvider = "numberOfByesProvider")
    public void testNumberOfByes(int participantQuantity, int numberOfParticipantPerMatch, int participantQualifiedPerMatch, int expectedNumberOfByes) {
        int expectedNumberOfByesComputed = CompetitionGroupFormat.CUSTOM.getNumberOfByes(participantQuantity, numberOfParticipantPerMatch, participantQualifiedPerMatch);
        LOGGER_testNumberOfByes.info("participantQuantity:" + participantQuantity + ", numberOfParticipantPerMatch:" + numberOfParticipantPerMatch + ", participantQualifiedPerMatch:" + participantQualifiedPerMatch + ", expectedNumberOfByes:" + expectedNumberOfByes + ",expectedNumberOfByesComputed:" + expectedNumberOfByesComputed);
        Assert.assertEquals(expectedNumberOfByesComputed, expectedNumberOfByes);

    }
}
