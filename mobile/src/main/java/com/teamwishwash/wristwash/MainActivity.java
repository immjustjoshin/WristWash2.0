package com.teamwishwash.wristwash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String HAND_WASHING_TECHNIQUE = "hand washing technique";
    public static final String HAND_WASH_SCORE = "hand wash score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declaring screen layouts
        TextView totalScore = (TextView) findViewById(R.id.totalScoreTextView);
        TextView score = (TextView) findViewById(R.id.scoreTextView);
        ListView detailList = (ListView) findViewById(R.id.detailsListView);
        List<HandWashTechnique> handWashTechniqueList = new ArrayList<>();

        // data for list of hand washing techniques
        // we can get data from smartwatch here. Below are just random data values.
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", 9.48));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", 4.63));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", 7.21));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", 2.35));

        // sets final total score using data above. Below is just an example value based on data above.
        double average = (9.48 + 4.63 + 7.21 + 2.35) / 4;
        score.setText(String.valueOf(average));

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
}
