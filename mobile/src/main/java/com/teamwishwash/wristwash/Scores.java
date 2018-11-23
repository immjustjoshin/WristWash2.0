package com.teamwishwash.wristwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
