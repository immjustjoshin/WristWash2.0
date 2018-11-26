package com.teamwishwash.wristwash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    TextView mTextView, scoreTextView;
    double finalscore;

    /** used to receive messages from other components of the handheld app through intents, i.e. receive labels **/
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(Constants.ACTION.SEND_SCORE)) {
                    double score = intent.getDoubleExtra("SCORE", -1.0);
                    finalscore = score;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);

        Intent intent = getIntent();
        final String test = intent.getStringExtra("SCORE");

        double score = -1.0;
        if (test != null) {
            score = Double.valueOf(test);
        }

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
