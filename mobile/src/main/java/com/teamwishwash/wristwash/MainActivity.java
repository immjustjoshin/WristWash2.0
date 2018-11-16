package com.teamwishwash.wristwash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MainActivity extends AppCompatActivity {

    /** used for debugging purposes */
    private static final String TAG = MainActivity.class.getName();

    /** The sensor manager which handles sensors on the wearable device remotely */
    private RemoteSensorManager remoteSensorManager;

    /** The url for which our server is placed at */
    private String BASE_URL = "http://78769de7.ngrok.io";

    /** post request parameter to add after BASE_URL*/
    private String POST_CALL = "/post";

    /** Number for the files*/
    public static int gestureNumber = 1;

    // layouts
    TextView countDownTimer, instructions;
    Button startButton, cancelButton;

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

        // declaring screen layouts
        startButton = (Button) findViewById(R.id.startButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        countDownTimer = (TextView) findViewById(R.id.countDownTextView);
        instructions = (TextView) findViewById(R.id.instructionsTextView);

        //start listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                instructions.setVisibility(View.VISIBLE);

                gestureNumber = 1;
                Toast.makeText(getApplicationContext(), "Starting Session!", Toast.LENGTH_LONG).show();
                startStopPrepTimer();
            }
        });

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
                Toast.makeText(getApplicationContext(), "Cancelling Session", Toast.LENGTH_SHORT).show();
                FileUtil.deleteData();
            }
        });

        verifyPermissions();
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
        }, 400);
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
            public void run() { // log at write, log at timer
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
            }
        }, 1500);
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
            startButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Finalizing Score...", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getResults();
                }
            }, 100);
        } else if (gestureNumber == 0) {
            // This if statement is when the user cancels hand washing session so it should do nothing
        } else {
            // Stops recording sensor data here
            stopDataCollection();
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
        Log.e("IM HERE: ", "I should be launching and getting the scores!");
        final Gson gson = new Gson();
        Results res = new Results();
        res.setListOfAccelFiles(list);
        final Intent intent = new Intent(this, Scores.class);
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
                    Log.e("Response: ", response.toString());
                    Results newResults = gson.fromJson(response.toString(), Results.class);
                    intent.putExtra(Scores.FINAL_SCORES, newResults.getScores());
                    startActivity(intent);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
