package com.teamwishwash.wristwash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /** used for debugging purposes */
    private static final String TAG = MainActivity.class.getName();

    /** The sensor manager which handles sensors on the wearable device remotely */
    private RemoteSensorManager remoteSensorManager;

    public static int gestureNumber = 1;

    // layouts
    TextView countDownTimer, instructions;
    Button startButton, cancelButton, requestButton;

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


        requestButton = (Button) findViewById(R.id.requestButton);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scoresIntent = new Intent(MainActivity.this, Scores.class);
                startActivity(scoresIntent);
//                FileUtil.segmentMotionData();
            }
        });

//        requestButton.setOnClickListener(new View.OnClickListener() {
//            String result = "";
//            @Override
//            public void onClick(View view) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            HTTPRequests.getScore(2, 2);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                thread.start();
//            }
//        });


        //start listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                instructions.setVisibility(View.VISIBLE);

                gestureNumber = 1;
                startStopPrepTimer();

//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        gestureNumber = 1;
//                        startStopPrepTimer();
//                    }
//                },500);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestureNumber = -1;
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
            startMainTimer();
        }
    }
    public void startMainTimer() {
        Intent startIntent = new Intent(MainActivity.this, DataWriterService.class);
        startIntent.setAction(Constants.ACTION.START_FOREGROUND);
        startService(startIntent);
        remoteSensorManager.startSensorService();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainTimerRunning = true;
                mainTimer = new CountDownTimer(mainTimeLeftInMilliSeconds, 1000) {
                    @Override
                    public void onTick(long l) {
                        mainTimeLeftInMilliSeconds = l;
                        updateMainTimer();
                    }
                    @Override
                    public void onFinish() {}
                }.start();
            }
        }, 600);
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
            gestureNumber = 1;
            instructions.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Finalizing Score...", Toast.LENGTH_SHORT).show();

            // Do a http calls here and then do intent call to scores.class and then make the call
            Intent scoresIntent = new Intent(MainActivity.this, Scores.class);
            startActivity(scoresIntent);
        } else if (gestureNumber == 0) {
            // this if statement if when the user cancels hand washing session so it should do nothing
        } else {
            Intent stopIntent = new Intent(MainActivity.this, DataWriterService.class);
            stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND);
            startService(stopIntent);
            remoteSensorManager.stopSensorService();
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
}
