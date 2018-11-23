package com.teamwishwash.wristwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Scores extends AppCompatActivity {

    // layouts
    TextView score;
    ListView detailList;
    List<HandWashTechnique> handWashTechniqueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Add back button to navigation bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // declaring screen layouts
        score = (TextView) findViewById(R.id.scoreTextView);
        detailList = (ListView) findViewById(R.id.detailsListView);

        // gets final scores for all hand washing gestures from intent
        Intent intent = getIntent();
        double[] scores = intent.getDoubleArrayExtra(Constants.VALUES.FINAL_SCORES);
        Results res = new Results();
        res.setScores(scores);
//        ArrayList<Double> finalScores = combineScores(scores);

        // Adds hand washing technique and scores to list
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", res.getRubbingPalmsScore()));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", res.getRubbingBackOfHandsScore()));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", res.getRubbingFingersScore()));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", res.getRubbingNailsScore()));

        double totalScore = res.getTotalScore();
        score.setText(String.valueOf(totalScore));

        // Sets color of score
        if (totalScore >= 0 && totalScore < 6) {
            score.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (totalScore >=6 && totalScore < 8) {
            score.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
        } else if (totalScore >= 8 && totalScore <= 10) {
            score.setTextColor(getResources().getColor(R.color.green, getTheme()));
        }

        // Init custom list adapter
        HandWashListAdapter detailScoresAdapter = new HandWashListAdapter(getApplicationContext(), handWashTechniqueList);
        detailList.setAdapter(detailScoresAdapter);

        // goes to more hand washing details on click
        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HandWashTechnique listItem = (HandWashTechnique) adapterView.getItemAtPosition(i);
                String technique = listItem.getHandWashTechnique();
                double score = listItem.getScore();
                showDetails(technique, score);
            }
        });
    }

    /**
     * Shows the details of a specific hand washing technique
     * after clicking on it in the list. Goes to new activity
     * screen called DetailScores.
     *
     * @param technique name of the hand washing technique that user pressed on
     */
    private void showDetails(String technique, double score) {
        Intent intent = new Intent(this, DetailedScores.class);
        intent.putExtra(Constants.VALUES.HAND_WASHING_TECHNIQUE, technique);
        intent.putExtra(Constants.VALUES.HAND_WASH_SCORE, score);
        startActivity(intent);
    }

//    /**
//     * This method combines the 6 scores into 5 scores.
//     * It will combine the score both the rubbing the back of
//     * hands score and combine the rubbing under both nails scores.
//     * This will leave 4 scores. A fifth score will be added to indicate
//     * the total score.
//     * @param list list of scores for the 6 hand washing gestures
//     * @return list that has the scores for the 4 hand washing techniques & a total score
//     */
//    private ArrayList<Double> combineScores(double[] list) {
//        ArrayList<Double> finalScores = new ArrayList<>();       // Score is a percentage out of 100
//        double gesture1 = list[0] * 100;                         // Rubbing palms score
//        double gesture2 = ((list[1] + list[2]) / 2) * 100;       // Rubbing back of hands core
//        double gesture3 = list[3] * 100;                         // Rubbing fingers score
//        double gesture4 = ((list[4] + list[5]) / 2) * 100;       // Rubbing under nails score
//
//        // Apply square root curve as the scoring system
//        if (gesture1 > 35) {
//            gesture1 = applySquareRootCurve(gesture1);
//        }
//        if (gesture2 > 35) {
//            gesture2 = applySquareRootCurve(gesture2);
//        }
//        if (gesture3 > 35) {
//            gesture3 = applySquareRootCurve(gesture3);
//        }
//        if (gesture4 > 35) {
//            gesture4 = applySquareRootCurve(gesture4);
//        }
//
//        // Formats scores to one decimal place
//        gesture1 = formatDecimals(gesture1 / 10);
//        gesture2 = formatDecimals(gesture2 / 10);
//        gesture3 = formatDecimals(gesture3 / 10);
//        gesture4 = formatDecimals(gesture4 / 10);
//
//        // Total Score of all gestures combined
//        double totalScore = (gesture1 + gesture2 + gesture3 + gesture4) / 4;
//        totalScore = formatDecimals(totalScore);
//
//        finalScores.add(gesture1);        // Adds rubbing palms score
//        finalScores.add(gesture2);        // Adds rubbing back of hands score
//        finalScores.add(gesture3);        // Adds rubbing fingers score
//        finalScores.add(gesture4);        // Adds rubbing under nails score
//        finalScores.add(totalScore);      // Adds total score
//        return finalScores;
//    }
//
//    /**
//     * Formats score to be out of 10 instead of 100.
//     * Also uses only 1 decimal place.
//     * @param score score to be formatted
//     * @return formatted score
//     */
//    private double formatDecimals(double score) {
//        DecimalFormat oneDecimal = new DecimalFormat("#.#");
//        return Double.valueOf(oneDecimal.format(score));
//    }
//
//    /**
//     * Applys a square root curve
//     * @param score score to be curved
//     * @return curved score
//     */
//    private double applySquareRootCurve(double score) {
//        return Math.sqrt(score) * 10;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // ends activity when back is pressed
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
