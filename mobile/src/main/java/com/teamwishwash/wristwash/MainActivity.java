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
        // HandWashingListAdapter detailScoresAdapter = new HandWashingListAdapter(this, R.layout.hand_wash_techniques_list, handWashingTechniques);
        ListAdapter detailListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, handWashingTechniques);
        detailList.setAdapter(detailListAdapter);

        // goes to more hand washing details on click
        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String handWash = String.valueOf(adapterView.getItemAtPosition(i));
                showDetails(handWash);
            }
        });
    }

    public void showDetails(String handWash) {
        Intent intent = new Intent(this, DetailedScores.class);
        intent.putExtra(HAND_WASHING_TECHNIQUE, handWash);
        startActivity(intent);
    }
}
