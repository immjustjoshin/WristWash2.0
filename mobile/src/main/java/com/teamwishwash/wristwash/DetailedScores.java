package com.teamwishwash.wristwash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedScores extends AppCompatActivity {

    private final String rubbingText = "If you want to improve your score, try:\n"
            + "- Rubbing your hands together more firmly\n"
            + "- Rubbing the entire palm of your hands\n\n"
            + "Additionally: \n"
            + "- Rubbing your hands so that your palms are perpendicular to the ground";

    private final String backHandText = "If you want to improve your score, try:\n"
            + "- Washing the whole back of your hand\n\n"
            + "Additionally: \n"
            + "- Washing the back of your left hand first\n"
            + "- Limiting the movement of the hand being washed\n"
            + "- Not to hit the watch while washing rubbing your left hand";

    private final String betweenFingerText = "If you want to improve your score, try:\n"
            + "- Rubbing the full length of your fingers\n\n"
            + "Additionally: \n"
            + "- Moving both your hands while rubbing between the fingers";

    private final String underNailsText = "If you want to improve your score, try:\n"
            + "- Rubbing under your nails consistently even if it tickles\n\n"
            + "Additionally: \n"
            + "- Washing under the nails of your right hand first\n"
            + "- Limiting the movement of the hand being washed\n"
            + "- Washing your hands so that your palms are perpendicular to the ground";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_scores);

        // Add back button in navigation bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // gets name of hand washing technique and score that was clicked on
        Intent intent = getIntent();
        String techniqueTitle = intent.getStringExtra(Scores.HAND_WASHING_TECHNIQUE);
        double scoreValue = intent.getDoubleExtra(Scores.HAND_WASH_SCORE, 0.0);

        // initializes TextView layouts
        TextView handWashingTechnique = (TextView) findViewById(R.id.handWashTechniqueTextView);
        TextView handWashScore = (TextView) findViewById(R.id.detailScoreTextView);
        TextView description = (TextView) findViewById(R.id.descriptionTextView);

        handWashingTechnique.setText(techniqueTitle);
        if (techniqueTitle.equals("Rubbing Palms")) {
            Log.e("BEFORE I SET THE TEXT1", "");
            description.setText(rubbingText);
            Log.e("AFTER I SET THE TEXT1", "");
        } else if (techniqueTitle.equals("Rubbing Back of Hands")) {
            Log.e("BEFORE I SET THE TEXT2", "");
            description.setText(backHandText);
            Log.e("AFTER I SET THE TEXT2", "");
        } else if (techniqueTitle.equals("Rubbing Between Fingers")) {
            Log.e("BEFORE I SET THE TEXT3", "");
            description.setText(betweenFingerText);
            Log.e("AFTER I SET THE TEXT3", "");
        } else if (techniqueTitle.equals("Rubbing Under Nails")) {
            Log.e("BEFORE I SET THE TEXT4", "");
            description.setText(underNailsText);
            Log.e("AFTER I SET THE TEXT4", "");
        } else {
            String message = "Whoops! Something went wrong in the code!";
            description.setText(message);
        }

        //sets color of score
        handWashScore.setText(String.valueOf(scoreValue));
        if (scoreValue >= 0 && scoreValue < 4) {
            handWashScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (scoreValue >=4 && scoreValue < 8) {
            handWashScore.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
        } else if (scoreValue >= 8 && scoreValue <= 10) {
            handWashScore.setTextColor(getResources().getColor(R.color.green, getTheme()));
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
