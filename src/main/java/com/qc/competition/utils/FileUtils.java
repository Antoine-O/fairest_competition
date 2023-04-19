package com.qc.competition.utils;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 23/03/2016.
 */
public class FileUtils {
    protected static String CLASS = FileUtils.class.getSimpleName();



    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;

    }

    protected static Logger LOGGER_fillFileDataAndSizeFromResource = Logger.getLogger(CLASS + ".fillFileDataAndSizeFromResource");


    public static boolean isFileResourceAvailable(String filenameAndPath) {
        InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
        boolean result = inputStream != null;
        return result;
    }


    public static String getFileResourceContent(String filenameAndPath) throws IOException {
        InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
        String content = new String(IOUtils.toByteArray(inputStream));
        return content;
    }

    public static String getFileContent(String filenameAndPath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filenameAndPath)));
        return content;
    }


    public static List<String> getResourceActiveSubFolders(String resourceFolderPath) {
        List<String> activeSubFolders = new ArrayList<>();
        try {
            String folderString = getFileResourceContent(resourceFolderPath + "/active");
            String[] lines = folderString.split("\\r?\\n");
            activeSubFolders = Arrays.asList(lines);
            Collections.sort(activeSubFolders);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return activeSubFolders;
    }
}
