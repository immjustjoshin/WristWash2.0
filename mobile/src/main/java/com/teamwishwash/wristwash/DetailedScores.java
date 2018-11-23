package com.teamwishwash.wristwash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedScores extends AppCompatActivity {

    // layouts
    TextView handWashingTechnique, handWashScore, description;
    ImageView gestureImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_scores);

        // Add back button in navigation bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // gets name of hand washing technique and score that was clicked on
        Intent intent = getIntent();
        String techniqueTitle = intent.getStringExtra(Constants.VALUES.HAND_WASHING_TECHNIQUE);
        double scoreValue = intent.getDoubleExtra(Constants.VALUES.HAND_WASH_SCORE, 0.0);

        // initializes TextView layouts
        handWashingTechnique = (TextView) findViewById(R.id.handWashTechniqueTextView);
        handWashScore = (TextView) findViewById(R.id.detailScoreTextView);
        description = (TextView) findViewById(R.id.descriptionTextView);
        gestureImage = (ImageView) findViewById(R.id.detailGestureImageView);

        // Sets name of hand washing technique selected
        handWashingTechnique.setText(techniqueTitle);

        // Assigns appropriate description & image for hand washing technique selected
        switch (techniqueTitle) {
            case "Rubbing Palms":
                description.setText(Constants.DESCRIPTION.rubbingText);
                gestureImage.setImageResource(R.drawable.gesture1);
                break;
            case "Rubbing Back of Hands":
                description.setText(Constants.DESCRIPTION.backHandText);
                gestureImage.setImageResource(R.drawable.gesture2);
                break;
            case "Rubbing Between Fingers":
                description.setText(Constants.DESCRIPTION.betweenFingerText);
                gestureImage.setImageResource(R.drawable.gesture4);
                break;
            case "Rubbing Under Nails":
                description.setText(Constants.DESCRIPTION.underNailsText);
                gestureImage.setImageResource(R.drawable.gesture5);
                break;
            default:
                String message = "Whoops! Something went wrong in the code!";
                description.setText(message);
                gestureImage.setVisibility(View.GONE);
        }
//        if (techniqueTitle.equals("Rubbing Palms")) {
//            description.setText(Constants.DESCRIPTION.rubbingText);
//            gestureImage.setImageResource(R.drawable.gesture_1_image);
//        } else if (techniqueTitle.equals("Rubbing Back of Hands")) {
//            description.setText(Constants.DESCRIPTION.backHandText);
//            gestureImage.setImageResource(R.drawable.gesture_2_image);
//        } else if (techniqueTitle.equals("Rubbing Between Fingers")) {
//            description.setText(Constants.DESCRIPTION.betweenFingerText);
//            gestureImage.setImageResource(R.drawable.gesture_4_image);
//        } else if (techniqueTitle.equals("Rubbing Under Nails")) {
//            description.setText(Constants.DESCRIPTION.underNailsText);
//            gestureImage.setImageResource(R.drawable.gesture_image_5);
//        } else {
//            String message = "Whoops! Something went wrong in the code!";
//            description.setText(message);
//            gestureImage.setVisibility(View.GONE);
//        }

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
