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


        // Adds hand washing technique and scores to list
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", Double.valueOf(Results.getRubbingPalmsScore())));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", Double.valueOf(Results.getRubbingBackOfHandsScore())));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", Double.valueOf(Results.getRubbingFingersScore())));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", Double.valueOf(Results.getRubbingNailsScore())));

        double average = Double.valueOf(Results.getTotalScore());
        score.setText(String.valueOf(average));
        if (average >= 0 && average < 4) {
            score.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (average >=4 && average < 8) {
            score.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
        } else if (average >= 8 && average <= 10) {
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
