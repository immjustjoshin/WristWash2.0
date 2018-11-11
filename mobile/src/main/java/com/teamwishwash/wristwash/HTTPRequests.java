package com.teamwishwash.wristwash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joshua on 11/8/2018.
 */

public class HTTPRequests {
    private static final String BASE_URL ="https://wishwash.herokuapp.com/";
//    private static AsyncHttpClient client = new AsynchHttpClient();

    public static void preprocess(long[] data) {
        StringBuilder result = new StringBuilder();

    }
    public static void getScore(int x, int y) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(BASE_URL + String.valueOf(x) + "," + String.valueOf(y));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Results.setRubbingPalmsScore(Integer.valueOf(result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
