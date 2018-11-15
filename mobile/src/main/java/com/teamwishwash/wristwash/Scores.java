package com.teamwishwash.wristwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Scores extends AppCompatActivity {

    public static final String HAND_WASHING_TECHNIQUE = "hand washing technique";
    public static final String HAND_WASH_SCORE = "hand wash score";
    public static final String FINAL_SCORES = "final scores";

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

        // gets final scores for all hand washing gestures
        Intent intent = getIntent();
        double[] scores = intent.getDoubleArrayExtra(Scores.FINAL_SCORES);
        ArrayList<Double> finalScores = combineScores(scores);

        // Adds hand washing technique and scores to list
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", finalScores.get(0)));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", finalScores.get(1)));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", finalScores.get(2)));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", finalScores.get(3)));

        double totalScore = finalScores.get(4);
        score.setText(String.valueOf(totalScore));
        if (totalScore >= 0 && totalScore < 4) {
            score.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (totalScore >=4 && totalScore < 8) {
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
    public void showDetails(String technique, double score) {
        Intent intent = new Intent(this, DetailedScores.class);
        intent.putExtra(HAND_WASHING_TECHNIQUE, technique);
        intent.putExtra(HAND_WASH_SCORE, score);
        startActivity(intent);
    }

    /**
     * This method combines the 6 scores into 5 scores.
     * It will combine the score both the rubbing the back of
     * hands score and combine the rubbing under both nails scores.
     * This will leave 4 scores. A fifth score will be added to indicate
     * the total score.
     * @param list list of scores for the 6 hand washing gestures
     * @return list that has the scores for the 4 hand washing techniques & a total score
     */
    public ArrayList<Double> combineScores(double[] list) {
        ArrayList<Double> finalScores = new ArrayList<>();
        finalScores.add(list[0] * 10);                          // Adds rubbing palms score
        finalScores.add(((list[1] + list[2]) / 2) * 10);        // Adds rubbing back of hands score
        finalScores.add(list[3] * 10);                          // Adds rubbing fingers score
        finalScores.add(((list[4] + list[5]) / 2) * 10);        // Adds rubbing under nails score

        double totalScore = 0;
        for (int i = 0; i < list.length; i++) {
            totalScore += list[i];
        }
        totalScore = (totalScore / 6) * 10;
        finalScores.add(totalScore);
        return finalScores;
    }

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
