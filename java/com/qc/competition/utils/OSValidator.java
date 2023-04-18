package com.qc.competition.utils;

import java.io.File;

/**
 * Created by Duncan on 21/06/2016.
 */
public class OSValidator {
    private static String OS = System.getProperty("os.name").toLowerCase();


    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

    public static String getOS() {
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

    public static String getDefaultTmpDir() {
        if (isWindows()) {
            return "c:" + File.separator + "temp";
        } else if (isMac()) {
            return File.separator + "tmp";
        } else if (isUnix()) {
            return File.separator + "tmp";
        } else if (isSolaris()) {
            return File.separator + "tmp";
        } else {
            return File.separator + "tmp";
        }
    }
}
