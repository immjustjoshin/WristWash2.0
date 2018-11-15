package com.teamwishwash.wristwash;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joshua on 11/9/2018.
 */

public class Results implements Serializable {

    /** ArrayList that holds the 6 accelerometer data files */
    private ArrayList<ArrayList<ArrayList<Double>>> listOfAccelFiles = new ArrayList<>();

    /** List that holds the final scores for each hand-washing gesture */
    private double scores[] = new double[6];


    // Sets all the files and scores
    /**
     * Method that returns the 6 accelerometer data files
     * that were recorded in the hand washing session. Each
     * accel file's data is in a 2-D array format. Each of
     * the 2-D array formatted accel files are then inside
     * another list which contains all 6 data files.
     * @return list of Accelerometer files of the hand-washing session
     */
    public ArrayList<ArrayList<ArrayList<Double>>> getListOfAccelFiles() {
        return listOfAccelFiles;
    }

    public void setListOfAccelFiles(ArrayList<ArrayList<ArrayList<Double>>> listOfAccelFiles) {
        this.listOfAccelFiles = listOfAccelFiles;
    }

    public double[] getScores() {
        return scores;
    }

    public void setScores(double[] scores) {
        this.scores = scores;
    }



    // Getters and setters for each individual hand washing score & total score

    // Getters
    public String getRubbingPalmsScore() {
        return String.valueOf(scores[0]);
    }

    public String getRubbingBackOfHandsScore() {
        return String.valueOf((scores[1] + scores[2]) / 2);
    }

    public String getRubbingFingersScore() {
        return String.valueOf(scores[3]);
    }

    public String getRubbingNailsScore() {
        return String.valueOf((scores[4] + scores[5]) / 2);
    }

    public String getTotalScore() {
        double rubbingPalms = Double.valueOf(getRubbingPalmsScore());
        double rubbingBackOfHands = Double.valueOf(getRubbingBackOfHandsScore());
        double rubbingFingers = Double.valueOf(getRubbingFingersScore());
        double rubbingNails = Double.valueOf(getRubbingNailsScore());

        double average = (rubbingPalms + rubbingBackOfHands + rubbingFingers + rubbingNails) / 4;
        return String.valueOf(average);
    }

    // Setters
    public void setRubbingPalmsScore(double result) {
        scores[0] = result;
    }

    public void setRubbingBackOfHandsScore1(double result) {
        scores[1] = result;
    }

    public void setRubbingBackOfHandsScore2(double result) {
        scores[2] = result;
    }

    public void setRubbingFingersScore(double result) {
        scores[3] = result;
    }

    public void setRubbingNailsScore1(double result) {
        scores[4] = result;
    }

    public void setRubbingNailsScore2(double result) {
        scores[5] = result;
    }
}
