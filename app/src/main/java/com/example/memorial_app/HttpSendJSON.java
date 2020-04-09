package com.example.memorial_app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class HttpSendJSON {
    public static String postJson(final String strPostUrl, final String json) {
        int responseCode = 999;
        //proxy認証（学校では必要）
        System.setProperty("proxySet", "true");
        System.setProperty("proxyHost", "http://wproxy.net.sendai-nct.ac.jp");
        System.setProperty("proxyPort", "8080");
        final String authUser = "a1911503";
        final String authPassword = "Megmilk193_";
        java.net.Authenticator.setDefault(
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );
        HttpURLConnection uc = null;
        try {
            final URL url = new URL(strPostUrl);
            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("POST");
            uc.setUseCaches(false);
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            final OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(uc.getOutputStream()),
                    "UTF-8");
            out.write(json);
            out.close();

           // responseCode = uc.getResponseCode();

            InputStreamReader temp = new InputStreamReader(uc.getInputStream());
            final BufferedReader in = new BufferedReader(temp);
            String line = in.readLine();
            String body = "";
            while (line != null) {
                body = body + line;
                line = in.readLine();
            }
            uc.disconnect();
            return body;
        } catch (final IOException e) {
            e.printStackTrace();
            System.out.println("inputstream error " + uc.getErrorStream().toString());
            System.out.println("response code : " + responseCode);
            return "client - IOException : " + e.getMessage();
        }
    }
}
