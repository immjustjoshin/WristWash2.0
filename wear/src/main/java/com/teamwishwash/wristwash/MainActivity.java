package com.teamwishwash.wristwash;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.teamwishwash.shared.SharedConstants;

public class MainActivity extends WearableActivity {

    TextView mTextView, scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);

        // SharedPreferences Set Up
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SharedConstants.VALUES.SCORE_KEY, MODE_PRIVATE);

        // Gets total score from Shared Preferences
        double score = (double) pref.getFloat("Total", -1);

        if (score >= 0.0) {
            scoreTextView.setText(String.valueOf(score));
        } else {
            String blankScore = "__.__";
            scoreTextView.setText(blankScore);
        }

        // Sets color of score based on performance
        if (score > 0 && score < 6) {
            scoreTextView.setTextColor(getResources().getColor(R.color.red, getTheme()));
        } else if (score >=6 && score < 8) {
            scoreTextView.setTextColor(getResources().getColor(R.color.yellow_orange, getTheme()));
        } else if (score >= 8 && score <= 10) {
            scoreTextView.setTextColor(getResources().getColor(R.color.green, getTheme()));
        }

        // Enables Always-on
        setAmbientEnabled();
    }
}
