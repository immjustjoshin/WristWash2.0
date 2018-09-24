package com.teamwishwash.wristwash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String HAND_WASHING_TECHNIQUE = "hand washing technique";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declaring screen layouts
        TextView totalScore = (TextView) findViewById(R.id.totalScoreTextView);
        ListView detailList = (ListView) findViewById(R.id.detailsListView);

        // data for list of hand washing techniques
        String[] handWashingTechniques = {"Rubbing Palms", "Rubbing Back of Hands", "Rubbing Between Fingers", "Rubbing Under Nails"};
        ListAdapter detailListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, handWashingTechniques);
        detailList.setAdapter(detailListAdapter);

        // will be used later if we want to add scores on the side of each hand washing technique
        // HandWashingListAdapter detailScoresAdapter = new HandWashingListAdapter(this, R.layout.hand_wash_techniques_list, handWashingTechniques);

        // goes to more hand washing details on click
        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String handWash = String.valueOf(adapterView.getItemAtPosition(i));
                showDetails(handWash);
            }
        });
    }

    /**
     * Shows the details of a specific hand washing technique.
     * Goes to the DetailedScores screen. The specific hand
     * washing technique that user clicks on for more details
     * is in the list of the main screen.
     *
     * @param handWash name of the hand washing technique that user pressed on
     */
    public void showDetails(String handWash) {
        Intent intent = new Intent(this, DetailedScores.class);
        intent.putExtra(HAND_WASHING_TECHNIQUE, handWash);
        startActivity(intent);
    }
}
