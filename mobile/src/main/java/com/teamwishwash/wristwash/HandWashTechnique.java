package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 9/24/2018.
 */

public class HandWashTechnique {
    private String handWashTechnique;
    private double score;

    // Constructor
    public HandWashTechnique(String handWashTechnique, double score) {
        this.handWashTechnique = handWashTechnique;
        this.score = score;
    }

    // Setters, getters
    public String getHandWashTechnique() {
        return handWashTechnique;
    }

    public void setHandWashTechnique(String handWashTechnique) {
        this.handWashTechnique = handWashTechnique;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
