package com.example;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class RestconfClientManager {

    private String authHeader;

    public RestconfClientManager(String username, String password) {
        try {
            // Initialize SSL context to trust all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create a hostname verifier that accepts all hostnames
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            // Create the Basic Authentication header
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
            this.authHeader = "Basic " + new String(encodedAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConfig(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/yang-data+xml"); // Set Accept header to application/yang-data+json
        conn.setRequestProperty("Authorization", authHeader); // Set Authorization header
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        System.out.println("GET Request URL: " + urlStr);
        System.out.println("GET Response Code: " + responseCode);

        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("GET Response: " + response.toString());
            return response.toString();
        }
    }

    public String putConfig(String urlStr, String requestBody) throws Exception {
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept", "application/yang-data+xml"); // Set Accept header to application/yang-data+json
        conn.setRequestProperty("Content-Type", "application/yang-data+xml"); // Set Content-Type to application/yang-data+xml
        conn.setRequestProperty("Authorization", authHeader); // Set Authorization header
        conn.setDoOutput(true);

        System.out.println("PUT Request URL: " + urlStr);
        System.out.println("PUT Request Body: " + requestBody);

        try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
            out.write(requestBody);
            out.flush();
        }

        int responseCode = conn.getResponseCode();
        System.out.println("PUT Response Code: " + responseCode);

        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("PUT Response: " + response.toString());
            return response.toString();
        }
    }

    public void close() {
        // Clean up resources if needed
    }
}
