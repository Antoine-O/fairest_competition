package com.qc.competition.utils;

import org.apache.commons.io.IOUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 23/03/2016.
 */
public class StringUtils {
    private static final Logger LOGGER = Logger.getLogger(StringUtils.class.getSimpleName());

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public static String cutAt(String string, int maxLength) {
        String cuttedString = string != null ? string.trim() : "";
        if (cuttedString.trim().length() > maxLength) {
            cuttedString = cuttedString.trim().substring(0, maxLength - "…".length()) + "…";
        }
        return cuttedString;
    }

    public static String numberWithTrailingZeros(int number, int maxLength) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(maxLength);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(number);
    }

    public static String readerToString(Reader reader) {
        String result = null;
        try {
//            reader.reset();
            result = IOUtils.toString(reader);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to convert reader to string", e);

        }
        return result;
    }

    public static String toMD5Hash(String str) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            result = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }

    public static String toUniqueLabel(String uniqueLabel) {
        uniqueLabel = StringUtils.stripAccents(uniqueLabel).replaceAll("\\s+", " ").replaceAll(",\\s", ",").replaceAll("\\s,", ",").replaceAll("\\s+", "_").trim().toUpperCase();
        Set<String> uniqueLabelTags = new LinkedHashSet(Arrays.asList(uniqueLabel.split(",")));
        uniqueLabel = String.join(",", uniqueLabelTags);
        return uniqueLabel.toLowerCase();
    }

    public static String stripSpaces(String string) {
        return string.replaceAll("\\s+", "_");
    }
}

