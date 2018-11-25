package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 10/6/2018.
 */

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;

import com.google.android.gms.wearable.WearableListenerService;
import com.teamwishwash.shared.SharedConstants;

/**
 * The Listener Service is responsible for handling messages received from the handheld device.
 * Currently, this includes only commands to start and stop the sensor service, but it could
 * also include commands to change the sampling rate, or provide some sort of notification on
 * the wearable device.
 */
public class ListenerService extends WearableListenerService{
    /** used for debugging purposes */
    private static final String TAG = ListenerService.class.getName();

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(SharedConstants.COMMANDS.START_SENSOR_SERVICE)) {
            Intent startServiceIntent = new Intent(this, SensorService.class);
            startServiceIntent.setAction(Constants.ACTION.START_SERVICE);
            startService(startServiceIntent);
        }

        if (messageEvent.getPath().equals(SharedConstants.COMMANDS.STOP_SENSOR_SERVICE)) {
            Intent stopServiceIntent = new Intent(this, SensorService.class);
            stopServiceIntent.setAction(Constants.ACTION.STOP_SERVICE);
            startService(stopServiceIntent);

            //note: we call startService() instead of stopService() and pass in an intent with the stop service action,
            //so that the service can unregister the sensors and do anything else it needs to do and then call stopSelf()
        }

        if (messageEvent.getPath().equals(SharedConstants.COMMANDS.SHOW_SCORE)) {
            Intent showScoreIntent = new Intent(this, MainActivity.class);
            showScoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(showScoreIntent);
        }
    }
}
