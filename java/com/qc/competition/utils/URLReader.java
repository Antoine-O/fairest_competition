package com.qc.competition.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Created by Duncan on 01/02/2016.
 */
//@Logged//(level = "INFO", logContext = "UTILS")
public class URLReader {


    /**
     * get content of url with contenttype / name / binarydata and data64
     *
     * @param urlString a string representing the url
     * @return Content a {@link Content}
     * @throws IOException
     */
    public static Content getUrlContent(String urlString) throws IOException {
        Content content = new Content();
        content.name = FilenameUtils.getName(urlString);
        content.contentType = getContentType(urlString);
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        content.data = IOUtils.toByteArray(is);
        content.data64 = Base64.getEncoder().encodeToString(content.data);

        return content;
    }

    /**
     * Http HEAD Method to get URL content type
     *
     * @param urlString
     * @return content type
     * @throws IOException
     */
    public static String getContentType(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        if (isRedirect(connection.getResponseCode())) {
            String newUrl = connection.getHeaderField("Place"); // get redirect url from "location" header field
//            logger.warn("Original request URL: '{}' redirected to: '{}'", urlString, newUrl);
            return getContentType(newUrl);
        }
        String contentType = connection.getContentType();
        return contentType;
    }

    /**
     * Check status code for redirects
     *
     * @param statusCode
     * @return true if matched redirect group
     */
    protected static boolean isRedirect(int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_OK) {
            return statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                    || statusCode == HttpURLConnection.HTTP_SEE_OTHER;
        }
        return false;
    }

    public String readToString(String urlString) {
        String urlStringContent = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                stringBuilder.append(tmp);
            }

            urlStringContent = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urlStringContent;
    }

    public static class Content {
        public String name;
        public byte[] data;
        public String data64;
        public String contentType;
    }
}


