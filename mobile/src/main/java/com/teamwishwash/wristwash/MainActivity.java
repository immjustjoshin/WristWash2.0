package com.teamwishwash.wristwash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** used for debugging purposes */
    private static final String TAG = MainActivity.class.getName();

    /** The sensor manager which handles sensors on the wearable device remotely */
    private RemoteSensorManager remoteSensorManager;

    public static final String HAND_WASHING_TECHNIQUE = "hand washing technique";
    public static final String HAND_WASH_SCORE = "hand wash score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteSensorManager = RemoteSensorManager.getInstance(this);

        // declaring screen layouts
        TextView totalScore = (TextView) findViewById(R.id.totalScoreTextView);
        TextView score = (TextView) findViewById(R.id.scoreTextView);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        ListView detailList = (ListView) findViewById(R.id.detailsListView);
        List<HandWashTechnique> handWashTechniqueList = new ArrayList<>();

        //start listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, DataWriterService.class);
                startIntent.setAction(Constants.ACTION.START_FOREGROUND);
                startService(startIntent);
                Toast.makeText(getApplicationContext(), "Collecting Data!", Toast.LENGTH_SHORT).show();
                remoteSensorManager.startSensorService();
            }
        });

        //stop listener
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, DataWriterService.class);
                stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND);
                startService(stopIntent);
                Toast.makeText(getApplicationContext(), "Stopping Data Collection", Toast.LENGTH_SHORT).show();
                remoteSensorManager.stopSensorService();
            }
        });

        //delete listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure you want to delete the gesture data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (FileUtil.deleteData()) {
                                    Toast.makeText(getApplicationContext(), "Data successfully deleted.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error: Directory may not have been deleted!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id){} //do nothing
                        })
                        .show();            }
        });

        // data for list of hand washing techniques. We can get data from smartwatch here. Below are just random data values.
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Palms", 9.48));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Back of Hands", 4.63));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Between Fingers", 7.21));
        handWashTechniqueList.add(new HandWashTechnique("Rubbing Under Nails", 2.35));

        // sets final total score & color using data above. Below is just an example value based on data above.
        double average = (9.48 + 4.63 + 7.21 + 2.35) / 4;
        score.setText(String.valueOf(average));
        if (average >= 0 && average < 4) {
            score.setTextColor(getResources().getColor(R.color.red));
        } else if (average >=4 && average < 8) {
            score.setTextColor(getResources().getColor(R.color.yellow_orange));
        } else if (average >= 8 && average <= 10) {
            score.setTextColor(getResources().getColor(R.color.green));
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
}
