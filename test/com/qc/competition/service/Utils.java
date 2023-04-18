package com.qc.competition.service;

import org.apache.commons.io.IOUtils;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Duncan on 10/03/2017.
 */
public class Utils {
    private static Logger LOGGER = Logger.getLogger(Utils.class);

    public static String getCssContent() {
        InputStream inputStream = Utils.class.getResourceAsStream("/com/qc/competition/service/cartouche.css");
        String cssContent = "";
        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                cssContent = IOUtils.toString(inputStreamReader);
            } catch (IOException e) {
                LOGGER.error("/com/qc/competition/service/cartouche.css", e);
            }
        }
        return cssContent;
    }

    public static Object[][] toObjectMatrix(List<List<Object>> parameterSetList) {
        Object[][] computationParameters = new Object[parameterSetList.size()][parameterSetList.get(0).size()];
        int row = 0;
        for (List<Object> parameterSet : parameterSetList) {
            int col = 0;
            for (Object parameter : parameterSet) {
                computationParameters[row][col] = parameter;
                col++;
            }
            row++;
        }
        return computationParameters;
    }
}
