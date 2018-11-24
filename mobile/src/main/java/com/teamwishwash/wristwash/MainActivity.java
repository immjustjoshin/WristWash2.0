package com.teamwishwash.wristwash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.teamwishwash.shared.SharedConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MainActivity extends AppCompatActivity {

    /** used for debugging purposes */
    private static final String TAG = MainActivity.class.getName();

    /** The sensor manager which handles sensors on the wearable device remotely */
    private RemoteSensorManager remoteSensorManager;

    /** The url for which our server is placed at */
    private String BASE_URL = "http://360341b5.ngrok.io";

    /** post request parameter to add after BASE_URL*/
    private String POST_CALL = "/post";

    /** Number for the files*/
    public static int gestureNumber = 1;

    // layouts
    TextView countDownTimer, instructions;
    Button startButton, cancelButton, recentScoreButton;
    ImageView gestureImage;

    // Timer variables
    CountDownTimer mainTimer, prepTimer;
    long mainTimeLeftInMilliSeconds = 10200;     // about 10 seconds
    long prepTimeLeftInMilliSeconds = 3200;     // about 3 seconds
    boolean mainTimerRunning = false;
    boolean prepTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteSensorManager = RemoteSensorManager.getInstance(this);

        // SharedPreferences SetUp
        final SharedPreferences pref = getSharedPreferences(SharedConstants.VALUES.SCORE_KEY, MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        // declaring screen layouts
        startButton = (Button) findViewById(R.id.startButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        recentScoreButton = (Button) findViewById(R.id.recentScoreButton);
        countDownTimer = (TextView) findViewById(R.id.countDownTextView);
        instructions = (TextView) findViewById(R.id.instructionsTextView);
        gestureImage = (ImageView) findViewById(R.id.gestureImageView);

        // Start listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                recentScoreButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                instructions.setVisibility(View.VISIBLE);
                gestureImage.setVisibility(View.VISIBLE);

                // Determines whether it is running a session or just getting recent scores
                editor.putBoolean("Session", true);
                editor.apply();

                gestureNumber = 1;
                Toast.makeText(getApplicationContext(), "Starting Session!", Toast.LENGTH_LONG).show();
                startStopPrepTimer();
            }
        });

        // Cancel listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestureNumber = -1;
                stopDataCollection();
                stopPrepTimer();
                stopMainTimer();
                startButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                instructions.setVisibility(View.GONE);
                recentScoreButton.setVisibility(View.GONE);
                gestureImage.setVisibility(View.GONE);
                gestureImage.setImageResource(R.drawable.gesture1);
                Toast.makeText(getApplicationContext(), "Cancelling Session", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.deleteData();
                    }
                });
            }
        });

        // Get Score listener
        recentScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Determines whether it is running a session or just getting recent scores
                editor.putBoolean("Session", false);
                editor.apply();

                // Gets recent scores that was saved
                double[] scores = new double[5];
                scores[0] = pref.getFloat("Palms", 0);
                scores[1] = pref.getFloat("Back", 0);
                scores[2] = pref.getFloat("Fingers", 0);
                scores[3] = pref.getFloat("Nails", 0);
                scores[4] = pref.getFloat("Total", 0);
                Intent scoresIntent = new Intent(getApplicationContext(), Scores.class);
                scoresIntent.putExtra(Constants.VALUES.RECENT_SCORES, scores);
                startActivity(scoresIntent);
            }
        });

        verifyPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasMotionDataFiles()) {
            recentScoreButton.setVisibility(View.VISIBLE);
        } else {
            recentScoreButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.helpItem:
                //goes to another screen that describes how to wash your hands
                Intent tutorialIntent = new Intent(this, Tutorial.class);
                startActivity(tutorialIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void startStopPrepTimer() {
        if (prepTimerRunning) {
            stopPrepTimer();
        } else {
            startPrepTimer();
        }
    }

    public void startPrepTimer() {
        String text = "Next gesture in: 3s";
        instructions.setText(text);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepTimerRunning = true;
                prepTimer = new CountDownTimer(prepTimeLeftInMilliSeconds, 1000) {
                    @Override
                    public void onTick(long l) {
                        prepTimeLeftInMilliSeconds = l;
                        updatePrepTimer();
                    }
                    @Override
                    public void onFinish() {}
                }.start();
            }
        }, 700);
    }

    public void updatePrepTimer() {
        int seconds = (int) prepTimeLeftInMilliSeconds / 1000;
        String timeLeft;
        if (seconds <= 0) {
            stopPrepTimer();
            startStopMainTimer();
            timeLeft = "START WASHING!";
            instructions.setText(timeLeft);
        } else {
            timeLeft = "Next gesture in: " + seconds + "s";
            instructions.setText(timeLeft);
        }
    }

    public void stopPrepTimer() {
        prepTimeLeftInMilliSeconds = 3200;
        prepTimerRunning = false;
        String text = "Next gesture in: 3s";
        instructions.setText(text);
        if (prepTimer != null) {
            prepTimer.cancel();
        }
    }

    public void startStopMainTimer() {
        if (mainTimerRunning) {
            stopMainTimer();
        } else {
            startDataCollection();
            startMainTimer();
        }
    }
    public void startMainTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainTimerRunning = true;
                mainTimer = new CountDownTimer(mainTimeLeftInMilliSeconds, 1000) {
                    @Override
                    public void onTick(long l) {
                        Log.d("TIMER", ": " + l);
                        mainTimeLeftInMilliSeconds = l;
                        updateMainTimer();
                    }
                    @Override
                    public void onFinish() {}
                }.start();
            }   // Adds a 1.6 sec delay to give time for the motion sensors to begin collecting data
        }, 1600);
    }

    public void updateMainTimer() {
        int seconds = (int) mainTimeLeftInMilliSeconds / 1000;
        String timeLeft;
        if (seconds <= 0) {
            stopMainTimer();
        } else {
            timeLeft = seconds + "s";
            countDownTimer.setText(timeLeft);
        }
    }

    public void stopMainTimer() {
        mainTimeLeftInMilliSeconds = 10200;
        String timeLeft = "10s";
        countDownTimer.setText(timeLeft);
        mainTimerRunning = false;
        gestureNumber++;

        if (mainTimer != null) {
            mainTimer.cancel();
        }

        if (gestureNumber > 6) {
            stopDataCollection();
            gestureNumber = 1;
            instructions.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            gestureImage.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
            gestureImage.setImageResource(R.drawable.gesture1);
            Toast.makeText(getApplicationContext(), "Finalizing Score...", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getResults();
                }
            }, 50);
        } else if (gestureNumber == 0) {
            // This if statement is when the user cancels hand washing session so it should do nothing
        } else {
            stopDataCollection();

            // Sets the different images for each hand washing gesture during the session
            switch (gestureNumber) {
                case 2:
                    gestureImage.setImageResource(R.drawable.gesture2);
                    break;
                case 3:
                    gestureImage.setImageResource(R.drawable.gesture3);
                    break;
                case 4:
                    gestureImage.setImageResource(R.drawable.gesture4);
                    break;
                case 5:
                    gestureImage.setImageResource(R.drawable.gesture5);
                    break;
                case 6:
                    gestureImage.setImageResource(R.drawable.gesture6);
                    break;
                default:
                    gestureImage.setImageResource(R.drawable.gesture1);
            }
            startStopPrepTimer();
        }
    }


    /**
     * Asks the user for storage permissions
     */
    public void verifyPermissions() {
        FileUtil.verifyStoragePermissions(MainActivity.this);
    }

    /**
     * Returns file number for accelerometer data
     * @return file number
     */
    public static int getFileNumber() {
        return gestureNumber;
    }

    public void startDataCollection() {
        Intent startIntent = new Intent(MainActivity.this, DataWriterService.class);
        startIntent.setAction(Constants.ACTION.START_FOREGROUND);
        startService(startIntent);
        remoteSensorManager.startSensorService();
    }

    public void stopDataCollection() {
        Intent stopIntent = new Intent(MainActivity.this, DataWriterService.class);
        stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND);
        startService(stopIntent);
        remoteSensorManager.stopSensorService();
    }

    /**
     * This method gets the results of the hand washing session.
     * First it extracts the motion data from each csv file
     * and then sends each file to the server for pre-processing
     * using a HTTP call request. After doing all this, a score
     * will be returned for the specific hand washing gesture.
     */
    public void getResults() {
        ArrayList<ArrayList<ArrayList<Double>>> list = FileUtil.extractMotionData();
        final Gson gson = new Gson();
        Results res = new Results();
        res.setListOfAccelFiles(list);
        final Intent scoresIntent = new Intent(this, Scores.class);
        try {
            JSONObject json = new JSONObject(gson.toJson(res));
            AsyncHttpClient client = new AsyncHttpClient();
            ByteArrayEntity entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
            client.post(this, BASE_URL + POST_CALL, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Results newResults = gson.fromJson(response.toString(), Results.class);
                    scoresIntent.putExtra(Constants.VALUES.FINAL_SCORES, newResults.getScores());
                    startActivity(scoresIntent);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean hasMotionDataFiles() {
        // Checks whether there are 6 files in motion-data directory to even show most recent score
        File motionDataDirectory = FileUtil.getMotionDataFile();
        if (motionDataDirectory.listFiles() != null) {
            return motionDataDirectory.listFiles().length == 6;
        }
        return false;
    }
}
