package com.teamwishwash.wristwash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joshua on 11/8/2018.
 */

public class pythonRequests {
    static final String PYTHON_API_URL;
    public static String getScore(String x, String y) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(PYTHON_API_URL + x + "," + y);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Okie?";
    }
}
