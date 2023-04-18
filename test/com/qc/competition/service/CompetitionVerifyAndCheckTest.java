package com.qc.competition.service;

import com.qc.competition.service.structure.CompetitionInstance;
import com.qc.competition.service.template.CompetitionInstanceGeneratorException;
import com.qc.competition.utils.json.JSONUtils;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by Duncan on 09/11/2015.
 */
public class CompetitionVerifyAndCheckTest extends CompetitionDetailsGeneratorTest {
    private static Logger LOGGER = Logger.getLogger(CompetitionVerifyAndCheckTest.class);

    @Test
    public void testVerifyAndCheck() throws IOException, CompetitionInstanceGeneratorException {

        Logger logger = LOGGER;
        File file = new File(CompetitionVerifyAndCheckTest.class.getResource("/com/qc/competition/service/competition_in_invalid_state.json").getFile());
        CompetitionInstance competitionInstance = JSONUtils.fileToJSONObject(file, CompetitionInstance.class);
        competitionInstance.verifyStatus();


    }


}
