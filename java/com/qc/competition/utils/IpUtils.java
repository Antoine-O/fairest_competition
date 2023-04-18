package com.qc.competition.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class IpUtils {
    public static String IP;

    public static String getMyIp() {
        if (IP == null || IP.trim().isEmpty()) {
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                IP = in.readLine(); //you get the IP as a String
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return IP;
    }

    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public static Boolean headCheck(String url) {
        Boolean ok = false;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if ((responseCode >= 200 && responseCode < 300) || responseCode == 405) {
                ok = true;
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


        return ok;
    }


    public static Boolean getCheck(String url) {
        Boolean ok = false;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if ((responseCode >= 200 && responseCode < 300) || responseCode == 405) {
                ok = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


        return ok;
    }

    public static boolean checkInternetConnection() {
        return headCheck("http://www.google.com");
    }

    public static String getHostFromUrl(String url) {
        String host = null;
        if (!url.startsWith("http"))
            url += "http://" + url;
        URL netUrl = null;
        try {
            netUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (netUrl != null) {
            host = netUrl.getHost();
            if (host.startsWith("www")) {
                host = host.substring("www".length() + 1);
            }

        }
        return host;
    }
}
