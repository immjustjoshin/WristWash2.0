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

        // gets name of hand washing technique that was clicked on
        Intent intent = getIntent();
        String techniqueTitle = intent.getStringExtra(MainActivity.HAND_WASHING_TECHNIQUE);
        double scoreValue = intent.getDoubleExtra(MainActivity.HAND_WASH_SCORE, 0.0);

        // displays hand washing technique name
        TextView handWashingTechnique = (TextView) findViewById(R.id.handWashTechniqueTextView);
        TextView handWashScore = (TextView) findViewById(R.id.detailScoreTextView);

        handWashingTechnique.setText(techniqueTitle);
        handWashScore.setText(String.valueOf(scoreValue));
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
