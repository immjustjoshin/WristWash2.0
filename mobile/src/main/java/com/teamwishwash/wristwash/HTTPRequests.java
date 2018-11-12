package com.teamwishwash.wristwash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Joshua on 11/8/2018.
 */

public class HTTPRequests {
    private static final String BASE_URL ="https://wishwash.herokuapp.com/";
//    private static AsyncHttpClient client = new AsynchHttpClient();

    public static void sendData(ArrayList<ArrayList<Double>> data) {
        StringBuilder result = new StringBuilder();
        // Results.getAccelFiles() to get list of accel files and send each file up one by one to server for preprocessing

    }
    public static void getScore(int x, int y) {
        // Do http call to get the score of each individual gesture
        // Do for loop from 1 to 6 and if i = 1 do Results.setRubbingPalmsScore()
        // If i = 2 do Results.setRubbingBackOfHandsScore1() then continue for rest of gestures
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
