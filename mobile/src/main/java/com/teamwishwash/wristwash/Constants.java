package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 10/6/2018.
 */

/**
 * Constants used for communication between components of the handheld application. For example,
 * the Data Writer Service can be sure it is receiving the data from the Data Receiver Service.
 *
 * @see DataWriterService
 * @see DataReceiverService
 */
public class Constants {
    public interface ACTION {
        String NAVIGATE_TO_APP = "com.teamwishwash.wristwash.action.navigate-to-app";
        String RECORD_LABEL = "com.teamwishwash.wristwash.action.record-label";
        String START_FOREGROUND = "com.teamwishwash.wristwash.action.start.foreground";
        String STOP_FOREGROUND = "com.teamwishwash.wristwash.action.stop-foreground";

        String SEND_ACCELEROMETER = "com.teamwishwash.wristwash.action.send-accelerometer";
        String SEND_GYROSCOPE = "com.teamwishwash.wristwash.action.send-gyroscope";
        String SEND_LABEL = "com.teamwishwash.wristwash.action.send-label";
    }

    public interface VALUES {
        String SENSOR_DATA = "com.teamwishwash.wristwash.values.sensor-data";
        String LABEL = "com.teamwishwash.wristwash.values.label";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
