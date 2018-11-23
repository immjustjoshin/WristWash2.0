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
        String HAND_WASHING_TECHNIQUE = "hand washing technique";
        String HAND_WASH_SCORE = "hand wash score";
        String FINAL_SCORES = "final scores";

    }

    public interface DESCRIPTION {
        String rubbingText = "If you want to improve your score, try:\n"
                + "- Rubbing your hands together more firmly\n"
                + "- Rubbing the entire palm of your hands\n\n"
                + "Additionally: \n"
                + "- Rubbing your hands so that your palms are perpendicular to the ground";

        String backHandText = "If you want to improve your score, try:\n"
                + "- Washing the whole back of your hand\n\n"
                + "Additionally: \n"
                + "- Washing the back of your left hand first\n"
                + "- Limiting the movement of the hand being washed\n"
                + "- Not to hit the watch while washing rubbing your left hand";

        String betweenFingerText = "If you want to improve your score, try:\n"
                + "- Rubbing the full length of your fingers\n\n"
                + "Additionally: \n"
                + "- Moving both your hands while rubbing between the fingers";

        String underNailsText = "If you want to improve your score, try:\n"
                + "- Rubbing under your nails consistently even if it tickles\n\n"
                + "Additionally: \n"
                + "- Washing under the nails of your right hand first\n"
                + "- Limiting the movement of the hand being washed\n"
                + "- Washing your hands so that your palms are perpendicular to the ground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
