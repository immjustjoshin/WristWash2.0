package com.teamwishwash.wristwash;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Joshua on 11/9/2018.
 */

public class Results implements Serializable {

    /** ArrayList that holds the 6 accelerometer data files */
    private ArrayList<ArrayList<ArrayList<Double>>> listOfAccelFiles = new ArrayList<>();

    /** List that holds the scores received from API server for each hand-washing gesture */
    private double scores[] = new double[6];

    /** List that holds the final scores after combining and curving scores for each hand-washing gesture*/
    private ArrayList<Double> finalScores = new ArrayList<>();

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
        combineScores();
    }

    /**
     * This method combines the 6 scores into 5 scores.
     * It will combine the score both the rubbing the back of
     * hands score and combine the rubbing under both nails scores.
     * This will leave 4 scores. A fifth score will be added to indicate
     * the total score. All scores will be given a square root curve
     * if scores is above 35 percent accuracy.
     */
    private void combineScores() {
                                                                     // Score is a percentage out of 100
        double gesture1 = scores[0] * 100;                           // Rubbing palms score
        double gesture2 = ((scores[1] + scores[2]) / 2) * 100;       // Rubbing back of hands core
        double gesture3 = scores[3] * 100;                           // Rubbing fingers score
        double gesture4 = ((scores[4] + scores[5]) / 2) * 100;       // Rubbing under nails score

        // Apply square root curve as the scoring system
        if (gesture1 > 35) {
            gesture1 = applySquareRootCurve(gesture1);
        }
        if (gesture2 > 35) {
            gesture2 = applySquareRootCurve(gesture2);
        }
        if (gesture3 > 35) {
            gesture3 = applySquareRootCurve(gesture3);
        }
        if (gesture4 > 35) {
            gesture4 = applySquareRootCurve(gesture4);
        }

        // Formats scores to one decimal place
        gesture1 = formatDecimals(gesture1 / 10);
        gesture2 = formatDecimals(gesture2 / 10);
        gesture3 = formatDecimals(gesture3 / 10);
        gesture4 = formatDecimals(gesture4 / 10);

        // Total Score of all gestures combined
        double totalScore = (gesture1 + gesture2 + gesture3 + gesture4) / 4;
        totalScore = formatDecimals(totalScore);

        finalScores.add(gesture1);        // Adds rubbing palms score
        finalScores.add(gesture2);        // Adds rubbing back of hands score
        finalScores.add(gesture3);        // Adds rubbing fingers score
        finalScores.add(gesture4);        // Adds rubbing under nails score
        finalScores.add(totalScore);      // Adds total score
    }

    /**
     * Formats score to be out of 10 instead of 100.
     * Also uses only 1 decimal place.
     * @param score score to be formatted
     * @return formatted score
     */
    private double formatDecimals(double score) {
        DecimalFormat oneDecimal = new DecimalFormat("#.#");
        return Double.valueOf(oneDecimal.format(score));
    }

    /**
     * Applys a square root curve
     * @param score score to be curved
     * @return curved score
     */
    private double applySquareRootCurve(double score) {
        return Math.sqrt(score) * 10;
    }


    // Getters
    public double getRubbingPalmsScore() {
        return finalScores.get(0);
    }

    public double getRubbingBackOfHandsScore() {
        return finalScores.get(1);
    }

    public double getRubbingFingersScore() {
        return finalScores.get(2);
    }

    public double getRubbingNailsScore() {
        return finalScores.get(3);
    }

    public double getTotalScore() {
        return finalScores.get(4);
    }
}
