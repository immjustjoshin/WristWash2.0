package com.teamwishwash.wristwash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamwishwash.shared.SharedConstants;

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

        /** SharedPreferences set up. Used for saving recent scores and for watch to access those scores */
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SharedConstants.VALUES.SCORE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // Add back button to navigation bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // declaring screen layouts
        score = (TextView) findViewById(R.id.scoreTextView);
        detailList = (ListView) findViewById(R.id.detailsListView);

        // Score placeholders
        double palmScore, backScore, fingerScore, nailScore, totalScore;

        // Intent related
        Intent intent = getIntent();
        Results res = new Results();


        if (pref.getBoolean("Session", false)) {
            // gets final scores for all hand washing gestures from session
            intent = getIntent();
            double[] scores = intent.getDoubleArrayExtra(Constants.VALUES.FINAL_SCORES);
            res.setScores(scores);

            palmScore = res.getRubbingPalmsScore();
            backScore = res.getRubbingBackOfHandsScore();
            fingerScore = res.getRubbingFingersScore();
            nailScore = res.getRubbingNailsScore();
            totalScore = res.getTotalScore();

            // Saves final scores in SharedPreferences
            editor.putFloat("Palms", (float) palmScore);
            editor.putFloat("Back", (float) backScore);
            editor.putFloat("Fingers", (float) fingerScore);
            editor.putFloat("Nails", (float) nailScore);
            editor.putFloat("Total", (float) totalScore);
            editor.apply();
        } else {
            // Goes here if get most recent score was pressed
            palmScore = Results.formatDecimals(pref.getFloat("Palms", 0));
            backScore = Results.formatDecimals(pref.getFloat("Back", 0));
            fingerScore = Results.formatDecimals(pref.getFloat("Fingers", 0));
            nailScore = Results.formatDecimals(pref.getFloat("Nails", 0));
            totalScore = Results.formatDecimals(pref.getFloat("Total", 0));
        }

        // Adds hand washing technique and scores to list
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", palmScore));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", backScore));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", fingerScore));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", nailScore));

        score.setText(String.valueOf(totalScore));

        // Sets color of score based on performance
        if (totalScore >= 0 && totalScore < 6) {
            score.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (totalScore >=6 && totalScore < 8) {
            score.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
        } else if (totalScore >= 8 && totalScore <= 10) {
            score.setTextColor(getResources().getColor(R.color.green, getTheme()));
        }

        // Inits & sets custom list adapter
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
