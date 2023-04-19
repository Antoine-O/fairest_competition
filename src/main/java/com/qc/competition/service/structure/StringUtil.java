package com.qc.competition.service.structure;

import com.qc.competition.utils.json.JSONObject;
import com.qc.competition.utils.json.JSONUtils;

/**
 * Created by Duncan on 20/01/2015.
 */
public class StringUtil {
    public static String INDENTATION_STRING = "\t";
    public static String VERTICAL_ALIGNEMENT_CHAR = "|";
    public static String VERTICAL_ALIGNEMENT_CHAR_START = "*";

    public static String getIndentationForLevel(int level) {
        StringBuilder indentation = new StringBuilder();
        if (level > 0) {
            indentation.append(VERTICAL_ALIGNEMENT_CHAR);
            for (int i = 0; i < level - 1; i++) {
                indentation.append(INDENTATION_STRING);
                indentation.append(VERTICAL_ALIGNEMENT_CHAR);
            }
            indentation.append(INDENTATION_STRING);
            indentation.append(VERTICAL_ALIGNEMENT_CHAR_START);
        } else {
            indentation.append(VERTICAL_ALIGNEMENT_CHAR_START);
        }
        return indentation.toString();
    }

    public static String objectArrayToString(Object[] objects) {
        StringBuilder objectArrayString = null;
        for (Object object : objects) {
            if (object == null) {
                objectArrayString = new StringBuilder("'" + object + "'");
            } else {
                if (object instanceof JSONObject) {
                    objectArrayString.append(" , '").append(JSONUtils.jsonObjectToString((JSONObject) object)).append("'");
                } else {
                    objectArrayString.append(" , '").append(object.toString()).append("'");
                }
            }
        }
        return objectArrayString.toString();
    }

    public static String replaceRiskyCharacters(String string) {
        return string.replaceAll("([^a-zA-Z0-9]+|\\s)", "_");
    }
}
