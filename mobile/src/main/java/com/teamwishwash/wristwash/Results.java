package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 11/9/2018.
 */

public class Results {

    public static double scores[] = new double[6];

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
}
