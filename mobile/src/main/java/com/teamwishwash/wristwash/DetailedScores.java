package com.teamwishwash.wristwash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_scores);

        // Add back button in navigation bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // gets name of hand washing technique and score that was clicked on
        Intent intent = getIntent();
        String techniqueTitle = intent.getStringExtra(MainActivity.HAND_WASHING_TECHNIQUE);
        double scoreValue = intent.getDoubleExtra(MainActivity.HAND_WASH_SCORE, 0.0);

        // intializes TextView layouts
        TextView handWashingTechnique = (TextView) findViewById(R.id.handWashTechniqueTextView);
        TextView handWashScore = (TextView) findViewById(R.id.detailScoreTextView);
        TextView description = (TextView) findViewById(R.id.descriptionTextView);

        //sets text & color
        handWashingTechnique.setText(techniqueTitle);
        handWashScore.setText(String.valueOf(scoreValue));
        if (scoreValue >= 0 && scoreValue < 4) {
            handWashScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
            description.setText(getString(R.string.badScoreDescription));
        } else if (scoreValue >=4 && scoreValue < 8) {
            handWashScore.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
            description.setText(getString(R.string.averageDescription));
        } else if (scoreValue >= 8 && scoreValue <= 10) {
            handWashScore.setTextColor(getResources().getColor(R.color.green, getTheme()));
            description.setText(getString(R.string.goodScoreDescription));
        }
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
