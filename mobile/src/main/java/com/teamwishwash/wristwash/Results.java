package com.teamwishwash.wristwash;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Joshua on 11/9/2018.
 */

public class Results {

    /** ArrayList that holds the 6 accelerometer data files */
    private static ArrayList<ArrayList<ArrayList<Double>>> listOfAccelFiles = new ArrayList<>();

    /** List that holds the final scores for each hand-washing gesture */
    private static double scores[] = new double[6];

    // Setters
    public static void setRubbingPalmsScore(double result) {
        scores[0] = result;
    }

    public static void setRubbingBackOfHandsScore1(double result) {
        scores[1] = result;
    }

    public static void setRubbingBackOfHandsScore2(double result) {
        scores[2] = result;
    }

    public static void setRubbingFingersScore(double result) {
        scores[3] = result;
    }

    public static void setRubbingNailsScore1(double result) {
        scores[4] = result;
    }

    public static void setRubbingNailsScore2(double result) {
        scores[5] = result;
    }

    public static void setListOfAccelFiles(ArrayList<ArrayList<ArrayList<Double>>> list) {
        listOfAccelFiles = list;
    }

    // Getters
    public static String getRubbingPalmsScore() {
        return String.valueOf(scores[0]);
    }

    public static String getRubbingBackOfHandsScore() {
        return String.valueOf((scores[1] + scores[2]) / 2);
    }

    public static String getRubbingFingersScore() {
        return String.valueOf(scores[3]);
    }

    public static String getRubbingNailsScore() {
        return String.valueOf((scores[4] + scores[5]) / 2);
    }

    public static String getTotalScore() {
        double rubbingPalms = Double.valueOf(getRubbingPalmsScore());
        double rubbingBackOfHands = Double.valueOf(getRubbingBackOfHandsScore());
        double rubbingFingers = Double.valueOf(getRubbingFingersScore());
        double rubbingNails = Double.valueOf(getRubbingNailsScore());

        double average = (rubbingPalms + rubbingBackOfHands + rubbingFingers + rubbingNails) / 4;
        return String.valueOf(average);
    }


    /**
     * Method that returns the 6 accelerometer data files
     * that were recorded in the hand washing session. Each
     * accel file's data is in a 2-D array format. Each of
     * the 2-D array formatted accel files are then inside
     * another list which contains all 6 data files.
     * @return list of Accelerometer files of the hand-washing session
     */
    public static ArrayList<ArrayList<ArrayList<Double>>> getAccelFiles() {
        return listOfAccelFiles;
    }
}
